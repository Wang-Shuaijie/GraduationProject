package com.wangshjm.blog.dao;

import com.wangshjm.blog.entity.Notice;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface NoticeMapper extends Mapper<Notice> {
    Notice findNewest();
}
