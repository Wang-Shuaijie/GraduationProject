package com.wangshjm.blog.service;

import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.entity.Comment;

import java.util.List;

public interface CommentService {
    /**
     * 添加评论
     *
     * @param comment
     * @return
     */
    int add(Comment comment);


    /**
     * 更新评论
     *
     * @param comment
     */
    void update(Comment comment);


    /**
     * 根据id查询评论
     *
     * @param id
     * @return
     */
    Comment findById(Long id);

    /**
     * 根据文章id查询所有父评论
     */
    List<Comment> findAllFirstComment(Long content_id);

    /**
     * 根据文章id和子评论ids查询所有子评论
     */
    List<Comment> findAllChildrenComment(Long content_id, String children);

    /**
     * 根据id删除评论
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 批量删除子评论
     *
     * @param children
     */
    void deleteChildrenComment(String children);

    /**
     * 根据文章id删除评论
     *
     * @param cid
     */
    void deleteByContentId(Long cid);

    //获取所有评论
    PageInfo<Comment> findAllComment(Integer pageNum, Integer pageSize);
}
