<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>



<%--
	
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<!-- <title>操作日志详细</title>-->
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<script type="text/javascript">
</script>
</head>
<body>
	<div id="customContent">
		<div class="title_box">
			<strong>操作日志明细</strong>
		</div>
		<div id="changeColor" style="width: 1150px; overflow: scroll;">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="stable" id="tb">
				<pg:list  requestKey="operateCounterDetail">
				<tr>
				<td>应用名称</td>
				<td><pg:cell colName="appName" /></td>
				</tr>
				<tr>
				<td>模块名称</td>
				<td><pg:cell colName="moduleName" /></td>
				</tr>
				<tr>
				<td>页面URL</td>
				<td><pg:cell colName="pageURL" /></td>
				</tr>
				<tr>
				<td>来源页面</td>
				<td><pg:cell colName="pageURL" /></td>
				</tr>
				<tr>
				<td>浏览器类型</td>
				<td><pg:cell colName="browserType" /></td>
				</tr>
				<tr>
				<td>操作人</td>
				<td><pg:cell colName="operator" /></td>
				</tr>
				<tr>
				<td>操作时间</td>
				<td><pg:cell colName="operTime" dateformat="yyyy-MM-dd  HH:mm:ss" /></td>
				</tr>
				<tr>
				<td>操作IP</td>
				<td><pg:cell colName="operateIp" /></td>
				</tr>
				<tr>
				<td>操作内容</td>
				<td><pg:cell colName="operContent" /></td>
				</tr>
				</pg:list>
			</table>
		</div>
	</div>
	<div id = "custombackContainer"></div>
</body>
</html>