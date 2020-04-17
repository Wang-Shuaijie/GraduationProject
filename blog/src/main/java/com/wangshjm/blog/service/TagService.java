package com.wangshjm.blog.service;

import com.alibaba.fastjson.JSONObject;
import com.wangshjm.blog.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> findAll();

    JSONObject findTags();
}
