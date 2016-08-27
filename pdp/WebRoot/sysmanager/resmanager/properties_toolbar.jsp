<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
AccessControl accessControl = AccessControl.getInstance();
accessControl.checkAccess(pageContext);

String resId = request.getParameter("selected");
String restypeId = request.getParameter("restypeId");
String restypeName=request.getParameter("restypeName");
	
if(restypeId == null) 
	restypeId = "";

if(resId==null)resId="";

//单击产生的参数
String resId2 = request.getParameter("resId2");
String resTypeId2 = request.getParameter("resTypeId2");
if(resId2==null)resId2="";
if(resTypeId2==null)resTypeId2="";
//从资源列表过来的参数
String resTypeName = request.getParameter("resTypeName");
String title = request.getParameter("title");
%>
<html>
<head>
<title>操作容器工具栏</title>
<link rel="stylesheet" type="text/css" href="../css/toolbar.css">

<script language="javascript" src="../scripts/toolbar.js"></script>
<script language="javascript">


function goResourceList()
{
	var restypeId = getPropertiesContent().document.all.item("restypeId");
	
	var temp = "";
	
	if(!restypeId)
	{
		alert("请选择资源！");
		return;
	}
	if(restypeId.length)
	{
		temp = restypeId[0]; 
	}
	else
	{
		temp = restypeId; 
	}
	
	var resTypeName = getPropertiesContent().document.all.item("resTypeName");

	getPropertiesContent().location.href="<%=rootpath%>/sysmanager/resmanager/reslist.jsp?restypeId="+temp.value+ "&restypeName="+resTypeName.value;		

}
function authorResource()
{
	

	var restypeId = getPropertiesContent().document.all.item("restypeId");
	
	if(!restypeId)
	{
		alert("请选择资源！");
		return;
	}
	var resId = getPropertiesContent().document.all.item("checkBoxOne");
	var resTypeName = getPropertiesContent().document.all.item("resTypeName");
	var title = getPropertiesContent().document.all.item("title");
	if(!resId)
	{
		
		return;
	}
	if(!resId.length )
	{
		if(!resId.checked)
		{
			alert("请选择要授权的资源！");
			return;
		}
		//授权
		getPropertiesContent().location.href="<%=rootpath%>/sysmanager/resmanager/hasPower_ajax.jsp?resId2="+resId.value+"&resTypeId2="+restypeId.value+"&resTypeName="+resTypeName.value+"&title="+title.value;
	}
	
	if(resId.length)
	{
		
		var count = 0 ;
		var index = -1;
		for(var i = 0; i < resId.length; i ++)
		{
			if(!resId[i].checked)
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
			alert("请选择要授权的资源！");
			return;
		}
		else if(count == 1)
		{
			//授权
			getPropertiesContent().location.href="<%=rootpath%>/sysmanager/resmanager/hasPower_ajax.jsp?resId2="+resId[index].value+"&resTypeId2="+restypeId.value+"&resTypeName="+resTypeName.value+"&title="+title[index].value;
			
		}
		else
		{
			alert("只能选择一个资源进行授权！");
			return;
		}
		
	}
	
		
	
}

var resId="<%=resId%>"; 
var restypeId="<%=restypeId%>";
var resTypeName="<%=resTypeName%>";
var title = "<%=title%>";
if(resId!=""){
		getPropertiesContent().location.href="<%=rootpath%>/sysmanager/resmanager/reslist.jsp?restypeId="+restypeId+ "&restypeName=<%=restypeName%>";
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
	
	

</script>
</head>
<body class="toolbarbodymargin">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_WORKSPACE%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">


<table width="100%" cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25><img
			class="normal" src="../images/actions.gif"></td>
		<td valign="middle"
			align="left" nowrap class="text">业务操作</td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle"
			align="right" width="*" nowrap class="text">
		<table cellpadding=0 cellspacing=0 border=0>
		<tr>
			<%
			 if (accessControl.checkPermission(restypeId,
                       AccessControl.WRITE_PERMISSION, AccessControl.RES_RESOURCE))
            {%>
			<td class=normal name="tool2" id="tools2" title="资源列表" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="goResourceList()"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="资源列表" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;资源列表&nbsp;</td></tr></table></td>
			<%}%>			
<!--------------------------------------------------------------------------------------------------------------			-->
			<%
			 if (accessControl.checkPermission(restypeId,
                       "respermissionset", AccessControl.RES_RESOURCE))
            {%>
			<td class=normal name="tool1" id="tools1" title="资源访问授权" valign=middle align=center onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) onmouseup=mouseup(this) onclick="authorResource()"><table border=0 cellPadding=0 cellSpacing=0 class="text">
			  <tr><td valign=center>&nbsp;<img src="../images/new.gif" alt="资源访问授权" width=16 height=16 border=0 class="normal"></td><td valign=center>&nbsp;资源访问授权&nbsp;</td></tr></table></td>			
			<%}%>                       
			</tr>
		</table>
		</td>
	</tr>
</table>

</div>

</body>
</html>

