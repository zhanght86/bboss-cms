<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
AccessControl accessControl = AccessControl.getInstance();
accessControl.checkAccess(pageContext);

			
%>
<html>
	<head>
		<title>操作容器工具栏</title>
		<link rel="stylesheet" type="text/css" href="../css/toolbar.css">
			<script language="javascript" src="../scripts/toolbar.js"></script>
		<script language="javascript">
		

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
		
		
	}
</script>
	</head>
	<body class="toolbarbodymargin">
		<div id="toolbarborder">
			<div id="toolbar" ondblclick="switchFrameworks(<%=Framework.SWITCH_WORKSPACE%>,<%=Framework.SWITCH_SCOPE_PERSPECTIVEMAIN%>,document.all.doubleclickcolumn);">
				<table width="100%"  cellpadding=0 cellspacing=0 border=0>
					<tr>
						<td valign="middle" align="center" width=25 >
							<img class="normal" src="../images/actions.gif">
						</td>
						<td valign="middle" align="left"  nowrap class="text">
							操作
						</td>
						<td id="doubleclickcolumn" recover="双击恢复" maxtitle="双击最大化" title="双击最大化" valign="middle" align="right" width="*"  nowrap class="text">
							<table  cellpadding=0 cellspacing=0 border=0>
							
									</table>
								</td>
							</table>
					
			</div>
		</div>
	</body>
</html>

