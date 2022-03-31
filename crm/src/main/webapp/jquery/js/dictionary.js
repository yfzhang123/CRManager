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

