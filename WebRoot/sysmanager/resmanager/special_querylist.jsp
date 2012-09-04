<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%
AccessControl accesscontroler = AccessControl.getInstance();
accesscontroler.checkAccess(request, response);
String special = (String)request.getParameter("special");
%>
<html>
<head>    
 <title>属性容器</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">

</SCRIPT>
<body>
	<div id="changeColor">
		<pg:listdata dataInfo="BrowseResList" keyName="BrowseResList"/> 
		<pg:pager maxPageItems="20" scope="request" data="BrowseResList" isList="false">
			<pg:param name="special"/>
			<pg:param name="resId"/>
			<pg:param name="resName"/>
			<pg:param name="opId"/>
			
			<pg:equal actual="${BrowseResList.itemCount}" value="0" >
				<div class="nodata">
				<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
			</pg:equal>
			<pg:notequal actual="${BrowseResList.itemCount}"  value="0">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
					<pg:header>
						<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.type"/></th>
						<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.token"/></th>
						<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.resource.name"/></th>
						<th><pg:message code="sany.pdp.personcenter.person.res.operation.type"/></th>
					</pg:header>
					
					<pg:list>
						<tr>	      	
							<td><pg:cell colName="resTypeName" defaultValue=""/></td>							
							<td><pg:cell colName="resId" defaultValue=""/></td>
							<td><pg:cell colName="resName" defaultValue=""/></td>
							<td><pg:cell colName="opName" defaultValue=""/></td>
						</tr>
					</pg:list>
				</table>
				<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
			</pg:notequal>
		</pg:pager>
	<div>

</body>

</html>
