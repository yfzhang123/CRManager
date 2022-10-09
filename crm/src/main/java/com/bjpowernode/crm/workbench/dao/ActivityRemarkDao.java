package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityRemarkDao {

    List<ActivityRemark> findAllByActivityId(String activityId);

    boolean insert(ActivityRemark activityRemark);

    boolean update(@Param("remarkId") String remarkId, @Param("noteContent") String noteContent, @Param("editBy") String editBy, @Param("editTime") String editTime, @Param("editFlag") String editFlag);

    boolean delete(String remarkId);
}
