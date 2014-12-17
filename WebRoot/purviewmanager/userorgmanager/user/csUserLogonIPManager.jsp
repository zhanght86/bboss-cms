<%
/*
 * <p>Title: CS用户登录管理</p>
 * <p>Description: CS用户登录管理</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-4-12
 * @author peng.yang
 * @version 1.0
 */
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../../sysmanager/include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	
	String userId = request.getParameter("userId");
	String action = request.getParameter("clear");
	String logonIp = "";
	try{
		DBUtil db = new DBUtil();
		String sql = "";
		if("yes".equals(action)){
			sql = "update td_sm_user set LOGON_IP='' where USER_ID = "+userId;
			db.executeUpdate(sql);
		}else{
			sql = " select LOGON_IP from td_sm_user where USER_ID = "+userId;
			db.executeSelect(sql);
			if(db.size() > 0){
				logonIp = db.getString(0,"LOGON_IP");
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	
	if("yes".equals(action)){
%>
		<script>
			alert("清空成功！");
			window.close();
		</script>
<%  }
%>
<html>
	<head>
		<title>CS用户登录管理</title>
<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
		<script language="JavaScript">
			function clearIp(){
				document.subClearIpForm.action ="csUserLogonIPManager.jsp";
				document.subClearIpForm.target="subClearIp";
				document.subClearIpForm.submit();
			}
		</script>
	</head>
	<body><center><br/><br/><br/>
		<form action="" id="subClearIpForm" name="subClearIpForm" target="subClearIp">
			<input type="hidden" name="userId" id="userId" value="<%=userId %>" />
			<input type="hidden" name="clear" id="clear" value="yes" />
			<span style="font:10pt" >CS用户登录IP:</span>
			<input type="text" name="csip" id="csip" size="30" readOnly value="<%=logonIp %>"/>
			<br/><br/>
			<input type="button" value="清除" nam="btn" class="input" onclick="clearIp()"/>
		</form>
	</center>
	</body>
	<iframe name="subClearIp" height="0" width="0"></iframe>
</html>
