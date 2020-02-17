package com.wangshjm.blog.dao;

import com.wangshjm.blog.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RoleMapper extends Mapper<Role> {
    /**
     * 根据用户id查询角色信息
     *
     * @param uid
     * @return
     */
    List<Role> findByUid(@Param("uid") Long uid);
}
