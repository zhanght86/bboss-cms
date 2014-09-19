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
	initliteRequiredStar('1,3,4');
});

function checkPage(){
	checkoutPageElement();
}

function submitFormData(){
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
<body >

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
	    <a href="#" class="bt_submit" onclick="checkPage()"><span>确定</span></a> 
	    <a href="#" class="bt_cancel"><span>取消</span></a> 
    </div>
    
   </div>
</div>

</body>
<script type="text/javascript">tabAction("tab","tabCon01");</script>
</html>
