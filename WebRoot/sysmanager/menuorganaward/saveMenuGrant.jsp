<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">
	function saveMenu() {
		//获取用户选择的类型
		var v = getNavigatorContent().document.all("choose").value;
		var type = "";
		var typeName = "";
		var idString = ""; //列表内所选定的ID串
		
		if(v=="1"){
			type = "user";
			typeName = "用户";
		}else{
			type = "role";
			typeName = "角色";
		}
		
		//通过类型获取用色/用户列表对象的所有行对象
		var tempRows = null; //= getNavigatorContent().document.frames.userOrRoleFrame.document.all.table1.rows
		
		if(type=="user"){
			tempRows = getNavigatorContent().document.frames.userOrRoleFrame.userList.document.all.table1.rows;
		}else{
			tempRows = getNavigatorContent().document.frames.userOrRoleFrame.document.all.table1.rows;
		}
		
		//把列表类的所有选定的行ID拼成ID串
		for(var i=0;i<tempRows.length;i++)
		{
			if(tempRows[i].flag == "true"){
				idString += tempRows[i].id + ":";
			}
		}
		if(idString==null || idString==""){
			alert("请选择"+typeName);
			return false;
		}
		
		//alert(window.parent.self.orgChooseTree.document.forms(0).id);
  		//把所有已选定的树节点ID放入hidden内
  		var treeids = window.parent.self.menuTree.document.forms(0).treeids;
  		var ids = window.parent.self.menuTree.document.getElementsByName("treeid");
  		treeids.value = "";
  		for(var i=0;i<ids.length;i++){
  			if(ids[i].checked)
  				treeids.value += ids[i].value+":";
  		}
  		//alert(treeids.value)
  		//把用户/角色ID串放入hidden内
  		window.parent.self.menuTree.document.forms(0).roleids.value = idString;
  		
  		window.parent.self.menuTree.document.forms(0).action = "save.jsp?roletype="+type+"&restypeid=column&opids=visible";
  		window.parent.self.menuTree.document.forms(0).submit();
  		//saveForm.action = "save.jsp?treeIds="+ids+"&roletype="+type+"&restypeid=column&roleids="+idString+"&opids=visible"
  		//saveForm.submit();
  		//alert(ids);
		//var w = showModalDialog("saveHandle.jsp?treeIds="+ids+"&roletype=&restypeid=column&roleids=&opids=read",
		//"saveMenuGrant","dialogWidth:550px;dialogHeight:350px;help:no;scroll:yes;status:no");
		
	}
</SCRIPT>		
<body scroll="no">
	<form name="saveForm" target="save" method="post" >
	<table id="table1" width="100%" cellspacing="0" cellpadding="3" border="0"  bordercolor="#EEEEEE">
		<tr height="23px" ><td class="headercolor" colspan="2">角色/用户对所有菜单的可点击权限的设置</td></tr>
		<tr><td align="right" width="80%">是否保存本次操作: </td>
			<td align="right"><input type="button"  class="input" name="save" onclick="saveMenu();" value="保存"></td>
		</tr>
	</table>
	</form>
	<IFRAME name="save" marginheight="0" marginwidth="0"></IFRAME>
</body>
</html>

