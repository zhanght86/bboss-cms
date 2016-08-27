<%
/**
 * <p>Title: 角色类型修改页面</p>
 * <p>Description: 角色类型修改页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-19
 * @author baowen.liu
 * @version 1.0
 **/
 %>
<%@ page contentType="text/html; charset=UTF-8" language="java"  errorPage="" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.RoleType"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager
				,com.frameworkset.util.StringUtil"%>
<%
	String typeId = StringUtil.replaceNull(request.getParameter("typeId"));
	RoleTypeManager roleTypeManager = new RoleTypeManager();
	RoleType roleType = roleTypeManager.getRoleType(typeId);
	String typeName = StringUtil.replaceNull(roleType.getTypeName());
	String typeDesc = StringUtil.replaceNull(roleType.getTypeDesc());
%>
<html>
<head>
<title>修改角色类型</title>
<%@ include file="/include/css.jsp"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
	
<SCRIPT language="javascript">
var api = frameElement.api, W = api.opener;

function save()
{
	if(roletypeform.typeName.value=="" || roletypeform.typeName.length==0)
	{
		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.name.notnull"/>', function(){}, api);
		return false;
	}
	if(roletypeform.typeDesc.value=="" || roletypeform.typeDesc.length==0)
	{
		W.$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.description.notnull"/>', function(){}, api);
		return false;
	}
	document.all.Submit4.disabled=true;
	document.all.btn_close.disabled=true;
	document.all.divProcessing.style.display = "block";
	roletypeform.action = "roletype_modify_do.jsp"
	roletypeform.target = "hiddenFrame"
	roletypeform.submit();
}
</SCRIPT>
</head>
<body>
<%
    AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
%>
<div style="height: 10px">&nbsp;</div>
<div class="form_box">
<form method="post" name="roletypeform">
		<table border="0" cellpadding="0" cellspacing="0" class="table4"> 
			<input type=hidden name="typeId" value=<%=typeId%> >
			<tr>
		   		<th width=85px><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.name"/>：</th>
				<td width=140px>
					<input name="typeName" type="text" value="<%=typeName%>" readonly size="22" />
				</td>
           </tr>
           <tr>
		   		<th width=85px><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.description"/>：</th>
				<td width=140px>
					<textarea name="typeDesc" rows="5" ><%=typeDesc%></textarea>
				</td>
           </tr>
      </table>
      <div class="btnarea" >
       		<a href="javascript:void(0)" class="bt_1" name="Submit4" onclick="save()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
       		<a href="javascript:void(0)" class="bt_2" name="btn_close" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.close"/></span></a>
        </div> 
</form>
</div>
<div id=divProcessing style="width:200px;height:30px;position:absolute;left:150px;top:260px;display:none">
	<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
		<tr>
			<td bgcolor=#3A6EA5>
				<marquee align="middle" behavior="alternate" scrollamount="5">
					<font color=#FFFFFF>...处理中...请等待...</font>
				</marquee>
			</td>
		</tr>
	</table>
</div>
</body>
<iframe name="hiddenFrame" width="0" height="0" style="display:none"></iframe>
</html>
