package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.exception.TraditionRequestException;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private ActivityRemarkDao activityRemarkDao;
    @Override
    public List<Activity> getActivityList() {
        return activityDao.findAll();

    }

    @Override
    public List<Activity> getActivityList(Integer pageNoIndex, Integer pageSize) {

        return activityDao.findAllByPage(pageNoIndex,pageSize);



    }

    @Override
    public boolean saveActivity(Activity activity,String createBy,String createTime) throws AjaxRequestException {
        //ID 创建人  创建时间  修改人 修改时间
        activity.setId(UUIDUtil.getUUID()).setCreateBy(createBy).setCreateTime(createTime).setEditBy(createBy).setEditTime(createTime);
        //新增操作
        boolean flag=activityDao.insert(activity);
        if(!flag)
            throw new AjaxRequestException("新增失败");
        return true;

    }

    @Override
    public long findActivityTotalCount() {
        return activityDao.findTotalCounts();

    }

    @Override
    public List<Activity> getActivityListByPageCondition(int pageNoIndex, Integer pageSize, String activityName, String username, String startDate, String endDate) {

        return activityDao.findAllByPageCondition( pageNoIndex,  pageSize, activityName, username,  startDate, endDate);
    }

    @Override
    public long findActivityTotalCountCondition(String activityName, String username, String startDate, String endDate) {
        return activityDao.findTotalCountsCondition(activityName,username,startDate,endDate);

    }

    @Override
    public Activity findaActivityById(String id) {
        return activityDao.findById(id);
    }

    @Override
    public boolean updateActivityById(Activity activity, String editBy, String editTime) {


        return activityDao.updateActivityById(activity.setEditTime(editTime).setEditBy(editBy));

    }

    @Override
    public void deleteActivityByIdList(String[] activityIds, String editBy, String editTime) throws AjaxRequestException {
        for (String activityId: activityIds){
            boolean flag=activityDao.updateIsDeleteById(activityId,editBy,editTime);
            if(!flag){
                throw new AjaxRequestException("修改失败");

            }
        }
    }

    @Override
    public void saveActivityList(List<Activity> activityList) throws TraditionRequestException {
        for (Activity activity :
                activityList) {
            boolean flag=activityDao.insert(activity);
            if(!flag){
                throw new TraditionRequestException("批量导入失败");
            }
        }
    }

    @Override
    public List<Activity> findActivityList() {
        return activityDao.findAllByIsDelete();

    }

    @Override
    public List<Activity> findActivityListByIds(String[] activityIds) {

       return activityDao.findListByIds(activityIds);

    }

    @Override
    public Activity findaActivityAndOwnerById(String id) {


        return activityDao.findActivityById(id);
    }

    @Override
    public List<ActivityRemark> findActivityRemarkList(String activityId) {
        return activityRemarkDao.findAllByActivityId(activityId);

    }

    @Override
    public boolean saveActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkDao.insert(activityRemark);

    }

    @Override
    public boolean updateActivityRemarkById(String remarkId, String noteContent, String editBy, String editTime,String editFlag) {
        return activityRemarkDao.update(remarkId,noteContent,editBy,editTime,editFlag);
    }

    @Override
    public boolean deleteActivityRemark(String remarkId) {

        return activityRemarkDao.delete(remarkId);
    }

}
