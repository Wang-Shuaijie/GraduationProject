package com.wangshjm.blog.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Table(name = "tb_user_content")
public class UserContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //用户id
    private Long uId;
    //类型id
    private Long categoryId;
    //文章标题
    private String title;
    //文章类型
    private String category;
    //文章标签
    private String tag;
    //是否私有
    private String personal;
    //上传时间
    private Date rptTime;
    //用户头像
    private String imgUrl;
    //用户昵称
    private String nickName;
    //点赞数
    private Integer upvote;
    //点踩数
    private Integer downvote;
    //评论数量
    private Integer commentNum;
    //文章内容(存储html文本,用于显示)
    private String content;
    //存储markdown标记语言(用于编辑)
    private String markdownContent;
    //是否通过
    private String enabled;
    //审核内容
    private String remark;
    //类型数量
    @Transient//非表中实际字段
    private Integer num;
}