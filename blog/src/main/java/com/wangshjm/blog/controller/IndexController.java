package com.wangshjm.blog.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

public class IndexController extends BaseController{

    @RequestMapping("/index")
    public String index(Model modle){
        return "/index";
    }
}
