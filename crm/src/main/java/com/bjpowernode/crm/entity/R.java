package com.bjpowernode.crm.entity;

import java.util.HashMap;
import java.util.Map;

/*公共返回值集合

 */
public class R extends HashMap {
    public static R ok(){
        R r=new R();
        r.put("code",0);
        r.put("msg","操作成功");
        return  r;
    }
    public static R ok(Integer code){
        R r=new R();
        r.put("code",code);
        r.put("msg","操作成功");
        return r;
    }
    public static R ok(String msg){
        R r=new R();
        r.put("code",0);
        r.put("msg",msg);
        return r;
    }
    public static R ok(Integer code,String msg){
        R r=new R();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }
    public static <T> R ok(Integer code,String msg,T data){
        R r=new R();
        r.put("code",0);
        r.put("msg",msg);
        r.put("data",data);
        return r;
    }
    public static R err(){
        R r=new R();
        r.put("code",1);
        r.put("msg","操作失败");
        return r;
    }
    public static R err(Integer code){
        R r=new R();
        r.put("code",code);
        r.put("msg","操作失败");
        return r;
    }
    public static R err(Integer code,String msg){
        R r=new R();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }
    public static R err(String msg){
        R r=new R();
        r.put("code",1);
        r.put("msg",msg);
        return r;
    }
    public static R ok(Map map){
        R r=new R();
        r.put("code",0);
        r.put("msg","操作成功");
        r.putAll(map);
        return r;
    }
}
