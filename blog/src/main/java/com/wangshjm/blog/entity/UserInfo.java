package com.wangshjm.blog.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private Date birthday;
    //爱好
    private String hobby;
    //地址
    private String address;

    public String getFormateBirthday(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(birthday);
    }
}