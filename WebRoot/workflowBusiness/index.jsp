<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>业务系统演示首页</title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<script type="text/javascript">

$(document).ready(function() {
	
});

function check1() {
	$("#content").load("<%=request.getContextPath()%>/workflowBusiness/business/toworkflowMain.page");
	$("#btnDiv").show();
}

function check2() {
	$("#content").load("<%=request.getContextPath()%>/workflowBusiness/business/toDealTask.page");
	$("#btnDiv").show();
}

function submitFormData(){
	//<pg:equal actual="${pagestate}" value="1">
	//$("form").attr("action","<%=request.getContextPath()%>/workflowBusiness/business/startProc.page");
	//</pg:equal>
	//<pg:in actual="${pagestate}" scope="2,3,4,5">
	 //  $("form").attr("action","${pageContext.request.contextPath}/conference/order/approveWorkFlow.page");
	// </pg:in>
	$("#meetingOrderForm").form('submit', {
	 onSubmit:function(){
		 blockUI();
		 if(!$("#meetingOrderForm").attr("subimt")){//防止重复提交
			$("#meetingOrderForm").attr("subimt","true");
		 }else{
			return false;
		  }		
	 },
	 success:function(result){
		 
		 alert(result);
	}
});	
}

</script>
</head>

<body class="easyui-layout">
	
	<div region="west" split="true" title="演示项目" style="width: 220px; padding1: 1px; overflow: hidden;">
		<div class="easyui-accordion" fit="true" border="false" align="center">
		
			<a href="javaScript:check1();">申请流程页面显示</a>
			<br/>
			<a href="javaScript:check2();">处理任务</a>
			<br/>
			
			
		</div>
	</div>
	
	<div region="center" style="overflow: hidden;" >
		<form method="post" id="meetingOrderForm" 
		action="${pageContext.request.contextPath}/workflowBusiness/business/startProc.page">
			<div id="content"></div>
			
			<div id="btnDiv" class="btnarea" style="display:none;">
				<a href="javascript:void(0)" class="bt_2" id="btn" onclick="submitFormData()"><span>提交</span></a>
			</div>	
		</form>
	</div>
		
</body>
</html>
