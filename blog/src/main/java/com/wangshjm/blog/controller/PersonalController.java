package com.wangshjm.blog.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wangshjm.blog.constant.ConstantsValue;
import com.wangshjm.blog.entity.User;
import com.wangshjm.blog.entity.UserContent;
import com.wangshjm.blog.entity.UserInfo;
import com.wangshjm.blog.service.*;
import com.wangshjm.blog.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String findList(Model model, @RequestParam(value = "pageNum", required = false) Integer pageNum,
                           @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (ObjectUtils.isEmpty(pageNum) && ObjectUtils.isEmpty(pageSize)) {
            pageNum = 1;
            pageSize = 10;
        }
        User user = getCurrentUser();
        model.addAttribute("user", user);
        UserInfo userInfo = userInfoService.findByUid(user.getId());
        model.addAttribute("userInfo", userInfo);
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
            pageSize = 10;
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
     * 根据用户id查询未公开
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
        if (ObjectUtils.isEmpty(pageSize)) {
            pageSize = 10;
        }
        UserContent userContent = new UserContent();
        userContent.setPersonal("1");
        userContent.setUId(user.getId());
        PageInfo<UserContent> page = userContentService.findAll(userContent, pageNum, pageSize);
        map.put("pagePersonal", page);
        return map;
    }


    /**
     * 查询未通过
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/findRefuse")
    @ResponseBody
    public Map<String, Object> findHot(@RequestParam(value = "pageNum", required = false) Integer pageNum, @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        Map map = new HashMap<String, Object>();
        User user = getCurrentUser();
        if (ObjectUtils.isEmpty(pageSize)) {
            pageSize = 10;
        }
        PageInfo<UserContent> refusePage = userContentService.findRefuseContent(user.getId(), pageNum, pageSize);
        map.put("pageRefuse", refusePage);
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
        return "redirect:/personal";
    }


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
                               @RequestParam(value = "nickName", required = false) String nickName,
                               @RequestParam(value = "sex", required = false) String sex,
                               @RequestParam(value = "education", required = false) String education,
                               @RequestParam(value = "address", required = false) String address,
                               @RequestParam(value = "birthday", required = false) String birthday,
                               @RequestParam(value = "work", required = false) String work,
                               @RequestParam(value = "hobby", required = false) String hobby,
                               @RequestParam(value = "information", required = false) String information){
        User user = getCurrentUser();
        UserInfo userInfo = userInfoService.findByUid(user.getId());
        boolean flag = false;
        if (userInfo == null) {
            userInfo = new UserInfo();
        } else {
            flag = true;
        }
        userInfo.setName(name);
        userInfo.setWork(work);
        userInfo.setEducation(education);
        userInfo.setHobby(hobby);
        userInfo.setAddress(address);
        userInfo.setSex(sex);
        userInfo.setInformation(information);
        userInfo.setBirthday(birthday);
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
        return "redirect:/personal";
    }

    /**
     * 修改密码
     *
     */
    @GetMapping("/updatePassword")
    @ResponseBody //返回json对象必须需要，除非使用@RestController
    public JSONObject updatePassword(@RequestParam(value = "id") String id,
                                     @RequestParam(value = "oldPassword") String oldPassword,
                                     @RequestParam(value = "newPassword") String newPassword) {
        JSONObject result = new JSONObject();
        User user = userService.findById(Long.parseLong(id));
        oldPassword = MD5Util.encodeToHex(ConstantsValue.SALT + oldPassword);
        if (user.getPassword().equals(oldPassword)) {
            newPassword = MD5Util.encodeToHex(ConstantsValue.SALT + newPassword);
            user.setPassword(newPassword);
            userService.update(user);
            result.put("status", 1);
        } else {
            result.put("status", 0);
        }

        return result;
    }

    @GetMapping("/updateEmail")
    @ResponseBody //返回json对象必须需要，除非使用@RestController
    public JSONObject updateEmail(@RequestParam(value = "id") String id,
                                     @RequestParam(value = "email") String email) {
        JSONObject result = new JSONObject();
        User user = userService.findById(Long.parseLong(id));
        try{
            user.setEmail(email);
            userService.update(user);
            result.put("status", 1);
        }catch (Exception e){
            result.put("status", 0);
        }
        return result;
    }



    @GetMapping("/updatePhone")
    @ResponseBody //返回json对象必须需要，除非使用@RestController
    public JSONObject updatePhone(@RequestParam(value = "id") String id,
                                  @RequestParam(value = "phone") String phone) {
        JSONObject result = new JSONObject();
        User user = userService.findById(Long.parseLong(id));
        try{
            user.setPhone(phone);
            //TODO 后期实现获取手机验证码才能更改手机号
            userService.update(user);
            result.put("status", 1);
        }catch (Exception e){
            result.put("status", 0);
        }
        return result;
    }

}
