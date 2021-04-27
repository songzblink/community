package top.zbsong.community.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.zbsong.community.dto.CommentDTO;
import top.zbsong.community.dto.ResultDTO;
import top.zbsong.community.mapper.CommentMapper;
import top.zbsong.community.model.Comment;
import top.zbsong.community.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
public class CommentController {
    @Autowired
    private CommentMapper commentMapper;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        User usr = (User)request.getSession().getAttribute("usr");
        if (usr == null) {
            return ResultDTO.errorOf(2002,"未登录，无法进行评论，请登录后重试。");
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentor(usr.getId());
        commentMapper.insert(comment);
        HashMap<String, String> map = new HashMap<>();
        map.put("message","成功");
        return map;
    }
}
