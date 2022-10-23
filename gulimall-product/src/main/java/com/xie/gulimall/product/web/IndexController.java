package com.xie.gulimall.product.web;

import com.xie.gulimall.product.entity.CategoryEntity;
import com.xie.gulimall.product.service.CategoryService;
import com.xie.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @title: IndexController
 * @Author Xie
 * @Date: 2022/10/11 22:00
 * @Version 1.0
 */
@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @GetMapping({"/","index.html"})
    public String indexPage(Model model){

        //TODO 1.查出所有的一级分类
      List<CategoryEntity> categoryEntities=categoryService.getLevel1Categorys();
      model.addAttribute("categorys",categoryEntities);
        //视图解析器进行拼串；
        //classpath:/templates/+返回值+.html
        return "index";
    }

    //index/catalog.json
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson(){
        Map<String, List<Catelog2Vo>> catalogJson=  categoryService.getCatalogJson();
                return catalogJson;
    }

    //index/catalog.json
    @ResponseBody
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
