package com.wangshjm.blog.service;

import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.entity.Notice;

public interface NoticeService {
    Notice findNewest();

    PageInfo<Notice> getAllNotice(Integer pageNum, Integer pageSize);
}
