package com.xie.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @title: CorsConfiguartion
 * @Author Xie
 * @Date: 2022/7/9 15:48
 * @Version 1.0
 */

@Configuration
public class GulimallCorsConfiguration {

    @Bean
    public CorsWebFilter corsWebFilter(){
    UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //1.配置跨域
        corsConfiguration.addAllowedHeader("*");//允许哪些头进行跨域
        corsConfiguration.addAllowedMethod("*");//允许哪些请求方式进行跨域
        corsConfiguration.addAllowedOrigin("*");//允许哪个请求来源进行跨域
        //corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.setAllowCredentials(true);//是否允许携带cook进行跨域
        source.registerCorsConfiguration("/**",corsConfiguration);

        return new CorsWebFilter(source);
    }
}
