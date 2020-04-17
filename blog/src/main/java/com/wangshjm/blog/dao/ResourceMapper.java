package com.wangshjm.blog.dao;

import com.wangshjm.blog.entity.Resource;
import com.wangshjm.blog.entity.Role;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ResourceMapper extends Mapper<Resource> {
    /**
     * 根据要访问的url,查询需要的权限
     * @param url
     * @return
     */
    Resource findByUrl(String url);

    List<Role> findRolesOfResource(Long resourceId);
}
