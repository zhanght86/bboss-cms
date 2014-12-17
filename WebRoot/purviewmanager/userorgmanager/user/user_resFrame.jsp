<%
/**
 * 
 * <p>Title: 用户资源授予页面</p>
 *
 * <p>Description: 用户权限授予tab页面</p>
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
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%@ page import="com.frameworkset.platform.resource.ResourceManager
				,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase
				,com.frameworkset.platform.sysmgrcore.manager.UserManager,com.frameworkset.platform.sysmgrcore.entity.User
				,com.frameworkset.platform.config.ConfigManager
				,com.frameworkset.platform.security.AccessControl,
				java.util.List,
				java.util.ArrayList"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String userId = request.getParameter("userId");
	String orgId = request.getParameter("orgId");
	
	
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(userId);
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
<title>用户【<%=userName%>】资源操作授予</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>

</head> 
<body bgcolor="#F7F8FC" >
	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">

		<tr>
			<td colspan="2">
				<tab:tabContainer id="user_resFrame" selectedTabPaneId="user-manage">
				<pg:list requestKey="resTypeList" needClear="false">
				
	      			<pg:equal colName="auto" value="true">
	      			<%
	      			String id = "purview-" + dataSet.getString("id");
	      			String iframeid = "iframe-" + dataSet.getString("id");
	      			String name = dataSet.getString("name");
	      			String link = dataSet.getString("resource")+"?userId="+userId+"&orgId="+orgId;
	      			%>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------>
					<tab:tabPane id="<%=id%>" tabTitle="<%=name%>" lazeload="true">
						<tab:iframe id="<%=iframeid%>" src="<%=link%>" frameborder="0" scrolling="no" width="98%" height="600">
						</tab:iframe>
					</tab:tabPane>
			
					
					
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


