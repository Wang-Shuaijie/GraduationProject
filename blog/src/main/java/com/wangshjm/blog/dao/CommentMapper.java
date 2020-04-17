package com.wangshjm.blog.dao;

import com.wangshjm.blog.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CommentMapper extends Mapper<Comment> {
    //根据回答者查询所有评论
    List<Comment> findByAnswererId(@Param("cid") long uid);

    //根据被回复人id查询评论
    List<Comment> findByRespondentId(@Param("cid") long uid);

    //根据文章id查询所有一级评论
    List<Comment> findAllFirstComment(@Param("cid") long cid);

    //根据文章id和二级评论ids查询出所有二级评论
    List<Comment> findAllChildrenComment(@Param("cid") long cid, @Param("children") String children);

    //插入评论并返回主键id 返回类型只是影响行数  id在Comment对象中
    int insertComment(Comment comment);


}
