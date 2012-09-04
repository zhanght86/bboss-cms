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
	

	String roleId = request.getParameter("roleId");
	
	String siteUrl = "../grantmanager/resSiteFrame.jsp?resTypeId=site&currRoleId="+roleId+"&role_type=role";
	String sitetplUrl  = "../grantmanager/resSiteFrame.jsp?resTypeId=sitetpl&currRoleId="+roleId+"&role_type=role";
	String sitefileUrl = "../grantmanager/resSiteFrame.jsp?resTypeId=sitefile&currRoleId="+roleId+"&role_type=role";
	String siteChannelUrl ="../grantmanager/resSiteFrame.jsp?resTypeId=site.channel&currRoleId="+roleId+"&role_type=role";;
	String siteDocUrl = "../grantmanager/resSiteFrame.jsp?resTypeId=site.doc&currRoleId="+roleId+"&role_type=role";
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
				<tab:tabContainer id="siteResFrame" selectedTabPaneId="site_res" skin="bluesky">
<!-- ------------------------------------------------------------------------------------------------------------------------------------------>
					<tab:tabPane id="site_res" tabTitle="站点资源" lazeload="true">
						<tab:iframe id="siteRes" src="<%=siteUrl%>" frameborder="0" 
								scrolling="no" width="98%" height="550">
						</tab:iframe>
					</tab:tabPane>
					
					<tab:tabPane id="sitetpl_res" tabTitle="站点模板资源" lazeload="true">
						<tab:iframe id="sitetplRes" src="<%=sitetplUrl%>" frameborder="0" 
								scrolling="no" width="98%" height="550">
						</tab:iframe>
					</tab:tabPane>
					
					<tab:tabPane id="sitefile_res" tabTitle="站点文件视图资源" lazeload="true">
						<tab:iframe id="sitefileRes" src="<%=sitefileUrl%>" frameborder="0" 
								scrolling="no" width="98%" height="550">
						</tab:iframe>
					</tab:tabPane>
					
					<tab:tabPane id="site.channel_res" tabTitle="站点频道资源" lazeload="true">
						<tab:iframe id="sitechannelRes" src="<%=siteChannelUrl%>" frameborder="0" 
								scrolling="no" width="98%" height="550">
						</tab:iframe>
					</tab:tabPane>
					
					<tab:tabPane id="site.doc_res" tabTitle="站点文档资源" lazeload="true">
						<tab:iframe id="sitedocRes" src="<%=siteDocUrl%>" frameborder="0" 
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
