package com.bjpowernode.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *对Date类型数据进行处理的工具类
 */
public class DateUtils {
    /**
     * 对指定date对象进行格式化
     */
    public static  String formateDateTime(Date date){
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sf.format(date);
        return format;
    }
}
