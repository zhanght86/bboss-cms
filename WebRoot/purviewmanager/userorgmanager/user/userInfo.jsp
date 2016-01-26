<%
/*
 * <p>Title: 新增用户页面</p>
 * <p>Description: 新增用户页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-20
 * @author liangbing.tao
 * @version 1.0
 */
 %>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.orgmanager.OrgManAction"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>			
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.config.ConfigManager"%>
<%@ page import="java.sql.Date" %>
<%@ page import="java.text.SimpleDateFormat"%>
<%@page import="com.frameworkset.platform.sysmgrcore.purviewmanager.GenerateServiceFactory,java.util.*"%>
				 
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%

		AccessControl accesscontroler = AccessControl.getInstance();
	    accesscontroler.checkManagerAccess(request,response);
	    
	    //获取当前系统的时间 并格式化
	    //baowen.liu
	    java.util.Date date=new java.util.Date();
	     SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	     String datestr = sdf.format(date);
	     
	    //用户登录名的长度--order by gao.tang
	    String userNamelength = ConfigManager.getInstance().getConfigValue("userNameLength");
	    String currOrgId =  request.getParameter("orgId");
	    
		if(currOrgId == null)
		{
			currOrgId = (String)request.getAttribute("orgId");
		}
		
		Organization org = OrgCacheManager.getInstance().getOrganization(currOrgId);
		
	    //action start
	    if("true".equals(request.getParameter("isNewUser")))
	    {
	    	//新建用户初始化action
		    request.setAttribute("currUser", null);
			request.setAttribute("reFlush", "false");
			
			request.setAttribute("isNew", "1");
		}
		String newUserName = "";
		//是否自动生成用户名
		boolean isAutoUserName = GenerateServiceFactory.getGenerateService().enableUserNameGenerate();
		//报错信息
		String errorMessage = "";
		if("true".equals(request.getParameter("storeUser")))
		{
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
			user.setUserWorknumber(request.getParameter("userWorknumber"));
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
			UserManager userManager = SecurityDatabase.getUserManager();
			String passwordDualedTime_ = request.getParameter("passwordDualedTime");
			int passwordDualedTime = userManager.getDefaultPasswordDualTime();
			try
			{
				passwordDualedTime = Integer.parseInt(passwordDualedTime_);
			}
			catch(Exception e)
			{
				
			}
			user.setPasswordDualedTime(passwordDualedTime);
			
			
			
			// 吴卫雄增加：判断用户是否存在，存在则转入操作失败页面
			// 潘伟林修改， 存在则提示用户登陆名重复，返回原页面，清空登陆名
			
			boolean isUserExist = false;
			if (userManager.isUserExist(user)) {
				request.setAttribute("isUserExist", "true");
				request.setAttribute("reFlush", "false");
					
				isUserExist = true ;
			}

			
			//System.out.println("isUserExist = " + isUserExist);
			if(isUserExist == false)//用户名存在不保存用户
			{
				newUserName = user.getUserName();
				try{
					if(newUserName != null && !"".equals(newUserName)){
						userManager.creatorUser(user,currOrgId,"1");
					}else{
						if(isAutoUserName){
			        		errorMessage = RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.loginname.generate.system.exception", request);
			        	}else{
			        		errorMessage = RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.loginname.null", request);
			        	}
					}
				}catch(Exception e){
					e.printStackTrace();
					errorMessage = e.getMessage();
					if(errorMessage != null && !"".equals(errorMessage)){
						errorMessage = errorMessage.replaceAll("\\n","\\\\n");
						errorMessage = errorMessage.replaceAll("\\r","\\\\r");
					}
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
		
		
		<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
		
        <script language="JavaScript">
        	var api = frameElement.api;
        	if(api){
        		W = api.opener;
        	}else{
        		api = parent.frameElement.api,W = api.opener;
        	}
			 
			var reFlush = "<%=reFlush%>";
			var userErr = 0;
				
			function userInfo(userName){ 
				var winbasic;
				var url="autoUserName.jsp?userName="+userName;
				window.showModalDialog(url,window,"dialogWidth:"+(200)+"px;dialogHeight:"+(150)+"px;scroll=yes;status=no;titlebar=no;toolbar=no;maximize=yes;minimize=0;");
			
			}
			
			var isUserExist = "<%=isUserExist%>";
			if ( isUserExist == "true"){
				<%
					if(isAutoUserName){
				%>
					$.dialog.alert("<pg:message code='sany.pdp.server.error'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				<%
					}else{
				%>
					$.dialog.alert("<pg:message code='sany.pdp.server.error'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				<%
					}
				%>
				//parent.divProcessing.style.display="none";
			}else{
				var flag = <%=request.getParameter("flag")%>
				if(flag=="2"){
				<%
					if(isAutoUserName){
						if(errorMessage != null && !"".equals(errorMessage)){
						%>
						$.dialog.alert("<pg:message code='sany.pdp.add.user.fail'/>：\n<%=errorMessage%>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						<%			
						}else{
				%>
							var newUserName = "<%=newUserName%>";
							$.dialog.alert("<pg:message code='sany.pdp.add.user.success'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
							userInfo(newUserName);
				<%
						}
					}else{
						if(errorMessage != null && !"".equals(errorMessage)){
						%>
							$.dialog.alert("<pg:message code='sany.pdp.add.user.fail'/>：\n<%=errorMessage%>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
						<%			
						}else{
				%>
							$.dialog.alert("<pg:message code='sany.pdp.add.user.success'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					<%
						}
					}
				%>
					//parent.divProcessing.style.display="none";
				}
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
				
				
				var  userName = document.UserInfoForm.userName.value;
				
				if(userName == "" || userName.length<1 || userName.replace(/\s/g,"")=="")
				{
					$.dialog.alert("<pg:message code='sany.pdp.input.username'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false;
				}
				
				
				
				var userRealname = document.UserInfoForm.userRealname.value;
				
				if(userRealname == "" || userRealname.length<1 || userRealname.replace(/\s/g,"")=="")
				{
					$.dialog.alert("<pg:message code='sany.pdp.input.realname'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return false;
				}
				
				var re_ = /^[a-zA-Z]\w*$/; 
				if(!re_.test(userName))
				{
				$.dialog.alert("<pg:message code="sany.pdp.check.username"/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return false; 
				}
				
				var re =  /^[A-Za-z0-9\u4e00-\u9fa5]+$/; 
				if(!re.test(userRealname))
				{
				$.dialog.alert("<pg:message code="sany.pdp.check.realname"/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return false; 
				}
				
				
				 
				{
					//var userName= document.forms[0].userName.value;
					//if (trim(userName).length == 0 ){
			    	//	alert("请输入登陆名！"); 
			    	//	return false;
			    	//} 
			    	
			    	//var userSn = document.forms[0].userSn.value;
			    	//if(trim(userSn).length ==0)
			    	//{   String userSn = String.valueOf(OrgManAction.getMaxOrgUserSn(currOrgId));
			    	    
			    	//}
			    	
			    	
					
						$.ajax({
							   type: "POST",
								url : "<%=request.getContextPath()%>/usermanager/addUser.page?storeUser=true&flag=2",
								data :formToJson("#UserInfoForm"),
								dataType : 'json',
								async:false,
								beforeSend: function(XMLHttpRequest){
										var validated =validateForm(UserInfoForm);
								      	if (validated){
								      		if(document.UserInfoForm.remark3.checked)
									    		document.UserInfoForm.remark3.value="<pg:message code='sany.pdp.common.yes'/>";
											else 
												document.UserInfoForm.remark3.value="<pg:message code='sany.pdp.common.no'/>";
								      		blockUI();	
								      		XMLHttpRequest.setRequestHeader("RequestType", "ajax");
								      	}
								      	else
								      	{			      		
								      		return false;
								      	}				 	
									},
								success : function(response){
									//去掉遮罩
									unblockUI();
									if(response.code=="success"){
										var msg = response.errormessage;
										 
										W.$.dialog.alert(msg,function(){	
												W.loaduserlist('<%=currOrgId%>');
												api.close();
										},api);													
									}else{
										W.$.dialog.alert("操作结果："+response.errormessage,function(){	
											 
											},api);	
										 
									}
								}
							  });
			   		//document.UserInfoForm.target = "saveuser";
					//document.UserInfoForm.action="../user/userInfo.jsp?storeUser=true&flag=2";
					//document.UserInfoForm.submit();
					
				}
			}
			
			function back()
			{
				window.close();
				window.returnValue = "ok";
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
					$.dialog.alert("<pg:message code='sany.pdp.no.httprequest'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
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
						$.dialog.alert("<pg:message code='sany.pdp.server.error'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					}
				}
			}
			
			function checkUser(){
				var userName = document.forms[0].userName.value;
				send_request('checkUser.jsp?userName='+userName);
			}
			
			
			window.onload=function setDefaultValue(){
	    		if(document.all("userSn").value==""){
	        		document.all("userSn").value="<%=userSn%>"
	    		}
			}
		</script>
	</head>
	<body>
		<div style="height: 10px">&nbsp;</div>
		<div align="center">
		<form name="UserInfoForm" id="UserInfoForm" method="post">
			<pg:beaninfo requestKey="currUser">
			
				<input type="hidden" name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
					<tr>
						<th><pg:message code="sany.pdp.user.login.name"/>：</th>
						<td height="25">
							<input type="text" name="userName" onchange="checkUser()" value="" validator="string" cnname="<pg:message code='sany.pdp.user.login.name'/>" maxlength="<%=userNamelength%>" onfocus="setDisable(this)">
							
							<span class="STYLE1">*</span>
							<div id=notice class="notice_STYLE" style="display: none;"><pg:message code="sany.pdp.username.exist"/>
							</div>
						</td>
						<th><pg:message code="sany.pdp.user.real.name"/>：</th>
						<td height="25">
							<input type="text" name="userRealname" value="<pg:cell colName="userRealname"  defaultValue=""/>" validator="string" cnname="真实名称" maxlength="100">
							<span class="STYLE1">*</span>
						</td>
					</tr>

					<tr>
						<th><pg:message code="sany.pdp.password"/>：</th>
						<td height="25">
							<input type="password" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue="123456"/>" validator="string" cnname="口令" maxlength="40">
							<span class="STYLE1">*<pg:message code="sany.pdp.default.password"/>123456</span>
						</td>
						<th><pg:message code="sany.pdp.identity.card"/>：</th>
						<td height="25">
							<input type="text" name="userIdcard" value="<pg:cell colName="userIdcard"  defaultValue=""/>" validator="stringNull" cnname="身份证号码" maxlength="18"><span class="STYLE1">*</span>
						</td>
					</tr>
					<tr>
						<th><pg:message code="sany.pdp.company.telephone"/>：</th>
						<td height="25">
							<input type="text" name="userWorktel" value="<pg:cell colName="userWorktel"  defaultValue=""/>" validator="phone" cnname="单位电话" maxlength="13">
						</td>
						<th><pg:message code="sany.pdp.sex"/>：</th>
						<td height="25">
							<dict:select type="sex" name="userSex" expression="{userSex}"  />
					</tr>

					<tr>
						<th><pg:message code="sany.pdp.family.telephone"/>：</th>
						<td height="25">
							<input type="text" name="homePhone" value="<pg:cell colName="homePhone"  defaultValue=""/>" validator="phone" cnname="家庭电话" maxlength="13">
						</td>
						<th><pg:message code="sany.pdp.email"/>：</th>
						<td height="25">
							<input type="text" name="mail" value="<pg:cell colName="mail"  defaultValue=""/>" validator="emailNull" cnname="电子邮件" maxlength="40">
						</td>
					</tr>

					<tr>
						<th><pg:message code="sany.pdp.moblie.telephone"/>1：</th>
						<td height="25">
							<input type="text" name="mobile" value="<pg:cell colName="mobile"  defaultValue=""/>" validator="phone" cnname="移动电话" maxlength="13">
						</td>
						<th><pg:message code="sany.pdp.moblie.telephone"/>1<pg:message code="sany.pdp.address"/>：</th>
						<td height="25">
							<input type="text" name="remark4" value="<pg:cell colName="remark4"  defaultValue=""/>" validator="stringNull" cnname="移动电话1归属地" maxlength="100">
						</td>
					</tr>

					<tr>
						<th><pg:message code="sany.pdp.moblie.telephone"/>2：</th>
						<td height="25">
							<input type="text" name="userMobiletel2" value="<pg:cell colName="userMobiletel2"  defaultValue=""/>" validator="phone" cnname="移动电话2" maxlength="13">
						</td>
						<th><pg:message code="sany.pdp.moblie.telephone"/>2<pg:message code="sany.pdp.address"/>：</th>
						<td height="25">
							<input type="text" name="remark5" value="<pg:cell colName="remark5"  defaultValue=""/>" validator="stringNull" cnname="移动电话2归属地" maxlength="100">
						</td>
					</tr>
					<tr>
						<th><pg:message code="sany.pdp.workflow.organization"/>：</th>
						<td height="25">
							<input readonly="true" type="text" name="ou" value="<pg:cell colName="ou"  defaultValue="<%=orgName%>"/>" validator="stringNull" cnname="组织机构" maxlength="100">
						</td>
						<th><pg:message code="sany.pdp.spell"/>：</th>
						<td height="25">
							<input type="text" name="userPinyin" value="<pg:cell colName="userPinyin"  defaultValue=""/>" validator="stringNull" cnname="拼音" maxlength="100">
						</td>
					</tr>
					<tr>
						<th><pg:message code="sany.pdp.user.type"/>：</th>
						<td height="25">
							<dict:select type="userType" name="userType" expression="{userType}" />
						</td>
						<th><pg:message code="sany.pdp.post.code"/>：</th>
						<td height="25">
							<input type="text" name="postalCode" value="<pg:cell colName="postalCode"  defaultValue=""/>" validator="intNull" cnname="邮政编码" maxlength="7">
						</td>
					</tr>
					<tr>
						<th><pg:message code="sany.pdp.fax"/>：</th>
						<td height="25">
							<input type="text" name="userFax" value="<pg:cell colName="userFax"  defaultValue=""/>" validator="phone" cnname="传真" maxlength="40">
						</td>
						<th>OICQ：</th>
						<td height="25">
							<input type="text" name="userOicq" value="<pg:cell colName="userOicq"  defaultValue=""/>" validator="intNull" cnname="OICQ" maxlength="13">
						</td>
					</tr>
					<tr>
						<th><pg:message code="sany.pdp.birthday"/>：</th>
						<td height="25">
							<input type="text" name="userBirthday" class="Wdate" onclick="WdatePicker()" readonly="true" value="<pg:cell colName="userBirthday"  defaultValue=""  />" validator="stringNull" cnname="生日" maxlength="40">
						</td>
						<th><pg:message code="sany.pdp.user.address"/>：</th>
						<td height="25">
							<input type="text" name="userAddress" value="<pg:cell colName="userAddress"  defaultValue=""/>" validator="stringNull" cnname="用户地址" maxlength="200">
						</td>
					</tr>
					<tr>
						<th><pg:message code="sany.pdp.login.count"/>：</th>
						<td height="25">
							<input type="text" name="userLogincount" value="<pg:cell colName="userLogincount"  defaultValue="0"/>" validator="intNull" readonly cnname="登录次数" maxlength="40">
						</td>
						<th><pg:message code="sany.pdp.role.organization.check"/>：</th>
						<td height="25">
							<dict:select type="isvalid" name="userIsvalid" expression="{userIsvalid}" />
						</td>
					</tr>
					<tr>
						<th><pg:message code="sany.pdp.register.date"/>：</th>
						<td height="25">
							<input type="text" name="userRegdate"  readonly="true" value="<%=datestr%>" validator="stringNull" cnname="注册日期" maxlength="40">
						</td>
						<th><pg:message code="sany.pdp.short.mobile"/>：</th>
						<td height="25">
							<input type="text" name="shortMobile" value="<pg:cell colName="shortMobile"  defaultValue=""/>" validator="phone" cnname="手机短号码" maxlength="40">
						</td>
					</tr>
					<tr>
						<th><pg:message code="sany.pdp.user.sort.number"/>： </th>
						<td height="25">
							<input readonly="true" type="text" name="userSn" value="<pg:cell colName="userSn"  defaultValue="<%=userSn %>" />" validator="stringNull" cnname="用户排序号" maxlength="40">
						</td>
						<td height="25" class="detailtitle" colspan="2">
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width='10%'>
										<input type="checkbox" name="remark3" <pg:equal colName="remark3" value="是">checked</pg:equal>>
									</td>
									<td width='150px'><pg:message code="sany.pdp.is.userinfo.secrecy"/></td>
								</tr>
							</table>
						</td>

					</tr>
					<tr>
					<th>
							 设置密码有效期
						</th>
						<td height="25" >
							
							<input type="text" name="passwordDualedTime" value="<pg:cell colName="passwordDualedTime"  defaultValue="-1" />" validator="intNull" cnname="密码有效期" maxlength="40" >天
							
						</td>
						<th>工号： </th>
						<td height="25" >
							<input type="text" name="userWorknumber" value="<pg:cell colName="userWorknumber"  />" validator="stringNull" cnname="用户工号" maxlength="40">
						</td>
					

					</tr>

					<input type="hidden" name="userId" value="<pg:cell colName="userId"  defaultValue=""/>" />
					<input name="orgId" value="<%=currOrgId%>" type="hidden" height="0">
				</table>	
				<div class="btnarea" >
						<%
								if(!isNew.equals("0") || accesscontroler.getUserID().equals( userid) ||
								 accesscontroler.checkPermission(currOrgId,"edituser",AccessControl.ORGUNIT_RESOURCE))
								{%>
										<a href="javascript:void(0)" class="bt_1" id="addButton"
											onclick="storeUser()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
								<%}%>	
								<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="UserInfoForm.reset();"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
								<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
				</div>
		</form>

	</pg:beaninfo>
	<iframe name="saveuser" height="0" width="0"></iframe>
	
	</div>
	</body>
</html>

