<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.frameworkset.platform.config.model.ResourceInfo"%>
<%@ page import="com.frameworkset.platform.config.ResourceInfoQueue"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ResManager"%>
<%@ page import="com.frameworkset.platform.config.model.Operation"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);

	String roleId = request.getParameter("roleId");

//查询资源列表
	List resQueue = null;
	ResourceManager resManager = new ResourceManager();
	resQueue = resManager.getResourceInfos();
	 for(int i = 0; i < resQueue.size(); i ++)
	        {
	            ResourceInfo res = (ResourceInfo)resQueue.get(i);
	            if(!res.isUsed())
	                continue;
	            String listId = res.getId();
	            String listName = res.getName();
			 }
	request.setAttribute("resList",resQueue);


%>
<html>
<head>    
 <title>角色权限资源查询</title>
<script language="JavaScript" src="../../../sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">

function sub(){
	var resIdVal = document.all("resId").value;
	var resName = document.all("resName").value;
	var resourcetypeVal = document.all("resourcetype").value;
	var operategroupVal = document.all("operategroup").value;
	rolereslist.action="roleres_querylist.jsp?roleId=<%=roleId%>&resId="+resIdVal+"&resdescribe="+resName+"&operatetype="+operategroupVal+"&restypeId="+resourcetypeVal;
	rolereslist.submit();
}

function getOperateType(id){
	getopergroup.location.href = "../../user/resChange.jsp?restypeId="+id;
}
</SCRIPT>
<%@ include file="/include/css.jsp"%>
<style>

body{
    SCROLLBAR-HIGHLIGHT-COLOR: #ffffff;
	SCROLLBAR-SHADOW-COLOR: #ffffff;
	SCROLLBAR-3DLIGHT-COLOR: #ffffff;
	SCROLLBAR-ARROW-COLOR: #ffffff;
	SCROLLBAR-TRACK-COLOR: #ffffff;
	SCROLLBAR-DARKSHADOW-COLOR: #ffffff;
	SCROLLBAR-FACE-COLOR: #ffffff;	
}
</style>
</head>

<body scrolling="no">
<div id="contentborder" valign="top" align="center" >
	<form name = "rolereslist" method="post" target="forDocList">
	<input type="hidden" name="roleId" value="<%=roleId%>">

		<table width="100%" border="0"   cellpadding="0" cellspacing="0" class="thin">
			<tr>
				<td height='20'>
					资源标识：<input type="text" name="resId"  />
					资源名称：<input type="text" name="resName"/>
				    <select class="select" id="resourcetype" name="resourcetype" onchange="getOperateType(this.options[this.selectedIndex].value)">
				      	<option value="">--请选择资源类型--</option>
				      	<pg:list requestKey="resList" needClear="false">
					    	<option value="<pg:cell colName="id"/>">
					        	<pg:cell colName="name"/>
							</option>
						</pg:list>
					</select>
					<select class="select" name="operategroup" id="operategroup">
				      	<option value="">--请选择资源操作--</option>
					</select>
				</td>
				<td>
					<input name="search" type="button" class="input" value="查询" onClick="sub()">
					<input name="reset" type="reset" class="input" value="重设">
				</td>
				</tr>
		</table>	
	</form>	
</div>
<iframe id="getopergroup" src="" border="0" height="0" width="0"></iframe>
</body>

</html>

