<%@ page session="true" language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.security.authorization.AccessException,com.frameworkset.platform.config.ConfigManager,com.liferay.portlet.iframe.action.SSOUserMapping"%>
<%@ page import="com.frameworkset.platform.ca.CaProperties"%>
<%@ page import="com.frameworkset.platform.ca.CAManager"%>
<%@ page import="com.frameworkset.platform.ca.CookieProperties"%>
<%@ page import="com.frameworkset.platform.framework.Framework"%>

<%@ page import="java.util.*"%>
<%
	String u = "", p = "", ck = "";
	
	String successRedirect = request.getParameter("successRedirect");
	System.out.println("successRedirect:" + successRedirect);
    boolean isCasServer = ConfigManager.getInstance().getConfigBooleanValue("isCasServer", false);
    //是否使用数字认证中心服务
    boolean CA_LOGIN_SERVER = CaProperties.CA_LOGIN_SERVER;
    
    boolean isCert = true;
    String certUserName = null;
    String certUserPassword = null;
    if(CA_LOGIN_SERVER){
    	isCert = CAManager.checkCertSn(request);
    	if(isCert){
    		certUserName = CAManager.getUserName(request);
    		certUserPassword = CAManager.getUserPassword(request);
    	}
    }

    if (isCasServer)
    {
        String userName = (String) session.getAttribute("edu.yale.its.tp.cas.client.filter.user");
        boolean state = false;
        if (userName != null && !"".equals(userName))
        {
            state = SSOUserMapping.isIncludeUser(userName);
        }
        if (state)
        {
            //系统管理版本号，2.0和2.0以上的版本,默认版本为1.0
            String systemVersion = ConfigManager.getInstance().getConfigValue("system.version", "1.0");
            String subsystem = request.getParameter("subsystem_id");
            String password = SSOUserMapping.getUserPassword(userName);
            if (subsystem == null)
                subsystem = "esb";
            
            
            if (systemVersion.compareTo("1.0") > 0)
            {
                AccessControl.getInstance().login(request, response, userName, password);

                //  if(subsystem == null) subsystem = "module";
                /** 
                需要全屏时，将response.sendRedirect("index.jsp");注释掉，
                将response.sendRedirect("sysmanager/refactorwindow.jsp");打开
                 */
                if(successRedirect == null)
	            {
	            	successRedirect = "desktop/desktop1.page";
	            }
                response.sendRedirect(successRedirect);
            }
            else
            {
                AccessControl control = AccessControl.getInstance();
                control.checkAccess(request, response, false);
                String user = control.getUserAccount();
                if (user == null || "".equals(user) || !userName.equals(user))
                {
                    AccessControl.getInstance().login(request, response, userName, password);
                    if (subsystem == null)
                        subsystem = "esb";
                    if(successRedirect == null)
		            {
		            	successRedirect = "desktop/desktop1.page";
		            }
                    response.sendRedirect(successRedirect);
                }
                else
                {
                    if (subsystem == null)
                        subsystem = "esb";
                    if(successRedirect == null)
		            {
		            	successRedirect = "desktop/desktop1.page";
		            }
                    response.sendRedirect(successRedirect);
                }
            }
        }
        else
        {
        	if(userName == null || userName.equals(""))
        	{
            	out.print("系统启用了cas单点登录功能，请在web.xml的CAS Filte中拦截login.jsp页面");
            }
            else
            {
            	out.print("用户【" + userName + "】在此应用中没有开通！ ");
            }
        }
    }
    else
    {
        //System.out.println(session.getSessionContext());
        String flag = request.getParameter("flag"); //是否触发提交
        AccessControl dd = AccessControl.getInstance();
        //登陆名称的长度
        String userNamelength = ConfigManager.getInstance().getConfigValue("userNameLength");

        if (flag == null)
        {
        }
        else
        {
        	//String successRedirect = request.getParameter("successRedirect");
            
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            
            String errorMessage = null;
            if (userName != null)
            {
                try
                {
                    AccessControl.getInstance().login(request, response, userName, password);
                    String subsystem = request.getParameter("subsystem_id");
                    
                  	
                    //System.out.println("orgName========================"+orgName);
                    //System.out.println("orgId========================"+orgId);
                    if(!isCert){
                    	CAManager.updateUserCERT_SN(request,userName);       	
                    }
                    if(CA_LOGIN_SERVER)
                    	request.getSession().setAttribute(CookieProperties.CERT_SN, CAManager.getCookieValue(request, CookieProperties.CERT_SN));
                    if (subsystem == null)
                        subsystem = "esb";
                    /** 
                    需要全屏时，将response.sendRedirect("index.jsp");注释掉，
                    将response.sendRedirect("sysmanager/refactorwindow.jsp");打开
                     */
                    
                    if(successRedirect == null)
		            {
                    	
		            	successRedirect = "desktop/desktop1.page";
		            }
                    response.sendRedirect(successRedirect);
                    //response.sendRedirect("sysmanager/refactorwindow.jsp?subsystem_id=" + subsystem);
                }
                catch (AccessException ex)
                {

                    errorMessage = ex.getMessage();
                    if (errorMessage != null)
                    {
                        errorMessage = errorMessage.replaceAll("\\n", "\\\\n");
                        errorMessage = errorMessage.replaceAll("\\r", "\\\\r");
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
                }
                            catch (Exception ex)
                            {
                                errorMessage = ex.getMessage();
                                System.out.print(errorMessage);
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN" >
<html>
<head><title>南宁市公安局数据调度平台</title>

<link href="<%=request.getContextPath() %>/css/login.css" rel="stylesheet" type="text/css">
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<META content="MSHTML 6.00.3790.449" name=GENERATOR>

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
		var s = loginForm.userName.value;
		var p = loginForm.password.value;
		
		if((s==""&&p!="")||(s==""&&p=="")){
		    alert("请输入用户名！");
	    	loginForm.userName.focus();
	    	window.event.returnValue=false;
		    }else if(p==""&&s!=""){
		    alert("请输入密码！");
	    	loginForm.password.focus();
	    	window.event.returnValue=false;
		    }
	    if(s!=""&&p!=""){
	    	document.loginForm.submit();
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
	
	function enterKeydowngoU(){
		if(window.event.keyCode == 13){
			if(loginForm.userName.value == "" && loginForm.userName.value == ""){
				alert("请输入用户名！");
				loginForm.userName.focus();
				window.event.returnValue=false;
			}else{
				loginForm.password.focus();
				window.event.returnValue=false;
			}
		}
	}
	
	function enterKeydowngoP(){
		if(window.event.keyCode == 13){
			if(loginForm.userName.value == "" && loginForm.userName.value == ""){
				alert("请输入用户名！");
				loginForm.userName.focus();
				window.event.returnValue=false;
			}else if(loginForm.userName.value != "" && loginForm.password.value == ""){
				alert("请输入密码！");
				loginForm.password.focus();
				window.event.returnValue=false;
			}else if(loginForm.userName.value != "" && loginForm.password.value != ""){
				//loginForm.subsystem_id.focus();
				loginForm.submit();
				window.event.returnValue=false;
			}
		}
	}
	
	function enterKeydowngoS(){
		if(window.event.keyCode == 13){
			if(loginForm.userName.value == "" && loginForm.userName.value == ""){
				alert("请输入用户名！");
				loginForm.userName.focus();
				window.event.returnValue=false;
			}else if(loginForm.userName.value != "" && loginForm.password.value == ""){
				alert("请输入密码！");
				loginForm.password.focus();
				window.event.returnValue=false;
			}else if(loginForm.userName.value != "" && loginForm.password.value != ""){
				loginForm.ok.focus();
				window.event.returnValue=false;
			}
		}
	}
	
	
</script>





</HEAD>
<body >
<form name="loginForm" action="desktop_login_1.jsp" method="post"><input type="hidden" name="flag" value="yes">	

<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#5693bd">

  <tr>
    <td align="center" valign="middle" height="100%" width="100%">
    <input name="macaddr_" type="hidden"/>
	<input name="machineName_" type="hidden"/>
	<input name="machineIp_" type="hidden"/>
    <table width="991" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td><img src="images/login_01.gif" width="991" height="161" /></td>
  </tr>
  <tr>
    <td><img src="images/login_02.gif" width="991" height="117" /></td>
  </tr>
  <tr>
    <td width="991" height="212" valign="top" background="images/login_03.gif">
    	<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="562">&nbsp;</td>
    <td><table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td colspan="2"  height="70">&nbsp;</td>
        </tr>
      <tr>
        <td width="90" height="40" align="right"><img src="images/login_06.gif" ></td>
        <td ><input name="userName" type="text" class="dl_border" maxlength="<%=userNamelength%>" onkeydown="enterKeydowngoU()" /></td>
        <td>&nbsp;</td>
      </tr>
      <tr>
		<td  height="40" align="right"><img src="images/login_07.gif" ></td>
        <td><input name="password" type="password" type="text" class="dl_border" onkeydown="enterKeydowngoP()" /></td>
      </tr>
      <tr>
        <td colspan="2" align="center">
        <input type="image" src="images/login_05.gif" width="118" height="41" onclick="saveName()"	 /></td>
        </tr>
    </table></td>
  </tr>
</table>
    
    </td>
  </tr>
  <tr>
    <td><img src="images/login_04.gif" width="991" height="89" /><input name="subsystem_id" type="hidden" value="esb"></input></td>
  </tr>
</table>
    
    </td>
  </tr>
</table>

</form>

</body></html>
<%
    }
%>
