package com.wangshjm.blog.controller;

import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.constant.ConstantsValue;
import com.wangshjm.blog.entity.User;
import com.wangshjm.blog.entity.UserContent;
import com.wangshjm.blog.entity.UserInfo;
import com.wangshjm.blog.service.*;
import com.wangshjm.blog.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class PersonalController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserContentService userContentService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UpvoteService upvoteService;
    @Autowired
    private EsDataService esDataService;

    /**
     * 初始化个人主页
     *
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public String findList(Model model, @RequestParam(value = "manage", required = false) String manage,
                           @RequestParam(value = "pageNum", required = false) Integer pageNum,
                           @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (ObjectUtils.isEmpty(pageNum) && ObjectUtils.isEmpty(pageSize)) {
            pageNum = 1;
            pageSize = 7;
        }
        User user = getCurrentUser();
        model.addAttribute("user", user);
        UserInfo userInfo = userInfoService.findByUid(user.getId());
        model.addAttribute("userInfo", userInfo);
        if (!StringUtils.isEmpty(manage)) {
            model.addAttribute("manage", manage);
        }
        UserContent content = new UserContent();
        UserContent personalContent = new UserContent();
        if (user != null) {
            content.setUId(user.getId());
            personalContent.setUId(user.getId());
        } else {
            return "/login";
        }
        //查询文章分类
        List<UserContent> ArticleCategorys = userContentService.findCategoryByUid(user.getId());
        model.addAttribute("categorys", ArticleCategorys);
        //发布的文章 不含私密文章
        content.setPersonal("0");
        pageSize = 7; //默认每页显示7条数据
        PageInfo<UserContent> page = userContentService.findAll(content, pageNum, pageSize); //分页
        model.addAttribute("page", page);

        //查询私密文章
        personalContent.setPersonal("1");
        PageInfo<UserContent> personalPage = userContentService.findAll(personalContent, pageNum, pageSize);
        model.addAttribute("personalPage", personalPage);

        //查询最热文章
        pageSize = 10; //默认每页显示10条数据
        PageInfo<UserContent> hotPage = userContentService.findByUpvote(pageNum, pageSize);
        model.addAttribute("hotPage", hotPage);
        return "/personal/person";
    }

    /**
     * 根据分类名称查询所有文章
     *
     * @param model
     * @param category
     * @return
     */
    @RequestMapping("/findByCategory")
    @ResponseBody
    public Map<String, Object> findByCategory(Model model, @RequestParam(value = "category", required = false) String category, @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        Map map = new HashMap<String, Object>();
        User user = getCurrentUser();
        if (ObjectUtils.isEmpty(pageSize)) {
            pageSize = 7;
        }
        UserContent userContent = new UserContent();
        userContent.setCategory(category);
        userContent.setUId(user.getId());
        userContent.setPersonal("0");
        PageInfo<UserContent> pageCategory = userContentService.findAll(userContent, pageNum, pageSize);
        map.put("pageCategory", pageCategory);
        return map;
    }

    /**
     * 根据用户id查询私人文章
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/findPersonal")
    @ResponseBody
    public Map<String, Object> findPersonal(@RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        Map map = new HashMap<String, Object>();
        User user = getCurrentUser();
        if (user == null) {
            map.put("pagePersonal", "fail");
            return map;
        }
        UserContent userContent = new UserContent();
        userContent.setPersonal("1");
        userContent.setUId(user.getId());
        pageSize = 10; //默认每页显示10条数据
        PageInfo<UserContent> page = userContentService.findAll(userContent, pageNum, pageSize);
        map.put("pagePersonal", page);
        return map;
    }


    /**
     * 查询文章并分页 根据点赞数倒序排列
     *
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/findHot")
    @ResponseBody
    public Map<String, Object> findHot(Model model, @RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        Map map = new HashMap<String, Object>();
        pageSize = 10; //默认每页显示15条数据
        PageInfo<UserContent> hotPage = userContentService.findByUpvote(pageNum, pageSize);
        map.put("hotPage", hotPage);
        return map;
    }

    /**
     * 根据文章id删除文章
     *
     * @param model
     * @param cid
     * @return
     */
    @RequestMapping("/delete")
    public String deleteContent(Model model, @RequestParam(value = "cid", required = false) Long cid) {
        commentService.deleteByContentId(cid);
        upvoteService.deleteByContentId(cid);
        userContentService.deleteById(cid);
        esDataService.delete(String.valueOf(cid));
        return "redirect:/list?manage=manage";
    }

//    /**
//     * 进入个人资料修改页面
//     *
//     * @param model
//     * @return
//     */
//    @RequestMapping("/profile")
//    public String profile(Model model, @RequestParam(value = "email", required = false) String email,
//                          @RequestParam(value = "password", required = false) String password,
//                          @RequestParam(value = "phone", required = false) String phone) {
//        User user = getCurrentUser();
//        if (StringUtils.isEmpty(user.getPassword()) && StringUtils.isEmpty(password)) {
//            return "redirect:/list";
//        }
//
//        if (!StringUtils.isEmpty(email)) {
//            user.setEmail(email);
//            user.setPassword(MD5Util.encodeToHex(ConstantsValue.SALT + password));
//            user.setPhone(phone);
//            userService.update(user);
//        }
//        UserInfo userInfo = userInfoService.findByUid(user.getId());
//        model.addAttribute("user", user);
//        model.addAttribute("userInfo", userInfo);
//
//        return "/personal/profile";
//    }

    /**
     * 保存个人头像
     *
     * @param url
     * @return
     */
    @RequestMapping("/saveImage")
    @ResponseBody
    public Map<String, Object> saveImage(@RequestParam(value = "url", required = false) String url) {
        Map map = new HashMap<String, Object>();
        User user = getCurrentUser();
        user.setImgUrl(url);
        userService.update(user);
        map.put("msg", "success");
        return map;
    }

    /**
     * 保存用户信息
     *
     * @param model
     * @param name
     * @param nickName
     * @param sex
     * @param address
     * @param birthday
     * @return
     */
    @RequestMapping("/saveUserInfo")
    public String saveUserInfo(Model model, @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "nick_name", required = false) String nickName,
                               @RequestParam(value = "sex", required = false) String sex,
                               @RequestParam(value = "address", required = false) String address,
                               @RequestParam(value = "birthday", required = false) String birthday) throws ParseException {
        User user = getCurrentUser();
        UserInfo userInfo = userInfoService.findByUid(user.getId());
        boolean flag = false;
        if (userInfo == null) {
            userInfo = new UserInfo();
        } else {
            flag = true;
        }
        userInfo.setName(name);
        userInfo.setAddress(address);
        userInfo.setSex(sex);
        Date bir = DateUtils.parseDate(birthday, "yyyy-MM-dd");

        userInfo.setBirthday(bir);
        userInfo.setUId(user.getId());
        if (!flag) {
            userInfoService.add(userInfo);
        } else {
            userInfoService.update(userInfo);
        }

        user.setNickName(nickName);
        userService.update(user);

        model.addAttribute("user", user);
        model.addAttribute("userInfo", userInfo);
        return "/personal/profile";
    }

    /**
     * 进入修改密码页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/repassword")
    public String repassword(Model model) {
        User user = getCurrentUser();
        if (user != null) {
            model.addAttribute("user", user);
            return "/personal/repassword";
        }
        return "/login";
    }

    /**
     * 修改密码
     *
     * @param model
     * @param oldPassword
     * @param password
     * @return
     */
    @RequestMapping("/updatePassword")
    public String updatePassword(Model model, @RequestParam(value = "old_password", required = false) String oldPassword,
                                 @RequestParam(value = "password", required = false) String password) {

        User user = getCurrentUser();
        if (user != null) {
            oldPassword = MD5Util.encodeToHex(ConstantsValue.SALT + oldPassword);
            if (user.getPassword().equals(oldPassword)) {
                password = MD5Util.encodeToHex(ConstantsValue.SALT + password);
                user.setPassword(password);
                userService.update(user);
                model.addAttribute("message", "success");
            } else {
                model.addAttribute("message", "fail");
            }
        }
        model.addAttribute("user", user);
        return "/personal/passwordSuccess";
    }
}
