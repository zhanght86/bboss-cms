<%
/*
 * <p>Title:角色下权限授予</p>
 * <p>Description: 角色下的权限授予</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-4-2
 * @author liangbing.tao
 * @version 1.0
 */
%>
<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>


<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>

<%@ page import="com.frameworkset.platform.resource.ResourceManager,
				 com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				 com.frameworkset.platform.sysmgrcore.manager.OrgManager,
				 com.frameworkset.platform.sysmgrcore.entity.Organization,
				 com.frameworkset.platform.config.ConfigManager,
				 com.frameworkset.platform.security.AccessControl,
				 java.util.List,
				 java.util.ArrayList"%>
				

<%

	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	
	String orgId = request.getParameter("orgId");//授权的机构ID

	OrgManager orgManager = SecurityDatabase.getOrgManager();
	
	Organization organization = orgManager.getOrgById(orgId);
	
	String orgName = organization.getRemark5() ;

	
	ResourceManager resManager = new ResourceManager();
	
	List list = resManager.getResourceInfos();
	
	//加开关，如果不允许超级管理员之外的用户进行菜单授权，当前用户又不是超级管理员，
	//则去掉菜单项，请注意，是超级管理员，重复，不是啰嗦！
	
	boolean state = ConfigManager.getInstance().getConfigBooleanValue("enablecolumngrant", true)
				 && !accesscontroler.getUserID().equals("1");
	
	if(list == null)
		list = new ArrayList();
	request.setAttribute("resTypeList",list);
	
%>

<html>
	<head>
		<tab:tabConfig/>
		<title>机构【<%=orgName%>】权限授予</title>
		<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
	</head> 
	
	<body bgcolor="#F7F8FC" >
		<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
	
			<tr>
				<td colspan="2">
					<tab:tabContainer id="role_resFrame" selectedTabPaneId="role-manage">
					<pg:list requestKey="resTypeList" needClear="false">
					
		      			<pg:equal colName="auto" value="true">
		      			<%
		      			String id = "purview-" + dataSet.getString("id");
		      			String iframeid = "iframe-" + dataSet.getString("id");
		      			String name = dataSet.getString("name");
		      			String link = dataSet.getString("resource")+"?orgId="+orgId;
		      			if(!(dataSet.getString("resource").equals("resColumnTree.jsp") && !accesscontroler.getUserID().equals("1")))
		      			{
	      				%>
		      			
	<!-- ------------------------------------------------------------------------------------------------------------------------------------------>
						<tab:tabPane id="<%=id%>" tabTitle="<%=name%>" lazeload="true">
							<tab:iframe id="<%=iframeid%>" src="<%=link%>" frameborder="0" scrolling="no" width="98%" height="600">
							</tab:iframe>
						</tab:tabPane>
						<%

							}
						%>
	<!-------------------------------------------------------------------------------------------------------------------------------->
					  </pg:equal>					
		  			</pg:list>	
					</tab:tabContainer>			
				</td>
			</tr>
		</table>	
	  <iframe name="exeman" width="0" height="0" style="display:none"></iframe>
	</body>
</html>


