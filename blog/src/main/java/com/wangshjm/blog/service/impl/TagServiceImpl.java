package com.wangshjm.blog.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wangshjm.blog.dao.TagMapper;
import com.wangshjm.blog.entity.Tag;
import com.wangshjm.blog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> findAll() {
        return tagMapper.selectAll();
    }

    @Override
    public JSONObject findTags() {
        List<Tag> tagList =  tagMapper.selectAll();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", tagList);
        jsonObject.put("tagsNum", tagList.size());
        jsonObject.put("status", 301);
        return jsonObject;
    }
}
