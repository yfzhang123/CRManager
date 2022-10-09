package com.bjpowernode.crm.workbench.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@Accessors(chain = true)
public class Activity implements Serializable {
    private String id;
    private String owner;
    private String name;
    private String startDate;
    private String endDate;
    private String cost;
    private String description;
    private String createTime;
    private String createBy;
    private String editTime;
    private String editBy;
    private String isDelete;

}
