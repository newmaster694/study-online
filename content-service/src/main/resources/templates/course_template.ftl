<!DOCTYPE html>
<html lang="zh-CN">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/static/img/asset-favicon.ico">
    <title>学成在线-${model.courseBase.name}</title>

    <link rel="stylesheet" href="/static/plugins/normalize-css/normalize.css" />
    <link rel="stylesheet" href="/static/plugins/bootstrap/dist/css/bootstrap.css" />
    <link rel="stylesheet" href="/static/css/page-learing-article.css" />
</head>

<body data-spy="scroll" data-target="#articleNavbar" data-offset="150">
<!-- 页面头部 -->
<!--#include virtual="/include/header.html"-->
<!--页面头部结束sss-->
<div id="learningArea">
<div class="article-banner">
    <div class="banner-bg"></div>
    <div class="banner-info">
        <div class="banner-left">
            <p>${model.courseBase.mtName!''}<span>\ ${model.courseBase.stName!''}</span></p>
            <p class="tit">${model.courseBase.name!''}</p>
            <p class="pic">
                <#if model.courseBase.charge=='201000'>
                    <span class="new-pic">免费</span>
                <#else>
                    <span class="new-pic">特惠价格￥${model.courseBase.price!''}</span>
                    <span class="old-pic">原价￥${model.courseBase.originalPrice!''}</span>
                </#if>
            </p>
            <p class="info">
                <a href="#" @click.prevent="startLearning()">马上学习</a>
                <span><em>难度等级</em>
                <#if model.courseBase.grade=='204001'>
                    初级
                 <#elseif model.courseBase.grade=='204002'>
                    中级
                <#elseif model.courseBase.grade=='204003'>
                    高级
                </#if>
                </span>
                <span><em>课程时长</em>2小时27分</span>
                <span><em>评分</em>4.7分</span>
                <span><em>授课模式</em>
                 <#if model.courseBase.teachmode=='200002'>
                     录播
                 <#elseif model.courseBase.teachmode=='200003'>
                     直播
                 </#if>
                </span>
            </p>
        </div>
        <div class="banner-rit">
            <p>
                <a href="http://www.51xuecheng.cn/course/preview/learning.html?id=${model.courseBase.id!''}" target="_blank">
                    <#if model.courseBase.pic??>
                        <img src="http://file.51xuecheng.cn${model.courseBase.pic!''}" alt="" width="270" height="156">
                    <#else>
                        <img src="/static/img/widget-video.png" alt="" width="270" height="156">
                    </#if>

                </a>
            </p>
            <p class="vid-act"><span> <i class="i-heart"></i>收藏 23 </span> <span>分享 <i class="i-weixin"></i><i class="i-qq"></i></span></p>
        </div>
    </div>
</div>
<div class="article-cont">
    <div class="tit-list">
        <a href="javascript:;" id="articleClass" class="active">课程介绍</a>
        <a href="javascript:;" id="articleItem">目录</a>
        <a href="javascript:;" id="artcleAsk">问答</a>
        <a href="javascript:;" id="artcleNot">笔记</a>
        <a href="javascript:;" id="artcleCod">评价</a>
        <!--<div class="down-fill">
            <span>资料下载</span>
            <ul>
                <li>java视频资料</li>
                <li>java视频资料</li>
                <li>java视频资料</li>
            </ul>
        </div>-->
    </div>
    <div class="article-box">
        <div class="articleClass" style="display: block">
            <!--<div class="rit-title">评价</div>-->
            <div class="article-cont">
                <div class="article-left-box">
                    <div class="content">

                        <div class="content-com suit">
                            <div class="title"><span>适用人群</span></div>
                            <div class="cont cktop">
                                <div >
                                    <p>${model.courseBase.users!''}</p>
                                </div>
                                <!--<span class="on-off">更多 <i class="i-chevron-bot"></i></span>-->
                            </div>
                        </div>
                        <div class="content-com course">
                            <div class="title"><span>课程制作</span></div>
                            <div class="cont">
                                <div class="img-box"><img src="/static/img/widget-myImg.jpg" alt=""></div>
                                <div class="info-box">
                                    <p class="name">教学方：<em>XX老师</em></p>
                                    <!-- <p class="lab">高级前端开发工程师 10年开发经验</p>-->
                                    <p class="info">JavaEE开发与教学多年，精通JavaEE技术体系，对流行框架JQuery、DWR、Struts1/2，Hibernate，Spring，MyBatis、JBPM、Lucene等有深入研究。授课逻辑严谨，条理清晰，注重学生独立解决问题的能力。</p>
                                    <!-- <p><span>难度等级</span>中级</p>
                                     <p><span>课程时长</span>8-16小时/周，共4周</p>
                                     <p><span>如何通过</span>通过所有的作业及考核，作业共4份，考核为一次终极考核</p>
                                     <p><span>用户评分</span>平均用户评分 <em>4.9</em> <a href="#">查看全部评价</a></p>
                                     <p><span>课程价格</span>特惠价格<em>￥999</em> <i> 原价1999 </i></p>-->
                                </div>
                            </div>

                        </div>
                        <div class="content-com about">
                            <div class="title"><span>课程介绍</span></div>
                            <div class="cont cktop">
                                <div >
                                    <p>${model.courseBase.description!""}</p>
                                </div>
                                <!--<span class="on-off">更多 <i class="i-chevron-bot"></i></span>-->
                            </div>
                        </div>
                        <div class="content-com prob">
                            <div class="title"><span>常见问题</span></div>
                            <div class="cont">
                                <ul>
                                    <li class="item"><span class="on-off"><i class="i-chevron-bot"></i> 我什么时候能够访问课程视频与作业？</span>
                                        <div class="drop-down">
                                            <p>课程安排灵活，课程费用支付提供180天全程准入和资格证书。自定进度课程建议的最后期限，但你不会受到惩罚错过期限，只要你赚你的证书在180天内。以会话为基础的课程可能要求你在截止日期前保持正轨，但如果你落后了，你可以切换到以后的会议，你完成的任何工作将与你转移。</p>
                                        </div>
                                    </li>
                                    <li class="item"><span class="on-off"><i class="i-chevron-bot"></i> 如何需要额外的时间来完成课程会怎么样？</span>
                                        <div class="drop-down">
                                            <p>课程安排灵活，课程费用支付提供180天全程准入和资格证书。自定进度课程建议的最后期限，但你不会受到惩罚错过期限，只要你赚你的证书在180天内。以会话为基础的课程可能要求你在截止日期前保持正轨，但如果你落后了，你可以切换到以后的会议，你完成的任何工作将与你转移。</p>
                                        </div>
                                    </li>
                                    <li class="item"><span class="on-off"><i class="i-chevron-bot"></i> 我支付次课程之后会得到什么？</span>
                                        <div class="drop-down">
                                            <p>课程安排灵活，课程费用支付提供180天全程准入和资格证书。自定进度课程建议的最后期限，但你不会受到惩罚错过期限，只要你赚你的证书在180天内。以会话为基础的课程可能要求你在截止日期前保持正轨，但如果你落后了，你可以切换到以后的会议，你完成的任何工作将与你转移。</p>
                                        </div>
                                    </li>
                                    <li class="item"><span class="on-off"><i class="i-chevron-bot"></i> 退款条例是如何规定的？</span>
                                        <div class="drop-down">
                                            <p>课程安排灵活，课程费用支付提供180天全程准入和资格证书。自定进度课程建议的最后期限，但你不会受到惩罚错过期限，只要你赚你的证书在180天内。以会话为基础的课程可能要求你在截止日期前保持正轨，但如果你落后了，你可以切换到以后的会议，你完成的任何工作将与你转移。</p>
                                        </div>
                                    </li>
                                    <li class="item"><span class="on-off"><i class="i-chevron-bot"></i> 有助学金？</span>
                                        <div class="drop-down">
                                            <p>课程安排灵活，课程费用支付提供180天全程准入和资格证书。自定进度课程建议的最后期限，但你不会受到惩罚错过期限，只要你赚你的证书在180天内。以会话为基础的课程可能要求你在截止日期前保持正轨，但如果你落后了，你可以切换到以后的会议，你完成的任何工作将与你转移。</p>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                <!--侧边栏-->
                <!--#include virtual="/include/course_detail_side.html"-->
                <!--侧边栏-->

            </div>
        </div>
        <div class="articleItem" style="display: none">
            <div class="article-cont-catalog">
                <div class="article-left-box">
                    <div class="content">
                        <#list model.teachplans as firstNode>
                            <div class="item">
                                <div class="title act"><i class="i-chevron-top"></i>${firstNode.pname}<span class="time">x小时</span></div>
                                <div class="drop-down" style="height: 260px;">
                                    <ul class="list-box">
                                        <#list firstNode.teachPlanTreeNodes as secondNode>
                                            <li><a href="http://www.51xuecheng.cn/course/preview/learning.html?id=${model.courseBase.id}&chapter=${secondNode.teachplanMedia.teachplanId!''}" target="_blank">${secondNode.pname}</a></li>
                                        </#list>
                                    </ul>
                                </div>
                            </div>
                        </#list>
                       <#-- <div class="item">
                            <div class="title act"><i class="i-chevron-top"></i>第一阶段 HTTP协议基础详解<span class="time">8小时</span></div>
                            <div class="about">使用Java消息中间件处理异步消息成为了分布式系统中的必修课，通过本门课程可以深入浅出的学习如何在Java中使用消息中间件并且一步一步打造更优雅的最佳实践方案。</div>
                            <div class="drop-down" style="height: 260px;">
                                <ul class="list-box">
                                    <li class="active">1.1 阅读：分级政策细节 <span>97’33”</span></li>
                                    <li>1.2 视频：为什么分为 A 部分、B 部分、C 部分 <span>66’15”</span></li>
                                    <li>1.3 视频：软件安装介绍 <span>86’42”</span></li>
                                    <li>1.4 阅读：Emacs安装 <span>59’00”</span></li>
                                    <li>1.5 作业1：Emacs安装 <span>93’29”</span></li>
                                    <li>阶段测试</li>
                                </ul>
                            </div>
                        </div>-->


                    </div>
                </div>
                <!--侧边栏-->
                <!--#include virtual="/include/course_detail_side.html"-->
                <!--侧边栏-->
            </div>
        </div>
        <#--<div class="articleItem" style="display: none">
            <div class="article-cont-catalog">
                <div class="article-left-box">
                    <div class="content">
                        <div class="item">
                            <div class="title act"><i class="i-chevron-top"></i>第一阶段 HTTP协议基础详解<span class="time">8小时</span></div>
                            <div class="about">使用Java消息中间件处理异步消息成为了分布式系统中的必修课，通过本门课程可以深入浅出的学习如何在Java中使用消息中间件并且一步一步打造更优雅的最佳实践方案。</div>
                            <div class="drop-down" style="height: 260px;">
                                <ul class="list-box">
                                    <li class="active">1.1 阅读：分级政策细节 <span>97’33”</span></li>
                                    <li>1.2 视频：为什么分为 A 部分、B 部分、C 部分 <span>66’15”</span></li>
                                    <li>1.3 视频：软件安装介绍 <span>86’42”</span></li>
                                    <li>1.4 阅读：Emacs安装 <span>59’00”</span></li>
                                    <li>1.5 作业1：Emacs安装 <span>93’29”</span></li>
                                    <li>阶段测试</li>
                                </ul>
                            </div>
                        </div>
                        <div class="item">
                            <div class="title"><i class="i-chevron-bot"></i>第二阶段 HTTP协议基础详解<span class="time">8小时</span></div>
                            <div class="about">使用Java消息中间件处理异步消息成为了分布式系统中的必修课，通过本门课程可以深入浅出的学习如何在Java中使用消息中间件并且一步一步打造更优雅的最佳实践方案。</div>
                            <div class="drop-down">
                                <ul class="list-box">
                                    <li class="active">1.1 阅读：分级政策细节 <span>97’33”</span></li>
                                    <li>1.2 视频：为什么分为 A 部分、B 部分、C 部分 <span>66’15”</span></li>
                                    <li>1.3 视频：软件安装介绍 <span>86’42”</span></li>
                                    <li>1.4 阅读：Emacs安装 <span>59’00”</span></li>
                                    <li>1.5 作业1：Emacs安装 <span>93’29”</span></li>
                                    <li>阶段测试</li>
                                </ul>
                            </div>
                        </div>
                        <div class="item">
                            <div class="title"><i class="i-chevron-bot"></i>第三阶段 HTTP协议基础详解<span class="time">3小时</span></div>
                            <div class="about">使用Java消息中间件处理异步消息成为了分布式系统中的必修课，通过本门课程可以深入浅出的学习如何在Java中使用消息中间件并且一步一步打造更优雅的最佳实践方案。</div>
                            <div class="drop-down">
                                <ul class="list-box">
                                    <li class="active">1.1 阅读：分级政策细节 <span>97’33”</span></li>
                                    <li>1.2 视频：为什么分为 A 部分、B 部分、C 部分 <span>66’15”</span></li>
                                    <li>1.3 视频：软件安装介绍 <span>86’42”</span></li>
                                    <li>1.4 阅读：Emacs安装 <span>59’00”</span></li>
                                    <li>1.5 作业1：Emacs安装 <span>93’29”</span></li>
                                    <li>阶段测试</li>
                                </ul>
                            </div>
                        </div>
                        <div class="item">
                            <div class="title"><i class="i-chevron-bot"></i>第四阶段 HTTP协议基础详解<span class="time">3小时</span></div>
                            <div class="about">使用Java消息中间件处理异步消息成为了分布式系统中的必修课，通过本门课程可以深入浅出的学习如何在Java中使用消息中间件并且一步一步打造更优雅的最佳实践方案。</div>
                            <div class="drop-down">
                                <ul class="list-box">
                                    <li class="active">1.1 阅读：分级政策细节 <span>97’33”</span></li>
                                    <li>1.2 视频：为什么分为 A 部分、B 部分、C 部分 <span>66’15”</span></li>
                                    <li>1.3 视频：软件安装介绍 <span>86’42”</span></li>
                                    <li>1.4 阅读：Emacs安装 <span>59’00”</span></li>
                                    <li>1.5 作业1：Emacs安装 <span>93’29”</span></li>
                                    <li>阶段测试</li>
                                </ul>
                            </div>
                        </div>
                        <div class="item">
                            <div class="title"><i class="i-chevron-bot"></i>第五阶段 HTTP协议基础详解<span class="time">3小时</span></div>
                            <div class="about">使用Java消息中间件处理异步消息成为了分布式系统中的必修课，通过本门课程可以深入浅出的学习如何在Java中使用消息中间件并且一步一步打造更优雅的最佳实践方案。</div>
                            <div class="drop-down">
                                <ul class="list-box">
                                    <li class="active">1.1 阅读：分级政策细节 <span>97’33”</span></li>
                                    <li>1.2 视频：为什么分为 A 部分、B 部分、C 部分 <span>66’15”</span></li>
                                    <li>1.3 视频：软件安装介绍 <span>86’42”</span></li>
                                    <li>1.4 阅读：Emacs安装 <span>59’00”</span></li>
                                    <li>1.5 作业1：Emacs安装 <span>93’29”</span></li>
                                    <li>阶段测试</li>
                                </ul>
                            </div>

                        </div>
                        <div class="item">
                            <a href="#" class="overwrite">毕业考核</a>
                        </div>
                    </div>
                </div>
                <!--侧边栏&ndash;&gt;
                <!--#include virtual="/include/course_detail_side.html"&ndash;&gt;
                <!--侧边栏&ndash;&gt;
            </div>
        </div>-->
        <div class="artcleAsk" style="display: none">
            <div class="article-cont-ask">
                <div class="article-left-box">
                    <div class="content">
                        <div class="content-title">
                            <p><a class="all">全部</a><a>精选</a><a>我的</a></p>
                            <p><a class="all">全部</a><span><a>1.1</a><a>1.2</a><a>1.3</a><a>1.4</a><a>1.5</a></span><a href="$" class="more">更多 <i class="i-chevron-bot"></i></a></p>
                        </div>
                        <div class="item">
                            <div class="item-left">
                                <p><img src="/static/img/widget-myImg.jpg" width="60px" alt=""></p>
                                <p>毛老师</p>
                            </div>
                            <div class="item-right">
                                <p class="title">如何用微服务重构应用程序?</p>
                                <p><span>我来回答</span></p>
                                <p>2017-3-20 <span><i></i>回答2</span><span><i></i>浏览2</span></p>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-left">
                                <p><img src="/static/img/widget-myImg.jpg" width="60px" alt=""></p>
                                <p>毛老师</p>
                            </div>
                            <div class="item-right">
                                <p class="title">如何用微服务重构应用程序?</p>
                                <p>在讨论如何将重构转化为微服务之前，退后一步，仔细观察微服务的内容和时间是很重要的。以下两个要点将会对任何微服务重构策略产生重大影响。 【最新 <i class="new">心跳347890</i> 的回答】</p>
                                <p>2017-3-20 <span class="action-box"><span><i class="i-answer"></i>回答 2</span><span><i class="i-browse"></i>浏览 12</span></span>
                                </p>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-left">
                                <p><img src="/static/img/widget-myImg.jpg" width="60px" alt=""></p>
                                <p>毛老师</p>
                            </div>
                            <div class="item-right">
                                <p class="title">如何用微服务重构应用程序?</p>
                                <p>在讨论如何将重构转化为微服务之前，退后一步，仔细观察微服务的内容和时间是很重要的。以下两个要点将会对任何微服务重构策略产生重大影响。 【最新 <i class="new">心跳347890</i> 的回答】</p>
                                <p>2017-3-20 <span class="action-box"><span><i class="i-answer"></i>回答 2</span><span><i class="i-browse"></i>浏览 12</span></span>
                                </p>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-left">
                                <p><img src="/static/img/widget-myImg.jpg" width="60px" alt=""></p>
                                <p>毛老师</p>
                            </div>
                            <div class="item-right">
                                <p class="title">如何用微服务重构应用程序?</p>
                                <p>在讨论如何将重构转化为微服务之前，退后一步，仔细观察微服务的内容和时间是很重要的。以下两个要点将会对任何微服务重构策略产生重大影响。 【最新 <i class="new">心跳347890</i> 的回答】</p>
                                <p>2017-3-20 <span class="action-box"><span><i class="i-answer"></i>回答 2</span><span><i class="i-browse"></i>浏览 12</span></span>
                                </p>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-left">
                                <p><img src="/static/img/widget-myImg.jpg" width="60px" alt=""></p>
                                <p>毛老师</p>
                            </div>
                            <div class="item-right">
                                <p class="title">如何用微服务重构应用程序?</p>
                                <p>在讨论如何将重构转化为微服务之前，退后一步，仔细观察微服务的内容和时间是很重要的。以下两个要点将会对任何微服务重构策略产生重大影响。 【最新 <i class="new">心跳347890</i> 的回答】</p>
                                <p>2017-3-20 <span class="action-box"><span><i class="i-answer"></i>回答 2</span><span><i class="i-browse"></i>浏览 12</span></span>
                                </p>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-left">
                                <p><img src="/static/img/widget-myImg.jpg" width="60px" alt=""></p>
                                <p>毛老师</p>
                            </div>
                            <div class="item-right">
                                <p class="title">如何用微服务重构应用程序?</p>
                                <p>在讨论如何将重构转化为微服务之前，退后一步，仔细观察微服务的内容和时间是很重要的。以下两个要点将会对任何微服务重构策略产生重大影响。 【最新 <i class="new">心跳347890</i> 的回答】</p>
                                <p>2017-3-20 <span class="action-box"><span><i class="i-answer"></i>回答 2</span><span><i class="i-browse"></i>浏览 12</span></span>
                                </p>
                            </div>
                        </div>

                        <div class="itemlast">
                            <a href="#" class="overwrite">显示更多问题</a>
                        </div>
                    </div>
                </div>
                <!--侧边栏-->
                <!--#include virtual="/include/course_detail_side.html"-->
                <!--侧边栏-->
            </div>
        </div>
        <div class="artcleNot" style="display: none;">
            <div class="article-cont-note">
                <div class="article-left-box">
                    <div class="content">
                        <div class="content-title">
                            <p><a class="all">全部</a><a>精选</a><a>我的</a></p>
                            <p><a class="all">全部</a><span><a>1.1</a><a>1.2</a><a>1.3</a><a>1.4</a><a>1.5</a></span><a href="$" class="more">更多 <i class="i-chevron-bot"></i></a></p>
                        </div>
                        <div class="item">
                            <div class="item-left">
                                <p><img src="/static/img/widget-myImg.jpg" width="60px" alt=""></p>
                                <p>毛老师</p>
                            </div>
                            <div class="item-right">
                                <span class="video-time"><i class="i-play"></i>2`10`</span>
                                <p><img src="/static/img/widget-demo.png" width="221" alt=""></p>
                                <p class="action-box">4小时前 <span class="active-box"><span><i class="i-coll"></i>采集</span><span><i class="i-laud"></i>赞</span></span>
                                </p>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-left">
                                <p><img src="/static/img/widget-myImg.jpg" width="60px" alt=""></p>
                                <p>毛老师</p>
                            </div>
                            <div class="item-right">
                                <p>在讨论如何将重构转化为微服务之前，退后一步，<br>仔细观察微服务的内容和时间是很重要的。<br>以下两个要点将会对任何微服务重构策略产生重大影响。 </p>
                                <p class="action-box">4小时前 <span class="active-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></span>
                                </p>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-left">
                                <p><img src="/static/img/widget-myImg.jpg" width="60px" alt=""></p>
                                <p>毛老师</p>
                            </div>
                            <div class="item-right">
                                <p>在讨论如何将重构转化为微服务之前，退后一步，<br>仔细观察微服务的内容和时间是很重要的。<br>以下两个要点将会对任何微服务重构策略产生重大影响。 </p>
                                <p class="action-box">4小时前 <span class="active-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></span>
                                </p>
                            </div>
                        </div>
                        <div class="item">
                            <div class="item-left">
                                <p><img src="/static/img/widget-myImg.jpg" width="60px" alt=""></p>
                                <p>毛老师</p>
                            </div>
                            <div class="item-right">
                                <p>在讨论如何将重构转化为微服务之前，退后一步，<br>仔细观察微服务的内容和时间是很重要的。<br>以下两个要点将会对任何微服务重构策略产生重大影响。 </p>
                                <p class="action-box">4小时前 <span class="active-box"><span><i class="i-edt"></i>编辑</span><span><i class="i-del"></i>删除</span><span><i class="i-laud"></i>赞</span></span>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
                <!--侧边栏-->
                <!--#include virtual="/include/course_detail_side.html"-->
                <!--侧边栏-->
            </div>
        </div>
        <div class="artcleCod" style="display: none;">
            <div class="article-cont">
                <div class="article-left-box">
                    <div class="comment-box">
                        <div class="evaluate">
                            <div class="eva-top">
                                <div class="tit">课程评分 </div>
                                <div class="star">
                                    <div class="score"><i>5</i></div>
                                </div><span class="star-score"> <i>5</i> 分</span></div>
                            <div class="eva-cont">
                                <div class="tit">学员评语 </div>
                                <div class="text-box">
                                    <textarea class="form-control" rows="5" placeholder="扯淡、吐槽、表扬、鼓励......想说啥说啥！"></textarea>
                                    <div class="text-right"><span>发表评论</span></div>
                                </div>
                            </div>
                        </div>
                        <div class="course-evaluate">
                            <div class="top-tit">评论
                                <span>
                        <label><input name="eval" type="radio" value="" checked /> 所有学生 </label>
                        <label><input name="eval" type="radio" value="" /> 完成者 </label>
                    </span>
                            </div>
                            <div class="top-cont">
                                <div class="cont-top-left">
                                    <div class="star-scor">
                                        <div class="star-show">
                                            <div class="score"><i>5</i></div>
                                        </div>
                                        <div class="scor">4.9分</div>
                                    </div>
                                    <div class="all-scor">总评分：12343</div>
                                </div>
                                <div class="cont-top-right">
                                    <div class="star-grade">五星
                                        <div class="grade">
                                            <div class="grade-percent"><span></span></div>
                                            <div class="percent-num"><i>95</i>%</div>
                                        </div>
                                    </div>
                                    <div class="star-grade">四星
                                        <div class="grade">
                                            <div class="grade-percent"><span></span></div>
                                            <div class="percent-num"><i>5</i>%</div>
                                        </div>
                                    </div>
                                    <div class="star-grade">三星
                                        <div class="grade">
                                            <div class="grade-percent"><span></span></div>
                                            <div class="percent-num"><i>0</i>%</div>
                                        </div>
                                    </div>
                                    <div class="star-grade">二星
                                        <div class="grade">
                                            <div class="grade-percent"><span></span></div>
                                            <div class="percent-num"><i>2</i>%</div>
                                        </div>
                                    </div>
                                    <div class="star-grade">一星
                                        <div class="grade">
                                            <div class="grade-percent"><span></span></div>
                                            <div class="percent-num"><i>1</i>%</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="comment-item-box">
                                <div class="title">评论 <span>12453条评论</span></div>
                                <div class="item">
                                    <div class="item-left">
                                        <p><img src="/static/img/widget-pic.png" width="60px" alt=""></p>
                                        <p>毛老师</p>
                                    </div>
                                    <div class="item-cent">
                                        <p>很受用，如果再深入下就更好了。虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！</p>
                                        <p class="time">2017-2-43</p>
                                    </div>
                                    <div class="item-rit">
                                        <p>
                                        <div class="star-show">
                                            <div class="score"><i>4</i></div>
                                        </div>
                                        </p>
                                        <p>评分 <span>5星</span></p>
                                    </div>
                                </div>
                                <div class="item">
                                    <div class="item-left">
                                        <p><img src="/static/img/widget-pic.png" width="60px" alt=""></p>
                                        <p>毛老师</p>
                                    </div>
                                    <div class="item-cent">
                                        <p>很受用，如果再深入下就更好了。虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！</p>
                                        <p class="time">2017-2-43</p>
                                    </div>
                                    <div class="item-rit">
                                        <p>
                                        <div class="star-show">
                                            <div class="score"><i>5</i></div>
                                        </div>
                                        </p>
                                        <p>评分 <span>5星</span></p>
                                    </div>
                                </div>
                                <div class="item">
                                    <div class="item-left">
                                        <p><img src="/static/img/widget-pic.png" width="60px" alt=""></p>
                                        <p>毛老师</p>
                                    </div>
                                    <div class="item-cent">
                                        <p>很受用，如果再深入下就更好了。虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！</p>
                                        <p class="time">2017-2-43</p>
                                    </div>
                                    <div class="item-rit">
                                        <p>
                                        <div class="star-show">
                                            <div class="score"><i>5</i></div>
                                        </div>
                                        </p>
                                        <p>评分 <span>5星</span></p>
                                    </div>
                                </div>
                                <div class="item">
                                    <div class="item-left">
                                        <p><img src="/static/img/widget-pic.png" width="60px" alt=""></p>
                                        <p>毛老师</p>
                                    </div>
                                    <div class="item-cent">
                                        <p>很受用，如果再深入下就更好了。虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！虽然都是入门级别的，但是也很使用，后续就需要自己发挥了！</p>
                                        <p class="time">2017-2-43</p>
                                    </div>
                                    <div class="item-rit">
                                        <p>
                                        <div class="star-show">
                                            <div class="score"><i>5</i></div>
                                        </div>
                                        </p>
                                        <p>评分 <span>5星</span></p>
                                    </div>
                                </div>
                                <div class="get-more">页面加载中...</div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--侧边栏-->
                <!--#include virtual="/include/course_detail_side.html"-->
                <!--侧边栏-->
            </div>
        </div>
    </div>
</div>
    <div class="popup-course">
        <div class="mask"></div>
        <!--欢迎访问课程弹窗- start -->
        <div class="popup-course-box">
            <div class="title">${model.courseBase.name} <span class="close-popup-course-box">×</span></div>
            <div class="content">
                <p>欢迎学习本课程，本课程免费您可以立即学习，也可加入我的课程表享受更优质的服务。</p>
                <p><a href="#" @click.prevent="addCourseTable()">加入我的课程表</a>  <a href="#" @click.prevent="startLearngin()">立即学习</a></p>
            </div>
        </div>
    </div>
    <div class="popup-box">
        <div class="mask"></div>
        <!--支付弹窗- start -->
        <div class="popup-pay-box">
            <div class="title">${model.courseBase.name} <span class="close-popup-pay-box">×</span></div>
            <div class="content">
                <img :src="qrcode" width="200" height="200" alt="请点击支付宝支付按钮，并完成扫码支付。"/>

                <div class="info">
                    <p class="info-tit">${model.courseBase.name}<span>课程有效期:${model.courseBase.validDays}天</span></p>
                    <p class="info-pic">课程价格 : <span>￥${model.courseBase.originalPrice!''}元</span></p>
                    <p class="info-new-pic">优惠价格 : <span>￥${model.courseBase.price!''}元</span></p>
                </div>
            </div>
            <div class="fact-pic">实际支付: <span>￥${model.courseBase.price!''}元</span></div>
            <div class="go-pay"><a href="#" @click.prevent="wxPay()">微信支付</a><a href="#" @click.prevent="aliPay()">支付宝支付</a><a href="#" @click.prevent="querypayresult()">支付完成</a><a href="#" @click.prevent="startLearngin()">试学</a></div>
        </div>
        <!--支付弹窗- end -->
        <div class="popup-comment-box">

        </div>
    </div>
    <!-- 页面底部 -->
    <!--底部版权-->
    <!--#include virtual="/include/footer.html"-->
    <!--底部版权-->
</div>
<script>var courseId = "${model.courseBase.id}";var courseCharge = "${model.courseBase.charge}"</script>
<!--#include virtual="/include/course_detail_dynamic.html"-->
</body>
