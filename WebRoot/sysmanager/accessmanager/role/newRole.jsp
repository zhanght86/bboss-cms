<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-template" prefix="template"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-nested" prefix="nested"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/tabpane-taglib.tld" prefix="tab"%>
<%@ taglib uri="../../../WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	String curOrgId = accessControl.getChargeOrgId(); 
	boolean isAdmin = accessControl.isAdmin();
	String isRoleExist = "false";
	if ( request.getAttribute("isRoleExist") != null){
		isRoleExist = "true";
	}
	//add by ge.tao
	//date 2008-01-25
	//新增角色, 角色类别没有回写的问题
	String roleTypeid = String.valueOf(session.getAttribute("roleTypeid"));
	session.removeAttribute("roleTypeid");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>系统管理</title>
			<%@ include file="/include/css.jsp"%>
		<%@ include file="/include/css.jsp"%>	
		<SCRIPT LANGUAGE="JavaScript"> 
			var isRoleExist = "<%=isRoleExist%>";
			if ( isRoleExist == "true" ){
			alert("角色名称已存在，请重新输入!");
			}
	
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
				
				if(isRoleExist != "true")
				{
					if (act == 1 || act == 2) 
					{
						alert("操作成功！");
						getNavigatorContent().location.href="../sysmanager/accessmanager/role/navigator_content.jsp";					
					}
				}
				
				form.action.value = 0;
			}
			
			
			
			function updateRole() {
				
				var form = document.forms[0];
				if (form.roleName.value.length < 1 || form.roleName.value.replace(/\s/g,"")=="") {			
					alert("角色名称必须填写");
					return;
				}
				if (form.roleDesc.value.length < 1 || form.roleDesc.value.replace(/\s/g,"")=="") {			
					alert("角色描述必须填写");
					return;
				}				
				if (form.roleName.value.length == "adminstrator") {			
					alert("系统管理员不能修改。谢谢合作:)");
					return;
				}
				
				var rn = form.roleName.value;
				var rd = form.roleDesc.value;
				if(rn.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					alert("角色名称不能有\\/|:*?<>\"'!等特殊字符");
					return;
				}	
				if(rd.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					alert("角色描述不能有\\/|:*?<>\"'!等特殊字符");
					return;
				}
				
				if(rn.length>100)
				{
					alert("角色名称过长,限制在100字符以内!");
					return;
				}
				if(rd.length>100)
				{
					alert("角色描述过长,限制在100字符以内!");
					return;
				}
				if ((form.roleType.value==""))
  	  			{
		  	  	    alert("请选择角色类别");		  	  	
		  	  	    return;
  	  	        }
				var form = document.forms[0];
				form.action.value = "1";
				form.submit();
			}
			
			function checkLength(e)
			{
				var elength = e.value.length;
				var v = e.value;				
				if(elength>=100)
				{
					e.value = v.substring(0,100);
				}
			}
			
</SCRIPT>
	</head>

	<body class="contentbodymargin" onload="loadPage();" scroll="no">
		<div id="contentborder" align="center">
			<html:form action="accessmanager/roleManager.do?method=roleEdit">
				<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1" class="thin">
					<tr class="tr">
						<td width="16%" height="25" class="detailtitle" align="right">角色名称*：</td>
						<td height="25">
						<html:text property="roleName" style="width=50%" maxlength="100"/>
							<html:hidden property="roleId" />
							<html:hidden property="action" />
						</td>
						</tr>
					<tr class="tr">					                   	
						<td width="16%" height="25" class="detailtitle" align="right">角色类型*：</td>						
					    <td>
					    <select name="roleType" class="cms_select">
						<option value="">--请选择角色类别--</option>
						<%
						List typenamelist = null;
						RoleTypeManager rtm = new RoleTypeManager();
						if(isAdmin){
							typenamelist = rtm.getTypeNameList();
						}else{
							typenamelist = rtm.getTypeNameList(curOrgId);
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
						<td height="25" class="detailtitle" align="right">描述*：</td>
						<td height="25">
							<html:textarea property="roleDesc" rows="4" cols="50" onkeyup="checkLength(this)"/>
						</td>
					</tr>
			</table>
            <hr width="100%">				
            <table width="60%" border="0" align="right" cellpadding="0" cellspacing="0" class="table">

				<tr class="tr">
					<td class="td">
						<div align="right">								
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
