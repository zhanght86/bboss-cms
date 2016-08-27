<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>管控指标</title>
	<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=basePath %>/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath %>/css/tab.winclassic.css">
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=basePath %>/sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath %>/sysmanager/css/tab.winclassic.css">
		<script language="JavaScript" src="<%=basePath %>/include/pager.js" type="text/javascript"></script>
		<script language="javascript" src="<%=basePath %>/sysmanager/scripts/selectTime.js"></script>
		<script language="JavaScript"
			src="<%=request.getContextPath()%>/public/datetime/calender_date.js"></script>
			<script type="text/javascript">
			
			function clearInfos()
			{
				var tableInfoForm = document.getElementById("tableInfoForm");
				tableInfoForm.xm.value = "";
				tableInfoForm.cardno.value = "";
				tableInfoForm.startDate.value = "";
				tableInfoForm.endDate.value = "";
			}
			function query()
			{
				var tableInfoForm = document.getElementById("tableInfoForm");
				tableInfoForm.action = "manageguide_list.jsp";
				tableInfoForm.target = "queryList";	
				tableInfoForm.submit();
			}
			</script>
  </head>
  
  <body>
    <body class="contentbodymargin" onload="query();">
		
		<div id="contentborder" align="center">
			<form name="tableInfoForm" id="tableInfoForm" action="" method="post">
			<div>
				<table width="100%" border="0" cellpadding="0" cellspacing="1" class="thin">
					<tr>
						<td width="10%" nowrap>指标编号:</td>
						<td width="15%" nowrap>
							<input type="text" name="controlCode" size="20" />
						</td>
						<td width="10%" nowrap>指标名称:</td>
						<td width="15%" nowrap>
							<input type="text" name="controlName" size="20" />
						</td>
						<td width="10%" nowrap>指标类型:</td>
						<td width="40%" nowrap>
						<input type="text" name="controlType" size="20" />
						     </td>
						</tr>
						<tr>
						<td align="center" colspan="6">
							<input type="button" name="queryButton" value="查询" class="input" onclick="query()"/>&nbsp;&nbsp;
							<input type="button" name="resetButton" value="重置" class="input"/>&nbsp;&nbsp;
							<input type="button" name="personinfButton" value="新增" class="input" />&nbsp;&nbsp;
							<input type="button" name="codeButton" value="修改" class="input" />&nbsp;&nbsp;
							<input type="button" name="hisButton" value="删除" class="input" />
						</td>
					</tr>
				</table>
			</div>
			<hr/>
			
			<iframe src="" name="queryList" frameborder="0" width="100%" height="480px">
	</body>
  </body>
</html>

