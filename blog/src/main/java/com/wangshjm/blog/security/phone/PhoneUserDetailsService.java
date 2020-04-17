package com.wangshjm.blog.security.phone;

import com.wangshjm.blog.entity.Role;
import com.wangshjm.blog.entity.User;
import com.wangshjm.blog.service.RoleService;
import com.wangshjm.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhoneUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.findByPhone(s);
        if(user == null){
            throw new UsernameNotFoundException("手机号码错误");
        }
        List<Role> roles = roleService.findByUid(user.getId());
        user.setRoles(roles);
        return user;
    }
}
