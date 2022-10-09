package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Tran;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TranDao {
    boolean insert(Tran tran);

    List<Tran> findAllPage(@Param("pageNo")Integer pageNoIndex, @Param("pageSize") Integer pageSize);

    long findTotalCounts();

    List<Tran> findAllByPageCondition(@Param("pageNo") Integer pageNoIndex, @Param("pageSize") Integer pageSize, @Param("source") String source, @Param("name") String name, @Param("owner") String owner, @Param("customerId") String customerId, @Param("contactsId") String contactsId, @Param("stage") String stage, @Param("type") String type);

    long findTotalCountsCondition(@Param("source") String source, @Param("name") String name, @Param("owner") String owner, @Param("customerId") String customerId, @Param("contactsId") String contactsId, @Param("stage") String stage, @Param("type") String type);

    Tran findTranById(String id);

    void deleteTranByIdList(String[] tranIds);

    boolean update(@Param("owner") String owner,@Param("money") String money, @Param("name") String name, @Param("expectedDate") String expectedDate, @Param("customerId") String customerId, @Param("stage") String stage, @Param("type") String type, @Param("source") String source, @Param("activityId") String activityId, @Param("contactsId") String contactsId, @Param("editBy") String editBy, @Param("editTime") String editTime,@Param("description") String description, @Param("contactSummary") String contactSummary,@Param("nextContactTime") String nextContactTime,@Param("id") String id);


    String findActivityId(String id);

    String findContactsId(String id);

    Tran findTranConvertById(String id);

    boolean updateStageById(@Param("tranId") String tranId, @Param("stage") String stage, @Param("editBy") String createBy, @Param("editTime") String createTime);
}
