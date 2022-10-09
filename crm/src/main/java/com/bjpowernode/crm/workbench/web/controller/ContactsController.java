package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.entity.R;
import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/workbench/contacts")
public class ContactsController {
    @Autowired
    ContactsService contactsService;
    @RequestMapping("/getContactsList.do")
    @ResponseBody
    public R  getContactsList()throws AjaxRequestException {
            List<Contacts> contactsList=contactsService.getContactsList();
            if (ObjectUtils.isEmpty(contactsList))
                throw new AjaxRequestException("当前联系人列表无数据");
            //查询成功
            return R.ok(0,"查询成功",contactsList);
        }
}


