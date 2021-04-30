package top.zbsong.community.dto;

import lombok.Data;

import java.util.List;

/**
 * Create By songzb on 2021/4/30
 *
 * @author songzb
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
