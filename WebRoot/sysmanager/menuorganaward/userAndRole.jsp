<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>
<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<% 
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request,response);
	//当前用户是否拥有超级管理员权限与部门管理员权限
	boolean isAdminOrOrgManager = false;
	//是否是管理员
	boolean isOrgManager = new OrgAdministratorImpl().isOrgManager(accessControl.getUserID());
	boolean isAdmin = accessControl.isAdmin();
	if(isAdmin || isOrgManager){
		isAdminOrOrgManager = true;
	}
%>
<html>
<head>
<title>资源与机构可见操作授予</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<SCRIPT type="text/javascript">
	function chooseUR(){
		var v = document.all("choose").value;
		var type = "";
		if(v=="1"){
			type = "user";
			userOrRoleFrame.location.href = "orgByUserFrame.jsp?";
		}else{
			type = "role";
			userOrRoleFrame.location.href = "roleList.jsp";
		}
		getPropertiesContent().document.frames.saveOrgGrant.location.href="saveOrgGrant.jsp?type="+type;
		getPropertiesContent().document.frames.orgChooseTree.location.href="orgChooseTree.jsp?type="+type;
		getPropertiesContent().document.frames.saveMenuGrant.location.href="saveMenuGrant.jsp?type="+type;
		getPropertiesContent().document.frames.menuTree.location.href="menuTree.jsp?type="+type;
	}
</SCRIPT>
</head>

<body scroll="no">
<%if(isAdminOrOrgManager){ %>
	<form action="" target="userOrRoleFrame" method="post" name="chooseForm">
		<table cellspacing="0" id="table2" cellpadding="0" border="0" width="100%">
			<tr>
				<td width="50%">
				 	请选择将要授予的对象:
				</td>
				<td>
					<select id="choose" name="choose" onchange="chooseUR()" style="width: 100px">
						<option value="1" selected>用户</option>
						<option value="2">角色</option>
					</select>
				</td>
			</tr>
		</table>
	</form>
	<iframe src="orgByUserFrame.jsp" width="100%" height="100%"
	 marginWidth=0 marginHeight=0 id="userOrRoleFrame" />
<%}else{ %>
<div align="center">没有权限！</div>
<%} %>
</body>
</html>


