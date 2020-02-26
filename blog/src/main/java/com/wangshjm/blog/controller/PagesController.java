package com.wangshjm.blog.controller;

import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.entity.User;
import com.wangshjm.blog.entity.UserContent;
import com.wangshjm.blog.entity.UserInfo;
import com.wangshjm.blog.service.EsDataService;
import com.wangshjm.blog.service.UserContentService;
import com.wangshjm.blog.service.UserInfoService;
import com.wangshjm.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class PagesController extends BaseController {
    @Autowired
    private EsDataService esDataService;
    @Autowired
    private UserContentService userContentService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserService userService;

    @RequestMapping("/accessDenied")
    public String accessDenied(Model model) {
        return "/accessDenied";
    }

    @RequestMapping("/toProfilePage")
    public String toProfilePage() {
        return "personal/profile";
    }

    @RequestMapping("/hot")
    public String hotPage(Model model, @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "pageNum", required = false) Integer pageNum,
                          @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (ObjectUtils.isEmpty(pageNum) && ObjectUtils.isEmpty(pageSize)) {
            pageNum = 1;
            pageSize = 5;
        }
        if (pageSize < 5) {
            pageSize = 5;
        }

        User user = getCurrentUser();
        model.addAttribute("user", user);

        if (!StringUtils.isEmpty(keyword)) {
            PageInfo<UserContent> page = esDataService.search(keyword, pageNum, pageSize);
            model.addAttribute("keyword", keyword);
            model.addAttribute("page", page);
        } else {
            PageInfo<UserContent> page = userContentService.findByUpvote(pageNum, pageSize);
            model.addAttribute("page", page);
        }
        return "/index";
    }

    @RequestMapping("/profile")
    public String profilePage(Model model, @RequestParam(value = "uid", required = false) Long userId,
                              @RequestParam(value = "pageNum", required = false) Integer pageNum,
                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (ObjectUtils.isEmpty(pageNum) && ObjectUtils.isEmpty(pageSize)) {
            pageNum = 1;
            pageSize = 3;
        }

        User user = userService.findById(userId);
        model.addAttribute("user", user);

        UserInfo userInfo = userInfoService.findByUid(userId);
        model.addAttribute("userInfo", userInfo);

        UserContent userContent = new UserContent();
        userContent.setUId(userId);
        PageInfo<UserContent> page = userContentService.findAll(userContent, pageNum, pageSize);
        model.addAttribute("page", page);

        return "/personal/profile";
    }
}
