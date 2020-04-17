package com.wangshjm.blog.service;

import com.wangshjm.blog.entity.LeaveMessage;

import java.util.List;

public interface LeaveMessageService {
    //新建留言
    int insert(LeaveMessage leaveMessage);
    //查找用户的已读消息
    List<LeaveMessage> findRead(Long uid);
    //查找用户的未读消息
    List<LeaveMessage> findUnRead(Long uid);

    int updateRead(Long id);
}
