package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public User findUserByLoginActAndLoginPwd(String loginAct, String loginPwd) {

        //登录操作
        User user=userDao.findUserByLoginActAndLoginPwd(loginAct,loginPwd);
        //校验用户操作




        return user ;
    }

    @Override
    public Map<String,Object> findUserByLoginActAndLoginPwd_new(String loginAct, String md5Pwd,String ip) throws LoginException {
        //登录操作
        User user=userDao.findUserByLoginActAndLoginPwd(loginAct,md5Pwd);
        HashMap<String, Object> resultMap = new HashMap<>();
        //使用异常处理器来决定失败的返回值内容

        if(user==null){
//            //用户名或密码错误
//            resultMap.put("code",1);
//            resultMap.put("msg","登录失败，用户名或密码错误");
//            resultMap.put("data",null);
//            return resultMap;
            throw new LoginException("用户名或密码错误");
        }
        //获取过期时间
        String expireTim = user.getExpireTime();
        //获取当前时间
        String now=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        //校验是否过期
        //过期时间为空，永不过期
        //返回1代表未过期
        //返回0代表马上过期
        //返回-1代表已经过期
        if(!StringUtils.isEmpty(expireTim)){
            if(expireTim.compareTo(now)<=0){
//                resultMap.put("code",1);
//                resultMap.put("msg","当前用户已过期");
//                resultMap.put("data",null);
//                return resultMap;
                throw new LoginException("当前用户已过期");
            }
        }

        //检验用户是否被锁定
        String lockState = user.getLockState();
        if(!StringUtils.isEmpty(lockState)){
            if("1".equals(lockState)){
//                resultMap.put("code",1);
//                resultMap.put("msg","当前用户已锁定");
//                resultMap.put("data",null);
//                return resultMap;
                throw new LoginException("当前用户已锁定");
            }
        }
//检验用户是否IP被限制
        //IP允许访问列表为空，允许任意访问
        String allowIps = user.getAllowIps();
        if(!StringUtils.isEmpty(allowIps)){
            if(!allowIps.contains(ip)){
                //IP受限
//                resultMap.put("code",1);
//                resultMap.put("msg","当前用户IP受限");
//                resultMap.put("data",null);
//                return resultMap;
                throw new LoginException("当前用户IP受限");
            }
        }


        //登录成功，将对象存入Session中，后续用于权限校验操作

        resultMap.put("code",0);
        resultMap.put("msg","登录成功");
        resultMap.put("data",user);

        return resultMap;
    }
}
