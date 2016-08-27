<%@ page session="true" language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.security.authorization.AccessException,com.frameworkset.platform.config.ConfigManager,com.liferay.portlet.iframe.action.SSOUserMapping"%>
<%@page import="com.frameworkset.platform.ca.CaProperties"%>
<%@page import="com.frameworkset.platform.ca.CAManager"%>
<%@page import="com.frameworkset.platform.ca.CookieProperties"%>

<%
	String successRedirect = request.getParameter("successRedirect");
	//System.out.println("successRedirect:" + successRedirect);
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
            String subsystem = "trans_client";
            String password = SSOUserMapping.getUserPassword(userName);
            if (subsystem == null)
                subsystem = "module";
            
            
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
	            	successRedirect = "index.jsp?subsystem_id=" + subsystem;
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
                        subsystem = "module";
                    if(successRedirect == null)
		            {
		            	successRedirect = "index.jsp?subsystem_id=" + subsystem;
		            }
                    response.sendRedirect(successRedirect);
                }
                else
                {
                    if (subsystem == null)
                        subsystem = "module";
                    if(successRedirect == null)
		            {
		            	successRedirect = "index.jsp?subsystem_id=" + subsystem;
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
                    String subsystem = "trans_client";
                    if(!isCert){
                    	CAManager.updateUserCERT_SN(request,userName);       	
                    }
                    if(CA_LOGIN_SERVER)
                    	request.getSession().setAttribute(CookieProperties.CERT_SN, CAManager.getCookieValue(request, CookieProperties.CERT_SN));
                    if (subsystem == null)
                        subsystem = "module";
                    /** 
                    需要全屏时，将response.sendRedirect("index.jsp");注释掉，
                    将response.sendRedirect("sysmanager/refactorwindow.jsp");打开
                     */
                    if(successRedirect == null)
		            {
		            	successRedirect = "index.jsp?subsystem_id=" + subsystem;
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
<HTML><HEAD><TITLE>系统登录</TITLE>
<link href="cms/inc/css/cms.css" rel="stylesheet" type="text/css">
<META http-equiv=Content-Type content="text/html; charset=UTF-8">

<META content="MSHTML 6.00.3790.449" name=GENERATOR>
<style type="text/css">
<!--
.input_text {border:1px #77849E solid;height:18px; width:120px; font-family:Verdana;font-size: 12px; color:#566682
}
.copyrightbox{margin-top:8px; font-family:Verdana, Arial, Helvetica, sans-serif; color:#E1E9F0; text-align:center}
.copyrightbox a:link{ text-decoration:none;color:#ffffff;}
.copyrightbox a:visited{ text-decoration:none;color:#ffffff;}
.copyrightbox a:hover{ text-decoration:underline;color:#EAE2CD;}

-->
</style>
<script language="JavaScript">
	function getName(){
		if(document.all.userName.value == ""){
			var aCookie = document.cookie.split("; ");
			for (var i=0; i < aCookie.length; i++){
				var pairSplit=aCookie[i].split("=");
				//alert(pairSplit[0]);
				if (pairSplit[0]=="USERNAME")// file://查找n
					loginForm.userName.value=pairSplit[1];//  file://取到n的值
			}
			loginForm.userName.focus();
		}
	}
	
	function saveName(){
		var s=loginForm.userName.value;
		if (s!="")   // 非空则保存
		{
			s="USERNAME="+s;
			//alert(s);
			document.cookie=s;  // 将保存到客户机中
		}
		document.loginForm.submit();
	}
	
	function enterKeydowngoU(){
		if(window.event.keyCode == 13){
			if(loginForm.userName.value == "" && loginForm.userName.value == ""){
				alert("请输入用户名！");
				loginForm.userName.focus();
			}else{
				loginForm.password.focus();
			}
		}
	}
	
	function enterKeydowngoP(){
		if(window.event.keyCode == 13){
			if(loginForm.userName.value == "" && loginForm.userName.value == ""){
				alert("请输入用户名！");
				loginForm.userName.focus();
			}else if(loginForm.userName.value != "" && loginForm.password.value == ""){
				alert("请输入密码！");
				loginForm.password.focus();
			}
			//else if(loginForm.userName.value != "" && loginForm.password.value != ""){
			//	loginForm.subsystem_id.focus();
			//}
			else if(loginForm.userName.value != "" && loginForm.password.value != ""){
				loginForm.ok.focus();
			}
		}
	}
	
	function enterKeydowngoS(){
		if(window.event.keyCode == 13){
			if(loginForm.userName.value == "" && loginForm.userName.value == ""){
				alert("请输入用户名！");
				loginForm.userName.focus();
			}else if(loginForm.userName.value != "" && loginForm.password.value == ""){
				alert("请输入密码！");
				loginForm.password.focus();
			}else if(loginForm.userName.value != "" && loginForm.password.value != ""){
				loginForm.ok.focus();
			}
		}
	}
	
	
</script>
<SCRIPT language=JScript event="OnCompleted(hResult,pErrorObject, pAsyncContext)" for=foo>
document.forms[0].macaddr_.value=unescape(MACAddr);
document.forms[0].machineName_.value=unescape(sDNSName);
document.forms[0].machineIp_.value=unescape(IPAddr);
//document.formbar.submit();
  </SCRIPT>
<SCRIPT language=JScript event=OnObjectReady(objObject,objAsyncContext) for=foo>
   if(objObject.IPEnabled != null && objObject.IPEnabled != "undefined" && objObject.IPEnabled == true)
   {
    if(objObject.MACAddress != null && objObject.MACAddress != "undefined")
    MACAddr = objObject.MACAddress;
    if(objObject.IPEnabled && objObject.IPAddress(0) != null && objObject.IPAddress(0) != "undefined")
    IPAddr = objObject.IPAddress(0);
    if(objObject.DNSHostName != null && objObject.DNSHostName != "undefined")
    sDNSName = objObject.DNSHostName;
    }
</SCRIPT>

<OBJECT id=locator classid=CLSID:76A64158-CB41-11D1-8B02-00600806D9B6 VIEWASTEXT></OBJECT>
<OBJECT id=foo classid=CLSID:75718C9A-F029-11d1-A1AC-00C04FB6C223></OBJECT>
<SCRIPT language=JScript>
   var service = locator.ConnectServer();
   var MACAddr ;
   var IPAddr ;
   var DomainAddr;
   var sDNSName;
   service.Security_.ImpersonationLevel=3;
   service.InstancesOfAsync(foo, 'Win32_NetworkAdapterConfiguration');  
</SCRIPT>
</HEAD>
<BODY onload="getName()" leftMargin=0 topMargin=0 style="overflow-y:hidden; background:url(mq/images/login_Bottom_bg.jpg)">
<form name="loginForm" action="clientMonitorlogin.jsp" method="post"><input type="hidden" name="flag" value="yes">	
  <TABLE width=776 height="100%" border=0 align=center cellPadding=0 cellSpacing=0>
  <input name="macaddr_" type="hidden"/>
  <input name="machineName_" type="hidden"/>
  <input name="machineIp_" type="hidden"/>
   <TR>
    <TD width="75" height=400 colspan="2"><div align="center">
      <table width="776" height="413" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td background="mq/images/clientmonitor.jpg" style="background-repeat:no-repeat"><div align="center">
            <table width="100%" height="410" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td>&nbsp;</td>
                <td height="150">&nbsp;</td>
              </tr>
              <%if(certUserName == null || certUserPassword == null){ 
              		if(!isCert){
              			String cert_sn = CAManager.getCookieValue(request,CookieProperties.CERT_SN);
              %>
              <tr>
                <td width="46%"></td>
                <td width="54%" height="27">
                <font color="red">
                	当前U-key编号：<%=cert_sn %>,还没有被用户绑定!<br/>请输入需要绑定的用户登录应用。如绑定成功（即正常登录应用）下次不需要用户再输入帐户和密码
      			</font>          
                </td>
              </tr>
              <%			
              		}
              %>
              <tr>
                <td width="46%">&nbsp;</td>
                <td width="54%" height="27">用户名：
                	<input name="userName" type="text" size="15" value="" class="input_text" 
                		maxlength="<%=userNamelength%>" onkeydown="enterKeydowngoU()">
                </td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td height="27">密　码：
                	<input name="password"  type="password" size="15"  class="input_text" 
                		onkeydown="enterKeydowngoP()"></td>
              </tr>
              <%}else{ %>
              	<tr>
              	<td width="46%">&nbsp;</td>
                <td width="54%" height="27">用  户：<%=certUserName %> 通过认证，请选择入口进入应用</td>
              	</tr>
              	<input name="userName" type="hidden" size="15" value="<%=certUserName%>" >
              	<input name="password" type="hidden" size="15" value="<%=certUserPassword%>" >
                		
              <%} %>
              <tr>
                <td height="5" colspan="2"></td>
                </tr>
              <tr>
                <td>&nbsp;</td>
                <td height="27"><label>&nbsp;&nbsp;
                  <input name="ok" type="button" class="cms_button" value="登录" onclick="saveName()" >&nbsp;&nbsp;&nbsp;
                  <input name="clear" type="reset" class="cms_button" value="清除" >
                </label></td>
              </tr>
              <tr>
                <td height="50">&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td height="28">&nbsp;</td>
              </tr>
              <tr>
                <td height="27" colspan="2"><div class="copyrightbox"> CopyRight 2007&nbsp;::::::::::::::::::::<a href="http://www.bbossgroups.com" target="_blank">访问三一集团网站</a></div></td>
                </tr>
            </table>
          </div></td>
        </tr>
      </table>
    </div></TD>
    </TR>
  </TABLE> 
</form>
</BODY></HTML>
<%
    }
%>
