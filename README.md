# Crm
## 系统设置模块/用户模块
### 数据库表结构
* tbl_user
  * id  `唯一标识，32位随机字符串`
  * loginAct `用户名`
    * name    `昵称`
  * loginPwd   `密码`
    * email    `邮箱`
    * expireTime `过期时间`
  * deptno    ``
  * allowIps  `Ip允许访问列表`
  * createTime  `创建时间`
  * createBy    `创建人`
  * editTime    `修改时间`
    * editBy     `修改人`
### Ajax异步查询模块
```
$.ajax({
  url:"".
  data:{},
  type:"POST",
  dataType:"json",
  success:function(data){

  }
})
```