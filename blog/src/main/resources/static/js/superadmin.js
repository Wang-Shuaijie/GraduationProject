
var deleteArticleId="";

$('.superAdminList .superAdminClick').click(function () {
    var flag = $(this).attr('class').substring(16);
    $('#statistics,#articleManagement,#articleTags,#articleCategories,#friendLink,#commentManagement,#userFeedback,#userManagement, #noticeManagement, #roleManagement').css("display","none");
    $("#" + flag).css("display","block");
});

//填充文章管理
function putInArticleManagement(data) {
    var articleManagementTable = $('.articleManagementTable');
    articleManagementTable.empty();
    var table = $('<table class="table am-table am-table-bd am-table-striped admin-content-table  am-animation-slide-right"></table>');
    table.append($('<thead>' +
        '<tr>' +
        '<th>文章标题</th><th>发布时间</th><th>文章分类</th><th>浏览量</th><th>操作</th>' +
        '</tr>' +
        '</thead>'));
    var tables = $('<tbody class="tables"></tbody>');
    $.each(data['result'], function (index, obj) {
        tables.append($('<tr id="a' + obj['id'] + '"><td><a href="">' + obj['title'] + '</a></td><td>' + timestampToYMDTime(obj['rptTime']) + '</td><td>' + obj['category'] + '</td> <td><span class="am-badge am-badge-success">' + (obj['upvote']+obj['commentNum']) + '</span></td>' +
            '<td>' +
            '<div class="am-dropdown" data-am-dropdown>' +
            '<button class="articleManagementBtn articleEditor am-btn am-btn-secondary">审核</button>' +
            '<button class="articleDeleteBtn articleDelete am-btn am-btn-danger">删除</button>' +
            '</div>' +
            '</td>' +
            '</tr>'));
    });
    table.append(tables);
    articleManagementTable.append(table);
    articleManagementTable.append($('<div class="my-row" id="page-father">' +
        '<div id="articleManagementPagination">' +
        '<ul class="am-pagination  am-pagination-centered">' +
        '</ul>' +
        '</div>' +
        '</div>'));

    $('.articleManagementBtn').click(function () {
        var $this = $(this);
        var id = $this.parent().parent().parent().attr("id").substring(1);
        window.location.replace("/verify?cid=" + id);
    });
    $('.articleDeleteBtn').click(function () {
        var $this = $(this);
        deleteArticleId = $this.parent().parent().parent().attr("id").substring(1);
        $('#deleteAlter').modal('open');
    })
}

//填充反馈信息
function putInAllFeedback(data) {
    var feedbackInfos = $('.feedbackInfos');
    feedbackInfos.empty();
    if(data['result'].length == 0){
        feedbackInfos.append('<div class="noNews">这里空空如也</div>');
    } else {
        $.each(data['result'], function (index, obj) {
            var feedbackInfo = $('<div class="feedbackInfo"></div>');
            feedbackInfo.append('<div class="feedbackInfoTitle">' +
                '<span class="feedbackName">' + obj['nickName'] + '</span>' +
                '<span class="feedbackTime">' + obj['time'] + '</span>' +
                '</div>');
            feedbackInfo.append('<div class="feedbackInfoContent">' +
                '<span class="feedbackInfoContentWord">反馈内容：</span>' +
                obj['feedbackContent'] +
                '</div>');
            var feedbackInfoContact = $('<div class="feedbackInfoContact"></div>');
            if(obj['contactInfo'] !== ""){
                feedbackInfoContact.append('<span class="contactInfo">联系方式：</span>' +
                    obj['contactInfo']);
            } else {
                feedbackInfoContact.append('<span class="contactInfo">联系方式：</span>' + '无'
                );
            }
            feedbackInfo.append(feedbackInfoContact);
            feedbackInfos.append(feedbackInfo);
        });
        feedbackInfos.append($('<div class="my-row" id="page-father">' +
            '<div id="feedbackPagination">' +
            '<ul class="am-pagination  am-pagination-centered">' +
            '<li class="am-disabled"><a href="">&laquo; 上一页</a></li>' +
            '<li class="am-active"><a href="">1</a></li>' +
            '<li><a href="">2</a></li>' +
            '<li><a href="">3</a></li>' +
            '<li><a href="">4</a></li>' +
            '<li><a href="">5</a></li>' +
            '<li><a href="">下一页 &raquo;</a></li>' +
            '</ul>' +
            '</div>' +
            '</div>'));
    }
}

function putInAllComment(data) {
    var commentManagementTable = $('.commentContent');
    commentManagementTable.empty();
    var table = $('<table class="table am-table am-table-bd am-table-striped admin-content-table  am-animation-slide-right"></table>');
    table.append($('<thead>' +
        '<tr>' +
        '<th>文章id</th><th>发布时间</th><th>内容</th><th>评论者id</th><th>被评论者id</th><th>操作</th>' +
        '</tr>' +
        '</thead>'));
    var tables = $('<tbody class="tables"></tbody>');
    $.each(data['result'], function (index, obj) {
        tables.append($('<tr id="a' + obj['id'] + '"><td><a href="">' + obj['contentId'] + '</a></td><td>' + timestampToYMDTime(obj['commentTime']) + '</td><td>' + obj['commentContent'] + '</td><td>' + obj['answererId'] + '</td><td>' + obj['respondentId'] + '</td>' +
            '<td>' +
            '<div class="am-dropdown" data-am-dropdown>' +
            '<button class="commentManagementBtn articleEditor am-btn am-btn-secondary">查看</button>' +
            '<button class="commentDeleteBtn articleDelete am-btn am-btn-danger">删除</button>' +
            '</div>' +
            '</td>' +
            '</tr>'));
    });
    table.append(tables);
    commentManagementTable.append(table);
    commentManagementTable.append($('<div class="my-row" id="page-father">' +
        '<div id="articleManagementPagination">' +
        '<ul class="am-pagination  am-pagination-centered">' +
        '</ul>' +
        '</div>' +
        '</div>'));

    $('.commentManagementBtn').click(function () {
        var $this = $(this);
        var id = $this.parent().parent().parent().attr("id").substring(1);
        window.location.replace("/watch?id=" + id);
    });
    $('.commentDeleteBtn').click(function () {
        var $this = $(this);
        deleteArticleId = $this.parent().parent().parent().attr("id").substring(1);
        $('#deleteAlter').modal('open');
    })
}

function putInAllUser(data) {
    var userManagementTable = $('.userInfoContent');
    userManagementTable.empty();
    var table = $('<table class="table am-table am-table-bd am-table-striped admin-content-table  am-animation-slide-right"></table>');
    table.append($('<thead>' +
        '<tr>' +
        '<th>用户昵称</th><th>邮箱</th><th>手机号</th><th>激活状态(0:未已激活;1:激活)</th><th>是否可用(0:不可用;1:可用)</th><th>操作</th>' +
        '</tr>' +
        '</thead>'));
    var tables = $('<tbody class="tables"></tbody>');
    $.each(data['result'], function (index, obj) {
        tables.append($('<tr id="a' + obj['id'] + '"><td><a href="">' + obj['nickName'] + '</a></td><td>' +obj['email'] + '</td><td>' + obj['phone'] + '</td><td>' + obj['state'] + '</td><td>' + obj['enable'] + '</td>' +
            '<td>' +
            '<div class="am-dropdown" data-am-dropdown>' +
            '<button class="commentManagementBtn articleEditor am-btn am-btn-secondary">激活</button>' +
            '<button class="commentManagementBtn articleEditor am-btn am-btn-danger">注销</button>' +
            '</div>' +
            '</td>' +
            '</tr>'));
    });
    table.append(tables);
    userManagementTable.append(table);
    userManagementTable.append($('<div class="my-row" id="page-father">' +
        '<div id="articleManagementPagination">' +
        '<ul class="am-pagination  am-pagination-centered">' +
        '</ul>' +
        '</div>' +
        '</div>'));

    $('.commentManagementBtn').click(function () {
        var $this = $(this);
        var id = $this.parent().parent().parent().attr("id").substring(1);
        window.location.replace("/watch?id=" + id);
    });
    $('.commentDeleteBtn').click(function () {
        var $this = $(this);
        deleteArticleId = $this.parent().parent().parent().attr("id").substring(1);
        $('#deleteAlter').modal('open');
    })
}

function putInAllRoles(data) {
    var roleManagementTable = $('.roleContent');
    roleManagementTable.empty();
    var table = $('<table class="table am-table am-table-bd am-table-striped admin-content-table  am-animation-slide-right"></table>');
    table.append($('<thead>' +
        '<tr>' +
        '<th>用户昵称</th><th>邮箱</th><th>手机号</th><th>激活状态(0:未已激活;1:激活)</th><th>是否可用(0:不可用;1:可用)</th><th>操作</th>' +
        '</tr>' +
        '</thead>'));
    var tables = $('<tbody class="tables"></tbody>');
    $.each(data['result'], function (index, obj) {
        tables.append($('<tr id="a' + obj['id'] + '"><td><a href="">' + obj['nickName'] + '</a></td><td>' +obj['email'] + '</td><td>' + obj['phone'] + '</td><td>' + obj['state'] + '</td><td>' + obj['enable'] + '</td>' +
            '<td>' +
            '<div class="am-dropdown" data-am-dropdown>' +
            '<button class="commentManagementBtn articleEditor am-btn am-btn-secondary">晋升管理员</button>' +
            '</div>' +
            '</td>' +
            '</tr>'));
    });
    table.append(tables);
    roleManagementTable.append(table);
    roleManagementTable.append($('<div class="my-row" id="page-father">' +
        '<div id="articleManagementPagination">' +
        '<ul class="am-pagination  am-pagination-centered">' +
        '</ul>' +
        '</div>' +
        '</div>'));

    $('.commentManagementBtn').click(function () {
        var $this = $(this);
        var id = $this.parent().parent().parent().attr("id").substring(1);
        window.location.replace("/watch?id=" + id);
    });
}

function putInAllNotice(data) {
    var noticeInfos = $('.noticeInfos');
    noticeInfos.empty();
    if(data['result'].length == 0){
        noticeInfos.append('<div class="noNews">这里空空如也</div>');
    } else {
        var table = $('<table class="table am-table am-table-bd am-table-striped admin-content-table  am-animation-slide-right"></table>');
        table.append($('<thead>' +
            '<tr>' +
            '<th>公告内容</th><th>发布时间</th><th>操作</th>' +
            '</tr>' +
            '</thead>'));
        var tables = $('<tbody class="tables"></tbody>');
        $.each(data['result'], function (index, obj) {
            tables.append($('<tr id="a' + obj['id'] + '"><td><a href="">' + obj['content'] + '</a></td><td>' + timestampToYMDTime(obj['time']) + '</td>' +
                '<td>' +
                '<div class="am-dropdown" data-am-dropdown>' +
                '<button class="articleManagementBtn articleEditor am-btn am-btn-secondary">编辑</button>' +
                '<button class="articleDeleteBtn articleDelete am-btn am-btn-danger">删除</button>' +
                '</div>' +
                '</td>' +
                '</tr>'));
        });
        table.append(tables);
        noticeInfos.append(table);
        noticeInfos.append($('<div class="my-row" id="page-father">' +
            '<div id="articleManagementPagination">' +
            '<ul class="am-pagination  am-pagination-centered">' +
            '</ul>' +
            '</div>' +
            '</div>'));

        $('.articleManagementBtn').click(function () {
            var $this = $(this);
            var id = $this.parent().parent().parent().attr("id").substring(1);
            window.location.replace("/editor?id=" + id);
        });
        $('.articleDeleteBtn').click(function () {
            var $this = $(this);
            deleteArticleId = $this.parent().parent().parent().attr("id").substring(1);
            $('#deleteAlter').modal('open');
        })
    }
}

//获得文章管理文章
function getArticleManagement(currentPage) {
    $.ajax({
        type:'post',
        url:'/getArticleManagement',
        dataType:'json',
        data:{
            rows:5,
            pageNum:currentPage
        },
        success:function (data) {
            putInArticleManagement(data);
            scrollTo(0,0);//回到顶部

            //分页
            $("#articleManagementPagination").paging({
                rows:data['pageInfo']['pageSize'],//每页显示条数
                pageNum:data['pageInfo']['pageNum'],//当前所在页码
                pages:data['pageInfo']['pages'],//总页数
                total:data['pageInfo']['total'],//总记录数
                callback:function(currentPage){
                    getArticleManagement(currentPage);
                }
            });
        },
        error:function () {
            alert("获取文章信息失败");
        }
    });
}

function getAllComment(currentPage) {
    $.ajax({
        type:'post',
        url:'/getAllComment',
        dataType:'json',
        data:{
            rows:5,
            pageNum:currentPage
        },
        success:function (data) {
            putInAllComment(data);
            scrollTo(0,0);//回到顶部

            //分页
            $("#articleManagementPagination").paging({
                rows:data['pageInfo']['pageSize'],//每页显示条数
                pageNum:data['pageInfo']['pageNum'],//当前所在页码
                pages:data['pageInfo']['pages'],//总页数
                total:data['pageInfo']['total'],//总记录数
                callback:function(currentPage){
                    getAllComment(currentPage);
                }
            });
        },
        error:function () {
            alert("获取评论信息失败");
        }
    });
}

function getAllUsers(currentPage) {
    $.ajax({
        type:'post',
        url:'/getAllUser',
        dataType:'json',
        data:{
            rows:5,
            pageNum:currentPage
        },
        success:function (data) {
           putInAllUser(data);
            scrollTo(0,0);//回到顶部

            //分页
            $("#articleManagementPagination").paging({
                rows:data['pageInfo']['pageSize'],//每页显示条数
                pageNum:data['pageInfo']['pageNum'],//当前所在页码
                pages:data['pageInfo']['pages'],//总页数
                total:data['pageInfo']['total'],//总记录数
                callback:function(currentPage){
                    getAllUser(currentPage);
                }
            });
        },
        error:function () {
            alert("获取用户信息失败");
        }
    });
}

//获得反馈信息
function getAllFeedback(currentPage) {
    $.ajax({
        type:'get',
        url:'/getAllFeedback',
        dataType:'json',
        data:{
            rows:10,
            pageNum:currentPage
        },
        success:function (data) {
            putInAllFeedback(data);
            scrollTo(0,0);//回到顶部

            //分页
            $("#feedbackPagination").paging({
                rows:data['pageInfo']['pageSize'],//每页显示条数
                pageNum:data['pageInfo']['pageNum'],//当前所在页码
                pages:data['pageInfo']['pages'],//总页数
                total:data['pageInfo']['total'],//总记录数
                callback:function(currentPage){
                    getAllFeedback(currentPage);
                }
            });
        },
        error:function () {
            alert("获取反馈信息失败");
        }
    });
}

//获得文章分类信息
function getArticleCategories() {
    $.ajax({
        type:'get',
        url:'/getArticleCategories',
        dataType:'json',
        data:{
        },
        success:function (data) {
            var categoryContent = $('.categoryContent');
            categoryContent.empty();
            categoryContent.append($('<div class="contentTop">' +
                '目前分类数：<span class="categoryNum">' + data['result'].length + '</span>' +
                '<div class="updateCategory">' +
                '<a class="addCategory"><i class="am-icon-plus-square"></i> 添加分类</a> / ' +
                '<a class="subCategory"><i class="am-icon-minus-square"></i> 删除分类</a>' +
                '</div>'));
            var categories = $('<div class="categories"></div>');
            $.each(data['result'], function (index, obj) {
                categories.append($('<span id="p' + obj['id'] + '" class="category">' + obj['categoryName'] + '</span>'));
            })
            categoryContent.append(categories);

            // 添加分类
            $('.addCategory').click(function () {
                $('#addCategory').modal({
                    relatedTarget: this,
                    onConfirm: function(e) {
                        var categoryName = e.data;
                        categoryName = $.trim(categoryName);
                        if(categoryName == ""){
                            dangerNotice("分类名不能为空！");
                        }else {
                            updateCategory(categoryName, 1);
                        }
                    },
                    onCancel: function(e) {
                    }
                });
            })

            // 删除分类
            $('.subCategory').click(function () {
                $('#subCategory').modal({
                    relatedTarget: this,
                    onConfirm: function(e) {
                        var categoryName = e.data;
                        categoryName = $.trim(categoryName);
                        if(categoryName == ""){
                            dangerNotice("分类名不能为空！");
                        }else {
                            updateCategory(categoryName, 2);
                        }
                    },
                    onCancel: function(e) {
                    }
                });
            })
        },
        error:function () {

        }
    });
}

//获得文章tag信息
function getArticleTags() {
    $.ajax({
        type:'get',
        url:'/getArticleTags',
        dataType:'json',
        data:{
        },
        success:function (data) {
            var tagContent = $('.tagContent');
            tagContent.empty();
            tagContent.append($('<div class="contentTop">' +
                '目前tag数：<span class="categoryNum">' + data['result'].length + '</span>' +
                '<div class="updateCategory">' +
                '<a class="addTag"><i class="am-icon-plus-square"></i> 添加标签</a> / ' +
                '<a class="subTag"><i class="am-icon-minus-square"></i> 删除标签</a>' +
                '</div>'));
            var categories = $('<div class="categories"></div>');
            $.each(data['result'], function (index, obj) {
                categories.append($('<span id="p' + obj['id'] + '" class="category">' + obj['tagName'] + '</span>'));
            })
            tagContent.append(categories);

            // 添加分类
            $('.addTag').click(function () {
                $('#addTag').modal({
                    relatedTarget: this,
                    onConfirm: function(e) {
                        var tagName = e.data;
                        tagName = $.trim(tagName);
                        if(tagName == ""){
                            dangerNotice("标签名不能为空！");
                        }else {
                            updateTag(tagName, 1);
                        }
                    },
                    onCancel: function(e) {
                    }
                });
            })

            // 删除分类
            $('.subTag').click(function () {
                $('#subTag').modal({
                    relatedTarget: this,
                    onConfirm: function(e) {
                        var tagName = e.data;
                        tagName = $.trim(tagName);
                        if(tagName == ""){
                            dangerNotice("分类名不能为空！");
                        }else {
                            updateTag(tagName, 2);
                        }
                    },
                    onCancel: function(e) {
                    }
                });
            })
        },
        error:function () {

        }
    });
}

function getAllRoles(currentPage){
    $.ajax({
        type:'post',
        url:'/getAllUser',
        dataType:'json',
        data:{
            rows:5,
            pageNum:currentPage
        },
        success:function (data) {
            putInAllRoles(data);
            scrollTo(0,0);//回到顶部

            //分页
            $("#articleManagementPagination").paging({
                rows:data['pageInfo']['pageSize'],//每页显示条数
                pageNum:data['pageInfo']['pageNum'],//当前所在页码
                pages:data['pageInfo']['pages'],//总页数
                total:data['pageInfo']['total'],//总记录数
                callback:function(currentPage){
                    getAllRoles(currentPage);
                }
            });
        },
        error:function () {
            alert("获取用户信息失败");
        }
    });
}

function getAllNotice(currentPage){
    $.ajax({
        type:'get',
        url:'/getAllNotice',
        dataType:'json',
        data:{
            rows:10,
            pageNum:currentPage
        },
        success:function (data) {
            putInAllNotice(data);
            scrollTo(0,0);//回到顶部

            //分页
            $("#articleManagementPagination").paging({
                rows:data['pageInfo']['pageSize'],//每页显示条数
                pageNum:data['pageInfo']['pageNum'],//当前所在页码
                pages:data['pageInfo']['pages'],//总页数
                total:data['pageInfo']['total'],//总记录数
                callback:function(currentPage){
                    getAllNotice(currentPage);
                }
            });
        },
        error:function () {
            alert("获取公告信息失败");
        }
    });
}

//点击反馈
$('.superAdminList .userFeedback').click(function () {
    getAllFeedback(1);
});
//点击文章管理
$('.superAdminList .articleManagement').click(function () {
    getArticleManagement(1);
});
//点击评论管理
$('.superAdminList .commentManagement').click(function () {
    getAllComment(1);
});
//点击标签管理
$('.superAdminList .articleTags').click(function () {
    getArticleTags(1);
});
//点击用户管理
$('.superAdminList .userManagement').click(function () {
    getAllUsers(1);
});
//点击权限管理
$('.superAdminList .roleManagement').click(function () {
    getAllRoles(1);
});
//点击分类管理
$('.superAdminList .articleCategories').click(function () {
    getArticleCategories(1);
});
//点击公告管理
$('.superAdminList .noticeManagement').click(function () {
    getAllNotice(1);
});
//点击友链管理
$('.superAdminList .friendLink').click(function () {
    $.ajax({
        type:'post',
        url:'/getFriendLink',
        dataType:'json',
        data:{
        },
        success:function (data) {
            var friendLinkContent = $('.friendLinkContent');
            friendLinkContent.empty();
            friendLinkContent.append($('<div class="contentTop">' +
                '目前友链数：' +
                '<span class="friendLinkNum">' + data['data'].length + '</span>' +
                '<div class="updateFriendLink">' +
                '<a class="addFriendLink"><i class="am-icon-plus-square"></i> 添加友链</a>' +
                '</div>' +
                '</div>'));
            var table = $('<table class="am-table am-table-bd am-table-striped admin-content-table  am-animation-slide-right"></table>');
            table.append($('<thead>' +
                '<tr>' +
                '<th>博主</th><th>博客地址</th><th>操作</th>' +
                '</tr>' +
                '</thead>'));
            var friendLinkManagementTable = $('<tbody class="friendLinkManagementTable"></tbody>');
            for(var i in data['data']){
                var friendLink = $('<tr id="p' + data['data'][i]['id'] + '">' +
                    '<td class="blogger">' + data['data'][i]['name'] + '</td>' +
                    '<td class="url">' + data['data'][i]['url'] + '</td>' +
                    '<td>' +
                    '<div class="am-dropdown" data-am-dropdown="">' +
                    '<button class="friendLinkManagementBtn articleEditor am-btn am-btn-secondary">编辑</button>' +
                    '<button class="friendLinkDeleteBtn articleDelete am-btn am-btn-danger">删除</button>' +
                    '</div>' +
                    '</td>' +
                    '</tr>');
                friendLinkManagementTable.append(friendLink);
            }
            table.append(friendLinkManagementTable);
            friendLinkContent.append(table);

            //添加友链
            $('.addFriendLink').click(function () {
                friendLinkId = "";
                $('#addFriendLink').modal('open');
                $('#blogger').val("");
                $('#url').val("");
            });

            updateFriendLinkEditAndDelBtn();
        },
        error:function () {
        }
    });
});
//获得仪表盘信息
getStatisticsInfo();
//获取统计信息
function getStatisticsInfo() {
    $.ajax({
        type:'get',
        url:'/getStatisticsInfo',
        dataType:'json',
        data:{
        },
        success:function (data) {
            $('.allVisitor').html(data['allVisitor']);
            $('.yesterdayVisitor').html(data['yesterdayVisitor']);
            $('.allUser').html(data['allUser']);
            $('.articleNum').html(data['articleNum']);
            // if(data['articleThumbsUpNum'] != 0){
            //     $('.articleThumbsUp').find('a').append($('<span class="am-badge am-badge-warning am-margin-right am-fr articleThumbsUpNum">' + data['data']['articleThumbsUpNum'] + '</span>'));
            // }
        },
        error:function () {
            alert("获取统计信息失败");
        }
    });
}

// 更新友链的编辑和删除按钮
function updateFriendLinkEditAndDelBtn() {
    //编辑友链
    $('.friendLinkManagementBtn').click(function () {
        $('#addFriendLink').modal('open');
        var $this = $(this).parent().parent().parent();
        var blogger = $this.find('.blogger').html();
        var url = $this.find('.url').html();
        friendLinkId = $this.attr('id').substring(1);
        $('#blogger').val(blogger);
        $('#url').val(url);
    });

    //删除友链
    $('.friendLinkDeleteBtn').click(function () {
        friendLinkId = $(this).parent().parent().parent().attr('id').substring(1);
        $('#deleteFriendLink').modal('open');
    });
}

//编辑或增加友链
$('.sureFriendLinkAddBtn').click(function () {
    var blogger = $.trim($('#blogger').val());
    var url = $.trim($('#url').val());
    if(blogger != "" && url != ""){
        $.ajax({
            type:'post',
            url:'/addFriendLink',
            dataType:'json',
            data:{
                id:friendLinkId,
                blogger:blogger,
                url:url
            },
            success:function (data) {
                if(data['status'] == 601){
                    successNotice(data['message']);
                    var tr = $('<tr id="p' + data['data'] + '"><td class="blogger">' + blogger + '</td><td class="url">' + url + '</td>' +
                        '<td>' +
                        '<div class="am-dropdown" data-am-dropdown="">' +
                        '<button class="friendLinkManagementBtn articleEditor am-btn am-btn-secondary">编辑</button>' +
                        '<button class="friendLinkDeleteBtn articleDelete am-btn am-btn-danger">删除</button>' +
                        '</div>' +
                        '</td>' +
                        '</tr>');
                    $('.friendLinkManagementTable').append(tr);
                    var friendLinkNum = $('.friendLinkNum').html();
                    $('.friendLinkNum').html(++friendLinkNum);

                    //刷新刚填充上的友链的两个按钮，使编辑和删除两个按钮的js生效
                    updateFriendLinkEditAndDelBtn();
                } else if (data['status'] == 602){
                    dangerNotice(data['message']);
                } else if(data['status'] == 603){
                    successNotice(data['message']);
                    $('#p' + friendLinkId).find($('.blogger')).html(blogger);
                    $('#p' + friendLinkId).find($('.url')).html(url);
                } else {
                    dangerNotice(data['message']);
                }
            },
            error:function () {
                alert("更新友链失败！")
            }
        });
    } else {
        dangerNotice("博主或博客地址不能为空！");
    }
});
//删除友链
$('.sureFriendLinkDeleteBtn').click(function () {
    $.ajax({
        type:'post',
        url:'/deleteFriendLink',
        dataType:'json',
        data:{
            id:friendLinkId
        },
        success:function (data) {
            if(data['status'] == 604){
                successNotice(data['message']);
                $('#p'+ friendLinkId).remove();
                var friendLinkNum = $('.friendLinkNum').html();
                $('.friendLinkNum').html(--friendLinkNum);
            } else {
                dangerNotice(data['message']);
            }
        },
        error:function () {
            alert("删除友链失败");
        }
    });
});

// 添加或者删除分类
function updateCategory(categoryName, type) {
    $.ajax({
        type:'post',
        url:'/updateCategory',
        dataType:'json',
        data:{
            categoryName:categoryName,
            type:type
        },
        success:function (data) {
            var categoryNum = $('.categoryNum').html();
            if(data['status'] == 401){
                $('.categories').append($('<span id="p' + data['data'] + '" class="category">' + categoryName + '</span>'));
                $('.categoryNum').html(++categoryNum);
                successNotice(data['message']);
            }else if (data['status'] == 402 || data['status'] == 404 || data['status'] == 405){
                dangerNotice(data['message']);
            } else if (data['status'] == 403){
                var allCategories = $('.category');
                $('.categoryNum').html(--categoryNum);
                for(var i=0;i<allCategories.length;i++){
                    if(allCategories[i].innerHTML == categoryName){
                        allCategories[i].remove();
                    }
                }
                successNotice(data['message']);
            }
        },
        error:function () {
            alert("操作失败");
        }
    });
}

// 添加或者删除标签
function updateTag(categoryName, type) {

}

//删除文章
$('.sureArticleDeleteBtn').click(function () {
    $.ajax({
        type:'get',
        url:'/deleteArticle',
        dataType:'json',
        data:{
            id:deleteArticleId
        },
        success:function (data) {
            if(data['result'] == 1){
                dangerNotice("删除文章失败")
            } else {
                successNotice("删除文章成功");
                getArticleManagement(1);
            }
        },
        error:function () {
            alert("删除失败");
        }
    });
});

//时间转换为2019年7月13日
function timestampToYMDTime(timestamp) {
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    Y = date.getFullYear() + '年';
    M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '月';
    D = date.getDate() + '日';
    h = date.getHours() + ':';
    m = date.getMinutes() + ':';
    s = date.getSeconds();
    return Y+M+D;
}