package com.wangshjm.blog.service;

import com.alibaba.fastjson.JSONObject;
import com.wangshjm.blog.entity.Category;

import java.util.List;

public interface CategoryService {
    //查找所有类型
    List<Category> findAll();

    Category findByCategoryName(String name);

    JSONObject findCategoryNameAndArticleNum();
}
