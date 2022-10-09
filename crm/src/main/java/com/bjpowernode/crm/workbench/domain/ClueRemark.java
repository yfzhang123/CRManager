package com.bjpowernode.crm.workbench.domain;

import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
public class ClueRemark {
    private String id;
    private String noteContent;
    private String createBy;
    private String createTime;
    private String editBy;
    private String editTime;
    private String editFlag;
    private String clueId;


}
