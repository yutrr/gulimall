package com.xie.gulimall.search;

import com.xie.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Data;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallSearchApplicationTests {
    @Autowired
    RestHighLevelClient client;

    /**
     * 测试存储数据到es
     * 更新也可以
     */
    @Test
    public void indexData() throws IOException {
        IndexRequest request = new IndexRequest("users");//索引名
        request.id("1");//文档id
        User user = new User();
        user.setName("张三");
        user.setAge(18);
        user.setGender("男");
        String jsonString = JSON.toJSONString(user);
        request.source(jsonString, XContentType.JSON);//要保存的内容
        //执行操作
        IndexResponse index = client.index(request, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //提取有用的响应数据
        System.out.println(index);
    }

    @Data
    class User{
        private String name;
        private String gender;
        private int age;
    }

    @Test
   public void contextLoads() {
        System.out.println(client);
    }

}
