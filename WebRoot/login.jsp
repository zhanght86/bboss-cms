<%@page import="com.frameworkset.platform.sysmgrcore.authenticate.LoginUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.liferay.portlet.iframe.action.WebDes"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.security.authorization.AccessException,com.frameworkset.platform.config.ConfigManager,com.liferay.portlet.iframe.action.SSOUserMapping,org.frameworkset.web.servlet.support.RequestContextUtils"%>
<%@ page import="com.frameworkset.platform.ca.CaProperties"%>
<%@ page import="com.frameworkset.platform.ca.CAManager"%>
<%@ page import="com.frameworkset.platform.ca.CookieProperties"%>
<%@ page import="com.frameworkset.platform.framework.Framework,com.sany.webseal.LoginValidate.*"%>
<%@ page import="org.frameworkset.web.servlet.support.WebApplicationContextUtils"%>
<%@ page import="org.frameworkset.spi.support.MessageSource"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
	
<%@ page import="java.util.*"%>
<%
	//AccessControl context_ = AccessControl.getInstance();
	//boolean success = context_.checkAccess(request, response,false);
	//if(success)
	//{
	//	out.println("对不起！用户"+context_.getUserAccount() + "("+context_.getUserName()+")已经登录系统，请注销后再重新登录系统!");
	//	return;
	//}
	String u = "", p = "", ck = "";

	String successRedirect = request.getParameter("successRedirect");
	String language = request.getParameter("language");
	boolean enable_login_validatecode = ConfigManager.getInstance()
			.getConfigBooleanValue("enable_login_validatecode", true);
	
	String errorMessage = null;
	
    String userName = request.getParameter("userName");
    int expiredays = userName != null?SecurityDatabase.getUserManager().getUserPasswordDualTimeByUserAccount(userName):0;
    String expriedtime_ = "";
    Date expiretime = expiredays > 0&&userName != null?SecurityDatabase.getUserManager().getPasswordExpiredTimeByUserAccount(userName):null;
    if(expiretime != null)
    {
    	SimpleDateFormat dateformt = new SimpleDateFormat("yyyy-MM-dd");
    	expriedtime_ = dateformt.format(expiretime);
    }
	String loginStyle = null;
	String system_id = null;
	if(language==null){
		language = RequestContextUtils.getLocaleResolver(request).resolveLocaleCode(request);
		
	}
	
		
	/* if(language.equals("zh_CN")){
	 	request.getSession().setAttribute("languageKey", java.util.Locale.CHINA);
	 }
	else if(language.equals("en_US")){
		 	request.getSession().setAttribute("languageKey", java.util.Locale.US);
	} */
	String loginPath = request.getParameter("loginPath");
	String subsystem_id = request.getParameter("subsystem_id");
	
	loginStyle = StringUtil.getCookieValue(request, "loginStyle");
	system_id = StringUtil.getCookieValue(request, "subsystem_id");
	
	if (loginPath != null) {
		StringUtil.addCookieValue(request, response, "loginStyle", loginPath);
		
	}
	if (subsystem_id != null) {
		StringUtil.addCookieValue(request, response, "subsystem_id", subsystem_id);		
	}
	
	boolean isCasServer = ConfigManager.getInstance()
			.getConfigBooleanValue("isCasServer", false);
			
	boolean isWebSealServer = ConfigManager.getInstance()
			.getConfigBooleanValue("isWebSealServer", false);
			
	//是否使用数字认证中心服务
	boolean CA_LOGIN_SERVER = CaProperties.CA_LOGIN_SERVER;

	boolean isCert = true;
	String certUserName = null;
	String certUserPassword = null;
	if (CA_LOGIN_SERVER) {
		isCert = CAManager.checkCertSn(request);
		if (isCert) {
			certUserName = CAManager.getUserName(request);
			certUserPassword = CAManager.getUserPassword(request);
		}
	}
	String specialuser = LoginUtil.isSpesialUser(request);
	if (isCasServer) {
		userName = (String) session
				.getAttribute("edu.yale.its.tp.cas.client.filter.user");
		boolean state = false;
		if (userName != null && !"".equals(userName)) {
			state = SSOUserMapping.isIncludeUser(userName);
		}
		if (state) {
			//系统管理版本号，2.0和2.0以上的版本,默认版本为1.0
			String systemVersion = ConfigManager.getInstance()
					.getConfigValue("system.version", "1.0");
			String subsystem = request.getParameter("subsystem_id");
			String password = SSOUserMapping.getUserPassword(userName);
			if (subsystem == null)
				subsystem = AccessControl.getDefaultSUBSystemID();

			if (systemVersion.compareTo("1.0") > 0) {
				AccessControl.getInstance().login(request, response,
						userName, password);

				//  if(subsystem == null) subsystem = "module";
				/** 
				需要全屏时，将response.sendRedirect("index.jsp");注释掉，
				将response.sendRedirect("sysmanager/refactorwindow.jsp");打开
				 */
				if (successRedirect == null) {
					if ((loginPath != null && loginPath.equals("1"))  || subsystem.equals("cms")) {
						successRedirect = "index.jsp?subsystem_id="
								+ subsystem;
				 } else if (loginPath == null || loginPath.equals("3")) {
						successRedirect = "sanydesktop/index.page";
				}  else if (loginPath.equals("2")) {
							successRedirect = "desktop/desktop1.page";
						} else if (loginPath.equals("4")) {
							successRedirect = "sanydesktop/webindex.page";
						}
				}
				AccessControl.recordIndexPage(request, successRedirect);
				AccessControl.recordeSystemLoginPage(request, response);
				response.sendRedirect(successRedirect);
				return;
			} else {
			//判断用户是否已经登录
				AccessControl control = AccessControl.getInstance();
				control.checkAccess(request, response, false);
				String user = control.getUserAccount();
				//如果没有登录则进行登录
				if (user == null || "".equals(user)
						|| !userName.equals(user)) {
					 if(!userName.equals(user))
						 	control.resetSession(session);
					AccessControl.getInstance().login(request,
							response, userName, password);
					if (subsystem == null)
						subsystem =  AccessControl.getDefaultSUBSystemID();
					if (successRedirect == null) {
						if ((loginPath != null && loginPath.equals("1"))  || subsystem.equals("cms")) {
							successRedirect = "index.jsp?subsystem_id="
									+ subsystem;
					 } else if (loginPath == null || loginPath.equals("3")) {
							successRedirect = "sanydesktop/index.page";
					}  else if (loginPath.equals("2")) {
							successRedirect = "desktop/desktop1.page";
						} else if (loginPath.equals("4")) {
							successRedirect = "sanydesktop/webindex.page";
						}
					}
					AccessControl.recordIndexPage(request, successRedirect);
					AccessControl.recordeSystemLoginPage(request, response);
					response.sendRedirect(successRedirect);
					return;
				} else {
					if (subsystem == null)
						subsystem =  AccessControl.getDefaultSUBSystemID();
					if (successRedirect == null) {
						 if ((loginPath != null && loginPath.equals("1"))  || subsystem.equals("cms")) {
								successRedirect = "index.jsp?subsystem_id="
										+ subsystem;
						 } else if (loginPath == null || loginPath.equals("3")) {
								successRedirect = "sanydesktop/index.page";
						} else if (loginPath.equals("2")) {
							successRedirect = "desktop/desktop1.page";
						}else if (loginPath.equals("4")) {
							successRedirect = "sanydesktop/webindex.page";
						}
								
					}
					AccessControl.recordIndexPage(request, successRedirect);
					AccessControl.recordeSystemLoginPage(request, response);
					response.sendRedirect(successRedirect);
					return;
				}
			}
		} else {
			if (userName == null || userName.equals("")) {
				out
						.print("系统启用了cas单点登录功能，请在web.xml的CAS Filte中拦截login.jsp页面");
			} else {
				out.print("用户【" + userName + "】在此应用中没有开通！ ");
			}
		}
	}
	else if((specialuser != null || isWebSealServer) && userName == null)
	{
		
		
		 String subsystem = request.getParameter("subsystem_id");
        try//uim检测
         {
        	 String ip = "";
	          if(specialuser != null)
	          {
	        	  userName = specialuser;
	        	  ip = com.frameworkset.util.StringUtil.getClientIP(request);
	          }
	          else        	  
	          {
		          CommonInfo info = new CommonInfo(); 
		          UimUserInfo userinfo = null;
		         
		          userinfo = info.validateUIM(request);
		          ip = userinfo.getUser_ip();		             
	          	  userName = userinfo.getUser_name();
	          }
	          AccessControl control = AccessControl.getInstance();
			control.checkAccess(request, response, false);
			String user = control.getUserAccount();	
			request.setAttribute("fromsso","true");
			if (user == null || "".equals(user) || !userName.equals(user)) {
			 
				
				
				 try
		         {
					 if(!userName.equals(user))
					 	control.resetSession(session);
		             String password = SSOUserMapping.getUserPassword(userName);
		             control = AccessControl.getInstance();
		             control.login(request,
								response, userName, password);
						
		             if (subsystem == null)
							subsystem =  AccessControl.getDefaultSUBSystemID();
						if (successRedirect == null) {
							 if ((loginPath != null && loginPath.equals("1"))  || subsystem.equals("cms")) {
								successRedirect = "index.jsp?subsystem_id="
										+ subsystem;
							}else if (loginPath == null || loginPath.equals("3")) {
								successRedirect = "sanydesktop/index.page";
							}
							 else if (loginPath.equals("2")) {
								successRedirect = "desktop/desktop1.page";
							} else if (loginPath.equals("4")) {
								successRedirect = "sanydesktop/webindex.page";
							}
						}
						AccessControl.recordIndexPage(request, successRedirect);
						AccessControl.recordeSystemLoginPage(request, response);
						response.sendRedirect(successRedirect);
						return;
					}
					catch(Exception e)
					{
						
						response.sendRedirect(request.getContextPath() + "/webseal/websealloginfail.jsp?userName=" + userName + "&ip=" + ip);
						return;
					}	
	             
	         
	        
	         
			} else {
				
				if (subsystem == null)
					subsystem =  AccessControl.getDefaultSUBSystemID();
				if (successRedirect == null) {
					if ((loginPath != null && loginPath.equals("1"))  || subsystem.equals("cms")) {
						successRedirect = "index.jsp?subsystem_id="
								+ subsystem;
					}else if (loginPath == null || loginPath.equals("3")) {
						successRedirect = "sanydesktop/index.page";
					}else  if (loginPath.equals("2")) {
						successRedirect = "desktop/desktop1.page";
					} else if (loginPath.equals("4")) {
						successRedirect = "sanydesktop/webindex.page";
					}
							
				}
				AccessControl.recordIndexPage(request, successRedirect);
				AccessControl.recordeSystemLoginPage(request, response);
				response.sendRedirect(successRedirect);
				return;
			}
         
         } 
        catch(Exception e)//检测失败,继续平台登录
      {
      		
      }
		
				
	} 
	else 	
	{
		//System.out.println(session.getSessionContext());
		String flag = request.getParameter("flag"); //是否触发提交
		AccessControl dd = AccessControl.getInstance();
		//登陆名称的长度
		

		if (flag == null) {
		} else {
			//String successRedirect = request.getParameter("successRedirect");

			
			String password = request.getParameter("password");
			WebDes wd = new WebDes();
			password = wd.strDec(password, userName, "", "");
			
			if (userName != null) {
				try {
					if(enable_login_validatecode)
					{
						String rand=request.getParameter("rand");
						String session_rand=(String)session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
						session.removeAttribute("rand");
						if(session_rand==null||(!session_rand.equalsIgnoreCase(rand))){
							throw new AccessException("验证码错误!");
						}
					}
					
					AccessControl.getInstance().login(request,
							response, userName, password);
					String subsystem = request
							.getParameter("subsystem_id");
					UserManager userManager = SecurityDatabase.getUserManager();   
					//System.out.println("orgName========================"+orgName);
					//System.out.println("orgId========================"+orgId);
					if (!isCert) {
						CAManager.updateUserCERT_SN(request, userName);
					}
					if (CA_LOGIN_SERVER)
						request.getSession().setAttribute(
								CookieProperties.CERT_SN,
								CAManager.getCookieValue(request,
										CookieProperties.CERT_SN));
					if (subsystem == null)
						subsystem =  AccessControl.getDefaultSUBSystemID();
					/** 
					需要全屏时，将response.sendRedirect("index.jsp");注释掉，
					将response.sendRedirect("sysmanager/refactorwindow.jsp");打开
					 */
					if (successRedirect == null) {
						if ((loginPath != null && loginPath.equals("1"))  || subsystem.equals("cms")) {
							successRedirect = "index.jsp?subsystem_id="
									+ subsystem;
					 } else if (loginPath == null || loginPath.equals("3")) {
							successRedirect = "sanydesktop/index.page";
					}  else if (loginPath.equals("2")) {
							successRedirect = "desktop/desktop1.page";
						} else if (loginPath.equals("4")) {
							successRedirect = "sanydesktop/webindex.page";
						}

					}
					AccessControl.recordIndexPage(request, successRedirect);
					AccessControl.recordeSystemLoginPage(request, response);
					response.sendRedirect(successRedirect);
					//response.sendRedirect("sysmanager/refactorwindow.jsp?subsystem_id=" + subsystem);
				} catch (AccessException ex) {

					errorMessage = ex.getMessage();
					if (errorMessage != null) {
						//errorMessage = errorMessage.replaceAll("\\n",
						//		"\\\\n");
						//errorMessage = errorMessage.replaceAll("\\r",
						//		"\\\\r");
					}
					else
					{
						errorMessage = org.frameworkset.web.servlet.support.RequestContextUtils.getI18nMessage("sany.pdp.login.failed", request);
					}

					
		                	//if(errorMessage==null){
		                	//	out.print("登陆失败，请确保输入的用户名和口令是否正确！");
		                	//}
		                	//else{
		                	//	out.print(errorMessage);
		                    //}
	                    
	               
				} catch (Exception ex) {
					errorMessage = ex.getMessage();
					ex.printStackTrace();
					if(errorMessage != null)
					{
						//errorMessage = errorMessage.replaceAll("\\n",
						//		"\\\\n");
						//errorMessage = errorMessage.replaceAll("\\r",
						//		"\\\\r");
					}
						else
						{
							errorMessage = org.frameworkset.web.servlet.support.RequestContextUtils.getI18nMessage("sany.pdp.login.failed", request);
						}
	                    //out.print(errorMessage+ "登陆失败，请确保输入的用户名和口令是否正确！");
						
	                
			
				}
			}

		}
%>
<%
	}
	
	String userNamelength = ConfigManager.getInstance()
				.getConfigValue("userNameLength");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=Framework.getSystemName("module") %></title>
<!--[if IE 6]> 
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/html/login/js/png.js"></script>
<script type="text/javascript">
DD_belatedPNG.fix('div');
</script>
<![endif]-->
<script type="text/javascript" src="<%=request.getContextPath()%>/include/jquery-1.4.2.min.js"></script>
<link href="<%=request.getContextPath()%>/html/login/stylesheet/login.css"	rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/html/js/commontool.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/common/scripts/WebDes.js"></script>

<script language="JavaScript">
	//最大化窗口
	/**
	var wHeight = window.screen.height-1;
	var wWidth = window.screen.width-1;
	window.moveTo(0,0)
	window.resizeTo(wWidth, wHeight);	

	
   if (window.opener) {
		window.opener.opener=null; 
		window.opener.open('','_self');
		window.opener.close();
	}
	*/
	
	function modifyExpiredPassword(){
		
		
  		var url="<%=request.getContextPath()%>/sysmanager/password/modifyExpiredUserPWD.jsp?<%="userAccount="+userName%>&<pg:dtoken element="param"/>";
  		$.dialog({close:reloadhref,title:'对不起，<%=userName %>的密码已经失效（密码有效期为<%=expiredays%>天），过期时间为<%=expriedtime_%>!',width:1050,height:550, content:'url:'+url,currentwindow:this}); 
  		
		
		
	}
	
	function reloadhref()
	{
		location.href = "login.jsp";
	}
	
	function changcode()
	{
		
		$("#img1").attr("src","Kaptcha.jpg?"+Math.random());
		
	}
	
	
	function getName(){
		loginForm.userName.focus();
		if(document.all.userName.value == ""){
			var aCookie = document.cookie.split("; ");
			for (var i=0; i < aCookie.length; i++){
				var pairSplit=aCookie[i].split("#");
				var un = pairSplit[0].split("=");
				var pn = pairSplit[1].split("=");
				//alert(pairSplit[0]);
				if(un[0]=="USERNAME"){// file://查找n
					loginForm.userName.value=un[1];//  file://取到n的值
				}
				if(pn[0]=="PASSWORD"){
					loginForm.password.value=pn[1];//  file://取到n的值
				}
			}
			loginForm.userName.focus();
		}
	}
	
	function saveName(event){
	
		var s = $("#userName").val();
		var p = $("#password").val();
		var y = $("#rand").val();
		if(y==""){
			$.dialog.alert("验证码不能为空");
			return;
		}
		if((s==""&&p!="")||(s==""&&p=="")){
			$.dialog.alert("<pg:message code='sany.pdp.input.login.name'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	    	$("#userName").focus();
	    	event.returnValue=false;
		    }else if(p==""&&s!=""){
		    $.dialog.alert("<pg:message code='sany.pdp.input.password'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	    	$("#password").focus();
	    	event.returnValue=false;
		    }
		    
	    if(s!=""&&p!=""){
 			document.getElementById('password').value = strEnc(p,s, "", "");
	    	$("#loginForm").submit();
		 }
		   
		/*var ischecked = document.getElementById("remeberpassword").checked;	
		if(ischecked){
			if (s!="" && p!=""){
				s="USERNAME="+s;
				p="PASSWORD="+p;
				document.cookie=s+"#"+p;  // 将保存到客户机中
				//alert(s);
			}
		}*/
		
	}
	
	function reset(){
		$("#loginForm").reset();
	}
	
	function enterKeydowngoU(event){
		var userName = $("#userName").val();
		var password = $("#password").val();
		if(event.keyCode == 13){
		var y = $("#rand").val();
		if(y==""){
			$.dialog.alert("验证码不能为空");
			return;
		}
			if(userName == ""){
				$.dialog.alert("<pg:message code='sany.pdp.input.login.name'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				$("#userName").focus();
				event.returnValue=false;
			}else{
				$("#password").focus();
				event.returnValue=false;
			}
		}
	}
	
	function enterKeydowngoP(event){
		var userName = $("#userName").val();
		var password = $("#password").val();
		if(event.keyCode == 13){
		var y = $("#rand").val();
		if(y==""){
			$.dialog.alert("验证码不能为空");
			return;
		}
			if(userName == "" ){
				$.dialog.alert("<pg:message code='sany.pdp.input.login.name'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				$("#userName").focus();
				event.returnValue=false;
			}else if(userName != "" && password == ""){
				$.dialog.alert("<pg:message code='sany.pdp.input.password'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				$("#password").focus();
				event.returnValue=false;
			}else if(userName != "" && password != ""){
				//loginForm.subsystem_id.focus();
				
				document.getElementById('password').value = strEnc(password,userName, "", "");
				$("#loginForm").submit();
				event.returnValue=false;
			}
		}
	}
	
	
	function enterKeydowngoV(event){
		var userName = $("#userName").val();
		var password = $("#password").val();
		if(event.keyCode == 13){
		var y = $("#rand").val();
		if(y==""){
			$.dialog.alert("验证码不能为空");
			$("#rand").focus();
			return;
		}
			if(userName == "" ){
				$.dialog.alert("<pg:message code='sany.pdp.input.login.name'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				$("#userName").focus();
				event.returnValue=false;
			}else if(userName != "" && password == ""){
				$.dialog.alert("<pg:message code='sany.pdp.input.password'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				$("#password").focus();
				event.returnValue=false;
			}else if(userName != "" && password != ""){
				//loginForm.subsystem_id.focus();
				
				document.getElementById('password').value = strEnc(password,userName, "", "");
				$("#loginForm").submit();
				event.returnValue=false;
			}
		}
	}
	
	function enterKeydowngoS(event){
		var userName = $("#userName").val();
		var password = $("#password").val();
		if(window.event.keyCode == 13){
			if(userName == "" ){
				$.dialog.alert("<pg:message code='sany.pdp.input.login.name'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				$("#userName").focus();
				event.returnValue=false;
			}else if(userName != "" && password == ""){
				$.dialog.alert("<pg:message code='sany.pdp.input.password'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				$("#password").focus();
				event.returnValue=false;
			}else if(userName != "" && password != ""){
				loginForm.ok.focus();
				event.returnValue=false;
			}
		}
	}
	function changeLan(){
		window.location.href="<%=request.getContextPath()%>/sanydesktop/cookieLocale.page?language="+$('#language').val() + "&<pg:dtoken element="param"/>";
	}
</script>
</HEAD>
<body>
<body id="logging_bg">
  <div class="c_log">
	<div class="c_logWrapper">
		<div class="c_logContent">
           <div class="c_log_top">		   </div>
		   <!--从这里开始-->
		   <div class="c_log_min">
		   	<div class="c_log_left" style="margin-left: 12px">
			  <div class="logo"><img src="html/login/images/sany_logo.jpg" />
			  </div>
			</div>
			<form id="loginForm" name="loginForm" action="login.jsp" method="post">
			<pg:dtoken/>
			<input type="hidden" name="flag" value="yes" />
			<input name="macaddr_" type="hidden" />
			<input name="machineName_" type="hidden" />
			<input name="machineIp_" type="hidden" />
			<ul class="c_log_right">
				<%
			  	if(errorMessage==null){
               		//out.print("登陆失败，请确保输入的用户名和口令是否正确！");
               	}
			  	else if(errorMessage.equals("PasswordExpired"))//密码过期
			  	{
			  		out.print("<li><label></label><font color='red'>密码已经失效（有效期为"+expiredays+"天），过期时间为"+expriedtime_+"!<a href='#' onclick='javascript:modifyExpiredPassword()'>点击修改</a></font></li>");
			  		
			  	}
			  		
			  	else{
               		out.print("<li><label></label><font color='red'>"+errorMessage + "</font></li>");
                   } 
		                  %>
				<li><label><pg:message code="sany.pdp.user.login.name"/>：</label><input id="userName" name="userName" type="text" maxlength="<%=userNamelength%>"	 onkeydown="enterKeydowngoU(event)"  /></li>
				<li><label><pg:message code="sany.pdp.login.password"/>：</label><input id="password" name="password" type="password" type="text"	onkeydown="enterKeydowngoP(event)" /></li>
				<%
				if(enable_login_validatecode){
				%><li><label>验证码：</label>
				<input id="rand" name="rand" type="text" style="width:120px" onkeydown="enterKeydowngoV(event)"/><a onclick="changcode()">看不清,换一张</a>
				
				</li>
				<li >
				<img src="Kaptcha.jpg" height="25" width="200" id="img1">
				</li><%} %>
				<li><label><pg:message code="sany.pdp.system"/>：</label>
				<select name="subsystem_id" style="width:160px;margin-left:-110px;">
					<option value="module"
						<%if(system_id == null||system_id.equals("module")){%>
						selected <%}%>>
						<pg:message code="sany.pdp.app.bom.manage"/>
					</option>
					<option value="cms"
						<%if(system_id != null && system_id.equals("cms")){%>
						selected <%}%>>
						<pg:message code="sany.pdp.content.manage"/>
					</option>
					
					<option value="esb"
						<%if(system_id != null && system_id.equals("esb")){%>
						selected <%}%>>
						请求服务平台
					</option>
					
					<option value="dp"
						<%if(system_id != null && system_id.equals("dp")){%>
						selected <%}%>>
						代理商门户
					</option>
					
					
				</select>
				</li>
                <li><label><pg:message code="sany.pdp.style"/>：</label>               
				<select name="loginPath"  style="width:160px;margin-left:-110px;">
					<option value="3"
						<%if(loginStyle == null||loginStyle.equals("3")){%>
						selected <%}%>>
						ISany
					</option>
					<option value="1"
						<%if(loginStyle != null&&loginStyle.equals("1")){%>
						selected <%}%>>
						<pg:message code="sany.pdp.tradition"/>
					</option>
					<option value="2"
						<%if(loginStyle != null&&loginStyle.equals("2")){%>
						selected <%}%>>
						Desktop
					</option>
					<option value="4"
						<%if(loginStyle != null&&loginStyle.equals("4")){%>
						selected <%}%>>
						WEBIsany
					</option>
					
				</select>
				</li>
				<li>
				<label><pg:message code="sany.pdp.language"/>：</label>
				<select name="language" id="language" style="width:160px;margin-left:-110px;" onchange="changeLan()">
				<%if(language.equals("zh_CN")){ %>
					<option value="zh_CN" selected>
						<pg:message code="sany.pdp.language.chinese"/>
					</option>
					<option value="en_US">
						<pg:message code="sany.pdp.language.english"/>
					</option>
					<%}else{ %>
					<option value="zh_CN" >
						<pg:message code="sany.pdp.language.chinese"/>
					</option>
					<option value="en_US" selected>
						<pg:message code="sany.pdp.language.english"/>
					</option>
					<%} %>
				</select>
				</li>
				<li class="log_bts" ><a href="#" class="log_bt c_20"  onclick="saveName(event)"><span><pg:message code="sany.pdp.login"/></span></a><a href="#" class="log_bt log_cancel" onclick="reset()" ><span><pg:message code="sany.pdp.common.operation.reset"/></span></a></li>
			</ul>
			</form>			
			<div class="Zclear"></div> 			
		  </div>
		   <div class="c_log_bot"></div>
		   <p class="c_edition"><pg:message code="sany.pdp.login.copyright"/></p>
      </div>
        <div class="c_logHeight"></div>
	</div>
</div>		

</body>
</html>

