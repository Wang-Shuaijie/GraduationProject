package com.wangshjm.blog.dao;

import com.wangshjm.blog.entity.Upvote;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UpvoteMapper extends Mapper<Upvote> {
}
