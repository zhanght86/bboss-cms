<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User" %>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
	
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	
	String userId = request.getParameter("userId");
	String orgId = request.getParameter("orgId");
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(userId);	
	String userName = user.getUserRealname();
%>
<html>
<head>
	<title>用户【<%=userName%>】权限复制</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
	<tab:tabConfig/>
</head>
<body>
	<div style="height: 10px">&nbsp;</div>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
		<tr>
			<td colspan="2">
				<div class="tabbox" id="tabbox">
	<ul class="tab" id="menu0">
		<li><a href="javascript:void(0)" class="current"
			onclick="setTab(0,0)"><span><pg:message code="sany.pdp.copy.authority.to.other.user"/></span></a></li>
		<li><a href="javascript:void(0)" onclick="setTab(0,1)"><span><pg:message code="sany.pdp.copy.authority.to.this"/></span></a></li>
	</ul>
</div>

<div id="main0">
	
	
	<ul id="tab1" style="display: block;">
						<iframe id="copyRest" src="" frameborder="0" scrolling="yes" width="99%" height="540">
						</iframe>
						</ul>
						<ul id="tab2">
						<iframe id="copySelf" src="" frameborder="0" scrolling="yes" width="99%" height="540">
						</iframe>
						</ul>
						</div>
			</td>
		</tr>
  </table>	

<iframe name="exeman" width="0" height="0" style="display:none"></iframe>
</body>
<script>
    window.onload=function init(){
        var path = "org2user.jsp?userId=<%=userId%>&orgId=<%=orgId%>";
        document.all("copyRest").src = path;
        var path = "otherOrg2user.jsp?userId=<%=userId%>&orgId=<%=orgId%>";
        document.all("copySelf").src = path;
    }
</script>
</html>

