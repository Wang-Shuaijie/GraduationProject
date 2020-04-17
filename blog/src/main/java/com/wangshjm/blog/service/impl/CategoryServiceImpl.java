package com.wangshjm.blog.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangshjm.blog.dao.CategoryMapper;
import com.wangshjm.blog.entity.Category;
import com.wangshjm.blog.service.CategoryService;
import com.wangshjm.blog.service.UserContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private UserContentService userContentService;

    public List<Category> findAll() {
        List<Category> categoryList = categoryMapper.selectAll();
        return categoryList;
    }

    public Category findByCategoryName(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryMapper.selectOne(category);
    }

    public JSONObject findCategoryNameAndArticleNum() {
        List<Category> categoryList = categoryMapper.selectAll();
        JSONArray categoryJsonArray = new JSONArray();
        JSONObject returnJson = new JSONObject();
        JSONObject categoryJson;
        for (Category category : categoryList) {
            categoryJson = new JSONObject();
            categoryJson.put("categoryName", category.getName());
            categoryJson.put("categoryArticleNum", userContentService.countByCategory(category.getName()));
            categoryJsonArray.add(categoryJson);
        }

        returnJson.put("result", categoryJsonArray);
        return returnJson;
    }
}
