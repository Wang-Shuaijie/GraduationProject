package com.wangshjm.blog.config;

import com.wangshjm.blog.constant.ConstantsValue;
import com.wangshjm.blog.security.*;
import com.wangshjm.blog.security.email.*;
import com.wangshjm.blog.security.phone.PhoneAuthenticationFilter;
import com.wangshjm.blog.security.phone.PhoneAuthenticationProvider;
import com.wangshjm.blog.security.phone.PhoneUserDetailsService;
import com.wangshjm.blog.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法安全设置
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    //用户验证服务
    @Autowired
    private EmailDetailsService emailDetailsService;
    @Autowired
    private PhoneUserDetailsService phoneUserDetailsService;
    @Autowired
    private PhoneAuthenticationProvider phoneAuthenticationProvider;


    //根据一个url请求，获得访问它所需要的roles权限
    @Autowired
    private MyFilterInvocationSecurityMetadataSourceImpl myFilterInvocationSecurityMetadataSource;

    //接收一个用户的信息和访问一个url所需要的权限，判断该用户是否可以访问
    @Autowired
    private AccessDecisionManagerImpl myAccessDecisionManager;

    //失败策略
    @Autowired
    private MyAuthenticationFailureHandler failureHandler;

    //成功策略
    @Autowired
    private MyAuthenticationSuccessHandler successHandler;

    //403页面
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PhoneAuthenticationFilter phoneFilter() {
        PhoneAuthenticationFilter filter = new PhoneAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        //在filter这里设置失败成功处理器保证其不失效
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }

    @Bean
    public EmailAuthenticationFilter emailFilter() {
        EmailAuthenticationFilter filter = new EmailAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //手机验证登录
        auth.authenticationProvider(phoneAuthenticationProvider)
                .userDetailsService(phoneUserDetailsService);


        auth.userDetailsService(emailDetailsService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return MD5Util.encodeToHex(ConstantsValue.SALT + charSequence.toString());
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                String encode = MD5Util.encodeToHex(ConstantsValue.SALT + charSequence.toString());
                return s.equals(encode);
            }
        });
    }

    //在这里配置哪些页面不需要认证
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/bootstrap/**", "/editormd/**", "/reply/**", "/zui/**", "/css/**", "/js/**", "/images/**", "/favicon.ico");
    }

    /**
     * 定义安全策略
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.authorizeRequests()       //配置安全策略
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource);
                        o.setAccessDecisionManager(myAccessDecisionManager);
                        return o;
                    }
                })
                .and()
                .anonymous()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/index")
                .failureHandler(failureHandler)
                .and()
                .logout().logoutUrl("/loginout").logoutSuccessUrl("/login")
                .permitAll()    //所有人可以登出
                .and().rememberMe().tokenValiditySeconds(86400)
                .and()
                .csrf()
                .disable()
                //配置自定义403响应
                .exceptionHandling()
                //未登录用户也要进行验证，没有这一句将直接跳到登录页
                //AuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常
                //AccessDeineHandler 用来解决认证过的用户访问无权限资源时的异常
//                .authenticationEntryPoint(xxx)
                .accessDeniedHandler(myAccessDeniedHandler);


        http.addFilterBefore(phoneFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(emailFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
