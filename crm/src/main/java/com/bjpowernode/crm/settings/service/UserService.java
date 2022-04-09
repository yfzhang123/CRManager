package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    //该注解的含义，代表方法已过期，调用时，会带有删除线
    @Deprecated
    public User findUserByLoginActAndLoginPwd(String loginAct, String loginPwd);
    public Map<String,Object> findUserByLoginActAndLoginPwd_new(String loginAct, String loginPwd,String ip) throws LoginException;

    List<User> findUserList();

}
