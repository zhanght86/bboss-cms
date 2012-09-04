<%
/**
 * <p>Title: 新增角色类型页面</p>
 * <p>Description: 新增角色类型页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-19
 * @author baowen.liu
 * @version 1.0
 **/
 %>
<%@ page contentType="text/html; charset=UTF-8" language="java"  errorPage="" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<html>
<head>
<title><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.add"/></title>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<SCRIPT language="javascript">
function save()
{   
	if(roletypeform.roletype.value.replace(/\s/g,"")=="" || roletypeform.roletype.length==0)
	{
		$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.notnull"/>');
		return false;
	}
	if(roletypeform.typedesc.value.replace(/\s/g,"")=="" || roletypeform.typedesc.length==0)
	{
		$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.description.notnull"/>');
		return false;
	}
	
	//document.all.Submit4.disabled=true;
	//document.all.btn_close.disabled=true;
	//document.all.divProcessing.style.display = "block";
	
	roletypeform.action = "roletype_add_do.jsp";
	roletypeform.target = "hiddenFrame";
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
<form method="post" name="roletypeform" id="roletypeform">
		<table border="0" cellpadding="0" cellspacing="0" class="table4"> 
		   <tr>
		   		<th width=85px><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type"/>：</th>
				<td width=140px>
					<input name="roletype" type="text"  value="" size="22" />
				</td>
           </tr>
           <tr>
           		<th width=85px><pg:message code="sany.pdp.purviewmanager.rolemanager.role.type.description"/>：</th>
           		<td width=140px>
           			<textarea name="typedesc"  rows="5" ></textarea>
           		</td>
           </tr>
      </table>
       <div class="btnarea" >
       		<a href="javascript:void(0)" class="bt_1" name="Submit4" onclick="save()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
       		<!--  <a href="javascript:void(0)" class="bt_2" name="btn_close" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.close"/></span></a>-->
        </div> 
</form>
</div>
</body>
<iframe name="hiddenFrame" width="0" height="0" style="display:none"></iframe>
</html>
