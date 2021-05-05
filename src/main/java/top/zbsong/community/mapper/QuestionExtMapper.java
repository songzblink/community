package top.zbsong.community.mapper;

import top.zbsong.community.dto.QuestionQueryDTO;
import top.zbsong.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {

    int incView(Question record);

    int incCommentCount(Question record);

    List<Question> selectRelated(Question question);

    /**
     * 如果search存在则加上search的限制
     * @param questionQueryDTO
     * @return
     */
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    /**
     * 分页返回，如果search存在则加上search的限制
     * @param questionQueryDTO
     * @return
     */
    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}