package com.xie.gulimall.search.controller;

import com.sun.org.apache.regexp.internal.RE;
import com.xie.gulimall.search.service.MallSearchService;
import com.xie.gulimall.search.vo.SearchParam;
import com.xie.gulimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @title: SearchController
 * @Author Xie
 * @Date: 2022/10/26 22:23
 * @Version 1.0
 */
@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    /**
     * 自动将页面提交过来的所有请求查询参数封装成指定的对象
     * @param param
     * @return
     */
    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model,HttpServletRequest request){
        String queryString = request.getQueryString();
        param.set_queryString(queryString);
        //1.根据传递进来的页面参数，去es中检索商品
      SearchResult result=mallSearchService.search(param);
      model.addAttribute("result",result);
        return "list";
    }
}
