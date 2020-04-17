package com.wangshjm.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.dao.NoticeMapper;
import com.wangshjm.blog.entity.Notice;
import com.wangshjm.blog.entity.UserContent;
import com.wangshjm.blog.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public Notice findNewest() {
        return noticeMapper.findNewest();
    }

    @Override
    public PageInfo<Notice> getAllNotice(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Example e = new Example(UserContent.class);
        e.setOrderByClause("time DESC");
        List<Notice> list = noticeMapper.selectByExample(e);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }
}
