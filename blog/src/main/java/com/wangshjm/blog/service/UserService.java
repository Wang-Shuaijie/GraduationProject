package com.wangshjm.blog.service;

import com.wangshjm.blog.entity.User;

public interface UserService {
    /**
     * 注册
     *
     * @param user
     * @return
     */
    int regist(User user);

    /**
     * 登录
     *
     * @param email
     * @param password
     * @return
     */
    User login(String email, String password);

    /**
     * 根据用户邮箱查询用户
     *
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 根据手机号查询用户
     *
     * @param phone
     * @return
     */
    User findByPhone(String phone);

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    User findById(Long id);

    /**
     * 根据邮箱账号删除用户
     *
     * @param email
     */
    void deleteByEmail(String email);

    /**
     * 更新用户信息
     *
     * @param user
     */
    void update(User user);
}
