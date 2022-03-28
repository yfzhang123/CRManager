function login() {
    //点击登录按钮，获取用户名和密码属性
    $("#loginBtn").click(function () {
        var loginAct = $("#loginAct").val();
        if (loginAct == "") {
            //没有输入用户名，不能够让它提交登录操作
            // return的含义就是跳出该方法，不再继续向下执行代码
            // alert("用户名不能为空");
            $("#msg").html("用户名不能为空");
            return;
        }
        // 对密码进行校验
        var loginPwd =$("#loginPwd").val();
        if(loginPwd ==""){
            $("#msg").html("密码不能为空");
            return;
        }
        //获取十天免登陆标记
        var flag=$("#flag").val();



        // 校验通过，将提示信息清空掉
        $("#msg").css("color","#00FF00");
        $("#msg").html("校验通过");
        //校验业务逻辑，用户名不能为空，密码也不能为空
        //通过ajax发送请求，进行登录操作，携带用户名和密码的参数
        $.ajax({
            url:"settings/user/login.do",
            data:{
                // 根据用户名和密码进行发送数据
                "loginAct":loginAct,
                "loginPwd":loginPwd,
                "flag":flag
            },
            type:"POST",
            dataType:"json",
            success:function(data){
                //接受服务区返回的响应数据
                //data:{code:xxx,msg:xxx,data:xxx}
                //code代表自定义的返回值的响应编码，0代表成功，1代表失败
                //msg代表编码的含义，code返回0，代表操作成功，可以根据指定msg提示信息，返回登录成功
                //data代表页面中需要加载的数据，加载列表时，需要，登录操作不需要
                if(data.code==0){
                    //操作成功
                    window.location.href="workbench/toIndex.do";
                    alert(data.msg);
                }else{
                    //操作失败，给出提示信息
                    alert(data.msg);
                    // $("#msg").html(data.msg);
                }

            }
        })


    });

}
function autoLogin(){
    //勾选/取消十天免登陆复选框时
    //设置标记，提交
    $("#autoLogin").click(function (){
      var ck=$(this).prop("checked");
      if(ck){
          //选中状态
          $("#flag").val("a");

      }else {
          //取消选中状态
          $("#flag").val("");
      }
    })
}