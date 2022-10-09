package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    List<Tran> getTranList(Integer pageNoIndex, Integer pageSize);

    long findTranTotalCount();

    List<Tran> getTranListByPageCondition(Integer pageNoIndex, Integer pageSize, String source, String name, String owner, String customerId, String contactsId, String stage, String type);

    long findTranTotalCountCondition(String source, String name, String owner, String customerId, String contactsId, String stage, String type);

    Tran findTranById(String id);

    void deleteTranByIdList(String[] tranIds);

    void saveTran(Tran tran, String customerName, String createBy, String createTime) throws AjaxRequestException;


    void updateTran(String id,Tran tran, String customerName, String editBy, String editTime) throws AjaxRequestException;

    String getActivityIdById(String id);

    String getContactsIdById(String id);

    Tran findTranConvertById(String id);

    List<TranHistory> findTranHistoryList(String tranId);

    void updateTranAndHistory(String tranId, String stage, String money, String expectedDate, String createBy, String createTime) throws AjaxRequestException;

    List<Map<String, Object>> findTransactionChartData();

}
