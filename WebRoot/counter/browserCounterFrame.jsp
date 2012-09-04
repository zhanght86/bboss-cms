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
<title>浏览统计数据</title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<script type="text/javascript">
	
	//页面加载时查询列表数据
	$(document).ready(function() {
		querySiteInfo();
	  });
	
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
				$.each(data,function(i,o){
					document.getElementById("siteId").options.add(new Option(data[i].name, data[i].siteId));
				});	
				
				getSiteSumCount(data[0].siteId);
				document.getElementById("browserCounterMain").src="browserCounterMain.jsp?siteId="+data[0].siteId;
			}	
		 });
	}
	
	function doSiteChange(siteId) {
		getSiteSumCount(siteId);
		document.getElementById("browserCounterMain").src="browserCounterMain.jsp?siteId="+siteId;
	}
	
	function statistic() {
		
		var siteId = document.getElementById("siteId").value;
		
		$.ajax({
	 	 	type: "POST",
			url : "statisticImmediately.freepage",
			data :{siteId:siteId},
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
		&nbsp;&nbsp;&nbsp;&nbsp;
		<select id="siteId" name="siteId" onchange="doSiteChange(value)">
			
		</select>
		&nbsp;&nbsp;
		访问总数：<font id="sumCount"></font>&nbsp;&nbsp;<a href="javascript:void(0)" class="bt_1"
												id="queryButton" onclick="statistic()"><span>立即统计</span> </a>
		<div style="height: 10px"></div>
		<iframe id="browserCounterMain"  name="browserCounterMain"  src=""  frameborder="0"  scrolling="no" width="100%"  height="1000" ></iframe>
</body>
</html>
