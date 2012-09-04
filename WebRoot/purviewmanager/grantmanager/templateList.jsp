
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	
	String siteId = request.getParameter("siteId");
	String currRoleId = request.getParameter("currRoleId");
	String currOrgId = request.getParameter("currOrgId");
	String role_type = request.getParameter("role_type");
	String resTypeId = request.getParameter("resTypeId");

	
%>
<html>
	<head>
		<title>属性容器</title>
<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/tree.css">
		<script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>
		<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">	
var jsAccessControl = new JSAccessControl("#DAE0E9","#F6F8FB","#F6F8FB");

//模态窗口
function openWin(url,swidth,sheight){
	var w = showModalDialog(url,window,"dialogWidth:"+swidth+"px;dialogHeight:"+sheight+"px;help:no;scroll:auto;status:no");
	return w;
}

</SCRIPT>		
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><body>
			
				<form name="ResSearchList" method="post" >
					 <table width=100% border="0" cellpadding="0" cellspacing="1" class="thin">
         				<tr valign='top'>
           					<td height='30'valign='middle' colspan="4"><img src='../images/edit.gif' width="16" height="16" border=0 align='absmiddle'>&nbsp;<strong>模板列表</strong></td>
         				</tr>
         			<pg:listdata dataInfo="TemplateofSiteList" keyName="TemplateofSiteList" />
						<!--分页显示开始,分页标签初始化-->
						<pg:pager maxPageItems="15" scope="request" data="TemplateofSiteList" isList="false">
							<tr>
								<!--设置分页表头-->
								<td width="25%" class="headercolor">模板名称</td>
								<td width="25%" class="headercolor">模板类型</td>
								<td width="35%" class="headercolor">模板风格</td>
								<td width="15%" class="headercolor">权限授予</td>
							</tr>
							
							<pg:param name="siteId"/>
							<pg:param name="currRoleId"/>
							<pg:param name="currOrgId"/>
							<pg:param name="role_type"/>
							<pg:param name="resTypeId"/>
							
							
							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
										暂时没有资源项
									</td>
								</tr>
							</pg:notify>

							<!--list标签循环输出每条记录-->
							<pg:list>
								<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'"
								>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="name" defaultValue="" />
									</td>
									
									
									<td class="tablecells" nowrap="nowrap">
										<pg:equal colName="type" value="0">首页模版</pg:equal>
										<pg:equal colName="type" value="1">概览</pg:equal>
										<pg:equal colName="type" value="2">细览</pg:equal>				
									</td>
									<td class="tablecells" nowrap="nowrap">
										<pg:cell colName="styleName" defaultValue=""/> 
									</td>
									<td class="tablecells" nowrap="nowrap" 
										onclick="openWin('operList_site_ajax.jsp?resTypeId=<%=resTypeId%>&currRoleId=<%=currRoleId%>&role_type=<%=role_type%>&resName=<pg:cell colName="name" defaultValue=""/>&resId=<pg:cell colName="templateId" defaultValue=""/>',600,500)">
										<img src="../images/new_doc.gif"/>
									</td>
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=4 align='center'>
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

