package com.wangshjm.blog.dao;

import com.wangshjm.blog.entity.Category;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface CategoryMapper extends Mapper<Category> {
    //根据类型名称查找id
    Category fingByCategoryName(String name);
}
