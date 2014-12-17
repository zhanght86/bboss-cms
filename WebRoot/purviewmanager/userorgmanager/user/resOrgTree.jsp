<%
/**
 * 
 * <p>Title: 机构tab页面</p>
 *
 * <p>Description: 机构tab页面，机构分为全局操作和详细操作</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: bboss</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
 */
 %>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@page import="com.frameworkset.common.poolman.DBUtil,
			com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String userId = request.getParameter("userId");
	String orgId = request.getParameter("orgId");
	String isOrgManager = request.getParameter("isOrgManager");
	String url = "operList_global.jsp?resTypeId=orgunit&currRoleId="+userId+"&currOrgId="+orgId+"&role_type=user";
	String urlreadorgname = "resReadOrgNameTree.jsp?resTypeId=orgunit&currRoleId="+userId+"&currOrgId="+orgId+"&role_type=user";
%>

<html>
<head>
<tab:tabConfig/>
<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
<%@ include file="/include/css.jsp"%>

</head> 
<!-- 
<frameset name="userId" cols="30,70" frameborder="no" border="0" framespacing="0" >
	
  	<frame src="resOrgTree.jsp?resTypeId=orgunit" name="globalOperList" id="globalOperList" scrolling="No" noresize="noresize" />
  	<frame src="user_operdefault.jsp" name="operList" scrolling="No" noresize="noresize" id="orgList" />
</frameset>
<noframes>
</noframes>
-->
<body>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">

		<tr>
			<td colspan="2">
				<tab:tabContainer id="orgResFrame" selectedTabPaneId="readorgname_res" skin="bluesky">
<!-- ------------------------------------------------------------------------------------------------------------------------------------------>
					<% 
						if("true".equals(isOrgManager)){
					%>
					<tab:tabPane id="orgGlog_res" tabTitle="机构全局权限" lazeload="true">
						<tab:iframe id="orgGlogRes" src="<%=url%>" frameborder="0" scrolling="no" width="98%" height="550">
						</tab:iframe>
					</tab:tabPane>
					<% 
						}
					%>
					<tab:tabPane id="readorgname_res" tabTitle="机构可访问税务机关权限" lazeload="true">
						<tab:iframe id="readorgnameRes" src="<%=urlreadorgname%>" frameborder="0" scrolling="no" width="98%" height="550">
						</tab:iframe>
					</tab:tabPane>
					
			
					
<!-------------------------------------------------------------------------------------------------------------------------------->
					
				</tab:tabContainer>			
			</td>
		</tr>
  </table>	
</body>

</html>
