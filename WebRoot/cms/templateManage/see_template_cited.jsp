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
	
	TemplateManager tlpm = new TemplateManagerImpl();
	
	String siteId = request.getParameter("siteId");
	String templateType = request.getParameter("templateType");
	String templateId = request.getParameter("templateId");
	String templateTypeDes = "概览模版";
	if("0".equals(templateType))
		templateTypeDes = "首页模版";
	else if("2".equals(templateType))
		templateTypeDes = "细览模版";
	else if("3".equals(templateType))
		templateTypeDes = "评论模版";
	int count = tlpm.getTplCtiedCount(templateId,0);
	String indexPagePath = request.getParameter("indexPagePath");
	if(indexPagePath!=null && indexPagePath.length()>0){
		count = tlpm.getIndexPageCtiedCount(indexPagePath,siteId);
	}
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
<title>::模板引用情况查看:::</title>
</head>
<base target="_self">
<body background="../images/grayline_bg.jpg">
<form action="doc_extfield_add_do.jsp" method="post"  name="extfieldForm" target="extfieldexeman" id="extfieldForm">	
	<table width="98%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" align="center">
		<tr >
			<td width="4%" height="20" align="right">
				<img src="../images/ico_point.gif" width="25" height="25">
			</td>
			<td height="20" >
				引用总次数为：<%=count%>次
			</td>
		</tr>
		<tr>
			<td colspan="2" style="height:9px; background-color:#3266B1"></td>
		</tr>
		<tr>
			<td colspan="2">
			<%
			if(templateType!=null && "2".equals(templateType)){%>
			<tab:tabContainer id="foo-extfield-add" selectedTabPaneId="foo-extfield">
				<tab:tabPane id="foo-extfield" tabTitle="引用文档">
				<table width="100%" border="0" cellspacing="1" cellpadding="4">
				<pg:listdata dataInfo="DocumentofTlpCitedList" keyName="DocumentofTlpCitedList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="8" scope="request" data="DocumentofTlpCitedList" isList="false">
				
				<pg:param name="templateId" />
				<pg:param name="templateType" />
				<pg:header>	
				<tr class="cms_report_tr">	
					<td width="14%" align="center">模板类型</td>		
					<td width="30%" align="center">文档名</td>
					<td width="26%" align="center">所属频道</td>
					<td width="30%" align="center">文档创建时间</td>
					
				</tr>
				</pg:header>
				<!--检测当前页面是否有记录-->
				<pg:notify>
					<tr class="labeltable_middle_tr_01" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td colspan=100 align='center'>暂时没有模板引用文档</td>
					</tr>
				</pg:notify>
				<!--list标签循环输出每条记录-->
				
				<pg:list>
				<tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td class="tablecells" align="center"><%=templateTypeDes%></td>
						<td class="tablecells" align="center">
							<strong><pg:cell colName="subtitle" defaultValue="" /></strong>
						</td>
						<td class="tablecells" align="center">
							<pg:cell colName="channelName" defaultValue="" />
						</td>
						<td class="tablecells" align="center">		
							<pg:cell colName="createTime" dateformat="yyyy-MM-dd HH:mm"  defaultValue="" />
						</td>
				</tr>
				</pg:list>
				<tr  class="labeltable_middle_tr_01">
						<td colspan=9 align='center'><div class="Data_List_Table_Bottom">共<pg:rowcount />个模板引用文档&nbsp;<pg:index /></div></td>
				</tr>
					<input name="queryString" value="<pg:querystring/>" type="hidden">
				</pg:pager>
				</table>
				</tab:tabPane>
				<tab:tabPane id="bar-extfield" tabTitle="引用频道">
				<table width="100%" border="0" cellspacing="1" cellpadding="4">
				<pg:listdata dataInfo="ChannelofTlpCitedList" keyName="ChannelofTlpCitedList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="8" scope="request" data="ChannelofTlpCitedList" isList="false">
				
				<pg:param name="templateId" />
				<pg:param name="templateType" />
				<pg:header>	
				<tr class="cms_report_tr">	
					<td width="14%" align="center">模板类型</td>		
					<td width="26%" align="center">频道名</td>
					<td width="28%" align="center">所属站点</td>
					<td width="32%" align="center">频道创建时间</td>
					
				</tr>
				</pg:header>
				<!--检测当前页面是否有记录-->
				<pg:notify>
					<tr class="labeltable_middle_tr_01" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td colspan=100 align='center'>暂时没有模板引用频道</td>
					</tr>
				</pg:notify>
				<!--list标签循环输出每条记录-->
				
				<pg:list>
				<tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td class="tablecells" align="center"><%=templateTypeDes%></td>
						<td class="tablecells" align="center">
							<strong><pg:cell colName="displayName" defaultValue="" /></strong>
						</td>
						<td class="tablecells" align="center">
							<pg:cell colName="siteName" defaultValue="" />
						</td>
						<td class="tablecells" align="center">		
							<pg:cell colName="createTime" dateformat="yyyy-MM-dd HH:mm"  defaultValue="" />
						</td>
				</tr>
				</pg:list>
				<tr  class="labeltable_middle_tr_01">
						<td colspan=9 align='center'><div class="Data_List_Table_Bottom">共<pg:rowcount />个模板引用频道&nbsp;<pg:index /></div></td>
				</tr>
					<input name="queryString" value="<pg:querystring/>" type="hidden">
				</pg:pager>
				</table>
				</tab:tabPane>
				</tab:tabContainer>
				<%}else{%>
				<table width="100%" border="0" cellspacing="1" cellpadding="4">
				<pg:listdata dataInfo="ChannelofTlpCitedList" keyName="ChannelofTlpCitedList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="8" scope="request" data="ChannelofTlpCitedList" isList="false">
				
				<pg:param name="templateId" />
				<pg:param name="indexPagePath" />
				<pg:param name="siteId" />
				<pg:header>	
				<tr class="cms_report_tr">	
					<td width="14%" align="center">模板类型</td>		
					<td width="26%" align="center">频道名</td>
					<td width="28%" align="center">所属站点</td>
					<td width="32%" align="center">频道创建时间</td>
					
				</tr>
				</pg:header>
				<!--检测当前页面是否有记录-->
				<pg:notify>
					<tr class="labeltable_middle_tr_01" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td colspan=100 align='center'>暂时没有模板引用频道</td>
					</tr>
				</pg:notify>
				<!--list标签循环输出每条记录-->
				
				<pg:list>
				<tr class="cms_data_tr" onMouseOver="this.style.background='#E9EDF3'" onMouseOut="this.style.background='#FFFFFF'">
						<td class="tablecells" align="center"><%=templateTypeDes%></td>
						<td class="tablecells" align="center">
							<strong><pg:cell colName="displayName" defaultValue="" /></strong>
						</td>
						<td class="tablecells" align="center">
							<pg:cell colName="siteName" defaultValue="" />
						</td>
						<td class="tablecells" align="center">		
							<pg:cell colName="createTime" dateformat="yyyy-MM-dd HH:mm"  defaultValue="" />
						</td>
				</tr>
				</pg:list>
				<tr  class="labeltable_middle_tr_01">
						<td colspan=9 align='center'><div class="Data_List_Table_Bottom">共<pg:rowcount />个模板引用频道&nbsp;<pg:index /></div></td>
				</tr>
					<input name="queryString" value="<pg:querystring/>" type="hidden">
				</pg:pager>
				</table>
				<%}%>
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