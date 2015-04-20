<%@ page language="java" session="false"  import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@page import="com.frameworkset.platform.sysmgrcore.authenticate.LoginUtil"%>
<%@page import="com.frameworkset.platform.framework.MenuHelper"%>
<%@page import="com.frameworkset.platform.framework.ModuleQueue"%>
<%@page import="com.frameworkset.platform.framework.Module"%>
<%@page import="com.frameworkset.platform.framework.Item"%>
<%@page import="com.frameworkset.platform.framework.ItemQueue"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<%@ include file="/common/jsp/common-css-lhgdialog.jsp"%>
<link href="../html3/hrm/stylesheet/index.css" rel="stylesheet" type="text/css" />

<style type="text/css">
#scrollWrap{height:31px;overflow:hidden;}
#scrollMsg li{height:31px;line-height:31px;overflow:hidden;font-size:12px;} 
</style>

<title>无标题文档</title>


<script type="text/javascript">
$(document).ready(function(){
	var host = '<dict:itemvalue type="newsservice" itemName="sitehost" defaultItemValue="${pageContext.request.contextPath}"/>';
		if(host.charAt(host.length - 1) != '/')
		{
			host = host + "/";
		}
		var site = '<dict:itemvalue type="newsservice" itemName="site" defaultItemValue="cms"/>';
		var noticechannel = '<dict:itemvalue type="newsservice" itemName="noticechannel" defaultItemValue="notice"/>';
		var noticecount = '<dict:itemvalue type="newsservice" itemName="noticecount" defaultItemValue="100"/>';
		var getnewsurl =  host + 'document/getNewsList.freepage?jsonp_callback=?';
	
		$.getJSON(getnewsurl, { site:site,channel:noticechannel,count:noticecount,isArrangeDoc:'true' },
           function(data) {   
			 if(data != null)
			{
				var comm = "";
				var sitedomain = data.sitedomain;
				if(sitedomain.charAt(sitedomain.length - 1) != '/')
				{
					sitedomain = sitedomain + "/";
				}
				for (var i=0; data.news && i<data.news.length; i++) {
					var d = data.news[i];
					comm = comm +"  <li>";
					comm = comm +"<a href=\""+d.docpuburl+"\" target='_blank'>";
					comm = comm +d.title+"("+d.createTime+")";
					comm = comm +"</a>";
					comm = comm +"</li>";
				}
				$("#scrollMsg").empty(); 
            	$("#scrollMsg").append(comm); 
			}
              
           });
		
		var policychannel = '<dict:itemvalue type="newsservice" itemName="policychannel" defaultItemValue="news"/>';
		var policycount = '<dict:itemvalue type="newsservice" itemName="policycount" defaultItemValue="5"/>';
		
		$.getJSON(getnewsurl, { site:site,channel:policychannel,count:policycount},
            function(data) {   
				 if(data != null)
				{
					 $("#more_policy").attr("href",data.channelIndex);
					var comm = "<ul>";
					var sitedomain = data.sitedomain;
					if(sitedomain.charAt(sitedomain.length - 1) != '/')
					{
						sitedomain = sitedomain + "/";
					}
					for (var i=0; data.news && i<data.news.length; i++) {
						var d = data.news[i];
			
							comm = comm +"  <li>";
							comm = comm +"<a href=\""+d.docpuburl+"\" target='_blank'>";
							comm = comm +d.title;
							comm = comm +"</a>";
							comm = comm +"</li>";
				

					}
					comm=comm+"</ul>";
					$("#policy").empty(); 
             		$("#policy").append(comm); 
				}
               
            });
		
		var commondocchannel = '<dict:itemvalue type="newsservice" itemName="commondocchannel" defaultItemValue="news"/>';
		var commondoccount = '<dict:itemvalue type="newsservice" itemName="commondoccount" defaultItemValue="5"/>';
		
		$.getJSON(getnewsurl, { site:site,channel:commondocchannel,count:commondoccount },
            function(data) {   
				 if(data != null)
				{
					 $("#moreCommonDoc").attr("href",data.channelIndex);
					 var comm = "<ul>";
					var sitedomain = data.sitedomain;
					if(sitedomain.charAt(sitedomain.length - 1) != '/')
					{
						sitedomain = sitedomain + "/";
					}
					for (var i=0; data.news && i<data.news.length; i++) {
						var d = data.news[i];
			
							comm = comm +"  <li>";
							comm = comm +"<a href=\"javascript:void(0)\" onclick=\"javascript:window.location.href='"+d.docpuburl+"';return false;\" target='_blank'>";
							comm = comm +d.title;
							comm = comm +"</a>";
							comm = comm +"</li>";
					}
					comm=comm+"</ul>";
					$("#commonDoc").empty(); 
             		$("#commonDoc").append(comm); 
				}
               
            });
		loadCustomMenu();
		
		
});
function opencustom(url,popurl,width,height,title,urltype,options)
{
	 
	 var ppup = options?options.popup:false;
	 var maxstate = options ?(options.maxstate === true || options.maxstate === false?options.maxstate :true):true;
	 var width = options && options.width ?options.width:777;
	 var height = options && options.height ?options.height:500;
	
	 if(ppup)
		$.dialog({ title:title,width:width,height:height, content:'url:'+popurl,lock: true,maxState:maxstate}); 	
	 else
	 {
		 top.location.href = url;
	 }
}

//快捷区域
function loadCustomMenu()
{
	//alert("aaaaa");
	var url =  '${pageContext.request.contextPath}/desktop/getCustomMenus.page';
	var urltype = '2';
	$.getJSON(url, { urltype:urltype },
       function(data) {   
			 if(data != null)
			{
				var custommenuobj={};
				
				for (var i=0; i<data.length; i++) {
					var d = data[i];
					var menuType='';
					if(d.extendAttribute.menuType=="" || d.extendAttribute.menuType== undefined ){
						menuType="default";
					}else{
						menuType=d.extendAttribute.menuType;
					}
					if(custommenuobj[menuType]=="" || custommenuobj[menuType]==undefined){
						custommenuobj[menuType] ="<div class=\"application\" id=\"custommenu"+menuType+"\"> <ul>";
					}

					var pathU=d.pathU;
					//pathU=pathU.replace("sanydesktop/index.page","sanydesktop/indexcommon.page");
					custommenuobj[menuType]=custommenuobj[menuType] +" <li><a href=\"javascript:void(0)\" onclick=\"opencustom('"+pathU+"','"+d.pathPopu+"',"+d.desktop_width+","+d.desktop_height+",'"+d.name+"','"+urltype+"',"+d.option+")\"><img src=\""+d.imageUrl+"\" width=\"56\" height=\"52\" /><br />"+d.name+"</a></li>";
					
				}
				$("#customMenu").empty();
				for(var prop in custommenuobj){
					$("#customMenu").append(custommenuobj[prop]);
				}

			}
          
       });
}
function openCustomMenu(){
	var popurl="../sanydesktop/frame.page?sany_menupath=module::menu://sysmenu$root/desktopmanager$item";

	//$.dialog({title:'aaaa',width:740,height:560, content:'url:'+url,lock: true});
	$.dialog({ close:loadCustomMenu,title:'设置快捷应用', content:'url:'+popurl,lock: true,width:940,height:600});

}

</script>
</head>

<body>
<div class="notice_area">
<div class="news_title">最新新闻公告：</div>
<div id="scrollWrap" >
<ul id="scrollMsg"> 
<!-- 
<li><a href="javascript:void(0)">关于7月25号小候鸟活动的说明（2014-07-24）</a></li>
		<li><a href="javascript:void(0)">关于绩效比例上及中上比例提高的说明（2014-06-28）</a></li>
		<li><a href="javascript:void(0)">关于半年度述职的填写说明（2014-06-01）</a></li>
 -->		
</ul>
</div>
</div>
<div class="index_contain">
  <div class="side_bar">
    <div class="title_right">
      <div class="more_style"><a href="javascript:void(0)">更多></a></div>
      <img src="../html3/hrm/images/right1.png" width="13" height="14"  align="absmiddle"/>待办事宜</div>
    <div class="right_content">审批类：<span class="num"><a href="javascript:void(0)">15</a></span>条<br/>
      通知类：<span class="num"><a href="javascript:void(0)">2</a></span>条</div>
    <div class="title_right">
      <div class="more_style"><a href="javascript:void(0)" id="more_policy" target="_blank">更多></a></div>
      <img src="../html3/hrm/images/right2.png" width="15" height="14" align="absmiddle"/>办事指南</div>
    <div class="right_content" id="policy">
    <!--   <ul>
      
        <li><a href="javascript:void(0)">内部竞聘流程</a></li>
        <li><a href="javascript:void(0)">岗位调动流程</a></li>
        <li><a href="javascript:void(0)">离职流程</a></li>
        <li><a href="javascript:void(0)">大事福利领取流程[三一重工]</a></li>
        <li><a href="javascript:void(0)">大事福利领取流程[昆山重机]</a></li>
        
      </ul> -->
    </div>
    <div class="title_right">
      <div class="more_style"><a href="javascript:void(0)" id="moreCommonDoc" target="_blank">更多></a></div>
      <img src="../html3/hrm/images/right3.png" width="14" height="12" align="absmiddle" />常用表格下载</div>
    <div class="right_content"  id="commonDoc">
    <!-- 
      <ul>
        <li><a href="javascript:void(0)">内部竞聘人员面试录用申请表</a></li>
        <li><a href="javascript:void(0)">解除劳动合同申请书（新修订）...</a></li>
        <li><a href="javascript:void(0)">离职交接清单（职能类）</a></li>
        <li><a href="javascript:void(0)"> 离职交接清单（研发/管理类） </a></li>
        <li><a href="javascript:void(0)">计划生育证明（已婚未育）</a> </li>
      </ul>
       -->
    </div>
  </div>
  <div class="index_left">
    <div class="top_h">
      <div class="left_h">成长值：<span class="num"><a href="javascript:void(0)">289</a></span></div>
      <div class="content_h"> 张雅，早上好，祝您今天工作有个好心情！<br/>
        我是小H，很高兴为您服务，有任何问题请点击我，我不会偷懒哦！<br/>
        您目前有<span class="num"><a href="javascript:void(0)">15</a></span>条待办，<span class="num"><a href="javascript:void(0)">2</a></span>条通知，及时处理会帮助我成长哦！ </div>
    </div>
    <div class="index_title">
      <div class="operate_right"><img src="../html3/hrm/images/quick.png"  width="16" height="16" align="absmiddle" />
      <a href="javascript:void(0)" onclick="openCustomMenu()" >设置快捷应用</a></div>
      快捷应用</div>
      <div id="customMenu"></div>
      <!-- 
    <div class="application">
      <ul>
        <li><a href="javascript:void(0)"><img src="../html3/images/app1.png" width="56" height="52" /><br />
          新建请假单</a></li>
        <li><a href="javascript:void(0)"><img src="../html3/images/app2.png" width="56" height="52" /><br />
          新建加班单</a></li>
        <li><a href="javascript:void(0)"><img src="../html3/images/app3.png" width="56" height="52" /><br />
          新建绩效合约</a></li>
        <li><a href="javascript:void(0)"><img src="../html3/images/app4.png" width="56" height="52" /><br />
          新建述职报告</a></li>
      </ul>
    </div>
    <div class="application">
      <ul>
        <li><a href="javascript:void(0)"><img src="../html3/images/app5.png" width="56" height="52" /><br />
          查询工资单</a></li>
        <li><a href="javascript:void(0)"><img src="../html3/images/app6.png" width="56" height="52" /><br />
          查询个人档案</a></li>
        <li><a href="javascript:void(0)"><img src="../html3/images/app7.png" width="56" height="52" /><br />
          查询调休余额</a></li>
        <li><a href="javascript:void(0)"><img src="../html3/images/app8.png" width="56" height="52" /><br />
          我的申请单</a></li>
        <li><a href="javascript:void(0)"><img src="../html3/images/app9.png" width="56" height="52" /><br />
          查询下属薪酬</a></li>
        <li><a href="javascript:void(0)"><img src="../html3/images/app10.png" width="56" height="52" /><br />
          查询下属绩效</a></li>
        <li><a href="javascript:void(0)"><img src="../html3/images/app11.png" width="56" height="52" /><br />
          下属合同到期日</a></li>
      </ul>
    </div>
    <div class="application">
      <ul>
        <li><a href="javascript:void(0)"><img src="../html3/images/app12.png" width="56" height="52" /><br />
          绩效处理</a></li>
        <li><a href="javascript:void(0)"><img src="../html3/images/app13.png" width="56" height="52" /><br />
          述职处理</a></li>
      </ul>
    </div>
    -->
    <div class="index_title">
      <div class="operate_right"><img src="../html3/hrm/images/quick2.png"  width="16" height="16" align="absmiddle" /><a href="javascript:void(0)">查看所有申请事项</a></div>
      我的待审申请</div>
    <div class="index_list">
      <div class="tableContainer">
        <table  border="0" cellspacing="0" cellpadding="0" class="sany_table">
          <tr>
            <th width="30">序号</th>
            <th width="80">申请类型</th>
            <th width="300">申请内容</th>
            <th width="100">当前节点</th>
            <th width="100">审批人</th>
            <th width="150">操作</th>
          </tr>
        </table>
        <div  class="table_index">
          <table border="0" cellspacing="0" cellpadding="0" class="sany_table">
            <tr>
              <td width="30">1</td>
              <td width="80">请假申请</td>
              <td width="300">5月27日的事假申请</td>
              <td width="100">部门领导审批</td>
              <td width="100">邱华 龙兵</td>
              <td width="150"><a href="javascript:void(0)">催办</a> <a href="javascript:void(0)">撤回</a> <a href="javascript:void(0)">废弃</a></td>
            </tr>
            <tr>
              <td>2</td>
              <td>调休申请</td>
              <td>5月26日的调休申请</td>
              <td>部门领导审批</td>
              <td>邱华 龙兵</td>
              <td><a href="javascript:void(0)">催办</a> <a href="javascript:void(0)">撤回</a> <a href="javascript:void(0)">废弃</a></td>
            </tr>
            <tr>
              <td>3</td>
              <td>调休申请</td>
              <td>5月20日-5月25日的年假申请</td>
              <td>部门领导审批</td>
              <td>邱华 龙兵</td>
              <td><a href="javascript:void(0)">催办</a> <a href="javascript:void(0)">撤回</a> <a href="javascript:void(0)">废弃</a></td>
            </tr>
            <tr>
              <td>4</td>
              <td>调休申请</td>
              <td>5月18日的加班申请</td>
              <td>部门领导审批</td>
              <td>邱华 龙兵</td>
              <td><a href="javascript:void(0)">催办</a> <a href="javascript:void(0)">撤回</a> <a href="javascript:void(0)">废弃</a></td>
            </tr>
            <tr>
              <td>5</td>
              <td>请假申请</td>
              <td>第二季度绩效合约提交申请</td>
              <td>部门领导审批</td>
              <td>邱华 龙兵</td>
              <td><a href="javascript:void(0)">催办</a> <a href="javascript:void(0)">撤回</a></td>
            </tr>
            <tr>
              <td>6</td>
              <td>绩效合约</td>
              <td>第一季度绩效合约总结</td>
              <td>部门领导审批</td>
              <td>邱华 龙兵</td>
              <td><a href="javascript:void(0)">催办</a> <a href="javascript:void(0)">撤回</a> <a href="javascript:void(0)">废弃</a></td>
            </tr>
            <tr>
              <td>7</td>
              <td>加班申请</td>
              <td>5月16日的加班申请</td>
              <td>部门领导审批</td>
              <td>龙兵</td>
              <td><a href="javascript:void(0)">催办</a> <a href="javascript:void(0)">撤回</a> <a href="javascript:void(0)">废弃</a></td>
            </tr>
          </table>
        </div>
      </div>
    </div>
    
    <div class="index_title">
      <div class="operate_right"><img src="../html3/hrm/images/quick3.png"  width="16" height="16" align="absmiddle" /><a href="javascript:void(0)">查看内部竞聘列表</a></div>
      内部应聘</div>
    <div class="index_list">
      <div class="tableContainer">
        <table border="0" cellspacing="0" cellpadding="0" class="sany_table">
          <tr>
            <th scope="col" width="30">序号</th>
            <th scope="col" width="100">职位名称</th>
            <th scope="col" width="55">需求人数</th>
            <th scope="col" width="150">所属部门</th>
            <th scope="col" width="60">最低学历</th>
            <th scope="col" width="60">最低司龄</th>
            <th scope="col" width="70">工作所在地</th>
            <th scope="col" width="70">发布时间</th>
            <th scope="col" width="80">操作</th>
          </tr>
        </table>
        <div  class="table_index">
          <table border="0" cellspacing="0" cellpadding="0" class="sany_table">
            <tr>
              <td width="30">1</td>
              <td width="100">铆工</td>
              <td width="55">30</td>
              <td width="150">港机事业部/珠海 </td>
              <td width="60">高中</td>
              <td width="60">0年</td>
              <td width="70">珠海</td>
              <td width="70">2014-07-27</td>
              <td width="80"><a href="javascript:void(0)">收藏</a> <a href="javascript:void(0)">竞聘</a></td>
            </tr>
            <tr>
              <td>2</td>
              <td>费用兼资金会计</td>
              <td>1 </td>
              <td>经营计划总部</td>
              <td>本科</td>
              <td>2年</td>
              <td>长沙</td>
              <td>2014-07-23</td>
              <td><a href="javascript:void(0)">收藏</a> <a href="javascript:void(0)">竞聘</a></td>
            </tr>
            <tr>
              <td>3</td>
              <td>项目经理 </td>
              <td>2</td>
              <td>经营计划总部 </td>
              <td>本科</td>
              <td>2年</td>
              <td>北非</td>
              <td>2014-07-22</td>
              <td><a href="javascript:void(0)">收藏</a> <a href="javascript:void(0)">竞聘</a></td>
            </tr>
            <tr>
              <td>4</td>
              <td>焊工</td>
              <td>50</td>
              <td>港机事业部/珠海</td>
              <td>高中</td>
              <td>0年</td>
              <td>珠海</td>
              <td>2014-07-22</td>
              <td><a href="javascript:void(0)">收藏</a> <a href="javascript:void(0)">竞聘</a></td>
            </tr>
            <tr>
              <td>5</td>
              <td>国际项目经理</td>
              <td>2</td>
              <td>港机事业部/珠海</td>
              <td>本科</td>
              <td>2年</td>
              <td>珠海</td>
              <td>2014-07-22</td>
              <td><a href="javascript:void(0)">收藏</a> <a href="javascript:void(0)">竞聘</a></td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>

<script type="text/javascript" src="../html3/js/scroll.js"></script>
