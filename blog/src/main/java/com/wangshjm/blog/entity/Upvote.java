package com.wangshjm.blog.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 点赞表实体
 */
@Data
@NoArgsConstructor
@Table(name = "tb_upvote")
public class Upvote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //点赞用户id
    private Long uId;
    //文章id
    private Long contentId;
    //点赞用户ip
    private String ip;
    //点赞
    private String upvote;
    //点踩
    private String downvote;
    //点赞时间
    private Date upvoteTime;
}