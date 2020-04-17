package com.wangshjm.blog.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Table(name = "tb_user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //用户id
    private Long uId;
    //姓名
    private String name;
    //性别
    private String sex;
    //生日
    private String birthday;
    //爱好
    private String hobby;
    //地址
    private String address;
    //公司或学校
    private String work;
    //学历
    private String education;
    //个人简介
    private String information;

}