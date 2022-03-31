package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.settings.domain.DictionaryType;
import com.bjpowernode.crm.settings.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/settings/dictionary")
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;
    @RequestMapping("/toIndex.do")
    public String toIndex(){
        return "/settings/dictionary/index";
    }
    /*
    Model   ModelAndView
    前者封装了数据，视图通过返回值进行跳转
    后者封装了视图和数据
     */
    @RequestMapping("/type/toIndex.do")
    public String toTypeIndex(Model model){
        //将数据字典列表查询处理
        List<DictionaryType> dictionaryTypeList= dictionaryService.findDictionaryTypeList();
        //通过Model对象，携带到页面中
        model.addAttribute("dictionaryTypeList",dictionaryTypeList);

        return "/settings/dictionary/type/index";
    }
    @RequestMapping("/value/toIndex.do")
    public String toValueIndex(){
        return "/settings/dictionary/value/index";
    }

    @RequestMapping("/type/toTypeSave.do")
    public String toTypeSave(){
        return "/settings/dictionary/type/save";
    }
    @RequestMapping("/type/checkCode.do")
    @ResponseBody
    public Map<String,Object> checkTypeCode(String code) throws AjaxRequestException {
        DictionaryType dictionaryType=dictionaryService.findDictionaryTypeByCode(code);
        //定义返回结果集
        Map<String,Object> resultMap=new HashMap<>();

        if (ObjectUtils.isEmpty(dictionaryType)){
            resultMap.put("code",0);
            resultMap.put("msg","");
            return resultMap;
        }else {
            throw new AjaxRequestException("编码已重复，无法新增");
        }

    }
    //添加数据
    @RequestMapping("/type/saveDictionaryType.do")
    @ResponseBody
    public Map<String,Object> saveDictionaryType(DictionaryType dictionaryType) throws AjaxRequestException {
        //新增操作
        int count=dictionaryService.saveDictionaryType(dictionaryType);
        Map<String,Object> resultMap=new HashMap<>();
        if(count!=0){
            resultMap.put("code",0);
            resultMap.put("msg","添加成功");
            return resultMap;

        }else {
            throw new AjaxRequestException("添加失败");
        }
    }

}
















