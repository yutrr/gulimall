package com.xie.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title: GulimallElasticSearchConfig
 * @Author Xie
 * @Date: 2022/9/7 21:03
 * @Version 1.0
 */

/**
 * 1.导入依赖
 * 2.编写配置，给容器中注入一个RestHighLevelClient
 *
 */
@Configuration
public class GulimallElasticSearchConfig {
    /*@Bean
    public RestHighLevelClient esRestClient(){
        RestClientBuilder builder=null;
        builder=RestClient.builder(new HttpHost("192.168.12.132",9200,"http"));
        RestHighLevelClient client=new RestHighLevelClient(builder);
        return client;
    }*/
    //全局通用设置项,单实例singleton,构建授权请求头,异步等信息
    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        builder.addHeader("Authorization","Bearer"+TOKEN);
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30*1024*1024*1024));
        COMMON_OPTIONS = builder.build();
    }
    @Bean
    public RestHighLevelClient esRestClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.12.132", 9200, "http")));
        return client;
    }
}
