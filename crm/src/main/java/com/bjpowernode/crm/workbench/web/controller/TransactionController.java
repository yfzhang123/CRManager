package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.entity.R;
import com.bjpowernode.crm.entity.RPage;
import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.exception.TraditionRequestException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.vo.TranHistoryVO;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/transaction")
public class TransactionController {
    @Autowired
    TranService tranService;
    @Autowired
    UserService userService;
    @Autowired
    CustomerService customerService;
    @RequestMapping("/toTransaction.do")
    public String toIndex(){
        return "/workbench/transaction/index";
    }
    @RequestMapping("/getTranListByPageComponent.do")
    @ResponseBody
    public RPage<List<Tran>> getTranListByPageComponent(Integer pageNo, Integer pageSize) throws AjaxRequestException {
        //根据当前页，计算出查询的索引值位置
        int pageNoIndex=(pageNo-1)*pageSize;
        List<Tran> tranList=tranService.getTranList(pageNoIndex,pageSize);
        if (ObjectUtils.isEmpty(tranList))
            throw new AjaxRequestException("当前交易模块无数据");
        //查询当前列表总记录数
        long totalRows=tranService.findTranTotalCount();
        //计算总页数
        long totalPages=totalRows%pageSize==0 ? totalRows/pageSize : (totalRows/pageSize)+1;
        //查询成功
        return new RPage<List<Tran>>().setCode(0).setMsg("查询成功")
                .setData(tranList)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setMaxRowsPerPage(20)
                .setVisiblePageLinks(3)
                .setTotalRows(totalRows)
                .setTotalPages(totalPages);
    }
    @RequestMapping("/getTranListByPageComponentCondition.do")
    @ResponseBody
    public RPage<List<Tran>> getTranListByPageComponentCondition(Integer pageNo, Integer pageSize,
                                                               String source,
                                                               String name,
                                                               String owner,
                                                               String customerId,String contactsId,
                                                               String stage,
                                                               String type) throws AjaxRequestException {
        //根据当前页，计算出查询的索引值位置
        int pageNoIndex=(pageNo-1)*pageSize;
        List<Tran> tranList=tranService.getTranListByPageCondition(pageNoIndex,pageSize,source,name,owner,customerId,contactsId,stage,type);
        if (ObjectUtils.isEmpty(tranList))
            throw new AjaxRequestException("当前交易无数据");
        //查询当前列表总记录数+条件过滤查询
        long totalRows=tranService.findTranTotalCountCondition(source,name,owner,customerId,contactsId,stage,type);
        //计算总页数
        long totalPages=totalRows%pageSize==0 ? totalRows/pageSize : (totalRows/pageSize)+1;
        //查询成功
        return new RPage<List<Tran>>().setCode(0).setMsg("查询成功")
                .setData(tranList)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setMaxRowsPerPage(20)
                .setVisiblePageLinks(3)
                .setTotalRows(totalRows)
                .setTotalPages(totalPages);
    }
    @RequestMapping("/toSave.do")
    public String toSave(Model model) throws TraditionRequestException {
        List<User> userList = userService.findUserList();
        if(ObjectUtils.isEmpty(userList)){
            throw new TraditionRequestException("查询用户列表异常");
        }
        model.addAttribute("userList",userList);
        return "/workbench/transaction/save";
    }
    /*@RequestMapping("/toUpdate.do")
    public String toUpdate(){
        return "/workbench/transaction/update";
    }*/
    @RequestMapping("/getTranById.do")
    @ResponseBody
    public R getTranById(String id) throws AjaxRequestException {
        Tran tran=tranService.findTranById(id);

       /* model.addAttribute("activityID",activityID);
        model.addAttribute("contactsID",contactsID);*/
//        System.out.println(activityID+""+contactsID);

        if(ObjectUtils.isEmpty(tran)){
            throw new AjaxRequestException("当前查询异常。。。。。");
        }
        return R.ok(0,"查询成功。。",tran);
    }
    @RequestMapping("/deleteTranList.do")
    @ResponseBody
    public R deleteTranList(String[] tranIds) throws AjaxRequestException {
        tranService.deleteTranByIdList(tranIds);
        return R.ok();
    }
    @RequestMapping("/getCustomerName.do")
    @ResponseBody
    public List<String> getCustomerName(String name){
        List<String> nameList=customerService.findCustomerNameListLike(name);
        return nameList;
    }
    /**
     * 新增交易
     * @param tran
     * @return
     */
    @RequestMapping("/saveTran.do")
    @ResponseBody
    public R saveTran(Tran tran, String customerName, String possibility, HttpSession session) throws AjaxRequestException {

        String createBy = ((User) session.getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();

        tranService.saveTran(tran,customerName,createBy,createTime);

        return R.ok();

    }
    @RequestMapping("/updateTran.do")
    @ResponseBody
    public R updateTran(String id,Tran tran, String customerName, String possibility, HttpSession session) throws AjaxRequestException {

        String editBy = ((User) session.getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        tranService.updateTran(id,tran,customerName,editBy,editTime);

        return R.ok();

    }
    @RequestMapping("/getId.do")
    @ResponseBody
    public R getId(String id){
        String activityID=tranService.getActivityIdById(id);
        String contactsID=tranService.getContactsIdById(id);
        String[] data=new String[2];
        data[0]=activityID;
        data[1]=contactsID;
        return R.ok(0,"查询成功。。",data);

    }
    @RequestMapping("/toTranDetail.do")
    public String toTranDetail(String id, Model model, HttpServletRequest request) throws TraditionRequestException {
        Tran tran=tranService.findTranConvertById(id);
        if(tran==null){
            throw new TraditionRequestException("查询交易失败");
        }
        //加载阶段对应可能性

        Map<String,String> sapMap= (Map<String,String>)request.getServletContext().getAttribute("sapMap");
        String stage=tran.getStage();
        String possibility=sapMap.get(stage);
        model.addAttribute("tran",tran);
        model.addAttribute("possibility",possibility);
        return "/workbench/transaction/detail";
    }
    /**
     * 接收tranId,查询交易历史列表数据
     * @param tranId
     * @return
     */
    @RequestMapping("/getTranHistoryList.do")
    @ResponseBody
    public R getTranHistoryList(String tranId,HttpServletRequest request){

        List<TranHistory> historyList = tranService.findTranHistoryList(tranId);
        List<TranHistoryVO> voList = new ArrayList<>();

        Map<String,String> sapMap = (Map<String, String>) request.getServletContext().getAttribute("sapMap");

        //根据历史记录封装它的可能性数据
        //将数据封装到VO对象中
        for (TranHistory history : historyList) {
            TranHistoryVO vo = new TranHistoryVO();
            //根据阶段加载对应可能性
            String possibility = sapMap.get(history.getStage());
            vo.setTranHistory(history).setPossibility(possibility);
//            System.out.println("vo :::>>> "+vo);
            voList.add(vo);
        }

        return R.ok(0,"查询成功",voList);

    }

    /**
     * 更新阶段数据,新增交易历史
     * @param tranId
     * @param stage
     * @param money
     * @param expectedDate
     * @return
     */
    @RequestMapping("/updateTranAndHistory.do")
    @ResponseBody
    public R updateTranAndHistory(String tranId,
                                  String stage,
                                  String money,
                                  String expectedDate,
                                  HttpSession session) throws AjaxRequestException {

        //获取修改和新增的属性(修改人和修改时间/创建人和创建时间)
        String createBy = ((User) session.getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();

        tranService.updateTranAndHistory(tranId,stage,money,expectedDate,createBy,createTime);

        return R.ok();

    }



}
