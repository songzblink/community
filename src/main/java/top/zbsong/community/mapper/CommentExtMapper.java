package top.zbsong.community.mapper;

import top.zbsong.community.model.Comment;


public interface CommentExtMapper {

    int incCommentCount(Comment comment);
}