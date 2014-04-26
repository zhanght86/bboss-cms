<%@page import="org.apache.cxf.jaxws.JaxWsProxyFactoryBean"%>
<%@page import="org.frameworkset.web.token.TokenStore"%>
<%@page import="com.sany.common.action.TokenService"%>
<%@page import="com.caucho.hessian.client.HessianProxyFactory"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8"%>
<%@page import="org.frameworkset.web.token.TokenHelper"%>

<%
/**
*  本势力
*
*
*/
String appid = "sim";
String secret = "A75399B0158B6A1AABBF8F7C3211EB13";
String account = "yinbp";//如果使用工号则loginType为2，否则为1
String worknumber = "10006673";
String tokenparamname = TokenStore.temptoken_param_name;

//hessian服务方式申请token
HessianProxyFactory factory = new HessianProxyFactory();
//String url = "http://localhost:8081/context/hessian?service=tokenService";
String url = "http://localhost:8080/SanyPDP/hessian?service=tokenService";
TokenService tokenService = (TokenService) factory.create(TokenService.class, url);
//通过hessian根据账号或者工号获取ticket

String ticket = tokenService.genTicket(account, worknumber, appid, secret);
String token = tokenService.genAuthTempToken(appid, secret, ticket);
//token = tokenService.genDualToken(appid, secret, ticket);
/**
* webservice方式申请token
*/
url = "http://localhost:8080/SanyPDP/cxfservices/tokenService";
JaxWsProxyFactoryBean WSServiceClientFactory = new  JaxWsProxyFactoryBean();
WSServiceClientFactory.setAddress(url);
WSServiceClientFactory.setServiceClass(TokenService.class);
tokenService = (TokenService)WSServiceClientFactory.create();
//通过webservice根据账号或者工号获取ticket
//String ticket = tokenService.genTicket(account, worknumber, appid, secret);
token = tokenService.genAuthTempToken(appid, secret, ticket);
//token = tokenService.genDualToken(appid, secret, ticket);
/**
* http请求方式申请令牌
*/
url = "http://localhost:8080/SanyPDP/token/genAuthTempToken.freepage?appid="+appid + "&secret="+secret + "&ticket="+ticket;
//url = "http://10.25.192.142:8081/SanyPDP/token/genDualToken.freepage?appid="+appid + "&secret="+secret + "&account="+account;
token = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);
//通过http根据账号或者工号获取ticket
//url = "http://10.25.192.142:8081/SanyPDP/token/genTicket.freepage?appid="+appid + "&secret="+secret + "&account="+account + "&worknumber="+worknumber;
//String ticket = = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);

org.frameworkset.web.servlet.context.WebApplicationContext  context = org.frameworkset.web.servlet.support.WebApplicationContextUtils.getWebApplicationContext();//获取实例

//通过以下方式获取mvc容器中的组件实例方法
tokenService = context.getTBeanObject("/token/*.freepage", TokenService.class);
//String ticket = tokenService.genTicket(account, worknumber, appid, secret);
token = tokenService.genAuthTempToken(appid, secret, ticket);

out.println("<div>isGuest:"+AccessControl.getAccessControl().isGuest()+"</div>");
out.println("<div>userAccount:"+AccessControl.getAccessControl().getUserAccount()+"</div>");

out.println("<div>账号token:"+tokenparamname + "=" + token+"</div>");

String accounttokenrequest = tokenparamname + "=" + token + "&appid=" + appid + "&secret="+secret;
//工号token
account = "10006673";//如果使用工号则loginType为2，否则为1
token = tokenService.genAuthTempToken(appid, secret, ticket);
out.println("<div>工号token:"+tokenparamname + "=" + token+"</div>");

String worknumbertokenrequest = tokenparamname + "=" + token + "&appid=" + appid + "&secret="+secret;
 %>
<html>
<head>
<title>签名令牌-单点登录界面</title>
</head>
<body>
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