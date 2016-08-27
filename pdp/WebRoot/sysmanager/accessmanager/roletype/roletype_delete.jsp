<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage="" %>
<%@ taglib   uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib   uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.RoleType"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager"%>
<%@ page import="java.util.*" %>
<html>
<head>
<title>角色类型删除</title>
<LINK href="../../../css.css" type=text/css rel=stylesheet>
<style type="text/css">
<!--
.STYLE2 {	color: #263F77;
	font-weight: bold;
}
.STYLE3 {color: #FF0000}
-->
</style>
</head>
<body>
<%AccessControl accessControl = AccessControl.getInstance();
			accessControl.checkManagerAccess(request,response); //页面登录保护

			boolean tag = true;
			String notice = "";

			//删除操作
			RoleTypeManager rtm = new RoleTypeManager();
			String id = request.getParameter("roletype_id");
			if ((id != null) && (id.length() > 0)) {
				StringTokenizer stValues = new StringTokenizer(id, ";");

				while (stValues.hasMoreElements()) {
					String nextId = (String) stValues.nextElement();
					if (rtm.hasRoles(nextId))//已经有角色的类别不能被删除
					{
						String name = rtm.getRoleType(nextId).getTypeName();
						notice = notice + "角色类别‘" + name + "’存在角色，不能被删除。\\n";
						notice = notice + "角色类别‘" + name + "’存在角色，不能被删除。\\n";
					} 
					else
					{
						boolean delTag = rtm.delRoleType(nextId);
						if(!delTag)
						{
							String name = rtm.getRoleType(id).getTypeName();
							notice = notice + "角色类别‘" + name + "’删除失败。\\n";
						}
					}
				}
				if ( (notice.length() == 1) || (notice.equals("")) )
				{
					notice = "删除成功！";
				}
			}

		%>
<SCRIPT>
	window.alert("<%=notice%>");
	parent.window.location.href = "roletype_list.jsp";
</SCRIPT>

