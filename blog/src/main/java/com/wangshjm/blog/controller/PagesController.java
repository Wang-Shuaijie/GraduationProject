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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @RequestMapping("/watch")
    public String watchArticle(Model model, @RequestParam(value = "cid", required = false) Long cid) {
        User user = getCurrentUser();
        UserContent userContent = userContentService.findById(cid);
        model.addAttribute("article", userContent);
        model.addAttribute("user", user);
        return "/watch";
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

    @RequestMapping("/verify")
    public String profilePage(Model model, @RequestParam(value = "cid", required = false) Long cid) {
        UserContent userContent = userContentService.findById(cid);
        model.addAttribute("article", userContent);

        return "/verify";
    }

    @RequestMapping("/archives")
    public String toPageArchives(){
        return "/archives";
    }

    @RequestMapping("/categories")
    public String toPageCategories(){
        return "/categories";
    }

    /**
     * 跳转到标签页
     */
    @RequestMapping("/tags")
    public String toPageTags(HttpServletResponse response,
                       HttpServletRequest request){
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String tag = request.getParameter("tag");

        if(!StringUtils.isEmpty(tag)){
            response.setHeader("tag", tag);
        }
        return "/tags";
    }

    @RequestMapping("/admin")
    public String toPageAdmin(){
        return "/admin/superadmin";
    }

    @RequestMapping("/friendlylink")
    public String toPageFriendlyLink(){
        return "/friendlyLink";
    }

    @RequestMapping("/header")
    public String toPageHeader(){
        return "/header";
    }

    @RequestMapping("/footer")
    public String toPageFooter(){
        return "/footer";
    }

}
