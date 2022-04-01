package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.exception.TraditionRequestException;
import com.bjpowernode.crm.settings.dao.DictionaryValueDao;
import com.bjpowernode.crm.settings.domain.DictionaryType;
import com.bjpowernode.crm.settings.domain.DictionaryValue;
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
    //返回字典值页面
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
    //跳转到字典类型编辑页面
    @RequestMapping("/type/toEdit.do")
    public String toTypeEdit(String code,Model model) throws TraditionRequestException {
        //根据编码查询字典类型数据
        DictionaryType dictionaryType = dictionaryService.findDictionaryTypeByCode(code);
        if (dictionaryType==null){
            throw new TraditionRequestException("当前数据查询异常");
        }
        //封装到Model对象中
        model.addAttribute("dictionaryType",dictionaryType);
        //跳转到修改页面
        return "/settings/dictionary/type/edit";
    }
    //更新字典类型操作
    @RequestMapping("/type/updateDictionaryType.do")
    @ResponseBody
    public Map<String,Object> updateDictionaryType(DictionaryType dictionaryType) throws AjaxRequestException {
        //更新操作，以update方法名称开头
        boolean flag=dictionaryService.updateDictionaryType(dictionaryType);
        if(!flag){
            //修改失败
            throw new AjaxRequestException("修改字典类型失败");

        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("code",0);
        resultMap.put("msg","修改成功");
        return resultMap;

    }
    //批量删除字典类型操作
    @RequestMapping("/type/batchDeleteDictionaryType.do")
    @ResponseBody
    public Map<String,Object> batchDeleteDictionaryType(String[] codes) throws AjaxRequestException {
        boolean flag=dictionaryService.batchDeleteDictionaryType(codes);
        if(!flag){
            throw new AjaxRequestException("删除失败");
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("code",0);
        resultMap.put("msg","删除成功");
        return resultMap;

    }
    //批量删除--考虑1对多关联关系
    @RequestMapping("/type/batchDeleteDictionaryTypeCondition.do")
    @ResponseBody
    public Map<String,Object> batchDeleteDictionaryTypeCondition(String[] codes) throws AjaxRequestException {
        List<String> codelist = dictionaryService.batchDeleteDictionaryTypeCondition(codes);
        if(ObjectUtils.isEmpty(codelist)){
            //批量删除，全部执行成功
            Map<String,Object> resultMap=new HashMap<>();
            resultMap.put("code",0);
            resultMap.put("msg","删除成功");
            return resultMap;
        }
        //部分内容无法删除
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("code",1);
        resultMap.put("msg","删除成功，部分数据有关联关系，无法删除");
        resultMap.put("data",codelist);
        return resultMap;

    }
    //
    @RequestMapping("/value/getDictionaryValueList.do")
    @ResponseBody
    public  Map<String,Object> getDictionaryValueList(){
        //查询字典值列表
        List<DictionaryValue> dictionaryValueList=dictionaryService.findDictionaryValueList();
        Map<String,Object> resultMap=new HashMap<>();
        if(ObjectUtils.isEmpty(dictionaryValueList)){
            //没有字典值列表数据
            resultMap.put("code",0);
            resultMap.put("msg","当前列表无数据");
            return resultMap;
        }
        resultMap.put("code",0);
        resultMap.put("msg","查询成功");
        resultMap.put("data",dictionaryValueList);
        return resultMap;
    }
}
















