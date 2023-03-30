package com.example.seckill.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title: MyRedissonConfig
 * @Author Xie
 * @Date: 2022/10/23 12:00
 * @Version 1.0
 */
@Configuration
public class MyRedissonConfig {
    /**
     * 所有对redisson的使用都是通过RedissonClient对象
     * @return
     */
    @Bean
    public RedissonClient redissonClient(){
        //1.创建配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.12.128:6379");

        //2.根据Config创建出RedissonClient示例
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
