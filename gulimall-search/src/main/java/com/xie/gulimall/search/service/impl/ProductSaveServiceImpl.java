package com.xie.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.xie.common.to.es.SkuEsModel;
import com.xie.gulimall.search.config.GulimallElasticSearchConfig;
import com.xie.gulimall.search.constant.EsConstant;
import com.xie.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: ProductSaveServiceImpl
 * @Author Xie
 * @Date: 2022/9/21 21:15
 * @Version 1.0
 */
@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productSaveUp(List<SkuEsModel> skuEsModels) throws IOException {

        //保存到es
        //1.给es中建立索引。product，建立好映射关系。

        //2.给es中保存这些数据
        //BulkRequest bulkRequest, RequestOptions requestOptions
        BulkRequest bulkRequest=new BulkRequest();
        for (SkuEsModel model : skuEsModels) {
            //1.构造保存请求,设置索引+id
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(model.getSkuId().toString());
            String s= JSON.toJSONString(model);
            //绑定请求与数据
           indexRequest.source(s, XContentType.JSON);
           //添加到批量保存
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

        //TODO 如果批量错误
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> item.getId()).collect(Collectors.toList());
        log.info("商品上架完成:{},返回数据:{}",collect,bulk.toString());


        return b;

    }
}
