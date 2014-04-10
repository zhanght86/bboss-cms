<%@page import="org.apache.cxf.jaxws.JaxWsProxyFactoryBean"%>
<%@page import="org.frameworkset.web.token.TokenStore"%>
<%@page import="com.sany.common.action.TokenService"%>
<%@page import="com.caucho.hessian.client.HessianProxyFactory"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8"%>

<%
/**
*  本势力
*
*
*/
String appid = "appid";
String secret = "xxxxxxxxxxxxxxxxxxxxxx";
String account = "yinbp";//如果使用工号则loginType为2，否则为1

String tokenparamname = TokenStore.temptoken_param_name;
//hessian服务方式申请token
HessianProxyFactory factory = new HessianProxyFactory();
//String url = "http://10.25.192.142:8081/context/hessian?service=tokenService";
String url = "http://192.168.1.101:8080"+request.getContextPath()+"/hessian?service=tokenService";
TokenService tokenService = (TokenService) factory.create(TokenService.class, url);
String token = tokenService.genAuthTempToken(appid, secret, account);
//token = tokenService.genDualToken(appid, secret, account);
/**
* webservice方式申请token
*/
url = "http://192.168.1.101:8080/SanyPDP/cxfservices/tokenService";
JaxWsProxyFactoryBean WSServiceClientFactory = new  JaxWsProxyFactoryBean();
WSServiceClientFactory.setAddress(url);
WSServiceClientFactory.setServiceClass(TokenService.class);
tokenService = (TokenService)WSServiceClientFactory.create();
token = tokenService.genAuthTempToken(appid, secret, account);
//token = tokenService.genDualToken(appid, secret, account);
/**
* http请求方式申请令牌
*/
url = "http://192.168.1.101:8080/SanyPDP/token/genAuthTempToken.freepage?appid="+appid + "&secret="+secret + "&account="+account;
//url = "http://10.25.192.142:8081/SanyPDP/token/genDualToken.freepage?appid="+appid + "&secret="+secret + "&account="+account;
token = org.frameworkset.spi.remote.http.HttpReqeust.httpPostforString(url);

out.println("<div>isGuest:"+AccessControl.getAccessControl().isGuest()+"</div>");
out.println("<div>userAccount:"+AccessControl.getAccessControl().getUserAccount()+"</div>");

out.println("<div>账号token:"+tokenparamname + "=" + token+"</div>");

String accounttokenrequest = tokenparamname + "=" + token + "&appid=" + appid + "&secret="+secret;
//工号token
account = "10006673";//如果使用工号则loginType为2，否则为1
token = tokenService.genAuthTempToken(appid, secret, account);
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
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&loginType=1&loginMenu=appbommanager&subsystem_id=module">创建领料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&loginType=1&loginMenu=appbommanager&subsystem_id=module">创建退料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&loginType=1&loginMenu=appbommanager&subsystem_id=module">工单管理</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&loginType=1&loginMenu=appbommanager&subsystem_id=module">台账查询</a></td></tr>
</table>
<table>
<tr><td>账号登录-直接跳转到指定页面</td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&loginType=1&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">创建领料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&loginType=1&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">创建退料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&loginType=1&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">工单管理</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=accounttokenrequest %>&loginType=1&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">台账查询</a></td></tr>
</table>
<table>
<tr><td>工号登录</td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&loginType=2&loginMenu=appbommanager&subsystem_id=module">创建领料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&loginType=2&loginMenu=appbommanager&subsystem_id=module">创建退料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&loginType=2&loginMenu=appbommanager&subsystem_id=module">工单管理</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&loginType=2&loginMenu=appbommanager&subsystem_id=module">台账查询</a></td></tr>
</table>

<table>
<tr><td>工号登录-直接跳转到指定页面</td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&loginType=2&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">创建领料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&loginType=2&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">创建退料单</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&loginType=2&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">工单管理</a></td></tr>
<tr><td><a target="_blank" href="<%=request.getContextPath() %>/sso/ssowithtoken.page?<%=worknumbertokenrequest %>&loginType=2&successRedirect=<%=URLEncoder.encode("/appbom/aaa.page?a=b&c=d") %>&subsystem_id=module">台账查询</a></td></tr>
</table>
</body>
</html>