package com.wangshjm.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.wangshjm.blog.entity.LeaveMessage;
import com.wangshjm.blog.service.LeaveMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
public class LeaveMessageController {
    @Autowired
    private LeaveMessageService leaveMessageService;

    @GetMapping(value = "/insertLeaveMessage")
    @Transactional(rollbackFor = {Exception.class})
    public JSONObject insertLeaveMessage(@RequestParam("answererId") String answererId, @RequestParam("respondentId") String respondentId,
                                         @RequestParam("message") String message, @RequestParam("isRead") String isRead) {
        JSONObject result = new JSONObject();
        LeaveMessage leaveMessage = new LeaveMessage();
        leaveMessage.setAnswererId(Long.parseLong(answererId));
        leaveMessage.setRespondentId(Long.parseLong(respondentId));
        leaveMessage.setMessage(message);
        leaveMessage.setIsRead(isRead);
        leaveMessage.setTime(new Date());
        try {
            leaveMessageService.insert(leaveMessage);
            result.put("result", 0);
        } catch (Exception e) {
            log.error("留言出错:{}", e.getMessage());
            result.put("result", 1);
        }
        return result;
    }

    @GetMapping(value = "/updateLeaveMessage")
    public JSONObject updateSatus(@RequestParam("id") String LeaveMessageId){
        JSONObject result = new JSONObject();
        try{
            leaveMessageService.updateRead(Long.parseLong(LeaveMessageId));
            result.put("result", 0);
        }catch (Exception e){
            log.error("出错:{}", e.getMessage());
            result.put("result", 1);
        }
        return result;
    }

}
