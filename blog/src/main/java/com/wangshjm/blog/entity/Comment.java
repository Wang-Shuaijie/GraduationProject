package com.wangshjm.blog.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 评论表实体
 */
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //文章id
    private Long conId;
    //评论者id
    private Long comId;
    //被评论者id
    private Long byId;
    //评论时间
    private Date commTime;
    //字评论ids
    private String children;
    //点赞数
    private Integer upvote;
    //评论内容
    private String comContent;

    //@Transient 表示该属性并非一个到数据库表的字段的映射,ORM框架将忽略该属性.
    @Transient
    private User user;

    @Transient
    private User byUser;

    @Transient
    private List<Comment> comList;

}