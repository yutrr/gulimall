package com.xie.gulimall.auth.feign;

import com.xie.common.utils.R;
import com.xie.gulimall.auth.vo.GiteeSocialUser;
import com.xie.gulimall.auth.vo.UserLoginVo;
import com.xie.gulimall.auth.vo.UserRegistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-member")
public interface MemberFeignService {
    @PostMapping("/member/member/register")
     R register(@RequestBody UserRegistVo vo);

    @PostMapping("/member/member/login")
     R login(@RequestBody UserLoginVo vo);

    @PostMapping(value = "/member/member/oauth2/login")
    R oauthLogin(@RequestBody GiteeSocialUser socialUser) throws Exception;
}
