<%
/**
 * <p>Title: 添加角色页面</p>
 * <p>Description: 添加角色页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-19
 * @author baowen.liu
 * @version 1.0
 **/
 %>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/tabpane-taglib.tld" prefix="tab"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager"%>
<%
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
		<title><pg:message code="sany.pdp.purviewmanager.rolemanager.role.add"/></title>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
			<link rel="stylesheet" type="text/css" href="../css/treeview.css">
		<SCRIPT LANGUAGE="JavaScript"> 
			var api = frameElement.api, W = api.opener;
		
			var isRoleExist = "<%=isRoleExist%>";
			if ( isRoleExist == "true" ){
			W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.name.exist"/>');
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
						W.$.dialog.alert('<pg:message code="sany.pdp.common.operation.success"/>');
											
					}
				}
				
				form.action.value = 0;
			}
			
			
			
			function updateRole() {
				
				var form = document.forms[0];
				if (form.roleName.value.length < 1 || form.roleName.value.replace(/\s/g,"")=="") {			
					W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.name.notnull"/>');
					return;
				}
				if (form.roleDesc.value.length < 1 || form.roleDesc.value.replace(/\s/g,"")=="") {			
					W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.description.notnull"/>');
					return;
				}				
				if (form.roleName.value.length == "adminstrator") {			
					W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.modfiy.cannot"/>');
					return;
				}
				
				var rn = form.roleName.value;
				var rd = form.roleDesc.value;
				if(rn.search(/[\\\/\|:\*\?<>"']/g) != -1){
					W.$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.name.nospecialcharacter'/>"+ "\\/|:*?<>\"'!" + "<pg:message code='sany.pdp.purviewmanager.rolemanager.role.name.nospecialcharacter.end'/>");
					return;
				}	
				if(rd.search(/[\\\/\|:\*\?<>"']/g) != -1){
					W.$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.description.nospecialcharacter'/>" + "\\/|:*?<>\"'!" + "<pg:message code='sany.pdp.purviewmanager.rolemanager.role.description.nospecialcharacter.end'/>");
					return;
				}
				
				if(rn.length>100)
				{
					W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.name.toolong"/>');
					return;
				}
				if(rd.length>100)
				{
					W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.description.toolong"/>');
					return;
				}
				if ((form.roleType.value==""))
  	  			{
		  	  	    W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.select"/>');		  	  	
		  	  	    return;
  	  	        }
				//document.all.update.disabled=true;
				//document.all.calc.disabled=true;
				//document.all.calc[1].disabled=true;
				//document.all.divProcessing.style.display = "block";
			  	form.action = "addRole_do.jsp";
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
			
			function doreset(){
				$("#reset").click();
			}
			
</SCRIPT>
	</head>
	<body onload="loadPage();" scroll="no">
	<div style="height: 10px">&nbsp;</div>
		<div class="form_box">
			<form method="post" name="roleform">
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
					<tr>
						<th width=85px><pg:message code="sany.pdp.purviewmanager.rolemanager.role.name"/>：</th>
						<td width=140px>
							<input type=text name="roleName"  maxlength="100"/><font color="red">*</font>
						</td>
					</tr>
					<tr>					                   	
						<th width=85px><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type"/>：</th>						
					    <td width=140px>
					    <select name="roleType" class="cms_select">
						<option value="">--<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.select"/>--</option>
						<%
						List typenamelist = null;
						RoleTypeManager rtm = new RoleTypeManager();
						typenamelist = rtm.getTypeNameList();
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
					<tr>
						<th width=85px><pg:message code="sany.pdp.purviewmanager.rolemanager.role.description"/>：</td>
						<td width=140px>
							<textarea name="roleDesc" rows="4" cols="40" rows="5" onkeyup="checkLength(this)"></textarea>
						</td>
					</tr>
			</table>
			<div class="btnarea" >
				<a href="javascript:void(0)" class="bt_1" name="update" onclick="updateRole()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
				<a href="javascript:void(0)" class="bt_2" name="calc" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
				<input type="reset" id="reset" style="display: none;" />
			</div>
			</form>
		</div>
	</body>
	<iframe name="hiddenFrame" width="0" height="0"></iframe>
</html>
