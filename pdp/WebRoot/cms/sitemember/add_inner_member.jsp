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
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	
	String reFlush = "true";
	if (request.getAttribute("reFlush") != null) {
		reFlush = "false";
	}
	
	String setDisbaled = "";
	if ( request.getAttribute("userNameDisable") != null ){
		setDisbaled = "disabled = true";
	}		
	
	UserInfoForm user = (UserInfoForm)request.getAttribute("currUser");
	
	if(user == null)
		user = new UserInfoForm();
		
	String isNew = (String)request.getAttribute("isNew");
	if(isNew == null ){
		isNew = "0";
	}
	String entpeach = (String)request.getAttribute("entpeach");
	String outtype="";
	if(entpeach.equals("entp")){
		outtype ="企业";
	}else{
		outtype ="个人";
	}
	
	String isaddorupdate = (String)request.getAttribute("isaddorupdate");
	
	if(isaddorupdate == null ){
		isaddorupdate = "1";
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
.style2 {
	font-size: 16px;
	font-weight: bold;
}
-->
        </style>
</head>

<script language="JavaScript">

var reFlush = "<%=reFlush%>";
var userErr = 0;



function trim(string){
  var temp="";
  string = ''+string;
  splitstring = string.split(" ");
  for(i=0;i<splitstring.length;i++){
    temp += splitstring[i];
  } 
  return temp;
 }
function storeUser()
{
	var UserInfoForm = document.all.UserInfoForm;
	if(UserInfoForm.userName.value.search(/\W/g)!=-1 ){
		alert("帐号只能为数字、字母和下划线!");
		UserInfoForm.userName.focus();
		return false;
	}
	

	if(UserInfoForm.userRealname.value.search(/[\\\/\|:\*\?<>"']/g)!=-1){
		alert("用户名称不能有\\/|:*?<>\"'!等特殊字符");
		UserInfoForm.userRealname.focus();
		return false;
	}
	document.UserInfoForm.userRealname.disabled = false;
	
	if (validateForm(UserInfoForm) )
	{
		var userRealname= document.forms[0].userRealname.value;
		if (trim(userRealname).length == 0 ){
    	alert("请输入用户名称！"); 
    	return false;
    } 
    	
   		
		document.UserInfoForm.action="../member/memberManager.do?method=storeInnerMember";
		document.UserInfoForm.submit();
	}
}
function updateUser()
{
	
	document.UserInfoForm.userName.disabled = false;
	
	if (validateForm(UserInfoForm) )
	{
		var userName= document.forms[0].userName.value;
		if (trim(userName).length == 0 ){
    	alert("请输入登陆名！"); 
    	return false;
    } 
    	
   		if (confirm("您确定要修改该用户信息吗？")){
		document.UserInfoForm.action="../member/memberManager.do?method=updateMember&flag=3";
		document.UserInfoForm.submit();
		}
	}
}
function defaultpass()//恢复用户初始密码
{
		var userId= document.forms[0].userId.value;
		if (confirm("您确定要恢复为初始密码吗？")){
			document.UserInfoForm.action="../member/memberManager.do?method=defaultpass&userName="+userId+"&flag=3";
			document.UserInfoForm.submit();
		}
}

function reloadUser(){
	document.UserInfoForm.submit();
}

function newUser()
{
	document.UserInfoForm.action="../user/userManager.do?method=newUser";
	document.UserInfoForm.submit();
}

function back()
{
	document.location.href="../member/memberManager.do?method=getMemberList&flag=3";	
}

function resetuser()
{
	var userId= document.forms[0].userId.value;
	document.location.href="../member/memberManager.do?method=updateMember&memberId="+userId;	
}

function setDisable(v){
	v.disabled = false;
	if  ( document.UserInfoForm.userId.value != "" )
	{
		v.disabled = true;
	}
}


//默认光标停留在文档标题输入框
	function document.onreadystatechange(){
		if (document.readyState!="complete") return;
		document.all.UserInfoForm.userName.focus();
	}
	
	function outmember()
	{
		document.location.href="../member/memberManager.do?method=newMember&flag=0";
	}
	function entpeach()
	{
		document.location.href="../member/memberManager.do?method=newMember&flag=1&entpeach=each";
	}
	
	</script>
	
	<body class="info"  onload="">
		<form   name="UserInfoForm" method="post">
		<pg:beaninfo requestKey="currUser">
		<input type="hidden" name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
		<input type="hidden" name="remark3" value="<pg:cell colName="remark3"  defaultValue=""/>" />
		<input type="hidden" name="shortMobile" value="<pg:cell colName="shortMobile"  defaultValue=""/>" />
		
		<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1"  align="center">
			 <tr height="35">
				<td colspan=4 height="25" class="detailtitle">
				  <div align="center">
			        <%if(isNew.equals("1")){%>
					  <span class="style2">新增内部用户</span>
			        <%}else{%>
					  <span class="style2">修改内部用户信息</span>
			        <%}%>
				    </div></td>
			</tr>	
			 <tr height="35">
				<td colspan=4 height="25" class="detailtitle">
				</td>
			</tr>	
			
			<tr>
				<td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle" width="23%"><strong>帐号:</strong></td>						
				<td colspan=3 height="25">
					<input type="text" style="width:160px" id="userName" name="userName" value="<pg:cell colName="userName"  defaultValue=""/>" validator="string" cnname="登陆名" maxlength="100" <%=setDisbaled%> onfocus="setDisable(this)"><span class="STYLE1">*</span>
				</td>
				
			</tr>
			
			<tr>
				<td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle" width="20%"><strong>用户名称:</strong></td>						
				<td colspan=3 height="25">
					<input type="text" style="width:160px" name="userRealname" value="<pg:cell colName="userRealname"  defaultValue=""/>" validator="string" cnname="单位名称" maxlength="100"><span class="STYLE1">*</span>
				</td>
				
			</tr>
			<tr>
				<td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle" width="20%"><strong>口令:</strong></td>						
				<td colspan=3 height="25">
					<input type="password" style="width:160px" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue="123456"/>" validator="string" cnname="口令" maxlength="40">
					<span class="STYLE1">*缺省口令为123456</span>
				</td>
				
				
			</tr>
			<tr>
				<td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle" width="20%"><strong>联系人:</strong></td>						
				<td colspan=3 height="25">
					<input type="text" style="width:160px" name="enterpriseLinkman" value="<pg:cell colName="enterpriseLinkman"  defaultValue=""/>">
					</td>
				
				
			</tr>
			<tr>
				<td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle" width="20%"><strong>联系电话:</strong></td>						
				<td colspan=3 height="25">
					<input type="text" style="width:160px" name="userWorktel" value="<pg:cell colName="userWorktel"  defaultValue=""/>"  cnname="单位电话" maxlength="13">
				</td>
				
				
			</tr>
			<tr>
				<td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle" width="20%"><strong>电子邮件:</strong></td>						
				<td colspan=3 height="25">
					<input type="text" style="width:160px" name="mail" value="<pg:cell colName="mail"  defaultValue=""/>" validator="emailNull" cnname="电子邮件" maxlength="40">
				</td>
				
			</tr>
			<tr>
				<td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle" width="20%"><strong>用户网址:</strong></td>						
				<td colspan=3 height="25">
						<input type="text" style="width:160px" name="network_address" value="<pg:cell colName="network_address"  defaultValue=""/>" >
				</td>
				
			</tr>
			<tr>
				<td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle">
					<strong> 忘记密码时问题:</strong>
				</td>
				<td colspan=3 height="25">
					<input type="text" style="width:160px" name="question" value="<pg:cell colName="question"  defaultValue=""/>" maxlength="100">
				</td>
				
			</tr>
			
			<tr>
				<td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle">
					<strong>忘记密码时答案:</strong>
				</td>
				<td colspan=3 height="25">
					<input type="text" style="width:160px" name="answer" value="<pg:cell colName="answer"  defaultValue=""/>" maxlength="40">
				</td>
				
			</tr>
			
			<tr>
				<td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle">
						<strong>联系地址:</strong>
				</td>
				<td  colspan=3 height="25">
						<input type="text" style="width:160px" name="userAddress" value="<pg:cell colName="userAddress"  defaultValue=""/>" >
				</td>
				
			</tr>
			
			
		  <tr>
		   		<td height="25" class="detailtitle" width="23%"></td>
				<td height="25" class="detailtitle">
					<strong>用户信息是否公开:</strong>
				</td>
				<td colspan=3 height="25">
					<dict:select type="enableopen" name="enableopen" expression="{enableopen}"/>								
				</td>
		 </tr>      
		 <tr>
		 	<td height="25" class="detailtitle" width="23%"></td>
			<td height="25" class="detailtitle">
				<strong>用户简介:</strong>
			</td>
			<td colspan=3 height="25">
				<textarea name="enterpriseIntro"   cols="40" rows="2" defaultValue=""><pg:cell colName="enterpriseIntro" defaultValue=""/></textarea>
			</td>
		</tr>
		 <tr>
		 	<td height="25" class="detailtitle" width="23%"></td>
			<td height="25" class="detailtitle">
				<strong>备注:</strong>
			</td>
			<td colspan=3 height="25">
				<textarea name="user_desc"   cols="40" rows="2" defaultValue=""><pg:cell colName="user_desc" defaultValue=""/></textarea>
			</td>
		</tr>			
				
					<input type="hidden" name="userId" value="<pg:cell colName="userId"  defaultValue=""/>" />
					
		</table>
		  </pg:beaninfo>
		</form>
		
		
		<table width="100%" cellpadding="2" cellspacing="0" align="center">
			<tr>
			
				<td align="center">
				
						<%
							//新增没有重置按钮
							if(isNew.equals("0")){
						%>
						<input name="update" type="button" class="input" value="修改" onClick="updateUser()">
						<input name="default" type="button" onClick="defaultpass()" class="input" value="密码恢复">
						<%
						}
						else {
						%>
						<input name="resave" type="button" class="input" value="保存" onClick="storeUser()">
						<%}%>
						<input name="Submit32" type="button" class="input" value="返回" onClick="back()">
					
				</td>
						
			</tr>
		</table>
		
	</body>
</html>