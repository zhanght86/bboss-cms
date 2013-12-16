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

</head>
<body>
	<div class="mcontent">
		<div class="tabbox" >
			<ul class="tab" id="menu2">
				<li><a href="javascript:void(0)" onclick='setTab(2,0,{frameid:"tab0iframe",framesrc:"browserCounterQuery.jsp?siteId=${param.siteId}"})'><span>站点访问记录</span></a></li>
				<li><a href="javascript:void(0)" onclick='setTab(2,1,{frameid:"tab2iframe",framesrc:"browserCounterGatherQuery.jsp?siteId=${param.siteId}"})'><span>站点访问页面汇总</span></a></li>
				<li><a href="javascript:void(0)" class="current" onclick="setTab(2,2)"><span>站点访问概况</span></a></li>
			</ul>
		</div>
		<div id="main2">
			<ul  id="tab1" style="display:none;">
				<iframe id="tab0iframe"  frameborder="0" width="100%"  height="630" ></iframe>
				
			</ul>
			<ul id="tab2" style="display: none;">
				<iframe id="tab2iframe"  frameborder="0" width="100%"  height="630" ></iframe>
			</ul>
			<ul id="tab3" style="display: block;">
				<jsp:include page="browserCounterView.jsp?siteId=${param.siteId}"  flush="true"/>
			</ul>
		</div>
	</div>
</body>
</html>
