package com.xie.gulimall.product.vo;

import com.xie.gulimall.product.entity.SkuImagesEntity;
import com.xie.gulimall.product.entity.SkuInfoEntity;
import com.xie.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @title: SkuItemVo
 * @Author Xie
 * @Date: 2022/11/7 21:03
 * @Version 1.0
 */
@Data
public class SkuItemVo {
    // 1、sku基本信息【标题、副标题、价格】pms_sku_info
   private SkuInfoEntity info;

   private Boolean hasStock=true;

    // 2、sku图片信息【每个sku_id对应了多个图片】pms_sku_images
   private List<SkuImagesEntity> images;

    // 3、spu下所有sku销售属性组合【不只是当前sku_id所指定的商品】
    private List<SkuItemSaleAttrVo> saleAttr;
    // 4、spu商品介绍【】
    private SpuInfoDescEntity desp;

    // 5、spu规格与包装【参数信息】
    private List<SpuItemAttrGroupVo> groupAttrs;
}
