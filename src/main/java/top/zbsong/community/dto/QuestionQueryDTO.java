package top.zbsong.community.dto;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private String search;
    private Integer page;
    private Integer size;
}
