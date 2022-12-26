package com.xie.gulimall.auth.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.TypeReference;
import com.xie.common.constant.AuthServerConstant;
import com.xie.common.exception.BizCodeEnume;
import com.xie.common.utils.R;
import com.xie.gulimall.auth.feign.MemberFeignService;
import com.xie.gulimall.auth.feign.ThirdPartFeignService;
import com.xie.gulimall.auth.vo.UserLoginVo;
import com.xie.gulimall.auth.vo.UserRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.xie.common.vo.MemberResponseVo;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @title: LoginController
 * @Author Xie
 * @Date: 2022/11/17 21:25
 * @Version 1.0
 */
@Controller
public class LoginController {

    @Autowired
    ThirdPartFeignService thirdPartFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MemberFeignService memberFeignService;
    /**
     * 发送一个请求直接跳转到一个页面。
     * SpringMVC viewcontroller：将请求和页面映射过来
     *
     */
    @ResponseBody
    @GetMapping("/sms/sendcode")
    public R sendCode(@RequestParam("phone")String phone){
        //TODO 1.接口防刷



        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!BeanUtil.isEmpty(redisCode)){
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis()-l<60000){
                //60s内不能再发
                return R.error(BizCodeEnume.SMS_CODE_EXECPTION.getCode(),BizCodeEnume.SMS_CODE_EXECPTION.getMsg());
            }
        }
        //2.验证码的再次校验. 存key-phone,value-code  sms:code:19942178172 ->456789
        //String code = UUID.randomUUID().toString().substring(0, 5)+"_"+System.currentTimeMillis();
        int code = (int) ((Math.random() * 9 + 1) * 100000);// 验证码只可以是数字
        String codeNum = String.valueOf(code);
        String redisStorage = codeNum + "_" + System.currentTimeMillis();
        //redis缓存验证码,防止同一个phone在60s内再次发送验证码
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX+phone,redisStorage,10, TimeUnit.MINUTES);
        thirdPartFeignService.sendCode(phone,codeNum);
        return R.ok();
    }

    /**
     * //TODO 重定向携带数据，利用session原理，把数据放在session中。
     * 只要跳到下一个页面取出这个数据以后，session里面的数据就会删掉
     *
     * //TODO 1.分布式下的session问题
     * RedirectAttributes redirectAttributes 模拟重定向携带数据
     * @param vo
     * @param result
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid UserRegistVo vo, BindingResult result,
                           RedirectAttributes redirectAttributes){
        //如果有错误回到注册页面
        if (result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

            redirectAttributes.addFlashAttribute("errors",errors);
            //Request method 'POST' not supported
            //用户注册-》/register[post]---->转发/reg.html(路径映射默认都是get方式访问的。)
            //校验出错，转发到注册页
            return "redirect:http://auth.gulimall.com/reg.html";
        }

        //1.校验验证码
        String code = vo.getCode();

        //获取存入Redis里的验证码
        String s = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if (!BeanUtil.isEmpty(s)) {
            if (code.equals(s.split("_")[0])) {
                //删除验证码；令牌机制
                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX+vo.getCode());
                //验证码通过。 //真正注册。调用远程服务进行注册
                R r = memberFeignService.register(vo);
                if (r.getCode()==0){
                    //成功
                    return "redirect:http://auth.gulimall.com/login.html";
                }else {
                    //失败
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg", r.getData("msg",new TypeReference<String>(){}));
                    redirectAttributes.addFlashAttribute("errors",errors);
                    return "redirect:http://auth.gulimall.com/reg.html";
                }
            }else {
                //验证码错误
                Map<String, String> errors=new HashMap<>();
                errors.put("code","验证码错误");
                redirectAttributes.addFlashAttribute("errors",errors);
                //校验出错，转发到注册页
                return "redirect:http://auth.gulimall.com/reg.html";
            }
        }else {
            // redis中验证码过期
            Map<String, String> errors=new HashMap<>();
            errors.put("code","验证码错误");
            redirectAttributes.addFlashAttribute("errors",errors);
            //校验出错，转发到注册页
            return "redirect:http://auth.gulimall.com/reg.html";
        }
    }

    /**
     * 判断session是否有loginUser，没有就跳转登录页面，有就跳转首页
     */
    @GetMapping("/login.html")
    public String loginPage(HttpSession session){
        //从session先取出来用户的信息，判断用户是否已经登录过了
        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);
        //如果用户没登录那就跳转到登录页面
        if (attribute==null){
            //没登录
            return "login";
        }else{
            return "redirect:http://gulimall.com";
        }

    }

    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes redirectAttributes, HttpSession session){

        //远程登录
        R login = memberFeignService.login(vo);
        if (login.getCode()==0){
            //成功
            MemberResponseVo data = login.getData("data", new TypeReference<MemberResponseVo>() {});
            //放到session中
            session.setAttribute(AuthServerConstant.LOGIN_USER, data);
            return "redirect:http://gulimall.com";
        }else {
            //失败
            Map<String,String> errors = new HashMap<>();
            errors.put("msg",login.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.gulimall.com/login.html";
        }

    }
}
