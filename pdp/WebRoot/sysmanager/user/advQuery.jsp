<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<html>
	<head>
		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script language="javascript" src="../scripts/selectTime.js"></script>
		<SCRIPT language="JavaScript" SRC="validateForm.js"></SCRIPT>
	
		<title>系统管理</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
	    <style type="text/css">
<!--
.STYLE1 {color: #FF0000}
-->
        </style>
</head>

<script language="JavaScript">
var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");



function queryUser()
{
	document.userInfo.action="../user/userManager.do?method=advQueryUser";
	document.userInfo.submit();	
}

function back()
{
	document.location.href="../user/userquery_content_tab.jsp";	
}

</script>

	<body class="info">

		<form name="userInfo" method="post" action="">		

				<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1" class="thin">
				
					<tr>
						<td height="25" class="detailtitle">
							<strong> 登陆名</strong>
						</td>
					  <td height="25">
						<input type="text" name="userName"  maxlength="200">					</td>
						<td height="25" class="detailtitle">
							<strong> 真实名称</strong>
						</td>
					  <td height="25">
						<input type="text" name="userRealname"  maxlength="100">						</td>
						
					</tr>					
					<tr>
						<td height="25" class="detailtitle">
							<strong> 单位电话</strong>
						</td>
					  <td height="25">
						<input type="text" name="userWorktel" maxlength="11"></td>
						<td height="25" class="detailtitle">
							<strong> 性别</strong>
						</td>
					  <td height="25">					  
					    <dict:select type="sex" textValue="--请选择性别--"  name="userSex" />						
						
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							<strong> 家庭电话</strong>
						</td>
						<td height="25">
							<input type="text" name="homePhone"  maxlength="11">
						</td>
						<td height="25" class="detailtitle">
							<strong> 电子邮件</strong>
						</td>
						<td height="25">
							<input type="text" name="mail"  validator="emailNull" cnname="电子邮件" maxlength="40">
						</td>
						
					</tr>

					<tr>
						<td height="25" class="detailtitle">
							<strong> 移动电话1</strong>
						</td>
						<td height="25">
							<input type="text" name="mobile"  validator="intNull" cnname="移动电话" maxlength="11">
						</td>
						<td height="25" class="detailtitle">
							<strong> 手机短号码</strong>
						</td>
						<td height="25">
							<input type="text" name="shortMobile" validator="stringNull"  cnname="手机短号码" maxlength="40">
						</td>
						
					</tr>
					
					<tr>
						<td height="25" class="detailtitle">
							<strong> 移动电话2</strong>
						</td>
						<td height="25">
							<input type="text" name="userMobiletel2"  validator="intNull" cnname="移动电话2" maxlength="11">
						</td>
						<td height="25" class="detailtitle">
							<strong> 拼音</strong>
						</td>
						<td height="25">
							<input type="text" name="userPinyin"  validator="stringNull" cnname="拼音" maxlength="100">
						</td>
						
					</tr>

					<tr>
						<td height="25" class="detailtitle">
							<strong> 用户类型</strong>
						</td>
						<td height="25">
							<dict:select type="userType" textValue="--请选择用户类型--" name="userType"  />
						</td>
						<td height="25" class="detailtitle">
							<strong> 邮政编码</strong>
						</td>
						<td height="25">
							<input type="text" name="postalCode"  validator="intNull" cnname="邮政编码" maxlength="7">
						</td>
						
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							<strong> 传真</strong>
						</td>
						<td height="25">
							<input type="text" name="userFax"  validator="stringNull" cnname="传真" maxlength="40">
						</td>
						<td height="25" class="detailtitle">
							<strong> OICQ</strong>
						</td>
						<td height="25">
							<input type="text" name="userOicq"  validator="intNull" cnname="OICQ" maxlength="13">
						</td>
						
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							<strong> 生日</strong>
						</td>
						<td height="25">
							<input type="text" name="userBirthday" onClick="selectTime('userInfo.userBirthday',1)" validator="stringNull" cnname="生日" maxlength="40">
						</td>
						<td height="25" class="detailtitle">
							<strong> 用户地址</strong>
						</td>
						<td height="25">
							<input type="text" name="userAddress" validator="stringNull" cnname="用户地址" maxlength="200">
						</td>
						
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							<strong> 登录次数</strong>
						</td>
						<td height="25">
							<input type="text" name="userLogincount"  validator="intNull"  cnname="登录次数" maxlength="40">
						</td>
						<td height="25" class="detailtitle">
							<strong> 是否有效</strong>
						</td>
						<td height="25">
						<dict:select type="isvalid"  textValue="--请选择状态--" name="userIsvalid" />
							
							
						</td>
						
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							<strong> 注册日期</strong>
						</td>
						<td height="25">
							<input type="text" name="userRegdate" onClick="selectTime('userInfo.userRegdate',1)"  validator="stringNull" cnname="注册日期" maxlength="40">
						</td>
						<td height="25" class="detailtitle">
							
						</td>
						<td height="25">
							<select name="userOrgType"> 
								<option selected value="hasMainOrg">有主机构用户</option> 
								<option value="noMainOrg">无主机构用户</option> 
								<option value="dis">离散用户</option>  
							</select>	
						</td>						
					</tr>					
		  </table>			
		</form>

		<hr width="98%">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<div align="center">
						<input name="qqq" type="button" class="input" value="查询" onClick="queryUser()">									
					</div>
				</td>							 
				<td>
					<div align="left">									
						<input name="bbb" type="submit" class="input" value="返回" onClick="back()">
					</div>
				</td>	
			</tr>
		</table>

	</body>
</html>

