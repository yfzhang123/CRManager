package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;

public interface ActivityService {
    //未分页
    @Deprecated//当前方法已过期
    List<Activity> getActivityList();
    @Deprecated//当前方法已过期
    List<Activity> getActivityList(Integer pageNoIndex, Integer pageSize);
//新增市场活动
    boolean saveActivity(Activity activity,String createBy,String createTime) throws AjaxRequestException;

//查询市场活动总记录数
@Deprecated
    long findActivityTotalCount();
//分页查询-条件过滤查询
    List<Activity> getActivityListByPageCondition(int pageNoIndex, Integer pageSize, String activityName, String username, String startDate, String endDate);
    //查询经过条件过滤的市场活动总记录数
    long findActivityTotalCountCondition(String activityName, String username, String startDate, String endDate);
}
