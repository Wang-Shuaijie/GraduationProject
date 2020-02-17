package com.wangshjm.blog.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if ("User is disabled".equals(exception.getMessage())) {
            request.setAttribute("error", "该用户未激活");
        } else {
            request.setAttribute("error", "用户名或密码错误");
        }
//        //设置失败跳转页,这样写没法传参
//        super.setDefaultFailureUrl("/login");
//        super.onAuthenticationFailure(request, response, exception);
        //用request跳转,不要用父类方法
        request.getRequestDispatcher("/login").forward(request, response);
    }
}
