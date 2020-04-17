package com.wangshjm.blog.service;

import com.wangshjm.blog.entity.UserInfo;

public interface UserInfoService {
    /**
     * 根据id查找用户信息
     *
     * @param id
     * @return
     */
    UserInfo findByUid(Long id);

    /**
     * 更新用户信息
     *
     * @param userInfo
     */
    void update(UserInfo userInfo);

    /**
     * 添加用户信息
     *
     * @param userInfo
     */
    void add(UserInfo userInfo);
}
