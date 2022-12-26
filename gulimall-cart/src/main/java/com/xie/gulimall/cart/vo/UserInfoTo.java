package com.xie.gulimall.cart.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @title: UserInfoVo
 * @Author Xie
 * @Date: 2022/12/12 21:59
 * @Version 1.0
 */
@ToString
@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;//一定封装了

    private boolean tempUser = false;
}
