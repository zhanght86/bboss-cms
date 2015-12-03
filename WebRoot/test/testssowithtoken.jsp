<%@page import="org.apache.cxf.jaxws.JaxWsProxyFactoryBean"%>
<%@page import="org.frameworkset.web.token.TokenStore"%>
<%@page import="org.frameworkset.web.token.ws.TokenService"%>
<%@page import="com.caucho.hessian.client.HessianProxyFactory"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page session="false" language="java"
	contentType="text/html; charset=utf-8"%>
<%@page import="org.frameworkset.web.token.TokenHelper"%>

<%
/**
*  本势力
*
*
*/
String appid = "tas";
String secret = "2d66d96f-ada4-4e12-a4e4-f4541c0b4bea";

String account = "marc";//如果使用工号则loginType为2，否则为1
account = request.getParameter("account");
if(account == null)
	account = "yinbp";//如果使用工号则loginType为2，否则为1
String worknumber = "10006857";
if(account .equals("yinbp"))
	worknumber = "10006673";//如果使用工号则loginType为2，否则为1
	
//姓名	性别	工号	手机	办公电话	邮箱	一级部门	二级部门	三级部门	岗位
//卿琳	男	21018433	13786144568	0731-85355081	qingl2@sany.com.cn	流程信息化总部	信息应用部	软件架构科	软件工程师	
if(account .equals("qingl2"))
	worknumber = "21018433";//如果使用工号则loginType为2，否则为1
String tokenparamname = TokenStore.temptoken_param_name;

//hessian服务方式申请token
HessianProxyFactory factory = new HessianProxyFactory();
//String url = "http://localhost:8080/context/hessian?service=tokenService";
//String url = "http://10.0.15.223/SanyToken/hessian?service=tokenService";
String url = "http://localhost:8080/SanyPDP/hessian?service=tokenService";
TokenService tokenService = (TokenService) factory.create(TokenService.class, url);
//通过hessian根据账号或者工号获取ticket

String ticket = tokenService.genTicket(account, worknumber, appid, secret);


//hessian服务方式申请token
factory = new HessianProxyFactory();
//String url = "http://localhost:8080/context/hessian?service=tokenService";
//url = "http://10.0.15.223/SanyToken/hessian?service=checktokenService";
url = "http://localhost:8080/SanyPDP/hessian?service=checktokenService";

org.frameworkset.web.token.ws.CheckTokenService  checkTokenService = (org.frameworkset.web.token.ws.CheckTokenService) factory.create(org.frameworkset.web.token.ws.CheckTokenService.class, url);
org.frameworkset.web.token.ws.TokenCheckResponse tokenCheckResponse = checkTokenService.checkTicket(appid, secret, ticket);
System.out.println(tokenCheckResponse.getResultcode());
System.out.println(tokenCheckResponse.isValidateResult());
System.out.println(tokenCheckResponse.getUserAccount());
System.out.println(tokenCheckResponse.getWorknumber());

String token = tokenService.genAuthTempToken(appid, secret, ticket);
//token = tokenService.genDualToken(appid, secret, ticket);
/**
* webservice方式申请token
*/
//url = "http://10.0.15.223/SanyToken/cxfservices/tokenService";
//JaxWsProxyFactoryBean WSServiceClientFactory = new  JaxWsProxyFactoryBean();
//WSServiceClientFactory.setAddress(url);
//WSServiceClientFactory.setServiceClass(TokenService.class);
//tokenService = (TokenService)WSServiceClientFactory.create();
//通过webservice根据账号或者工号获取ticket
//String ticket = tokenService.genTicket(account, worknumber, appid, secret);
//token = tokenService.genAuthTempToken(appid, secret, ticket);
//token = tokenService.genDualToken(appid, secret, ticket);
/**
* http请求方式申请令牌
*/
//url = "http://10.0.15.223/SanyToken/token/genAuthTempToken.freepage?appid="+appid + "&secret="+secret + "&ticket="+ticket;
//url = "http://10.25.192.142:8080/SanyPDP/token/genDualToken.freepage?appid="+appid + "&secret="+secret + "&account="+account;
//token = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);
//通过http根据账号或者工号获取ticket
//url = "http://10.25.192.142:8080/SanyPDP/token/genTicket.freepage?appid="+appid + "&secret="+secret + "&account="+account + "&worknumber="+worknumber;
//String ticket = = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);

//org.frameworkset.web.servlet.context.WebApplicationContext  context = org.frameworkset.web.servlet.support.WebApplicationContextUtils.getWebApplicationContext();//获取实例

//通过以下方式获取mvc容器中的组件实例方法
//tokenService = context.getTBeanObject("/token/*.freepage", TokenService.class);
//String ticket = tokenService.genTicket(account, worknumber, appid, secret);
//token = tokenService.genAuthTempToken(appid, secret, ticket);

out.println("<div>isGuest:"+AccessControl.getAccessControl().isGuest()+"</div>");
out.println("<div>userAccount:"+AccessControl.getAccessControl().getUserAccount()+"</div>");

out.println("<div>账号token:"+tokenparamname + "=" + token+"</div>");

String accounttokenrequest = "_dt_token_=" + token ;
String accountticketrequest = "_dt_ticket_=" + ticket ;
String erroraccountticketrequest = "_dt_ticket_=w";//this is an error ticket;

//工号token

//token = tokenService.genAuthTempToken(appid, secret, ticket);
out.println("<div>工号token:"+tokenparamname + "=" + token+"</div>");

String worknumbertokenrequest =  "_dt_token_=" + token ;
 %>
<html>
<head>
<title>签名令牌-单点登录界面</title>
</head>
<body>
<table>
<tr><td>ticket sso</td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithticket.page?<%=accountticketrequest %>&loginMenu=appbommanager&subsystem_id=module">创建领料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithticket.page?<%=erroraccountticketrequest %>&loginMenu=appbommanager&subsystem_id=module">创建领料单-错误的ticket</a></td></tr>
</table>
<table>
<table>
<tr><td>账号登录</td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&loginMenu=appbommanager&subsystem_id=module">创建领料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&loginMenu=appbommanager&subsystem_id=module">创建退料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&loginMenu=appbommanager&subsystem_id=module">工单管理</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&loginMenu=appbommanager&subsystem_id=module">台账查询</a></td></tr>
</table>
<table>
<tr><td>账号登录-直接跳转到指定页面</td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">创建领料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">创建退料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">工单管理</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">台账查询</a></td></tr>
</table>
<table>
<tr><td>工号登录</td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&loginMenu=appbommanager&subsystem_id=module">创建领料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&loginMenu=appbommanager&subsystem_id=module">创建退料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&loginMenu=appbommanager&subsystem_id=module">工单管理</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&loginMenu=appbommanager&subsystem_id=module">台账查询</a></td></tr>
</table>

<table>
<tr><td>工号登录-直接跳转到指定页面</td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">创建领料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">创建退料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">工单管理</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">台账查询</a></td></tr>
</table>
</body>
</html>