package com.xie.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xie.common.utils.PageUtils;
import com.xie.gulimall.member.entity.MemberEntity;
import com.xie.gulimall.member.exception.PhoneExistException;
import com.xie.gulimall.member.exception.UserNameExistException;
import com.xie.gulimall.member.vo.GiteeSocialUser;
import com.xie.gulimall.member.vo.MemberRegistVo;
import com.xie.gulimall.member.vo.MemberUserLoginVo;

import java.util.Map;

/**
 * 会员
 *
 * @author xiehaijun
 * @email xiehaijun@gmail.com
 * @date 2022-07-07 18:04:32
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 用户注册
     * @param vo
     */
    void register(MemberRegistVo vo);

    /**
     * 判断邮箱是否重复
     * @param phone
     * @return
     */
    void checkPhoneUnique(String phone) throws PhoneExistException;
    /**
     * 判断用户名是否重复
     * @param userName
     * @return
     */
    void checkUserNameUnique(String userName) throws UserNameExistException;

    MemberEntity login(MemberUserLoginVo vo);

    MemberEntity login(GiteeSocialUser socialUser) throws Exception;
}

