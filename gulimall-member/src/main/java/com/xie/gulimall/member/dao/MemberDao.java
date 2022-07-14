package com.xie.gulimall.member.dao;

import com.xie.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author xiehaijun
 * @email xiehaijun@gmail.com
 * @date 2022-07-07 18:04:32
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
