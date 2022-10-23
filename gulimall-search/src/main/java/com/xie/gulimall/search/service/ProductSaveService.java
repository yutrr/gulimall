package com.xie.gulimall.search.service;

import com.xie.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

public interface ProductSaveService {
    boolean productSaveUp(List<SkuEsModel> skuEsModels) throws IOException;
}
