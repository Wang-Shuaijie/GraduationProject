package com.wangshjm.blog.controller;

import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.entity.LeaveMessage;
import com.wangshjm.blog.entity.User;
import com.wangshjm.blog.entity.UserContent;
import com.wangshjm.blog.entity.UserInfo;
import com.wangshjm.blog.service.*;
import com.wangshjm.blog.utils.TransCodingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    @Autowired
    private LeaveMessageService leaveMessageService;

    @RequestMapping("/accessDenied")
    public String accessDenied(Model model) {
        return "/accessDenied";
    }

    @GetMapping("/watch")
    public String watchArticle(Model model, @RequestParam("cid") Long cid, HttpServletResponse response,
                               HttpServletRequest request) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        User user = getCurrentUser();
        UserContent userContent = userContentService.findById(cid);
        model.addAttribute("article", userContent);
        model.addAttribute("user", user);
        //将文章id存入响应头
        response.setHeader("cid", String.valueOf(cid));
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

        User visitor = getCurrentUser();
        model.addAttribute("visitor", visitor);

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

    @GetMapping("/personal")
    public String personalPage(Model model, @RequestParam(value = "pageNum", required = false) Integer pageNum,
                               @RequestParam(value = "pageSize", required = false) Integer pageSize){
        if (ObjectUtils.isEmpty(pageNum) && ObjectUtils.isEmpty(pageSize)) {
            pageNum = 1;
            pageSize = 10;
        }
        User user = getCurrentUser();
        if(user == null){
            return "/login";
        }
        model.addAttribute("user", user);
        UserInfo userInfo = userInfoService.findByUid(user.getId());
        model.addAttribute("userInfo", userInfo);
        UserContent content = new UserContent();
        UserContent personalContent = new UserContent();

        content.setUId(user.getId());
        personalContent.setUId(user.getId());

        //查询文章分类
        List<UserContent> ArticleCategorys = userContentService.findCategoryByUid(user.getId());
        model.addAttribute("categorys", ArticleCategorys);
        //已发布的文章 不含私密文章
        content.setPersonal("0");

        PageInfo<UserContent> page = userContentService.findAll(content, pageNum, pageSize); //分页
        model.addAttribute("page", page);

        //查询未公开
        personalContent.setPersonal("1");
        PageInfo<UserContent> personalPage = userContentService.findAll(personalContent, pageNum, pageSize);
        model.addAttribute("personalPage", personalPage);

        //查询未通过
        PageInfo<UserContent> refusePage = userContentService.findRefuseContent(user.getId(), pageNum, pageSize);
        model.addAttribute("refusePage", refusePage);

        //查询消息
        List<LeaveMessage> readList = leaveMessageService.findRead(user.getId());
        List<LeaveMessage> unReadList = leaveMessageService.findUnRead(user.getId());
        model.addAttribute("readList", readList);
        model.addAttribute("unReadList", unReadList);

        return "/personal/person";
    }

    @RequestMapping("/archives")
    public String toPageArchives(HttpServletResponse response,
                                 HttpServletRequest request) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String archive = request.getParameter("archive");

        if (!StringUtils.isEmpty(archive)) {
            response.setHeader("archive", archive);
        }
        return "/archives";
    }

    @RequestMapping("/categories")
    public String toPageCategories(HttpServletResponse response,
                                   HttpServletRequest request) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String category = request.getParameter("category");

        if (!StringUtils.isEmpty(category)) {
            response.setHeader("category", TransCodingUtil.stringToUnicode(category));
        }
        return "/categories";
    }

    /**
     * 跳转到标签页
     */
    @RequestMapping("/tags")
    public String toPageTags(HttpServletResponse response,
                             HttpServletRequest request) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String tag = request.getParameter("tag");

        if (!StringUtils.isEmpty(tag)) {
            response.setHeader("tag", TransCodingUtil.stringToUnicode(tag));
        }
        return "/tags";
    }

    @RequestMapping("/admin")
    public String toPageAdmin() {
        return "/admin/superadmin";
    }

    @RequestMapping("/friendlylink")
    public String toPageFriendlyLink() {
        return "/friendlyLink";
    }

    @RequestMapping("/header")
    public String toPageHeader() {
        return "/header";
    }

    @RequestMapping("/footer")
    public String toPageFooter() {
        return "/footer";
    }

}
