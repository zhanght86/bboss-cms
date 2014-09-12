<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/workflowBusiness/css/css.jsp"%>
<%
	AccessControl control = AccessControl.getAccessControl();

	request.setAttribute("userId", control.getUserID()); //1
	request.setAttribute("userName", control.getUserName()); //系统管理员
	request.setAttribute("userAccount", control.getUserAccount()); //admin
%>
<script type="text/javascript" src="${pageContext.request.contextPath}/workflowBusiness/js/dateUtils.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/workflowBusiness/js/stringUtils.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/workflowBusiness/js/workflowCandidate.js" ></script>
<script type="text/javaScript"> 
	$(document).ready(function(){
		$("form").data("user", { userId: "${userId}", userName: "${userName}" });
		$("form").data("coordinateObj", { id: "${task.taskDefKey}" });
		$("form").data("pagestate", "${pagestate}");  
		$("form").data("assignees", "${assignees}");
		//如果是初始提交页面，加载相关数据
		<pg:in actual="${pagestate}" scope="1,2">workflowaddInit();</pg:in>
	/* 	<pg:equal actual="${pagestate}" value="3">showRecall();showTemp();</pg:equal> */
		
	});
	var ctx="${pageContext.request.contextPath}";
	function setCandidate(node_key){
		setTaskCandidateUsers(node_key);
	}
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/workflowBusiness/js/workflow.js" ></script>
<input type="hidden" id="pagestate" name="pagestate" value="${pagestate}"/>
<input type="hidden" id="assignees" name="assignees" value="${assignees}"/>
<input type="hidden" id="userId" name="userId" value="${userId}"/>
<input type="hidden" id="userName" name="userName" value="${userName}"/>
<input type="hidden" id="userAccount" name="userAccount" value="${userAccount}"/>

<ul style="display: none">  
  
    <%@ include file="dealRegion.jsp"%>

	
	<%@ include file="nodeConfigRegion.jsp"%>
	
</ul>

<pg:list requestKey="taskHistorList">
	<script type="text/javaScript">
		selectNode('<pg:cell colName="TASK_DEF_KEY_" />','已执行');
	</script>
</pg:list>
	
