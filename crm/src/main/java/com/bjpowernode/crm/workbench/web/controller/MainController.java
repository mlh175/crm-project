package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author:马立皓
 * @time:22:03 2022/6/27
 */
@Controller
public class MainController {
    @RequestMapping("/workbench/main/index.do")
    public String index(){
        //跳转到main/index.jsp页面
        return "workbench/main/index";
    }
}
