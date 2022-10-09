package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.exception.TraditionRequestException;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueDao clueDao;
    @Autowired
    private ClueRemarkDao clueRemarkDao;
    @Autowired
    private ClueActivityRelationDao clueActivityRelationDao;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private ContactsDao contactsDao;
    @Autowired
    private CustomerRemarkDao customerRemarkDao;
    @Autowired
    private ContactsRemarkDao contactsRemarkDao;
    @Autowired
    private ContactsActivityRelationDao contactsActivityRelationDao;
    @Autowired
    private TranDao tranDao;
    @Autowired
    private TranHistoryDao tranHistoryDao;
    @Override
    public boolean saveClue(Clue clue) {
        return false;
    }

    @Override
    public Clue findClueById(String id) {

        return clueDao.findById(id);
    }

    @Override
    public List<Clue> getClueList() {
        return clueDao.findAll();

    }

    @Override
    public List<Clue> getClueList(Integer pageNoIndex, Integer pageSize) {
        return clueDao.findAllByPage(pageNoIndex,pageSize);

    }

    @Override
    public long findClueTotalCount() {

        return clueDao.findTotalCounts();
    }

    @Override
    public List<Clue> getClueListByPageCondition(Integer pageNoIndex, Integer pageSize, String clueName, String username, String comp, String mphone, String phone, String source, String state) {

        return clueDao.findAllByPageCondition(pageNoIndex,pageSize,clueName,username,comp,mphone,phone,source,state);
    }

    @Override
    public long findClueTotalCountCondition(String clueName, String username, String comp, String mphone, String phone, String source, String state) {

        return clueDao.findTotalCountsCondition(clueName,username,comp,mphone,phone,source,state);
    }

    @Override
    public boolean saveClue(Clue clue, String createBy, String createTime) throws AjaxRequestException {
        //ID 创建人  创建时间  修改人 修改时间
        clue.setId(UUIDUtil.getUUID()).setCreateBy(createBy).setCreateTime(createTime).setEditBy(createBy).setEditTime(createTime);
        //新增操作
        boolean flag=clueDao.saveClue(clue);
        if(!flag)
            throw new AjaxRequestException("新增失败");
        return true;

    }

    @Override
    public boolean updateClueById(Clue clue, String editBy, String editTime) {

        return clueDao.updateClueById(clue.setEditTime(editTime).setEditBy(editBy));
    }

    @Override
    public void deleteClueByIdList(String[] clueIds) {
        clueDao.deleteByIdList(clueIds);
    }

    @Override
    public Clue findClueAndOwnerById(String id) {

        return clueDao.findClueById(id);
    }

    @Override
    public List<ClueRemark> findClueRemarkList(String clueId) {

        return clueRemarkDao.findClueRemarkList(clueId);
    }

    @Override
    public boolean updateClueRemarkById(String remarkId, String noteContent, String editBy, String editTime, String editFlag) {

        return clueRemarkDao.update(remarkId,noteContent,editBy,editTime,editFlag);
    }

    @Override
    public boolean saveClueRemark(ClueRemark setEditTime) {

        return clueRemarkDao.save(setEditTime);
    }

    @Override
    public boolean deleteClueRemark(String remarkId) {

        return clueRemarkDao.delete(remarkId);
    }

    @Override
    public List<Activity> findActivityRelationList(String clueId) {

        return clueDao.findRelationList(clueId);
    }

    @Override
    public boolean deleteClueActivityRelation(String carId) {
        return clueActivityRelationDao.delete(carId);

    }

    @Override
    public List<Map<String, String>> findActivityRelationListMap(String clueId) {

        return clueDao.findActivityRelationListMap(clueId);
    }

    @Override
    public List<Activity> findActivityUnRelationList(String clueId) {

        return clueDao.findActivityUnRelationList(clueId);
    }

    @Override
    public List<Activity> searchLikeActivityUnRelationList(String clueId, String activityName) {

        return clueDao.findLikeActivityUnRelationList(clueId,activityName);
    }

    @Override
    public void saveClueActivityRelation(String clueId, String[] activityIds) throws AjaxRequestException {
        ArrayList<ClueActivityRelation> carList = new ArrayList<>();
        for (String activityId :
                activityIds) {
            ClueActivityRelation car=new ClueActivityRelation()
                    .setId(UUIDUtil.getUUID())
                    .setActivityId(activityId)
                    .setClueId(clueId);
            carList.add(car);

        }
        boolean flag=clueActivityRelationDao.insertList(carList);
        if(!flag){
            throw new AjaxRequestException("批量导入失败");
        }

    }

    @Override
    public void saveConvert(String clueId, String flag, String owner, String createBy, String createTime, Tran tran) throws TraditionRequestException {
        //1、根据线索id,查询出线索数据
        Clue clue=clueDao.findClueById(clueId);
        //2、根据线索的客户名称，查看是否有当前的客户信息
        String customerName=clue.getCompany();
        String fullname=clue.getFullname();
        Customer customer=customerDao.findByName(customerName);

        Contacts contacts=null;
        if(customer==null){
            //3、没有客户信息，新增一条记录
            customer=new Customer()
                    .setId(UUIDUtil.getUUID())
                    .setCreateBy(createBy)
                    .setCreateTime(createTime)
                    .setEditBy(createBy)
                    .setEditTime(createTime)
                    .setDescription(clue.getDescription())
                    .setName(customerName)
                    .setOwner(owner)
                    .setPhone(clue.getPhone())
                    .setAddress(clue.getAddress())
                    .setContactSummary(clue.getContactSummary())
                    .setWebsite(clue.getWebsite())
                    .setNextContactTime(clue.getNextContactTime());
            boolean customerFlag=customerDao.insert(customer);
            if(!customerFlag){
                throw new TraditionRequestException("新增客户失败");
            }

            //4、新增联系人信息
            contacts=new Contacts()
                    .setId(UUIDUtil.getUUID())
                    .setCreateBy(createBy)
                    .setCreateTime(createTime)
                    .setEditBy(createBy)
                    .setEditTime(createTime)
                    .setAddress(clue.getAddress())
                    .setAppellation(clue.getAppellation())
                    .setContactSummary(clue.getContactSummary())
                    .setCustomerId(customer.getId())
                    .setDescription(clue.getDescription())
                    .setEmail(clue.getEmail())
                    .setFullname(clue.getFullname())
                    .setJob(clue.getJob())
                    .setMphone(clue.getMphone())
                    .setNextContactTime(clue.getNextContactTime())
                    .setOwner(clue.getOwner())
                    .setSource(clue.getSource());
            boolean contactsFlag=contactsDao.insert(contacts);
            if(!contactsFlag){
                throw new TraditionRequestException("新增联系人失败");
            }
            }else{
            //由于联系人可能存在重名问题，后面可以执行清除操作
            List<Contacts> contacts1=contactsDao.findByFullName(fullname);
            if(ObjectUtils.isEmpty(contacts1)){
                //新增联系人
                contacts=new Contacts()
                        .setId(UUIDUtil.getUUID())
                        .setCreateBy(createBy)
                        .setCreateTime(createTime)
                        .setEditBy(createBy)
                        .setEditTime(createTime)
                        .setAddress(clue.getAddress())
                        .setAppellation(clue.getAppellation())
                        .setContactSummary(clue.getContactSummary())
                        .setCustomerId(customer.getId())
                        .setDescription(clue.getDescription())
                        .setEmail(clue.getEmail())
                        .setFullname(clue.getFullname())
                        .setJob(clue.getJob())
                        .setMphone(clue.getMphone())
                        .setNextContactTime(clue.getNextContactTime())
                        .setOwner(clue.getOwner())
                        .setSource(clue.getSource());
                boolean contactsFlag=contactsDao.insert(contacts);
                if(!contactsFlag){
                    throw new TraditionRequestException("新增联系人失败");
                }

            }else{
                contacts=contacts1.get(0);
            }

        }
        //5、根据线索id，查询出线索的备注信息列表，完成转换
        List<ClueRemark> clueRemarkList=clueRemarkDao.findListByClueId(clueId);
        if (!ObjectUtils.isEmpty(clueRemarkList)) {
            //转换操作
            List<CustomerRemark> customerRemarkList=new ArrayList<>();
            List<ContactsRemark> contactsRemarkList=new ArrayList<>();
            for (ClueRemark clueRemark :
                    clueRemarkList) {
                CustomerRemark customerRemark = new CustomerRemark()
                        .setCustomerId(customer.getId())
                        .setCreateTime(createTime)
                        .setEditBy(createBy)
                        .setEditTime(createTime)
                        .setId(UUIDUtil.getUUID())
                        .setEditFlag("0")
                        .setNoteContent(clueRemark.getNoteContent())
                        .setCreateBy(createBy);
                customerRemarkList.add(customerRemark);
                //封装到联系人备注信息中
                ContactsRemark contactsRemark = new ContactsRemark()
                        .setContactsId(contacts.getId())
                        .setCreateTime(createTime)
                        .setCreateBy(createBy)
                        .setEditFlag("0")
                        .setEditBy(createBy)
                        .setEditTime(createTime)
                        .setId(UUIDUtil.getUUID())
                        .setNoteContent(clueRemark.getNoteContent())
                        ;
                contactsRemarkList.add(contactsRemark);
            }

            //
            boolean customerRemarkFlag=customerRemarkDao.insert(customerRemarkList);
            if(!customerRemarkFlag){
                throw new TraditionRequestException("导入客户备注信息失败");
            }
            boolean contactsRemarkFlag=contactsRemarkDao.insert(contactsRemarkList);
            if(!contactsRemarkFlag){
                throw new TraditionRequestException("导入联系人备注信息失败");
            }
        }
        //6 根据线索id，查询出线索和市场活动的中间表数据，完成转换
        List<ClueActivityRelation> clueActivityRelationList=clueActivityRelationDao.findListByClueId(clueId);
        if(!ObjectUtils.isEmpty(clueActivityRelationList)){
            //完成转换
            List<ContactsActivityRelation> contactsActivityRelations = new ArrayList<>();
            for (ClueActivityRelation car :
                    clueActivityRelationList) {
                ContactsActivityRelation c=new ContactsActivityRelation()
                        .setId(UUIDUtil.getUUID())
                        .setContactsId(contacts.getId())
                        .setActivityId(car.getActivityId());
                contactsActivityRelations.add(c);
            }
            //
            boolean contactsActivityRelationFlag=contactsActivityRelationDao.insert(contactsActivityRelations);
            if(!contactsActivityRelationFlag){
                throw new TraditionRequestException("导入联系人关系信息失败");
            }
        }
    //7 判断是否创建交易
    if("a".equals(flag)){
        //8 新增交易和交易历史
        tran.setCustomerId(customer.getId())
                .setId(UUIDUtil.getUUID())
                .setCreateBy(createBy)
                .setCreateTime(createTime)
                .setOwner(owner)
                .setContactsId(contacts.getId())
                .setContactSummary(clue.getContactSummary())
                .setDescription(clue.getDescription())
                .setNextContactTime(clue.getNextContactTime())
                .setEditTime(createTime)
                .setEditBy(createBy);
        //新增交易历史记录
        TranHistory tranHistory = new TranHistory()
                .setTranId(tran.getId())
                .setStage(tran.getStage())
                .setMoney(tran.getMoney())
                .setId(UUIDUtil.getUUID())
                .setExpectedDate(tran.getExpectedDate())
                .setCreateBy(createBy)
                .setCreateTime(createTime)
                ;
        boolean tranFlag=tranDao.insert(tran);
        if(!tranFlag){
            throw new TraditionRequestException("导入交易信息失败");
        }
        boolean tranhistoryFlag=tranHistoryDao.insert(tranHistory);
        if(!tranhistoryFlag){
            throw new TraditionRequestException("导入交易历史信息失败");
        }
    }
    //9 删除线索相关数据，根据线索id进行删除操作
        //用于线索的数据已经转化成客户和联系人数据，就可以删除掉了
        //先删除多对多关系，再一对多，再一方数据
        if(!ObjectUtils.isEmpty(clueActivityRelationDao.findListByClueId(clueId))){
            boolean carFlag=clueActivityRelationDao.deleteListByClueId(clueId);
            if(!carFlag){
                throw new TraditionRequestException("删除线索和市场活动关联关系失败");
            }
        };
        if(!ObjectUtils.isEmpty(clueRemarkDao.findListByClueId(clueId))){
            boolean remarkFlag=clueRemarkDao.deleteListByClueId(clueId);
            if(!remarkFlag){
                throw new TraditionRequestException("删除线索备注信息失败");
            }
        };

        boolean cFlag=clueDao.deleteById(clueId);
        if(!cFlag){
                throw new TraditionRequestException("删除线索失败");
        }

    }
}
