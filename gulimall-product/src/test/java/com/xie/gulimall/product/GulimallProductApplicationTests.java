package com.xie.gulimall.product;

import com.xie.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * 1.引入oss-starter
 * 2.配置key,endpoint相关信息即可
 * 3.使用OSSClient 进行相关操作
 */
@Slf4j
@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    CategoryService categoryService;

    @Test
    public void testFindPath(){
        Long[] catelogPath = categoryService.findCatelogPath(231L);
        log.info("完整路径：{}", Arrays.asList(catelogPath));
    }

    @Test
    void contextLoads() {
    }
    @Test
    public void testUpload() {
        Integer a=new Integer(2);
        Integer b=new Integer(2);
        if (a==b){
            System.out.println(a==b);
        }

    }

}
