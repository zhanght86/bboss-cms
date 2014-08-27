<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%
	AccessControl control = AccessControl.getAccessControl();

	request.setAttribute("userId", control.getUserID()); //1
	request.setAttribute("userName", control.getUserName()); //系统管理员
	request.setAttribute("userAccount", control.getUserAccount()); //admin
	System.out.println(control.getUserID()+","+control.getUserName()+","+control.getUserAccount());
%>
<script type="text/javascript" src="${pageContext.request.contextPath}/workflowBusiness/js/dateUtils.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/workflowBusiness/js/stringUtils.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/workflowBusiness/js/workflow.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/workflowBusiness/js/workflowCandidate.js" ></script>

<script type="text/javaScript"> 
	$(document).ready(function(){
		
	});
	
	function setCandidate(node_key){
		setTaskCandidateUsers(node_key);
	}
	
</script>

<ul >

	<input type="hidden" id="pagestate" name="pagestate" value="${pagestate}"/>
	<input type="hidden" id="assignees" name="assignees" value="${assignees}"/>
	<input type="hidden" id="userId" name="userId" value="${userId}"/>
	<input type="hidden" id="userName" name="userName" value="${userName}"/>
	<input type="hidden" id="userAccount" name="userAccount" value="${userAccount}"/>
	<input type="hidden" id="context" value="${pageContext.request.contextPath}"/>
	
	<div id="main1">
		<%@ include file="dealRegion.jsp"%>
	</div>

	<div class="tabbox">
	    <ul class="tab" id="menu3">
			<li><a href="javascript:void(0)" class="current" onclick="setTab(3,0)"><span>审批记录</span></a></li>
			<li><a href="javascript:void(0)" onclick="setTab(3,1)"><span>流程图</span></a></li>
		</ul>
	</div>
	
	<div id="main3">
		<%@ include file="nodeConfigRegion.jsp"%>
	</div>
	
</ul>
