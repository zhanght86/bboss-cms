<%
/**
 * 
 * <p>Title: 用户组tab页面</p>
 *
 * <p>Description: 用户组tab页面，用户组分为全局操作和详细操作</p>
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
	String url = "operList_global.jsp?resTypeId=group&currRoleId="+userId+"&currOrgId="+orgId+"&role_type=user";
	String lefturl = "usergroupsetTree.jsp?resTypeId=group&currRoleId="+userId+"&currOrgId="+orgId+"&role_type=user";
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
				<tab:tabContainer id="groupResFrame" selectedTabPaneId="groupGlog_res" skin="bluesky">
<!-- ------------------------------------------------------------------------------------------------------------------------------------------>
					<tab:tabPane id="groupGlog_res" tabTitle="用户组全局权限" lazeload="true">
						<tab:iframe id="groupGlogRes" src="<%=url%>" frameborder="0" scrolling="no" width="98%" height="550">
						</tab:iframe>
					</tab:tabPane>
<!-------------------------------------------------------------------------------------------------------------------------------->
					
				</tab:tabContainer>			
			</td>
		</tr>
  </table>	
</body>

</html>
