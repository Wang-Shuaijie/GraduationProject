package com.wangshjm.blog.service.impl;

import com.wangshjm.blog.dao.FriendLinkMappper;
import com.wangshjm.blog.entity.FriendLink;
import com.wangshjm.blog.service.FriendLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendLinkServiceImpl implements FriendLinkService {
    @Autowired
    private FriendLinkMappper friendLinkMappper;

    @Override
    public List<FriendLink> findAllFriendLink() {
        return friendLinkMappper.selectAll();
    }
}
