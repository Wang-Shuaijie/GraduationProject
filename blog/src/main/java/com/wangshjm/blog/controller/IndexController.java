package com.wangshjm.blog.controller;

import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.entity.Comment;
import com.wangshjm.blog.entity.Upvote;
import com.wangshjm.blog.entity.User;
import com.wangshjm.blog.entity.UserContent;
import com.wangshjm.blog.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    /**
     * 进入首页
     *
     * @param model
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model, @RequestParam(value = "keyword", required = false) String keyword,
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
            PageInfo<UserContent> page = userContentService.findAll(null, pageNum, pageSize);
            model.addAttribute("page", page);
        }
        return "/index";
    }

    @RequestMapping("/watch")
    public String watchArticle(Model model, @RequestParam(value = "cid", required = false) Long cid) {
        User user = getCurrentUser();
        UserContent userContent = userContentService.findById(cid);
        model.addAttribute("article", userContent);
        model.addAttribute("user", user);
        return "/watch";
    }

    /**
     * 点赞或踩
     *
     * @param id
     * @param uid
     * @param upvote 1:点赞; -1:点踩
     * @return
     */
    @RequestMapping("/upvote")
    @ResponseBody
    public Map<String, Object> upvote(@RequestParam(value = "id", required = false) long id,
                                      @RequestParam(value = "uid", required = false) Long uid,
                                      @RequestParam(value = "upvote", required = false) int upvote) {
        Map<String, Object> map = new HashMap<String, Object>();
        User user = getCurrentUser();
        if (ObjectUtils.isEmpty(user)) {
            map.put("data", "fail");
            return map;
        }
        Upvote up = new Upvote();
        up.setContentId(id);
        up.setUId(user.getId());

        Upvote upv = upvoteService.findByUidAndConId(up);
        UserContent userContent = userContentService.findById(id);
        if (upvote == -1) {
            if (!ObjectUtils.isEmpty(upv)) {
                //一个人只能点一次赞或踩
                if ("1".equals(upv.getDownvote())) {
                    map.put("data", "down");
                    return map;
                } else {
                    upv.setDownvote("1");
                    upv.setUpvoteTime(new Date());
                    upv.setIp(getClientIpAddress());
                    upvoteService.update(upv);
                }
            } else {
                up.setDownvote("1");
                up.setUpvoteTime(new Date());
                up.setIp(getClientIpAddress());
                upvoteService.add(up);
            }
            userContent.setUpvote(userContent.getDownvote() + upvote);
        } else {
            if (!ObjectUtils.isEmpty(upv)) {
                if ("1".equals(upv.getUpvote())) {
                    map.put("data", "done");
                    return map;
                } else {
                    upv.setUpvote("1");
                    upv.setUpvoteTime(new Date());
                    upv.setIp(getClientIpAddress());
                    upvoteService.update(upv);
                }
            } else {
                up.setUpvote("1");
                up.setUpvoteTime(new Date());
                up.setIp(getClientIpAddress());
                upvoteService.add(up);
            }
            userContent.setUpvote(userContent.getUpvote() + upvote);
        }

        userContentService.update(userContent);
        map.put("data", "success");
        return map;
    }


    /**
     * 点击展开列表
     *
     * @param model
     * @param content_id
     * @return
     */
    @RequestMapping("/reply")
    @ResponseBody
    public Map<String, Object> reply(Model model, @RequestParam(value = "content_id", required = false) Long content_id) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Comment> list = commentService.findAllFirstComment(content_id);
        if (!CollectionUtils.isEmpty(list)) {
            for (Comment c : list) {
                List<Comment> coments = commentService.findAllChildrenComment(c.getConId(), c.getChildren());
                if (!CollectionUtils.isEmpty(coments)) {
                    for (Comment com : coments) {
                        if (com.getById() != null) {
                            User byUser = userService.findById(com.getById());
                            com.setByUser(byUser);
                        }
                    }
                    c.setComList(coments);
                }
            }
        }
        map.put("list", list);
        return map;
    }


    /**
     * 点击评论按钮
     *
     * @param id
     * @param content_id
     * @param uid
     * @param bid
     * @param oSize
     * @param comment_time
     * @param upvote
     * @return
     */
    @RequestMapping("/comment")
    @ResponseBody
    public Map<String, Object> comment(@RequestParam(value = "id", required = false) Long id,
                                       @RequestParam(value = "content_id", required = false) Long content_id,
                                       @RequestParam(value = "uid", required = false) Long uid,
                                       @RequestParam(value = "by_id", required = false) Long bid,
                                       @RequestParam(value = "oSize", required = false) String oSize,
                                       @RequestParam(value = "comment_time", required = false) String comment_time,
                                       @RequestParam(value = "upvote", required = false) Integer upvote) throws ParseException {
        Map<String, Object> map = new HashMap<String, Object>();

        if (ObjectUtils.isEmpty(id)) {
            Comment comment = new Comment();
            processComment(comment, content_id, uid, bid, oSize, comment_time, upvote);
            map.put("data", comment);

            UserContent userContent = userContentService.findById(content_id);
            Integer num = userContent.getCommentNum();
            userContent.setCommentNum(num + 1);
            userContentService.update(userContent);
        } else {
            //点赞
            Comment c = commentService.findById(id);
            c.setUpvote(upvote);
            commentService.update(c);
        }
        return map;
    }


    /**
     * 点击一级评论块的评论按钮
     *
     * @param id
     * @param content_id
     * @param uid
     * @param bid
     * @param oSize
     * @param comment_time
     * @param upvote
     * @return
     */
    @RequestMapping("/comment_child")
    @ResponseBody
    public Map<String, Object> addCommentChild(@RequestParam(value = "id", required = false) Long id,
                                               @RequestParam(value = "content_id", required = false) Long content_id,
                                               @RequestParam(value = "uid", required = false) Long uid,
                                               @RequestParam(value = "by_id", required = false) Long bid,
                                               @RequestParam(value = "oSize", required = false) String oSize,
                                               @RequestParam(value = "comment_time", required = false) String comment_time,
                                               @RequestParam(value = "upvote", required = false) Integer upvote) throws ParseException {
        Map map = new HashMap<String, Object>();


        Comment comment = new Comment();
        processComment(comment, content_id, uid, bid, oSize, comment_time, upvote);
        //System.out.println(comment.getId());

        Comment com = commentService.findById(id);
        if (StringUtils.isEmpty(com.getChildren())) {
            com.setChildren(comment.getId().toString());
        } else {
            com.setChildren(com.getChildren() + "," + comment.getId());
        }
        commentService.update(com);
        map.put("data", comment);

        UserContent userContent = userContentService.findById(content_id);
        Integer num = userContent.getCommentNum();
        userContent.setCommentNum(num + 1);
        userContentService.update(userContent);
        return map;

    }

    private void processComment(Comment comment, Long content_id, Long uid, Long bid, String oSize, String comment_time, Integer upvote) throws ParseException {
        Date date = DateUtils.parseDate(comment_time, "yyyy-MM-dd HH:mm:ss");
        comment.setComContent(oSize);
        comment.setCommTime(date);
        comment.setConId(content_id);
        comment.setComId(uid);
        if (upvote == null) {
            upvote = 0;
        }
        comment.setById(bid);
        comment.setUpvote(upvote);

        User u = userService.findById(uid);

        comment.setUser(u);
        commentService.add(comment);
    }

    /**
     * 删除评论
     *
     * @param id
     * @param uid
     * @param con_id
     * @param fid
     * @return
     */
    @RequestMapping("/deleteComment")
    @ResponseBody
    public Map<String, Object> deleteComment(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "uid", required = false) Long uid,
                                             @RequestParam(value = "con_id", required = false) Long con_id, @RequestParam(value = "fid", required = false) Long fid) {
        int num = 0;
        Map map = new HashMap<String, Object>();
        User user = getCurrentUser();
        if (ObjectUtils.isEmpty(user)) {
            map.put("data", "fail");
            return map;
        }

        if (user.getId().equals(uid)) {
            Comment comment = commentService.findById(id);
            if (StringUtils.isEmpty(comment.getChildren())) {
                deleteChildComment(id, fid, num);
            } else {
                deleteFirstComment(comment, id, num);
            }

            updateCommentNum(map, con_id, num);
        } else {
            map.put("data", "no-access");
        }

        return map;
    }

    //删除子评论
    private int deleteChildComment(Long id, Long fid, int num) {
        if (ObjectUtils.isEmpty(fid)) {
            return num;
        }
        //去除id
        Comment fcomm = commentService.findById(fid);
        String child = getString(fcomm.getChildren(), id);
        fcomm.setChildren(child);
        commentService.update(fcomm);
        commentService.deleteById(id);
        num = num + 1;
        return num;
    }

    //删除父评论
    private int deleteFirstComment(Comment comment, Long id, int num) {
        String children = comment.getChildren();
        commentService.deleteChildrenComment(children);
        String[] arr = children.split(",");
        commentService.deleteById(id);
        num = num + arr.length + 1;
        return num;
    }

    private Map<String, Object> updateCommentNum(Map<String, Object> map, Long con_id, int num) {
        UserContent content = userContentService.findById(con_id);
        if (!ObjectUtils.isEmpty(content)) {
            if (content.getCommentNum() - num >= 0) {
                content.setCommentNum(content.getCommentNum() - num);
            } else {
                content.setCommentNum(0);
            }
            userContentService.update(content);
            map.put("data", content.getCommentNum());
        }

        return map;
    }

    private String getString(String str, Long id) {
        String[] arr = str.split(",");
        String s = "";
        if (arr != null && arr.length > 0) {
            for (int i = 0; i < arr.length; i++) {
                System.out.println(id.toString().equals(arr[i]));
                if (id.toString().equals(arr[i])) {
                    //System.out.println(arr[i]);
                    continue;
                } else {
                    if (i == arr.length - 1) {
                        s = s + arr[i];
                    } else {
                        s = s + arr[i] + ",";
                    }
                }


            }
        }
        if (s.endsWith(",")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
