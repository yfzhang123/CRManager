package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityDao {
    List<Activity> findAll();

    List<Activity> findAllByPage(@Param("pageNo") Integer pageNoIndex,
                                 @Param("pageSize") Integer pageSize);

    boolean insert(Activity activity);

    long findTotalCounts();

    List<Activity> findAllByPageCondition(@Param("pageNo") int pageNoIndex,
                                          @Param("pageSize") Integer pageSize,
                                          @Param("name") String activityName,
                                          @Param("username") String username,
                                          @Param("startDate") String startDate,
                                          @Param("endDate") String endDate);

    long findTotalCountsCondition( @Param("name") String activityName,
                                   @Param("username") String username,
                                   @Param("startDate") String startDate,
                                   @Param("endDate") String endDate);
}
