package com.bjpowernode.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/workbench")
public class WorkbenchIndexController {
    @RequestMapping("/toIndex.do")
    public String toIndex(){
        return "/workbench/index";
    }
}
