package com.wangshjm.blog.dao;

import com.wangshjm.blog.entity.Tag;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface TagMapper extends Mapper<Tag> {
}
