package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.entity.R;
import com.bjpowernode.crm.entity.RPage;
import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.exception.TraditionRequestException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/clue")
public class ClueController {
    @Autowired
    private ClueService clueService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping("/toClueIndex.do")
    public String toIndex(){
        return "/workbench/clue/index";
    }
    @RequestMapping("/getClueList.do")
    @ResponseBody
    public R getClueList() throws AjaxRequestException {
        List<Clue> clueList=clueService.getClueList();
        if(ObjectUtils.isEmpty(clueList)){
            throw new AjaxRequestException("当前线索数据异常");
        }
        return R.ok(0,"查询成功",clueList);
    }
    @RequestMapping("/getClueListByPageComponent.do")
    @ResponseBody
    public RPage<List<Clue>> getClueListByPageComponent(Integer pageNo, Integer pageSize) throws AjaxRequestException {
        //根据当前页，计算出查询的索引值位置
        int pageNoIndex=(pageNo-1)*pageSize;
        List<Clue> clueList=clueService.getClueList(pageNoIndex,pageSize);
        if (ObjectUtils.isEmpty(clueList))
            throw new AjaxRequestException("当前线索模块无数据");
        //查询当前列表总记录数
        long totalRows=clueService.findClueTotalCount();
        //计算总页数
        long totalPages=totalRows%pageSize==0 ? totalRows/pageSize : (totalRows/pageSize)+1;
        //查询成功
        return new RPage<List<Clue>>().setCode(0).setMsg("查询成功")
                .setData(clueList)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setMaxRowsPerPage(20)
                .setVisiblePageLinks(3)
                .setTotalRows(totalRows)
                .setTotalPages(totalPages);
    }
    //
    /**
     // * 分页查询 + 分页组件加载 + 条件过滤查询
     // * @param pageNoIndex
     // * @param pageSize
     以下四个参数有可能为空，在mybatis文件中进行动态sql判断处理

     // * @param activityName 市场活动名称，模糊查询
     // * @param username 用户名称，等值查询
     // * @param startDate 开始时间 大于等于开始时间
     // * @param endDate 结束时间 小于等于结束时间
     "clueName":clueName, 客户名称  模糊查询
     "username":username,用户名称，等值查询
     "comp":comp,公司 等值查询
     "mphone":mphone,手机号  等值查询
     "phone":phone, 座机  等值查询
     "source":source, 来源 等值查询
     "state":state 状态  等值查询

     // */
    @RequestMapping("/getClueListByPageComponentCondition.do")
    @ResponseBody
    public RPage<List<Clue>> getClueListByPageCompentCondition(Integer pageNo, Integer pageSize,
                                                                       String clueName,
                                                                       String username,
                                                                       String comp,
                                                                       String mphone,String phone,
                                                               String source,
                                                               String state) throws AjaxRequestException {
        //根据当前页，计算出查询的索引值位置
        int pageNoIndex=(pageNo-1)*pageSize;
        List<Clue> clueList=clueService.getClueListByPageCondition(pageNoIndex,pageSize,clueName,username,comp,mphone,phone,source,state);
        if (ObjectUtils.isEmpty(clueList))
            throw new AjaxRequestException("当前线索无数据");
        //查询当前列表总记录数+条件过滤查询
        long totalRows=clueService.findClueTotalCountCondition(clueName,username,comp,mphone,phone,source,state);
        //计算总页数
        long totalPages=totalRows%pageSize==0 ? totalRows/pageSize : (totalRows/pageSize)+1;
        //查询成功
        return new RPage<List<Clue>>().setCode(0).setMsg("查询成功")
                .setData(clueList)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setMaxRowsPerPage(20)
                .setVisiblePageLinks(3)
                .setTotalRows(totalRows)
                .setTotalPages(totalPages);
    }
    //
    @RequestMapping("/saveClue.do")
    @ResponseBody
    public R saveClue(Clue clue, HttpSession session) throws AjaxRequestException {
        //获取创建人和创建时间（修改人和修改时间）
        String createBy=((User)session.getAttribute("user")).getName();
        String createTime= DateTimeUtil.getSysTime();

        boolean flag=clueService.saveClue(clue,createBy,createTime);
        if(!flag)
            throw new AjaxRequestException("新增失败");
        return R.ok();
    }
    //
    @RequestMapping("/getClueById.do")
    @ResponseBody
    public R getClueById(String id) throws AjaxRequestException {
        Clue clue=clueService.findClueById(id);
        if(ObjectUtils.isEmpty(clue)){
            throw new AjaxRequestException("当前查询异常。。。。。");
        }
        return R.ok(0,"查询成功。。",clue);
    }
    //
    @RequestMapping("/updateClue.do")
    @ResponseBody
    public R updateClue(Clue clue,HttpSession session) throws AjaxRequestException {
        String editBy =((User) session.getAttribute("user")).getName();
        String editTime=DateTimeUtil.getSysTime();
        boolean flag=clueService.updateClueById(clue,editBy,editTime);
        if(!flag){
            throw new AjaxRequestException("修改失败");
        }
        return R.ok();
    }
    //
    @RequestMapping("/deleteClueList.do")
    @ResponseBody
    public R deleteClueList(String[] clueIds) throws AjaxRequestException {
        clueService.deleteClueByIdList(clueIds);
        return R.ok();
    }
    @RequestMapping("/toClueDetail.do")
    public String toDetail (String id, Model model) throws TraditionRequestException {
        Clue clue=clueService.findClueAndOwnerById(id);
        if(clue==null){
            throw new TraditionRequestException("查询异常");
        }
        model.addAttribute("clue",clue);
        return "/workbench/clue/detail";
    }
    @RequestMapping("/getClueRemarkList.do")
    @ResponseBody
    public R getClueRemarkList (String clueId){
        List<ClueRemark> clueRemarkList=clueService.findClueRemarkList(clueId);
        return R.ok(0,ObjectUtils.isEmpty(clueRemarkList)?"当前线索没有备注信息":"查询成功",clueRemarkList);
    }
    @RequestMapping("/remark/updateClueRemark.do")
    @ResponseBody
    public R updateClueRemark(String remarkId,String noteContent,HttpSession session ) throws AjaxRequestException {
        String editBy=((User) session.getAttribute("user")).getName();
        String editTime=DateTimeUtil.getSysTime();
        boolean flag=clueService.updateClueRemarkById(remarkId,noteContent,editBy,editTime,"1");
        if(!flag){
            throw new AjaxRequestException("修改失败");
        }
        return R.ok();
    }
    @RequestMapping("/remark/saveClueRemark.do")
    @ResponseBody
    public R saveClueRemark(String clueId,String noteContent,HttpSession session) throws AjaxRequestException {
        String createBy=((User) session.getAttribute("user")).getName();
        String createTime=DateTimeUtil.getSysTime();
        boolean flag=clueService.saveClueRemark(
                new ClueRemark()
                        .setNoteContent(noteContent)
                        .setClueId(clueId)
                        .setId(UUIDUtil.getUUID())
                        .setEditFlag("0")
                        .setCreateBy(createBy)
                        .setCreateTime(createTime)
                        .setEditBy(createBy)
                        .setEditTime(createTime)
        );
        if(!flag)
            throw new AjaxRequestException("新增失败");
        return R.ok();
    }
    @RequestMapping("/remark/deleteRemark.do")
    @ResponseBody
    public R deleteRemark(String remarkId) throws AjaxRequestException {
        boolean flag=clueService.deleteClueRemark(remarkId);
        if(!flag){
            throw new AjaxRequestException("删除失败");
        }
        return R.ok();

    }
    @RequestMapping("/getActivityRelationList.do")
    @ResponseBody
    public R getActivityRelationList(String clueId){
//        List<Activity> activityList=clueService.findActivityRelationList(clueId);
        List<Map<String,String>> activityList=clueService.findActivityRelationListMap(clueId);

        return R.ok(0,ObjectUtils.isEmpty(activityList)?"当前无关联市场活动列表数据":"查询成功",activityList);
    }
    @RequestMapping("/deleteClueActivityRelation.do")
    @ResponseBody
    public R deleteClueActivityRelation(String carId) throws AjaxRequestException {
        boolean flag=clueService.deleteClueActivityRelation(carId);
        if(!flag){
            throw new AjaxRequestException("删除关联关系失败");
        }
        return R.ok();
    }

    /**
     * 根据线索id查询未关联活动
     * @param clueId
     * @return
     */
    @RequestMapping("/getActivityUnRelationList.do")
    @ResponseBody
    public R getActivityUnRelationList(String clueId){
        List<Activity> activityList=clueService.findActivityUnRelationList(clueId);
        return R.ok(0,ObjectUtils.isEmpty(activityList)?"当前线索没有未关联市场活动列表数据":"查询成功",activityList);
    }
    @RequestMapping("/searchLikeActivityUnRelationList.do")
    @ResponseBody
    public R searchLikeActivityUnRelationList(String clueId,String activityName){
        List <Activity> activityList=clueService.searchLikeActivityUnRelationList(clueId,activityName);
        return R.ok(0,ObjectUtils.isEmpty(activityList)?"当前线索没有未关联市场活动列表数据":"查询成功",activityList);
    }
    @RequestMapping("/saveClueActivityRelation.do")
    @ResponseBody
    public R saveClueActivityRelation(String clueId,String[] activityIds) throws AjaxRequestException {
        clueService.saveClueActivityRelation(clueId,activityIds);
        return R.ok();
    }
    @RequestMapping("/toConvert.do")
    public String toConvert(String id,Model model){
        Clue clue=clueService.findClueAndOwnerById(id);
        model.addAttribute("clue",clue);
        return "/workbench/clue/convert";
    }
    @RequestMapping("/getActivityResourceList.do")
    @ResponseBody
    public R getActivityResourceList(String clueId){
        List<Activity> activityList = clueService.findActivityRelationList(clueId);
        if(ObjectUtils.isEmpty(activityList)){
            activityList = activityService.findActivityList();
            return R.ok(0,"查询所有市场活动数据成功",activityList);

        }
        return R.ok(0,"查询已关联市场活动数据成功",activityList);
    }
    @RequestMapping("/convert.do")

    public String convert(String clueId, String flag, Tran tran,HttpSession session) throws TraditionRequestException {
        String createBy=((User) session.getAttribute("user")).getName();
        String createTime=DateTimeUtil.getSysTime();
        String owner=((User) session.getAttribute("user")).getId();
        //线索转换
        clueService.saveConvert(clueId,flag,owner,createBy,createTime,tran);
        //转换成功，跳转到线索首页面
        return "redirect:/workbench/clue/toClueIndex.do";

    }
}
