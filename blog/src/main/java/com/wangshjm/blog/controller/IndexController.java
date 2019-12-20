package com.wangshjm.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.entity.User;
import com.wangshjm.blog.entity.UserContent;
import com.wangshjm.blog.service.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class IndexController extends BaseController {
    @Autowired
    private UserContentService userContentService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UpvoteService upvoteService;
    @Autowired
    private EsDataService esDataService;


    @RequestMapping("/index")
    public String index(Model model, @RequestParam(value = "keyword", required = false) String keyword,
                        @RequestParam(value = "pageNum", required = false) Integer pageNum,
                        @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        User user = (User) getSession().getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
        if (!StringUtils.isEmpty(keyword)) {
            PageInfo<UserContent> page = esDataService.search(keyword, pageNum, pageSize);
            model.addAttribute("keyword", keyword);
            model.addAttribute("page", page);
        } else {
            PageInfo page = userContentService.findAll(pageNum, pageSize);
            model.addAttribute("page", page);
            model.addAttribute("list", JSONObject.toJSONString(page.getList()));
        }
        return "/index";
    }
}
