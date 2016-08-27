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
<%@ page import="com.frameworkset.platform.config.model.Operation,
				com.frameworkset.platform.config.ConfigManager"%>

<%
AccessControl accesscontroler = AccessControl.getInstance();
accesscontroler.checkAccess(request, response);

String userId = (String)request.getParameter("userId");
String orgId = (String)request.getParameter("orgId");
String typeName = (String)request.getParameter("typeName");

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
 <title>用户机构权限资源查询</title>
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">

function sub(){
	var auto = "";
	if(document.all("auto")){
		auto = document.all("auto").value;
	}
	userreslist.action="userres_querylist.jsp?restypeId="+document.all("resourcetype").value+"&resId="+
		document.all("resId").value+"&opId="+document.all("operategroup").value+
		"&resName="+document.all("resName").value+"&typeName=<%=typeName%>&auto="+auto;
	userreslist.submit();
}

function getOperateType(id){
		getopergroup.location.href = "../user/resChange.jsp?restypeId="+id;
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
	<form name = "userreslist" method="post" target="forDocList">
	<input type="hidden" name="userId" value="<%=userId%>">
	<input type="hidden" name="orgId" value="<%=orgId%>">

		<table width="100%" border="0"   cellpadding="0" cellspacing="0" class="thin">
			<tr>
				<td height='20'>
					资源标识：<input type="text" name="resId" size="15"/>
					资源名称：<input type="text" name="resName" size="15"/>
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
					<% 
					if(ConfigManager.getInstance().getConfigBooleanValue("enablerresmanaer", false))
					{
					%>
					<select class="select" name="auto" id="auto">
				      	<option value="">--资源类别--</option>
						<option value="0">系统资源</option>
						<option value="1">自定义资源</option>
					</select>
					<% 
					}
					%>
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
