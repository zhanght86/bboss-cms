<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>

<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,
				com.frameworkset.platform.sysmgrcore.web.struts.form.*,
				com.frameworkset.platform.config.ConfigManager,
				com.frameworkset.platform.sysmgrcore.manager.OrgManager,
				com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				java.util.Date,
				java.text.SimpleDateFormat"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
    //获取当前系统的时间 并格式化,为注册用户添加时间
    //baowen.liu 2008-4-25
	 java.util.Date date=new java.util.Date();
	 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	  String datestr = sdf.format(date);
	  
	String currOrgId = (String) request.getParameter("orgId");
	session.setAttribute("orgId",currOrgId);
	
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
	
	//用户登录名的长度--order by gao.tang
	String userNamelength = ConfigManager.getInstance().getConfigValue("userNameLength");
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	String orgName = "";
	if(orgManager.getOrgById(currOrgId).getRemark5() != null){
		orgName = orgManager.getOrgById(currOrgId).getRemark5();
	}else{
		orgName = orgManager.getOrgById(currOrgId).getOrgName();
	}
	
%>

<html>
	<head>
		<script language="JavaScript" src="<%=request.getContextPath()%>/public/datetime/calender_date.js" ></script>
		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script language="javascript" src="../../sysmanager/scripts/selectTime.js"></script>
		<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
		
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>新增用户</title>
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">
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
	
	if(document.UserInfoForm.userName.value.search(/\W/g)!=-1 ){
		alert("登陆名只能为数字、字母和下划线!");
		document.UserInfoForm.userName.focus();
		return false;
	}
	if (validateForm(UserInfoForm))
	{
		var userName= document.forms[0].userName.value;
		if (trim(userName).length == 0 ){
    	alert("请输入登陆名！"); 
    	return false;
    } 
    	if(document.UserInfoForm.remark3.checked)
    		document.UserInfoForm.remark3.value="是";
		else document.UserInfoForm.remark3.value="否";
   		document.UserInfoForm.target = "adduser";
		document.UserInfoForm.action = "../../sysmanager/user/userManager.do?method=storeUser&flag=2";
		document.UserInfoForm.submit();
	}
}
function defaultpass()//恢复用户初始密码
{
		var userId= document.forms[0].userId.value;
		//alert(userId);
		document.UserInfoForm.action="../../sysmanager/user/userManager.do?method=defaultpass&userName="+userId;
		document.UserInfoForm.submit();
	
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

	<body class="info"  onload="window_onload()">
		<form name="UserInfoForm" method="post">
		<pg:beaninfo requestKey="currUser">
		<input type="hidden" name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
		<input type="hidden" name="addcmsuser" value="addcmsuser" /><!--用于标识CMS中的用户管理-->
		<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1" class="thin">
			<tr>
				<td height="25" class="detailtitle" width="23%"> 登陆名</td>						
				<td height="25">
					<input type="text" name="userName" onchange="checkUser()" value="<pg:cell colName="userName"  defaultValue=""/>" validator="string" cnname="登陆名" maxlength="<%=userNamelength%>" <%=setDisbaled%> onfocus="setDisable(this)"><span class="STYLE1">*</span><div id=notice class="STYLE1">用户名已存在</div>						</td>
				<td height="25" class="detailtitle" width="20%"> 真实名称</td>						
				<td height="25">
					<input type="text" name="userRealname" value="<pg:cell colName="userRealname"  defaultValue=""/>" validator="string" cnname="真实名称" maxlength="100"><span class="STYLE1">*</span>	</td>
				</tr>
			
			<tr>						
				<td height="25" class="detailtitle"> 口令</td>
				<td height="25">
					<input type="password" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue="123456"/>" validator="string" cnname="口令" maxlength="40"><span class="STYLE1">*缺省口令为123456</span>
				</td>
				<td height="25" class="detailtitle"> 身份证号码</td>
					<td height="25">
					<input type="text" name="userIdcard" value="<pg:cell colName="userIdcard"  defaultValue=""/>" validator="intNull" cnname="身份证号码" maxlength="18">
				</td>					
			</tr>
			
			<tr><td height="25" class="detailtitle"> 单位电话</td>						
				<td height="25">
					<input type="text" name="userWorktel" value="<pg:cell colName="userWorktel"  defaultValue=""/>"  validator="intNull" cnname="单位电话" maxlength="13"></td>
				<td height="25" class="detailtitle"> 性别</td>
				<td height="25">					  					    
					    <dict:select type="sex" name="userSex" expression="{userSex}" />
				</tr>
			
			<tr><td height="25" class="detailtitle"> 家庭电话</td>
				<td height="25">
							<input type="text" name="homePhone" value="<pg:cell colName="homePhone"  defaultValue=""/>"  validator="intNull" cnname="家庭电话" maxlength="13"></td>				
				<td height="25" class="detailtitle"> 电子邮件</td>
				<td height="25">
							<input type="text" name="mail" value="<pg:cell colName="mail"  defaultValue=""/>" validator="emailNull" cnname="电子邮件" maxlength="40"></td>
			</tr>
			
			<tr>
				<td height="25" class="detailtitle"> 移动电话1</td>
				<td height="25">
							<input type="text" name="mobile" value="<pg:cell colName="mobile"  defaultValue=""/>" validator="intNull" cnname="移动电话" maxlength="13"></td>
				<td height="25" class="detailtitle"> 移动电话1归属地</td>
				<td height="25">
							<input type="text" name="remark4" value="<pg:cell colName="remark4"  defaultValue=""/>" validator="stringNull" cnname="移动电话1归属地" maxlength="100"></td>
			</tr>
			
			<tr><td height="25" class="detailtitle"> 移动电话2</td>
				<td height="25">
					<input type="text" name="userMobiletel2" value="<pg:cell colName="userMobiletel2"  defaultValue=""/>" validator="intNull" cnname="移动电话2" maxlength="13"></td>
				<td height="25" class="detailtitle"> 移动电话2归属地</td>
				<td height="25">
							<input type="text" name="remark5" value="<pg:cell colName="remark5"  defaultValue=""/>" validator="stringNull" cnname="移动电话2归属地" maxlength="100"></td>
		   </tr>
		   <tr>
		   <td height="25" class="detailtitle"> 组织机构</td>
				<td height="25">
					<input type="hidden" name="ou" value="<pg:cell colName="ou"  defaultValue=""/>" validator="stringNull" cnname="组织机构" maxlength="100">
					<input type="text" value="<%=orgName%>" disabled="true">
				</td>
		   <td height="25" class="detailtitle"> 拼音</td>
				<td height="25">
							<input type="text" name="userPinyin" value="<pg:cell colName="userPinyin"  defaultValue=""/>" validator="stringNull" cnname="拼音" maxlength="100"></td>
	       </tr>   
		   <tr><td height="25" class="detailtitle"> 用户类型</td>
			   <td height="25">
							<dict:select type="userType" name="userType" expression="{userType}" /></td>						
			   <td height="25" class="detailtitle"> 邮政编码</td>
			   <td height="25">
							<input type="text" name="postalCode" value="<pg:cell colName="postalCode"  defaultValue=""/>" validator="intNull" cnname="邮政编码" maxlength="7"></td>	
		   </tr>
					<tr>
						<td height="25" class="detailtitle">
							 传真
						</td>
						<td height="25">
							<input type="text" name="userFax" value="<pg:cell colName="userFax"  defaultValue=""/>" validator="stringNull" cnname="传真" maxlength="40">
						</td>
						<td height="25" class="detailtitle">
							 OICQ
						</td>
						<td height="25">
							<input type="text" name="userOicq" value="<pg:cell colName="userOicq"  defaultValue=""/>" validator="intNull" cnname="OICQ" maxlength="13">
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							 生日
						</td>
						<td height="25">
							<input type="text" name="userBirthday" onclick="showdate(document.all('userBirthday'))" readonly="true" value="<pg:cell colName="userBirthday"  defaultValue=""  />" validator="stringNull" cnname="生日" maxlength="40">
						</td>
						<td height="25" class="detailtitle">
							 用户地址
						</td>
						<td height="25">
							<input type="text" name="userAddress" value="<pg:cell colName="userAddress"  defaultValue=""/>" validator="stringNull" cnname="用户地址" maxlength="200">
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							 登录次数
						</td>
						<td height="25">
							<input type="text" name="userLogincount" value="<pg:cell colName="userLogincount"  defaultValue="0"/>" validator="intNull" readonly cnname="登录次数" maxlength="40">
						</td>
						<td height="25" class="detailtitle">
							 是否有效
						</td>
						<td height="25">
							<dict:select type="isvalid" name="userIsvalid" expression="{userIsvalid}" />							
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							 注册日期
						</td>
						<td height="25">
							<input type="text" name="userRegdate" readonly="true" value="<%=datestr%>" validator="stringNull" cnname="注册日期" maxlength="40">
						</td>
						<td height="25" class="detailtitle">
							 手机短号码
						</td>
						<td height="25">
							<input type="text" name="shortMobile" value="<pg:cell colName="shortMobile"  defaultValue=""/>" validator="stringNull"  cnname="手机短号码" maxlength="40">
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">
							 用户排序号</strong>
						</td>
						<td height="25">
							<input type="text" name="userSn" value="<pg:cell colName="userSn"  defaultValue="" />" validator="stringNull" cnname="用户排序号" maxlength="40">
						</td>
						<td height="25" class="detailtitle" colspan="2">
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr><td width='10%'>
						<input type="checkbox" name="remark3" <pg:equal colName="remark3" value="是">checked</pg:equal>>
						</td><td width='150px'>个人信息是否保密</td></tr>
						</table>
						</td>
    				
					</tr>
										
					<input type="hidden" name="userId" value="<pg:cell colName="userId"  defaultValue=""/>" />
					<input name="orgId" value="<%=currOrgId%>" type="hidden" height="0">
		</table>
		  </pg:beaninfo>
		</form>

		<hr width="100%">
		<table width="100%"  cellpadding="2" cellspacing="0" >
			<tr>
				<td>
					<table width="35%" border="0" align="right" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<div align="center">
									<input name="resave" type="button" class="input" value="保存" onClick="storeUser()">	
									<input name="Submit32" type="button" class="input" value="返回" onClick="window.close();">
								</div>
							</td>
						
          		</tr>
					</table>
				</td>
			</tr>
		</table>
		<iframe name="adduser" width="0" height="0"></iframe>
	</body>
</html>