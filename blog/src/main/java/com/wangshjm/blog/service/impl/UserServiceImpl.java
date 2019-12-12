package com.wangshjm.blog.service.impl;

import com.wangshjm.blog.dao.UserMapper;
import com.wangshjm.blog.entity.User;
import com.wangshjm.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public int regist(User user) {
        return userMapper.insert(user);
    }

    @Override
    public User login(String email, String password) {
        return userMapper.findUserByEmailAndPwd(email, password);
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public User findByPhone(String phone) {
        return userMapper.findByPhone(phone);
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        userMapper.delete(user);
    }

    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKey(user);
    }
}
