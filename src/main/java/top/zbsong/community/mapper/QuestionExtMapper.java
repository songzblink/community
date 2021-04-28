package top.zbsong.community.mapper;

import top.zbsong.community.model.Question;

public interface QuestionExtMapper {

    int incView(Question record);

    int incCommentCount(Question record);
}