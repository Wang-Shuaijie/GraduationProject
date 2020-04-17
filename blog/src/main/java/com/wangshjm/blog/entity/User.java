package com.wangshjm.blog.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "tb_user")
public class User implements UserDetails {
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

    /**
     * 获取角色信息（权限）
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtils.isEmpty(roles)) {
            return null;
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        for (Role r : roles) {
//            System.out.println(r.getRoleValue());
            authorities.add(new SimpleGrantedAuthority(r.getRoleValue()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    /**
     * 账户未过期
     * 这里没有用到，直接返回 True,默认为false
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户未锁定
     * 没有用到，直接返回 True,默认为false
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码未过期
     * 没有用到，直接返回 True,默认为false
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (!StringUtils.isEmpty(state) && "1".equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 为了区分是否为同一用户，重写equals和hashcode方法
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return getEmail().equals(((User) obj).getEmail()) || getUsername().equals(((User) obj).getUsername());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }
}