package com.wangshjm.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.entity.UserContent;
import com.wangshjm.blog.service.CategoryService;
import com.wangshjm.blog.service.UserContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserContentService userContentService;

    /**
     * 获得所有分类名以及每个分类名的文章数目
     * @return
     */
    @GetMapping(value = "/findCategoriesNameAndArticleNum")
    public Map findCategoriesNameAndArticleNum(){
        JSONObject resultJson = categoryService.findCategoryNameAndArticleNum();
        return resultJson;
    }


    /**
     * 分页获得该分类下的文章
     * @return
     */
    @GetMapping(value = "/getCategoryArticle")
    public Map getCategoryArticle(@RequestParam("category") String category,
                                  HttpServletRequest request){
        JSONObject result = new JSONObject();
        if(!StringUtils.isEmpty(category)){
            category = category;
        }
        int rows = Integer.parseInt(request.getParameter("rows"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        UserContent userContent = new UserContent();
        userContent.setCategory(category);
        PageInfo pageInfo = userContentService.findAll(userContent, pageNum, rows);
        result.put("pageInfo", pageInfo);
        result.put("result", pageInfo.getList());
        result.put("category", category);
        return result;
    }

}
