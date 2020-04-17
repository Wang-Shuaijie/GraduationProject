package com.wangshjm.blog.dao;

import com.wangshjm.blog.entity.Category;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface CategoryMapper extends Mapper<Category> {

}
