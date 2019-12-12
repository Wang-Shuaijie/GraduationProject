package com.wangshjm.blog.utils;

public class TestUtil {
    public static void main(String args[]) throws Exception {
        String email = "2283613941@qq.com";
        String validateCode = "邮件发送测试";
        MailUtil.sendEmailMessage(email, validateCode);
    }
}
