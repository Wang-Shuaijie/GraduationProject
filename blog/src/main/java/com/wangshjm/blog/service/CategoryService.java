package com.wangshjm.blog.service;

import com.wangshjm.blog.dao.CategoryMapper;
import com.wangshjm.blog.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> findAll() {
        List<Category> categoryList = categoryMapper.selectAll();
        return categoryList;
    }

    public Category findByCategoryName(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryMapper.selectOne(category);
    }
}
