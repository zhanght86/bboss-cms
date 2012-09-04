<% 
/*
 * <p>Title: 资源权限显示列表</p>
 * <p>Description: 资源查询显示列表</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-26
 * @author baowen.liu
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.frameworkset.platform.config.model.ResourceInfo,
				 com.frameworkset.platform.sysmgrcore.manager.ResManager,
				 com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				 org.frameworkset.spi.SPIException"%>
<%@ page import="com.frameworkset.platform.config.ResourceInfoQueue"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String jobId = (String)request.getParameter("jobId");
	//String resTypeId = (String)request.getParameter("resTypeId");
	//ResourceManager resourceManager = new ResourceManager();
	
	//ResManager resManager = SecurityDatabase.getResourceManager();
	//List UserResList = new ArrayList();
	//UserResList = resManager.getJobRoleResList(jobId);
	//request.setAttribute("UserResList", UserResList);
	String[] checkValues = request.getParameterValues("permission");
	if(checkValues != null && checkValues.length > 0){
		for(int i = 0; i < checkValues.length; i++){
			String opId = checkValues[i].substring(1, checkValues[i].indexOf("+", 1));
			checkValues[i] = checkValues[i].substring(checkValues[i].indexOf("+", 1));
			String resId = checkValues[i].substring(1, checkValues[i].indexOf("+", 1));
			checkValues[i] = checkValues[i].substring(checkValues[i].indexOf("+", 1));
			String roleId = checkValues[i].substring(1, checkValues[i].indexOf("+", 1));
			checkValues[i] = checkValues[i].substring(checkValues[i].indexOf("+", 1));
			String resTypeId = checkValues[i].substring(1, checkValues[i].indexOf("+", 1));
			checkValues[i] = checkValues[i].substring(checkValues[i].indexOf("+", 1));
			String types = checkValues[i].substring(1);
			try {
				ResManager resManager = SecurityDatabase.getResourceManager();
				resManager.deleteRoleResOp(opId, resId, roleId, resTypeId, types);
			} catch (SPIException e) {
				e.printStackTrace();
			}
		}
	}
%>
<html>

<head>    
 <title>属性容器</title>
<%@ include file="/include/css.jsp"%>
<link href="../css/treeview.css" rel="stylesheet" type="text/css">

<script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../scripts/pager.js" type="text/javascript"></script>
<script>
var isSucceed = "<%=request.getParameter("isSucceed")%>"
if(isSucceed=="1"){
	alert("删除记录成功!");
}
function sub(){
	var isSelect = false;
	var outMsg;
	for(var i = 0; i < queryList.elements.length; i++){
		var e = queryList.elements[i];
		if(e.name == 'permission'){
			if(e.checked){
				isSelect = true;
				break;
			}
		}
	}
	if(isSelect){
		outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
		if (confirm(outMsg)){
			document.queryList.action = "jobres_querylist.jsp?jobId=<%=jobId%>&isSucceed=1";
			document.queryList.submit();
			return true;
		}
	}else{
		alert("至少要选择一条记录！");
	}
}
</script>
</head>

<body class="contentbodymargin">
<div id="contentborder" align="center">
	<form name="queryList" method=post action=""/>
		<table width="100%" border="0"  cellpadding="0" cellspacing="1" class="thin">
			
			
			<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.JobRoleList" keyName="JobRoleList"/>
			<!--分页显示开始,分页标签初始化-->
			<pg:pager maxPageItems="20" scope="request" data="JobRoleList" isList="false">
			<tr class="labeltable_middle_td">
			<!--设置分页表头-->
				<td height='20' class="headercolor"></td>
				<td height='20' class="headercolor">资源类型</td>
				<td height='20' class="headercolor">资源标识</td>
				<td height='20' class="headercolor">资源名称</td>
				<td height='20' class="headercolor">操作类型</td>
				<td height='20' class="headercolor">角色名称</td>
			</tr>
      		<pg:param name="jobId" />
      		<pg:param name="resName" />
      		<pg:param name="resid" />
      		<pg:param name="resTypeId" />
      		<pg:param name="opId" />
  
			<!--检测当前页面是否有记录-->
			<pg:notify>
			<tr height="18px" >
				<td class="detailcontent" colspan=100 align='center'>暂时没有资源</td>
			</tr>
			</pg:notify>			      
			<!--list标签循环输出每条记录-->			      
			<pg:list>	
			<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">
				<%
					if(dataSet.getString("resTypeName") == "未知" || dataSet.getString("opName") == "未知" ){
				%>
				<td class="tablecells" align=left>
				<input type="checkbox" name="permission" value="+<pg:cell colName="opId" defaultValue=""/>+<pg:cell colName="resId" defaultValue=""/>+<pg:cell colName="roleId" defaultValue=""/>+<pg:cell colName="resTypeId" defaultValue=""/>+<pg:cell colName="types" defaultValue=""/>"/>
				</td>
				<td class="tablecells" align=left><font color=red><pg:cell colName="resTypeName" defaultValue=""/>(<pg:cell colName="resTypeId" defaultValue=""/>)</font></td>
				<td class="tablecells" align=left><font color=red><pg:cell colName="resid" defaultValue=""/></font></td>
				<td class="tablecells" align=left><font color=red><pg:cell colName="resName" defaultValue=""/></font></td>
				<td class="tablecells" align=left><font color=red><pg:cell colName="opName" defaultValue=""/>(<pg:cell colName="opId" defaultValue=""/>)</font></td>
				<td class="tablecells" align=left><font color=red><pg:cell colName="roleName" defaultValue=""/></font></td>
				<%	}else{	
				%>
				<td class="tablecells" align=left>
				<input disabled="true" type="checkbox" name="permission" value="+<pg:cell colName="opId" defaultValue=""/>+<pg:cell colName="resId" defaultValue=""/>+<pg:cell colName="roleId" defaultValue=""/>+<pg:cell colName="resTypeId" defaultValue=""/>+<pg:cell colName="types" defaultValue=""/>"/>
				</td>
				<td class="tablecells" align=left><pg:cell colName="resTypeName" defaultValue=""/>(<pg:cell colName="resTypeId" defaultValue=""/>)</td>
				<td class="tablecells" align=left><pg:cell colName="resId" defaultValue=""/></td>
				<td class="tablecells" align=left><pg:cell colName="resName" defaultValue=""/></td>
				<td class="tablecells" align=left><pg:cell colName="opName" defaultValue=""/>(<pg:cell colName="opId" defaultValue=""/>)</td>
				<td class="tablecells" align=left><pg:cell colName="roleName" defaultValue=""/></td>
				<%}%>

				
			</tr>
			</pg:list>
			<tr height="18px" >
				<td class="detailcontent" colspan=5 align='center'><pg:index/><input type="hidden" name="queryString" value="<pg:querystring/>"></td>
				<td class="detailcontent" colspan=1 align='left'><input type="button" class="input" onClick="sub()" value="删除" /></td>
			</tr>   			   	      
			</pg:pager>
		</table>	
	</form>	
</div>

</body>

</html>
