package com.xie.gulimall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @title: GulimallWebConfig
 * @Author Xie
 * @Date: 2022/11/17 22:08
 * @Version 1.0
 */
@Configuration
public class GulimallWebConfig implements WebMvcConfigurer {

    /**
     * 视图映射
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        /**
         * @GetMapping("/login.html")
         *     public String loginPage();
         *         return "login";
         *     }
         */
        //registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/reg.html").setViewName("reg");
    }
}
