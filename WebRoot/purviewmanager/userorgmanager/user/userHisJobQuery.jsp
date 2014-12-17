<% 
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.UserManagerImpl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
/*
 * <p>Title: 用户历史任职情况</p>
 * <p>Description: 用户历史任职情况</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-9-9
 * @author gao.tang
 * @version 1.0
 */
 %>
 
<% 
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request, response);
	
	String userId = request.getParameter("userId");

		User user = new UserManagerImpl().getUserById(userId);
	
%>
<html>
<head>
	<title><pg:message code="sany.pdp.his.post"/></title>

<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
</head>
<body>
<br/>
<form name="com.frameworkset.goform" method="post">
</form>

	<div class="title_box">
		<strong><pg:message code="sany.pdp.his.post"/></strong>
	</div>
	<div id="changeColor">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table3" id="tb">	
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.UserHisJobQuery" keyName="userHisJobQuery" />
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="15" scope="request" data="userHisJobQuery" isList="false">
		<pg:equal actual="${userHisJobQuery.itemCount}" value="0" >
				 	
						<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
						
				</pg:equal> 
				<pg:notequal actual="${userHisJobQuery.itemCount}"  value="0">
		<pg:header>
			<th><pg:message code="sany.pdp.workflow.organization"/></th>
			<th><pg:message code="sany.pdp.job"/></th>
			<th><pg:message code="sany.pdp.take.office.time"/></th>
			<th><pg:message code="sany.pdp.leave.office.time"/></th>
		</pg:header>
		<pg:param name="userId"/>
		<!--list标签循环输出每条记录-->
		<pg:list>
			<tr>
				<td  height='20' align="left" class="tablecells">
					<%--<pg:cell colName="orgid" defaultValue="" /> <pg:cell colName="remark5" defaultValue="" />
					 --%>
					 <pg:cell colName="remark5" defaultValue="" />
				</td>
				<td  height='20' align="left" class="tablecells">
					<%--<pg:cell colName="jobid" defaultValue="" /> <pg:cell colName="jobname" defaultValue="" />
					--%>
					 <pg:cell colName="jobname" defaultValue="" />
				</td>
				<td  height='20' align="left" class="tablecells">
					<pg:cell colName="JOB_STARTTIME" defaultValue=""  dateformat="yyyy-MM-dd HH:mm:ss" />
				</td>
				<td  height='20' align="left" class="tablecells">
					<pg:cell colName="JOB_QUASHTIME"  defaultValue=""  dateformat="yyyy-MM-dd HH:mm:ss" />
				</td> 
			</tr>
		</pg:list>
		</table>
		<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div>
	    </pg:notequal>
		</pg:pager>
		</div>


</body>
</html>
