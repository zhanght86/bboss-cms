<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%
	String currOrgId = (String) session.getAttribute("orgId");
	String userId = request.getParameter("userId");
	String reFlush = "true";
	if (request.getAttribute("reFlush") != null) {
		reFlush = "false";
	}
	String isUserExist = "false";
	if ( request.getAttribute("isUserExist") != null){
		isUserExist = "true";
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
	UserManager userManager = SecurityDatabase.getUserManager();
	User quser = userManager.getUserById(userId);
	if(quser.getRemark3()==null||quser.getRemark3().equals(""))
	quser.setRemark3("否");
%>

<html>
	<head>
		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script language="javascript" src="../scripts/selectTime.js"></script>
		<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>userInfo</title>
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
var reFlush = "<%=reFlush%>";
var userErr = 0;
	
var isUserExist = "<%=isUserExist%>";
if ( isUserExist == "true"){
	alert("登陆名已存在，请重新输入登陆名!");
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
	document.UserInfoForm.userName.disabled = false;
	
	if (validateForm(UserInfoForm) )
	{
		var userName= document.forms[0].userName.value;
		if (trim(userName).length == 0 ){
    	alert("请输入登陆名！"); 
    	return false;
    } 
    	if(document.UserInfoForm.remark3.checked)
    		document.UserInfoForm.remark3.value="是";
		else document.UserInfoForm.remark3.value="否";
   		//alert(document.UserInfoForm.remark3.value);
		document.UserInfoForm.action="../user/userManager.do?method=storeUser";
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
	document.location.href="../user/userManager.do?method=getUserList&orgId=<%=currOrgId%>";	
}

function resetuser()
{
	document.location.href="../user/userManager.do?method=getUser";	
}

function setDisable(v){
	v.disabled = false;
	if  ( document.UserInfoForm.userId.value != "" )
	{
		v.disabled = true;
	}
}

function window_onload() 
{
	notice.style.display="none";  
}

var http_request = false;
//初始化，指定处理的函数，发送请求的函数
function send_request(url){
	http_request = false;
	//开始初始化XMLHttpRequest对象
	if(window.XMLHttpRequest){//Mozilla
		http_request = new XMLHttpRequest();
		if(http_request.overrideMimeType){//设置MIME类别
			http_request.overrideMimeType("text/xml");						
		}
	}
	else if(window.ActiveXObject){//IE
		try{
			http_request = new ActiveXObject("Msxml2.XMLHTTP");
		}catch(e){
			try{
				http_request = new ActiveXObject("Microsoft.XMLHTTP");							
			}catch(e){
			}
		}
	}
	if(!http_request){
		alert("不能创建XMLHttpRequest对象");
		return false;
	}
	http_request.onreadystatechange = processRequest;
	http_request.open("GET",url,true);
	http_request.send(null);
}

function processRequest(){
	if(http_request.readyState == 4){
		if(http_request.status == 200){
			if(http_request.responseText == 0)
				//alert("用户名已存在");
				notice.style.display="block";  
			else{
				userErr = 1;
				notice.style.display="none";  
				}
			
		}
		else{
			alert("对不起，服务器错误");
		}
	}
}

function checkUser(){
	var userName = document.forms[0].userName.value;	
	send_request('checkUser.jsp?userName='+userName);
}


</script>

	<body class="info">

		<form name="userInfo" method="post" action="">
			
			
			<pg:beaninfo requestKey="currUser">
			<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1" class="thin">
				<tr>
					<td height="25" class="detailtitle"><strong> 登陆名</strong></td>					
			    	<td height="25">
					<input type="text" name="userName" onchange="checkUser()" value="<pg:cell colName="userName"  defaultValue=""/>" validator="string" cnname="登陆名" maxlength="200"  onfocus="setDisable(this)"></td>

					<td height="25" class="detailtitle"><strong> 真实名称</strong></td>
					<td height="25">
					<input type="text" name="userRealname" value="<pg:cell colName="userRealname"  defaultValue=""/>" validator="string" cnname="真实名称" maxlength="100"></td>
					
				</tr>			
					 
				<tr><td height="25" class="detailtitle"><strong> 口令</strong></td>
					<td height="25">
							<pg:equal colName="remark3" value="否"><input type="password" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue=""/>"></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="password" name="userIdcard" value="******"></pg:equal>
				  </td>
							<td height="25" class="detailtitle"><strong> 身份证号码</strong></td>
							<td height="25">
									<pg:equal colName="remark3" value="否"><input type="password" name="userIdcard" value="<pg:cell colName="userIdcard"  defaultValue=""/>" ></pg:equal>
									<pg:equal colName="remark3" value="是"><input type="password" name="userIdcard" value="******"></pg:equal>
							</td>
						
																  
				</tr>
				<tr><td height="25" class="detailtitle"><strong> 单位电话</strong></td>																			
					<td height="25">
							<pg:equal colName="remark3" value="否"><input type="text" name="userWorktel" value="<pg:cell colName="userWorktel"  defaultValue=""/>"></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="userWorktel" value="******"></pg:equal>						
					</td>						
					<td height="25" class="detailtitle"><strong> 性别</strong></td>
					<td height="25">					  
					    	<pg:equal colName="remark3" value="否"><dict:select type="sex" name="userSex" expression="{userSex}" textValue="--请选择性别--"/></pg:equal>
					    	<pg:equal colName="remark3" value="是"><input type="text" name="userSex" value="******"></pg:equal>				
					</td>
					
			   </tr>
			   <tr><td height="25" class="detailtitle"><strong> 家庭电话</strong></td>
					<td height="25">	
							<pg:equal colName="remark3" value="否"><input type="text" name="homePhone" value="<pg:cell colName="userHometel"  defaultValue=""/>" ></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="homePhone" value="******"></pg:equal>			
					</td>
					<td height="25" class="detailtitle"><strong> 电子邮件</strong></td>
					<td height="25">
							<pg:equal colName="remark3" value="否"><input type="text" name="mail" value="<pg:cell colName="userEmail"  defaultValue=""/>" ></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="mail" value="******"></pg:equal>			
					</td>
					
			  </tr>
			  <tr><td height="25" class="detailtitle"><strong> 移动电话1</strong></td>
				  <td height="25">
				   		    <pg:equal colName="remark3" value="否"><input type="text" name="mobile" value="<pg:cell colName="userMobiletel1"  defaultValue=""/>" ></pg:equal>
				   		    <pg:equal colName="remark3" value="是"><input type="text" name="mobile" value="******"></pg:equal>			
				  </td>
				  <td height="25" class="detailtitle"><strong>移动电话1归属地</strong></td>
				  <td height="25">
				            <pg:equal colName="remark3" value="否"><input type="text" name="remark4" value="<pg:cell colName="remark4"  defaultValue=""/>" ></pg:equal>
				            <pg:equal colName="remark3" value="是"><input type="text" name="remark4" value="******"></pg:equal>			
				  </td>
				 
			 </tr>
			 <tr><td height="25" class="detailtitle"><strong> 移动电话2</strong></td>
				 <td height="25">		
						    <pg:equal colName="remark3" value="否"><input type="text" name="userMobiletel2" value="<pg:cell colName="userMobiletel2"  defaultValue=""/>" ></pg:equal>
						    <pg:equal colName="remark3" value="是"><input type="text" name="userMobiletel2" value="******"></pg:equal>							
				 </td>
				 <td height="25" class="detailtitle"><strong> 移动电话2归属地</strong></td>
		    	 <td height="25">
							<pg:equal colName="remark3" value="否"><input type="text" name="remark5" value="<pg:cell colName="remark5"  defaultValue=""/>" ></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="remark5" value="******"></pg:equal>
				 </td>
				
			</tr>
			<tr>
		
				  <td height="25" class="detailtitle"><strong> 组织机构</strong></td>
				  <td height="25">
				            <pg:equal colName="remark3" value="否"><input type="text" name="ou" value="" ></pg:equal>
				            <pg:equal colName="remark3" value="是"><input type="text" name="ou" value="******"></pg:equal>			
				  </td>
                 <td height="25" class="detailtitle"><strong> 拼音</strong></td>
		    	 <td height="25">
							<pg:equal colName="remark3" value="否"><input type="text" name="userPinyin" value="<pg:cell colName="userPinyin"  defaultValue=""/>" ></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="userPinyin" value="******"></pg:equal>
				 </td>
				
			</tr>
			<tr><td height="25" class="detailtitle"><strong> 用户类型</strong></td>
				<td height="25">		
							<pg:equal colName="remark3" value="否"><dict:select type="userType" name="userType" expression="{userType}" textValue="--请选择用户类型--" /></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="userType" value="******"></pg:equal>				
				</td>
				
				<td height="25" class="detailtitle"><strong> 邮政编码</strong></td>
				<td height="25">
							<pg:equal colName="remark3" value="否"><input type="text" name="postalCode" value="<pg:cell colName="userPostalcode"  defaultValue=""/>" ></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="userPostalcode" value="******"></pg:equal>
				</td>
				
			</tr>
			<tr><td height="25" class="detailtitle"><strong> 传真</strong></td>
				<td height="25">
							<pg:equal colName="remark3" value="否"><input type="text" name="userFax" value="<pg:cell colName="userFax"  defaultValue=""/>" ></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="userFax" value="******"></pg:equal>
				</td>
				<td height="25" class="detailtitle"><strong> OICQ</strong></td>
				<td height="25">
							<pg:equal colName="remark3" value="否"><input type="text" name="userOicq" value="<pg:cell colName="userOicq"  defaultValue=""/>" ></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="userOicq" value="******"></pg:equal>
							
				</td>
				
			</tr>
		    <tr><td height="25" class="detailtitle"><strong> 生日</strong></td>						
				<td height="25">
							<pg:equal colName="remark3" value="否"><input type="text" name="userBirthday"  value="<pg:cell colName="userBirthday"  defaultValue=""  />" ></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="userBirthday" value="******"></pg:equal>
							
				</td>
				<td height="25" class="detailtitle"><strong> 用户地址</strong></td>
				<td height="25">
							<pg:equal colName="remark3" value="否"><input type="text" name="userAddress" value="<pg:cell colName="userAddress"  defaultValue=""/>" ></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="userAddress" value="******"></pg:equal>
							
				</td>
				
			</tr>
			<tr><td height="25" class="detailtitle"><strong> 登录次数</strong></td>
				<td height="25">
							<pg:equal colName="remark3" value="否"><input type="text" name="userLogincount" value="<pg:cell colName="userLogincount"  defaultValue="0"/>" ></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="userLogincount" value="******"></pg:equal>
							
				</td>
				<td height="25" class="detailtitle"><strong> 是否有效</strong></td>
				<td height="25">
							<pg:equal colName="remark3" value="否"><dict:select type="isvalid" name="userIsvalid" expression="{userIsvalid}" textValue="--请选择状态--" /></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="userIsvalid" value="******"></pg:equal>
				</td>
				
			</tr>
			<tr>
				<td height="25" class="detailtitle"><strong> 注册日期</strong></td>
				<td height="25">
							<pg:equal colName="remark3" value="否"><input type="text" name="userRegdate" value="<pg:cell colName="userRegdate"  defaultValue=""  />" ></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="userRegdate" value="******"></pg:equal>
				</td>
				<td height="25" class="detailtitle"><strong> 手机短号码</strong></td>
				<td height="25">
							<pg:equal colName="remark3" value="否"><input type="text" name="shortMobile" value="<pg:cell colName="remark2"  defaultValue=""/>"></pg:equal>
							<pg:equal colName="remark3" value="是"><input type="text" name="shortMobile" value="******"></pg:equal>
				</td>
				
			</tr>	
			<tr>
						<td height="25" class="detailtitle" colspan="4">
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr><td width='10%'>
						<input type="checkbox" name="remark3" <pg:equal colName="remark3" value="是">checked</pg:equal>>
						</td><td width='150px'>个人信息是否保密</td></tr>
						</table>
						</td>
    				
			 </tr>				
				<input type="hidden" name="userId" value="<pg:cell colName="userId"  defaultValue=""/>" />				
		    </table>
			</pg:beaninfo>			
		</form>
		<hr width="100%">
		<table width="100%" border="1" cellpadding="2" cellspacing="0" bordercolorlight="#3B4D61" bordercolordark="#ffffff">
			<tr bgcolor="#415A76">
				<td>
					<table width="35%" border="0" align="right" cellpadding="0" cellspacing="0">
						<tr>							
							<td>
								<div align="center">
									<input name="Submit32" type="submit" class="input" value="返回" onclick="goback()">
								</div>
							</td>							
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<%@ include file="../sysMsg.jsp"%>
	</body>
</html>