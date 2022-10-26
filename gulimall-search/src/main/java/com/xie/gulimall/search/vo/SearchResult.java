package com.xie.gulimall.search.vo;

import com.xie.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: SearchResponse
 * @Author Xie
 * @Date: 2022/10/26 23:14
 * @Version 1.0
 */
@Data
public class SearchResult {
    //查询到的所有商品信息
    private List<SkuEsModel> product;

    /**
     * 以下是分页信息
     */
    private Integer pageNum;//当前页码
    private Long total;//总记录数
    private Integer totalPages;//总页码
    private List<Integer> pageNavs;
    private List<BrandVo> brands;//当前查询到的结果， 所有涉及到的品牌
    private List<CatalogVo> catalogs;//当前查询到的结果， 所有涉及到的所有分类
    private List<AttrVo> attrs;//当前查询到的结果， 所有涉及到的所有属性
    //==========以上是返回给页面的所有信息============
    //面包屑导航数据
    private List<NavVo> navs = new ArrayList<>();
    private List<Long> attrIds = new ArrayList<>();

    @Data
    public static class NavVo {
        private String navName;
        private String navValue;
        private String link;
    }

    //品牌vo
    @Data
    public static class BrandVo {
        private Long brandId;
        private String brandName;
        private String brandImg;
    }

    //分类vo
    @Data
    public static class CatalogVo{
        private Long catalogId;
        private String catalogName;
    }

    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}
