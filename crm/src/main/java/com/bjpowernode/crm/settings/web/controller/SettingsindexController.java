package com.bjpowernode.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
public class SettingsindexController {
//    跳转到系统设置首页面
    @RequestMapping("/toIndex.do")
    public String toIndex(){
        return "/settings/index";
    }
}
