package com.wangshjm.blog.service.impl;

import com.wangshjm.blog.dao.ResourceMapper;
import com.wangshjm.blog.entity.Resource;
import com.wangshjm.blog.entity.Role;
import com.wangshjm.blog.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public Resource getResourceByUrl(String url) {
        return resourceMapper.findByUrl(url);
    }

    @Override
    public List<Role> getRoles(Long resourceId) {
        return resourceMapper.findRolesOfResource(resourceId);
    }
}
