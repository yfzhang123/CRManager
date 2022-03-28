package com.bjpowernode.crm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings/dictionary")
public class DictionaryController {
    @RequestMapping("/toIndex.do")
    public String toIndex(){
        return "/settings/dictionary/index";
    }
    @RequestMapping("/type/toIndex.do")
    public String toTypeIndex(){
        return "/settings/dictionary/type/index";
    }
    @RequestMapping("/value/toIndex.do")
    public String toValueIndex(){
        return "/settings/dictionary/value/index";
    }
}
















