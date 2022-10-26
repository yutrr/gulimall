package com.xie.gulimall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @title: ThreadConfigProperties
 * @Author Xie
 * @Date: 2022/11/9 22:17
 * @Version 1.0
 */
@ConfigurationProperties(prefix ="gulimall.thread" )
@Component
@Data
public class ThreadConfigProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}
