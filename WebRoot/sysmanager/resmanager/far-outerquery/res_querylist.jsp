<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
/**
 * 
 * <p>Title: 资源授予情况列表</p>
 *
 * <p>Description: 资源授予情况列表</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2008-11-4
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accessControl = AccessControl.getInstance();
	if(!accessControl.checkManagerAccess(request, response)){
		return;
	}
	
	//授予的对象查询，包括（机构，用户，角色，机构岗位）  必选
	String type = request.getParameter("type");
%>
<html>
<head>
	<title>资源操授予查询</title>
	<script type="text/javascript" language="Javascript">
	</script>
</head>
<body>
	<div id="changeColor">
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.purviewmanager.tag.ResQueryList" keyName="ResQueryList"/> 
		<pg:pager maxPageItems="20" scope="request" data="ResQueryList" isList="false">
			<pg:param name="restypeId"/>
	  		<pg:param name="resId"/>
	  		<pg:param name="resName"/>
	  		<pg:param name="operategroup"/>
	  		<pg:param name="type"/>
	  		<pg:param name="selid"/>
	  		<pg:param name="selname"/>
	  		<pg:param name="isRecursion"/>
			
			<pg:equal actual="${ResQueryList.itemCount}" value="0" >
				<div class="nodata">
				<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
			</pg:equal>
			<pg:notequal actual="${ResQueryList.itemCount}"  value="0">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
					<pg:header>
						<%if(type.equals("user")){ %>
						<th><pg:message code="sany.pdp.sysmanager.resource.user.belong.org"/></th>
						<th><pg:message code="sany.pdp.sysmanager.resource.user.account"/></th>
						<th><pg:message code="sany.pdp.sysmanager.resource.user.name"/></th>
						<%}else if(type.equals("role")){ %>
						<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.name"/></th>
						<th><pg:message code="sany.pdp.sysmanager.resource.role.type"/></th>
						<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.creator"/></th>
						<%}else if(type.equals("orgjob")){ %>
						<th><pg:message code="sany.pdp.sysmanager.resource.post.belong.org"/></th>
						<th><pg:message code="sany.pdp.personcenter.person.post.name"/></th>
						<th><pg:message code="sany.pdp.sysmanager.resource.post.creator"/></th>
						<%}else if(type.equals("org")){ %>
						<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.displayname"/></th>
						<%} %>
						<th><pg:message code="sany.pdp.role.operation.type"/></th>
						<th><pg:message code="sany.pdp.role.resource.source"/></th>
						<th><pg:message code="sany.pdp.role.empower.time"/></th>
					</pg:header>
					
					<pg:list>
						<tr>	      	
						    <%if(type.equals("user")){ %>
							<td><pg:cell colName="remark5" defaultValue=""/></td>							
							<td><pg:cell colName="userName" defaultValue=""/></td>
							<td><pg:cell colName="userRealname" defaultValue=""/></td>
							<%}else if(type.equals("role")){ %>
							<td><pg:cell colName="roleName" defaultValue=""/></td>							
							<td><pg:cell colName="roleTypeName" defaultValue=""/></td>
							<td><pg:cell colName="userRealname" defaultValue=""/></td>
							<%}else if(type.equals("orgjob")){ %>
							<td><pg:cell colName="remark5" defaultValue=""/></td>							
							<td><pg:cell colName="jobName" defaultValue=""/></td>
							<td><pg:cell colName="userRealname" defaultValue=""/></td>
							<%}else if(type.equals("org")){ %>
							<td><pg:cell colName="remark5" defaultValue=""/></td>
							<%} %>
							<td><pg:cell colName="opName" defaultValue=""/></td>
							<td>
							<pg:cell colName="resResource" defaultValue=""/>
							</td>
							<td>
								<pg:cell colName="sDate" defaultValue="" dateformat="yyyy-MM-dd HH:mm:ss"/>
							</td>
						</tr>
					</pg:list>
				</table>
				<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
			</pg:notequal>
		</pg:pager>
	<div>

</body>
</html>

