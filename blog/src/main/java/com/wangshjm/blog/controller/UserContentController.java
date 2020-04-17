package com.wangshjm.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserContentController {
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

    @PostMapping("/getArticleManagement")
    public JSONObject getArticleManagement(@RequestParam("rows") String rows,
                                           @RequestParam("pageNum") String pageNum) {
        JSONObject result = new JSONObject();
        PageInfo pageInfo = userContentService.findAll(null, Integer.parseInt(pageNum), Integer.parseInt(rows));
        result.put("pageInfo", pageInfo);
        result.put("result", pageInfo.getList());
        return result;
    }

    /**
     * 删除文章
     *
     * @param id 文章id
     */
    @GetMapping(value = "/deleteArticle")
    public JSONObject deleteArticle(@RequestParam("id") String id) {
        JSONObject result = new JSONObject();
        try {
            commentService.deleteByContentId(Long.parseLong(id));
            upvoteService.deleteByContentId(Long.parseLong(id));
            userContentService.deleteById(Long.parseLong(id));
            esDataService.delete(id);
            result.put("result", 0);
        } catch (Exception e) {
            log.debug(e.getMessage());
            result.put("result", 1);
        }
        return result;


    }
}
