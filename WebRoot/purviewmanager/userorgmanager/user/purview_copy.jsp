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
	String p1 = "org2user.jsp?userId="+userId+"&orgId="+orgId;
	String p2 = "otherOrg2user.jsp?userId="+userId+"&orgId="+orgId;
%>
<html>
<head>
	<title>用户【<%=userName%>】权限复制</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
	<tab:tabConfig/>
</head>
<body>
	 
			
			<tab:tabContainer id="user-manage-update" selectedTabPaneId="t1" skin="sany">
					<tab:tabPane id="t1"  tabTitleCode="sany.pdp.copy.authority.to.this"  lazeload="true">
						<tab:iframe id="i1" src="<%=p1 %>" frameborder="0" scrolling="no" width="100%" height="100%">
						</tab:iframe>
					</tab:tabPane>
					<tab:tabPane id="t2"  tabTitleCode="sany.pdp.copy.authority.to.other.user"   lazeload="true">
						<tab:iframe id="i2" src="<%=p2 %>" frameborder="0" scrolling="no" width="100%" height="100%">
						</tab:iframe>
					</tab:tabPane>
			</tab:tabContainer>
			 
		 
</body>
 
</html>

