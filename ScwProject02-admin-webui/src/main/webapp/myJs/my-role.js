//声明专门的函数显示确认模态框
function showConfirmModal(roleArray) {

//    打开模态框
    $("#removeModal").modal("show");

//    清除旧的数据
    $("#roleNameSpan").empty();

//    在全局变量范围创建数组用来存放角色id
       window.roleArray = [];

//    遍历roleArray数组
    $.each(roleArray, function (i, n) {
           var roleName= n.roleName;
           $("#roleNameSpan").append(roleName+"</br>");

        var roleId = n.roleId;
    //调用数组对象的push()方法存入新元素
        window.roleArray.push(roleId);
    })


}


//生成分页效果，读取全局变量，不传参，灵活性较高
function generatePage() {

//    1.获取分页数据
    var pageInfo = getPageInfoRemote();

//    2.填充表格
    fillableBody(pageInfo);

    //生成导航条
    generateNavigator(pageInfo);
}

/*
remote : 远程
 */

// 远程访问服务器端程序获取 pageInfo 数据
function getPageInfoRemote() {

    /*
      // 异步方式
      $.ajax({
           url: "role/get/page/info.json",
           type: "post",
           dataType: "json",
           // async : false,//这里改成同步的，否则怕数据还没有取到就执行完成了
           data: {
               "pageNum": pageNum,
               "pageSize": pageSize,
               "keyword": keyword
           },
           success: function (data) {
               var  pageInfo = data.data;
               //取数据后生成表格
               fillableBody(pageInfo)
               console.log(pageInfo)
           },
           error: function (data) {
               console.log(data)
           }
       });*/

    //同步方式,调用$.ajax()函数发送请求并接受$.ajax()函数的返回值
    var ajaxResult = $.ajax({
        url: "role/get/page/info.json",
        type: "post",
        dataType: "json",
        async: false,//这里改成同步的，否则怕数据还没有取到就执行完成了
        data: {
            "pageNum": pageNum,
            "pageSize": pageSize,
            "keyword": keyword
        }
    });


//    判断当前响应状态码是否为200
    var statusCode = ajaxResult.status;


    if (statusCode != 200) {
        layer.msg("服务器端程序调用失败！响应码=" + statusCode + " 说明信息=" + ajaxResult.statusText)
        return null;
    }
//      加入状态码是200，说明请求成功，获取pageInfo
//    从    console.log(ajaxResult);的打印结果来看，responseJSON保存了后台传过来的json数据
    var resultEntity = ajaxResult.responseJSON;

//    从resultEntity获取result属性，后台数据的状态
    var result = resultEntity.result;

//    判断result是否成功
    if (result == "FAIL") {
        layer.msg("访问失败!Message：" + resultEntity.message);
        return null;
    }

//    所有数据都无异常,就开始返回数据
    var pageInfo = resultEntity.data;
    return pageInfo;


}

// 填充表格数据
function fillableBody(pageInfo) {

    //清除tbody中旧的数据
    $("#rolePageBody").empty();

    //为了让没有搜索结果时不显示页码导航条
    $("#Pagination").empty();

    var html = "";
    //判断pageInfo是否有效
    if (pageInfo == null ||
        typeof (pageInfo) == "undefined" ||
        pageInfo.list == null ||
        pageInfo.list.length == 0) {

        html += "<tr><td colspan='4'>抱歉，没有查询到您要的数据</td></tr>";
        $("#rolePageBody").html(html);
        return null;
    } else {
        $.each(pageInfo.list, function (i, r) {
            html += " <tr>";
            html += " <td><input type='checkbox' name='xz' value=" + r.id + " name='id'></td>";
            html += " <td>" + (i + 1) + "</td>";
            html += " <td>" + r.name + "</td>";
            html += "  <td>";
            html += "<button id='"+r.id+"' type='button' class='btn btn-success btn-xs checkBtn'><i class='glyphicon glyphicon-check'></i></button>";
            html += "<button type='button' id=" + r.id + " class='btn btn-primary btn-xs editShowBtn'><i class='glyphicon glyphicon-pencil '></i></button>";
            html += "<button type='button'  id=" + r.id + " class='btn btn-danger btn-xs removeBtn'><i class='glyphicon glyphicon-remove'></i></button>";
            html += " </td>";
            html += " </tr>";
        })

    }
    //填充数据
    $("#rolePageBody").html(html);

}

// 生成分页页码导航条
function generateNavigator(pageInfo) {

//    获取总记录数
    var totalRecord = pageInfo.total;

//    声明相关属性
    var properties = {
        //如1 2 3...20 21 22...50 51 52,即在左右两边的显示的几条页码即为边缘页，中间为主体页
        num_edge_entries: 3,     //边缘页
        num_display_entries: 5,    //主体页
        callback: paginationCallback,  //回调函数
        items_per_page: pageInfo.pageSize,//每页显示的纪录数
        current_page: pageInfo.pageNum - 1,//设置一开始的页码,Pagination内部需要将页码设置为0才能用
        prev_text: "上一页", //上一页按钮文本
        next_text: "下一页" //下一页按钮文本
    };

    $("#Pagination").pagination(totalRecord, properties);

}

// 翻页时的回调函数
function paginationCallback(pageIndex, jQuery) {

    //修改windows对象的pageNum对象
    window.pageNum = pageIndex + 1;//因为在初始化函数设置为了0，则在我们使用的时候需要+再来使用

    //调用分页函数
    /*
    在    generatePage()函数中调用了getPageInfoRemote函数，
    而getPageInfoRemote函数中是包含了ajax，ajax请求调用了
    这三个变量并将这三个变量发送了出去，
    data: {
            "pageNum": pageNum,
            "pageSize": pageSize,
            "keyword": keyword
        }

    所以我们只需要调用generatePage函数即可
     */
    generatePage();

    //取消浏览器的默认行为
    return false;
}