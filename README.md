# Crm
## 系统设置/字典模块
### 修改字典类型
#### 跳转到修改页面回显数据
* 前端代码
```javascript
function toTypeEdit() {

    //给修改按钮,添加点击事件
    $("#toTypeEditBtn").click(function () {

        //获取选中的复选框,并且只能选中一个
        var cks = $("input[name=ck]:checked");

        if(cks.length != 1){
            //要么没有选中,要么选中了多个
            alert("修改操作能选中一条数据...");
            return;
        }
        
        //选中了一条数据,获取它的value属性(类型编码)
        var code = cks[0].value;
        
        if(code == ""){
            alert("当前页面加载数据异常,请刷新后再试...");
            return;
        }
        
        //发送传统请求
        window.location.href = "settings/dictionary/type/toEdit.do?code="+code;

    })
}
```

* 后台代码
```java
/**
 * 跳转到字典类型编辑页面
 *      根据编码查询字典类型数据
 * @param code
 * @return
 * @throws TranditionRequestException
 */
@RequestMapping("/type/toEdit.do")
public String toTypeEdit(String code,Model model) throws TranditionRequestException {

    //根据编码查询字典类型数据
    DictionaryType dictionaryType = dictionaryService.findDictionaryTypeByCode(code);

    if(dictionaryType == null)
        throw new TranditionRequestException("当前数据查询异常...");

    //封装到Model对象中
    model.addAttribute("dictionaryType",dictionaryType);

    //跳转到修改页面
    return "/settings/dictionary/type/edit";
}
```

#### 修改操作
* 前端代码
```javascript
function updateDictionaryType() {
    
    //给字典类型修改页面的更新按钮绑定点击事件
    $("#updateDictionaryTypeBtn").click(function () {
        
        //获取页面中的属性
        var code = $("#code").val();
        
        if(code == ""){
            $("#msg").html("当前数据加载异常,请刷新后再试...");
            return;
        }
        
        var name = $("#name").val();
        var description = $("#description").val();
        
        //校验通过,清空提示信息
        $("#msg").html("");
        
        //发送ajax请求,进行更新操作
        $.ajax({
            url:"settings/dictionary/type/updateDictionaryType.do",
            data:{
                "code":code,
                "name":name,
                "description":description
            },
            type:"POST",
            dataType:"json",
            success:function(data) {
                //data : {code:0/1,msg:xxx}
                //修改成功,跳转到字典类型的首页面,查看更新的信息
                if(data.code == 0){
                    window.location.href = "settings/dictionary/type/toIndex.do";
                }else{
                    $("#msg").html(data.msg);
                }
            }
        })
    })
}
```

* 后台代码
```java
/**
 * 更新字典类型操作
 *      根据编码更新类型名称和描述
 * @param dictionaryType
 * @return
 */
@RequestMapping("/type/updateDictionaryType.do")
@ResponseBody
public Map<String,Object> updateDictionaryType(DictionaryType dictionaryType) throws AjaxRequestException {

    //更新操作,以update方法名称开头,开启事务
    boolean flag = dictionaryService.updateDictionaryType(dictionaryType);

    if(!flag){
        //修改失败,抛出异常
        throw new AjaxRequestException("修改字典类型失败...");
    }

    Map<String,Object> resultMap = new HashMap<>();
    resultMap.put("code",0);
    resultMap.put("msg","修改成功...");

    return resultMap;

}
```

* Sql
```xml
<!--boolean update(DictionaryType dictionaryType);-->
<update id="update">
    update tbl_dic_type
    set name = #{name},description = #{description}
    where code = #{code}
</update>
```

### 批量删除
* 前端代码
```javascript
function batchDeleteDictionaryType() {

    //给删除按钮,绑定点击事件
    $("#batchDeleteDictionaryTypeBtn").click(function () {

        //获取勾选的复选框的数量
        var cks = $("input[name=ck]:checked");

        //遍历所有的复选框
        //获取复选框中的value属性,进行拼接成参数
        //值得注意的是,拼接的&,分隔符,是要比遍历的次数少一个
        //http://localhost:8080/crm/xxx?codes=xxx&codes=xxx
        var params = "";
        for (var i=0; i<cks.length; i++){

            params += "codes="+cks[i].value;

            //拼接分隔符
            if(i < cks.length -1)
                params += "&";

        }

        //console.log("params",params);

        //由于删除是一个非常危险的动作,给出提示信息
        if(confirm("您确定要删除这些数据嘛?")){
            //发送ajax请求,进行批量删除操作
            $.ajax({
                url:"settings/dictionary/type/batchDeleteDictionaryType.do?"+params,
                data:{
                },
                type:"POST",
                dataType:"json",
                success:function(data) {
                    //data : {code:0/1,msg:xxx}
                    if(data.code == 0){
                        //删除成功,刷新页面
                        //跳转到字典类型首页面,就是查询列表操作
                        window.location.href = "settings/dictionary/type/toIndex.do";
                    }else{
                        //删除失败,弹出提示信息
                    }
                }
            })
        }
    })
}
```

* 后台代码 `controller`
```java 
/**
 * 批量删除字典类型
 * @param codes
 * @return
 */
@RequestMapping("/type/batchDeleteDictionaryType.do")
@ResponseBody
public Map<String,Object> batchDeleteDictionaryType(String[] codes) throws AjaxRequestException {

    //批量删除
    boolean flag = dictionaryService.deleteDictionaryList(codes);

    if(!flag)
        throw new AjaxRequestException("删除失败...");

    Map<String,Object> resultMap = new HashMap<>();
    resultMap.put("code",0);
    resultMap.put("msg","删除成功...");

    return resultMap;
}
```

* 后台代码 `方式1,遍历删除` `service遍历删除`
```java
@Override
public boolean deleteDictionaryList(String[] codes) throws AjaxRequestException {

    //批量删除
    //1. 可以根据遍历的方式进行逐个删除
    for (String code : codes) {

        boolean flag = dictionaryTypeDao.deleteByCode(code);

        if(!flag)
            throw new AjaxRequestException("删除失败...");

    }

    return true;
}
```

* Sql
```xml
<!--boolean deleteByCode(String code);-->
<delete id="deleteByCode">
    delete from tbl_dic_type where code = #{code}
</delete>
```

* 后台代码 `方式2,批量删除` `service`
```java
@Override
public boolean deleteDictionaryList(String[] codes) throws AjaxRequestException {

    //批量删除
    //1. 可以根据遍历的方式进行逐个删除
    //for (String code : codes) {
    //    boolean flag = dictionaryTypeDao.deleteByCode(code);
    //    if(!flag)
    //        throw new AjaxRequestException("删除失败...");
    //}

    //2. 直接批量删除,通过sql语句
    boolean flag = dictionaryTypeDao.deleteListByCodes(codes);

    if(!flag)
        throw new AjaxRequestException("批量删除失败...");

    return true;
}
```

* Sql
```xml
<!--
    boolean deleteListByCodes(String[] codes);
    delete from tbl_dic_type where code in (?,?,?)
-->
<delete id="deleteListByCodes">
    delete from tbl_dic_type
    where code in
    <foreach collection="array" item="c" separator="," open="(" close=")">
        #{c}
    </foreach>
</delete>
```

### 考虑关联关系的批量删除
* 前端代码
```javascript
function batchDeleteDictionaryType() {

    //给删除按钮,绑定点击事件
    $("#batchDeleteDictionaryTypeBtn").click(function () {

        //获取勾选的复选框的数量
        var cks = $("input[name=ck]:checked");

        //遍历所有的复选框
        //获取复选框中的value属性,进行拼接成参数
        //值得注意的是,拼接的&,分隔符,是要比遍历的次数少一个
        //http://localhost:8080/crm/xxx?codes=xxx&codes=xxx
        var params = "";
        for (var i=0; i<cks.length; i++){

            params += "codes="+cks[i].value;

            //拼接分隔符
            if(i < cks.length -1)
                params += "&";

        }

        //console.log("params",params);

        //由于删除是一个非常危险的动作,给出提示信息
        if(confirm("您确定要删除这些数据嘛?")){
            //发送ajax请求,进行批量删除操作
            $.ajax({
                //批量删除,字典类型
                //url:"settings/dictionary/type/batchDeleteDictionaryType.do?"+params,
                //批量删除,考虑一对多的关联关系
                url:"settings/dictionary/type/batchDeleteDictionaryTypeCondition.do?"+params,
                data:{
                },
                type:"POST",
                dataType:"json",
                success:function(data) {
                    //data : {code:0/1,msg:xxx}
                    if(data.code == 0){
                        //删除成功,刷新页面
                        //跳转到字典类型首页面,就是查询列表操作
                        window.location.href = "settings/dictionary/type/toIndex.do";
                    }else{
                        //删除失败,弹出提示信息
                        alert("当前数据,包含关联关系[ "+data.data+" ],请解除关联关系再在删除");
                        //跳转到字典类型首页面,就是查询列表操作
                        window.location.href = "settings/dictionary/type/toIndex.do";
                    }
                }
            })
        }
    })
}
```

* 后台代码 `controller`
```java
/**
 * 批量删除字典类型
 *      一方 : tbl_dic_type
 *      多方 : tbl_dic_value
 * @param codes
 * @return
 */
@RequestMapping("/type/batchDeleteDictionaryTypeCondition.do")
@ResponseBody
public Map<String,Object> batchDeleteDictionaryTypeCondition(String[] codes) throws AjaxRequestException {

    //批量删除
    List<String> codeList = dictionaryService.deleteDictionaryListCondition(codes);

    if(ObjectUtils.isEmpty(codeList)){
        //批量删除,全部执行成功,删除的数据,没有关联关系
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("code",0);
        resultMap.put("msg","删除成功...");

        return resultMap;
    }

    //有部分内容无法进行删除,说明包含了关联关系
    Map<String,Object> resultMap = new HashMap<>();
    resultMap.put("code",1);
    resultMap.put("msg","删除成功...");
    resultMap.put("data",codeList);

    return resultMap;

}
```

* 后台代码 `service`
```java
@Override
public List<String> deleteDictionaryListCondition(String[] codes) throws AjaxRequestException {

    List<String> codeList = new ArrayList<>();

    //遍历接收到的参数集合
    for (String code : codes) {

        //根据编码查询是否有一方关联的多方数据,字典值的列表
        int valueCount = dictionaryValueDao.findCountByTypeCode(code);

        if(valueCount == 0){
            //如果没有关联的列表数据,则可以删除
            boolean flag = dictionaryTypeDao.deleteByCode(code);

            if(!flag)
                throw new AjaxRequestException("删除失败...");

        }else{
            //如果有关联的列表数据,则无法删除
            //将无法删除的数据(code),存入到集合中返回到页面,进行提示给用户
            codeList.add(code);
        }

    }

    //将无法删除的编码集合返回
    return codeList;

}
```

* Sql
```xml
<!--int findCountByTypeCode(String code);-->
<select id="findCountByTypeCode" resultType="java.lang.Integer">
    select count(*) from tbl_dic_value where typeCode = #{code}
</select>
```

## 系统设置/字典值
### 加载字段值列表 `异步加载`
* 异步加载,不会阻塞页面显示,数据异步加载.
* 同步加载,所有数据都需要加载完成后,才会显示页面.
* 前端代码
```javascript
function getDictionaryValueList() {

    //发送ajax请求,获取字典值列表数据
    $.ajax({
        url:"settings/dictionary/value/getDictionaryValueList.do",
        data:{
        },
        type:"POST",
        dataType:"json",
        success:function(data) {
            //data:{code:0/1,msg:xxx,data:[{字典值}...]}
            if(data.code == 0){
                //数据获取成功,异步加载

                //异步加载步骤
                //1. 定义字符串标签
                var html = "";

                //2. 遍历集合
                //参数1,data.data获取返回值中的集合数据
                //参数2,遍历时,执行的方法,i代表遍历的索引值,n代表遍历的对象
                $.each(data.data,function (i, n) {

                    //3. 将页面中需要加载的标签封装到字符串标签中
                    //4. 将字符串标签中的数据,替换为动态的数据
                    html += '<tr class="'+(i%2==0?'active':'')+'">';
                    html += '<td><input type="checkbox" name="ck"/></td>';
                    html += '<td>'+(i+1)+'</td>';
                    html += '<td>'+n.value+'</td>';
                    html += '<td>'+n.text+'</td>';
                    html += '<td>'+n.orderNo+'</td>';
                    html += '<td>'+n.typeCode+'</td>';
                    html += '</tr>';

                })

                //5. 将字符串标签加载到页面容器中
                $("#dictionaryValueListBody").html(html);

            }else{
                //数据查询失败,弹出提示信息
                alert(data.msg);
            }
        }
    })
}
```

* 后台代码
```java
/**
 * 查询字典值列表
 * @return
 */
@RequestMapping("/value/getDictionaryValueList.do")
@ResponseBody
public Map<String,Object> getDictionaryValueList(){

    //查询字典值列表
    List<DictionaryValue> dictionaryValueList = dictionaryService.findDictionaryValueList();

    //封装返回值结果集
    Map<String,Object> resultMap = new HashMap<>();

    if(ObjectUtils.isEmpty(dictionaryValueList)){
        //没有字典值列表数据
        resultMap.put("code",1);
        resultMap.put("msg","当前列表无数据...");
        return resultMap;
    }

    resultMap.put("code",0);
    resultMap.put("msg","查询成功...");
    resultMap.put("data",dictionaryValueList);
    return resultMap;

}
```

### 全选和反选 `jquery`
* 前端代码
```javascript
function selectValueAll() {

    $("#selectValueAll").click(function () {

        //获取所有的复选框,设置它们的选中状态
        //jquery方式获取选中状态并设置
        // $("input[name=ck]").prop("checked",$(this).prop("checked"))
        //dom方式获取选中状态并设置
        $("input[name=ck]").prop("checked",this.checked)

    })
}


function reverseValueAll() {

    //之前我们通过给所有的复选框进行绑定点击事件来实现的
    //现在就无法实现了,因为这种方式需要通过页面中的标签来绑定
    // $("input[name=ck]").click(function () {
    //     alert("123")
    // })
    //但是现在标签,在js代码中进行异步加载完成
    //通过它的页面中的父标签来给子标签绑定事件完成
    //父标签通过on方法来给子标签绑定事件
    //参数1,事件名称
    //参数2,绑定的对象(子标签)
    //参数3,点击后执行的方法
    $("#dictionaryValueListBody").on("click","input[name=ck]",function () {
        $("#selectValueAll").prop(
            "checked",
            $("input[name=ck]").length == $("input[name=ck]:checked").length)
    })
}
```