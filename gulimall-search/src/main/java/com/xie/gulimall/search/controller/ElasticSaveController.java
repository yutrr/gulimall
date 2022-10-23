package com.xie.gulimall.search.controller;

import com.xie.common.exception.BizCodeEnume;
import com.xie.common.to.es.SkuEsModel;
import com.xie.common.utils.R;
import com.xie.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @title: ElasticSaveController
 * @Author Xie
 * @Date: 2022/9/21 21:10
 * @Version 1.0
 */
@Slf4j
@RequestMapping("/search")
@RestController
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;
    //上架商品
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels){
        boolean b=false;
        try {
            b=productSaveService.productSaveUp(skuEsModels);
        } catch (IOException e) {
            log.error("商品上架错误:{}",e);
            return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());
        }
        if (!b) {
            return R.ok();
        }else {return R.error(BizCodeEnume.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_UP_EXCEPTION.getMsg());}
    }
}
