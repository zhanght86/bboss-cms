<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>
<%
/**
 * 
 * <p>Title: 用户资源授予页面</p>
 *
 * <p>Description: 用户权限授予tab页面</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
 */
 %>
 <%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>

<%@page import="com.frameworkset.platform.config.model.ResourceInfo"%>

<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@ page import="com.frameworkset.platform.resource.ResourceManager
				,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase
				,com.frameworkset.platform.config.ConfigManager
				,com.frameworkset.platform.security.AccessControl,
				java.util.List,
				java.util.ArrayList"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	String curSystem = accesscontroler.getCurrentSystem();
	
	//授权类型 user或role
	String role_type = "user";
	
	//当前选中授权ID；根据授权类型有user，role两种授权类型，分别为用户ID，和角色ID
	String currRoleId = request.getParameter("currRoleId");
	
	//是否批量授权 true:批量；false:单个
	String isBatch = "false";
	
	//当前所选用户所属机构ID
	String orgId = request.getParameter("orgId");
	
	//是否管理员
	boolean isOrgManager = new OrgAdministratorImpl().isOrgManager(currRoleId);
	
	
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(currRoleId);
	String userName = user.getUserRealname();
	
	ResourceManager resManager = new ResourceManager();
	
	List list = resManager.getResourceInfos();
	
	//加开关，如果不允许超级管理员之外的用户进行菜单授权，当前用户又不是超级管理员，则去掉菜单项，请注意，是超级管理员，重复，不是啰嗦！
	boolean state = ConfigManager.getInstance().getConfigBooleanValue("enablecolumngrant", true) && !accesscontroler.getUserID().equals("1");
	
	if(list == null)
		list = new ArrayList();
	request.setAttribute("resTypeList",list);
	
%>

<html>
<head>
<tab:tabConfig/>
<title><pg:message code="sany.pdp.sys.purview.authorize"/></title>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
</head> 
<body>
	<div style="height: 10px">&nbsp;</div>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">

		<tr>
			<td colspan="2">
				<tab:tabContainer id="user_resFrame" selectedTabPaneId="purview-orgunit" skin="sany">
				<pg:list requestKey="resTypeList" needClear="false">
				
	      			<pg:equal colName="auto" value="true">
	      			<%
	      			ResourceInfo resourceInfo = (ResourceInfo)dataSet.getOrigineObject();
	      			boolean isAppend = resourceInfo.containSystem(curSystem);
	      			String id = "purview-" + dataSet.getString("id");
	      			String iframeid = "iframe-" + dataSet.getString("id");
	      			String name = dataSet.getString("name");
	      			name = resourceInfo.getName(request);
	      			StringBuffer link = new StringBuffer()
	      				.append(dataSet.getString("resource")).append("?currRoleId=").append(currRoleId)
		      			.append("&orgId=").append(orgId)
		      			.append("&role_type=").append(role_type)
		      			.append("&isBatch=").append(isBatch);
	      			System.out.println(link.toString());
	      			//System.out.println("link = " + link.toString());
	      			//boolean stateOrgColumn = (id.trim().equals("purview-orgunit") || id.trim().equals("purview-column"));
	      			boolean stateOrgColumn = (id.trim().equals("purview-group") || id.trim().equals("purview-role")
	      					 || id.trim().equals("purview-dict") || id.trim().equals("purview-job"));
	      			if(isAppend)
	      			{		 
	      				if(!(id.trim().equals("purview-column") && !accesscontroler.getUserID().equals("1")&& !state))
	      				{
	      					if(!isOrgManager && !stateOrgColumn)
	      					{
	      			%>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------>
					<tab:tabPane id="<%=id%>" tabTitle="<%=name%>" lazeload="true">
						<tab:iframe id="<%=iframeid%>" src="<%=link.toString()%>" frameborder="0" scrolling="no" width="98%" height="600">
						</tab:iframe>
					</tab:tabPane>
<!-------------------------------------------------------------------------------------------------------------------------------->
						<%
							}else if(isOrgManager)
							{
						%>
							<tab:tabPane id="<%=id%>" tabTitle="<%=name%>" lazeload="true">
								<tab:iframe id="<%=iframeid%>" src="<%=link.toString()%>" frameborder="0" scrolling="no" width="98%" height="600">
								</tab:iframe>
							</tab:tabPane>
					<%		}
						}
					}
					
					%>
				  </pg:equal>					
	  			</pg:list>	
				</tab:tabContainer>			
			</td>
		</tr>
	</table>	
	
  <iframe name="exeman" width="0" height="0" style="display:none"></iframe>
</body>
</html>


