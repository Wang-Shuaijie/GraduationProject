package com.wangshjm.blog.service.impl;

import com.wangshjm.blog.dao.UserInfoMapper;
import com.wangshjm.blog.entity.UserInfo;
import com.wangshjm.blog.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService{
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo findByUid(Long id) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUId(id);
        return userInfoMapper.selectOne(userInfo);
    }

    @Override
    public void update(UserInfo userInfo) {
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
    }

    @Override
    public void add(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }
}
