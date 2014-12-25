<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>业务系统演示首页</title>
<%@ include file="/common/jsp/common-css-lhgdialog.jsp"%>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/html3/js/tab.js"></script>
<link href="${pageContext.request.contextPath}/html3/mam/stylesheet/entry.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
var api = frameElement.api, W = api.opener;
$(document).ready(function() {
	initliteRequiredStar('1,4');
	showLogStyle('1');
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
			if (result=="success") {
				$.dialog.alert("保存成功",function(){
					api.close();
		 			W.modifyQueryData();
				});
			}
			
		 }
	});	
}

// 暂存审批表单临时数据
function submitTempFormData () {
	
	
	$("#workflowForm").attr("action","${pageContext.request.contextPath}/workflow/businessDemo/tempSaveFormDatas.page?businessKey=${businessKey}");

	submitForm0();
	
}

function submitFormData(){
	
	var pagestate	= 	"${pagestate}";
	
	// 新增页面,暂存页面(开启流程实例)
	if (pagestate == '1' || pagestate == '2'){
		
		if (checkoutPageElement()){
			
			$("#workflowForm").attr("action","${pageContext.request.contextPath}/workflow/businessDemo/startProc.page?processKey=${processKey}&businessKey=${businessKey}");

			submitForm0();
			
		}
		
	}
	
	// 审批人或提交人处理任务
	if (pagestate == '3' || pagestate == '4' || pagestate == '7'){
		
		if (checkoutPageElement()){
			
			$("#workflowForm").attr("action","${pageContext.request.contextPath}/workflow/businessDemo/approveWorkFlow.page?processKey=${processKey}");

			submitForm0();
			
		}
		
	}
	
	// 第三方查看或流程结束查看
	if ( pagestate == '5' || pagestate == '6' ){
		
		api.close();
		W.modifyQueryData();
		
	}
	
}

// 切换显示审批记录的样式
function showLogStyle(style){
	var filterLog = false;
	var styleHtml ='';
	
	// 合并并行节点产生的撤销、驳回、废弃日志记录
	if (style == '1') {
		filterLog = true;
	}else if (style == '0') {// 不合并
		filterLog = false;
	}
	
	var url = "<%=request.getContextPath()%>/workflow/businessDemo/getHisTaskInfo.page?"
		+"businessKey=${businessKey}&filterLog="+filterLog;
		
	$("#logInfo").load(url,function(){loadjs()});
	
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
	    	<div class="tickets">业务页面</div>
	    </ul>
	    
	   	<div class="bottom_area"></div>
	    <div class="submit_operation"> 
		    <a href="#" class="bt_submit" onclick="submitFormData()"><span>确定</span></a> 
		    <pg:in actual="${pagestate}" scope="1,2">
		    <a href="#" class="bt_submit" onclick="submitTempFormData()"><span>暂存</span></a> 
		    </pg:in>
		    <a href="#" class="bt_cancel" onclick="closeDlg()"><span>取消</span></a> 
	    </div>
	    
	   </div>
	</div>
</form>

</body>
<script type="text/javascript">tabAction("tab","tabCon01");</script>
</html>
