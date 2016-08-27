<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="pager-taglib" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@page import="com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%
	UserInfoForm user = (UserInfoForm)request.getAttribute("currUser");
	if(user == null)
		user = new UserInfoForm();
		String rootpath = request.getContextPath();
%>
<html><head>
<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
<script language="JavaScript">
function storeUser()
{

	if (validateForm(UserInfoForm))
	{
		document.UserInfoForm.userSex.disabled=false;
		document.UserInfoForm.userType.disabled=false;
   		document.UserInfoForm.userIsvalid.disabled=false;
		document.UserInfoForm.action="<%=rootpath%>/sysmanager/user/userManager.do?method=storeUser&person=person";
		document.UserInfoForm.submit();
	}
}
</SCRIPT>
<title>个人详细信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><link type="text/css" rel="StyleSheet" href="<%=rootpath%>/default.css">
<link type="text/css" rel="StyleSheet" href="<%=rootpath%>/css/auto-input.css">   
</head>
<body  ENCTYPE="multipart/form-data"  >
<form method="POST" name="UserInfoForm"  >


<pg:beaninfo requestKey="currUser">
<input type="hidden" name="userId" value="<pg:cell colName="userId"  defaultValue=""/>" />
<table border="0" align="center" width="100%" cellpadding="0" cellspacing="0">
<tr>
<td width="100%" align="left" ></td>
</tr>
<tr>
<td width="100%" align="left" ><table border='0' align="left" width="100%" cellpadding='0' cellspacing='0'>
<tr><td><table border='0' align="left" width="100%" cellpadding='0' cellspacing='0'>
  <tr height='24px'>    <td width='100%' height='27px' background='<%=rootpath%>/images/oa_r7_c7.jpg' class='form_title'>
&nbsp;&nbsp;个人详细信息</td>
</table></td></tr>
<tr><td><table width="100%" align="left" class="form_body" cellspacing="0" cellpadding="0" >
<tr>
<td width="100%" align="left" ><table width="100%" align="left"   cellpadding="0" cellspacing="0" >

<tr>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>登陆名</div></td><td width='*'><input type="text" name="userName" value="<pg:cell colName="userName"  defaultValue=""/>"     class="text" readonly="true" labelClass="label" style="width:95%" ></td></tr></table></td>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>真实名称</div></td><td width='*'><input type="text" name="userRealname" value="<pg:cell colName="userRealname"  defaultValue=""/>"     class="text" readonly="true" labelClass="label" style="width:95%" onlyDate="false" ></td></tr></table></td>
</tr>

<tr>
<td width="50%" align="left" >&nbsp;</td>
<td width="50%" align="left" >&nbsp;</td>
</tr>

<tr>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>口令</div></td><td width='*'><input type="password" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue=""/>"     class="text" readonly="true" labelClass="label" style="width:95%" ></td></tr></table></td>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>身份证号码</div></td><td width='*'><input type="password" name="userIdcard" value="<pg:cell colName="userIdcard"  defaultValue=""/>"     class="text"  labelClass="label" style="width:95%" ></td></tr></table></td>
</tr>

<tr>
<td width="50%" align="left" >&nbsp;</td>
<td width="50%" align="left" >&nbsp;</td>
</tr>

<tr>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>单位电话</div></td><td width='*'><input type="password" name="userWorktel" value="<pg:cell colName="userWorktel"  defaultValue=""/>"     class="text" labelClass="label" style="width:95%" ></td></tr></table></td>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>性别</div></td><td width='*'> <input type="password" name="userSex" value="<pg:cell colName="userSex"  defaultValue=""/>"     class="text" labelClass="label" style="width:95%" ></td></tr></table></td>
</tr>

<tr>
<td width="50%" align="left" >&nbsp;</td>
<td width="50%" align="left" >&nbsp;</td>
</tr>

<tr>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>家庭电话</div></td><td width='*'><input type="password" name="homePhone" value="<pg:cell colName="homePhone"  defaultValue=""/>"     class="text" labelClass="label" style="width:95%" ></td></tr></table></td>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>电子邮件</div></td><td width='*'><input type="password" name="mail" value="<pg:cell colName="mail"  defaultValue=""/>"     class="text" readonly="true" labelClass="label" style="width:95%" ></td></tr></table></td>
</tr>

<tr>
<td width="50%" align="left" >&nbsp;</td>
<td width="50%" align="left" >&nbsp;</td>
</tr>

<tr>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>移动电话1</div></td><td width='*'><input type="password" name="mobile" value="<pg:cell colName="mobile"  defaultValue=""/>"     class="text"  labelClass="label" style="width:95%" ></td></tr></table></td>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>组织机构</div></td><td width='*'><input type="password" name="ou" value="<pg:cell colName="ou"  defaultValue=""/>"     class="text" readonly="true" labelClass="label" style="width:95%" ></td></tr></table></td>
</tr>

<tr>
<td width="50%" align="left" >&nbsp;</td>
<td width="50%" align="left" >&nbsp;</td>
</tr>

<tr>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>移动电话2</div></td><td width='*'><input type="password" name="userMobiletel2" value="<pg:cell colName="userMobiletel2"  defaultValue=""/>"     class="text" labelClass="label" style="width:95%" ></td></tr></table></td>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>拼音</div></td><td width='*'><input type="passwordt" name="userPinyin" value="<pg:cell colName="userPinyin"  defaultValue=""/>"     class="text" readonly="true" labelClass="label" style="width:95%" ></td></tr></table></td>
</tr>

<tr>
<td width="50%" align="left" >&nbsp;</td>
<td width="50%" align="left" >&nbsp;</td>
</tr>



<tr>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>用户类型</div></td><td width='*'><input type="password" name="userType" value="<pg:cell colName="userType"  defaultValue=""/>"     class="text"  labelClass="label" style="width:95%" ></td></tr></table></td>

<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>邮政编码</div></td><td width='*'><input type="password" name="postalCode" value="<pg:cell colName="postalCode"  defaultValue=""/>"     class="text"  labelClass="label" style="width:95%" ></td></tr></table></td>
</tr>

<tr>
<td width="50%" align="left" >&nbsp;</td>
<td width="50%" align="left" >&nbsp;</td>
</tr>

<tr>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>传真</div></td><td width='*'><input type="password" name="userFax" value="<pg:cell colName="userFax"  defaultValue=""/>"     class="text"  labelClass="label" style="width:95%" ></td></tr></table></td>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>OICQ</div></td><td width='*'><input type="password" name="userOicq" value="<pg:cell colName="userOicq"  defaultValue=""/>"     class="text"  labelClass="label" style="width:95%" ></td></tr></table></td>
</tr>

<tr>
<td width="50%" align="left" >&nbsp;</td>
<td width="50%" align="left" >&nbsp;</td>
</tr>

<tr>
<td width="50%" align="left" >&nbsp;</td>
<td width="50%" align="left" >&nbsp;</td>
</tr>

<tr>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>生日</div></td><td width='*'><input type="password" name="userBirthday" value="<pg:cell colName="userBirthday"  defaultValue=""/>"     class="text" readonly="true" labelClass="label" style="width:95%" ></td></tr></table></td>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>用户地址</div></td><td width='*'><input type="password" name="userAddress" value="<pg:cell colName="userAddress"  defaultValue=""/>"     class="text"  labelClass="label" style="width:95%" ></td></tr></table></td>
</tr>

<tr>
<td width="50%" align="left" >&nbsp;</td>
<td width="50%" align="left" >&nbsp;</td>
</tr>

<tr>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>登录次数</div></td><td width='*'><input type="password" name="userLogincount" value="<pg:cell colName="userLogincount"  defaultValue=""/>"     class="text" readonly="true" labelClass="label" style="width:95%" ></td></tr></table></td>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>是否有效</div></td><td width='*'><input type="password" name="userIsvalid" value="<pg:cell colName="userIsvalid"  defaultValue=""/>"     class="text" readonly="true" labelClass="label" style="width:95%" ></td></tr></table></td>

</tr>

<tr>
<td width="50%" align="left" >&nbsp;</td>
<td width="50%" align="left" >&nbsp;</td>
</tr>

<tr>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>注册日期</div></td><td width='*'><input type="password" name="userRegdate" value="<pg:cell colName="userRegdate"  defaultValue=""/>"     class="text" readonly="true" labelClass="label" style="width:95%" ></td></tr></table></td>
<td width="50%" align="left" ><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td width='90px'><div class='label'>手机短号码</div></td><td width='*'><input type="password" name="shortMobile" value="<pg:cell colName="shortMobile"  defaultValue=""/>"     class="text" readonly="true" labelClass="label" style="width:95%" ></td></tr></table></td>
</tr>

<tr>
<td width="50%" align="left" >&nbsp;</td>
<td width="50%" align="left" >&nbsp;</td>
</tr>
</table>
</td>
</tr>

</table>
</td></tr></table>
</td>
</tr>
</table>
<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0">
</table>
	</pg:beaninfo>
</form>
</body>

</html>

