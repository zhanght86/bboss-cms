<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%
AccessControl accessControl = AccessControl.getInstance();
accessControl.checkAccess(pageContext);


String userId = request.getParameter("userId");

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
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/toolbar.css">


<script language="javascript" src="../../sysmanager/scripts/toolbar.js"></script>
<script language="javascript">

function userinfo()
{
	//基本信息	
	var userId = getPropertiesContent().document.all.item("checkBoxOne");
	var userId1 = getPropertiesContent().document.all.item("userId");
	if(userId1!=null){
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
						
		getPropertiesContent().location.href="../member/memberManager.do?method=getMember&flag=2&memberId="+temp.value;
		
	}else{
	if(!userId)
	{
		
		return;
	}
	if(!userId.length )
	{
		if(!userId.checked)
		{
			alert("请选择要查看的用户或双击！");
			return;
		}
		//授权
		getPropertiesContent().location.href="../member/memberManager.do?method=getMember&flag=2&memberId="+userId.value;
		
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
			alert("请选择要查看的用户或双击！");
			return;
		}
		else if(count == 1)
		{
			//授权
			getPropertiesContent().location.href="../member/memberManager.do?method=getMember&flag=2&memberId="+userId[index].value;
			
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
	var userId1 = getPropertiesContent().document.all.item("userId");
	if(userId1 == null)
	{
		userId1 = getPropertiesContent().userId;
	}
	
	
		
		if(userId1!=null){
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
		  getPropertiesContent().location.href="../member/memberManager.do?method=getRoleList&userId="+temp.value;
		}else{	
	if(!userId)
	{
		
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
		 getPropertiesContent().location.href="../member/memberManager.do?method=getRoleList&userId="+userId.value;
		
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
			 getPropertiesContent().location.href="../member/memberManager.do?method=getRoleList&userId="+userId[index].value;
		}
		else
		{
			alert("只能选择一个用户进行查看！");
			return;
		}
		
	}
  }
}





var uId = "<%=userId%>";

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
		
		if(action != 6 && (uId == null || uId == "" ))
		{
			alert("请在用户信息列表中选择一个用户！");
			return;
		}
		     if(action == 6&&getPropertiesContent()){
		        getPropertiesContent().location.href="../sitemember/charge_memberList.jsp";
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
			align="left"  nowrap class="text">收费用户操作</td>
		<td id="doubleclickcolumn" title="双击以最大化" valign="middle"
			align="right" width="*"  nowrap class="text">
		<table  cellpadding=0 cellspacing=0 border=0>
		<tr>
		
			<td class=normal name="tool1" id="tools1" title="会员列表" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(6)" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../../sysmanager/images/new.gif" alt="会员列表" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;用户列表&nbsp;</td></tr></table></td>
			      
<!------------------------------------------------------------------------------------------------------------------>
			
			<td class=normal name="tool1" id="tools1" title="会员基本信息" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="userinfo()" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../../sysmanager/images/new.gif" alt="会员基本信息" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;用户基本信息&nbsp;</td></tr></table></td>

<!------------------------------------------------------------------------------------------------------------------>
			        
			 <td class=normal name="tool3" id="tools3" title="会员角色授予" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="userrole()" ><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../../sysmanager/images/edit.gif" alt="会员角色授予" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;用户角色授予&nbsp;</td></tr></table></td>
		
			
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>
