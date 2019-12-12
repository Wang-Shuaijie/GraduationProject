package com.wangshjm.blog.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

@Data
@NoArgsConstructor
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //邮箱
    private String email;
    //密码
    private String password;
    //联系方式
    private String phone;
    //昵称
    private String nickName;
    //激活状态
    private String state;
    //头像
    private String imgUrl;
    //是否可用
    private String enable;

    @Transient
    protected List<Role> roles;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", nickName='" + nickName + '\'' +
                ", state='" + state + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", enable='" + enable + '\'' +
                ", roles=" + roles +
                '}';
    }
}