<%
/**
 * <p>Title: 角色更新前台</p>
 * <p>Description: 角色更新前台</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-19
 * @author liangbing.tao
 * @version 1.0
 **/
 %>
 <%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
 

<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.RoleType"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>


<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	
	String roleId = request.getParameter("roleId");
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	UserManager userManager=SecurityDatabase.getUserManager();
	Role role = roleManager.getRoleById(roleId);
	String roleName = "";
	String roleDesc = "";
	String roleTypeid = "";
	
	String userName="";
	String userRealName="";
	if(role != null)
	{
		roleName = role.getRoleName();
		roleDesc = role.getRoleDesc();
		roleTypeid = role.getRoleType();
		
		String roleOwnerId=String.valueOf(role.getOwner_id());
		User user=userManager.getUserById(roleOwnerId);
		userName=user.getUserName();
	    userRealName=user.getUserRealname();
		
		
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title><pg:message code="sany.pdp.purviewmanager.rolemanager.role"/>【<%=roleName %>】<pg:message code="sany.pdp.sys.info.view"/></title>
			<link rel="stylesheet" type="text/css" href="../css/treeview.css">		
	</head>
	<body>
		<div style="height: 10px">&nbsp;</div>
		<div class="form_box">
			<form method="post" name="roleform">
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
					<tr>
						<th width=85px><pg:message code="sany.pdp.purviewmanager.rolemanager.role.name"/>：</th>
						<td width=140px>
							<input type=text name="roleName" readonly="readonly"  value="<%=roleName%>" maxlength="100"
							disabled="disabled" class="w120" />
						</td>
					</tr>
					<tr>		
						<th width=85px><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type"/>：</th>			                   	
					    <td>
					    <select name="roleType" class="w120" disabled="disabled">						
						<%
						List typenamelist = null;
						RoleTypeManager rtm = new RoleTypeManager();
						typenamelist = rtm.getTypeNameList();
						if(typenamelist != null)
						{
							for(int i=0;i<typenamelist.size();i++)
							{
								RoleType rt = (RoleType)typenamelist.get(i);
								if(roleTypeid.equals(rt.getRoleTypeID()))
								{
									%>
									<option value="<%=rt.getRoleTypeID()%>" selected><%=rt.getTypeName()%></option>
									<%
								}
								else
								{
									%>
									<option value="<%=rt.getRoleTypeID()%>"><%=rt.getTypeName()%></option>
									<%
								}
							}
						}
						%>												
						</select>		
					    </td>
					</tr>
					<tr>
						<th width=85px><pg:message code="sany.pdp.purviewmanager.rolemanager.role.description"/>：</th>
						<td width=140px>
							<textarea name="roleDesc" rows="4" cols="40" rows="5" disabled="disabled"  class="w120" ><%=roleDesc%></textarea>
						</td>
					</tr>
					<tr>
						<th width=85px><pg:message code="sany.pdp.purviewmanager.rolemanager.role.creator"/>：</td>
						<td width=140px>
							<input type=text name="creatorName" readonly="readonly"  value="<%=userName%>【<%=userRealName%>】" maxlength="100"
							disabled="disabled" class="w120" />
						</td>
					</tr>
			</table>
			</form>
		</div>
	</body>
</html>
