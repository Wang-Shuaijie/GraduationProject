package com.wangshjm.blog.controller;

import com.google.code.kaptcha.Constants;
import com.wangshjm.blog.constant.ConstantsValue;
import com.wangshjm.blog.entity.User;
import com.wangshjm.blog.service.UserService;
import com.wangshjm.blog.utils.MD5Util;
import com.wangshjm.blog.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
public class RegisterControlller extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/doRegister")
    @Transactional(rollbackFor = {Exception.class})
    public String doRegister(Model model, @RequestParam(value = "email", required = false) String email,
                             @RequestParam(value = "password", required = false) String password,
                             @RequestParam(value = "phone", required = false) String phone,
                             @RequestParam(value = "nickName", required = false) String nickname,
                             @RequestParam(value = "code", required = false) String code) {
        User user = new User();
        user.setNickName(nickname);
        user.setPassword(MD5Util.encodeToHex(ConstantsValue.SALT + password));
        user.setPhone(phone);
        user.setEmail(email);
        user.setState("0");
        user.setEnable("0");
        //默认头像
        user.setImgUrl("/images/icon_m.jpg");
        //生成邮件激活码
        String validateCode = MD5Util.encodeToHex(ConstantsValue.SALT + email + password);
        redisTemplate.opsForValue().set(email, validateCode, 24, TimeUnit.HOURS);// 24小时 有效激活 redis保存激活码
        try {
            userService.regist(user);
        } catch (Exception e) {
            log.error("注册出错:{}", e.getMessage());
            model.addAttribute("error", "注册出现了问题，稍后重试！");
            return "/register";
        }
        //发送激活邮件
        MailUtil.sendEmailMessage(email, validateCode);
        String message = email + "," + validateCode;
        model.addAttribute("message", message);
        return "/regist/registerSuccess";

    }

    /**
     * 判断手机号是否已经被注册
     *
     * @param model
     * @param phone
     * @return
     */
    @RequestMapping("/checkPhone")
    @ResponseBody
    public Map<String, Object> checkPhone(Model model, @RequestParam(value = "phone", required = false) String phone) {
        Map map = new HashMap<String, Object>();
        User user = userService.findByPhone(phone);
        if (user == null) {
            //未注册
            map.put("message", "success");
        } else {
            //已注册
            map.put("message", "fail");
        }
        return map;
    }


    /**
     * 判断邮箱是否可用
     *
     * @param model
     * @param email
     * @return
     */
    @RequestMapping("/checkEmail")
    @ResponseBody
    public Map<String, Object> checkEmail(Model model, @RequestParam(value = "email", required = false) String email) {
        Map map = new HashMap<String, Object>();
        User user = userService.findByEmail(email);
        if (user == null) {
            //未注册
            map.put("message", "success");
        } else {
            //已注册
            map.put("message", "fail");
        }
        return map;
    }

    /**
     * 账号是否激活
     *
     * @param model
     * @return
     */
    @RequestMapping("/activecode")
    public String active(Model model) {
        //判断激活码有无过期 是否正确
        String validateCode = getRequest().getParameter("validateCode");
        String email = getRequest().getParameter("email");
        String code = redisTemplate.opsForValue().get(email);
        log.info("验证邮箱为：" + email + ",邮箱激活码为：" + code + ",用户链接的激活码为：" + validateCode);
        //判断是否已激活
        User userTrue = userService.findByEmail(email);
        if (userTrue != null && "1".equals(userTrue.getState())) {
            //已激活
            model.addAttribute("success", "您已激活,请直接登录！");
            return "/login";
        }
        if (code == null) {
            //激活码过期
            model.addAttribute("fail", "您的激活码已过期,请重新注册！");
            userService.deleteByEmail(email);
            return "/regist/activeFail";
        }

        if (!StringUtils.isEmpty(validateCode) && validateCode.equals(code)) {
            //激活码正确
            userTrue.setEnable("1");
            userTrue.setState("1");
            userService.update(userTrue);
            model.addAttribute("email", email);
            return "/regist/activeSuccess";
        } else {
            //激活码错误
            model.addAttribute("fail", "您的激活码错误,请重新激活！");
            return "/regist/activeFail";
        }
    }

    @RequestMapping("/register")
    public String register(Model model) {
        log.info("进入注册页面");
        return "/register";
    }

}
