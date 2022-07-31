package com.bjpowernode.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author:马立皓
 * @time:22:42 2022/6/22
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public String index(){

        return "index";
    }
}
