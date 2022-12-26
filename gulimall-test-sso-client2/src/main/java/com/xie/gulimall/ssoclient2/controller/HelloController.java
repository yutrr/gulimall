package com.xie.gulimall.ssoclient2.controller;

/**
 * @title: HelloController
 * @Author Xie
 * @Date: 2022/12/7 21:44
 * @Version 1.0
 */

import cn.hutool.core.bean.BeanUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试单点登录
 */
@Controller
public class HelloController {


    /**
     * 无需登录就可访问
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/hello")
    public String hello() {
        return "hello";
    }


    @GetMapping(value = "/boss")
    public String employees(Model model, HttpSession session, @RequestParam(value = "token", required = false) String token) {

        if (!BeanUtil.isEmpty(token)) {
            RestTemplate restTemplate=new RestTemplate();
            ResponseEntity<String> forEntity = restTemplate.getForEntity("http://ssoserver.com:8080/userinfo?token=" + token, String.class);
            String body = forEntity.getBody();

            session.setAttribute("loginUser", body);
        }
        Object loginUser = session.getAttribute("loginUser");

        if (loginUser == null) {

            return "redirect:" + "http://ssoserver.com:8080/login.html"+"?redirect_url=http://client2.com:8082/boss";
        } else {


            List<String> emps = new ArrayList<>();

            emps.add("张三");
            emps.add("李四");

            model.addAttribute("emps", emps);
            return "employees";
        }
    }
}
