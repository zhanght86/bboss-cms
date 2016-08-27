<%
/*
 * <p>Title:角色资源权限查询框架页面</p>
 * <p>Description: 角色资源权限查询框架页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-28
 * @author baowen.liu
 * @version 1.0
 */
%>


<%@ page contentType="text/html; charset=UTF-8" language="java"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>


<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil,
				com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);

	String roleId = (String)request.getParameter("roleId");
	String type = (String)request.getParameter("typeName");
    RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById(roleId);
	String name=role.getRoleName();
   

%>
<HTML>
 <HEAD>
   <title>【<%=name%>】角色资源权限查询</title>
   
 </HEAD>
  <frameset rows="90,*" border=0>
	<frame frameborder=0  noResize scrolling="no" marginWidth=0 name="forQuery" src="roleres_query.jsp?name=<%=name%>&type=<%=type%>"></frame>		
	<frame frameborder=0 noResize scrolling="auto" marginWidth=0 name="forDocList" src="roleres_querylist.jsp?name=<%=name%>&type=<%=type%>"></frame>
	</frameset>	
	
</HTML>
