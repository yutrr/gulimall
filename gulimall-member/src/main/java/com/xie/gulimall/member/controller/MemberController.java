package com.xie.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.xie.common.exception.BizCodeEnume;
import com.xie.gulimall.member.exception.PhoneExistException;
import com.xie.gulimall.member.exception.UserNameExistException;
import com.xie.gulimall.member.feign.CouponFeignService;
import com.xie.gulimall.member.vo.GiteeSocialUser;
import com.xie.gulimall.member.vo.MemberRegistVo;
import com.xie.gulimall.member.vo.MemberUserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xie.gulimall.member.entity.MemberEntity;
import com.xie.gulimall.member.service.MemberService;
import com.xie.common.utils.PageUtils;
import com.xie.common.utils.R;



/**
 * 会员
 *
 * @author xiehaijun
 * @email xiehaijun@gmail.com
 * @date 2022-07-07 18:04:32
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    CouponFeignService couponFeignService;

    @RequestMapping("/coupons")
    public R test(){
        MemberEntity memberEntity=new MemberEntity();
        memberEntity.setNickname("张三");
        R membercoupons = couponFeignService.membercoupons();
        return R.ok().put("member",memberEntity).put("coupons",membercoupons.get("coupons"));
    }

    @PostMapping("/oauth2/login")
    public R oauthLogin(@RequestBody GiteeSocialUser socialUser) throws Exception{
       MemberEntity memberEntity= memberService.login(socialUser);

       if (memberEntity!=null){
           return R.ok().setData(memberEntity);
       }else {
           return R.error(BizCodeEnume.LOGINACCT_PASSWORD_INVALD_EXCEPTION.getCode(),BizCodeEnume.LOGINACCT_PASSWORD_INVALD_EXCEPTION.getMsg());
       }
    }

    @PostMapping("/login")
    public R login(@RequestBody MemberUserLoginVo vo){
        MemberEntity entity=memberService.login(vo);
        if (entity!=null){
            //TODO 1.登陆成功处理
            return R.ok().setData(entity);
        }else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_INVALD_EXCEPTION.getCode(),BizCodeEnume.LOGINACCT_PASSWORD_INVALD_EXCEPTION.getMsg());
        }
    }

    @PostMapping("/register")
    public R register(@RequestBody MemberRegistVo vo){
        try {
            memberService.register(vo);
            //异常机制：通过捕获对应的自定义异常判断出现何种错误并封装错误信息
        }catch (PhoneExistException e) {
            return R.error(BizCodeEnume.PHONE_EXIST_EXCEPTION.getCode(),BizCodeEnume.PHONE_EXIST_EXCEPTION.getMsg());
        } catch (UserNameExistException e) {
            return R.error(BizCodeEnume.USER_EXIST_EXCEPTION.getCode(),BizCodeEnume.USER_EXIST_EXCEPTION.getMsg());
        }
        return R.ok();
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
