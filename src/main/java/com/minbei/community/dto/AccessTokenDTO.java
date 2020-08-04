package com.minbei.community.dto;

import lombok.Data;

/**
 * Java bean
 * 实体类
 */
@Data
public class AccessTokenDTO {

    /**
     * client_id：Github生成的Api访问接口id
     * client_secret：Github生成的Api访问接口id
     * code：
     * redirect_uri：完成相应后页面返回的地址
     * state：
     */
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;

}
