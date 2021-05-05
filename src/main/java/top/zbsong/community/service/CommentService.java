package top.zbsong.community.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zbsong.community.dto.CommentDTO;
import top.zbsong.community.enums.CommentTypeEnum;
import top.zbsong.community.enums.NotificationTypeEnum;
import top.zbsong.community.enums.NotificationStatusEnum;
import top.zbsong.community.exception.CustomizeErrorCode;
import top.zbsong.community.exception.CustomizeException;
import top.zbsong.community.mapper.*;
import top.zbsong.community.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Create By songzb on 2021/4/28
 *
 * @author songzb
 */
@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Autowired
    CommentExtMapper commentExtMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionExtMapper questionExtMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            // 回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);

            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }


            // 增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);

            // 通知
            // 这里取巧，notification里并没有直接指向回复的具体评论，而是指向问题的id
            // 如果需要找到回复的具体评论的功能时，再改回来。
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT.getType(), question.getId());
        } else {
            // 回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);

            // 通知
            createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION.getType(), question.getId());
        }

    }

    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, int notificationType, Long outerId) {
        // 不应该提醒自己回复了自己
        if (receiver.equals(comment.getCommentator())) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType);
        notification.setOuterId(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.isEmpty()) {
            return new ArrayList<>();
        }
        // 获取去重的评论人
        // map 遍历并返回结果集
        // collect 把结果集变为list对象
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());

        // 获取评论人并转换为 map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(new ArrayList<>(commentators));
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        // 转换 comment 为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
