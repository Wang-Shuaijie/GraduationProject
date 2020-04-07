package com.wangshjm.blog.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.entity.Category;
import com.wangshjm.blog.entity.FriendLink;
import com.wangshjm.blog.entity.Tag;
import com.wangshjm.blog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SuperAdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserContentService userContentService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private FeedBackService feedBackService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FriendLinkService friendLinkService;

    /**
     * 获得所有评论
     *
     * @return
     */
    @PostMapping(value = "/getAllComment")
    public JSONObject getAllComment(@RequestParam("rows") String rows,
                                    @RequestParam("pageNum") String pageNum) {
        JSONObject resultJson = new JSONObject();
        PageInfo pageInfo = commentService.findAllComment(Integer.parseInt(pageNum), Integer.parseInt(rows));
        resultJson.put("pageInfo", pageInfo);
        resultJson.put("result", pageInfo.getList());
        return resultJson;
    }

    /**
     * 获得所有用户
     *
     * @return
     */
    @PostMapping(value = "/getAllUser")
    public JSONObject getAllUser(@RequestParam("rows") String rows,
                                    @RequestParam("pageNum") String pageNum) {
        JSONObject resultJson = new JSONObject();
        PageInfo pageInfo = userService.findAllUser(Integer.parseInt(pageNum), Integer.parseInt(rows));
        resultJson.put("pageInfo", pageInfo);
        resultJson.put("result", pageInfo.getList());
        return resultJson;
    }

    /**
     * 分页获得所有反馈信息
     *
     * @param rows    一页大小
     * @param pageNum 当前页
     */
    @GetMapping(value = "/getAllFeedback")
    public JSONObject getAllFeedback(@RequestParam("rows") String rows,
                                     @RequestParam("pageNum") String pageNum) {
        JSONObject resultJson = new JSONObject();
        PageInfo pageInfo = feedBackService.getAllFeedBack(Integer.parseInt(pageNum), Integer.parseInt(rows));
        resultJson.put("pageInfo", pageInfo);
        resultJson.put("result", pageInfo.getList());
        return resultJson;
    }

    /**
     * 分页获得所有反馈信息
     *
     * @param rows    一页大小
     * @param pageNum 当前页
     */
    @GetMapping(value = "/getAllNotice")
    public JSONObject getAllNotice(@RequestParam("rows") String rows,
                                     @RequestParam("pageNum") String pageNum) {
        JSONObject resultJson = new JSONObject();
        PageInfo pageInfo = noticeService.getAllNotice(Integer.parseInt(pageNum), Integer.parseInt(rows));
        resultJson.put("pageInfo", pageInfo);
        resultJson.put("result", pageInfo.getList());
        return resultJson;
    }

    /**
     * 获得统计信息
     *
     * @return
     */
    @GetMapping(value = "/getStatisticsInfo")
    public JSONObject getStatisticsInfo() {
        JSONObject returnJson = new JSONObject();
        returnJson.put("allUser", userService.countUserNum());
        returnJson.put("articleNum", userContentService.countArticle());
        return returnJson;
    }

    /**
     * 获得所有分类
     */
    @GetMapping(value = "/getArticleCategories")
    public JSONObject getArticleCategories() {
        List<Category> categoryList = categoryService.findAll();
        JSONObject returnJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for(Category c : categoryList){
            jsonObject = new JSONObject();
            jsonObject.put("id", c.getId());
            jsonObject.put("categoryName", c.getName());
            jsonArray.add(jsonObject);
        }

        returnJson.put("result", jsonArray);
        return returnJson;
    }

    /**
     * 获得所有tag
     */
    @GetMapping(value = "/getArticleTags")
    public JSONObject getArticleTags() {
        List<Tag> tagList = tagService.findAll();
        JSONObject returnJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for(Tag c : tagList){
            jsonObject = new JSONObject();
            jsonObject.put("id", c.getId());
            jsonObject.put("tagName", c.getName());
            jsonArray.add(jsonObject);
        }

        returnJson.put("result", jsonArray);
        return returnJson;
    }

    /**
     * 添加或删除分类
     */
    @PostMapping(value = "/updateCategory")
    public JSONObject updateCategory(@RequestParam("categoryName") String categoryName,
                                     @RequestParam("type") int type) {
        //DataMap data = categoryService.updateCategory(categoryName, type);
        return null;
    }

    /**
     * 获得友链
     */
    @PostMapping(value = "/getFriendLink")
    public JSONObject getFriendLink() {
        JSONObject resultJson = new JSONObject();
        List<FriendLink> friendLinkList = friendLinkService.findAllFriendLink();
        resultJson.put("data", friendLinkList);
        return resultJson;
    }

    /**
     * 添加或编辑友链
     */
    @PostMapping(value = "/addFriendLink")
    public JSONObject addFriendLink(@RequestParam("id") String id,
                                    @RequestParam("blogger") String blogger,
                                    @RequestParam("url") String url) {
//        FriendLink friendLink = new FriendLink(blogger, url);
//        DataMap data;
//        if(StringUtil.BLANK.equals(id)){
//            data = friendLinkService.addFriendLink(friendLink);
//        } else {
//            data = friendLinkService.updateFriendLink(friendLink, Integer.parseInt(id));
//        }
        return null;
    }

    /**
     * 删除友链
     */
    @PostMapping(value = "/deleteFriendLink")
    public JSONObject deleteFriendLink(@RequestParam("id") int id) {
        //DataMap data = friendLinkService.deleteFriendLink(id);
        return null;
    }

}
