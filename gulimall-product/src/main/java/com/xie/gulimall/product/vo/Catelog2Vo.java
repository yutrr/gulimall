package com.xie.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @title: Catelog2Vo
 * @Author Xie
 * @Date: 2022/10/13 20:17
 * @Version 1.0
 */
//二级分类
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Catelog2Vo {
    private String catalog1Id; //1级父分类id
    private List<Catelog3Vo> catalog3List; //三级子分类
    private String id;
    private String name;


    /**
     * 三级分类
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Catelog3Vo{
        private String catalog2Id;//父分类，2级分类id
        private String id;
        private String name;
    }

}
