package com.wangshjm.blog.security.phone;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class PhoneAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String phoneParameter = "telephone";
    public static final String codeParameter = "phone_code";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public PhoneAuthenticationFilter() {
        super(new AntPathRequestMatcher("/phoneLogin"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String phone = this.obtainPhone(request);
        String phone_code = this.obtainValidateCode(request);
        if (phone == null) {
            phone = "";
        }
        if (phone_code == null) {
            phone_code = "";
        }

        phone = phone.trim();
        String cache_code = redisTemplate.opsForValue().get(phone);

        if (!phone_code.equals(cache_code)) {
            log.info("验证失败!");
            throw new UsernameNotFoundException("手机验证码错误或已过期");
        }
        log.info("验证成功！");
        PhoneAuthenticationToken authRequest = new PhoneAuthenticationToken(phone);
        this.setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);

    }

    protected void setDetails(HttpServletRequest request, PhoneAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    protected String obtainPhone(HttpServletRequest request) {
        return request.getParameter(phoneParameter);
    }

    protected String obtainValidateCode(HttpServletRequest request) {
        return request.getParameter(codeParameter);
    }
}
