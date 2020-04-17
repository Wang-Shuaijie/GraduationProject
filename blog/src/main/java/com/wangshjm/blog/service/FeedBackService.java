package com.wangshjm.blog.service;

import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.entity.FeedBack;

public interface FeedBackService {
    PageInfo<FeedBack> getAllFeedBack(Integer pageNum, Integer pageSize);
}
