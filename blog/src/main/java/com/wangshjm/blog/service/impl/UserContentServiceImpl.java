package com.wangshjm.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.dao.UserContentMapper;
import com.wangshjm.blog.entity.UserContent;
import com.wangshjm.blog.service.UserContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Repository
public class UserContentServiceImpl implements UserContentService {
    @Autowired
    private UserContentMapper userContentMapper;


    @Override
    public int addContent(UserContent content) {
        return userContentMapper.insert(content);
    }

    @Override
    public List<UserContent> findByUserId(Long uid) {
        UserContent userContent = new UserContent();
        userContent.setUId(uid);
        List<UserContent> list = userContentMapper.select(userContent);
        return list;
    }

    @Override
    public UserContent findById(long id) {
        UserContent userContent = new UserContent();
        userContent.setId(id);
        List<UserContent> list = userContentMapper.findByJoin(userContent);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public int update(UserContent content) {
        return userContentMapper.updateByPrimaryKeySelective(content);
    }

    @Override
    public int deleteById(Long id) {
        return userContentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<UserContent> findCategoryByUid(Long uid) {
        return userContentMapper.findCategoryByUid(uid);
    }


    @Override
    public PageInfo<UserContent> findAll(UserContent userContent, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);//开始分页
        List<UserContent> list = userContentMapper.findByJoin(userContent);
        PageInfo<UserContent> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<UserContent> findByUpvote(Integer pageNum, Integer pageSize) {
        Example e = new Example(UserContent.class);
        e.setOrderByClause("upvote DESC");
        PageHelper.startPage(pageNum, pageSize);//开始分页
        List<UserContent> list = userContentMapper.selectByExample(e);
        PageInfo<UserContent> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}
