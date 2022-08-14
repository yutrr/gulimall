package com.xie.gulimall.order;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootTest
class GulimallOrderApplicationTests {

    @Test
    void contextLoads() throws IOException {
        BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
        String readLine = bf.readLine();
        String[] s = readLine.split(" ");
        int length = s[s.length - 1].length();

        System.out.println(length);
    }

}
