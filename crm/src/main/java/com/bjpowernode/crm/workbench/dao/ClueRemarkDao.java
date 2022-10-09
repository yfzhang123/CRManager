package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClueRemarkDao {
    List<ClueRemark> findClueRemarkList(String clueId);

    boolean update(@Param("id") String remarkId, @Param("noteContent") String noteContent, @Param("editBy") String editBy, @Param("editTime") String editTime, @Param("editFlag") String editFlag);

    boolean save(ClueRemark setEditTime);

    boolean delete(String remarkId);

    List<ClueRemark> findListByClueId(String clueId);

    boolean deleteListByClueId(String clueId);
}
