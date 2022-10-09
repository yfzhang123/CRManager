package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ClueDao {

    List<Clue> findAll();

    List<Clue> findAllByPage(@Param("pageNo")Integer pageNoIndex, @Param("pageSize") Integer pageSize);

    long findTotalCounts();

    List<Clue> findAllByPageCondition(
            @Param("pageNo") Integer pageNoIndex,
            @Param("pageSize") Integer pageSize,
            @Param("clueName") String clueName,
            @Param("username") String username,
            @Param("comp") String comp,
            @Param("mphone") String mphone,
            @Param("phone") String phone,
            @Param("source") String source,
            @Param("state") String state);

    long findTotalCountsCondition(
            @Param("clueName")String clueName,
            @Param("username")String username,
            @Param("comp") String comp,
            @Param("mphone")String mphone,
            @Param("phone")String phone,
            @Param("source")String source,
            @Param("state")String state);

    boolean saveClue(Clue clue);

    Clue findById(String id);

    boolean updateClueById(Clue clue);

    void deleteByIdList(String[] clueIds);

    Clue findClueById(String id);

    List<Activity> findRelationList(String clueId);

    List<Map<String, String>> findActivityRelationListMap(String clueId);

    List<Activity> findActivityUnRelationList(String clueId);

    List<Activity> findLikeActivityUnRelationList(@Param("clueId") String clueId, @Param("activityName") String activityName);

    boolean deleteById(String clueId);
}
