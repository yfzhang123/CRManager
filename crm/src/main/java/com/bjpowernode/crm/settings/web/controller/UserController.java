package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
//lombok提供的日志输出信息
@Slf4j
@Controller
@RequestMapping("/settings/user")
public class UserController {
    @Autowired
    private UserService userService;
//    跳转到登录页面+十天免登陆操作
    /*当用户已经勾选了十天免登陆操作。并进行了登录
    后续用户访问时，将会自动登录，无需停留在登录页面*/
    @RequestMapping("/toLogin.do")
    public String toLogin(HttpServletRequest reques) throws LoginException {
        String loginAct="";
        String loginPwd="";
        Cookie[] cookies=reques.getCookies();
        if(!ObjectUtils.isEmpty(cookies)){
            for (Cookie cookie :
                    cookies) {
              if(cookie.getName().equals("loginAct")){
                  loginAct=cookie.getValue();
                  continue;
              }
              if(cookie.getName().equals("loginPwd")){
                  loginPwd=cookie.getValue();

              }
            }
        }
        //自动登录操作
        if(!StringUtils.isEmpty(loginAct) && !StringUtils.isEmpty(loginPwd)) {
            String ip = reques.getRemoteAddr();
            Map<String, Object> resultMap = userService.findUserByLoginActAndLoginPwd_new(loginAct, loginPwd, ip);
           User user = (User) resultMap.get("data");
           if(!ObjectUtils.isEmpty(user)){
               reques.getSession().setAttribute("user",user);
               //重定向到首页面(工作台)
               return "redirect:/workbench/toIndex.do";
           }
        }

        return "/login";
    }
    /**
     * 登录操作+十天免登陆操作，存入Cookie
     *      返回值:通过jackson的json转换工具,将Map集合转换json对象返回
     *          {code:0,msg:xxx,data:xxx}
     */
    @RequestMapping("/login.do")
    @ResponseBody
    public Map<String,Object> login (@RequestParam(value = "loginAct",required = true) String loginAct,
                                     @RequestParam(value = "loginPwd") String loginPwd , HttpSession session,
                                     HttpServletResponse response,
                                     HttpServletRequest request,
                                      String flag) throws LoginException {
//            输出日志，通过占位符方式，进行值填充
        log.info("登陆操作：用户名={}，密码={}",loginAct,loginPwd,flag);
        //将密码进行加密操作
        String md5 = MD5Util.getMD5(loginPwd);
        log.info("登陆操作：用户名={}，加密密码={}",loginAct,md5);


        //根据用户名和密码(加密后),查询数据库,完成登录操作
//        User user=userService.findUserByLoginActAndLoginPwd(loginAct,md5);
        //获取IP地址
        String ip = request.getRemoteAddr();
        log.info("IP地址={}",ip);
        //校验用户名和密码，过期时间，锁定状态，IP是否受限

        Map<String,Object> resultMap=userService.findUserByLoginActAndLoginPwd_new(loginAct,md5,ip);
        User user=(User)resultMap.get("data");
        if(user ==null){
            //在service层封装返回的结果集集合
            //1、用户名或密码错误
            //2、用户被锁定
            //3、用户已过期
            //4、IP受限，无法登陆
            //5、登录成功
            return resultMap;
        }
        //封装Map集合，用于返回数据
//        Map<String,Object> resultMap = new HashMap<>();
//        if(user==null){
//            //用户名或密码错误
//            resultMap.put("code",1);
//            resultMap.put("msg","登录失败，用户名或密码错误");
//            resultMap.put("data",null);
//            return resultMap;
//
//        }

//        //登录成功，将对象存入Session中，后续用于权限校验操作
 /*       session.setAttribute("user",user);
        resultMap.put("code",0);
        resultMap.put("msg","登录成功");
        resultMap.put("data",null);
        return resultMap;*/



        session.setAttribute("user",user);
        //十天免登陆
        //将用户名和密码存入到Cookie中
        if(!StringUtils.isEmpty(flag)){
            Cookie loginActCookie =new Cookie("loginAct",loginAct);
            Cookie loginPwdCookie =new Cookie("loginPwd",md5);
            //设置Cookie的属性，maxAge生命周期
            loginActCookie.setMaxAge(60*60*24*10);
            loginPwdCookie.setMaxAge(60*60*24*10);
            //设置Cookie存储的路径，设置为根目录，这样在工程的任意位置都可以获取到该Cookie
            loginActCookie.setPath("/");
            loginPwdCookie.setPath("/");
            //通过response对象，将Cookie响应到浏览器当中
            response.addCookie(loginActCookie);
            response.addCookie(loginPwdCookie);


        }
        return resultMap;



    }

}
