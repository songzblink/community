package top.zbsong.community.dto;

import lombok.Data;
import top.zbsong.community.model.User;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    private String outerTitle;
    private Long outerId;
    private Integer type;
    private String typeName;
}
