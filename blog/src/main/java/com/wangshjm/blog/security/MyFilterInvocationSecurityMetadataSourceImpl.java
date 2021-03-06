package com.wangshjm.blog.security;

import com.wangshjm.blog.entity.Resource;
import com.wangshjm.blog.entity.Role;
import com.wangshjm.blog.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
/**
 * 接收用户请求的地址，返回访问该地址需要的所有权限
 */
public class MyFilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private ResourceService resourceService;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //得到用户的请求地址,控制台输出一下
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        System.out.println("用户请求的地址是：" + requestUrl);

        //如果登录页面就不需要权限
        if ("/login".equals(requestUrl)) {
            return null;
        }
        //对请求url处理，去除参数部分，便于比较
        if (requestUrl.indexOf("?") != -1) {
            requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
        }
        Resource resource = resourceService.getResourceByUrl(requestUrl);

//        //如果没有匹配的url则说明大家登录都可以访问
//        if(resource == null) {
//            return SecurityConfig.createList("ROLE_LOGIN");
//        }
        //如果没有匹配的url则说明大家都可以访问
        if (resource == null) {
            return SecurityConfig.createList("ROLE_VISIT");
        }

        //将resource所需要到的roles按框架要求封装返回（ResourceService里面的getRoles方法是基于RoleRepository实现的）
        List<Role> roles = resourceService.getRoles(resource.getId());
        int size = roles.size();
        String[] values = new String[size];
        for (int i = 0; i < size; i++) {
            values[i] = roles.get(i).getRoleValue();
        }
        return SecurityConfig.createList(values);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
