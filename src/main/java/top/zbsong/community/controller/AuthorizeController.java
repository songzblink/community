package top.zbsong.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbsong.community.dto.GithubUser;
import top.zbsong.community.mapper.UserMapper;
import top.zbsong.community.model.User;
import top.zbsong.community.provider.GithubProvider;
import top.zbsong.community.dto.AccessTokenDTO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Create By songzb on 2021/4/23
 *
 * @author songzb
 */
@Controller
public class AuthorizeController {
    @Autowired
    GithubProvider githubProvider;
    @Autowired
    UserMapper userMapper;


    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state, HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        // 1.接入Github认证登陆成功，获取到了用户信息
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null) {
            // 2.在本地登录，为该用户生成一个token
            // 把用户写入数据库
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(System.currentTimeMillis());
            userMapper.insert(user);
            // 3.将token放入cookie
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            // 登陆失败

            return "redirect:/";
        }
    }
}
