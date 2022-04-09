package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.settings.dao.DictionaryTypeDao;
import com.bjpowernode.crm.settings.dao.DictionaryValueDao;
import com.bjpowernode.crm.settings.domain.DictionaryType;
import com.bjpowernode.crm.settings.domain.DictionaryValue;
import com.bjpowernode.crm.settings.service.DictionaryService;


import com.bjpowernode.crm.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryTypeDao dictionaryTypeDao;
    @Autowired
    private DictionaryValueDao dictionaryValueDao;
    @Override
    public List<DictionaryType> findDictionaryTypeList(){
        return dictionaryTypeDao.findDictionaryTypeList();
    }

    @Override
    public DictionaryType findDictionaryTypeByCode(String code) {
        return dictionaryTypeDao.findDictionaryTypeByCode(code);
    }

    @Override
    public int saveDictionaryType(DictionaryType dictionaryType) {
        return dictionaryTypeDao.addDictionaryType(dictionaryType);
    }

    @Override
    public boolean updateDictionaryType(DictionaryType dictionaryType) {
        return dictionaryTypeDao.updateDictionaryType(dictionaryType);
    }

    @Override
    public boolean batchDeleteDictionaryType(String[] codes) throws AjaxRequestException {
        //批量删除
        //根据遍历方式逐个删除
//        for (String code :
//                codes) {
//            boolean flag=dictionaryTypeDao.deleteBycode(code);
//            if(!flag)
//                throw new AjaxRequestException("删除失败");
//        }
        //直接批量删除
        boolean flag=dictionaryTypeDao.deletelistBycodes(codes);
        if(!flag){
            throw new AjaxRequestException("删除失败");
        }
        return true;


    }

    @Override
    public List<String> batchDeleteDictionaryTypeCondition(String[] codes) throws AjaxRequestException {
        List<String> codeList=new ArrayList<>();
        //遍历接受到的参数集合
        for (String code :
                codes) {
            //根据编码查询是否有一方关联的多方数据，字典值的列表
           int valueCount=dictionaryValueDao.findListByTypeCode(code);
           if(valueCount==0){
               //可以删除
               boolean flag = dictionaryTypeDao.deleteBycode(code);
               if(!flag){
                   throw new AjaxRequestException("删除失败");
               }
           }else{
               //有关联关系，无法删除
               //将无法删除的数据（code)，存入到集合中返回到页面，提示给用户
               codeList.add(code);
           }
        }

    //将无法删除的数据编码集合返回
        return codeList;
    }

    @Override
    public List<DictionaryValue> findDictionaryValueList() {

        return dictionaryValueDao.findAll();
    }

    @Override
    public boolean saveDictionaryValue(DictionaryValue dictionaryValue) throws AjaxRequestException {
        //给字典值的id进行赋值操作
        dictionaryValue.setId(UUIDUtil.getUUID());
        //新增操作
        boolean flag=dictionaryValueDao.insert(dictionaryValue);
        if(!flag)
            throw new AjaxRequestException("新增失败");
        return true;
    }

    @Override
    public DictionaryValue findDictionaryValueById(String id) {
        return dictionaryValueDao.findDictionaryValueById(id);

    }

    @Override
    public boolean updateDictionaryValue(DictionaryValue dictionaryValue) {
        return dictionaryValueDao.updateDictionaryValue(dictionaryValue);

    }

    @Override
    public boolean batchDeleteDictionaryValue(String[] ids) throws AjaxRequestException {
        //批量删除
        //根据遍历方式逐个删除
//        for (String id :
//                ids) {
//            boolean flag=dictionaryValueDao.deleteByid(id);
//            if(!flag)
//                throw new AjaxRequestException("删除失败");
//        }
        //直接批量删除
        boolean flag=dictionaryValueDao.deletelistByids(ids);
        if(!flag){
            throw new AjaxRequestException("删除失败");
        }
        return true;
    }


}
