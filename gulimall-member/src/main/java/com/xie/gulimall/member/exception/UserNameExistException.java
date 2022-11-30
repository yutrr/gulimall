package com.xie.gulimall.member.exception;

/**
 * @title: UserNameExistException
 * @Author Xie
 * @Date: 2022/11/23 21:04
 * @Version 1.0
 */
public class UserNameExistException extends RuntimeException{
    public UserNameExistException() {
        super("存在相同的用户名");
    }
}
