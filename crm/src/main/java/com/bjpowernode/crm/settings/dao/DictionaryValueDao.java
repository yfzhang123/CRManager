package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.DictionaryValue;

import java.util.List;

public interface DictionaryValueDao {
    int findListByTypeCode(String code);

    List<DictionaryValue> findAll();

    boolean insert(DictionaryValue dictionaryValue);

    DictionaryValue findDictionaryValueById(String id);

    boolean updateDictionaryValue(DictionaryValue dictionaryValue);

    boolean deletelistByids(String[] ids);
}
