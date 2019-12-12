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

    private Long uId;

    private String title;

    private String category;

    private String personal;

    private Date rptTime;

    private String imgUrl;

    private String nickName;

    private Integer upvote;

    private Integer downvote;

    private Integer commentNum;

    private String content;

    private String editormd;//md格式

    @Transient
    private  Integer num;

}