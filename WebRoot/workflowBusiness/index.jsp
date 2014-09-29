<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>业务系统演示首页</title>
<%@ include file="/common/jsp/common-css-lhgdialog.jsp"%>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/html3/js/tab.js"></script>

<script type="text/javascript">

$(document).ready(function() {
	initliteRequiredStar('1,4');
});

function submitForm0(){
	
	$("#workflowForm").form('submit', {
		 onSubmit:function(){
			 blockUI();
			 if(!$("#workflowForm").attr("subimt")){//防止重复提交
				$("#workflowForm").attr("subimt","true");
			 }else{
				return false;
			 }		
		 },
		 success:function(result){
			unblockUI();
			alert(result);
		 }
	});	
}

// 暂存审批表单临时数据
function submitTempFormData () {
	
	$("#workflowForm").attr("action","${pageContext.request.contextPath}/workflowBusiness/business/tempSaveFormDatas.page");

	submitForm0();
}

function submitFormData(){
	
	var pagestate	= 	"${pagestate}";
	
	// 新增页面,暂存页面(开启流程实例)
	if (pagestate == '1' || pagestate == '2'){
		
		if (checkoutPageElement()){
			
			$("#workflowForm").attr("action","${pageContext.request.contextPath}/workflowBusiness/business/startProc.page");

			submitForm0();
			
		}
		
	}
	
	// 审批人或提交人处理任务
	if (pagestate == '3' || pagestate == '4'){
		
		if (checkoutPageElement()){
			
			$("#workflowForm").attr("action","${pageContext.request.contextPath}/workflowBusiness/business/approveWorkFlow.page");

			submitForm0();
			
		}
		
	}
	
}

</script>
</head>
<body >

<form action="" id="workflowForm" method="post">
	<div class="main_contain">
	  <div class="u_tab_div2" id="tab">
	    <div class="tab1"><span class="u_tab_hover"><a href="#">流程</a></span></div>
	    <div class="tab2"><span class="u_tab"><a href="#">内容</a></span></div>
	  </div>
	  
	  <div id="tabCon01">
	    <ul class="ul_news">
	    	<%@ include file="workflowMain.jsp"%>
	    	
	    </ul>
	    <ul class="ul_news" style="display:none;">
	    	<div class="tickets"></div>
	    </ul>
	    
	   	<div class="bottom_area"></div>
	    <div class="submit_operation"> 
		    <a href="#" class="bt_submit" onclick="submitFormData()"><span>确定</span></a> 
		    <pg:equal actual="${pagestate}" value="1">
		    <a href="#" class="bt_submit" onclick="submitTempFormData()"><span>暂存</span></a> 
		    </pg:equal>
		    <a href="#" class="bt_cancel"><span>取消</span></a> 
	    </div>
	    
	   </div>
	</div>
</form>

</body>
<script type="text/javascript">tabAction("tab","tabCon01");</script>
</html>
