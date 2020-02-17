package com.wangshjm.blog.service;


import com.wangshjm.blog.entity.Category;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCategoryService {
    @Autowired
    private CategoryService categoryService;

    @Test
    public void testFind(){
        Category category = categoryService.findByCategoryName("程序开发");
        System.out.println(category.getId());
    }
}
