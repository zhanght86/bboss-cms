<%
/*
 * <p>Title: 在职人员列表</p>
 * <p>Description: 在职人员列表</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-26
 * @author baowen.liu
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
    
%>
<html>
	<head>	
		
<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/treeview.css">
		<link href="../../html/stylesheet/common.css" rel="stylesheet" type="text/css" />
	
	</head>	   
	
	<body>
  		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
  		<pg:listdata dataInfo="JobSeeList" keyName="JobSeeList" />
						<!-- 分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="13" id="JobSeeList" scope="request" data="JobSeeList" isList="false">
						<pg:equal actual="${JobSeeList.itemCount}" value="0" >
						<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
					</pg:equal> 
					<pg:notequal actual="${JobSeeList.itemCount}"  value="0">
						<pg:header>
							<th><pg:message code="sany.pdp.personcenter.person.realname"/></th>
							<th><pg:message code="sany.pdp.personcenter.person.loginname"/></th>
							<th><pg:message code="sany.pdp.job"/></th>
							<th><pg:message code="sany.pdp.workflow.organization"/></th>
							<th><pg:message code="sany.pdp.jobmanage.job.time"/></th>
							<th><pg:message code="sany.pdp.jobmange.job.statu"/></th>
						</pg:header>
							<pg:param name="orgname" />
							<pg:param name="userName" />
							<pg:param name="jobname" />
							<pg:param name="jobId" />

						<!--list标签循环输出每条记录-->
							<pg:list>								
								<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" onclick="" >
									
									<td class="tablecells" nowrap="nowrap" height='20'>
										<pg:cell colName="userRealname" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap" height='20'>
										<pg:cell colName="userName" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="jobName" defaultValue="" />
									</td>									
									<td class="tablecells" nowrap="nowrap" >
										<pg:cell colName="orgName" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="jobStartTime"  dateformat="yyyy-MM-dd HH:mm:ss"/>
									</td>
									
									<td class="tablecells" nowrap="nowrap">
										
										<pg:equal colName="fettle" value="1"><pg:message code="sany.pdp.jobmanage.on.job"/></pg:equal>
										<pg:equal colName="fettle" value="0"><pg:message code="sany.pdp.jobmanage.out.job"/></pg:equal>		
										<pg:equal colName="fettle" value=""><pg:message code="sany.pdp.jobmanage.unknown"/></pg:equal>
					  					
									</td>	
								</tr>
							</pg:list>

		  </table>
		  <div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
		  </pg:notequal>
						</pg:pager>
   </body>
   
<html>
