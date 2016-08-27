<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role" %>
<%
AccessControl accessControl = AccessControl.getInstance();
accessControl.checkAccess(pageContext);
String currRoleId = request.getParameter("roleId");

String roleTabId = (String)session.getAttribute("roleTabId");

String roleName ="";

//点击了用户，置session
if(currRoleId != null){
	session.setAttribute("currRoleId",currRoleId);
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById(currRoleId);
	roleName = role.getRoleName();
}
if(currRoleId == null){
	session.setAttribute("currRoleId",null);
	currRoleId = "";
}
%>
<html>
<head>
<title>操作容器工具栏</title>
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/toolbar.css">


<script language="javascript" src="../../sysmanager/scripts/toolbar.js"></script>
<script language="javascript">
var rid = "<%=currRoleId%>";
var tabId = "<%=roleTabId%>";
if(rid !="" && tabId !=""){
	if(tabId == 2){	
	bussinessAction(2);

	}
	else if(tabId == 3){	
		bussinessAction(3);
	
	}
	else if(tabId == 1){	
		getPropertiesContent().location.href="../memberManager/roleManager.do?flag=1&method=getRole&roleId="+rid;
	
	}
}else{
	getPropertiesContent().location.href="../sitemember/newRole.jsp";
}

		
	

	function bussinessAction(action){
		
	
		    if(action == 1&&getPropertiesContent()){
		    if(rid =="" || rid ==null){
	    		alert("请在左边的角色树上选择一个角色在进行操作！");
				return;
				
	    	}
	    	else	
		       getPropertiesContent().location.href="../memberManager/roleManager.do?method=getRole&flag=1&roleId="+rid;
		    } else if(action == 2 && getPropertiesContent()){
	    	if(rid =="" || rid ==null){
	    		alert("请在左边的角色树上选择一个角色在进行操作！");
				return;
				
	    	}
	    	else						
				
				 getPropertiesContent().location.href="../memberManager/roleManager.do?method=RoleMember&roleId="+rid;
	    	}else if(action == 3 && getPropertiesContent()){
	    	if(rid =="" || rid ==null){
	    		alert("请在左边的角色树上选择一个角色在进行操作！");
				return;
				
	    	}
	    	else						
			getPropertiesContent().location.href="../sitemember/role2chnl.jsp?roleId=<%=currRoleId%>";
	    	}
		  	
	  }




	
</script>
</head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_WORKSPACE%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">

<table width="100%"  cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25 ><img
			class="normal" src="../../sysmanager/images/actions.gif"></td>
		<td valign="middle"
			align="left"  nowrap class="text">当前角色:<%=roleName%></td>
		<td id="doubleclickcolumn" title="双击以最大化" valign="middle"
			align="right" width="*"  nowrap class="text">
		<table  cellpadding=0 cellspacing=0 border=0>
		<tr>
			<td class=normal name="tool2" id="tools2" title="角色基本信息" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction (1)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../../sysmanager/images/edit.gif" alt="角色基本信息" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;角色基本信息&nbsp;</td></tr></table></td>
          

<!-------------------------------------------------------------------------------------------------------------			-->
					
			<td class=normal name="tool2" id="tools2" title="角色授予用户" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction (2)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../../sysmanager/images/edit.gif" alt="角色授予用户" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;角色授予会员&nbsp;</td></tr></table></td>
          

<!-------------------------------------------------------------------------------------------------------------			-->
         
			<td class=normal name="tool3" id="tools3" title="频道资源授予" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction (3)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../../sysmanager/images/edit.gif" alt="频道资源授予" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;频道资源授予&nbsp;</td></tr></table></td>
		           		
			
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>
