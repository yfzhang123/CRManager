package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.entity.R;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/chart")
public class ChartController {
    @Autowired
    TranService tranService;
    @RequestMapping("/activity/toIndex.do")
    public String toActivityIndex(){
        return "/workbench/chart/activity/index";
    }
    @RequestMapping("/clue/toIndex.do")
    public String toClueIndex(){
        return "/workbench/chart/clue/index";
    }
    @RequestMapping("/customerAndContacts/toIndex.do")
    public String tocustomerAndContactsIndex(){
        return "/workbench/chart/customerAndContacts/index";
    }
    @RequestMapping("/transaction/toIndex.do")
    public String totransactionIndex(){
        return "/workbench/chart/transaction/index";
    }
    @RequestMapping("/transaction/getChartData.do")
    @ResponseBody
    public R getTransactionChartData(){

        //按照分组查询交易历史列表数据,将每个分组的数量查询出来
        //获取dataList
        List<Map<String,Object>> dataList = tranService.findTransactionChartData();


        //获取nameList
        List<String> nameList = new ArrayList<>();
        for (Map<String, Object> map : dataList) {
            String name = (String) map.get("name");
            nameList.add(name);
        }

        //将nameList和dataList封装到Map集合中返回到前端页面进行展示
        Map<String,List> resultMap = new HashMap<>();
        resultMap.put("dataList",dataList);
        resultMap.put("nameList",nameList);

        return R.ok(0,"查询成功",resultMap);
    }


}
