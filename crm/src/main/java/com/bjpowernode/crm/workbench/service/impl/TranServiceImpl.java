package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {
    @Autowired
    TranDao tranDao;
    @Autowired
    CustomerDao customerDao;
    @Autowired
    TranHistoryDao tranHistoryDao;
    @Override
    public List<Tran> getTranList(Integer pageNoIndex, Integer pageSize) {
        return tranDao.findAllPage(pageNoIndex,pageSize);

    }

    @Override
    public long findTranTotalCount() {
        return tranDao.findTotalCounts();

    }

    @Override
    public List<Tran> getTranListByPageCondition(Integer pageNoIndex, Integer pageSize, String source, String name, String owner, String customerId, String contactsId, String stage, String type) {
        return tranDao.findAllByPageCondition(pageNoIndex,pageSize,source,name,owner,customerId,contactsId,stage,type);

    }

    @Override
    public long findTranTotalCountCondition(String source, String name, String owner, String customerId, String contactsId, String stage, String type) {

        return tranDao.findTotalCountsCondition(source,name,owner,customerId,contactsId,stage,type);
    }

    @Override
    public Tran findTranById(String id) {

        return tranDao.findTranById(id);
    }

    @Override
    public void deleteTranByIdList(String[] tranIds) {
        tranDao.deleteTranByIdList(tranIds);
    }

    @Override
    public void saveTran(Tran tran, String customerName, String createBy, String createTime) throws AjaxRequestException {
        //查询客户
        Customer customer=customerDao.findByName(customerName);
        //没有，创建
        if(customer==null){
            customer=new Customer()
                    .setNextContactTime(tran.getNextContactTime())
                    .setOwner(tran.getOwner())
                    .setName(customerName)
                    .setId(UUIDUtil.getUUID())
                    .setDescription(tran.getDescription())
                    .setCreateTime(createTime)
                    .setCreateBy(createBy)
                    .setContactSummary(tran.getContactSummary())
                    .setEditBy(createBy)
                    .setEditTime(createTime);
            boolean count = customerDao.insert(customer);
            if(!count)
                throw new AjaxRequestException("新增客户失败...");
        }
        //如果有,则直接给tran对象赋值customerId
        //新增交易和交易历史
        tran.setId(UUIDUtil.getUUID())
                .setEditBy(createBy)
                .setEditTime(createTime)
                .setCreateBy(createBy)
                .setCreateTime(createTime)
                .setCustomerId(customer.getId());

        boolean tranCount = tranDao.insert(tran);
        if(!tranCount)
            throw new AjaxRequestException("新增交易失败...");

        TranHistory history = new TranHistory()
                .setCreateBy(createBy)
                .setCreateTime(createTime)
                .setExpectedDate(tran.getExpectedDate())
                .setId(UUIDUtil.getUUID())
                .setMoney(tran.getMoney())
                .setStage(tran.getStage())
                .setTranId(tran.getId());

        boolean historyCount = tranHistoryDao.insert(history);

        if (!historyCount)
            throw new AjaxRequestException("新增交易历史失败...");
    }

    @Override
    public void updateTran(String id,Tran tran, String customerName, String editBy, String editTime) throws AjaxRequestException {
        //查询客户
        Customer customer=customerDao.findByName(customerName);
        //没有，创建
        if(customer==null){
            customer=new Customer()
                    .setNextContactTime(tran.getNextContactTime())
                    .setOwner(tran.getOwner())
                    .setName(customerName)
                    .setId(UUIDUtil.getUUID())
                    .setDescription(tran.getDescription())
                    .setCreateTime(editTime)
                    .setCreateBy(editBy)
                    .setContactSummary(tran.getContactSummary())
                    .setEditBy(editBy)
                    .setEditTime(editTime);
            boolean count = customerDao.insert(customer);
            if(!count)
                throw new AjaxRequestException("新增客户失败...");
        }
        //如果有,则直接给tran对象赋值customerId
        tran.setEditBy(editBy)
                .setEditTime(editTime)
                .setCustomerId(customer.getId());

        boolean tranCount = tranDao.update(
                tran.getOwner(),tran.getMoney(),tran.getName(),tran.getExpectedDate(),
                tran.getCustomerId(),tran.getStage(),
                tran.getType(),tran.getSource(),tran.getActivityId(),tran.getContactsId(),
                tran.getEditBy(),tran.getEditTime(),tran.getDescription(),tran.getContactSummary(),
                tran.getNextContactTime(),id);
        if(!tranCount)
            throw new AjaxRequestException("更新交易失败...");

        TranHistory history = new TranHistory()
                .setCreateBy(editBy)
                .setCreateTime(editTime)
                .setExpectedDate(tran.getExpectedDate())
                .setId(UUIDUtil.getUUID())
                .setMoney(tran.getMoney())
                .setStage(tran.getStage())
                .setTranId(tran.getId());

        boolean historyCount = tranHistoryDao.insert(history);

        if (!historyCount)
            throw new AjaxRequestException("创建交易历史失败...");
    }

    @Override
    public String getActivityIdById(String id) {
        return tranDao.findActivityId(id);
    }

    @Override
    public String getContactsIdById(String id) {

        return tranDao.findContactsId(id);
    }

    @Override
    public Tran findTranConvertById(String id) {
        return tranDao.findTranConvertById(id);
    }

    @Override
    public List<TranHistory> findTranHistoryList(String tranId) {
        return tranHistoryDao.findById(tranId);
    }

    @Override
    public void updateTranAndHistory(String tranId, String stage, String money, String expectedDate, String createBy, String createTime) throws AjaxRequestException {
        //更新交易阶段
        boolean flag = tranDao.updateStageById(tranId,stage,createBy,createTime);

        if(!flag)
            throw new AjaxRequestException("更新交易失败...");
        //新增交易历史记录
        TranHistory history = new TranHistory()
                .setCreateBy(createBy)
                .setTranId(tranId)
                .setCreateTime(createTime)
                .setStage(stage)
                .setMoney(money)
                .setExpectedDate(expectedDate)
                .setId(UUIDUtil.getUUID());

        boolean hFlag = tranHistoryDao.insert(history);

        if(!hFlag)
            throw new AjaxRequestException("新增交易历史记录失败...");
    }

    @Override
    public List<Map<String, Object>> findTransactionChartData() {

        return  tranHistoryDao.loadTransactionHistoryData();
    }
}
