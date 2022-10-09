package com.bjpowernode.crm.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
/**
封装公共分页返回值结果集
 */
@Data
@Accessors(chain = true)
//可以通过链式编程方式进行封装实体类
public class RPage<T> implements Serializable {
    private Integer code;
    private String msg;
    private Integer pageNo;//当前页
    private Integer pageSize;//每页记录数
    private Integer maxRowsPerPage;//每页最多显示的记录数，20
    private Integer visiblePageLinks;//显示几个卡片
    private long totalPages;//总页数
    private long totalRows;//总记录数
    private T data;//返回页面的数据，分页的数据


}
