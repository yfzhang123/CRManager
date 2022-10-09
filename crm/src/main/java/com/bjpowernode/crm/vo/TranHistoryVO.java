package com.bjpowernode.crm.vo;


import com.bjpowernode.crm.workbench.domain.TranHistory;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class TranHistoryVO implements Serializable {

    //封装实体类和其他属性关系

    private TranHistory tranHistory;
    private String possibility;

}
