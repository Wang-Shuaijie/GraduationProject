package com.wangshjm.blog.service.impl;

import com.wangshjm.blog.dao.RoleMapper;
import com.wangshjm.blog.entity.Role;
import com.wangshjm.blog.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Role findById(Long id) {
        Role role = new Role();
        role.setId(id);
        return roleMapper.selectOne(role);
    }

    @Override
    public int add(Role role) {
        return roleMapper.insert(role);
    }

    @Override
    public List<Role> findByUid(Long uid) {
        return roleMapper.findByUid(uid);
    }

    @Override
    public int grant(Long uid) {
        return roleMapper.grantUser(uid);
    }
}
