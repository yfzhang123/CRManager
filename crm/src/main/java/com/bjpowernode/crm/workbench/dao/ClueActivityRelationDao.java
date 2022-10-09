package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.ArrayList;
import java.util.List;

public interface ClueActivityRelationDao {
    boolean delete(String carId);

    boolean insertList(ArrayList<ClueActivityRelation> carList);

    List<ClueActivityRelation> findListByClueId(String clueId);

    boolean deleteListByClueId(String clueId);

//    List<Clue> select(String clueId);
}
