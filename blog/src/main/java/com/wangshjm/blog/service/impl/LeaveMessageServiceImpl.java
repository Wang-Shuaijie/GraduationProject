package com.wangshjm.blog.service.impl;

import com.wangshjm.blog.dao.LeaveMessageMapper;
import com.wangshjm.blog.entity.LeaveMessage;
import com.wangshjm.blog.service.LeaveMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LeaveMessageServiceImpl implements LeaveMessageService {
    @Autowired
    private LeaveMessageMapper leaveMessageMapper;

    @Override
    public int insert(LeaveMessage leaveMessage) {
        return leaveMessageMapper.insert(leaveMessage);
    }

    @Override
    public List<LeaveMessage> findRead(Long uid) {
        return leaveMessageMapper.findRead(uid);
    }

    @Override
    public List<LeaveMessage> findUnRead(Long uid) {
        return leaveMessageMapper.findUnRead(uid);
    }

    @Override
    public int updateRead(Long id) {
        LeaveMessage leaveMessage = leaveMessageMapper.selectByPrimaryKey(id);
        leaveMessage.setIsRead("1");
        return leaveMessageMapper.updateByPrimaryKey(leaveMessage);
    }

}
