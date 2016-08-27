<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>

<%@ page import="com.frameworkset.platform.security.AccessControl
				,com.frameworkset.platform.config.ConfigManager"%>
<%

AccessControl accessControl = AccessControl.getInstance();
accessControl.checkAccess(pageContext);


String orgid = request.getParameter("selected");
String resName = request.getParameter("resName");


if(orgid==null)orgid="";

//当前用户是否拥有超级管理员权限与部门管理员权限
boolean isAdminOrOrgManager = false;
//是否是管理员
boolean isOrgManager = new com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl().isOrgManager(accessControl.getUserID());
boolean isAdmin = accessControl.isAdmin();
if(isAdmin || isOrgManager){
	isAdminOrOrgManager = true;
}


%>
<html>
<head>
<title>操作容器工具栏</title>
<link rel="stylesheet" type="text/css" href="../css/toolbar.css">


<script language="javascript" src="../scripts/toolbar.js"></script>
<script language="javascript">
var orgid="<%=orgid%>";
var resName ="<%=resName%>";
	if(orgid!="")
		getPropertiesContent().location.href="<%=rootpath%>/orgmanager/org.do?method=getOrgInfo&orgId="+orgid;
	if(orgid=="0")
		getPropertiesContent().location.href="<%=rootpath%>/sysmanager/orgmanager/new_org.jsp";

	//else
	//getPropertiesContent().location.href="<%=rootpath%>/sysmanager/orgmanager/organsearch.jsp";

	//if(resName==null || resName=="null"){
	//	getPropertiesContent().location.href="<%=rootpath%>/sysmanager/orgmanager/new_org.jsp";
	//}
	    


	function getElementByName(eName){
		var elements = document.body.all;
		for(i = 0;i< elements.length;i++){
           if (elements[i].name==eName) return elements[i];
		}
		return null;
	}
	function selectTool(toolName) {
	    var tool=getElementByName(toolName);
		if (tool) {
		    tool.onclick();
		}
	}

	function bussinessAction(action){
	
		if(orgid==""){
			alert("请在机构树中选择您要操作的机构!");
			//getPropertiesContent().location.href="../orgmanager/noorg.jsp";
			
			return;       
		}
		
		if(orgid!=""){
	    if(action == 1 && getPropertiesContent()){
			getPropertiesContent().location.href="<%=rootpath%>/orgmanager/org.do?method=getOrgInfo&orgId="+orgid+"&resName="+resName;
	    }
	    else if(action == 2&& getPropertiesContent()){
			getPropertiesContent().location.href="<%=rootpath%>/orgmanager/orgoperjob.do?method=showOrgallJob&orgId="+orgid;
	    }

	    else if(action == 3&& getPropertiesContent()){
	       getPropertiesContent().location.href="<%=rootpath%>/sysmanager/orgmanager/orgsubuserlist.jsp?orgId="+orgid;
	    }
		else if(action == 4&& getPropertiesContent()){
	        getPropertiesContent().location.href="<%=rootpath%>/sysmanager/orgmanager/suborglist.jsp?orgId="+orgid;
	    }
	    else if(action == 5&& getPropertiesContent()){
	        getPropertiesContent().location.href="<%=rootpath%>/createMail.do?method=getUserList&orgId="+orgid;
	    }
	    else if(action == 6&& getPropertiesContent()){
	        getPropertiesContent().location.href="<%=rootpath%>/orgmanager/orgrole.do?method=getRoleList&orgId="+orgid;
	    }
	     else if(action == 7&& getPropertiesContent()){
	        getPropertiesContent().location.href="<%=rootpath%>/sysmanager/orgmanager/change2job.jsp?orgId="+orgid;
	    }
	    else if(action == 8){
			window.showModalDialog("<%=rootpath%>/sysmanager/orgmanager/orgres_queryframe.jsp?orgId="+
				orgid,"","dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
	    }
	 }


	}
</script>
</head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_WORKSPACE%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">
<table width="100%" cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25><img fullscreenrecover="还原" fullscreen="全屏" title="全屏"
			class="normal" onclick="dofullscreen(<%=Framework.SWITCH_WORKSPACE%>,document.all.doubleclickcolumn);" src="../images/actions.gif"></td>
		<td valign="middle"
			align="left" nowrap class="text">业务操作</td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle"
			align="right" width="*" nowrap class="text">
		<%if(isAdminOrOrgManager){ %>
		<table cellpadding=0 cellspacing=0 border=0>
		<tr>
			<%
			 if (accessControl.checkPermission(orgid,
                       AccessControl.WRITE_PERMISSION, AccessControl.ORGUNIT_RESOURCE))
            {%>
			  <td class=normal name="tool1" id="tools1" title="机构基本信息" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(1)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="机构基本信息" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;机构基本信息&nbsp;</td></tr></table></td>
			<%}%>
<!----------------------------------------------------------------------------------------------------->

			<%
			 if (accessControl.checkPermission(orgid,
                        "orgjobset", AccessControl.ORGUNIT_RESOURCE)
                        && ConfigManager.getInstance().getConfigBooleanValue("enableorgrole", true))
            {%>
			<td class=normal name="tool2" id="tools2" title="机构资源列表" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(8)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="机构资源列表" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;机构资源列表&nbsp;</td></tr></table></td>
			<%}%>
<!----------------------------------------------------------------------------------------------------->
			<%
			 if (accessControl.checkPermission(orgid,
                        "orgjobset", AccessControl.ORGUNIT_RESOURCE))
            {%>
			<td class=normal name="tool2" id="tools2" title="岗位设置" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(2)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="岗位设置" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;岗位设置&nbsp;</td></tr></table></td>
			<%}%>
<!----------------------------------------------------------------------------------------------------->


			<%
			 if (accessControl.checkPermission(orgid,
                        "directorgs", AccessControl.ORGUNIT_RESOURCE))
            {%>
			<td class=normal name="tool4" id="tools3" title="直属子机构" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(4)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="直属子机构" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;直属子机构&nbsp;</td></tr></table></td>
			<%}%>
<!----------------------------------------------------------------------------------------------------->				

			<%
			 if (accessControl.checkPermission(orgid,
                        "orgroleset", AccessControl.ORGUNIT_RESOURCE)
                        && ConfigManager.getInstance().getConfigBooleanValue("enableorgrole", true))
            {%>
			<td class=normal name="tool5" id="tools5" title="角色授予" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(6)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="角色授予" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;角色授予&nbsp;</td></tr></table></td>
			<%}%>				

			
			</tr>
		</table>
		<%} %>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>

