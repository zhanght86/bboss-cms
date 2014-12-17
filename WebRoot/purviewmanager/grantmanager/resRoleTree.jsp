<%
/**
 * 
 * <p>Title: 角色资源tab页面</p>
 *
 * <p>Description: 角色资源tab页面，角色有全局全操作，与角色授予权限设置</p>
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
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	//权限授予类型  两种类型role与user
	String role_type = request.getParameter("role_type");
	
	//是否批量权限授予 true批量权限授予；false单个权限授予
	String isBatch = request.getParameter("isBatch");
	
	//currRoleId当前选中授权ID  传过来的ID为user或role的ID，根据role_type来区别  如果是批量权限授予多个user与role的ID用","分隔
	String currRoleId = request.getParameter("currRoleId");
	
	//用户所属机构ID
	String orgId = "";
	if(role_type.equals("user")){
		orgId = request.getParameter("orgId");
	}
	
	StringBuffer url = new StringBuffer()
		.append("operList_global.jsp?resTypeId=role&currRoleId=").append(currRoleId)
		.append("&currOrgId=").append(orgId)
		.append("&role_type=").append(role_type)
		.append("&isBatch=").append(isBatch);
	StringBuffer lefturl = new StringBuffer()
		.append("rolesetTree.jsp?resTypeId=role&currRoleId=").append(currRoleId)
		.append("&currOrgId=").append(orgId)
		.append("&role_type=").append(role_type)
		.append("&isBatch=").append(isBatch);
		
%>

<html>
<head>
<tab:tabConfig/>

</head> 
<body>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">

		<tr>
			<td colspan="2">
				<tab:tabContainer id="roleResFrame" selectedTabPaneId="roleGlog_res" skin="sany">
				<%
				if(!"user".equals(role_type))
				{
				%>
<!-- ------------------------------------------------------------------------------------------------------------------------------------------>
					<tab:tabPane id="roleGlog_res"  tabTitleCode="sany.pdp.purviewmanager.rolemanager.role.global.purview"  lazeload="true">
						<tab:iframe id="roleGlogRes" src="<%=url.toString()%>" frameborder="0" scrolling="no" width="98%" height="550">
						</tab:iframe>
					</tab:tabPane>
					<tab:tabPane id="roleset_res"  tabTitleCode="sany.pdp.purviewmanager.rolemanager.role.authorize.purview"  lazeload="true">
						<tab:iframe id="rolesetRes" src="<%=lefturl.toString()%>" frameborder="0" scrolling="no"  width="98%" height="550">
						</tab:iframe>
					</tab:tabPane>
<!-------------------------------------------------------------------------------------------------------------------------------->
				<%
				}
				%>
				<tab:tabPane id="roleGlog_attention"  tabTitleCode="sany.pdp.purviewmanager.rolemanager.role.authorize.notice">
					<font color="red"><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.notice.content"/></font>
					</tab:tabPane>
				
				</tab:tabContainer>			
			</td>
		</tr>
  </table>	
</body>

</html>