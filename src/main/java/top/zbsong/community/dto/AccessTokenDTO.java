package top.zbsong.community.dto;

import lombok.Data;

/**
 * Create By songzb on 2021/4/23
 *
 * @author songzb
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
