function selectAll(){
    //点击表头复选框，选中所有下方复选框标签
    $("#selectAllBtn").click(function (){

        //给每个复选框，添加name属性
        //通过jquery属性选择器，加载所有下方复选框标签
    var cks=$("input[name=ck]");
        //获取表头复选框选中状态，更新所有复选框的选中状态
    var flag=$(this).prop("checked");
    //更新所有遍历的复选框的选中状态
        for (var i = 0; i < cks.length; i++) {
            cks[i].checked=flag;
        }

    })
}
function revers(){
    //根据所有下方复选框，添加事件
    $("input[name=ck]").click(function (){
        //获取复选框数量
        var ckslength=$("input[name=ck]").length;
        //获取所有下方已选中复选框数量
        var ckds=$("input[name=ck]:checked").length;
        //比对，若数量相等，则全选
        if(ckds==ckslength){
            //更新表头复选框为选中状态
            $("#selectAllBtn").prop("checked",true);

        }else {
            $("#selectAllBtn").prop("checked",false);

        }
    })
}
function checkCode(){
     //给编码的输入框，添加失去焦点事件
    $("#code").blur(function (){
        //获取编码内容
        var code=$("#code").val();
        if(code==""){
            $("#msg").html("编码内容不能为空").css("color","#FF5555")
            return;
        }

        //取消提示信息
        $("#msg").html("")
        //    发送ajax请求，进行异步查询结果
        $.ajax({
            url:"settings/dictionary/type/checkCode.do",
            data:{"code":code},
            type:"POST",
            dataType:"json",
            success:function (data){
                //data:{code:0/1,msg:xxx}
                if(data.code==0){
                    //代表可以新增数据，编码未重复
                    $("#msg").html(data.msg).css("color","#77FF77");
                }else {
                    //显示返回信息
                    $("#msg").html(data.msg).css("color","#FF5555");
                }

            }
        })



    })
}
function saveDictionaryType() {
    $("#saveDictionaryTypeBtn").click(function (){
       var code= $("#code").val();
        if(code==""){
            $("#msg").html("编码不能为空").css("color","#FF5555");
            return;
        }
        var errMsg=$("#msg").html();
        if(errMsg!=""){
            return;
        }
        var name=$("#name").val();
        var description=$("#description").val();

        //校验通过，发送ajax请求
        $.ajax({
            url: "settings/dictionary/type/saveDictionaryType.do",
            data: {"code":code,
                "name":name,
                "description":description
            },
            type: "POST",
            dataType: "json",
            success:function (data){
                //data:{code:0/1,msg:xxx}
                if(data.code==0){
                    //新增成功，跳转到字典类型首页面
                    window.location.href="settings/dictionary/type/toIndex.do";
                }else {
                    //显示返回信息
                    $("#msg").html(data.msg).css("color","#FF5555");
                }
            }

        })

    })
}
function toTypeEdit() {
    $("#toTypeEditBtn").click(function (){
        //获取选中的复选框，只能选一个
        var cks=$("input[name=ck]:checked");
        if(cks.length!=1){
            //要么没有选中，要么选中多个
            alert("修改操作选中一条数据")
            return;
        }
        //选中一条数据
        var code=cks[0].value;
        if(code==""){
            alert("当前页面加载数据异常，请刷新后重试");
            return;
        }
        //发送传统请求
        // alert(cks[0].code)
        // alert(cks)
        window.location.href="settings/dictionary/type/toEdit.do?code="+code;
    })

}
function updateDictionaryType() {
    //字典类型修改页面更新按钮绑定点击事件
    $("#updateDictionaryTypeBtn").click(function (){
        //获取页面中的属性
        var code=$("#code").val();
        var name=$("#name").val();
        var description=$("#description").val();
        if(code==""){
            $("#msg").html("当前数据加载异常，请刷新后重试");
            return;
        }
        //校验通过，情况提示信息
        $("#msg").html("");
        //发送ajax请求，进行更新操作
        $.ajax({
            url: "settings/dictionary/type/updateDictionaryType.do",
            data: {
                "code":code,
                "name":name,
                description:description
            },
            type: "POST",
            dataType: "json",
            success:function (data){
                //data:{code:0/1,msg:xxx}
                if(data.code==0){
                    //新增成功，跳转到字典类型首页面,查看更新信息
                    window.location.href="settings/dictionary/type/toIndex.do";
                }else {
                    //显示返回信息
                    $("#msg").html(data.msg);
                }

            }

        })


    })
}

function batchDeleteDictionaryType(){
    $("#batchDeleteDictionaryTypeBtn").click(function (){
        //获取勾选的复选框数量
        var count =$("input[name=ck]:checked");
        //遍历所有复选框
        //获取复选框中的value属性，拼接成参数
        //http://localhost:8080/crm/xxx?codes=xxx&codes=xxx
        var params="";
        for (var i=0;i<count.length;i++){
            params+="codes="+count[i].value;
            //拼接分隔符
            if(i<count.length-1){
                params+="&";
            }
        }
        if(confirm("您确定要删除这些数据吗？")){
            //发送ajax请求，进行批量删除操作
            $.ajax({
                // url: "settings/dictionary/type/batchDeleteDictionaryType.do?"+params,
                url: "settings/dictionary/type/batchDeleteDictionaryTypeCondition.do?"+params,
                data: {},
                type: "POST",
                dataType: "json",
                success:function (data){
                    //data:{code:0/1,msg:xxx}
                    if(data.code==0){
                        //删除成功，跳转到字典类型首页面,查看更新信息
                        window.location.href="settings/dictionary/type/toIndex.do";
                    }else {
                        //显示返回信息
                        alert(data.msg+",不能删除的数据类型为:"+data.data);
                        //删除成功，跳转到字典类型首页面,查看更新信息
                        window.location.href="settings/dictionary/type/toIndex.do";
                        // $("#msg").html(data.msg);
                    }
                }
            })
        }
    })
}

function getDictionaryValueList() {

    //发生ajax请求，获取字典值列表数据
    $.ajax({
        // url: "settings/dictionary/type/batchDeleteDictionaryType.do?"+params,
        url: "settings/dictionary/value/getDictionaryValueList.do",
        data: {},
        type: "POST",
        dataType: "json",
        success:function (data){
            //data:{code:0/1,msg:xxx,data:[{字典值}]}
            if(data.code==0){
                //数据获取成功，异步加载
                //1 定义字符串标签
                var  html="";
                //2 遍历集合
                //参数1，data.data获取返回值中的集合数据
                //参数2，遍历时，执行的方法i代表便利的索引值，n代表遍历的对象
                $.each(data.data,function (i,n) {
                    //3 将页面中需要加载的标签封装到字符串标签中
                    //4  将字符串标签中的数据替换为动态的数据‘
                      html +='<tr class="'+(i%2==0?'active':'')+'">';
                      html +='<td><input type="checkbox" name="ck" value="'+n.id+'"/></td>';
                      html +='<td>'+(i+1)+'</td>';
                    // html +='<td>'+n.id+'</td>';
                      html +='<td>'+n.value+'</td>';
                      html +='<td>'+n.text+'</td>';
                      html +='<td>'+n.orderNo+'</td>';
                      html +='<td>'+n.typeCode+'</td>';
                      html +='</tr>';

                })

                //5 将字符串标签加载到页面容器中
                $("#dictionaryValueListBody").html(html);



            }else {
                //数据查询失败，弹出提示信息
                alert(data.msg);

            }
        }
    })

}
function selectValueAll(){
    //点击表头复选框，选中所有下方复选框标签
    $("#selectValueAll").click(function(){
        // alert(1);
        //给每个复选框，添加name属性
        //通过jquery属性选择器，加载所有下方复选框标签
        $("input[name=ck]").prop("checked",$(this).prop("checked"));
        //获取表头复选框选中状态，更新所有复选框的选中状态
        //更新所有遍历的复选框的选中状态
    })
}

function reversValueAll(){
    //之前通过给所有复选框进行绑定点击事件实现
    //现在不能实现，因为这种方式需要通过页面中标签来绑定
    //现在标签，在js代码中进行异步加载完成
    //父标签通过on方法给子标签绑定事件
    //参数1，事件名称
    //参数2，绑定对象（子标签）
    //参数3 点击后执行的方法
    $("#dictionaryValueListBody").on("click","input[name=ck]",function (){
        $("#selectValueAll").prop(
            "checked",
            $("input[name=ck]").length==$("input[name=ck]:checked").length
        )
    })
}
function getDictionaryTypeList(){
    $.ajax({
        url: "settings/dictionary/type/getDictionaryTypeList.do",
        data: {},
        type: "POST",
        dataType: "json",
        success:function (data){
            //data:{code:0/1,msg:xxx,data:[{字典类型}]}
            if(data.code==0){
                //定义字符串标签
                var html="";

                //便利返回值集合
                $.each(data.data,function(i,n){
                    //将页面加载标签内容封装到字符串标签，替换为动态数据

                    if(i==0)
                        html+="<option></option>";
                    //通过双引号进行数据嵌套
                    html+="<option value='"+n.code+"'>"+n.name+"</option>";
                    //通过单引号进行数据嵌套
                    // html+='<option value="'+n.code+'">'+n.name+'</option>';
                })
                //将字符串标签加载的页面标签容器中
                $("#create-dicTypeCode").html(html);
            }else {
                //显示返回信息
                alert(data.msg);

            }

        }

    })
}


function saveDictionaryValue(){
    $("#saveDictionaryValueBtn").click(function (){
        //校验编码和字典值
        var typeCode=$("#create-dicTypeCode").val();
        if(typeCode==""){
            alert("请选择要关联的字典类型编码");
            return;
        }
        var value=$("#create-dicValue").val();
        if(value==""){
            alert("请输入字典值");
            return;
        }
        var text=$("#create-text").val();
        var orderNo=$("#create-orderNo").val();
        //发送ajax请求，进行新增操作
        $.ajax({
            url: "settings/dictionary/value/saveDictionaryValue.do",
            data: {
                "typeCode":typeCode,
                "value":value,
                "text":text,
                "orderNo":orderNo
            },
            type: "POST",
            dataType: "json",
            success:function (data){
                //data:{code:0/1,msg:xxx}
                if(data.code==0){
                    //新增成功，跳转到字典值首页面
                    window.location.href="settings/dictionary/value/toIndex.do";
                }else {
                    alert(data.msg);
                }
            }

        })

    })
}
function toValueEdit(){
    $("#toValueEditBtn").click(function (){
        //获取选中的复选框，只能选一个

        var cks=$("input[name=ck]:checked");
        if(cks.length!=1){
            //要么没有选中，要么选中多个
            alert("修改操作选中一条数据")
            return;
        }
        //选中一条数据
        var id=cks[0].value;

        if(id==""){
            alert("当前页面加载数据异常，请刷新后重试");
            return;
        }
        //发送传统请求

        window.location.href="settings/dictionary/value/toEdit.do?id="+id;
    })
}

function updateDictionaryValue() {
    //字典类型修改页面更新按钮绑定点击事件
    $("#updateDictionaryValueBtn").click(function (){
        //获取页面中的属性
        var typeCode=$("#value-typeCode").val();
        var value=$("#value-value").val();
        var text=$("#value-text").val();
        var orderNo=$("#value-orderNo").val();
        var id=$("#value-id").val();
        if(typeCode==""){
            $("#msg").html("当前数据加载异常，请刷新后重试");
            return;
        }
        //校验通过，情况提示信息
        $("#msg").html("");
        //发送ajax请求，进行更新操作
        $.ajax({
            url: "settings/dictionary/value/updateDictionaryValue.do",
            data: {
                "id":id,
                "typeCode":typeCode,
                "value":value,
                "text":text,
                "orderNo":orderNo
            },
            type: "POST",
            dataType: "json",
            success:function (data){
                //data:{code:0/1,msg:xxx}
                if(data.code==0){
                    //新增成功，跳转到字典类型首页面,查看更新信息
                    window.location.href="settings/dictionary/value/toIndex.do";
                }else {
                    //显示返回信息
                    $("#msg").html(data.msg);
                }

            }

        })


    })
}
function batchDeleteDictionaryValue(){
    $("#batchDeleteDictionaryValueBtn").click(function (){
        // alert("111")
        //获取勾选的复选框数量
        var count =$("input[name=ck]:checked");
        //遍历所有复选框
        //获取复选框中的value属性，拼接成参数
        //http://localhost:8080/crm/xxx?codes=xxx&codes=xxx
        var params="";
        for (var i=0;i<count.length;i++){
            params+="ids="+count[i].value;
            //拼接分隔符
            if(i<count.length-1){
                params+="&";
            }
        }
        // alert(params)
        if(confirm("您确定要删除这些数据吗？")){
            //发送ajax请求，进行批量删除操作
            $.ajax({
                url: "settings/dictionary/value/batchDeleteDictionaryValue.do?"+params,
                // url: "settings/dictionary/value/batchDeleteDictionaryValueCondition.do?"+params,
                data: {},
                type: "POST",
                dataType: "json",
                success:function (data){
                    //data:{code:0/1,msg:xxx}
                    if(data.code==0){
                        //删除成功，跳转到字典类型首页面,查看更新信息
                        // alert(data.msg)
                        window.location.href="settings/dictionary/value/toIndex.do";
                    }else {
                        //显示返回信息

                        //删除失败

                        $("#msg").html(data.msg);
                    }
                }
            })
        }
    })
}