package com.bjpowernode.crm.interceptor;

import com.bjpowernode.crm.exception.InterceptorException;
import com.bjpowernode.crm.settings.domain.User;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceotor implements HandlerInterceptor {
    //控制器访问前的拦截方法
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //获取登录的用户
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        //没有用户信息，证明没有登录
        //除了登录操作和跳转用户页面的请求外，全部拦截
        //除了登录操作和跳转用户页面操作和十天免登陆操作
        if(ObjectUtils.isEmpty(user)){
            //拦截该请求
            throw new InterceptorException();
        }
        return true;
    }
    //控制器访问后的拦截方法
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
//页面加载完成前的回调方法
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
