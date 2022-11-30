package com.xie.gulimall.member.exception;

/**
 * @title: PhoneExistException
 * @Author Xie
 * @Date: 2022/11/23 21:04
 * @Version 1.0
 */
public class PhoneExistException extends RuntimeException{
    public PhoneExistException() {
        super("存在相同的手机号");
    }
}
