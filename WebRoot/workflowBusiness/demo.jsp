<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>业务演示</title>
<%@ include file="/common/jsp/common-css-lhgdialog.jsp"%>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/html3/js/tab.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	
	$("#wait").hide();
	
	$("#entrustwait").hide();
	
	queryList();  
	
	queryEntrustList();
       		
    $("#businessType").combotree({
   		url:"<%=request.getContextPath()%>/workflow/businesstype/showComboxBusinessTree.page"
   	});
           	
});

//加载实时任务列表数据  
function queryList(){
	var processIntsId = $("#queryForm #processIntsId").val();
	var processKey = $("#queryForm #processKey").val();
	var taskState = $("#queryForm #taskState").val();
	var taskName = $("#queryForm #taskName").val();
	var taskId = $("#queryForm #taskId").val();
	var businessTypeId = $("#queryForm #businessType").combotree('getValue');
	var businessKey = $("#queryForm #businessKey").val();
	var appName = $("#queryForm #appName").val();
	
    $("#ontimeContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryOntimeTaskData.page #customContent", 
    	{"processIntsId":processIntsId, "processKey":processKey,"taskState":taskState,"taskId":taskId,
    	"taskName":taskName,"businessTypeId":businessTypeId,"businessKey":businessKey,"appName":appName},
    	function(){});
    
}

function modifyQueryData(){
	$("#ontimeContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryOntimeTaskData.page?"+$("#querystring").val()+" #customContent",function(){loadjs()});
	$("#entrustContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryEntrustTaskData.page?"+$("#querystring").val()+" #entrustContent",function(){loadjs()});
}
	
function doreset(){
	$("#reset").click();
}

</script>
</head>

<body>

<div class="content_box" >
	
	<sany:menupath menuid="ontimeTask"/>
		
	<div class="search">
		<table>
			<tr><td>
			    <div class="sany_li">
			      <input name="" type="text"  class="input1" value="单号" onfocus="this.value=''" onblur="if(this.value==''){this.value='单号'}" />
			    </div> 
			    <a href="#" class="bt_search"><span>查询</span></a>
	    	</td></tr>
	    </table>
  	</div>
  
	<div class="title_1">

		<div class="rightbtn">
			<a href="#" class="bt_small" onclick="getEntrustInfo();"><span>申请</span></a>
		</div>
			
		<strong>演示列表</strong>
		<img id="wait" src="<%=request.getContextPath()%>/common/images/wait.gif" />				
	</div>
	
	<div id="demoContainer" style="overflow:auto"></div>
			
			
</div>
	
</body>
</html>
