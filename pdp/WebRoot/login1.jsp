<%@ page session="true" language="java"	contentType="text/html;charset=utf-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.security.authorization.AccessException,com.frameworkset.platform.config.ConfigManager,com.liferay.portlet.iframe.action.SSOUserMapping"%>
<%@ page import="com.frameworkset.platform.ca.CaProperties"%>
<%@ page import="com.frameworkset.platform.ca.CAManager"%>
<%@ page import="com.frameworkset.platform.ca.CookieProperties"%>
<%@ page import="com.frameworkset.platform.framework.Framework,com.sany.webseal.LoginValidate.*"%>
	
<%@ page import="java.util.*"%>
<%
	String u = "", p = "", ck = "";

	String successRedirect = request.getParameter("successRedirect");
	
    String userName = request.getParameter("userName");
	Cookie[] cookies = request.getCookies();
	String loginStyle = null;
	String system_id = null;
	if (cookies != null) {
		Map<String, String> cookieMap = new HashMap<String, String>();
		for (Cookie temp : cookies) {
			cookieMap.put(temp.getName(), temp.getValue());
		}
		loginStyle = cookieMap.get("loginStyle");
		system_id = cookieMap.get("subsystem_id");
	}

	String loginPath = request.getParameter("loginPath");
	String subsystem_id = request.getParameter("subsystem_id");
	if (loginPath != null) {
		Cookie loginPathCookie = new Cookie("loginStyle", loginPath);
		loginPathCookie.setMaxAge(3600 * 24);
		response.addCookie(loginPathCookie);
	}
	if (subsystem_id != null) {
		Cookie system_idCookie = new Cookie("subsystem_id",
				subsystem_id);
		system_idCookie.setMaxAge(3600 * 24);
		response.addCookie(system_idCookie);
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
				subsystem = "module";

			if (systemVersion.compareTo("1.0") > 0) {
				AccessControl.getInstance().login(request, response,
						userName, password);

				//  if(subsystem == null) subsystem = "module";
				/** 
				需要全屏时，将response.sendRedirect("index.jsp");注释掉，
				将response.sendRedirect("sysmanager/refactorwindow.jsp");打开
				 */
				if (successRedirect == null) {
					if (loginPath == null || loginPath.equals("1")) {
							successRedirect = "index.jsp?subsystem_id="
									+ subsystem;
						} else if (loginPath.equals("2")) {
							successRedirect = "desktop/desktop1.page";
						} else if (loginPath.equals("3")) {
							successRedirect = "sanydesktop/index.page";
						}
				}
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
					AccessControl.getInstance().login(request,
							response, userName, password);
					if (subsystem == null)
						subsystem = "module";
					if (successRedirect == null) {
						if (loginPath == null || loginPath.equals("1")) {
							successRedirect = "index.jsp?subsystem_id="
									+ subsystem;
						} else if (loginPath.equals("2")) {
							successRedirect = "desktop/desktop1.page";
						} else if (loginPath.equals("3")) {
							successRedirect = "sanydesktop/index.page";
						}
					}
					response.sendRedirect(successRedirect);
					return;
				} else {
					if (subsystem == null)
						subsystem = "module";
					if (successRedirect == null) {
						if (loginPath == null || loginPath.equals("1")) {
							successRedirect = "index.jsp?subsystem_id="
									+ subsystem;
						} else if (loginPath.equals("2")) {
							successRedirect = "desktop/desktop1.page";
						} else if (loginPath.equals("3")) {
							successRedirect = "sanydesktop/index.page";
						}
								
					}
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
	else if(isWebSealServer && userName == null)
	{
		try {
	            String subsystem = request.getParameter("subsystem_id");
	             AccessControl control = AccessControl.getInstance();
				control.checkAccess(request, response, false);
				String user = control.getUserAccount();
				if (user == null || "".equals(user)) {
				 CommonInfo info = new CommonInfo(); 
	             UimUserInfo userinfo = null;
	             String ip = "";
	             try
	             {
		             userinfo = info.validateUIM(request);
		             ip = userinfo.getUser_ip();		             
		             userName = userinfo.getUser_name();
		             try
			         {
			             String password = SSOUserMapping.getUserPassword(userName);
						 AccessControl.getInstance().login(request,
									response, userName, password);
							if (subsystem == null)
								subsystem = "module";
							if (successRedirect == null) {
								if (loginPath == null || loginPath.equals("3")) {
									successRedirect = "sanydesktop/index.page";
								}
								else  if (loginPath.equals("1")) {
									successRedirect = "index.jsp?subsystem_id="
											+ subsystem;
								} else if (loginPath.equals("2")) {
									successRedirect = "desktop/desktop1.page";
								} 
							}
							response.sendRedirect(successRedirect);
							return;
						}
						catch(Exception e)
						{
							
							response.sendRedirect("webseal/websealloginfail.jsp?userName=" + userName + "&ip=" + ip);
							return;
						}	
		         }
		         catch(Exception e)
		         {
		         	
		         }
		         
				} else {
					if (subsystem == null)
						subsystem = "module";
					if (successRedirect == null) {
						if (loginPath == null || loginPath.equals("1")) {
							successRedirect = "index.jsp?subsystem_id="
									+ subsystem;
						} else if (loginPath.equals("2")) {
							successRedirect = "desktop/desktop1.page";
						} else if (loginPath.equals("3")) {
							successRedirect = "sanydesktop/index.page";
						}
								
					}
					response.sendRedirect(successRedirect);
					return;
				}
			
         }
         catch(Throwable ex)
         {
                   String errorMessage = ex.getMessage();
					errorMessage = errorMessage.replaceAll("\\n",
								"\\\\n");
						errorMessage = errorMessage.replaceAll("\\r",
								"\\\\r");
					%>
					<script language="javascript">
	                <!--
	                    alert("<%=errorMessage%>登陆失败，请确保输入的用户名和口令是否正确！");
						
	                //-->
	                </script>
	                <%
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

			String errorMessage = null;
			if (userName != null) {
				try {
					AccessControl.getInstance().login(request,
							response, userName, password);
					String subsystem = request
							.getParameter("subsystem_id");

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
						subsystem = "module";
					/** 
					需要全屏时，将response.sendRedirect("index.jsp");注释掉，
					将response.sendRedirect("sysmanager/refactorwindow.jsp");打开
					 */

					if (successRedirect == null) {
						if (loginPath.equals("1")) {
							successRedirect = "index.jsp?subsystem_id="
									+ subsystem;
						} else if (loginPath.equals("2")) {
							successRedirect = "desktop/desktop1.page";
						} else if (loginPath.equals("3")) {
							successRedirect = "sanydesktop/index.page";
						}

					}
					response.sendRedirect(successRedirect);
					//response.sendRedirect("sysmanager/refactorwindow.jsp?subsystem_id=" + subsystem);
				} catch (AccessException ex) {

					errorMessage = ex.getMessage();
					if (errorMessage != null) {
						errorMessage = errorMessage.replaceAll("\\n",
								"\\\\n");
						errorMessage = errorMessage.replaceAll("\\r",
								"\\\\r");
					}

					//System.out.print(errorMessage);
				%>
					<script language="javascript">
		                <!--
		                	var msg =  "<%=errorMessage%>";
		                	if(msg=="null"){
		                		alert("登陆失败，请确保输入的用户名和口令是否正确！");
		                	}else{
		                    	alert(msg);
		                    }
	                    
	                //-->
	                </script>
				<%
				} catch (Exception ex) {
					errorMessage = ex.getMessage();
					errorMessage = errorMessage.replaceAll("\\n",
								"\\\\n");
						errorMessage = errorMessage.replaceAll("\\r",
								"\\\\r");
					%>
					<script language="javascript">
	                <!--
	                    alert("<%=errorMessage%>"+ "登陆失败，请确保输入的用户名和口令是否正确！");
						
	                //-->
	                </script>
			<%
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

<title>应用台账管理系统</title>
<!--[if IE 6]> 
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/html/login/js/png.js"></script>
<script type="text/javascript">
DD_belatedPNG.fix('div');
</script>
<![endif]-->
<script type="text/javascript" src="<%=request.getContextPath()%>/include/jquery-1.4.2.min.js"></script>
<link href="<%=request.getContextPath()%>/html/login/stylesheet/login.css"	rel="stylesheet" type="text/css" />
<script language="JavaScript">
	//最大化窗口
	var wHeight = window.screen.height-1;
	var wWidth = window.screen.width-1;
	window.moveTo(0,0)
	window.resizeTo(wWidth, wHeight);	

	/**
   if (window.opener) {
		window.opener.opener=null; 
		window.opener.open('','_self');
		window.opener.close();
	}
	*/
	
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
	
	function saveName(){
	
		var s = $("#userName").val();
		var p = $("#password").val();
		if((s==""&&p!="")||(s==""&&p=="")){
		    alert("请输入用户名！");
	    	$("#userName").focus();
	    	window.event.returnValue=false;
		    }else if(p==""&&s!=""){
		    alert("请输入密码！");
	    	$("#password").focus();
	    	window.event.returnValue=false;
		    }
		    
	    if(s!=""&&p!=""){
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
	
	function enterKeydowngoU(){
		var userName = $("#userName").val();
		var password = $("#password").val();
		if(window.event.keyCode == 13){
			if(userName == ""){
				alert("请输入用户名！");
				$("#userName").focus();
				window.event.returnValue=false;
			}else{
				("#password").focus();
				window.event.returnValue=false;
			}
		}
	}
	
	function enterKeydowngoP(){
		var userName = $("#userName").val();
		var password = $("#password").val();
		if(window.event.keyCode == 13){
			if(userName == "" ){
				alert("请输入用户名！");
				$("#userName").focus();
				window.event.returnValue=false;
			}else if(userName != "" && password == ""){
				alert("请输入密码！");
				$("#password").focus();
				window.event.returnValue=false;
			}else if(userName != "" && password != ""){
				//loginForm.subsystem_id.focus();
				$("#loginForm").submit();
				window.event.returnValue=false;
			}
		}
	}
	
	function enterKeydowngoS(){
		var userName = $("#userName").val();
		var password = $("#password").val();
		if(window.event.keyCode == 13){
			if(userName == "" ){
				alert("请输入用户名！");
				$("#userName").focus();
				window.event.returnValue=false;
			}else if(userName != "" && password == ""){
				alert("请输入密码！");
				$("#password").focus();
				window.event.returnValue=false;
			}else if(userName != "" && password != ""){
				loginForm.ok.focus();
				window.event.returnValue=false;
			}
		}
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
		   	<div class="c_log_left">
			  <div class="logo"><img src="html/login/images/sany_logo.jpg" /></div>
			</div>
			<form id="loginForm" name="loginForm" action="login.jsp" method="post">
			<input type="hidden" name="flag" value="yes" />
			<input name="macaddr_" type="hidden" />
			<input name="machineName_" type="hidden" />
			<input name="machineIp_" type="hidden" />
			<ul class="c_log_right">
				<li><label>用户名：</label><input id="userName" name="userName" type="text" maxlength="<%=userNamelength%>"	 onkeydown="enterKeydowngoU()"  /></li>
				<li><label>密&nbsp;&nbsp;&nbsp;&nbsp;码：</label><input id="password" name="password" type="password" type="text"	onkeydown="enterKeydowngoP()" /></li>
				<li><label>系&nbsp;&nbsp;&nbsp;&nbsp;统：</label>
				<select name="subsystem_id" style="width:160px;margin-left:-110px;">
					<option value="module"
						<%if(system_id == null||system_id.equals("module")){%>
						selected <%}%>>
						应用台账管理
					</option>
					
					
				</select>
				</li>
                <li><label>风&nbsp;&nbsp;&nbsp;&nbsp;格：</label>               
				<select name="loginPath"  style="width:160px;margin-left:-110px;">
					<option value="3"
						<%if(loginStyle == null||loginStyle.equals("3")){%>
						selected <%}%>>
						ISany风格
					</option>
					<option value="1"
						<%if(loginStyle != null&&loginStyle.equals("1")){%>
						selected <%}%>>
						传统页面风格
					</option>
					<option value="2"
						<%if(loginStyle != null&&loginStyle.equals("2")){%>
						selected <%}%>>
						desktop风格
					</option>
					
				</select>
				</li>
				<li class="log_bts" ><a href="javascript:void(0)" class="log_bt c_20"  onclick="saveName()"><span>登 录</span></a><a href="javascript:void(0)" class="log_bt log_cancel" onclick="reset()" ><span>重 置</span></a></li>
			</ul>
			</form>			
			<div class="Zclear"></div> 			
		  </div>
		   <div class="c_log_bot"></div>
		   <p class="c_edition">版权所有：三一集团&nbsp;&nbsp;&nbsp;&nbsp;开发者：IT总部&nbsp;&nbsp;&nbsp;&nbsp;版本号：V1.0</p> 
      </div>
        <div class="c_logHeight"></div>
	</div>
</div>		

</body>
</html>

