
// 失败消息盒
function dangerNotice(notice) {
    $('.dangerNotice').html(notice);
    $('.dangerNoticeAlert').css("display","block");
    var closeNoticeBox = setTimeout(function () {
        $('.dangerNoticeAlert').css("display","none");
    },3000);
}
// 成功消息盒
function successNotice(notice) {
    $('.successNotice').html(notice);
    $('.successNoticeAlert').css("display","block");
    var closeNoticeBox = setTimeout(function () {
        $('.successNoticeAlert').css("display","none");
    },3000);
}
//设置右侧栏箭头动画显示
var sidebar_toggle = $("#sidebar_toggle");
var sidebar_toggle_line_first = $(".sidebar-toggle-line-first");
var sidebar_toggle_line_middle = $(".sidebar-toggle-line-middle");
var sidebar_toggle_line_last = $(".sidebar-toggle-line-last");
sidebar_toggle.mouseenter(function () {
    sidebar_toggle_line_first.animate({
        width:'50%',
        top:'2px',
        left:'0px'
    },function () {
        sidebar_toggle_line_first.css({
            'transform':'rotate(-45deg)',
            'transition':'transform 0.5',
            'width':'50%',
            'top':'2px',
            'left':'0px'
        })
    });
    sidebar_toggle_line_middle.animate({
        width:'90%',
        top:'0px',
        left:'0px'
    },function () {
        sidebar_toggle_line_middle.css({
            'transform':'rotate(0deg)',
            'transition':'transform 0.5',
            'width':'90%',
            'top':'0px',
            'left':'0px'
        })
    });
    sidebar_toggle_line_last.animate({
        width:'50%',
        top:'-2px',
        left:'0px'
    },function () {
        sidebar_toggle_line_last.css({
            'transform':'rotate(45deg)',
            'transition':'transform 0.5, width 0.5',
            'width':'50%',
            'top':'-2px',
            'left':'0px'
        })
    })
});
sidebar_toggle.mouseleave(function () {
    sidebar_toggle_line_first.animate({
        top:'0px',
        left:'0px'
    },function () {
        sidebar_toggle_line_first.css({
            'transform':'rotateZ(0deg)',
            'transition':'transform 0.5, width 0.5',
            'width':'100%',
            'top':'0px',
            'left':'0px'
        })
    });
    sidebar_toggle_line_middle.animate({
        top:'0px',
        left:'0px'
    },function () {
        sidebar_toggle_line_middle.css({
            'transform':'rotateZ(0deg)',
            'transition':'transform 0.5, width 0.5',
            'width':'100%',
            'top':'0px',
            'left':'0px'
        })
    });
    sidebar_toggle_line_last.animate({
        top:'-0px',
        left:'0px'
    },function () {
        sidebar_toggle_line_last.css({
            'transform':'rotateZ(0deg)',
            'transition':'transform 0.5',
            'width':'100%',
            'top':'-0px',
            'left':'0px'
        })
    })
});

//获得访客量，除文章显示界面外其他界面访客量通用
var pageName = window.location.pathname + window.location.search;
$.ajax({
    type:'get',
    url:'/getVisitorNumByPageName',
    dataType:'json',
    data:{
        pageName:pageName.substring(1)
    },
    success:function (data) {
        if(data['status'] == 103){
            $("#totalVisitors").html(0);
            $("#visitorVolume").html(0);
        } else {
            $("#totalVisitors").html(data['data']['totalVisitor']);
            $("#visitorVolume").html(data['data']['pageVisitor']);
        }
    },
    error:function () {
    }
});

//点击右侧栏获得日志、分类、标签数目以及微信公众号图片
$('#sidebar_toggle').click(function () {
    $.ajax({
        type:'get',
        url:'/findArchivesCategoriesTagsNum',
        dataType:'json',
        data:{
        },
        success:function (data) {
            $('.archivesNum').html(data['data']['archivesNum']);
            $('.categoriesNum').html(data['data']['categoriesNum']);
            $('.tagsNum').html(data['data']['tagsNum']);
        },
        error:function () {
        }
    });
    $('.weixinPublic').attr("src","https://zhy-myblog.oss-cn-shenzhen.aliyuncs.com/static/img/weixin.jpg");
});

//获得登录用户未读消息
$.ajax({
    type:'post',
    url:'/getUserNews',
    dataType:'json',
    data:{
    },
    success:function (data) {
        var thisPageName = window.location.pathname + window.location.search;
        var news = $('.news');
        if(data['status'] != 101 && data['data']['result'] != 0){
            news.append($('<span class="newsNum am-badge am-badge-danger am-round">' + data['data']['result']['allNewsNum'] + '</span>'));
            if(thisPageName === "/user"){
                if(data['data']['result']['commentNum'] !== 0){
                    $('.commentMessage').find('a').append($('<span class="commentNotReadNum am-margin-right am-fr am-badge am-badge-danger am-round">' + data['data']['result']['commentNum'] + '</span>'));
                }
                if(data['data']['result']['leaveMessageNum'] !== 0){
                    $('.leaveWord').find('a').append($('<span class="leaveMessageNotReadNum am-margin-right am-fr am-badge am-badge-danger am-round">' + data['data']['result']['leaveMessageNum'] + '</span>'));
                }
            }
        }
    },
    error:function () {
    }
});

//反馈
$('.feedbackClick').click(function () {
    $('.feedback').css("display","block")
});
$('.feedbackClose').click(function () {
    $('.feedback').css("display","none")
});
$('.feedbackFormBtn').click(function () {
    var feedbackFormContent = $('#feedbackFormContent');
    var feedbackFormQQ = $('.feedbackFormQQ');
    if(feedbackFormContent.val().length == 0){
        dangerNotice("反馈内容不能为空哦！")
    } else {
        $.ajax({
            type:'POST',
            url:'/submitFeedback',
            dataType:'json',
            data:{
                feedbackContent:feedbackFormContent.val(),
                contactInfo:feedbackFormQQ.val()
            },
            success:function (data) {
                if(data['status'] == 101){
                    $.get("/toLogin",function(data,status,xhr){
                        window.location.replace("/login");
                    });
                } else {
                    successNotice("反馈成功，我会尽快解决的！");
                    $('.feedback').css("display","none");
                }
            },
            error:function () {
            }
        });
    }
});


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