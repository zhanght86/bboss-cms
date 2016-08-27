<%@ include file="../../include/global1.jsp"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role
				,com.frameworkset.platform.config.ConfigManager" %>
<%

AccessControl accessControl = AccessControl.getInstance();
accessControl.checkManagerAccess(request,response);

String orgid = request.getParameter("selected");


String roleTabId = (String)session.getAttribute("roleTabId");


if(roleTabId == null){
	roleTabId = "1";
}

%>
<html>
<head>
<title>操作容器工具栏</title>
<link rel="stylesheet" type="text/css" href="../../css/toolbar.css">
<script language="javascript" src="../../scripts/toolbar.js"></script>
<script language="javascript">
var orgid="<%=orgid%>"; 
 <%
	String currRoleId = (String)request.getParameter("roleId");
	session.setAttribute("currRoleId", currRoleId);
	if(currRoleId == null)
		currRoleId = "";
	Role r = null;	
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	r = roleManager.getRoleById(currRoleId);
	String roleName = "请选择角色";
	if(r != null){
		roleName = r.getRoleName();		
	}
%>
var rid = "<%=currRoleId%>";
//alert(getPropertiesContent());
//alert(getPropertiesContent().location);
//alert(getPropertiesContent().location.href);
//判断当前的角色是在哪个tab页面
	var tabId = "<%=roleTabId%>";
	
	if(tabId == 1){	
		bussinessAction(1);
	
	}
	else if(tabId == 2){	
		bussinessAction(2);
	
	}
	else if(tabId == 3){	
		bussinessAction(3);
	
	}
	
	else if(tabId == 4){	
		bussinessAction(4);
	
	}
	
	else if(tabId == 5){	
		bussinessAction(5);
	
	}
	else if(tabId == 6){	
		bussinessAction(6);
	
	}
	else if(tabId == 7){	
		bussinessAction(7);
	
	}
	//角色权限回收
	function reclaimPurview(){
	    var roleId = rid;
	    if(roleId == null || roleId == ""){
	        alert("请在左边的角色树中选择一个角色！");
	        return false;
	    }
	    if(roleId != null && roleId != ""){
		    if(!confirm("确定要回收角色的权限吗?")){
		        return false;
		    }
			document.form1.action = "../../../purviewmanager/reclaimManager/reclaimRoleRes_do.jsp?roleId="+roleId;
	    	document.form1.target = "hiddenFrame";
	    	document.form1.submit();
		}
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
	function bussinessActionWraper(action){	
		if(rid == "" )
		{
			alert("请在左边的角色树中选择一个角色！");
			return;
		}
		bussinessAction(action);
	}
	function bussinessAction(action){
		if(rid!= "1" && rid!= "2" && rid!= "3" && rid!= "4"){//处理不是系统管理员也不是机构管理员也不是普通用户的情况
			if(rid =="" || !getPropertiesContent())
	    	{
    			getPropertiesContent().location.href="../../accessmanager/role/noRole.jsp";
	    	}
	    	else
	    	{
	    		if(action == 1 && getPropertiesContent())
		    	{
					getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
				}	   
			    else if(action == 2 && getPropertiesContent())
			    {
			    	getPropertiesContent().location.href="../../accessmanager/role/role2user.jsp?roleId=<%=currRoleId%>";
			    }
			    else if(action == 3&& getPropertiesContent())
			    {
			    	getPropertiesContent().location.href="../../accessmanager/role/resFrame.jsp";
			    }
			    else if(action == 4 && getPropertiesContent())
			    {
			    	getPropertiesContent().location.href="../../accessmanager/role/role2group.jsp?roleId=<%=currRoleId%>";
				}
			    else if(action == 5&&getPropertiesContent())
			    {
			    	getPropertiesContent().location.href="../../accessmanager/role/rolecopy.jsp?roleId=<%=currRoleId%>";
				}
			    else if(action == 6 && getPropertiesContent())
			    {
			     	getPropertiesContent().location.href="../../accessmanager/role/role2org.jsp?roleId=<%=currRoleId%>";
			    }
		    	else if(action == 7&&getPropertiesContent())
		    	{
			     	getPropertiesContent().location.href="../../accessmanager/role/role2list.jsp?roleId=<%=currRoleId%>";
			    }
	    	}
	    	
		}
		else if(rid == "1"){//处理系统管理员的情况
	     	if(action == 1 && getPropertiesContent())
	     	{
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}	   
		    else if(action == 2 && getPropertiesContent())
		    {
		    	getPropertiesContent().location.href="../../accessmanager/role/role2user.jsp?roleId=<%=currRoleId%>";
			}
		    else if(action == 3&&getPropertiesContent())
		    {
		    	alert("此角色是系统管理员，不能进行此项操作！");
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
		    else if(action == 4&&getPropertiesContent())
		    {
		    	getPropertiesContent().location.href="../../accessmanager/role/role2group.jsp?roleId=<%=currRoleId%>";
			}
		    else if(action == 5&&getPropertiesContent())
		    {
		    	getPropertiesContent().location.href="../../accessmanager/role/rolecopy.jsp?roleId=<%=currRoleId%>";
			}
		    else if(action == 6 && getPropertiesContent())
		    {
		    	getPropertiesContent().location.href="../../accessmanager/role/role2org.jsp?roleId=<%=currRoleId%>";
			}
	      	else if(action == 7 && getPropertiesContent())
	      	{
	    		alert("此角色是系统管理员，不能进行此项操作！");
	    		getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
	    	}
	  	}
	  	else if(rid == "3"){//处理机构管理员的情况
	     	if(action == 1 && getPropertiesContent())
	     	{
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}	   
		    else if(action == 2 && getPropertiesContent())
		    {
		    	alert("此角色是机构管理员，不能进行此项操作！");
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
		    else if(action == 3&&getPropertiesContent())
		    {
		    	alert("此角色是机构管理员，不能进行此项操作！");
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
		    else if(action == 4&&getPropertiesContent())
		    {
		    	alert("此角色是机构管理员，不能进行此项操作！");
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
		    else if(action == 5&&getPropertiesContent())
		    {
		    	alert("此角色是机构管理员，不能进行此项操作！");
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
		    else if(action == 6 && getPropertiesContent())
		    {
		     	alert("此角色是机构管理员，不能进行此项操作！");
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
	      	else if(action == 7 && getPropertiesContent())
	      	{
	    		getPropertiesContent().location.href="../../accessmanager/role/role2list.jsp?roleId=<%=currRoleId%>";
			}
	  	}
	  	else if(rid == "4"){//处理机构管理员角色模板的情况
	     	if(action == 1 && getPropertiesContent())
	     	{
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}	   
		    else if(action == 2 && getPropertiesContent())
		    {
		    	alert("此角色是机构管理员角色模板，不能进行此项操作！");
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
				<%
				if(accessControl.isAdmin())
				{
				%>
				else if(action == 3&&getPropertiesContent())
			    {
			    	getPropertiesContent().location.href="../../accessmanager/role/resFrame.jsp";
				}
				<%
				}
				else
				{
				%>
				else if(action == 3&&getPropertiesContent())
			    {
			    	alert("此角色是机构管理员角色模板，不能进行此项操作！");
			    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
				}
				<%
				}
				%>
		    else if(action == 4&&getPropertiesContent())
		    {
		    	alert("此角色是机构管理员角色模板，不能进行此项操作！");
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
		    else if(action == 5&&getPropertiesContent())
		    {
		    	alert("此角色是机构管理员角色模板，不能进行此项操作！");
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
		    else if(action == 6 && getPropertiesContent())
		    {
		     	alert("此角色是机构管理员角色模板，不能进行此项操作！");
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
	      	else if(action == 7 && getPropertiesContent())
	      	{
	    		getPropertiesContent().location.href="../../accessmanager/role/role2list.jsp?roleId=<%=currRoleId%>";
			}
	  	}
	  	else if(rid == "2"){//处理普通用户的情况
	     	if(action == 1 && getPropertiesContent())
	     	{
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}	   
		    else if(action == 2 && getPropertiesContent())
		    {
		    	alert("此角色是普通用户，不能进行此项操作！");
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
			
			<%
			if(accessControl.isAdmin())
			{
			%>
			else if(action == 3&&getPropertiesContent())
		    {
		    	getPropertiesContent().location.href="../../accessmanager/role/resFrame.jsp";
		    }
			<%
			}
			else
			{
			%>
			else if(action == 3&&getPropertiesContent())
		    {
		    	alert("此角色是普通用户，不能进行此项操作！");
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
			<%
			}
			%>
		    
		    else if(action == 4&&getPropertiesContent())
		    {
		    	alert("此角色是普通用户，不能进行此项操作！");
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
		    else if(action == 5&&getPropertiesContent())
		    {
		    	alert("此角色是普通用户，不能进行此项操作！");
		    	getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
		    else if(action == 6 && getPropertiesContent())
		    {
		     	alert("此角色是普通用户，不需要查看角色资源列表！");
	    		getPropertiesContent().location.href="../../../accessmanager/roleManager.do?method=getRoleById&roleId=<%=currRoleId%>";
			}
	      	else if(action == 7 && getPropertiesContent())
	      	{
	      		getPropertiesContent().location.href="../../accessmanager/role/role2list.jsp?roleId=<%=currRoleId%>";
	    	}
	  	}
	}
</script>
</head>
<body class="toolbarbodymargin">
<form name="form1" method="post">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_WORKSPACE%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">

<table width="100%" cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25><img
			class="normal" src="../../images/actions.gif"></td>
		<!-- <td valign="middle"
			align="left" nowrap class="text" width="10%">&nbsp;当前角色：<%=roleName%></td> -->
		<td valign="middle"
			align="left" nowrap class="text" >业务操作</td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle"
			align="right" width="*" nowrap class="text">
		<table cellpadding=0 cellspacing=0 border=0>
		<tr>
			<%
			if (accessControl.checkPermission(currRoleId,
                       AccessControl.WRITE_PERMISSION, AccessControl.ROLE_RESOURCE))
            {
            %>
			<td class=normal name="tool1" id="tools1" title="角色基本信息" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessActionWraper(1)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../../images/new.gif" alt="角色基本信息" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;角色信息&nbsp;</td></tr></table></td>
			<%}%>
<!-------------------------------------------------------------------------------------------------------------			-->
			<%
			 if (accessControl.checkPermission(currRoleId,
                       "userset", AccessControl.ROLE_RESOURCE))
            {%>
			<td class=normal name="tool2" id="tools2" title="角色授予用户" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessActionWraper(2)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../../images/edit.gif" alt="角色授予用户" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;角色授予用户&nbsp;</td></tr></table></td>
            <%}%>  
<!-------------------------------------------------------------------------------------------------------------			-->
            
            <%
			 if (accessControl.checkPermission(currRoleId,
                       "orgset", AccessControl.ROLE_RESOURCE)
                       && ConfigManager.getInstance().getConfigBooleanValue("enableorgrole", true))
            {%>
			<td class=normal name="tool6" id="tools6" title="角色授予机构" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessActionWraper(6)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../../images/edit.gif" alt="角色授予机构" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;角色授予机构&nbsp;</td></tr></table></td>
            <%}%>         
<!-------------------------------------------------------------------------------------------------------------			-->
            
            <%
			 if (accessControl.checkPermission(currRoleId,
                       "resopset", AccessControl.ROLE_RESOURCE))
            {%>
			<td class=normal name="tool3" id="tools3" title="资源操作授予" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessActionWraper(3)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../../images/edit.gif" alt="资源操作授予" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;资源操作授予&nbsp;</td></tr></table></td>
			<%}%>                  
<!-------------------------------------------------------------------------------------------------------------			-->
			
			 <%
			 if (accessControl.checkPermission(currRoleId,
                       "groupset", AccessControl.ROLE_RESOURCE)
                       && ConfigManager.getInstance().getConfigBooleanValue("enablergrouprole", true))
            {%>
			<td class=normal name="tool4" id="tools4" title="角色授予用户组" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessActionWraper(4)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../../images/edit.gif" alt="角色授予用户组" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;角色授予用户组&nbsp;</td></tr></table></td>				
			<%}%>
<!-------------------------------------------------------------------------------------------------------------			-->
		
			<%
			 if (accessControl.checkPermission(currRoleId,
                       "resopsee", AccessControl.ROLE_RESOURCE))
            {%>
			<td class=normal name="tool7" id="tools7" title="角色资源列表" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessActionWraper(7)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../../images/edit.gif" alt="角色资源列表" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;角色资源列表&nbsp;</td></tr></table></td>				
			<%}%>		
<!-------------------------------------------------------------------------------------------------------------			-->
			
			<%
			 if (accessControl.checkPermission(currRoleId,
                       "copy", AccessControl.ROLE_RESOURCE))
            {%>
			<td class=normal name="tool5" id="tools5" title="角色权限复制" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="bussinessActionWraper(5)"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../../images/edit.gif" alt="角色权限复制" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;角色权限复制&nbsp;</td></tr></table></td>				
			<%}%>	

<!-------------------------------------------------------------------------------------------------------------			-->
			
			<%
			//资源回收
			 if (accessControl.checkPermission(currRoleId,
                       "userresset", AccessControl.ROLE_RESOURCE))
            {%>
			<td class=normal name="tool5" id="tools5" title="角色权限回收" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="reclaimPurview()"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../../images/edit.gif" alt="角色权限回收" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;角色权限回收&nbsp;</td></tr></table></td>				
			<%}%>	
		
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
</div>
<iframe name="hiddenFrame" id="hiddenFrame" src="" width=0 height=0 ></iframe>
</form>
</body>

</html>
