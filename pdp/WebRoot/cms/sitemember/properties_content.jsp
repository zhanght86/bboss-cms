<%@include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-template" prefix="template"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested" prefix="nested"%>
<%
	String f = (String)request.getAttribute("iss");
	if(f == null){
		f = "1";
	}
	String nflag = (String)request.getParameter("nflag");
	if(nflag == null){
		nflag = "0";
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>系统管理</title>
<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
		<SCRIPT LANGUAGE="JavaScript"> 
			
			function loadPage() {
				
				var form = document.forms[0];
				var act = form.action.value;
				if(form.roleName.value =="administrator"){					
					form.roleName.disabled = "true";
					form.roleDesc.disabled = "true";
					form.delete1.disabled = "true";
					form.update.disabled = "true";					
				}
				if (act == 2) {
					form.roleId.value = "";
					form.roleName.value = "";
					form.roleDesc.value = "";
				}
				
				if (act == 1 || act == 2) {
					getNavigatorContent().location.href="../sysmanager/accessmanager/role/navigator_content.jsp";					
				}

				form.action.value = 0;
			}
			
			function createRole() {
			
					getPropertiesContent().location.href="../membermanager/newRole.jsp";					
							
				
			}
			
			function updateRole() {
				var form = document.forms[0];
				if (form.roleName.value.length < 1) {			
					alert("角色名称必须填写");
					return;
				}
				if (form.roleDesc.value.length < 1) {			
					alert("角色描述必须填写");
					return;
				}				
				if (form.roleName.value.length == "adminstrator") {			
					alert("系统管理员不能修改。谢谢合作:)");
					return;
				}
				
				var form = document.forms[0];
				form.action.value = "1";
				form.submit();
			}
			
			function delRole() {
				var form = document.forms[0];
				if (form.roleId.value != "") {			
					if (window.confirm("注意删除后将不可恢复，你确认要删除这个角色吗？")) {				
						form.action.value = "2";
						form.submit();
					}
				} else {
					alert("请先在左边的用户组列表中选择需要删除的组!");
				}
			}
</SCRIPT>
	</head>

	<body class="contentbodymargin" onload="loadPage();" scroll="no">
		<div id="contentborder" align="center">
			<html:form action="accessmanager/roleManager.do?method=roleEdit">
				<table class="table" width="100%" height="25" border="0" cellpadding="0" cellspacing="2">
					<tr class="tr">
						<td width="16%" height="25" class="detailtitle">
							<strong>角色名称：</strong>
						</td>
						<td height="25">
						<html:text property="roleName"  style="width:30%" />
							<html:hidden property="roleId" />
							<html:hidden property="action" />
						</td>
					</tr>
					<tr class="tr">
						<td height="25" class="detailtitle">
							<strong>描述：</strong>
						</td>
						<td height="25">
							<html:textarea property="roleDesc" style="width:30%" />
						</td>
						<td align="right" class="td"></td>
					</tr>
				</table>
				<hr width="98%">
				<table width="60%" border="0" align="right" cellpadding="0" cellspacing="0" class="table">

					<tr class="tr">
						<td class="td">
							<div align="right">
								<input name="new" type="button" onClick="createRole()" class="input" value="新增">
								<input name="delete1" type="button" onClick="delRole()" class="input" value="删除">
								<input name="update" type="button" onClick="updateRole()" class="input" value="保存">
								<input name="calc" type="reset" class="input" value="重置">
							</div>
						</td>
					</tr>
				</table>
			</html:form>
		</div>
	</body>
</html>
