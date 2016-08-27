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

String userId = request.getParameter("userId");
String qstring = request.getParameter("qstring");
String qqstring = qstring;
if(qstring != null){
	qstring = java.net.URLEncoder.encode(qstring);
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
		
		
		getPropertiesContent().location.href="../user/userManager.do?method=getUser&dUserInfo=1&qstring=<%=qstring%>&userId="+temp.value;
	    
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
		
		getPropertiesContent().location.href="../user/userManager.do?method=getUser&dUserInfo=1&qstring=<%=qstring%>&userId="+userId.value;
		
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
			getPropertiesContent().location.href="../user/userManager.do?method=getUser&dUserInfo=1&qstring=<%=qstring%>&userId="+userId[index].value;
			
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
function userorg()
{
	//调入机构
	var userId = getPropertiesContent().document.all.item("checkBoxOne");
	//var orgId = getPropertiesContent().document.all.item("orgId");
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
		
		 
		  getPropertiesContent().location.href="../user/org2discrete.jsp?userId="+temp.value;
		}else{	
	if(!userId)
	{
		
		return;
	}
	if(!userId.length )
	{
		if(!userId.checked)
		{
			alert("请选择要调入的用户！");
			return;
		}
		//授权
		// getPropertiesContent().location.href="../user/user2group.jsp?orgId="+orgId.value+"&userId="+userId.value;
		getPropertiesContent().location.href="../user/org2discrete.jsp?userId="+userId.value;
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
			alert("请选择要调入的用户！");
			return;
		}
		else if(count == 1)
		{
			//授权
			getPropertiesContent().location.href="../user/org2discrete.jsp?&userId="+userId[index].value;
		}
		else
		{
			alert("只能选择一个用户进行调入！");
			return;
		}
		
	}
  }
}
var qstring="<%=qstring%>";
var uId = "<%=userId%>";
var orgid = "0";
//alert("uId.length():"+uId.length);
//alert("uId:" + uId);
if(uId ==""){
	getPropertiesContent();
	getPropertiesContent().location.href="../user/discreteUserList.jsp";


}

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
		if(action != 6 && uId == "")
		{
			alert("请在离散用户列表中选择一个用户!");
			return;
		}

	    if(action == 1 && getPropertiesContent()){
			getPropertiesContent().location.href="../user/userManager.do?method=getUser&dUserInfo=1&qstring=<%=qstring%>&userId=<%=userId%>";
	    }
	    else if(action == 3&&getPropertiesContent()){
	    //   getPropertiesContent().location.href="../user/userOrg.do?method=getOrgList&orgId="+orgid;
	    	getPropertiesContent().location.href="../user/org2discrete.jsp?uid="+uId;
	    	
	    }
	    else if(action == 6&&getPropertiesContent()){
	        getPropertiesContent().location.href="../user/discreteUserList.jsp?<%=qqstring%>";
	    }
	}
</script>
<style type="text/css">
<!--
.style1 {color: #CCCCCC}
-->
</style>
</head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_WORKSPACE%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">
<table width="100%" height=25 cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25 height=25><img
			class="normal" src="../images/actions.gif"></td>
		<td valign="middle"
			align="left" height=25 nowrap class="text">用户操作</td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle"
			align="right" width="*" height=25 nowrap class="text">
		<table height=25 cellpadding=0 cellspacing=0 border=0>
		<tr>
			
			<td class=normal name="tool1" id="tools1" title="用户列表" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessAction(6)" height=25><table cellSpacing=0 cellPadding=0 border=0><tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="用户列表" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;<span class="style1">用户列表</span>&nbsp;</td>
			</tr></table></td>
			

			<%
			 if (accessControl.checkPermission("lisan",
                         "lisanusermanager", AccessControl.ORGUNIT_RESOURCE))
            {%>
			<td class=normal name="tool1" id="tools1" title="用户基本信息" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="userinfo()" height=25><table cellSpacing=0 cellPadding=0 border=0><tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="用户基本信息" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;<span class="style1">用户基本信息&nbsp;</span></td>
			</tr></table></td>
			<%}%>

			<%
			 if (accessControl.checkPermission("lisan",
                         "lisanusertoorg", AccessControl.ORGUNIT_RESOURCE))
            {%>
            <!--
			<td class=normal name="tool3" id="tools3" title="调入机构" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="userorg()" height=25><table cellSpacing=0 cellPadding=0 border=0><tr><td valign=center>&nbsp;<img src="../images/edit.gif" alt="调入机构" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;<span class="style1">调入机构&nbsp;</span></td>
			</tr></table></td>
			-->
			<%}%>

			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>

