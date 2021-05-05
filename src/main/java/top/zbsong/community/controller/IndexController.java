package top.zbsong.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.zbsong.community.dto.PaginationDTO;
import top.zbsong.community.service.QuestionService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model, @RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "size", defaultValue = "5") Integer size, @RequestParam(name = "search", required = false) String search) {
        /**/
        // 查询数据库，返回主页的问题列表信息
        PaginationDTO pagination = questionService.list(search, page, size);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        return "index";
    }
}
