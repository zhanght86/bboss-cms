<%
/**
 * <p>Title: 角色更新前台</p>
 * <p>Description: 角色更新前台</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
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
		<title><pg:message code="sany.pdp.purviewmanager.rolemanager.role"/>【<%=roleName %>】<pg:message code="sany.pdp.sys.info.modfiy"/></title>
			<link rel="stylesheet" type="text/css" href="../css/treeview.css">
		<SCRIPT LANGUAGE="JavaScript"> 
			function updateRole() 
			{
				
				var form = document.forms[0];
				if (form.roleName.value.length < 1 || form.roleName.value.replace(/\s/g,"")=="") 
				{			
					$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.name.notnull"/>');
					return;
				}
				if (form.roleDesc.value.length < 1 || form.roleDesc.value.replace(/\s/g,"")=="") 
				{			
					$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.description.notnull"/>');
					return;
				}				
				
				if (form.roleName.value.length == "adminstrator") 
				{			
					$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.modfiy.cannot"/>');
					return;
				}
				
				var rn = form.roleName.value;
				var rd = form.roleDesc.value;
				if(rn.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.name.nospecialcharacter'/>"+ "\\/|:*?<>\"'!" + "<pg:message code='sany.pdp.purviewmanager.rolemanager.role.name.nospecialcharacter.end'/>");
					return;
				}	
				if(rd.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.description.nospecialcharacter'/>" + "\\/|:*?<>\"'!" + "<pg:message code='sany.pdp.purviewmanager.rolemanager.role.description.nospecialcharacter.end'/>");
					return;
				}
				
				if(rn.length>100)
				{
					$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.name.toolong"/>');
					return;
				}
				if(rd.length>100)
				{
					$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.description.toolong"/>');
					return;
				}
				if ((form.roleType.value==""))
  	  			{
					$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.select"/>');		  	  	
		  	  	    return;
  	  	        }
				
			  	form.action = "modifyRole_do.jsp";
				form.target = "hiddenFrame";
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
	<body>
		<div style="height: 10px">&nbsp;</div>
		<div class="form_box">
			<form method="post" name="roleform">
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
					<input type="hidden" name="roleId" value="<%=roleId%>"/>
					<tr>
						<th width=85px><pg:message code="sany.pdp.role.name"/>：</th>
						<td width=140px>
							<input type=text name="roleName" readonly="readonly"  value="<%=roleName%>" maxlength="100"
							<%
							if(roleId.equals("1") || roleId.equals("2") || roleId.equals("3") || roleId.equals("4"))
							{
							%>
							disabled
							<%
							}
							%>
							class="w120"  />
						</td>
					</tr>
					<tr>					                   	
						<th width=85px><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type"/>：</th>						
					    <td width=140px>
					    <select name="roleType" class="cms_select"  class="w120" 
					    <%
						if(roleId.equals("1") || roleId.equals("2") || roleId.equals("3") || roleId.equals("4"))
						{
						%>
						disabled
						<%
						}
						%>
					    >						
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
						</select><font color="red">*</font>		
					    </td>
					</tr>
					<tr>
						<th width=85px><pg:message code="sany.pdp.purviewmanager.rolemanager.role.description"/>：</th>
						<td width=140px>
							<textarea name="roleDesc" rows="4" cols="40" rows="5" onkeyup="checkLength(this)" class="w120"
							<%
							if(roleId.equals("1") || roleId.equals("2") || roleId.equals("3") || roleId.equals("4"))
							{
							%>
							disabled
							<%
							}
							%>
							><%=roleDesc%></textarea><font color="red">*</font>
						</td>
					</tr>
					<tr>
						<th width=85px><pg:message code="sany.pdp.purviewmanager.rolemanager.role.creator"/>：</th>
						<td width=140px>
							<input type=text name="creatorName" readonly="readonly"  value="<%=userName%>【<%=userRealName%>】" maxlength="100"
							disabled="disabled"  class="w120"/>
						</td>
					</tr>
			</table>
			<div class="btnarea" >
				<%
					if(roleId.equals("1") || roleId.equals("2") || roleId.equals("3") || roleId.equals("4"))
					{
					%>
						<a href="javascript:void(0)" class="bt_1" name="update" onclick=""><span><font color="gray"><pg:message code="sany.pdp.common.operation.save"/></font></span></a>
						<a href="javascript:void(0)" class="bt_2" name="calc" ><span><font color="gray"><pg:message code="sany.pdp.common.operation.clear"/></font></span></a>
					<%
					}
					else
					{
					%>								
						<a href="javascript:void(0)" class="bt_1" name="update" onclick="updateRole()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
						<a href="javascript:void(0)" class="bt_2" name="calc" ><span><pg:message code="sany.pdp.common.operation.clear"/></span></a>
					<%
					}
				%>
			</div>
			</form>
		</div>
	</body>
	<iframe name="hiddenFrame" width="0" height="0"></iframe>
</html>
