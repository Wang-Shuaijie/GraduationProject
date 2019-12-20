package com.wangshjm.blog.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.dao.CommentMapper;
import com.wangshjm.blog.dao.UserContentMapper;
import com.wangshjm.blog.entity.UserContent;
import com.wangshjm.blog.service.UserContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Repository
public class UserContentServiceImpl implements UserContentService {
    @Autowired
    private UserContentMapper userContentMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PageInfo<UserContent> findAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);//开始分页
        List<UserContent> list = userContentMapper.findByJoin(null);
        PageInfo<UserContent> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

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
    public List<UserContent> findAll() {
        return userContentMapper.select(null);
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
    public PageInfo<UserContent> findByCategory(String category, Long uid, Integer pageNum, Integer pageSize) {
        UserContent userContent = new UserContent();
        if (!StringUtils.isEmpty(category) && !"null".equals(category)) {
            userContent.setCategory(category);
        }
        userContent.setUId(uid);
        userContent.setPersonal("0");
        PageHelper.startPage(pageNum, pageSize);//开始分页
        List<UserContent> list = userContentMapper.select(userContent);
        PageInfo<UserContent> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<UserContent> findPersonal(Long uid, Integer pageNum, Integer pageSize) {
        UserContent userContent = new UserContent();
        userContent.setUId(uid);
        userContent.setPersonal("1");
        PageHelper.startPage(pageNum, pageSize);//开始分页
        List<UserContent> list = userContentMapper.select(userContent);
        PageInfo<UserContent> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}
