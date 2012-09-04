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
	
	String userId = request.getParameter("currRoleId");
	String orgId = request.getParameter("orgId");
	String role_type = request.getParameter("role_type");
	
	String channelUrl = "resChannelFrame.jsp?resTypeId=channel&currRoleId="+userId+"&currOrgId="+orgId+"&role_type="+role_type;
	String channeldocUrl  = "resChannelFrame.jsp?resTypeId=channeldoc&currRoleId="+userId+"&currOrgId="+orgId+"&role_type="+role_type;
	
%>

<html>
<head>
<tab:tabConfig/>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>

</head> 
<body>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">

		<tr>
			<td colspan="2">
				<tab:tabContainer id="channelResFrame" selectedTabPaneId="channel_res" skin="sany">
<!-- ------------------------------------------------------------------------------------------------------------------------------------------>
					<tab:tabPane id="channel_res" tabTitle="频道资源" lazeload="true">
						<tab:iframe id="channelRes" src="<%=channelUrl%>" frameborder="0" 
								scrolling="no" width="98%" height="550">
						</tab:iframe>
					</tab:tabPane>
					
					<tab:tabPane id="channeldoc_res" tabTitle="频道文档资源" lazeload="true">
						<tab:iframe id="channeldocRes" src="<%=channeldocUrl%>" frameborder="0" 
								scrolling="no" width="98%" height="550">
						</tab:iframe>
					</tab:tabPane>
					
<!-------------------------------------------------------------------------------------------------------------------------------->
					
				</tab:tabContainer>			
			</td>
		</tr>
  </table>	
</body>

</html>
