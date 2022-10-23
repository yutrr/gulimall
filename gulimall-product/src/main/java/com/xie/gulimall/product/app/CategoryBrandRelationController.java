package com.xie.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xie.gulimall.product.entity.BrandEntity;
import com.xie.gulimall.product.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xie.gulimall.product.entity.CategoryBrandRelationEntity;
import com.xie.gulimall.product.service.CategoryBrandRelationService;
import com.xie.common.utils.PageUtils;
import com.xie.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author xiehaijun
 * @email xiehaijun@gmail.com
 * @date 2022-07-07 17:58:59
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 获取当前品牌关联的所有分类列表
     */
    @GetMapping("/catelog/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R cateloglist(@RequestParam("brandId") Long  brandId){
        List<CategoryBrandRelationEntity> data=categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId));
        return R.ok().put("data", data);
    }

    /**
     * /product/categorybrandrelation/brands/list
     * 1.controller:处理请求，接收和校验数据
     * 2.Service接收controller传来的数据，进行业务处理
     * 3.Controller接收Service处理完的数据，封装页面指定的vo
     */

    //Bug 有问题
    @GetMapping("/brands/list")
    public R relationBrandsList(@RequestParam(value = "catId",required = true)Long catId){
        List<BrandEntity> vos=categoryBrandRelationService.getBrandsByCatId(catId);
        List<BrandVo> collect = vos.stream().map(item -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());
            return brandVo;
        }).collect(Collectors.toList());
        return R.ok().put("data",collect);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){


		categoryBrandRelationService.saveDetail(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
