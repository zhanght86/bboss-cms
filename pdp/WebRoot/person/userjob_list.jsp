<%@ page language="java" contentType="text/html;charset=UTF-8"%>

<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	
   accesscontroler.checkAccess(request,response);
   

   	String type = "user";

%>

<html >
      

<head>
		<title>属性容器</title>
<body>
	<div id="changeColor">
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.JobByUserList" keyName="JobByUserList"/> 
		<pg:pager maxPageItems="20" scope="request" data="JobByUserList" isList="false">
			
			<pg:equal actual="${JobByUserList.itemCount}" value="0" >
				<div class="nodata">
				<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
			</pg:equal>
			<pg:notequal actual="${JobByUserList.itemCount}"  value="0">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb" >
					<pg:header>
						<th><pg:message code='sany.pdp.personcenter.person.post.name'/></th>
						<th><pg:message code='sany.pdp.personcenter.person.post.belong.org'/></th>
						<th><pg:message code='sany.pdp.personcenter.person.post.duty'/></th>
						<th><pg:message code='sany.pdp.personcenter.person.post.person.number'/></th>
						<th><pg:message code='sany.pdp.personcenter.person.post.contion'/></th>
						<th><pg:message code='sany.pdp.personcenter.person.post.level'/></th>
						<th><pg:message code='sany.pdp.personcenter.person.post.description'/></th>
					</pg:header>
					<pg:list>
							<tr>	      	
								<td><pg:cell colName="jobName" defaultValue=""/></td>							
								<td ><pg:cell colName="jobNumber" defaultValue=""/></td>
								<td><pg:cell colName="jobFunction" defaultValue=""/></td>
								<td><pg:cell colName="jobAmount" defaultValue=""/></td>
								<td><pg:cell colName="jobCondition" defaultValue=""/></td>
								<td><pg:cell colName="jobRank" defaultValue=""/></td>
								<td><pg:cell colName="jobDesc" defaultValue=""/></td>
							</tr>
						</pg:list>	
				</table>
				<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
			</pg:notequal>
		</pg:pager>
	</div>
</body>
</html>

