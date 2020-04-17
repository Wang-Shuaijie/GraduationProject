package com.wangshjm.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.dao.CommentMapper;
import com.wangshjm.blog.entity.Comment;
import com.wangshjm.blog.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public int add(Comment comment) {
        return commentMapper.insertComment(comment);
    }

    @Override
    public void update(Comment comment) {
        commentMapper.updateByPrimaryKey(comment);
    }


    @Override
    public Comment findById(Long id) {
        Comment comment = new Comment();
        comment.setId(id);
        return commentMapper.selectOne(comment);
    }

    @Override
    public List<Comment> findAllFirstComment(Long content_id) {
        return commentMapper.findAllFirstComment(content_id);
    }

    @Override
    public List<Comment> findAllChildrenComment(Long content_id, String children) {
        return commentMapper.findAllChildrenComment(content_id, children);
    }

    @Override
    public void deleteById(Long id) {
        Comment c = new Comment();
        c.setId(id);
        commentMapper.deleteByPrimaryKey(c);
    }

    @Override
    public void deleteChildrenComment(String children) {
        Example example = new Example(Comment.class);
        Example.Criteria criteria = example.createCriteria();
        List<Object> list = new ArrayList<Object>();
        String[] split = children.split(",");
        for (int i = 0; i < split.length; i++) {
            list.add(split[i]);
        }
        criteria.andIn("id", list);
        commentMapper.deleteByExample(example);
    }

    @Override
    public void deleteByContentId(Long cid) {
        Comment comment = new Comment();
        comment.setContentId(cid);
        commentMapper.delete(comment);
    }

    @Override
    public PageInfo<Comment> findAllComment(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);//开始分页
        List<Comment> list = commentMapper.selectAll();
        PageInfo<Comment> pageInfo = new PageInfo(list);
        return pageInfo;
    }
}
