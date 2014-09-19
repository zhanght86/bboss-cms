<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/common-css-lhgdialog.jsp"%>
<link href="${pageContext.request.contextPath}/html3/mam/stylesheet/entry.css" rel="stylesheet" type="text/css" />

<%
	AccessControl control = AccessControl.getAccessControl();

	request.setAttribute("userId", control.getUserID()); //1
	request.setAttribute("userName", control.getUserName()); //系统管理员
	request.setAttribute("userAccount", control.getUserAccount()); //admin
%>
<script type="text/javascript" src="${pageContext.request.contextPath}/workflowBusiness/js/workflow_base.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/workflowBusiness/js/workflow.js" ></script>

<script type="text/javaScript"> 
	<%-- js可能需要的变量 --%>
	var ctx			=	"${pageContext.request.contextPath}";	<%-- 上下文路径 --%>
	var pagestate	= 	"${pagestate}";							<%-- 页面状态 --%>
	var taskKey		= 	"${task.taskDefKey}";					<%-- 当前任务key --%>
	var userId		= 	"${userId}";							<%-- 处理人工号 --%>
	var userName	= 	"${userName}";							<%-- 处理人名字 --%>
	var userAccount	= 	"${userAccount}";						<%-- 处理人域账号 --%>
	var nodeStarNum	=   "";										<%-- 非空节点序号 --%>
	
</script>

<div class="main_contain">
	<%--  后台保存需要的参数 --%>
	<input type="hidden" id="processKey" name="processKey" value="${processKey}"/>
	<input type="hidden" id="userName" name="userName" value="${userName}"/>
	<input type="hidden" id="userAccount" name="userAccount" value="${userAccount}"/>

    <%@ include file="dealRegion.jsp"%>
    
    <%@ include file="processPic.jsp"%>
	
	<%@ include file="nodeConfigRegion.jsp"%>
	
	<%@ include file="hiTaskList.jsp"%>
	
</div>

	
