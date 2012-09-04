<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(pageContext);

%>
<html>
<head>
<title>操作容器工具栏</title>
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/toolbar.css">
<script language="javascript" src="../../sysmanager/scripts/toolbar.js"></script>
<script language="javascript">
	
	function bussinessAction(obj){
		if(obj == 1){
			getPropertiesContent().location.href="user_defined_configure.jsp";
		}
		if(obj == 2){
			getPropertiesContent().location.href="issue_war_manager.jsp";
		}
	}
</script>
<style type="text/css">
<!--
.style1 {color: #000000}
-->
</style>
</head>
<body class="toolbarbodymargin" onload="">
<div id="toolbarborder">
<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_WORKSPACE%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">

<table width="100%"  cellpadding=0 cellspacing=0 border=0>
	<tr>
		<td valign="middle" align="center" width=25 ><img
			class="normal" src="../../sysmanager/images/actions.gif"></td>
		<td valign="middle"
			align="left"  nowrap class="text">portal模板发布管理&nbsp;
	    </td>
		<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle"
			align="right" width="*"  nowrap class="text">
		<table  cellpadding=0 cellspacing=0 border=0>
			<tr>
			      <td class=normal name="tool1" id="tools1" title="自定义发布配置" valign=middle align=center 
			      		onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) 
			      		onmouseup=mouseup(this) onclick="bussinessAction(1)">
			      	<table border=0 cellPadding=0 cellSpacing=0 class="text">
			  			<tr>		
			  				<td valign=center>&nbsp;<img src="<%=rootpath %>/sysmanager/images/edit.gif" alt="自定义发布配置" 
			  					width=16 height=16 border=0 class="normal">
			  				</td>
			  				<td valign=center>&nbsp;自定义发布配置&nbsp;</td>
			  			</tr>
			  		</table>
			  	  </td>
			  	  <td class=normal name="tool2" id="tools2" title="发布文件管理" valign=middle align=center 
			      		onmousedown=mousedown(this) onmouseout=mouseout(this) onmouseover=mouseover(this) 
			      		onmouseup=mouseup(this) onclick="bussinessAction(2)">
			      	<table border=0 cellPadding=0 cellSpacing=0 class="text">
			  			<tr>		
			  				<td valign=center>&nbsp;<img src="<%=rootpath %>/sysmanager/images/edit.gif" alt="发布文件管理" 
			  					width=16 height=16 border=0 class="normal">
			  				</td>
			  				<td valign=center>&nbsp;发布文件管理&nbsp;</td>
			  			</tr>
			  		</table>
			  	  </td>  
<!-----------------------------------------------------------------------------------------------------			-->
			 	   	
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
</div>
</body>
</html>
