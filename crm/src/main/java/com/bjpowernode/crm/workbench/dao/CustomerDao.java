package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {
    boolean insert(Customer customer);

    Customer findByName(String customerName);

    List<String> findCustomerNameListLike(String name);
}
