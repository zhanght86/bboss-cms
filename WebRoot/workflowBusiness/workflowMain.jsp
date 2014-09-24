<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/common-css-lhgdialog.jsp"%>
<link href="${pageContext.request.contextPath}/html3/mam/stylesheet/entry.css" rel="stylesheet" type="text/css" />
 
<script type="text/javaScript"> 
	<%-- js可能需要的变量 --%>
	var ctx			=	"${pageContext.request.contextPath}";						<%-- 上下文路径 --%>
	var pagestate	= 	"${pagestate}";												<%-- 页面状态 --%>
	var taskKey		= 	"${task.taskDefKey}";										<%-- 当前任务key --%>
	var userName	= 	"<sany:accesscontrol userattribute='userName'/>";			<%-- 处理人名字 --%>
	var userAccount	= 	"<sany:accesscontrol userattribute='userAccount'/>";		<%-- 处理人域账号 --%>
	var nodeStarNum	=   "";															<%-- 非空节点序号 --%>
</script>

<script type="text/javascript" src="${pageContext.request.contextPath}/workflowBusiness/js/workflow_base.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/workflowBusiness/js/workflow.js" ></script>


<div class="main_contain">
	<%--  后台保存需要的参数 --%>
	<input type="hidden" id="processKey" name="processKey" value="${processKey}"/>
	<input type="hidden" id="userName" name="userName" value="<sany:accesscontrol userattribute="userName"/>"/>
	<input type="hidden" id="userAccount" name="userAccount" value="<sany:accesscontrol userattribute="userAccount"/>"/>

    <%@ include file="dealRegion.jsp"%>
    
    <%@ include file="processPic.jsp"%>
	
	<%@ include file="nodeConfigRegion.jsp"%>
	
	<%@ include file="hiTaskList.jsp"%>
	
</div>

	
