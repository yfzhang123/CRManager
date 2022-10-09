package com.bjpowernode.crm.workbench.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ActivityRemark implements Serializable {
    private String id;
    private String  noteContent ;
    private String createTime;
    private String createBy;
    private String editTime;
    private String editBy;
    private String editFlag;
    private String activityId;
}
