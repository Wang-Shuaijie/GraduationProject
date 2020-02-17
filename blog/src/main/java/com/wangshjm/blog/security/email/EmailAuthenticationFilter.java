package com.wangshjm.blog.security.email;

import com.google.code.kaptcha.Constants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EmailAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final String codeParameter = "code";
    private final String usernameParameter = "username";
    private final String passwordParameter = "password";

    public EmailAuthenticationFilter() {
        super(new AntPathRequestMatcher("/emailLogin"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);
        String code = this.obtainCode(request);
        String cacheCode = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (!code.equals(cacheCode)) {
            throw new UsernameNotFoundException("验证码错误");
        }
        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }
        username = username.trim();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        this.setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(this.codeParameter);
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.passwordParameter);
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.usernameParameter);
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}
