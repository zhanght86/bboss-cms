<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
	描述：实时任务管理
	作者：谭湘
	版本：1.0
	日期：2014-05-06
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>实时任务管理</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">

$(document).ready(function() {

	$("#wait").hide();
       		
	queryList();   
       		
    $('#delBatchButton').click(function() {
           delBatch();
    });
           	
});
       
//加载实时任务列表数据  
function queryList(){
	var processIntsId = $("#processIntsId").val();
	var processKey = $("#processKey").val();
	var taskState = $("#taskState").val();
	var taskName = $("#taskName").val();
	var taskId = $("#taskId").val();
	
    $("#ontimeContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryOntimeTaskData.page #customContent", 
    	{processIntsId:processIntsId, processKey:processKey,taskState:taskState,taskId:taskId,taskName:taskName},
    	function(){loadjs();});
}

// 签收任务
function signTask(taskId,SuspensionState) {
	
	if (SuspensionState == '2'){
		alert("当前流程已被挂起,不能签收！");
		return ;
	}
	
	$.ajax({
 	 	type: "POST",
		url : "<%=request.getContextPath()%>/workflow/taskManage/signTask.page",
		data :{"taskId":taskId},
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
		success : function(data){
			if (data != 'success') {
				alert("流程实例签收出错："+data);
			}else {
				queryList();
			}
		}	
	 });
}

// 处理任务
function doTask(taskId,SuspensionState,taskState){
	
	if (SuspensionState == '2'){
		alert("当前流程已被挂起,不能处理！");
		return ;
	}
	
	var processKey = $("#processKey").val();
	
	var url="<%=request.getContextPath()%>/workflow/taskManage/toDealTask.page?processKey="+ processKey;
	
	$.dialog({ title:'任务处理',width:1100,height:620, content:'url:'+url});
	
	<%--
	$.ajax({
 	 	type: "POST",
		url : "<%=request.getContextPath()%>/workflow/taskManage/completeTask.page",
		data :{"taskId":taskId,"taskState":taskState},
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
		success : function(data){
			if (data != 'success') {
				alert(data);
			}else {
				queryList();
			}
		}	
	 });
	 --%>
}

//查看流程实例详情
function viewDetailInfo(processInstId) {
	var url="<%=request.getContextPath()%>/workflow/taskManage/viewTaskDetailInfo.page?processInstId="+processInstId;
	$.dialog({ title:'明细查看',width:1100,height:620, content:'url:'+url});
}
	
function doreset(){
	$("#reset").click();
}

</script>
</head>

<body>
	<div class="mcontent" style="width:98%;margin:0 auto;overflow:auto;">
	
		<sany:menupath />
		
		<div id="rightContentDiv">
			
			<div id="searchblock">
			
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				
				<div class="search_box">
					<form id="queryForm" name="queryForm">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
										<tr>
											<th>任务ID：</th>
											<td><input id="taskId" name="taskId" type="text" class="w120"/></td>
											<th>任务名称：</th>
											<td><input id="taskName" name="taskName" type="text" class="w120"/></td>
											<th>流程实例ID：</th>
											<td><input id="processIntsId" name="processIntsId" type="text" class="w120"/></td>
											<th>签收状态：</th>
											<td>
												<select id="taskState" name="taskState" class="select1" style="width: 125px;">
												    <option value="0" selected>全部</option>
													<option value="1">未签收</option>
													<option value="2">已签收</option>
												</select>
											</td>
											<td style="text-align:right">
												<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
												<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
												<input type="hidden" name="processKey" id="processKey" value="${processKey}"/>
												<input type="reset" id="reset" style="display:none"/>
											</td>
										</tr>
									</table>
								</td>
								<td class="right_box"></td>
							</tr>
						</table>
					</form>
				</div>
				
				<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
			</div>
			
			<div class="title_box">
				<div class="rightbtn">
				<!--
					<a href="#" class="bt_small" id="upBatchButton"><span>开启</span></a>
					<a href="#" class="bt_small" id="delBatchButton"><span><pg:message code="sany.pdp.common.batch.delete"/></span></a>
					<a href="#" class="bt_small" id="upBatchButton"><span>升级</span></a>
				-->
				</div>
					
				<strong>实时任务列表</strong>
				<img id="wait" src="<%=request.getContextPath()%>/common/images/wait.gif" />				
			</div>
			
			<div id="ontimeContainer" style="overflow:auto"></div>
		</div>
	</div>
</body>
</html>
