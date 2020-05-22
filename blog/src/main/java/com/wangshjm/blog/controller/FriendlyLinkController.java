package com.wangshjm.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.wangshjm.blog.entity.FriendLink;
import com.wangshjm.blog.service.FriendLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FriendlyLinkController {
    @Autowired
    private FriendLinkService friendLinkService;

    /**
     * 获得所有友链信息
     */
    @PostMapping(value = "/getFriendLinkInfo")
    public JSONObject getFriendLink(){
        JSONObject resultJson = new JSONObject();
        List<FriendLink> linkList = friendLinkService.getAllFriendLink();
        resultJson.put("data", linkList);
        return resultJson;
    }
}
