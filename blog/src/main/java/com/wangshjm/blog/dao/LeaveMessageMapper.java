package com.wangshjm.blog.dao;

import com.wangshjm.blog.entity.LeaveMessage;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface LeaveMessageMapper extends Mapper<LeaveMessage> {
    //查找用户的已读消息
    List<LeaveMessage> findRead(Long uid);
    //查找用户的未读消息
    List<LeaveMessage> findUnRead(Long uid);
}
