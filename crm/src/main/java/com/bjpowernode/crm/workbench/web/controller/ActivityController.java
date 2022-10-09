package com.bjpowernode.crm.workbench.web.controller;

import com.alibaba.druid.util.StringUtils;
import com.bjpowernode.crm.entity.R;
import com.bjpowernode.crm.entity.RPage;
import com.bjpowernode.crm.exception.AjaxRequestException;
import com.bjpowernode.crm.exception.TraditionRequestException;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    //跳转到市场活动首页面
    @RequestMapping("/toActivityIndex.do")
    public String toIndex(){
        return "/workbench/activity/index";
    }
    /*查询市场活动列表（未分页）

     */
    @RequestMapping("/getActivityList.do")
    @ResponseBody
    /*public Map<String,Object> getActivityList() throws AjaxRequestException {
        List<Activity> activityList=activityService.getActivityList();
        if (ObjectUtils.isEmpty(activityList))
            throw new AjaxRequestException("当前市场活动无数据");
        //查询成功
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("code",0);
        resultMap.put("msg","查询成功");
        resultMap.put("data",activityList);
        return resultMap;
    }*/
    //自定义返回值结果集
    public R getActivityList() throws AjaxRequestException {
        List<Activity> activityList=activityService.getActivityList();
        if (ObjectUtils.isEmpty(activityList))
            throw new AjaxRequestException("当前市场活动无数据");
        //查询成功
        return R.ok(0,"查询成功",activityList);
    }
    //获取市场活动列表数据-分页查询
    @RequestMapping("/getActivityListByPage.do")
    @ResponseBody
    public R getActivityListByPage(Integer pageNo,Integer pageSize) throws AjaxRequestException {
        //根据当前页，计算出查询的索引值位置
        int pageNoIndex=(pageNo-1)*pageSize;
        List<Activity> activityList=activityService.getActivityList(pageNoIndex,pageSize);
        if (ObjectUtils.isEmpty(activityList))
            throw new AjaxRequestException("当前市场活动无数据");
        //查询成功
        return R.ok(0,"查询成功",activityList);
    }
    //获取市场活动列表数据-分页组件
    @RequestMapping("/getActivityListByPageComponent.do")
    @ResponseBody
    public RPage<List<Activity>> getActivityListByPageCompent(Integer pageNo, Integer pageSize) throws AjaxRequestException {
        //根据当前页，计算出查询的索引值位置
        int pageNoIndex=(pageNo-1)*pageSize;
        List<Activity> activityList=activityService.getActivityList(pageNoIndex,pageSize);
        if (ObjectUtils.isEmpty(activityList))
            throw new AjaxRequestException("当前市场活动无数据");
        //查询当前列表总记录数
        long totalRows=activityService.findActivityTotalCount();
        //计算总页数
        long totalPages=totalRows%pageSize==0 ? totalRows/pageSize : (totalRows/pageSize)+1;
        //查询成功
        return new RPage<List<Activity>>().setCode(0).setMsg("查询成功")
                .setData(activityList)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setMaxRowsPerPage(20)
                .setVisiblePageLinks(3)
                .setTotalRows(totalRows)
                .setTotalPages(totalPages);
    }
    /**
    // * 分页查询 + 分页组件加载 + 条件过滤查询
    // * @param pageNoIndex
    // * @param pageSize
     以下四个参数有可能为空，在mybatis文件中进行动态sql判断处理

     // * @param activityName 市场活动名称，模糊查询
     // * @param username 用户名称，等值查询
     // * @param startDate 开始时间 大于等于开始时间
     // * @param endDate 结束时间 小于等于结束时间

    // */
    @RequestMapping("/getActivityListByPageComponentCondition.do")
    @ResponseBody
    public RPage<List<Activity>> getActivityListByPageCompentCondition(Integer pageNo, Integer pageSize,
                                                                        String activityName,
                                                                       String username,
                                                                       String startDate,
                                                                       String endDate) throws AjaxRequestException {
        //根据当前页，计算出查询的索引值位置
        int pageNoIndex=(pageNo-1)*pageSize;
        List<Activity> activityList=activityService.getActivityListByPageCondition(pageNoIndex,pageSize,activityName,username,startDate,endDate);
        if (ObjectUtils.isEmpty(activityList))
            throw new AjaxRequestException("当前市场活动无数据");
        //查询当前列表总记录数+条件过滤查询
        long totalRows=activityService.findActivityTotalCountCondition(activityName,username,startDate,endDate);
        //计算总页数
        long totalPages=totalRows%pageSize==0 ? totalRows/pageSize : (totalRows/pageSize)+1;
        //查询成功
        return new RPage<List<Activity>>().setCode(0).setMsg("查询成功")
                .setData(activityList)
                .setPageNo(pageNo)
                .setPageSize(pageSize)
                .setMaxRowsPerPage(20)
                .setVisiblePageLinks(3)
                .setTotalRows(totalRows)
                .setTotalPages(totalPages);
    }



    @RequestMapping("/saveActivity.do")
    @ResponseBody
    public R saveActivity(Activity activity, HttpSession session) throws AjaxRequestException {
        //获取创建人和创建时间（修改人和修改时间）
        String createBy=((User)session.getAttribute("user")).getName();
        String createTime=DateTimeUtil.getSysTime();

        boolean flag=activityService.saveActivity(activity,createBy,createTime);
        if(!flag)
            throw new AjaxRequestException("新增失败");
        return R.ok();
    }
    @RequestMapping("/getActivityById.do")
    @ResponseBody
    public R getActivityById(String id) throws AjaxRequestException {
        Activity activity=activityService.findaActivityById(id);
        if(ObjectUtils.isEmpty(activity)){
            throw new AjaxRequestException("当前查询异常。。。。。");
        }
        return R.ok(0,"查询成功。。",activity);
    }

    @RequestMapping("/updateActivity.do")
    @ResponseBody
    public R updateActivity(Activity activity,HttpSession session) throws AjaxRequestException {
        String editBy =((User) session.getAttribute("user")).getName();
        String editTime=DateTimeUtil.getSysTime();
        boolean flag=activityService.updateActivityById(activity,editBy,editTime);
        if(!flag){
            throw new AjaxRequestException("修改失败");
        }
        return R.ok();
    }
    @RequestMapping("/deleteActivityList.do")
    @ResponseBody
    public R deleteActivityList(String[] activityIds,HttpSession session) throws AjaxRequestException {
        String editBy=((User) session.getAttribute("user")).getName();
        String editTime=DateTimeUtil.getSysTime();
        activityService.deleteActivityByIdList(activityIds,editBy,editTime);
        return R.ok();
    }

    @RequestMapping("/importActivity.do")
    public String importActivity(MultipartFile activityFile,HttpSession session) throws TraditionRequestException , IOException {
        //文件上传
        String owner=((User) session.getAttribute("user")).getId();
        String createBy=((User) session.getAttribute("user")).getName();
        String createTime=DateTimeUtil.getSysTime();
        //文件原始名称
//        System.out.println(1);
        String originalFilename=activityFile.getOriginalFilename();

        //文件后缀名
        String suffix=originalFilename.substring(
                originalFilename.lastIndexOf(".")+1
        );
        String timeFileName=DateTimeUtil.getSysTimeForUpload()+"."+suffix;
//        System.out.println(suffix);
        if(!(StringUtils.equalsIgnoreCase(suffix,"xls") || StringUtils.equalsIgnoreCase(suffix,"xlsx")) ){
            throw new TraditionRequestException("文件格式错误");
        }
      /*  if(!(suffix.equals("xls") || suffix.equals("xlsx"))) {
            throw new TraditionRequestException("文件格式错误");
        }*/
        //准备新的文件名称
        //可以使用UUID或时间戳重命名
        String uuidFileName= UUIDUtil.getUUID()+".xls";
        String url="D:/yangfan_desk/sixStage/project/CRM/crm/src/main/webapp/dataDir";

        //创建保存上传文件的磁盘路径
        File file=new File(url+"/"+timeFileName);
        if(!file.exists()){
            file.mkdirs();
        }
        activityFile.transferTo(file);
        //校验格式
        //批量导入
        InputStream in=new FileInputStream(file);
        Workbook workbook=null;
        if("xls".equals(suffix)){
            workbook=new HSSFWorkbook(in);
        }else {
            workbook=new XSSFWorkbook(in);
        }
        System.out.println(1);
        //获取页码对象，通过工作簿对象
        Sheet sheet=workbook.getSheetAt(0);
        //获取行对象，读取到最后的行号
        System.out.println(1);

        int lastRowNum=sheet.getLastRowNum();
        List<Activity> activityList=new ArrayList<>();
        System.out.println(activityList);
        System.out.println("lastRowNum:"+lastRowNum);
        for (int i = 0; i < lastRowNum; i++) {
            Row row =sheet.getRow(i+1);
            String name=row.getCell(0).getStringCellValue();
            String startDate=row.getCell(1).getStringCellValue();
            String endDate=row.getCell(2).getStringCellValue();
            Cell cost=row.getCell(3);
            cost.setCellType(CellType.STRING);
            String cost1=cost.getStringCellValue();
            String description=row.getCell(4).getStringCellValue();
            Activity activity=new Activity();
            activity.setName(name).setStartDate(startDate).setEndDate(endDate).setCost(cost1).setDescription(description)
                    .setIsDelete("0").setId(UUIDUtil.getUUID()).setCreateBy(createBy).setEditBy(createBy).setCreateTime(createTime).setOwner(owner);
            activityList.add(activity);
        }
        activityService.saveActivityList(activityList);


        return "redirect:/workbench/activity/toActivityIndex.do";

    }
    @RequestMapping("/exportActivityAll.do")
    public void exportActivityAll(HttpServletResponse response) throws TraditionRequestException, IOException {
        //将所有未删除市场活动数据查询出来
        List <Activity> activityList=activityService.findActivityList();
        if(ObjectUtils.isEmpty(activityList)){
            throw new TraditionRequestException("当前市场活动无数据");
        }
        exportActivity(response,activityList);


    }
    private void exportActivity(HttpServletResponse response, List<Activity> activityList) throws IOException {
        //封装到工作簿对象中
        Workbook workbook=new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("市场活动列表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("唯一标识");
        row.createCell(1).setCellValue("所有者");
        row.createCell(2).setCellValue("名称");
        row.createCell(3).setCellValue("开始时间");
        row.createCell(4).setCellValue("结束时间");
        row.createCell(5).setCellValue("成本");
        row.createCell(6).setCellValue("描述");
        row.createCell(7).setCellValue("创建人");
        row.createCell(8).setCellValue("创建时间");
        row.createCell(9).setCellValue("修改人");
        row.createCell(10).setCellValue("修改时间");
        for (int i = 0; i < activityList.size(); i++) {
            Row row1 = sheet.createRow(i + 1);
            Activity a = activityList.get(i);
            row1.createCell(0).setCellValue(a.getId());
            row1.createCell(1).setCellValue(a.getOwner());
            row1.createCell(2).setCellValue(a.getName());
            row1.createCell(3).setCellValue(a.getStartDate());
            row1.createCell(4).setCellValue(a.getEndDate());
            row1.createCell(5).setCellValue(a.getCost());
            row1.createCell(6).setCellValue(a.getDescription());
            row1.createCell(7).setCellValue(a.getCreateBy());
            row1.createCell(8).setCellValue(a.getCreateTime());
            row1.createCell(9).setCellValue(a.getEditBy());
            row1.createCell(10).setCellValue(a.getEditTime());
        }
        //设置响应头数据，响应数据类型为流类型
        response.setContentType("octets/stream");
        //设置响应文件名称
        response.setHeader("Content-Disposition","attachment;filename=Activity-"+DateTimeUtil.getSysTime()+".xls");
        //通过response响应到浏览器中
        //
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
    }

    @RequestMapping("/exportActivityXz.do")
    public void exportActivityXz(String[] activityIds,HttpServletResponse response) throws TraditionRequestException, IOException {
        List<Activity> activityList=activityService.findActivityListByIds(activityIds);
        if(ObjectUtils.isEmpty(activityList)){
            throw new TraditionRequestException("当前市场活动无数据");
        }
        exportActivity(response,activityList);


    }
    @RequestMapping("/toDetail.do")
    public String toDetail (String id, Model model) throws TraditionRequestException {
        Activity activity=activityService.findaActivityAndOwnerById(id);
        if(activity==null){
            throw new TraditionRequestException("查询异常");
        }
        model.addAttribute("activity",activity);
        return "/workbench/activity/detail";
    }
    @RequestMapping("/getActivityRemarkList.do")
    @ResponseBody
    public R getActivityRemarkList (String activityId){
        List<ActivityRemark> activityRemarkList=activityService.findActivityRemarkList(activityId);
        return R.ok(0,ObjectUtils.isEmpty(activityRemarkList)?"当前市场活动没有备注信息":"查询成功",activityRemarkList);
    }
    @RequestMapping("/remark/saveActivityRemark.do")
    @ResponseBody
    public R saveActivityRemark(String activityId,String noteContent,HttpSession session) throws AjaxRequestException {
        String createBy=((User) session.getAttribute("user")).getName();
        String createTime=DateTimeUtil.getSysTime();
        boolean flag=activityService.saveActivityRemark(
                new ActivityRemark()
                        .setNoteContent(noteContent)
                        .setActivityId(activityId)
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
    @RequestMapping("/remark/updateActivityRemark.do")
    @ResponseBody
    public R updateActivityRemark(String remarkId,String noteContent,HttpSession session ) throws AjaxRequestException {
        String editBy=((User) session.getAttribute("user")).getName();
        String editTime=DateTimeUtil.getSysTime();
        boolean flag=activityService.updateActivityRemarkById(remarkId,noteContent,editBy,editTime,"1");
        if(!flag){
            throw new AjaxRequestException("修改失败");
        }
        return R.ok();
    }
    //物理删除
    @RequestMapping("/remark/deleteRemark.do")
    @ResponseBody
    public R deleteRemark(String remarkId) throws AjaxRequestException {
        boolean flag=activityService.deleteActivityRemark(remarkId);
        if(!flag){
            throw new AjaxRequestException("删除失败");
        }
        return R.ok();

    }
}
