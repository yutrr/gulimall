package com.xie.gulimallssoclient1;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootTest
class GulimallTestSsoClient1ApplicationTests {

    @Test
    void contextLoads() {


        LinkedHashMap<String, String> linkedHashMap=new LinkedHashMap<>();
        linkedHashMap.put("a","{'name':'zhangsan','age':'20'}");
        linkedHashMap.put("b","{'name':'lisi','age':'21'}");
        linkedHashMap.put("c","{'name':'wangwu','age':'22'}");
        Map<String, Object> map= new HashMap<>();

        map.put("common", linkedHashMap.toString());

        map.put("private", "zz");
        map.put("acte","{}");

        String str1 = new Gson().toJson(map);
        System.out.println(map);
        System.out.println(str1);

    }

}
