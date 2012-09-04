<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.List"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-template" prefix="template"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested" prefix="nested"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/tabpane-taglib.tld" prefix="tab"%>
<%@ taglib uri="../../../WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.RoleType"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	String curOrgId = accessControl.getChargeOrgId();
	
	String f = (String)request.getAttribute("iss");
	if(f == null){
		f = "1";
	}
	String nflag = (String)request.getParameter("nflag");
	if(nflag == null){
		nflag = "0";
	}
	
	
	String currRoleId = (String)request.getParameter("roleId");
	if(currRoleId == null)
	{
		currRoleId= "";
	}
	
	String roleName = "";
	String roleTypeid = "";
	String roleType = "";
	String roleTypeCreatorOrgId = "";
	RoleTypeManager rtm = new RoleTypeManager();
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	Role role = roleManager.getRoleById(currRoleId);
	if(role != null)
	{
		roleName = role.getRoleName();
		roleTypeid = role.getRoleType();
		RoleType rt = rtm.getRoleType(roleTypeid);
		roleType = 	rt.getTypeName();	
		roleTypeCreatorOrgId = rt.getCreatorOrgId();
	}
	boolean isCurOrgId = roleTypeCreatorOrgId.equals(curOrgId);
	boolean isAdmin = accessControl.isAdmin();

%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>系统管理</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">
	<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
		<SCRIPT LANGUAGE="JavaScript"> 
			
			function loadPage() {
				var form = document.forms[0];
				var act = form.action.value;
				if(form.roleId.value =="1" || form.roleId.value =="2" || form.roleId.value =="3" || form.roleId.value =="4"){					
					form.roleName.disabled = "true";
					form.roleDesc.disabled = "true";
					form.delete1.disabled = "true";
					form.update.disabled = "true";
					form.calc.disabled = "true";				
				}
				if (act == 2) {
					form.roleId.value = "";
					form.roleName.value = "";
					form.roleDesc.value = "";
				}
				if (act == 1 || act == 2) {
					alert("操作成功！");
					getNavigatorContent().location.href="../sysmanager/accessmanager/role/navigator_content.jsp";					
				}
				form.action.value = 0;
			}
			
			function updateRole() {
				document.all("roleType").disabled = false;
				var form = document.forms[0];
				if (form.roleDesc.value.length < 1 || form.roleDesc.value.replace(/\s/g,"")=="") {			
					alert("角色描述必须填写");
					return;
				}
				var rd = form.roleDesc.value;
				if(rd.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					alert("角色描述不能有\\/|:*?<>\"'!等特殊字符");
					return;
				}
				if(rd.length>100)
				{
					alert("角色描述过长,限制在100字符以内!");
					return;
				}
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

			function checkLength(e)
			{
				var elength = e.value.length;
				if(elength>100)
				{
					alert("请限制在100字符以内!");
				}
			}
</SCRIPT>
	</head>

	<body class="contentbodymargin" onload="loadPage();" scroll="no">
		<div id="contentborder" align="center">
			<html:form action="accessmanager/roleManager.do?method=roleEdit">
				<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1" class="thin">
					
					<tr class="tr">
						<td width="16%" height="25" class="detailtitle" align="right">
							角色名称：
						</td>
						<td height="25">
						<html:text property="roleName" style="width=50%" maxlength="100" value="<%=roleName%>" />
							<html:hidden property="roleId" />
							<html:hidden property="action" />
						</td>
					</tr>
					<tr class="tr">					                   	
						<td width="16%" height="25" class="detailtitle" align="right">角色类型：</td>						
					    <td>
					    <select name="roleType" class="cms_select" 
					    <%
					    	if(!isCurOrgId && !isAdmin){
					    		out.print("disabled");
					      	}
					    %>
					    >
						
						<%
						List typenamelist = null;
						if(isCurOrgId && !isAdmin){
							typenamelist = rtm.getTypeNameList(curOrgId);
						}else{
							typenamelist = rtm.getTypeNameList();
						}
						request.setAttribute("typenamelist",typenamelist);
						%>
						<pg:list requestKey="typenamelist">
						<option value="<pg:cell colName="roleTypeID"/>" 
							<pg:equal colName="roleTypeID" value="<%=roleTypeid%>">
								selected
								</pg:equal>
						>
						<pg:cell colName="typeName"/> 
						</pg:list>
						</select>		
					    </td>
					</tr>
					<tr class="tr">
						<td height="25" class="detailtitle" align="right">
							描述：
						</td>
						<td height="25">
							<html:textarea property="roleDesc" rows="4" cols="50" onkeyup="checkLength(this)"/>
						</td>
					</tr>
				</table>
				<hr width="98%">
				<table width="60%" border="0" align="right" cellpadding="0" cellspacing="0" class="table">

					<tr class="tr">
						<td class="td">
							<div align="right">
								<%
								 if (accessControl.checkPermission(currRoleId,
					                       "delete", AccessControl.ROLE_RESOURCE))
					            {%>
								<input name="delete1" type="button" onClick="delRole()" class="input" value="删除">
								<%}%>
								<%
								 if (accessControl.checkPermission(currRoleId,
					                       "edit", AccessControl.ROLE_RESOURCE))
					            {%>
								<input name="update" type="button" onClick="updateRole()" class="input" value="保存">
								<input name="calc" type="reset" class="input" value="重置">
								<%}%>
							</div>
						</td>
					</tr>
				</table>
			</html:form>
		</div>
	</body>
</html>
