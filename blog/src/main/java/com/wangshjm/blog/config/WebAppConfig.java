package com.wangshjm.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourceHandler = url请求的路径
        registry.addResourceHandler("/upload/**")
                //addResourceLocations=图片存放在服务器的真实路径
                .addResourceLocations("file:G://GraduationProject/upload/");
    }
}
