package com.xie.gulimall.auth.vo;

import lombok.Data;

/**
 * @title: GiteeSocialUser
 * @Author Xie
 * @Date: 2022/11/29 23:15
 * @Version 1.0
 */
@Data
public class GiteeSocialUser {
    private String accessToken;

    private String tokenType;

    private Long expiresIn;

    private String refreshToken;

    private String scope;

    private String createdAt;
}
