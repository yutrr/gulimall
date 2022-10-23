package com.xie.gulimall.product.feign;

import com.xie.common.to.es.SkuEsModel;
import com.xie.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-search")
public interface SearchFeignService {

    @PostMapping("/search/product")
     R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
