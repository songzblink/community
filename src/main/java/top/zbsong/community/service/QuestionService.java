package top.zbsong.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zbsong.community.dto.PaginationDTO;
import top.zbsong.community.dto.QuestionDTO;
import top.zbsong.community.mapper.QuestionMapper;
import top.zbsong.community.mapper.UserMapper;
import top.zbsong.community.model.Question;
import top.zbsong.community.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {

        // page检查
        Integer totalCount = questionMapper.count();
        int totalPage = (totalCount % size == 0 ? totalCount / size : totalCount / size + 1);
        if (page > totalPage) {
            page = totalPage;
        }
        if (page < 1) {
            page = 1;
        }

        // 计算真正的偏移地址
        Integer offset = (page - 1) * size;
        PaginationDTO paginationDTO = new PaginationDTO();




        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        paginationDTO.setPagination(totalCount, page, size);

        return paginationDTO;
    }

    public PaginationDTO listByUserId(Integer userId, Integer page, Integer size) {
        // page检查

        Integer totalCount = questionMapper.countByUserId(userId);
        int totalPage = (totalCount % size == 0 ? totalCount / size : totalCount / size + 1);
        if (page > totalPage) {
            page = totalPage;
        }
        if (page < 1) {
            page = 1;
        }

        // 计算真正的偏移地址
        Integer offset = (page - 1) * size;
        PaginationDTO paginationDTO = new PaginationDTO();




        List<Question> questions = questionMapper.listByUserId(userId, offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        paginationDTO.setPagination(totalCount, page, size);

        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            // 创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.create(question);
        } else {
            // 更新
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);
        }
    }
}
