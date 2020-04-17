package com.wangshjm.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/toLoginPage")
    public String toLogin(){
        return "login";
    }
}
