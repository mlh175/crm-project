package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:马立皓
 * @time:19:33 2022/6/23
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;
    /**
     * url要和controller方法处理玩请求之后，响应信息返回的页面的资源目录一致
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request,HttpServletResponse response){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        //调用service层方法，查询用户
        User user = userService.queryUserByLoginActAndPwd(map);
        //根据查询结果，生成响应信息
        ReturnObject returnObject=new ReturnObject();
        if (user==null) {
            //登陆失败，用户名或密码错误
            returnObject.setCode("0");
            returnObject.setMessage("用户名或密码错误");
        }else {
            //进一步判断账号是否合法
            //user.getExpireTime()
            //new Date()
            String dateTime = DateUtils.formateDateTime(new Date());
            if (dateTime.compareTo(user.getExpireTime())>0) {
                //登陆失败，账号已过期
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            }else if("0".equals(user.getLockState())){
                //登陆失败，状态已经锁定
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态已经锁定 ");        //获取远程ip
            }else if (!user.getAllowIps().contains(request.getRemoteAddr())){
                //登陆失败，ip受限
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");
            }else {
                //登陆成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                //把user放在session中
                request.getSession().setAttribute(Contants.SESSION_USER,user);
                //如果需要记住密码，则往外写cookie
                if ("true".equals(isRemPwd)) {
                    Cookie cookie = new Cookie("loginAct", user.getLoginAct());
                    cookie.setMaxAge(10*24*36*36);
                    response.addCookie(cookie);
                    Cookie cookie1 = new Cookie("loginPwd", user.getLoginPwd());
                    cookie1.setMaxAge(10*24*36*36);
                    response.addCookie(cookie1);
                }else {
                    Cookie cookie = new Cookie("loginAct", "1");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    Cookie cookie1 = new Cookie("loginPwd", "1");
                    cookie1.setMaxAge(0);
                    response.addCookie(cookie1);
                }
            }
        }

            return returnObject;
    }


    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        //销毁cookie
        Cookie cookie = new Cookie("loginAct", "1");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        Cookie cookie1 = new Cookie("loginPwd", "1");
        cookie1.setMaxAge(0);
        response.addCookie(cookie1);
        //清空session
        request.getSession(false).invalidate();
        //跳转页面
        return "redirect:/";
    }


}
