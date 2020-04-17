package com.wangshjm.blog.service;

import com.wangshjm.blog.entity.Role;

import java.util.List;

public interface RoleService {
    /**
     * 根据id查询角色
     *
     * @param id
     * @return
     */
    Role findById(Long id);

    /**
     * 添加角色
     *
     * @param role
     * @return
     */
    int add(Role role);

    /**
     * 根据用户id查询所有角色
     *
     * @param uid
     * @return
     */
    List<Role> findByUid(Long uid);

    /**
     * 给用户赋予角色权限
     * @param uid
     * @return
     */
    int grant(Long uid);
}
