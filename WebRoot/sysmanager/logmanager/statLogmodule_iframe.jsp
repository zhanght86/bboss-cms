<%     
  response.setHeader("Cache-Control", "no-cache"); 
  response.setHeader("Pragma", "no-cache"); 
  response.setDateHeader("Expires", -1);  
  response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>


<html>
<head>
<title>模块日志数统计</title>
</head>	
	
<body>
	<div id="changeColor">
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.LogModuleStat" keyName="LogDetailSearchList" />
		<pg:pager maxPageItems="15" scope="request" data="LogDetailSearchList" isList="false">
			<pg:param name="opRemark5"/>
			<pg:param name="isHis"/>
			<pg:param name="opOrgid"/>
			<pg:param name="isRecursion"/>
			<pg:param name="startDate"/>
			<pg:param name="endDate"/>
			<pg:param name="logVisitorial"/>
		
		<pg:equal actual="${LogDetailSearchList.itemCount}" value="0" >
			<div class="nodata">
			<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
		</pg:equal>
		<pg:notequal actual="${LogDetailSearchList.itemCount}"  value="0">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
				<pg:header>
					<th><pg:message code="sany.pdp.sysmanager.log.module.name"/></th>
					<th><pg:message code="sany.pdp.sysmanager.log.data.num"/></th>
				</pg:header>
				<pg:list>
					<tr>
						<td><pg:cell colName="module" defaultValue="" /></td>
						<td><pg:cell colName="count" defaultValue="" /></td>
					</tr>
				</pg:list>
			</table>
			<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
		</pg:notequal>
	</pg:pager>
	</div>
</body>
</html>
