
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.bean.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.security.authorization.AuthUser"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    DocumentManager docManager = new DocumentManagerImpl();
    String docid = request.getParameter("docid");
    //当前任务id
    String taskid = request.getParameter("taskid"); 
    TaskDocument task = docManager.getTaskInfo(Integer.parseInt(taskid));
    String advice = task.getTaskOpinion();
    Date dtime = task.getSubmitTime();
    String submitName = task.getSubmitUserName();
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>审核意见查看</title>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
	</head>
	<body>
		<table width="100%" border="0">
		  <tr>
			<td align="left">
				审核人：<input value=<%=submitName%> disabled type="text" name="time">
			</td>
		  </tr>
		  <tr>
			<td align="left">
				提交时间：<input value=<%=dtime%> disabled type="text" name="time">
			</td>
		  </tr>
		  <tr>
		  <td align="left">
				审核意见：
			</td>
		  </tr>
		  <tr>
			<td align="center">
				<textarea name="opinion" rows="25" disabled="disabled" cols="60"><%=advice%></textarea>
			</td>
		  </tr>
		  <tr>
			<td align="center" colspan="2">
				<input name="closewin" type="button" class="cms_button" value="返回" onClick="window.close()">
			</td>
		  </tr>
		</table>

	</body>
</html>