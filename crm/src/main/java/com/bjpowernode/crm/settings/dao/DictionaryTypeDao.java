package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.DictionaryType;

import java.util.List;

public interface DictionaryTypeDao {
    public List<DictionaryType> findDictionaryTypeList();
    DictionaryType findDictionaryTypeByCode(String Code);

    int addDictionaryType(DictionaryType dictionaryType);
//    DictionaryType findById(String id);
}
