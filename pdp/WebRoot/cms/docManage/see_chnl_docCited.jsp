<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.customform.*"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	
	DocumentManager dm = new DocumentManagerImpl();
	
	String docId = request.getParameter("docId");
	String docName = request.getParameter("docName");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<tab:tabConfig/>
<script language="javascript" src="../inc/js/selectTime.js"></script>
<SCRIPT LANGUAGE="JavaScript">
	//
	
</SCRIPT>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>::文档被引用情况查看:::</title>
</head>
<base target="_self">
<body background="../images/grayline_bg.jpg">
<form action="doc_extfield_add_do.jsp" method="post"  name="extfieldForm" target="extfieldexeman" id="extfieldForm">	
	<table width="98%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">
		
		<tr>
			<td colspan="2" style="height:9px; background-color:#3266B1"></td>
		</tr>
		<tr>
			<td colspan="2">
				<table width="100%" border="0" cellspacing="1" cellpadding="4">
				<pg:listdata dataInfo="ChnlOfDocCitedList" keyName="ChnlOfDocCitedList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="9" scope="request" data="ChnlOfDocCitedList" isList="false">
				
				<pg:param name="docId" />
				<pg:header>	
				<tr class="cms_report_tr">	
					<td width="36%" align="center">文档名</td>		
					<td width="18%" align="center">引用频道</td>
					<td width="10%" align="center">引用人</td>
					<td width="18%" align="center">频道所属站点</td>
					<td width="18%" align="center">引用时间</td>
					
				</tr>
				</pg:header>
				<!--检测当前页面是否有记录-->
				<pg:notify>
					<tr class="labeltable_middle_tr_01" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td colspan=100 align='center'>文档"<%=docName%>"尚未被任何频道引用</td>
					</tr>
				</pg:notify>
				<!--list标签循环输出每条记录-->
				
				<pg:list>
				<tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td class="tablecells" align="center"><%=docName%></td>
						<td class="tablecells" align="center">
							<strong><pg:cell colName="curChannelName" defaultValue="" /></strong>
						</td>
						<td class="tablecells" align="center">
							<pg:cell colName="citeUserName" defaultValue="" />
						</td>
						<td class="tablecells" align="center">
							<pg:cell colName="curSiteName" defaultValue="" />
						</td>
						<td class="tablecells" align="center">		
							<pg:cell colName="citeTime" dateformat="yyyy-MM-dd HH:mm"  defaultValue="" />
						</td>
				</tr>
				</pg:list>
				<tr  class="labeltable_middle_tr_01">
						<td colspan=9 align='center'><div class="Data_List_Table_Bottom">共<pg:rowcount />个模板引用频道&nbsp;<pg:index /></div></td>
				</tr>
					<input name="queryString" value="<pg:querystring/>" type="hidden">
				</pg:pager>
				</table>
			</td>
		</tr>
	</table>
	<br>
	<div style="text-align:center" background="../images/grayline_bg.jpg">
		<INPUT name="button" type="button" class="cms_button" onClick="window.close()"  value="返回">
	</div>
</form>
<iframe name="extfieldexeman" width="0" height="0" style="display:none"></iframe>

</body>
</html>