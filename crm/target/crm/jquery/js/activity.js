function getActivityList(){
    $.ajax({
        url: "workbench/activity/getActivityList.do",
        data: {},
        type: "POST",
        dataType: "json",
        success:function (data){
            //data:{code:0/1,msg:xxx,data:[{市场活动}]}
            if(data.code==0){
                //查询成功，异步加载
                var html="";
                $.each(data.data,function (i,n){
                    html+='<tr class="'+(i%2==0?'active':'')+'">';
                    html+='<td><input type="checkbox"/></td>';
                    html+='<td><a style="text-decoration: none; cursor: pointer;" onClick="window.location.href=workbench/activity/toActivityDetail.do+;">'+n.name+'</a></td>';
                    html+='<td>'+n.owner+'</td>';
                    html+='<td>'+n.startDate+'</td>';
                    html+='<td>'+n.endDate+'</td>';
                    html+='</tr>';

                })
                $("#activityListBody").html(html);
            }else {
                alert(data.msg);
            }
        }

    })
}
//分页查询
function getActivityListByPage(pageNo,pageSize) {
    $.ajax({
        url: "workbench/activity/getActivityListByPage.do",
        data: {
            "pageNo":pageNo,
            "pageSize":pageSize
        },
        type: "POST",
        dataType: "json",
        success:function (data){
            //data:{code:0/1,msg:xxx,data:[{市场活动}]}
            if(data.code==0){
                //查询成功，异步加载
                var html="";
                $.each(data.data,function (i,n){
                    html+='<tr class="'+(i%2==0?'active':'')+'">';
                    html+='<td><input type="checkbox" name="ck"/></td>';
                    html+='<td><a style="text-decoration: none; cursor: pointer;" onClick="window.location.href=workbench/activity/toActivityDetail.do+;">'+n.name+'</a></td>';
                    html+='<td>'+n.owner+'</td>';
                    html+='<td>'+n.startDate+'</td>';
                    html+='<td>'+n.endDate+'</td>';
                    html+='</tr>';

                })
                $("#activityListBody").html(html);
            }else {
                alert(data.msg);
            }
        }

    })
}
//分页查询+加载分页组件
function getActivityListByPageComponent(pageNo,pageSize){
    $.ajax({
        url: "workbench/activity/getActivityListByPageComponent.do",
        data: {
            "pageNo":pageNo,
            "pageSize":pageSize
        },
        type: "POST",
        dataType: "json",
        success:function (data){
            //未加载分页组件
            //data:{code:0/1,msg:xxx,data:[{市场活动}]}
            //加载分页组件的返回值
            //data:{pageNo:xxx,pageSize:xxx,data:[{市场活动}]}
            if(data.code==0){
                //查询成功，异步加载
                var html="";
                $.each(data.data,function (i,n){
                    html+='<tr class="'+(i%2==0?'active':'')+'">';
                    html+='<td><input type="checkbox" name="ck"/></td>';
                    html+='<td><a style="text-decoration: none; cursor: pointer;" onClick="window.location.href=workbench/activity/toActivityDetail.do+;">'+n.name+'</a></td>';
                    html+='<td>'+n.owner+'</td>';
                    html+='<td>'+n.startDate+'</td>';
                    html+='<td>'+n.endDate+'</td>';
                    html+='</tr>';

                })
                $("#activityListBody").html(html);
                //当异步数据加载完成后，初始化页面的分页组件
                $("#activityPage").bs_pagination({
                    currentPage:data.pageNo,//页码
                    rowsPerPage:data.pageSize,//每页显示的记录条数
                    maxRowsPerPage:data.maxRowsPerPage,//每页最多显示的记录条数
                    totalPages:data.totalPages,//总页数
                    totalRows:data.totalRows,
                    visiblePageLinks:data.visiblePageLinks,
                    showGoToPage: true,
                    showRowsPerPage: true,
                    showRowsInfo: true,
                    showRowsDefaultInfo: true,
                    //在组件中点击了，分页按钮或页面按钮都会执行下面的回调方法
                    onChangePage:function (event,data){
                        getActivityListByPageComponent(data.currentPage,data.rowsPerPage);
                    }

                });
            }else {
                alert(data.msg);
            }
        }

    })
}
//条件查询
function searchActivityCondition(){
    $("#searchBtn").click(function (){
        //获取查询条件 封装到分页查询方法中
        //ajax请求
        //将查询的条件,封装到隐藏域中
        var name = $("#search-name").val();
        var owner = $("#search-owner").val();
        var startDate = $("#search-startDate").val();
        var endDate = $("#search-endDate").val();

        $("#hidden-name").val(name);
        $("#hidden-owner").val(owner);
        $("#hidden-startDate").val(startDate);
        $("#hidden-endDate").val(endDate);

        //获取查询的条件,可以将它封装到分页查询的方法中
        //可以直接调用分页查询方法即可
        getActivityListByPageComponentCondition(1,2);

    })
}
/**
 * 分页查询 + 分页组件加载 + 条件过滤查询
 * @param pageNo
 * @param pageSize
 */
function getActivityListByPageComponentCondition(pageNo,pageSize) {

    //获取条件过滤查询的属性
    //var activityName = $("#search-name").val();
    //var username = $("#search-owner").val();
    //var startDate = $("#search-startDate").val();
    //var endDate = $("#search-endDate").val();

    //隐藏域中获取条件过滤查询的数据
    var activityName = $("#hidden-name").val();
    var username = $("#hidden-owner").val();
    var startDate = $("#hidden-startDate").val();
    var endDate = $("#hidden-endDate").val();

    $.ajax({
        url:"workbench/activity/getActivityListByPageComponentCondition.do",
        data:{
            "pageNo":pageNo,
            "pageSize":pageSize,
            "activityName":activityName,
            "username":username,
            "startDate":startDate,
            "endDate":endDate
        },
        type:"POST",
        dataType:"json",
        success:function(data) {
            //未加载分页组件:
            //data:{code:0/1,msg:xxx,data:[{市场活动}]}
            //加载分页组件:
            //data:{pageNo:xxx,pageSize:xxx,data:[{市场活动}...]...}
            if(data.code == 0){
                //查询成功,异步加载
                var html = "";

                $.each(data.data,function (i, n) {
                    html += '<tr class="'+(i%2==0?'active':'')+'">';
                    html += '<td><input type="checkbox" name="ck"/></td>';
                    html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'detail.jsp\';">'+n.name+'</a></td>';
                    html += '<td>'+n.owner+'</td>';
                    html += '<td>'+n.startDate+'</td>';
                    html += '<td>'+n.endDate+'</td>';
                    html += '</tr>';
                })

                $("#activityListBody").html(html);

                //当异步数据加载完成后,初始化页面的分页组件
                $("#activityPage").bs_pagination({
                    currentPage: data.pageNo, // 页码
                    rowsPerPage: data.pageSize, // 每页显示的记录条数
                    maxRowsPerPage: data.maxRowsPerPage, // 每页最多显示的记录条数
                    totalPages: data.totalPages, // 总页数
                    totalRows: data.totalRows, // 总记录条数

                    visiblePageLinks: data.visiblePageLinks, // 显示几个卡片

                    showGoToPage: true,
                    showRowsPerPage: true,
                    showRowsInfo: true,
                    showRowsDefaultInfo: true,

                    //当在组件中点击了,分页按钮或页码的按钮或跳转到第几页,都会执行下面的回调方法
                    onChangePage : function(event, data){
                        //回调自己的分页加载组件的方法
                        getActivityListByPageComponentCondition(data.currentPage , data.rowsPerPage);
                    }
                });
            }else{
                alert(data.msg);
            }
        }
    })
}

//全选与反选
function selectActivityAll(){
    //点击表头复选框，选中所有下方复选框标签
    $("#selectActivityAll").click(function(){
        // alert(1);
        //给每个复选框，添加name属性
        //通过jquery属性选择器，加载所有下方复选框标签
        $("input[name=ck]").prop("checked",$(this).prop("checked"));
        //获取表头复选框选中状态，更新所有复选框的选中状态
        //更新所有遍历的复选框的选中状态
    })

}
function reversActivityAll(){
    //之前通过给所有复选框进行绑定点击事件实现
    //现在不能实现，因为这种方式需要通过页面中标签来绑定
    //现在标签，在js代码中进行异步加载完成
    //父标签通过on方法给子标签绑定事件
    //参数1，事件名称
    //参数2，绑定对象（子标签）
    //参数3 点击后执行的方法
    $("#activityListBody").on("click","input[name=ck]",function (){
        $("#selectActivityAll").prop(
            "checked",
            $("input[name=ck]").length==$("input[name=ck]:checked").length
        )
    })
}
//日历控件
function initDateTimePicker(){
    $(".time").datetimepicker({
        minView:"month",
        language:"zh-CN",
        format:'yyyy-mm-dd',
        autoclose:true,
        todayBtn:true,
        pickerPosition:"bottom-left"

    });
}

function opencreateActivityModelBtn(){
    $("#opencreateActivityModelBtn").click(function (){
        $.ajax({
            url: "settings/user/getUserList.do",
            data: {},
            type: "POST",
            dataType: "json",
            success:function (data){
                //data:{code:0/1,msg:xxx,data:[{用户}]}
                if(data.code==0){
                    //异步加载，所有者下拉列表数据
                    var html="";
                    $.each(data.data,function (i,n){
                        html+="<option value='"+n.id+"'>"+n.name+"</option>";
                    })
                    $("#create-owner").html(html);
                    //设置默认选中当前登录用户
                    // alert($("#userId").val())
                    $("#create-owner").val($("#userId").val());
                    //打开模态窗口
                    //页面定义：data-toggle="modal":打开的是模态窗口
                    //  					data-target="#createActivityModal"：打开目标的模态窗口
                    //js代码打开模态窗口
                    $("#createActivityModal").modal("show");
                    //js代码关闭模态窗口
                    // $("#createActivityModel").modal("hide");


                }
            }

        })
    })

}

function saveActivityBtn(){
    $("#saveActivityBtn").click(function (){
        var owner=$("#create-owner").val();
        if(owner==""){
            alert("请选择所有者");
            return;
        }
        var name=$("#create-name").val();
        if(name==""){
            alert("请输入市场活动名称");
            return;
        }
        var startDate=$("#create-startDate").val();
        var endDate=$("#create-endDate").val();
        var cost=$("#create-cost").val();
        var description=$("#create-description").val();
        //校验通过，发送ajax请求进行新增，新增成功刷新列表页面
        $.ajax({
            url: "workbench/activity/saveActivity.do",
            data: {
                "owner":owner,
                "name":name,
                "startDate":startDate,
                "endDate":endDate,
                "cost":cost,
                "description":description


            },
            type: "POST",
            dataType: "json",
            success:function (data){
                if(data.code==0){
                    getActivityListByPageComponentCondition(1,2);
                    //关闭模态窗口
                    $("#createActivityModal").modal("hide");
                }

            }

        })
    })
}


function f(){
    $.ajax({
        url: "",
        data: {},
        type: "POST",
        dataType: "json",
        success:function (data){

        }

    })
}