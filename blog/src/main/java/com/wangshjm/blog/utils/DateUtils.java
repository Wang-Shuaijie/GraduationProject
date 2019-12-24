package com.wangshjm.blog.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    /**
     * 将日期字符串根据指定格式转成日期格式
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date StringToDate(String dateStr, String formatStr){
        DateFormat dd=new SimpleDateFormat(formatStr);
        Date date=null;
        try {
            date = dd.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
