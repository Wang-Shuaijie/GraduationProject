package com.wangshjm.blog.service.impl;

import com.wangshjm.blog.dao.UpvoteMapper;
import com.wangshjm.blog.entity.Upvote;
import com.wangshjm.blog.service.UpvoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UpvoteServiceImpl implements UpvoteService {
    @Autowired
    private UpvoteMapper upvoteMapper;

    @Override
    public Upvote findByUidAndConId(Upvote upvote) {
        return upvoteMapper.selectOne(upvote);
    }

    @Override
    public int add(Upvote upvote) {
        return upvoteMapper.insert(upvote);
    }

    @Override
    public void update(Upvote upvote) {
        upvoteMapper.updateByPrimaryKey(upvote);
    }

    @Override
    public void deleteByContentId(Long cid) {
        Upvote upvote = new Upvote();
        upvote.setContentId(cid);
        upvoteMapper.delete(upvote);
    }
}
