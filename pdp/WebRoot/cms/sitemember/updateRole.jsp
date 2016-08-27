<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(pageContext);
	String reFlush = "true";
	session.setAttribute("roleTabId","1");
	if (request.getAttribute("reFlush") != null) {
		reFlush = "false";
	}
	String isUserExist =(String)request.getAttribute("isUserExist");

	String setDisbaled = "";
	if ( request.getAttribute("userNameDisable") != null ){
		setDisbaled = "disabled = true";
	}		
	
	RoleManagerForm user = (RoleManagerForm)request.getAttribute("currUser");
	
	if(user == null)
		user = new RoleManagerForm();
		
	String isNew = (String)request.getAttribute("isNew");
	if(isNew == null ){
		isNew = "1";
	}
	
%>

<html>
	<head>
		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script language="javascript" src="../../sysmanager/scripts/selectTime.js"></script>
		<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>userInfo</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">
	    <style type="text/css">
<!--
.STYLE1 {color: #FF0000}
-->
        </style>
</head>

<script language="JavaScript">
var isNew ="<%=isNew%>";

if(isNew == "1" ){
	getNavigatorContent().location.href ="../sitemember/navigator_content.jsp?anchor=0&expand=0&request_scope=session"; 
}


var reFlush = "<%=reFlush%>";
var userErr = 0;
	
var isUserExist = "<%=isUserExist%>";
if ( isUserExist == "true"){
	alert("会员名称已存在，请重新输入会员名称!");
}

function trim(string){
  var temp="";
  string = ''+string;
  splitstring = string.split(" ");
  for(i=0;i<splitstring.length;i++){
    temp += splitstring[i];
  } 
  return temp;
 }
function storeRole()
{
	var RoleManagerForm = document.all.RoleManagerForm; 
	if(RoleManagerForm.roleName.value.search(/[*\|:<>\?"]/g)!=-1){
		alert("用户名只能包含数字、字符和下划线!");
		RoleManagerForm.roleName.focus();
		return false;
	}
	document.RoleManagerForm.roleName.disabled = false;
	
	if (validateForm(RoleManagerForm) )
	{
		var roleName= document.forms[0].roleName.value;
		if (trim(roleName).length == 0 ){
    	alert("请输入角色名！"); 
    	return false;
    } 
    	
   		
		document.RoleManagerForm.action="../memberManager/roleManager.do?method=storeRole";
		document.RoleManagerForm.submit();
	}
}
//修改
function updateRole()
{
	if(RoleManagerForm.roleName.value.search(/[*\|:<>\?"]/g)!=-1){
		alert("用户名只能包含数字、字符和下划线!");
		RoleManagerForm.roleName.focus();
		return false;
	}	
	document.RoleManagerForm.roleName.disabled = false;
	
	if (validateForm(RoleManagerForm) )
	{
		var roleName= document.forms[0].roleName.value;
		if (trim(roleName).length == 0 ){
    	alert("请输入角色名！"); 
    	return false;
    } 
    	
   		
		document.RoleManagerForm.action="../memberManager/roleManager.do?method=updateRole&flag=1";
		document.RoleManagerForm.submit();
	}
}
//删除
function deleteRole()
{
	var roleId= document.forms[0].roleId.value;
    if(roleId=="1"){
    	alert("此角色是系统管理员，不能删除！");
    	return;
    }
    if (confirm("您确定删除角色吗？（删除后是不能恢复的）")){
						
		document.RoleManagerForm.action="../memberManager/roleManager.do?method=deleteRoleByRoleId&flag=1&roleId="+roleId;
		document.RoleManagerForm.submit();
		return true;

	}
}


//返回
function back()
{
	if(flag =="1")
	{
		document.location.href="../sitemember/newRole.jsp";
	}
	else
	{
		document.location.href="../sitemember/drugRole.jsp";
	}
}
//重置
function resetuser()
{
	var roleId= document.forms[0].roleId.value;
	document.location.href="../memberManager/roleManager.do?method=getRole&flag=1&roleId="+roleId;
}

function setDisable(v){
	v.disabled = false;
	if  ( document.RoleManagerForm.roleId.value == "1" )
	{
		v.disabled = true;
	}
}


//默认光标停留在角色名称输入框
	function document.onreadystatechange(){
		if (document.readyState!="complete") return;
		document.all.RoleManagerForm.roleName.focus();
	}
</script>

	<body class="info"  onload="">
		<form  id="RoleManagerForm" name="RoleManagerForm" method="post">
		<pg:beaninfo requestKey="currUser">
		<input type="hidden" name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
		<input type="hidden" name="remark2" value="<pg:cell colName="remark2"  defaultValue=""/>" />
		<input type="hidden" name="remark3" value="<pg:cell colName="remark3"  defaultValue=""/>" />
		<input type="hidden" name="roleType" value="<pg:cell colName="roleType"  defaultValue=""/>" />
		<input type="hidden" name="roleUsage" value="<pg:cell colName="roleUsage"  defaultValue=""/>" />
		<input type="hidden" name="roleId" value="<pg:cell colName="roleId"  defaultValue=""/>" />
		<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1" class="thin">
			<tr>
				<td height="25" class="detailtitle" width="23%"><strong>角色名称</strong></td>						
				<td height="25">
					<input type="text" id="roleName" name="roleName" style="width:200px" value="<pg:cell colName="roleName"  defaultValue=""/>" validator="string" cnname="角色名称" maxlength="100" <%=setDisbaled%> onfocus="setDisable(this)"><span class="STYLE1">*</span>
				</td>
			</tr>
			
			<tr>						
				<td height="25" class="detailtitle"><strong>角色描述</strong></td>
				<td height="25">
					<textarea name="roleDesc"  style="width:200px" rows="4" defaultValue=""><pg:cell colName="roleDesc" defaultValue=""/></textarea>
				</td>
								
			</tr>
		</table>
		</pg:beaninfo>
		</form>
	
		<hr width="100%">
		<table width="100%" cellpadding="2" cellspacing="0" bordercolorlight="#3B4D61" bordercolordark="#ffffff">
			<tr>
				<td>
					<table width="35%" border="0" align="right" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<div align="center">
																		
								</div>
							</td>
							 
							<td>
								<div align="center">
									<input name="Submit31" type="submit" class="input" value="新增" onClick="back()">
									<input name="update" type="button" class="input" value="修改" onClick="updateRole()">
									<input name="delete" type="button" class="input" value="删除" onClick="deleteRole()">
									<input name="calc" type="button" onClick="resetuser()" class="input" value="重置">
								</div>
							</td>
						
							<td>
								<div align="center">
								
								</div>
							</td>
						
          				</tr>
					</table>
				</td>
			</tr>
		</table>
		
	</body>
</html>