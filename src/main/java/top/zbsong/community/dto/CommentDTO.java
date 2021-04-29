package top.zbsong.community.dto;

import lombok.Data;
import top.zbsong.community.model.User;

/**
 * Create By songzb on 2021/4/29
 *
 * @author songzb
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Integer commentCount;
    private User user;
}
