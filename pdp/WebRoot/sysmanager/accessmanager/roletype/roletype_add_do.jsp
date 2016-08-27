<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*" errorPage="" %>
<%@ include file="../../../sysmanager/include/global1.jsp"%>
<%@ taglib   uri="../../../WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib   uri="../../../WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.RoleType"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager"%>
<html>
<head><title></title>
<LINK href="../../../css.css" type=text/css rel=stylesheet>
<style type="text/css">
<!--
.STYLE2  {	color: #263F77;
	font-weight: bold;
}
.STYLE3 {color: #FF0000}
-->
</style>
</head>
<body>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response); //页面登录保护
	String curOrgId = accessControl.getChargeOrgId();
	String curUserId = accessControl.getUserID(); 
%>
<% 
		RoleType rt = new RoleType();
		rt.setTypeName(request.getParameter("roletype"));
		rt.setTypeDesc(request.getParameter("typedesc"));
		rt.setCreatorUserId(curUserId);
		rt.setCreatorOrgId(curOrgId);
		RoleTypeManager rtm = new RoleTypeManager();
		boolean flag = rtm.isRoleTypeExist(rt);
		System.out.println(flag);
		if(!flag)
		{
		if(rtm.addRoleType(rt))
		{
%>
		<script language="javascript">
		window.alert("新增角色类型成功!");
		window.dialogArguments.location="roletype_list.jsp";
		window.close();  
		</script>
<%
		}
		}
		else
		{
%>
<script language="javascript">
		window.alert("新增角色已经存在!");  
		</script>
		<%
		}
		%>
</body>
</html>

