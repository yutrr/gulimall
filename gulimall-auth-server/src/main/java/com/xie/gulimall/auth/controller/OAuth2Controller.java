package com.xie.gulimall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xie.common.constant.AuthServerConstant;
import com.xie.common.utils.HttpUtils;
import com.xie.common.utils.R;
import com.xie.gulimall.auth.constant.GiteeConstant;
import com.xie.gulimall.auth.feign.MemberFeignService;
import com.xie.gulimall.auth.vo.GiteeSocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.xie.common.vo.MemberResponseVo;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @title: OAuth2Controller
 * @Author Xie
 * @Date: 2022/11/29 22:56
 * @Version 1.0
 */
@Slf4j
@Controller
public class OAuth2Controller {
    
    @Autowired
    MemberFeignService memberFeignService;

    @GetMapping("/oauth2.0/gitee/success")
    public String gitee(@RequestParam("code")String code, HttpSession session) throws Exception{

        //这几个参数格式是强制性的 可以参考码云的官方api
        Map<String,String> params=new HashMap<>();
        params.put("grant_type","authorization_code");
        params.put("code",code);
        params.put("client_id", GiteeConstant.clientId);
        params.put("redirect_uri",GiteeConstant.callback);
        params.put("client_secret",GiteeConstant.secret);

        //1.根据用户授权返回的code换取access_token
        HttpResponse response = HttpUtils.doPost("https://gitee.com", "/oauth/token",
                "post", new HashMap<>(), params, new HashMap<>());
        //2.处理
        if (response.getStatusLine().getStatusCode()==200){
            //获取到了access_token
            String json = EntityUtils.toString(response.getEntity());//获取到json串
            GiteeSocialUser socialUser = JSON.parseObject(json, GiteeSocialUser.class);

            //知道了哪个社交用户
            //1）、当前用户如果是第一次进网站，自动注册进来（为当前社交用户生成一个会员信息，以后这个社交账号就对应指定的会员）
            //登录或者注册这个社交用户
            System.out.println("登录后用code换取的token值：" + socialUser.getAccessToken());
            //调用远程服务
            R oauthLogin = memberFeignService.oauthLogin(socialUser);
            if (oauthLogin.getCode() == 0) {
                MemberResponseVo data = oauthLogin.getData("data", new TypeReference<MemberResponseVo>() {});
                log.info("登录成功：用户信息：\n{}",data.toString());


                //1、第一次使用session，命令浏览器保存卡号，JSESSIONID这个cookie
                //以后浏览器访问哪个网站就会带上这个网站的cookie
                //子域之间  .gulimall.com  auth.gulimall.com  order.gulimall.com
                //发卡的时候（指定域名为父域名），即使是子域系统发的卡，也能让父域直接使用。
                //TODO 1、默认发的令牌。当前域（解决子域session共享问题）
                //TODO 2、使用JSON的序列化方式来序列化对象到Redis中
                session.setAttribute(AuthServerConstant.LOGIN_USER, data);

                //2、登录成功跳回首页
                return "redirect:http://gulimall.com";
            } else {
                return "redirect:http://auth.gulimall.com/login.html";
            }
        } else {
            return "redirect:http://auth.gulimall.com/login.html";

        }
    }
}
