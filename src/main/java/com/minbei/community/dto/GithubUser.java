package com.minbei.community.dto;


import lombok.Data;

/**
 * Java bean
 * 实体类
 */
@Data
public class GithubUser {
    /**
     * name：用户名
     * id：用户id（唯一标识）
     * bio：描述
     */
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;

}
