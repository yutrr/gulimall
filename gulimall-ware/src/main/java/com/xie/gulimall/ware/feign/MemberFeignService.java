package com.xie.gulimall.ware.feign;

import com.xie.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-member")
public interface MemberFeignService {
    /**
     * 信息
     */
    @RequestMapping("/member/memberreceiveaddress/info/{id}")
    //@RequiresPermissions("member:memberreceiveaddress:info")
     R addrInfo(@PathVariable("id") Long id);
}
