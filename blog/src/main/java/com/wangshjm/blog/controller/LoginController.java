package com.wangshjm.blog.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.wangshjm.blog.entity.User;
import com.wangshjm.blog.service.UserService;
import com.wangshjm.blog.utils.RandStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
public class LoginController extends BaseController {
    @Autowired
    private Producer captchaProducer;
    @Autowired
    private UserService userService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping(value = "/captcha")
    public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //用字节数组存储
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        ServletOutputStream responseOutputStream =
                response.getOutputStream();
        final HttpSession httpSession = request.getSession();
        try {
            //生产验证码字符串并保存到session中
            String createText = captchaProducer.createText();
            //打印随机生成的字母和数字
            log.debug(createText);
            httpSession.setAttribute(Constants.KAPTCHA_SESSION_KEY, createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = captchaProducer.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
            captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
            responseOutputStream.write(captchaChallengeAsJpeg);
            responseOutputStream.flush();
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } finally {
            responseOutputStream.close();
        }
    }

    @RequestMapping("/checkCode")
    @ResponseBody
    public Map<String, Object> checkCode(Model model, @RequestParam(value = "code", required = false) String code) {
        Map map = new HashMap<String, Object>();
        String vcode = (String) getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (code.equals(vcode)) {
            map.put("message", "success");
        } else {
            map.put("message", "fail");
        }
        return map;
    }

    @RequestMapping("/login")
    public String register(Model model) {
        User user = getCurrentUser();
        if (user != null) {
            return "/login";
        }
        return "/login";
    }

    /**
     * 发送手机验证码
     *
     * @param model
     * @param telephone
     * @return
     */
    @RequestMapping("/sendSms")
    @ResponseBody
    public Map<String, Object> index(Model model, @RequestParam(value = "telephone", required = false) final String telephone) {
        Map<String, Object> map = new HashMap<>();
        try { //  发送验证码操作
            final String code = RandStringUtils.getCode();
            redisTemplate.opsForValue().set(telephone, code, 60, TimeUnit.SECONDS);// 60秒 有效 redis保存验证码
            // 调用ActiveMQ jmsTemplate，发送一条消息给MQ
            jmsTemplate.send("login_msg", new MessageCreator() {
                public Message createMessage(javax.jms.Session session) throws JMSException {
                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setString("telephone", telephone);
                    mapMessage.setString("code", code);
                    return mapMessage;
                }
            });
        } catch (Exception e) {
            log.debug(e.getMessage());
            map.put("msg", false);
        }
        map.put("msg", true);
        return map;
    }

    /**
     * 登出
     *
     * @param model
     * @return
     */
    @RequestMapping("/loginout")
    public String exit(Model model) {
        log.info("退出登录");
        getSession().removeAttribute("user");
        getSession().invalidate();
        return "/login";
    }

}
