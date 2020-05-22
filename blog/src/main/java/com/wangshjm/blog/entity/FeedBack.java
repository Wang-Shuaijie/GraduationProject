package com.wangshjm.blog.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * 反馈信息
 */
@Data
@NoArgsConstructor
@Table(name = "tb_feedback")
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String feedbackContent;

    private Long userId;

    @Transient
    private String nickName;

    private String contactInfo;

    private Date time;
}
