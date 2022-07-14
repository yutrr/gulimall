package com.xie.gulimall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 1.整合mybatis-plus
 *   1.导入依赖
 *   2.配置
 *     2.1 导入数据库驱动
 *     2.2 在application.yml配置数据源相关信息
 *   3.配置mybatis-plus
 *     3.1 使用@MapperScan
 *     3.2 告诉mybatis-plus，sql映射文件位置
 * 2.逻辑删除
 *  1），配置全局的逻辑删除规则(省略)
 *  2），配置逻辑删除的组件Bean(省略)
 *  3）、加上逻辑删除注解@TableLogic
 *
 * 3.JSR303
 * 1）给Bean添加校验注解
 */

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.xie.gulimall.product.dao")
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
