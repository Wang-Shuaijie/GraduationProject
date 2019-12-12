package com.wangshjm.blog.dao;

import com.wangshjm.blog.entity.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    User findUserByEmailAndPwd(@Param("email") String email, @Param("password") String password);

    User findByEmail(String email);

    User findByPhone(String phone);
}
