package com.wangshjm.blog.dao;

import com.wangshjm.blog.entity.UserContent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface UserContentMapper extends Mapper<UserContent> {
    /**
     * 根据用户id查询文章分类
     *
     * @param uid
     * @return
     */
    List<UserContent> findCategoryByUid(@Param("uid") long uid);

    /**
     * 解决：个人头像修改之后，发现首页的文章作者头像并未改变
     * 因为用户更新了昵称或者个人头像时，都要去更新一遍 user_content 表的 nick_name 和 img_url 字段，这样频繁操作数据库太耗性能
     * 使用两表关联查询。查询文章的时候根据用户 id 关联查询用户表 uesr 和文章表 user_content。这里使用左连接查询
     */
    List<UserContent> findByJoin(UserContent userContent);
}
