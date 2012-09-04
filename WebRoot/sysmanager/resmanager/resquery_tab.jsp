<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request, response); 
%>
<html >    
	<head>
		<title>属性容器</title>
		<tab:tabConfig/>		
	</head>
	<body>
		<sany:menupath menuid="resquery"/>
		
		<tab:tabContainer id="resquery-container" selectedTabPaneId="resQuery"  skin="sany">
			<tab:tabPane id="resQuery"  tabTitleCode="sany.pdp.sysmanager.resource.normal">
				<tab:iframe id="res_queryframe" src="res_queryframe.jsp" frameborder="0" width="100%" height="550"/>
			</tab:tabPane>
			<tab:tabPane id="special-resQuery"  lazeload="true"  tabTitleCode="sany.pdp.sysmanager.resource.special">
				 <tab:iframe id="special_Frame" src="special_Frame.jsp" name="specialRes"  width="100%" height="550" frameborder="0" style="display:inline" />				
			</tab:tabPane>
			
			<tab:tabPane id="special-farresQuery"  lazeload="true"  tabTitleCode="sany.pdp.sysmanager.resource.authorize">
				 <tab:iframe id="res_farqueryframe" src="far-outerquery/res_queryframe.jsp" name="specialRes"  width="100%" height="550" frameborder="0" style="display:inline" />				
			</tab:tabPane>
	
			
		</tab:tabContainer>
	</body>
</html>

