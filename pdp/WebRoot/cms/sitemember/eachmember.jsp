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
	String isUserExist =(String)request.getAttribute("isUserExist");

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
-->
        </style>
</head>

<script language="JavaScript">

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
function storeUser()
{
	var UserInfoForm = document.all.UserInfoForm; 
	if(UserInfoForm.userName.value.search(/\W/g)!=-1){
		alert("用户名只能包含数字、字符和下划线!");
		UserInfoForm.userName.focus();
		return false;
	}
	document.UserInfoForm.userName.disabled = false;
	
	if (validateForm(UserInfoForm) )
	{
		var userName= document.forms[0].userName.value;
		if (trim(userName).length == 0 ){
    	alert("请输入登陆名！"); 
    	return false;
    } 
    	
   		
		document.UserInfoForm.action="../member/memberManager.do?method=storeMember";
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
    	
   		
		document.UserInfoForm.action="../member/memberManager.do?method=updateMember";
		document.UserInfoForm.submit();
	}
}
function defaultpass()//恢复用户初始密码
{
		var userId= document.forms[0].userId.value;
		if (confirm("您确定要恢复为初始密码吗？")){
			document.UserInfoForm.action="../member/memberManager.do?method=defaultpass&userName="+userId;
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
	document.location.href="../member/memberManager.do?method=getMemberList";	
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
		document.location.href="../member/memberManager.do?method=newMember&flag=1&entpeach=entp";
	}
</script>

	<body class="info"  onload="">
		<form  id="UserInfoForm" name="UserInfoForm" method="post">
		<pg:beaninfo requestKey="currUser">
		<input type="hidden" name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
		<input type="hidden" name="remark3" value="<pg:cell colName="remark3"  defaultValue=""/>" />
		<input type="hidden" name="shortMobile" value="<pg:cell colName="shortMobile"  defaultValue=""/>" />
		
		<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1" class="thin">
			<%if(isaddorupdate.equals("1")){%>
<!--			<tr>-->
<!--				<td height="25" class="detailtitle" width="23%"><strong>>>申请会员类型</strong></td>						-->
<!--				<td colspan=3 height="25">-->
<!--						<select name="userkind"  style="width:100px" onChange="outmember()">-->
<!--				         <option value="1" select>外部会员</option>-->
<!--				         <option value="0">内部会员</option>-->
<!--				        </select>-->
<!--				</td>-->
<!--				-->
<!--			</tr>-->
			<tr>
						<td height="25" class="detailtitle">
							<strong>>>选择外部会员类型</strong>
						</td>
						<td colspan=3 height="25">
								<select name="type"  style="width:100px" onChange="entpeach()">
						         <option value="1" <%if(entpeach.equals("each")){%>selected<%}%>>个人</option>
						         <option value="2" <%if(entpeach.equals("entp")){%>selected<%}%>>企业</option>
						        </select>
						</td>
					
			</tr>
			<%}%>
			<tr>
				<td height="25" class="detailtitle" width="23%"><strong>会员名称</strong></td>						
				<td height="25">
					<input type="text" id="userName" name="userName" value="<pg:cell colName="userName"  defaultValue=""/>" validator="string" cnname="登陆名" maxlength="100" <%=setDisbaled%> onfocus="setDisable(this)"><span class="STYLE1">*</span>
				</td>
				<td height="25" class="detailtitle" width="20%"><strong> 真实姓名</strong></td>						
				<td height="25">
					<input type="text" name="userRealname" value="<pg:cell colName="userRealname"  defaultValue=""/>" validator="string" cnname="真实名称" maxlength="100"><span class="STYLE1">*</span>	</td>
			</tr>
			
			<tr>						
				<td height="25" class="detailtitle"><strong> 口令</strong></td>
				<td height="25">
					<input type="password" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue="123456"/>" validator="string" cnname="口令" maxlength="40"><span class="STYLE1">*缺省口令为123456</span>
				</td>
				<td height="25" class="detailtitle"><strong> 身份证号码</strong></td>
					<td height="25">
					<input type="text" name="userIdcard" value="<pg:cell colName="userIdcard"  defaultValue=""/>" validator="stringNull" cnname="身份证号码" maxlength="40">
				</td>					
			</tr>
			
			<tr><td height="25" class="detailtitle"><strong> 单位电话</strong></td>						
				<td height="25">
					<input type="text" name="userWorktel" value="<pg:cell colName="userWorktel"  defaultValue=""/>"  cnname="单位电话" maxlength="13"></td>
				<td height="25" class="detailtitle"><strong> 性别</strong></td>
				<td height="25">					  					    
					    <dict:select style="width:100px" dictTypeName="sex" name="userSex" defaultValue="M" value="<%=user.getUserSex()%>"/>
				</tr>
			
			<tr><td height="25" class="detailtitle"><strong> 家庭电话</strong></td>
				<td height="25">
							<input type="text" name="homePhone" value="<pg:cell colName="homePhone"  defaultValue=""/>"  cnname="家庭电话" maxlength="13"></td>				
				<td height="25" class="detailtitle"><strong> 电子邮件</strong></td>
				<td height="25">
							<input type="text" name="mail" value="<pg:cell colName="mail"  defaultValue=""/>" validator="emailNull" cnname="电子邮件" maxlength="40"></td>
			</tr>
			
			<tr>
				<td height="25" class="detailtitle"><strong> 移动电话1</strong></td>
				<td height="25">
							<input type="text" name="mobile" value="<pg:cell colName="mobile"  defaultValue=""/>" cnname="移动电话" maxlength="13"></td>
				<td height="25" class="detailtitle"><strong> 移动电话1归属地</strong></td>
				<td height="25">
							<input type="text" name="remark4" value="<pg:cell colName="remark4"  defaultValue=""/>" validator="stringNull" cnname="移动电话1归属地" maxlength="100"></td>
			</tr>
			
			<tr><td height="25" class="detailtitle"><strong> 移动电话2</strong></td>
				<td height="25">
					<input type="text" name="userMobiletel2" value="<pg:cell colName="userMobiletel2"  defaultValue=""/>" cnname="移动电话2" maxlength="13"></td>
				<td height="25" class="detailtitle"><strong> 移动电话2归属地</strong></td>
				<td height="25">
							<input type="text" name="remark5" value="<pg:cell colName="remark5"  defaultValue=""/>" validator="stringNull" cnname="移动电话2归属地" maxlength="100"></td>
		   </tr>
		   <tr>
			   <td height="25" class="detailtitle">
			   		<strong> 过期时间</strong>
			   </td>
				<td height="25">
					<input type="text" name="pastTime" readonly="true" onClick="selectTime('UserInfoForm.pastTime',1)" value="<pg:cell colName="pastTime"  defaultValue=""  />" maxlength="40">
				</td>
		   <td height="25" class="detailtitle"><strong> 拼音</strong></td>
				<td height="25">
							<input type="text" name="userPinyin" value="<pg:cell colName="userPinyin"  defaultValue=""/>" validator="stringNull" cnname="拼音" maxlength="100"></td>
	       </tr>   
		   <tr>
						<td height="25" class="detailtitle">
							<strong> 会员类型</strong>
						</td>
			   <td height="25">
<!--							<dict:select style="width:100px" dictTypeName="userType" name="userType" defaultValue="1" value="<%=user.getUserType()%>" />-->
							<input type="userType" name="userType" value="个人"  readonly="true">  
				</td>						
			   <td height="25" class="detailtitle"><strong> 邮政编码</strong></td>
			   <td height="25">
							<input type="text" name="postalCode" value="<pg:cell colName="postalCode"  defaultValue=""/>" validator="intNull" cnname="邮政编码" maxlength="7"></td>	
		   </tr>
					<tr>
						<td height="25" class="detailtitle">
							<strong> 传真</strong>
						</td>
						<td height="25">
							<input type="text" name="userFax" value="<pg:cell colName="userFax"  defaultValue=""/>" validator="stringNull" cnname="传真" maxlength="40">
						</td>
						<td height="25" class="detailtitle">
							<strong> OICQ</strong>
						</td>
						<td height="25">
							<input type="text" name="userOicq" value="<pg:cell colName="userOicq"  defaultValue=""/>" validator="intNull" cnname="OICQ" maxlength="13">
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							<strong> 生日</strong>
						</td>
						<td height="25">
							<input type="text" name="userBirthday" readonly="true" onClick="selectTime('UserInfoForm.userBirthday',1)" value="<pg:cell colName="userBirthday"  defaultValue=""  />" validator="stringNull" cnname="生日" maxlength="40">
						</td>
						<td height="25" class="detailtitle">
							<strong>排序号</strong>
						</td>
						<td height="25">
							<input type="text" name="userSn" value="<pg:cell colName="userSn"  defaultValue="" />" validator="stringNull" cnname="用户排序号" maxlength="40">
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							<strong> 登录次数</strong>
						</td>
						<td height="25">
							<input type="text" name="userLogincount" value="<pg:cell colName="userLogincount"  defaultValue="0"/>" validator="intNull" readonly cnname="登录次数" maxlength="40">
						</td>
						<td height="25" class="detailtitle">
							<strong> 是否有效</strong>
						</td>
						<td height="25">
							<dict:select style="width:100px" dictTypeName="isvalid" name="userIsvalid" defaultValue="1" value="<%=String.valueOf(user.getUserIsvalid())%>"/>							
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							<strong> 注册日期</strong>
						</td>
						<td height="25">
							<input type="text" name="userRegdate" readonly="true" onClick="selectTime('UserInfoForm.userRegdate',1)" value="<pg:cell colName="userRegdate"  defaultValue="" />" validator="stringNull" cnname="注册日期" maxlength="40">
						</td>
						<td height="25" class="detailtitle">
							<strong>是否公开联系方式和资料</strong>
						</td>
						<td height="25">
							<dict:select style="width:100px" dictTypeName="enableopen" name="enableopen" defaultValue="1" value="<%=user.getEnableopen()%>"/>							
						</td>
					</tr>
					
				
					<tr>
						<td height="25" class="detailtitle">
							<strong> 密码取回问题</strong>
						</td>
						<td colspan=3 height="25">
							<textarea name="question"  cols="40" rows="2" defaultValue=""><pg:cell colName="question" defaultValue=""/></textarea>
						</td>
						
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							<strong>密码取回答案</strong>
						</td>
						<td colspan=3 height="25">
							<textarea name="answer"   cols="40" rows="2" defaultValue=""><pg:cell colName="answer" defaultValue=""/></textarea>
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							<strong> &nbsp;地址</strong>
						</td>
						<td colspan=3 height="25">
							<textarea name="userAddress"   cols="40" rows="2" defaultValue=""><pg:cell colName="userAddress" defaultValue=""/></textarea>
						</td>
					</tr>
				
					<input type="hidden" name="userId" value="<pg:cell colName="userId"  defaultValue=""/>" />
					
		</table>
		  </pg:beaninfo>
		</form>
		
		<hr width="100%">
		<table width="100%" cellpadding="2" cellspacing="0" >
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
								</div>
							</td>
						
							<td>
								<div align="center">
									<input name="Submit32" type="submit" class="input" value="返回" onClick="back()">
								</div>
							</td>
						
          				</tr>
					</table>
				</td>
			</tr>
		</table>
		
	</body>
</html>