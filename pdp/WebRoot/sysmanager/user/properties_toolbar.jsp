<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%
AccessControl accessControl = AccessControl.getInstance();
accessControl.checkAccess(pageContext);

String orgid =  request.getParameter("orgId");
String userId = request.getParameter("userId");

if(orgid == null){
	orgid = "";
}



//点击了用户，置session
if(userId != null){
	session.setAttribute("currUserId",Integer.valueOf(userId));
}
if(userId == null){
	session.setAttribute("currUserId",null);
	userId = "";
}
%>
<html>
<head>
<title>操作容器工具栏</title>
<link rel="stylesheet" type="text/css" href="../css/toolbar.css">


<script language="javascript" src="../scripts/toolbar.js"></script>
<script language="javascript">

function userinfo()
{
	//基本信息	
	var userId = getPropertiesContent().document.all.item("checkBoxOne");
	var userId1 = getPropertiesContent().document.all.item("userId");
	if(userId1 != null && userId1.value != "")
	{
		var temp = "";
	
		if(!userId1)
		{
			return;
		}
		if(userId1.length)
		{
			temp = userId1[0]; 
		}
		else
		{
			temp = userId1; 
		}
		
		getPropertiesContent().location.href="../user/userManager.do?method=getUser&userId="+temp.value;;
		
	}
	else
	{
		if(!userId)
		{
			alert("请选择要查看的用户！");
			return;
		}
		if(!userId.length)
		{
			if(!userId.checked)
			{
				alert("请选择要查看的用户！");
				return;
			}
			//授权
			getPropertiesContent().location.href="../user/userManager.do?method=getUser&userId="+userId.value;
		}
	
		if(userId.length)
		{
			
			var count = 0 ;
			var index = -1;
			for(var i = 0; i < userId.length; i ++)
			{
				if(!userId[i].checked)
				{
					continue;
				}
				else
				{				
					count ++;
					index = i;
				}
			}
			
			if(count == 0)
			{
				alert("请选择要查看的用户！");
				return;
			}
			else if(count == 1)
			{
				//授权
				getPropertiesContent().location.href="../user/userManager.do?method=getUser&userId="+userId[index].value;
				
			}
			else
			{
				alert("只能选择一个用户进行查看！");
				return;
			}
		}	
	}
}
//---------------------------------------------------------------------------------------------------------------------
function userjob()
{
	//隶属岗位	
	var userId = getPropertiesContent().document.all.item("checkBoxOne");
	var orgId = getPropertiesContent().document.all.item("orgId");
	var userId1 = getPropertiesContent().document.all.item("userId");
		
	if(userId1 != null && userId1.value != ""){
		var temp = "";
	
		if(!userId1)
		{
			return;
		}
		if(userId1.length)
		{
			temp = userId1[0]; 
		}
		else
		{
			temp = userId1; 
		}
		
		getPropertiesContent().location.href="../user/userJob.do?method=getJobList&userId="+temp.value+"&orgId="+orgId.value;
	}
	else
	{	
		if(!userId)
		{
			alert("请选择要查看的用户！");
			return;
		}
		if(!userId.length )
		{
			if(!userId.checked)
			{
				alert("请选择要查看的用户！");
				return;
			}
			//授权
			getPropertiesContent().location.href="../user/userJob.do?method=getJobList&userId="+userId.value+"&orgId="+orgId.value;
			
		}
		
		if(userId.length)
		{
			
			var count = 0 ;
			var index = -1;
			for(var i = 0; i < userId.length; i ++)
			{
				if(!userId[i].checked)
				{
					continue;
				}
				else
				{				
					count ++;
					index = i;
				}
			}
			
			if(count == 0)
			{
				alert("请选择要查看的用户！");
				return;
			}
			else if(count == 1)
			{
				//授权
				getPropertiesContent().location.href="../user/userJob.do?method=getJobList&userId="+userId[index].value+"&orgId="+orgId.value;
				
			}
			else
			{
				alert("只能选择一个用户进行查看！");
				return;
			}
			
		}
	}
}
//--------------------------------------------------------------------------------------------------------------------

function userorg()
{
	//隶属机构	
	var userId = getPropertiesContent().document.all.item("checkBoxOne");
	var orgId = getPropertiesContent().document.all.item("orgId");
	var userId1 = getPropertiesContent().document.all.item("userId");
		
		if(userId1 != null && userId1.value != ""){
		var temp = "";
	
		if(!userId1)
		{
			return;
		}
		if(userId1.length)
		{
			temp = userId1[0]; 
		}
		else
		{
			temp = userId1; 
		}
		
		 getPropertiesContent().location.href="../user/userOrg.do?method=getOrgList&orgId="+orgId.value+"&userId="+temp.value;
	}
	else
	{	
	if(!userId)
	{
		alert("请选择要查看的用户！");
		return;
	}
	if(!userId.length )
	{
		if(!userId.checked)
		{
			alert("请选择要查看的用户！");
			return;
		}
		//授权
		getPropertiesContent().location.href="../user/userOrg.do?method=getOrgList&orgId="+orgId.value+"&userId="+userId.value;
		
		}
	
	if(userId.length)
	{
		
		var count = 0 ;
		var index = -1;
		for(var i = 0; i < userId.length; i ++)
		{
			if(!userId[i].checked)
			{
				continue;
			}
			else
			{				
				count ++;
				index = i;
			}
		}
		
		if(count == 0)
		{
			alert("请选择要查看的用户！");
			return;
		}
		else if(count == 1)
		{
			//授权
			getPropertiesContent().location.href="../user/userOrg.do?method=getOrgList&orgId="+orgId.value+"&userId="+userId[index].value;
			
		}
		else
		{
			alert("只能选择一个用户进行查看！");
			return;
		}
		
	}
  }
}
//-------------------------------------------------------------------------------------------------------------------
function usergroup()
{
	//隶属组	
	var userId = getPropertiesContent().document.all.item("checkBoxOne");
	var orgId = getPropertiesContent().document.all.item("orgId");
	var userId1 = getPropertiesContent().document.all.item("userId");
		
		if(userId1 != null && userId1.value != ""){
		var temp = "";
	
		if(!userId1)
		{
			return;
		}
		if(userId1.length)
		{
			temp = userId1[0]; 
		}
		else
		{
			temp = userId1; 
		}
		
		  getPropertiesContent().location.href="../user/user2group.jsp?orgId="+orgId.value+"&userId="+temp.value;
	}
	else
	{	
	if(!userId)
	{
		alert("请选择要查看的用户！");
		return;
	}
	if(!userId.length )
	{
		if(!userId.checked)
		{
			alert("请选择要查看的用户！");
			return;
		}
		//授权
		 getPropertiesContent().location.href="../user/user2group.jsp?orgId="+orgId.value+"&userId="+userId.value;
		
		}
	
	if(userId.length)
	{
		
		var count = 0 ;
		var index = -1;
		for(var i = 0; i < userId.length; i ++)
		{
			if(!userId[i].checked)
			{
				continue;
			}
			else
			{				
				count ++;
				index = i;
			}
		}
		
		if(count == 0)
		{
			alert("请选择要查看的用户！");
			return;
		}
		else if(count == 1)
		{
			//授权
			getPropertiesContent().location.href="../user/user2group.jsp?orgId="+orgId.value+"&userId="+userId[index].value;
		}
		else
		{
			alert("只能选择一个用户进行查看！");
			return;
		}
		
	}
  }
}
//----------------------------------------------------------------------------------------------------------------------
function userrole()
{
	//角色授予	
	var userId = getPropertiesContent().document.all.item("checkBoxOne");
	var orgId = getPropertiesContent().document.all.item("orgId");
	var userId1 = getPropertiesContent().document.all.item("userId");
	if(userId1 == null)
	{
		userId1 = getPropertiesContent().userId;
	}
	if(orgId == null)
	{
		orgId = getPropertiesContent().orgId;
	}
	
		
		if(userId1 != null && userId1.value != ""){
		var temp = "";
	
		if(!userId1)
		{
			return;
		}
		if(userId1.length)
		{
			temp = userId1[0]; 
		}
		else
		{
			temp = userId1; 
		}
		  getPropertiesContent().location.href="../user/userRole.do?method=getRoleList&orgId="+orgId.value+"&userId="+temp.value;
	}
	else
	{	
	if(!userId)
	{
		alert("请选择要查看的用户！");
		return;
	}
	if(!userId.length )
	{
		if(!userId.checked)
		{
			alert("请选择要查看的用户！");
			return;
		}
		//授权
		 getPropertiesContent().location.href="../user/userRole.do?method=getRoleList&orgId="+orgId.value+"&userId="+userId.value;
		
		}
	
	if(userId.length)
	{
		
		var count = 0 ;
		var index = -1;
		for(var i = 0; i < userId.length; i ++)
		{
			if(!userId[i].checked)
			{
				continue;
			}
			else
			{				
				count ++;
				index = i;
			}
		}
		
		if(count == 0)
		{
			alert("请选择要查看的用户！");
			return;
		}
		else if(count == 1)
		{
			//授权
			 getPropertiesContent().location.href="../user/userRole.do?method=getRoleList&orgId="+orgId.value+"&userId="+userId[index].value;
		}
		else
		{
			alert("只能选择一个用户进行查看！");
			return;
		}
		
	}
  }
}
//-------------------------------------------------------------------------------------------------------------------
function usercopy()
{
	//权限复制
	var userId = getPropertiesContent().document.all.item("checkBoxOne");
	var orgId = getPropertiesContent().document.all.item("orgId");
	var userId1 = getPropertiesContent().document.all.item("userId");
		
	if(userId1 != null && userId1.value != ""){
		var temp = "";
	
		if(!userId1)
		{
			return;
		}
		if(userId1.length)
		{
			temp = userId1[0]; 
		}
		else
		{
			temp = userId1; 
		}
		
		  getPropertiesContent().location.href="../user/org2user.jsp?orgId="+orgId.value+"&userId="+temp.value;
	}
	else
	{	
	if(!userId)
	{
		alert("请选择要查看的用户！");
		return;
	}
	if(!userId.length )
	{
		if(!userId.checked)
		{
			alert("请选择要查看的用户！");
			return;
		}
		//授权
		 getPropertiesContent().location.href="../user/org2user.jsp?orgId="+orgId.value+"&userId="+userId.value;
		
		}
	
	if(userId.length)
	{
		
		var count = 0 ;
		var index = -1;
		for(var i = 0; i < userId.length; i ++)
		{
			if(!userId[i].checked)
			{
				continue;
			}
			else
			{				
				count ++;
				index = i;
			}
		}
		
		if(count == 0)
		{
			alert("请选择要查看的用户！");
			return;
		}
		else if(count == 1)
		{
			//授权
			getPropertiesContent().location.href="../user/org2user.jsp?orgId="+orgId.value+"&userId="+userId[index].value;
		}
		else
		{
			alert("只能选择一个用户进行查看！");
			return;
		}
		
	}
  }
}
//---------------------------------------------------------------------------------------------------------------------
function userop()
{
	//资源操作授予
	var userId = getPropertiesContent().document.all.item("checkBoxOne");
	var orgId = getPropertiesContent().document.all.item("orgId");
	var userId1 = getPropertiesContent().document.all.item("userId");
	
	if(userId1 != null && userId1.value != ""){
		var temp = "";
	
		if(!userId1)
		{
			return;
		}
		if(userId1.length)
		{
			temp = userId1[0]; 
		}
		else
		{
			temp = userId1; 
		}
		
		  getPropertiesContent().location.href="../user/user_resFrame.jsp?orgId="+orgId.value+"&userId="+temp.value;
	}
	else
	{	
	if(!userId)
	{
		alert("请选择要查看的用户！");
		return;
	}
	if(!userId.length )
	{
		if(!userId.checked)
		{
			alert("请选择要查看的用户！");
			return;
		}
		//授权
		 getPropertiesContent().location.href="../user/user_resFrame.jsp?orgId="+orgId.value+"&userId="+userId.value;
		
		}
	
	if(userId.length)
	{
		
		var count = 0 ;
		var index = -1;
		for(var i = 0; i < userId.length; i ++)
		{
			if(!userId[i].checked)
			{
				continue;
			}
			else
			{				
				count ++;
				index = i;
			}
		}
		
		if(count == 0)
		{
			alert("请选择要查看的用户！");
			return;
		}
		else if(count == 1)
		{
		
			//授权
			getPropertiesContent().location.href="../user/user_resFrame.jsp?orgId="+orgId.value+"&userId="+userId[index].value;
		}
		else
		{
			alert("只能选择一个用户进行查看！");
			return;
		}
		
	}
  }
}


var orgid="<%=orgid%>";
var uId = "<%=userId%>";
if(orgid!="" && uId =="")
	getPropertiesContent().location.href="../user/userManager.do?method=getUserList&orgId="+orgid;

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
		if(orgid == "")
		{
			alert("请在机构树中选择用户所在的部门!");
			return;
		}
		if(action != 6 && (uId == null || uId == "" ))
		{
			alert("请在用户信息列表中选择一个用户！");
			return;
		}
		    if(action == 1 && getPropertiesContent()){
		    	
				getPropertiesContent().location.href="../user/userManager.do?method=getUser";
		    }
		    else if(action == 2&&getPropertiesContent()){
				getPropertiesContent().location.href="../user/userJob.do?method=getJobList&orgId="+orgid;
		    }
	
		    else if(action == 3&&getPropertiesContent()){
		       getPropertiesContent().location.href="../user/userOrg.do?method=getOrgList&orgId="+orgid;
		    }
			else if(action == 4&&getPropertiesContent()){
		        getPropertiesContent().location.href="../user/user2group.jsp?userId=<%=userId%>";
		    }
		    else if(action == 5&&getPropertiesContent()){
		        getPropertiesContent().location.href="../user/userRole.do?method=getRoleList&orgId="+orgid;
		    }
		    else if(action == 6&&getPropertiesContent()){
		        getPropertiesContent().location.href="../user/userManager.do?method=getUserList&orgId="+orgid;
		    }
		    else if(action == 7&&getPropertiesContent()){
		        getPropertiesContent().location.href="../user/user_resFrame.jsp?userId=<%=userId%>";
		       
		    }
		     else if(action == 8&&getPropertiesContent()){
		        getPropertiesContent().location.href="../user/org2user.jsp?userId=<%=userId%>";
		       
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
			class="normal" src="../images/actions.gif"></td>
		<td valign="middle"
			align="left"  nowrap class="text">用户操作</td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle"
			align="right" width="*"  nowrap class="text">
		<table  cellpadding=0 cellspacing=0 border=0>
		<tr>
		<!--	
			<td class=normal name="tool1" id="tools1" title="用户列表" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(6)" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="用户列表" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;用户列表&nbsp;</td></tr></table></td>
		-->	           
<!------------------------------------------------------------------------------------------------------------------>
<!--			<%
			 if (accessControl.checkPermission(orgid,
                         "edituser", AccessControl.ORGUNIT_RESOURCE))
            {%>
			<td class=normal name="tool1" id="tools1" title="用户基本信息" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="userinfo()" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="用户基本信息" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;基本信息&nbsp;</td></tr></table></td>
			<%}%>             -->
<!------------------------------------------------------------------------------------------------------------------>
<!--
			<%
			 if (accessControl.checkPermission(orgid,
                         "userjobset", AccessControl.ORGUNIT_RESOURCE))
            {%>
			<td class=normal name="tool2" id="tools2" title="隶属岗位" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="userjob()" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="隶属岗位" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;隶属岗位&nbsp;</td></tr></table></td>
			<%}%>  -->
<!------------------------------------------------------------------------------------------------------------------>
<!--
			<%
			 if (accessControl.checkPermission(orgid,
                         "userorgset", AccessControl.ORGUNIT_RESOURCE))
            {%>
			<td class=normal name="tool3" id="tools3" title="隶属机构" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="userorg()" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="隶属机构" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;隶属机构&nbsp;</td></tr></table></td>
			<%}%>           -->             
<!------------------------------------------------------------------------------------------------------------------>
<!--
			<%
			 if (accessControl.checkPermission(orgid,
                         "usergroupset", AccessControl.ORGUNIT_RESOURCE))
            {%>
			<td class=normal name="tool3" id="tools3" title="隶属组" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="usergroup()" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="隶属组织" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;隶属组&nbsp;</td></tr></table></td>
			<%}%>           -->
<!------------------------------------------------------------------------------------------------------------------>
<!--
			<%
			 if (accessControl.checkPermission(orgid,
                         "userroleset", AccessControl.ORGUNIT_RESOURCE))
            {%>
			<td class=normal name="tool3" id="tools3" title="角色授予" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="userrole()" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="角色授予" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;角色授予&nbsp;</td></tr></table></td>
			<%}%>             -->       
<!------------------------------------------------------------------------------------------------------------------>
<!--			
			<%
			 if (accessControl.checkPermission(orgid,
                         "userresset", AccessControl.ORGUNIT_RESOURCE))
            {%>
			<td class=normal name="tool7" id="tools7" title="资源操作授予" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="userop()" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="资源操作授予" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;资源操作授予&nbsp;</td></tr></table></td>
			<%}%>         -->                
<!------------------------------------------------------------------------------------------------------------------>
			<!--
			<%
			 if (accessControl.checkPermission(orgid,
                         "userauthorcopy", AccessControl.ORGUNIT_RESOURCE))
            {%>
			<td class=normal name="tool8" id="tools8" title="用户权限复制" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="usercopy()" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="用户权限复制" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;权限复制&nbsp;</td></tr></table></td>
			<%}%>		
			-->
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>

