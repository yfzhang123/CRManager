package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.exception.TraditionRequestException;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.domain.Tran;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean saveClue(Clue clue);

    Clue findClueById(String id);

    List<Clue> getClueList();

    List<Clue> getClueList(Integer pageNoIndex, Integer pageSize);

    long findClueTotalCount();

    List<Clue> getClueListByPageCondition(Integer pageNoIndex, Integer pageSize, String clueName, String username, String comp, String mphone, String phone, String source, String state);

    long findClueTotalCountCondition(String clueName, String username, String comp, String mphone, String phone, String source, String state);

    boolean saveClue(Clue clue, String createBy, String createTime) throws AjaxRequestException;

    boolean updateClueById(Clue clue, String editBy, String editTime);

    void deleteClueByIdList(String[] clueIds);

    Clue findClueAndOwnerById(String id);

    List<ClueRemark> findClueRemarkList(String clueId);

    boolean updateClueRemarkById(String remarkId, String noteContent, String editBy, String editTime, String editFlag);

    boolean saveClueRemark(ClueRemark setEditTime);

    boolean deleteClueRemark(String remarkId);

    List<Activity> findActivityRelationList(String clueId);

    boolean deleteClueActivityRelation(String carId);

    List<Map<String, String>> findActivityRelationListMap(String clueId);

    List<Activity> findActivityUnRelationList(String clueId);

    List<Activity> searchLikeActivityUnRelationList(@Param("clueId") String clueId, @Param("activityName") String activityName);

    void saveClueActivityRelation(String clueId, String[] activityIds) throws AjaxRequestException;

    void saveConvert(String clueId, String flag, String owner, String createBy, String createTime, Tran tran) throws TraditionRequestException;
}
