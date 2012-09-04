<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<html>
<head>
<title>系统管理</title>
<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	
	String roleId = (String) session.getAttribute("currRoleId");
	
	UserOrgManagerForm s = (UserOrgManagerForm)request.getAttribute("userOrgForm");	
	String oid = "";
	String uid = "";
	if(s != null){
    	oid =  s.getOrgId();
    	uid = s.getUserId();
    }   
    session.setAttribute("roleTabId", "2"); 
    //<frame src="userList.jsp" name="userList" scrolling="No" noresize="noresize" id="orgList" />
  
%>
</head> 
<frameset name="frame1" cols="30,70" frameborder="no"  border="1" framespacing="0" oid="<%=oid%>" uid="<%=uid%>">
  <frame src="orgTree.jsp" name="orgTree" id="orgTree" />
  <%
  if (accessControl.checkPermission(roleId,
                       "userset", AccessControl.ROLE_RESOURCE))
  {
  %>
  <frameset name="frame2" rows="65%,*" frameborder="yes" border="1" framespacing="1" >
    <frame src="userList_ajax.jsp" name="userList" scrolling="no" id="orgList"  />
    <frame src="roleUserList.jsp" name="roleUserList" scrolling="no" id="roleUserList"  />
  </frameset>
  <%
  }
  else
  {
  %>
  <frame src="userList_ajax.jsp" name="userList" scrolling="No" noresize="noresize" id="orgList" />
  <%
  }
  %>
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

