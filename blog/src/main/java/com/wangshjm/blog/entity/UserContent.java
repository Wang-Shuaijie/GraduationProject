package com.wangshjm.blog.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Data
@NoArgsConstructor
public class UserContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //用户id
    private Long uId;
    //文章标题
    private String title;
    //文章类型
    private String category;
    //是否私有
    private String personal;
    //上传时间
    private Date rptTime;
    //用户头像(这个字段可以删除)
    private String imgUrl;
    //用户昵称（这个字段可以删除）
    private String nickName;
    //点赞数
    private Integer upvote;
    //点踩数
    private Integer downvote;
    //评论数量
    private Integer commentNum;
    //文章内容
    private String content;
    //类型数量
    @Transient//非表中实际字段
    private Integer num;
}