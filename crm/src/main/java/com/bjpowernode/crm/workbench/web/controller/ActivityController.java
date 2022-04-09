package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.entity.R;
import com.bjpowernode.crm.entity.RPage;
import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    //跳转到市场活动首页面
    @RequestMapping("/toActivityIndex.do")
    public String toIndex(){
        return "/workbench/activity/index";
    }
    /*查询市场活动列表（未分页）

     */
    @RequestMapping("/getActivityList.do")
    @ResponseBody
    /*public Map<String,Object> getActivityList() throws AjaxRequestException {
        List<Activity> activityList=activityService.getActivityList();
        if (ObjectUtils.isEmpty(activityList))
            throw new AjaxRequestException("当前市场活动无数据");
        //查询成功
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("code",0);
        resultMap.put("msg","查询成功");
        resultMap.put("data",activityList);
        return resultMap;
    }*/
    //自定义返回值结果集
    public R getActivityList() throws AjaxRequestException {
        List<Activity> activityList=activityService.getActivityList();
        if (ObjectUtils.isEmpty(activityList))
            throw new AjaxRequestException("当前市场活动无数据");
        //查询成功
        return R.ok(0,"查询成功",activityList);
    }
    //获取市场活动列表数据-分页查询
    @RequestMapping("/getActivityListByPage.do")
    @ResponseBody
    public R getActivityListByPage(Integer pageNo,Integer pageSize) throws AjaxRequestException {
        //根据当前页，计算出查询的索引值位置
        int pageNoIndex=(pageNo-1)*pageSize;
        List<Activity> activityList=activityService.getActivityList(pageNoIndex,pageSize);
        if (ObjectUtils.isEmpty(activityList))
            throw new AjaxRequestException("当前市场活动无数据");
        //查询成功
        return R.ok(0,"查询成功",activityList);
    }
    //获取市场活动列表数据-分页组件
    @RequestMapping("/getActivityListByPageComponent.do")
    @ResponseBody
    public RPage<List<Activity>> getActivityListByPageCompent(Integer pageNo, Integer pageSize) throws AjaxRequestException {
        //根据当前页，计算出查询的索引值位置
        int pageNoIndex=(pageNo-1)*pageSize;
        List<Activity> activityList=activityService.getActivityList(pageNoIndex,pageSize);
        if (ObjectUtils.isEmpty(activityList))
            throw new AjaxRequestException("当前市场活动无数据");
        //查询当前列表总记录数
        long totalRows=activityService.findActivityTotalCount();
        //计算总页数
        long totalPages=totalRows%pageSize==0 ? totalRows/pageSize : (totalRows/pageSize)+1;
        //查询成功
        return new RPage<List<Activity>>().setCode(0).setMsg("查询成功")
                .setData(activityList)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setMaxRowsPerPage(20)
                .setVisiblePageLinks(3)
                .setTotalRows(totalRows)
                .setTotalPages(totalPages);
    }
    /**
    // * 分页查询 + 分页组件加载 + 条件过滤查询
    // * @param pageNoIndex
    // * @param pageSize
     以下四个参数有可能为空，在mybatis文件中进行动态sql判断处理

     // * @param activityName 市场活动名称，模糊查询
     // * @param username 用户名称，等值查询
     // * @param startDate 开始时间 大于等于开始时间
     // * @param endDate 结束时间 小于等于结束时间

    // */
    @RequestMapping("/getActivityListByPageComponentCondition.do")
    @ResponseBody
    public RPage<List<Activity>> getActivityListByPageCompentCondition(Integer pageNo, Integer pageSize,
                                                                        String activityName,
                                                                       String username,
                                                                       String startDate,
                                                                       String endDate) throws AjaxRequestException {
        //根据当前页，计算出查询的索引值位置
        int pageNoIndex=(pageNo-1)*pageSize;
        List<Activity> activityList=activityService.getActivityListByPageCondition(pageNoIndex,pageSize,activityName,username,startDate,endDate);
        if (ObjectUtils.isEmpty(activityList))
            throw new AjaxRequestException("当前市场活动无数据");
        //查询当前列表总记录数+条件过滤查询
        long totalRows=activityService.findActivityTotalCountCondition(activityName,username,startDate,endDate);
        //计算总页数
        long totalPages=totalRows%pageSize==0 ? totalRows/pageSize : (totalRows/pageSize)+1;
        //查询成功
        return new RPage<List<Activity>>().setCode(0).setMsg("查询成功")
                .setData(activityList)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setMaxRowsPerPage(20)
                .setVisiblePageLinks(3)
                .setTotalRows(totalRows)
                .setTotalPages(totalPages);
    }



    @RequestMapping("/saveActivity.do")
    @ResponseBody
    public R saveActivity(Activity activity, HttpSession session) throws AjaxRequestException {
        //获取创建人和创建时间（修改人和修改时间）
        String createBy=((User)session.getAttribute("user")).getName();
        String createTime=DateTimeUtil.getSysTime();

        boolean flag=activityService.saveActivity(activity,createBy,createTime);
        if(!flag)
            throw new AjaxRequestException("新增失败");
        return R.ok();
    }
}
