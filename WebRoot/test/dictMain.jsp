<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="java.util.*" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>

<%@ include file="/common/jsp/importtaglib.jsp"%>


<html>
<head>
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<%@ include file="/common/jsp/css.jsp"%>	
	<tab:tabConfig/>
	<title>权限管理</title>
	<style type="text/css">  
       body{margin:5px;padding:5px;}  
    </style>
</head>
<body>
	
	<tab:tabContainer id="dictManage" selectedTabPaneId="dictTypeDef" skin="sany">
		<tab:tabPane id="dictTypeDef"  tabTitle="字典定义"  lazeload="false">
			<tab:iframe id="dictTypeDef" src="${pageContext.request.contextPath}/perspective_main.frame?menu_path=module%3A%3Amenu%3A%2F%2Fsysmenu%24root%2Fdictionarymanager%24module%2Fdictmanage_info%24item&menu_type=14&external_params=sanymenupath_menuid%3Ddictmanage_info" frameborder="0" scrolling="no"  width="100%" height="90%">
			</tab:iframe>
		</tab:tabPane>
		<tab:tabPane id="dictContent" tabTitle="基础字典" lazeload="false">
			<tab:iframe id="dictContent"  src="${pageContext.request.contextPath}/perspective_main.frame?menu_path=module%3A%3Amenu%3A%2F%2Fsysmenu%24root%2Fdictionarymanager%24module%2Fdictmanage_content_0%24item&menu_type=14&external_params=sanymenupath_menuid%3Ddictmanage_content_0" frameborder="0" scrolling="no" width="99%" height="90%">
			</tab:iframe>
		</tab:tabPane>
		<tab:tabPane id="dictAttachField" tabTitle="高级字典" lazeload="false">
			<tab:iframe id="dictAttachField" src="${pageContext.request.contextPath}/perspective_main.frame?menu_path=module%3A%3Amenu%3A%2F%2Fsysmenu%24root%2Fdictionarymanager%24module%2FdictAttachField%24item&menu_type=14&external_params=sanymenupath_menuid%3DdictAttachField" frameborder="0" scrolling="no" width="99%" height="90%">
			</tab:iframe>
		</tab:tabPane>
		<tab:tabPane id="inputType" tabTitle="输入类型管理" lazeload="false">
			<tab:iframe id="inputType" src="${pageContext.request.contextPath}/perspective_main.frame?menu_path=module%3A%3Amenu%3A%2F%2Fsysmenu%24root%2Fdictionarymanager%24module%2Finputtypemanage%24item&menu_type=14&external_params=sanymenupath_menuid%3Dinputtypemanage" frameborder="0" scrolling="no" width="99%" height="90%">
			</tab:iframe>
		</tab:tabPane>

	</tab:tabContainer>		

</body>
</html>

