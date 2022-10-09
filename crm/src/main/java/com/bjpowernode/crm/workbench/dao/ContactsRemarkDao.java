package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ContactsRemark;
import com.bjpowernode.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface ContactsRemarkDao {
    boolean insert(List<ContactsRemark> contactsRemarkList);

}
