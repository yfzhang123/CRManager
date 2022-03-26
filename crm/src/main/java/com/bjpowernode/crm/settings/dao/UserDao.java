package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/*由于在pom.xml中定义build标签
该标签下加载java目录下*.xml文件，该配置文件就是mybatis的映射配置文件
如果没有定义，默认加载resources目录下的*.xml映射配置文件*/
public interface UserDao {
    //mybatis提供增删改查注解，进行sql的输出
//    @Select("select * from tbl_user where loginAct=#{loginAct}")
    public User findUserByLoginActAndLoginPwd (String loginAct, String loginPwd);
//    public User findUserByLoginActAndLoginPwd_new (String loginAct, String loginPwd,String ip);
}
