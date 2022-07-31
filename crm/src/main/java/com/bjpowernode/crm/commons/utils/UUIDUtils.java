package com.bjpowernode.crm.commons.utils;

import java.util.UUID;

/**
 * @author:马立皓
 * @time:8:35 2022/6/29
 */
public class UUIDUtils {
    /**
     * 获取UUID的值
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
