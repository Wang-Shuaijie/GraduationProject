package com.wangshjm.blog.controller;

import com.wangshjm.blog.entity.Category;
import com.wangshjm.blog.entity.User;
import com.wangshjm.blog.entity.UserContent;
import com.wangshjm.blog.service.CategoryService;
import com.wangshjm.blog.service.EsDataService;
import com.wangshjm.blog.service.UserContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
@Slf4j
public class WriteController extends BaseController {
    @Autowired
    private UserContentService userContentService;
    @Autowired
    private EsDataService esDataService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/write")
    public String write(Model model, @RequestParam(value = "cid", required = false) Long cid,
                        @RequestParam(value = "style", required = false) String style) {
        User user = getCurrentUser();
        List<Category> categoryList = categoryService.findAll();
        if (!ObjectUtils.isEmpty(cid)) {
            UserContent userContent = userContentService.findById(cid);
            model.addAttribute("article", userContent);
        } else {
            model.addAttribute("article", null);
        }
        model.addAttribute("user", user);
        model.addAttribute("categoryList", categoryList);
        //富文本编辑器或markdown编辑器
        if ("1".equals(style)) {
            return "/write/editormd";
        } else {
            return "/write/kindeditor";
        }
    }

    @RequestMapping("/doWrite")
    public String doWrite(Model model, @RequestParam(value = "cid", required = false) Long cid,
                          @RequestParam(value = "tagName", required = false) String tagName,
                          @RequestParam(value = "category", required = false) String categoryName,
                          @RequestParam(value = "title", required = false) String title,
                          @RequestParam(value = "content", required = false) String kindeditor,
                          @RequestParam(value = "html", required = false) String mdeditorHtml,
                          @RequestParam(value = "doc", required = false) String mdeditorDoc,
                          @RequestParam(value = "private", required = false) String personal) {
        User user = getCurrentUser();
        UserContent userContent = new UserContent();
        if (!ObjectUtils.isEmpty(cid)) {
            userContent = userContentService.findById(cid);
        }
        userContent.setTag(tagName);
        userContent.setCategory(categoryName);
        Category category = categoryService.findByCategoryName(categoryName);
        userContent.setCategoryId(category.getId());

        if (StringUtils.isEmpty(kindeditor)) {
            userContent.setContent(mdeditorHtml);
        } else {
            userContent.setContent(kindeditor);
        }
        if (!StringUtils.isEmpty(mdeditorDoc)) {
            userContent.setMarkdownContent(mdeditorDoc);
        }

        String imgUrl = user.getImgUrl();
        if (StringUtils.isEmpty(imgUrl)) {
            userContent.setImgUrl("/images/icon_m.jpg");
        } else {
            userContent.setImgUrl(imgUrl);
        }
        if ("on".equals(personal)) {
            userContent.setPersonal("1");
        } else {
            userContent.setPersonal("0");
        }
        userContent.setTitle(title);
        userContent.setUId(user.getId());
        userContent.setNickName(user.getNickName());

        if (cid == null) {
            userContent.setUpvote(0);
            userContent.setDownvote(0);
            userContent.setCommentNum(0);
            userContent.setRptTime(new Date());
            userContentService.addContent(userContent);
            esDataService.saveOrUpdate(userContent);

        } else {
            userContentService.update(userContent);
            esDataService.saveOrUpdate(userContent);
        }
        model.addAttribute("article", userContent);
        return "/write/writesuccess";
    }

}
