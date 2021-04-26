package top.zbsong.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbsong.community.dto.PaginationDTO;
import top.zbsong.community.mapper.QuestionMapper;
import top.zbsong.community.mapper.UserMapper;
import top.zbsong.community.model.User;
import top.zbsong.community.service.QuestionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action, HttpServletRequest request, Model model, @RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "size", defaultValue = "5") Integer size) {
        // 1.验证是否登录
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }


        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
        } else if ("replies".equals(action)) {
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }

        // 查询数据库，返回问题列表信息
        PaginationDTO pagination = questionService.listByUserId(user.getId(), page, size);
        model.addAttribute("pagination", pagination);
        return "profile";
    }
}
