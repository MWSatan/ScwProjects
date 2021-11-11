//生成zTree的方法
function generateZTree() {
    //   1.创建JSON对象用于存储zTree的数据
    var setting = {
        "view": {
            "addDiyDom": myAddDivDom,  //自定义图标
            "addHoverDom": myAddHoverDom,   //添加鼠标移入节点范围时出现添加按钮组
            "removeHoverDom": myRemoveHoverDom,//添加鼠标移出节点范围时出现移除按钮组
        },
        /*
        特殊用途：当后台数据只能生成 url 属性，又不想实现点击节点跳转的功能时，
        可以直接修改此属性为其他不存在的属性名称     默认值："url",找不到所以不跳转并且因为不发请求，所以没有404
         */
        "data": {
            "key": {
                "url": "notExists"
            }
        }

    };

    //    2.准备生成zTree数据，发送ajax请求得到数据
    $.ajax({
        url: "menu/get/whole/tree.json",
        type: "get",
        dataType: "json",
        success: function (data) {

            var result = data.result;
            if (result == "SUCCESS") {
                //取出真实数据
                var zNodes = data.data;

                //    拿到数据后初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);

            } else {
                layer.msg("error!!!   " + data.message);
            }

        },
        error: function (data) {
            layer.msg("请求发送失败");
        }
    })

}

/*
treeNode的部分值
isParent: true/false  是否有父节点
level: 1              权限，o为根节点，1为分支节点，2为叶子节点
id: 7                 数据库中的id
children: Array(3) [ {…}, {…}, {…} ]   存在的子节点
icon: "glyphicon\r\nglyphicon-user"  后端传过来的icon,\r\为回车符
name: "实名认证审核"    当前节点的名称
nocheck: false
open: false               是否是打开状态
parentTId: "treeDemo_7"   当前节点的父节点的命名名称
pid: 7                    当前节点的父节点的id名称
tId: "treeDemo_8"         当前节点的父节点的命名名称
url: "auth_cert/index.htm"
zAsync: true/false             是否是异步

*/

// 添加鼠标移出节点范围时移除节点中添加的按钮组
function myRemoveHoverDom(treeId, treeNode) {


    var btnGroupIdBtn = treeNode.tId + "_btnGrp";

//    移除按钮
    $("#" + btnGroupIdBtn).remove();
}

// 鼠标移入节点范围时出现按钮组
function myAddHoverDom(treeId, treeNode) {



//    按钮组的标签结构为：<span><a><i></i></a><a><i></i></a></span>
//    按钮出现的位置为;节点中  treeDemo_n_a超链接的后面,使用::after标签

//    为了在需要移除按钮的时候能够精确定位到按钮组所在的span，需要给span设置有规律的id

    var btnGrp = treeNode.tId + "_btnGrp";

//    判断当前节点是否有添加按钮，防止重复添加,大于0就说明已经添加过了
    if ($("#" + btnGrp).length > 0) {
        return;
    }


//    找到附着按钮的组的超链接
//    anchor : 锚
    var anchorId = treeNode.tId + "_a";


    var addBtn = "<a id='" + treeNode.id + "' class='btn btn-info dropdown-toggle btn-xs addBtn'  style='margin-left:10px;padding-top:0px;' href='#' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";//添加按钮
    var removeBtn = "<a id='" + treeNode.id + "' class='btn btn-info dropdown-toggle btn-xs removeBtn' style='margin-left:10px;padding-top:0px;' title='删除子节点' href='#'>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";//删除按钮
    var editBtn = "<a id='" + treeNode.id + "' class='btn btn-info dropdown-toggle btn-xs editBtn' style='margin-left:10px;padding-top:0px;' href='#' title='修改子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";//修改按钮


//  拼接按钮用的html
    var btnHtml = "";
//    获取节点的等级，通过等级来决定添加的具体按钮
    var treeLevel = treeNode.level;
    if (treeLevel == 0) {
        //    说明是根节点，只能添加子节点
        btnHtml += addBtn;

    }

    if (treeLevel == 1) {
        //    说明是分支节点,可以添加，修改，删除需要判断是否有子节点
        btnHtml += addBtn;

        //添加修改按钮
        btnHtml += editBtn;

        //获取子节点的个数
        var childLength = treeNode.children.length;
        if (childLength == 0) {
            // 添加删除按钮
            btnHtml += removeBtn;
        }

    }

    if (treeLevel == 2) {
        //    说明是叶子节点，可以修改和删除
        btnHtml += editBtn;
        btnHtml += removeBtn;

    }
//    执行在超链接后面附加span元素的操作
    $("#" + anchorId).after("<span id='" + btnGrp + "'>" + btnHtml + "</span>");


}


//修改默认的图标
function myAddDivDom(treeId, treeNode) {

    /*//treeID是整个树形结构附着的ul标签的id
    console.log("treeId " +treeId);

    //当前树形节点的全部的数据，包括从后端查询得到的Menu对象的全部属性
    console.log(treeNode);*/

//   图标的显示 <span id="treeDemo_8_span">实名认证审核</span>，可以发现命名规则为treeDemo_顺序序号_命名


//   所以ul标签的当前节点名称可以通过访问treeNode的tId属性得到
    var spanId = treeNode.tId + "_ico"

//    根据控制图标的span标签找到这个span标签
    //    删除旧的class
    $("#" + spanId).removeClass();

//    添加新的class,即数据库中的icon属性，由于数据库已经经过后端传了进来，所以直接引用即可
    $("#" + spanId).addClass(treeNode.icon);

}

