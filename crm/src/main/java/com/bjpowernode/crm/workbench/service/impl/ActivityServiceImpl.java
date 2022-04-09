package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityDao activityDao;
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
}
