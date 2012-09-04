<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page
	import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.security.authorization.AccessException"%>
<%@page import="com.frameworkset.platform.framework.Framework"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.UserManagerImpl"%>
<%@page import="java.util.List"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@page
	import="com.frameworkset.platform.sysmgrcore.manager.db.PortalUserManagerWebServiceImpl"%>

<%
	//System.out.println(session.getSessionContext());	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", -1);
	response.setDateHeader("max-age", 0);

	String doIt = request.getParameter("doIt");
	String message = "注意isSynPortalUser值设置为true，才会进行同步";
	if (doIt != null && doIt.equals("add")) {
		
		long totalBegin = System.currentTimeMillis();

		UserManagerImpl managerImpl = new UserManagerImpl();
		PortalUserManagerWebServiceImpl managerWebServiceImpl = new PortalUserManagerWebServiceImpl();

		List userList = managerImpl.getUserList();
		for (int i = 0; i < userList.size(); i++) {
			User user = (User) userList.get(i);
			managerWebServiceImpl.addUser(user, false);
		}

		long totalEnd = System.currentTimeMillis();
		message = ("批量同步用户操作完毕,同步" + userList.size() + "名用户，共耗时:"
				+ (totalEnd - totalBegin) + "ms.");
		
	}
%>
<html>
	<head>
		<script type="text/javascript">
			function addBatch(){	
				document.forms[0].submit();	
				document.getElementById('message').innerHTML = "";					
				document.getElementById('span').innerHTML = "批量同步用户开始!请等待...";						
			}
		</script>
	</head>
	<body>
		<form action="synPortalUser2.jsp" method="post">
			<center>
			<div id="message">
				<%=message%>
			</div>
			
			<br>
			
			<div id="span">
				批量用户同步到Portal(调用Web Services） &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" value="启动" onclick="addBatch();" />
				<input type="hidden" name="doIt" value="add">				
				
			</div>
			</center>
		</form>
	</body>
</html>
