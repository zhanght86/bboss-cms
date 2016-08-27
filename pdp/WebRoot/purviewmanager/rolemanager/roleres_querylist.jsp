<%
/*
 * <p>Title: 角色资源查询列表</p>
 * <p>Description: 角色资源查询列表</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-28
 * @author baowen.liu
 * @version 1.0
 *
 */
 %>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);

%>

<html>
	<head>    
		<title>属性容器</title>
		<script language="JavaScript" src="../../scripts/common.js" type="text/javascript"></script>		
		<script language="JavaScript" src="../../scripts/pager.js" type="text/javascript"></script>
		<SCRIPT language="javascript">
			function getOperateType(id)
			{
			    getopergroup.location.href = "resChange.jsp?restypeId="+id;
			}
		</SCRIPT>
	</head>
	<body>
		<table  width="100%" border="0" cellpadding="0" cellspacing="0" >
			<tr>
				<td><div style="width: 5px">&nbsp;</div></td>
				<td>
					<div id="changeColor">
						 <pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.BrowseResList"  keyName="BrowseResList"/>
						 <pg:pager maxPageItems="20" scope="request" data="BrowseResList" isList="false">
						 	<pg:param name="restypeId"/>
							<pg:param name="resId"/>
							<pg:param name="resName"/>
							<pg:param name="opId"/>
							<pg:param name="userId"/>
							<pg:param name="id"/>
							<pg:param name="orgjob"/>
							<pg:param name="type"/>
							<pg:param name="name"/>
							<pg:param name="typeName"/>
							<pg:param name="resResource"/>
							<pg:param name="orgId"/>
							
							<pg:equal actual="${BrowseResList.itemCount}" value="0" >
								<div class="nodata">
								<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
							</pg:equal>
							<pg:notequal actual="${BrowseResList.itemCount}"  value="0">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb" >
									<pg:header>
										<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.type" /></th>
										<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.token" /></th>
										<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.name" /></th>
										<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.operation" /></th>
										<%
											if("USER".equals(request.getParameter("typeName")))
											{
										%>
											<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.source" /></th>
										<%
											}
										%>
									</pg:header>
									<pg:list>
										<tr>	      	
											<td>
												<pg:cell colName="resTypeName" defaultValue=""/>
											</td>							
											<td >
												<pg:cell colName="resId" defaultValue=""/>
											</td>
											<td>
												<pg:cell colName="resName" defaultValue=""/>
											</td>
											<td>
												<pg:cell colName="opName" defaultValue=""/>
											</td>
											<%
												if("role".equals(request.getParameter("typeName")))
												{
											%>
												<td>
													<pg:cell colName="resResource" defaultValue=""/>
												</td>
											<%
												}
											%>
											<!-- <td class="tablecells" align=left nowrap><pg:cell colName="roleId" defaultValue=""/></td> -->
										</tr>
									</pg:list>
								</table>
								<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
							</pg:notequal>
						 </pg:pager>
					</div>
				</td>
			</tr>
		</table>
		<div style="width: 100px">&nbsp;</div>
		
	
	<iframe id="getopergroup" src="" border="0" height="0" width="0"></iframe>
	</body>

</html>
