package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranHistoryDao {
    boolean insert(TranHistory tranHistory);

    List<TranHistory> findById(String tranId);

    List<Map<String, Object>> loadTransactionHistoryData();

}
