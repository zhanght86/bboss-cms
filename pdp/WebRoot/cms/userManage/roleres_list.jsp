<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	String reFlush = "false";
	if (request.getAttribute("reFlush") != null) {
		reFlush = "true";
	}
	String restype=(String)request.getParameter("restypeId");	
	String restypeName=request.getParameter("restypeName");
	String parent_resId = request.getParameter("parent_resId");
%>
<html>
	<head>
		<title>资源列表</title>
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">		
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script src="../inc/js/func.js"></script>
<script language="JavaScript" src="<%=rootpath%>/sysmanager/resmanager/common.js" type="text/javascript"></script>
<script language="JavaScript" src="<%=rootpath%>/sysmanager/include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">	

</SCRIPT>		
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			<body>
			
				<form name="ResSearchList" method="post" >
				<input type="hidden" name="resTypeName" value="<%=restypeName%>"/>
				<input type="hidden" name="restypeId" value="<%=restype%>"/>
					 <table width=100% border="0" cellpadding="0" cellspacing="1" bordercolor="#B7CBE4"  class="Datalisttable">
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="7">
							<br>
							<div align="left">&nbsp;<b>
							资源列表</b>
							</td>
         				</tr>
         			<pg:listdata dataInfo="ResSearchList" keyName="ResSearchList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="10" scope="request" data="ResSearchList" isList="false">
							<tr class="cms_report_tr">
								<!--设置分页表头-->
								<input class="text" type="hidden" name="selectId">
								<td class="headercolor" height='30' width="25%">资源类型</td>
								<td class="headercolor" height='30' width="25%">资源名称</td>
								<td class="headercolor" height='30' width="35%">描述信息</td>
								<td class="headercolor" height='30' width="15%">权限授予</td>
							</tr>
							<pg:param name="restypeId" />
							<pg:param name="title" />
							<pg:param name="path" />

							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center' height='30'>
										暂时没有资源项
									</td>
								</tr>
							</pg:notify>

							<!--list标签循环输出每条记录-->
							<pg:list>
								<tr class="cms_data_tr" onMouseOver="high(this)" onMouseOut="unhigh(this)">
									<td class="tablecells" nowrap="nowrap" height='30'>
										<pg:cell colName="restypeName" defaultValue="" />
									</td>
									
									
									<td class="tablecells" nowrap="nowrap" height='30' style="cursor:hand" onclick="getRes('<pg:cell colName='resId' defaultValue=''/>','<pg:cell colName='restypeId' defaultValue=''/>')">
									<input type="hidden" name="title" value="<pg:cell colName="title" defaultValue="" />"/>
										<pg:cell colName="title" defaultValue="" />
									</td>
									<td class="tablecells" nowrap="nowrap" height='30'>
										<pg:cell colName="path" defaultValue="暂时为空" />
									</td>
									<td onclick="openWin('roleoperList_res.jsp?resId2=<pg:cell colName="title" defaultValue="" />&resTypeId2=<%=restype%>&resTypeName=<pg:cell colName="title" defaultValue="" />&title=<pg:cell colName="title" defaultValue="" />',600,500)">
									<IMG src="../images/new_doc.gif" border="0"  style="cursor:hand">
									</td>
								</tr>
							</pg:list>
							<tr height="18px">
								<td colspan=7 align='center' class="Data_List_Table_Bottom">
									 <pg:index />						
								</td>
						</tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>

				  </table>
				</form>
		
</body>
<center>
</html>
