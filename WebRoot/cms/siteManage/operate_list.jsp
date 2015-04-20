<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.*"%>
<%@ page import="com.frameworkset.platform.config.model.Operation"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
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
	<title>操作列表</title>
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
					<%
					if(restypeName!=null)
					{
					%>
					<div align="left">&nbsp;<b>
					<%=restypeName%></b>&nbsp;>>&nbsp;
					<a href="javascript:void(0)" onclick="newRes()"><u>新增</u></a> |
					<a href="javascript:void(0)" onclick="javascript:dealRecord(1); return false;"><u>删除</u></a></div>
					<%}%>
					<hr size="1">
					<div align="left">&nbsp;</div>
					</td>
 				</tr>
 			<pg:listdata dataInfo="operateList" keyName="operateList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="10" scope="request" data="operateList" isList="false">
					<tr class="cms_report_tr">
						<!--设置分页表头-->
						<td class="headercolor" height='30' width="4%">
							<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','checkBoxOne')">
						</td>
						<input class="text" type="hidden" name="selectId">
						<td class="headercolor" height='30' width="18%">资源类型</td>
						<td class="headercolor" height='30' width="18%">资源名称</td>
						<td class="headercolor" height='30' width="30%">描述信息</td>
						<td class="headercolor" height='30' width="10%">授予用户</td>
						<td class="headercolor" height='30' width="10%">授予角色</td>
						<td class="headercolor" height='30' width="10%">授予组织</td>
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
								<input type="checkBox" name="checkBoxOne" onClick="checkOne('checkBoxAll','checkBoxOne')" value="<pg:cell colName="resId" defaultValue=""/>" width="10">
							</td>
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
							<td onclick="openWin('user_iframe.jsp?resId2=<pg:cell colName="title" defaultValue=""/>&resTypeId2=<%=restype%>&resTypeName=<pg:cell colName="restypeName" defaultValue=""/>&title=<pg:cell colName="title" defaultValue=""/>',screen.availWidth-100,screen.availHeight-50)">
							<IMG src="new_doc.gif" border="0"  style="cursor:hand">
							</td>
							<td onclick="openWin('role_iframe.jsp?resId2=<pg:cell colName="title" defaultValue=""/>&resTypeId2=<%=restype%>&resTypeName=<pg:cell colName="restypeName" defaultValue=""/>&title=<pg:cell colName="title" defaultValue=""/>',screen.availWidth-100,screen.availHeight-50)">
							<IMG src="new_doc.gif" border="0"  style="cursor:hand">
							</td>
							<td onclick="openWin('org_iframe.jsp?resId2=<pg:cell colName="title" defaultValue=""/>&resTypeId2=<%=restype%>&resTypeName=<pg:cell colName="restypeName" defaultValue=""/>&title=<pg:cell colName="title" defaultValue=""/>',screen.availWidth-100,screen.availHeight-50)">
							<IMG src="new_doc.gif" border="0"  style="cursor:hand">
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
