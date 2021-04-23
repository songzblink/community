package top.zbsong.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbsong.community.provider.GithubProvider;
import top.zbsong.dto.AccessTokenDTO;

/**
 * Create By songzb on 2021/4/23
 *
 * @author songzb
 */
@Controller
public class AuthorizeController {
    @Autowired
    GithubProvider githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id("8f3c188a9891dd464883");
        accessTokenDTO.setClient_secret("8a7b5524f513dc87171f55714dc963c070c70920 ");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:8888/callback");
        accessTokenDTO.setState(state);
        githubProvider.getAccessToken(accessTokenDTO);
        return "index";
    }
}
