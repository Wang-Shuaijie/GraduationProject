package com.wangshjm.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class PagesController {
    @RequestMapping("/accessDenied")
    public String accessDenied(Model model) {
        return "/accessDenied";
    }
}
