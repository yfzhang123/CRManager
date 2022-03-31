package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DictionaryTypeDao;
import com.bjpowernode.crm.settings.domain.DictionaryType;
import com.bjpowernode.crm.settings.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryTypeDao dictionaryTypeDao;
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
}
