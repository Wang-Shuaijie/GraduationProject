package com.wangshjm.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.service.TagService;
import com.wangshjm.blog.service.UserContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TagController {
    @Autowired
    private TagService tagService;
    @Autowired
    private UserContentService userContentService;

    /**
     * 分页获得该标签下的文章
     * @param tag
     * @return
     */
    @PostMapping(value = "/getTagArticle")
    public JSONObject getTagArticle(@RequestParam("tag") String tag,
                                    HttpServletRequest request){
        JSONObject result = new JSONObject();
        if(StringUtils.isEmpty(tag)){
            return tagService.findTags();
        }

        int rows = Integer.parseInt(request.getParameter("rows"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        PageInfo pageInfo = userContentService.findByTag(tag, pageNum, rows);
        result.put("pageInfo", pageInfo);
        result.put("result", pageInfo.getList());
        result.put("tag", tag);
        return result;
    }


}
