package com.bjpowernode.crm.listener;

import com.bjpowernode.crm.settings.domain.DictionaryValue;
import com.bjpowernode.crm.settings.service.DictionaryService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class SysLoadListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //加载数据字典列表到服务器缓存中
        ServletContextListener.super.contextInitialized(sce);
        //手动加载Spring容器
        ApplicationContext app=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-service.xml");
        //从容器中获取字典的Service对象
        DictionaryService bean=app.getBean(DictionaryService.class);
        //调用service方法，进行数据查询
        Map<String, List<DictionaryValue>> cacheData=bean.findCacheData();
        //添加到缓存中
        for (Map.Entry<String, List<DictionaryValue>> entry :cacheData.entrySet()
        ){
            String key = entry.getKey();
            List<DictionaryValue> value = entry.getValue();
            /*System.out.println("key:"+key);
            System.out.println("value:"+value);*/
            //存入到服务器缓存中
            sce.getServletContext().setAttribute(key,value);
        }
        //-------加载数据字典列表到服务器缓存中-------
        //-------加载阶段和可能性数据到服务器缓存中-------

        //创建Map集合,封装kv键值对
        Map<String,String> sapMap = new HashMap<>();

        //将Stage2Possibility.properties属性文件读取到内存中(Map集合,键值对)
        ResourceBundle bundle = ResourceBundle.getBundle("properties/Stage2Possibility");//文件名称不带后缀名

        //获取key的Set集合,然后根据key获取value数据
        //将key和value封装到Map集合中,存入到服务器缓存中
        for (String key : bundle.keySet()) {
            String value = bundle.getString(key);
            sapMap.put(key,value);
            System.out.println("key :::>>> "+key +" value :::>>> "+value);
        }
        sce.getServletContext().setAttribute("sapMap",sapMap);

        //-------加载阶段和可能性数据到服务器缓存中-------

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
