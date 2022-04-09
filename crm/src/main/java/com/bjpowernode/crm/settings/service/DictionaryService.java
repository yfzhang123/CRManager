package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.settings.domain.DictionaryType;
import com.bjpowernode.crm.settings.domain.DictionaryValue;

import java.util.List;

public interface DictionaryService {
    public List<DictionaryType> findDictionaryTypeList();

    DictionaryType findDictionaryTypeByCode(String code);

    int saveDictionaryType(DictionaryType dictionaryType);

    boolean updateDictionaryType(DictionaryType dictionaryType);

    boolean batchDeleteDictionaryType(String[] codes) throws AjaxRequestException;

    List<String> batchDeleteDictionaryTypeCondition(String[] codes) throws AjaxRequestException;

    List<DictionaryValue> findDictionaryValueList();

    boolean saveDictionaryValue(DictionaryValue dictionaryValue) throws AjaxRequestException;

    DictionaryValue findDictionaryValueById(String id);

    boolean updateDictionaryValue(DictionaryValue dictionaryValue);

    boolean batchDeleteDictionaryValue(String[] ids) throws AjaxRequestException;


}
