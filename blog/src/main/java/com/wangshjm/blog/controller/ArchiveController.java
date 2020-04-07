package com.wangshjm.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.service.ArchiveService;
import com.wangshjm.blog.service.UserContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ArchiveController {
    @Autowired
    private ArchiveService archiveService;
    @Autowired
    private UserContentService userContentService;

    /**
     * 获得所有归档日期以及每个归档日期的文章数目
     * @return
     */
    @GetMapping(value = "/findArchiveNameAndArticleNum")
    public JSONObject findArchiveNameAndArticleNum(){
        JSONObject resultJson = archiveService.findArchiveNameAndArticleNum();
        return resultJson;
    }

    /**
     * 分页获得该归档日期下的文章
     */
    @GetMapping(value = "/getArchiveArticle")
    public JSONObject getArchiveArticle(@RequestParam("archive") String archive,
                                    HttpServletRequest request){
        JSONObject result = new JSONObject();
        int rows = Integer.parseInt(request.getParameter("rows"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        PageInfo pageInfo = userContentService.findByArchive(archive, pageNum, rows);
        result.put("pageInfo", pageInfo);
        result.put("result", pageInfo.getList());
        result.put("archive", archive);
        return result;
    }
}
