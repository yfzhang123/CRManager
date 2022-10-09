package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.dao.ContactsDao;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    ContactsDao contactsDao;
    @Override
    public List<Contacts> getContactsList() {

        return contactsDao.fndAll();
    }
}
