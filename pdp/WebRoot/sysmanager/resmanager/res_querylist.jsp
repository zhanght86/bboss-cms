<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
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
function getSpecial(){
	var special = document.all.special.value;
	window.location.href = "res_querylist.jsp?special="+special;
}
</SCRIPT>
<%@ include file="/include/css.jsp"%>
<link href="../css/contentpage.css" rel="stylesheet" type="text/css">
<link href="../css/tab.winclassic.css" rel="stylesheet" type="text/css">
<body class="contentbodymargin">
<div id="contentborder" align="center">
	<form>
		<table width="100%" border="0"  cellpadding="0" cellspacing="1" class="thin">	
			 <pg:listdata dataInfo="BrowseResList" keyName="BrowseResList"/> 
			<!--分页显示开始,分页标签初始化-->
			<pg:pager maxPageItems="20" scope="request" data="BrowseResList" isList="false">
			<%if("unprotected".equals(special)){%>
			<tr class="labeltable_middle_td">
				<td height='20'  colspan="4">系统中未受保护的资源</td>
			</tr>
			<%}else if("exclude".equals(special)){%>
			<tr class="labeltable_middle_td">
				<td height='20'  colspan="4">只有超级管理员才有的资源</td>
			</tr>
			<%}%>
			<tr class="labeltable_middle_td">
				<td height='20'  colspan="4">
					请选择您要查询的资源:
					<select name="special" id="special" onchange="getSpecial()">
						<option value="" selected></option>
						<option value="unprotected">系统中未受保护的资源</option>
						<option value="exclude">只有超级管理员才有的资源</option>
					</select>
				</td>
			</tr>
			
			<tr class="labeltable_middle_td">
			<!--设置分页表头-->
				<td height='20' class="headercolor" nowrap>资源类型</td>
				<td height='20' class="headercolor" nowrap>资源标识</td>
				<td height='20' class="headercolor" nowrap>资源名称</td>
				<td height='20' class="headercolor" nowrap>操作类型</td>
			</tr>
			<pg:param name="special"/>

			<!--检测当前页面是否有记录-->
			<pg:notify>
			<tr height="18px" >
				<td class="detailcontent" colspan=100 align='center'>暂时没有资源</td>
			</tr>
			</pg:notify>			      
			<!--list标签循环输出每条记录-->			      
			<pg:list>	

			<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">	      	
				<td class="tablecells" align=left  nowrap><pg:cell colName="resTypeName" defaultValue=""/></td>							
				<td class="tablecells" align=left nowrap><pg:cell colName="resId" defaultValue=""/></td>
				<td class="tablecells" align=left nowrap><pg:cell colName="resName" defaultValue=""/></td>
				<td class="tablecells" align=left><pg:cell colName="opName" defaultValue=""/></td>
			</tr>
			</pg:list>
			<tr height="18px" >
				<td class="detailcontent" colspan=5 align='center'><pg:index/><input type="hidden" name="queryString" value="<pg:querystring/>"></td>
			</tr> 
			</pg:pager>
		</table>	
	</form>	
</div>
</body>

</html>
