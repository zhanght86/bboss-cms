<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>


<%--
	描述：浏览统计计数数据查询页面
	作者：gw_hel
	版本：1.0
	日期：2012-08-27
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>操作统计数据</title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<script type="text/javascript">
	
	//页面加载时查询列表数据
	$(document).ready(function() {
		queryAppInfo();
	  });
	//获得站点的浏览的总访问量
	function getSiteSumCount(siteId) {
		$.ajax({
	 	 	type: "POST",
			url : "getBrowserCount.freepage",
			data :{siteId:siteId},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				document.getElementById("sumCount").innerText = data;
			}	
		 });
	}
	//查询应用
	function queryAppInfo() {
		$.ajax({
	 	 	type: "POST",
			url : "../sanylog/getAllApp.freepage",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				$.each(data,function(i,o){//为下拉添加选项
					document.getElementById("siteId").options.add(new Option(data[i].appName, data[i].appId));
				});	
				//获得站点的浏览的总访问量,默认为返回数据的第一个节点
				//getSiteSumCount(data[0].appId);
				//加载页面mainframe 并传入站点的SiteID
				document.getElementById("operateCounterMain").src="operateCounterMain.jsp?siteId="+data[0].appId;
			}	
		 });
	}
	//页面加载时查询列表数据
	function querySiteInfo() {
		$.ajax({
	 	 	type: "POST",
			url : "../site/getAllSite.freepage",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				$.each(data,function(i,o){//为下拉添加选项
					document.getElementById("siteId").options.add(new Option(data[i].name, data[i].siteId));
				});	
				//获得站点的浏览的总访问量,默认为返回数据的第一个节点
				//getSiteSumCount(data[0].siteId);
				//加载页面mainframe 并传入站点的SiteID
				document.getElementById("operateCounterMain").src="operateCounterMain.jsp?siteId="+data[0].siteId;
			}	
		 });
	}
	//根据siteId来选择数据
	function doSiteChange(siteId) {
		//getSiteSumCount(siteId);
		document.getElementById("operateCounterMain").src="operateCounterMain.jsp?siteId="+siteId;
	}
	
	//立即统计
	function statistic() {
		$.ajax({
	 	 	type: "POST",
			url : "statisticOperCounterImmediately.freepage",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				
				if (data == "success") {
					$.dialog.alert("统计成功！");
				} else {
					$.dialog.alert(data);
				}
			}	
		 });
	}
</script>
</head>
<body>
<sany:menupath />

		&nbsp;&nbsp;&nbsp;&nbsp;
		<select id="siteId" name="siteId" onchange="doSiteChange(value)">
			
		</select>
		&nbsp;&nbsp;
		
		实时统计：<font id="sumCount"></font>&nbsp;&nbsp;<a href="javascript:void(0)" class="bt_1"
												id="queryButton" onclick="statistic()"><span>立即统计</span> </a> 
		<div style="height: 10px"></div>
		<iframe id="operateCounterMain"  name="operateCounterMain"  src=""  frameborder="0"  scrolling="no" width="100%"  height="1000" ></iframe>
</body>
</html>
