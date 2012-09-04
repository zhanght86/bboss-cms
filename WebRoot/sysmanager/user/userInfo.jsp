<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.purviewmanager.GenerateServiceFactory"%><%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>

<%@page import="com.frameworkset.platform.sysmgrcore.orgmanager.OrgManAction,com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@ page import="com.frameworkset.platform.security.*,com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager,
				 com.frameworkset.platform.sysmgrcore.manager.LogManager,
				 com.frameworkset.platform.sysmgrcore.manager.UserManager,
				 com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				 com.frameworkset.platform.config.ConfigManager,
				 com.frameworkset.platform.synchronize.httpclient.ApachePostMethodClient,
				 java.sql.Date"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%

		AccessControl accesscontroler = AccessControl.getInstance();
	    accesscontroler.checkAccess(request, response);
	    //用户登录名的长度--order by gao.tang
	    String userNamelength = ConfigManager.getInstance().getConfigValue("userNameLength");
	    //用户登录名称是否自动生成
	    //boolean isAutoCreateUserName = ConfigManager.getInstance().getConfigBooleanValue("isAutoCreateUserName");
	    
	    String currOrgId =  request.getParameter("orgId");
		if(currOrgId == null)
			currOrgId = (String)request.getAttribute("orgId");
		Organization org = OrgCacheManager.getInstance().getOrganization(currOrgId);
	    //action start
	    if("true".equals(request.getParameter("isNewUser"))){
	    	//新建用户初始化action
		    request.setAttribute("currUser", null);
			request.setAttribute("reFlush", "false");
			request.getSession().setAttribute("currUserId", null);
			request.setAttribute("isNew", "1");
		}
		//是否自动生成登陆名
		boolean isAutoUserName = GenerateServiceFactory.getGenerateService().enableUserNameGenerate();
		String isSuccess = "";
		String newUserName = "";
		//异常信息
		String eMessage = "";
		if("true".equals(request.getParameter("storeUser"))){
			
	        //System.out.println("end rizhi");
	        //保存用户
			User user = new User();
			if(isAutoUserName){
				try{
					Map map = new HashMap();
					map.put("orgId",currOrgId);
					user.setUserName(GenerateServiceFactory.getGenerateService().generateUserName(map));
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				user.setUserName(request.getParameter("userName"));
			}
			user.setUserPassword(request.getParameter("userPassword"));
			user.setUserRealname(request.getParameter("userRealname"));
			user.setUserSn(new Integer(request.getParameter("userSn")));
			user.setUserSex(request.getParameter("userSex"));
			user.setUserIsvalid(new Integer(request.getParameter("userIsvalid")));
			user.setUserHometel(request.getParameter("homePhone"));
			user.setUserMobiletel1(request.getParameter("mobile"));
			user.setUserPostalcode(request.getParameter("postalCode"));
			user.setRemark2(request.getParameter("shortMobile"));
			user.setUserEmail(request.getParameter("mail"));
			user.setUserMobiletel2(request.getParameter("userMobiletel2"));
			user.setRemark1(request.getParameter("remark1"));
			user.setRemark3(request.getParameter("remark3"));
			user.setRemark4(request.getParameter("remark4"));
			user.setRemark5(request.getParameter("remark5"));
				
			user.setUserType(request.getParameter("userType"));
			user.setUserPinyin(request.getParameter("userPinyin"));
	
			user.setUserWorktel(request.getParameter("userWorktel"));
			user.setUserFax(request.getParameter("userFax"));
			user.setUserOicq(request.getParameter("userOicq"));
			if(!"".equals(request.getParameter("userBirthday")))
				user.setUserBirthday(Date.valueOf(request.getParameter("userBirthday")));

			user.setUserAddress(request.getParameter("userAddress"));
			user.setUserLogincount(new Integer(request.getParameter("userLogincount")));
			user.setUserIdcard(request.getParameter("userIdcard"));
			if(!"".equals(request.getParameter("userRegdate")))
				user.setUserRegdate(Date.valueOf(request.getParameter("userRegdate")));
			if(request.getParameter("istaxmanager")==null){
				user.setIstaxmanager(0);
			}else{
				user.setIstaxmanager(Integer.valueOf(request.getParameter("istaxmanager")).intValue());
			}
			UserManager userManager = SecurityDatabase.getUserManager();
			// 吴卫雄增加：判断用户是否存在，存在则转入操作失败页面
			// 潘伟林修改， 存在则提示用户登陆名重复，返回原页面，清空登陆名
			if (user.getUserId() == null) {
				if (userManager.isUserExist(user)) {
					request.setAttribute("isUserExist", "true");
					request.setAttribute("reFlush", "false");
				}

			} else {
				// add by pwl 增加修改邮箱用户密码
				String mailValidata = ConfigManager.getInstance()
						.getConfigValue("mailValidata");
				if (mailValidata != null && mailValidata.equals("1")) {
					User oldUser = userManager.getUserById(user
							.getUserId().toString());
					if (!oldUser.getUserPassword().equals(
							user.getUserPassword())) {
						String url = "http://"
								+ ConfigManager.getInstance().getConfigValue(
										"mailServer")
								+ "/creator_changepw.asp?username="
								+ user.getUserEmail() + "&pw1="
								+ user.getUserPassword();
						ApachePostMethodClient client = new ApachePostMethodClient(
								url);
						try {
							String clientResponse = client.sendRequest();
							//System.out.println("response=" + clientResponse);
						} catch (Exception ex) {
							System.out.println(ex.toString());
						} finally {
							client = null;
						}
					}
				}
			}
			newUserName = user.getUserName();
			try{
				if(newUserName != null && !"".equals(newUserName)){
					userManager.creatorUser(user,currOrgId,"1");
		        }else{
		        	if(isAutoUserName){
		        		eMessage = "系统自动生成登陆名异常！";
		        	}else{
		        		eMessage = "登陆名为null！";
		        	}	
		        }
			}catch(Exception e){
				e.printStackTrace();
				eMessage = e.getMessage();
				if(eMessage != null && !"".equals(eMessage)){
					eMessage = eMessage.replaceAll("\\n","\\\\n");
					eMessage = eMessage.replaceAll("\\r","\\\\r");
				}
			}
		}
	    
	    //action end
	    
		
		String orgName = org.getRemark5();
			String userSn = String.valueOf(OrgManAction
					.getMaxOrgUserSn(currOrgId));
			String reFlush = "true";
			if (request.getAttribute("reFlush") != null) {
				reFlush = "false";
			}
			String isUserExist = "false";
			if (request.getAttribute("isUserExist") != null) {
				isUserExist = "true";
			}

			UserInfoForm user = (UserInfoForm) request.getAttribute("currUser");
			String userid = "";
			if (user == null)
			{
				user = new UserInfoForm();
			}
			else
			{
				userid = user.getUserId();
			}

			String isNew = (String) request.getAttribute("isNew");
			if (isNew == null) {
				isNew = "0";
			}

			%>

<html>
	<head>
		<title>新增用户</title>
		<script language="JavaScript" src="<%=request.getContextPath()%>/public/datetime/calender_date.js"></script>
		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script language="javascript" src="../scripts/selectTime.js"></script>
		<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<style type="text/css">

.notice_STYLE{color: #FF0000;display : none }
.STYLE1 {color: #FF0000}

        </style>
	</head>

	<script language="JavaScript">

var reFlush = "<%=reFlush%>";
var userErr = 0;
	
var isUserExist = "<%=isUserExist%>";
if ( isUserExist == "true"){
	alert("登陆名已存在，请重新输入登陆名!");
}
var isSuccess = "<%=isSuccess%>";
if(isSuccess=="1"){
	alert("cuowu");
}

var flag = "<%=request.getParameter("flag")%>";
if(flag=="2"){
	<%
		if(isAutoUserName){
			if(eMessage != null && !"".equals(eMessage)){
			%>
				alert("新增失败!原因如下：\n<%=eMessage%>");
			<%			
			}else{
	%>
				var newUserName = "<%=newUserName%>";
				alert("新增用户成功！");
				userInfo(newUserName);
	<%
			}
		}else{
			if(eMessage != null && !"".equals(eMessage)){
				%>
					alert("新增失败!原因如下：\n<%=eMessage%>");
				<%			
			}else{
	%>
				alert("新增用户成功！");
	<%
			}
		}
	%>
	parent.divProcessing.style.display="none";
   	parent.document.getElementsByName("resave")[0].disabled = false;
   	parent.document.getElementsByName("Submit31")[0].disabled = false;
   	parent.document.getElementsByName("Submit32")[0].disabled = false;  	
	window.returnValue="ok";
	window.close();
	
}

function userInfo(userName){ 
	//用户基本信息 userInfo_tab.jsp
	var winbasic;
	var url="autoUserName.jsp?userName="+userName;
	window.showModalDialog(url,window,"dialogWidth:"+(200)+"px;dialogHeight:"+(150)+"px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;");
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
	
	if(document.UserInfoForm.userName.value.search(/\W/g)!=-1 ){
		alert("登陆名只能为数字、字母和下划线!");
		document.UserInfoForm.userName.focus();
		return false;
	}
	if (validateForm(UserInfoForm) )
	{
		var userName= document.forms[0].userName.value;
		if (trim(userName).length == 0 ){
    		alert("请输入登陆名！"); 
    		return false;
    	} 
    	if(document.UserInfoForm.remark3.checked)
    		document.UserInfoForm.remark3.value="是";
		else 
			document.UserInfoForm.remark3.value="否";
		
		if(document.UserInfoForm.istaxmanager.checked)
			document.UserInfoForm.istaxmanager.value="1";
		else
			document.UserInfoForm.istaxmanager.value="0";
			
		divProcessing.style.display="";
   		document.getElementsByName("resave")[0].disabled = true;
   		document.getElementsByName("Submit31")[0].disabled = true;
   		document.getElementsByName("Submit32")[0].disabled = true;
   		
   		document.UserInfoForm.target = "saveuser";
		document.UserInfoForm.action="../user/userInfo.jsp?storeUser=true&flag=2";
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
	//document.location.href="../user/userManager.do?method=getUserList&orgId=<%=currOrgId%>";	
	window.close();
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
			if(http_request.responseText == 0){
				//alert("用户名已存在");
				notice.style.display="block";  
			}
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

	<body class="info" onload="window_onload()">
		<form name="UserInfoForm" method="post">
			<pg:beaninfo requestKey="currUser">
				<input type="hidden" name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
				<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1" class="thin">
					<tr>
						<td height="25" class="detailtitle" width="23%">登陆名</td>
						<td height="25">
							<%if(GenerateServiceFactory.getGenerateService().enableUserNameGenerate()){ %>
							<input type="hidden" name="userName" value="1" >
							由系统自动生成
							<%}else{ %>
							<input type="text" name="userName" onchange="checkUser()" value="" validator="string" cnname="登陆名" maxlength="<%=userNamelength%>" onfocus="setDisable(this)">
							
							<span class="STYLE1">*</span>
							<div id=notice class="notice_STYLE">用户名已存在
							</div>
							<%} %>
						</td>
						<td height="25" class="detailtitle" width="20%">真实名称</td>
						<td height="25">
							<input type="text" name="userRealname" value="<pg:cell colName="userRealname"  defaultValue=""/>" validator="string" cnname="真实名称" maxlength="100">
							<span class="STYLE1">*</span>
						</td>
					</tr>

					<tr>
						<td height="25" class="detailtitle">口令</td>
						<td height="25">
							<input type="password" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue="123456"/>" validator="string" cnname="口令" maxlength="40">
							<span class="STYLE1">*缺省口令为123456</span>
						</td>
						<td height="25" class="detailtitle">身份证号码</td>
						<td height="25">
							<input type="text" name="userIdcard" value="<pg:cell colName="userIdcard"  defaultValue=""/>" validator="intNull" cnname="身份证号码" maxlength="18">
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">单位电话</td>
						<td height="25">
							<input type="text" name="userWorktel" value="<pg:cell colName="userWorktel"  defaultValue=""/>" validator="intNull" cnname="单位电话" maxlength="13">
						</td>
						<td height="25" class="detailtitle">性别</td>
						<td height="25">
							<dict:select type="sex" name="userSex" expression="{userSex}"  />
					</tr>

					<tr>
						<td height="25" class="detailtitle">家庭电话</td>
						<td height="25">
							<input type="text" name="homePhone" value="<pg:cell colName="homePhone"  defaultValue=""/>" validator="intNull" cnname="家庭电话" maxlength="13">
						</td>
						<td height="25" class="detailtitle">电子邮件</td>
						<td height="25">
							<input type="text" name="mail" value="<pg:cell colName="mail"  defaultValue=""/>" validator="emailNull" cnname="电子邮件" maxlength="40">
						</td>
					</tr>

					<tr>
						<td height="25" class="detailtitle">移动电话1</td>
						<td height="25">
							<input type="text" name="mobile" value="<pg:cell colName="mobile"  defaultValue=""/>" validator="intNull" cnname="移动电话" maxlength="13">
						</td>
						<td height="25" class="detailtitle">移动电话1归属地</td>
						<td height="25">
							<input type="text" name="remark4" value="<pg:cell colName="remark4"  defaultValue=""/>" validator="stringNull" cnname="移动电话1归属地" maxlength="100">
						</td>
					</tr>

					<tr>
						<td height="25" class="detailtitle">移动电话2</td>
						<td height="25">
							<input type="text" name="userMobiletel2" value="<pg:cell colName="userMobiletel2"  defaultValue=""/>" validator="intNull" cnname="移动电话2" maxlength="13">
						</td>
						<td height="25" class="detailtitle">移动电话2归属地</td>
						<td height="25">
							<input type="text" name="remark5" value="<pg:cell colName="remark5"  defaultValue=""/>" validator="stringNull" cnname="移动电话2归属地" maxlength="100">
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">组织机构</td>
						<td height="25">
							<input readonly="true" type="text" name="ou" value="<pg:cell colName="ou"  defaultValue="<%=orgName%>"/>" validator="stringNull" cnname="组织机构" maxlength="100">
						</td>
						<td height="25" class="detailtitle">拼音</td>
						<td height="25">
							<input type="text" name="userPinyin" value="<pg:cell colName="userPinyin"  defaultValue=""/>" validator="stringNull" cnname="拼音" maxlength="100">
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">用户类型</td>
						<td height="25">
							<dict:select type="userType" name="userType" expression="{userType}" />
						</td>
						<td height="25" class="detailtitle">邮政编码</td>
						<td height="25">
							<input type="text" name="postalCode" value="<pg:cell colName="postalCode"  defaultValue=""/>" validator="intNull" cnname="邮政编码" maxlength="7">
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">传真</td>
						<td height="25">
							<input type="text" name="userFax" value="<pg:cell colName="userFax"  defaultValue=""/>" validator="stringNull" cnname="传真" maxlength="40">
						</td>
						<td height="25" class="detailtitle">OICQ</td>
						<td height="25">
							<input type="text" name="userOicq" value="<pg:cell colName="userOicq"  defaultValue=""/>" validator="intNull" cnname="OICQ" maxlength="13">
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">生日</td>
						<td height="25">
							<input type="text" name="userBirthday" onclick="showdate(document.all('userBirthday'))" readonly="true" value="<pg:cell colName="userBirthday"  defaultValue=""  />" validator="stringNull" cnname="生日" maxlength="40">
						</td>
						<td height="25" class="detailtitle">用户地址</td>
						<td height="25">
							<input type="text" name="userAddress" value="<pg:cell colName="userAddress"  defaultValue=""/>" validator="stringNull" cnname="用户地址" maxlength="200">
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">登录次数</td>
						<td height="25">
							<input type="text" name="userLogincount" value="<pg:cell colName="userLogincount"  defaultValue="0"/>" validator="intNull" readonly cnname="登录次数" maxlength="40">
						</td>
						<td height="25" class="detailtitle">是否有效</td>
						<td height="25">
							<dict:select type="isvalid" name="userIsvalid" expression="{userIsvalid}" />
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">注册日期</td>
						<td height="25">
							<input type="text" name="userRegdate" onclick="showdate(document.all('userRegdate'))" readonly="true" value="<pg:cell colName="userRegdate"  defaultValue="" />" validator="stringNull" cnname="注册日期" maxlength="40">
						</td>
						<td height="25" class="detailtitle">手机短号码</td>
						<td height="25">
							<input type="text" name="shortMobile" value="<pg:cell colName="shortMobile"  defaultValue=""/>" validator="stringNull" cnname="手机短号码" maxlength="40">
						</td>
					</tr>
					<tr>
						<td height="25" class="detailtitle">用户排序号 </td>
						<td height="25">
							<input type="text" name="userSn" value="<pg:cell colName="userSn"  defaultValue="" />" validator="stringNull" cnname="用户排序号" maxlength="40">
						</td>
						<td height="25" class="detailtitle" colspan="2">
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width='10%'>
										<input type="checkbox" name="remark3" <pg:equal colName="remark3" value="是">checked</pg:equal>>
									</td>
									<td width='150px'>个人信息是否保密</td>
								</tr>
							</table>
						</td>

					</tr>
					<tr>
						<td height="25" class="detailtitle" colspan="2">
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width='10%'>
										<input type="checkbox" name="istaxmanager" <pg:equal colName="istaxmanager" value="1">checked</pg:equal>>
									</td>
									<td width='150px'>是否税管员</td>
								</tr>
							</table>
						</td>
					</tr>

					<input type="hidden" name="userId" value="<pg:cell colName="userId"  defaultValue=""/>" />
					<input name="orgId" value="<%=currOrgId%>" type="hidden" height="0">
				</table>
			
			
		</form>

		<hr width="100%">
		<table width="100%" cellpadding="2" cellspacing="0">
			<tr>
				<td>
					<table width="35%" border="0" align="right" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<div align="center">
								<%
								if(!isNew.equals("0") || accesscontroler.getUserID().equals( userid) ||
								 accesscontroler.checkPermission(currOrgId,"edituser",AccessControl.ORGUNIT_RESOURCE))
								{%>
									<input name="resave" type="button" class="input" value="保存" onClick="storeUser()">
								<%}%>	
								</div>
							</td>
							<td>
								<div align="center">
									<input name="Submit31" type="button" class="input" value="全部清空" onClick="UserInfoForm.reset();">
								</div>
							</td>
							<td>
								<div align="center">
									<input name="Submit32" type="submit" class="input" value="关闭" onClick="back()">
								</div>
							</td>

						</tr>
					</table>
				</td>
			</tr>
		</table>
		</pg:beaninfo>
<iframe name="saveuser" height="0" width="0"></iframe>
<div id=divProcessing style="width:200px;height:30px;position:absolute;left:300px;top:450px;display:none">
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
	<script>
	window.onload=function setDefaultValue(){
	    if(document.all("userSn").value==""){
	        document.all("userSn").value="<%=userSn%>"
	    }
	}
	</script>
</html>
