package com.wangshjm.blog.security.email;

import com.wangshjm.blog.entity.Role;
import com.wangshjm.blog.entity.User;
import com.wangshjm.blog.service.RoleService;
import com.wangshjm.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.findByEmail(s);
        if (user == null) {
            throw new UsernameNotFoundException("用户名错误");
        }
        List<Role> roles = roleService.findByUid(user.getId());
        user.setRoles(roles);
        return user;
    }
}
