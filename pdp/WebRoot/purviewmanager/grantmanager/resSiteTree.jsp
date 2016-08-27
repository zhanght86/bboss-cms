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
	
	String siteUrl = "resSiteFrame.jsp?resTypeId=site&currRoleId="+userId+"&currOrgId="+orgId+"&role_type="+role_type;
	

	
	String sitetplUrl  = "resSiteFrame.jsp?resTypeId=sitetpl&currRoleId="+userId+"&currOrgId="+orgId+"&role_type="+role_type;
	String sitefileUrl = "resSiteFrame.jsp?resTypeId=sitefile&currRoleId="+userId+"&currOrgId="+orgId+"&role_type="+role_type;
	String siteChannelUrl ="resSiteFrame.jsp?resTypeId=site.channel&currRoleId="+userId+"&currOrgId="+orgId+"&role_type="+role_type;;
	String siteDocUrl = "resSiteFrame.jsp?resTypeId=site.doc&currRoleId="+userId+"&currOrgId="+orgId+"&role_type="+role_type;
	
	//增加站点应用
	String siteAppUrl = "resSiteAppFrame.jsp?resTypeId=app_column&currRoleId="+userId+"&currOrgId="+orgId+"&role_type="+role_type;
	
	
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
				<tab:tabContainer id="siteResFrame" selectedTabPaneId="site_res" skin="sany">
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
					
					<tab:tabPane id="site.app_res" tabTitle="站点应用资源" lazeload="true">
						<tab:iframe id="siteAppRes" src="<%=siteAppUrl%>" frameborder="0" 
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
