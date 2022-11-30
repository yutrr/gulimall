package com.xie.gulimall.member.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @title: MemberRegistVo
 * @Author Xie
 * @Date: 2022/11/23 20:45
 * @Version 1.0
 */
@Data
public class MemberRegistVo {

    private String userName;

    private String password;

    private String phone;
}
