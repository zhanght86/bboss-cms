<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
	描述：委托任务管理
	作者：谭湘
	版本：1.0
	日期：2014-07-11
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>委托任务管理</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">

$(document).ready(function() {

	$("#wait").hide();
       		
	queryEntrustList();   
       		
    $("#entrustForm #businessType").combotree({
   		url:"<%=request.getContextPath()%>/workflow/businesstype/showComboxBusinessTree.page"
   	});
           	
});
       
//加载实时任务列表数据  
function queryEntrustList(){
	var processIntsId = $("#entrustForm #processIntsId").val();
	var processKey = $("#entrustForm #processKey").val();
	var taskState = $("#entrustForm #taskState").val();
	var taskName = $("#entrustForm #taskName").val();
	var taskId = $("#entrustForm #taskId").val();
	var businessTypeId = $("#entrustForm #businessType").combotree('getValue');
	var businessKey = $("#entrustForm #businessKey").val();
	
    $("#entrustContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryEntrustTaskData.page #entrustContent", 
        	{"processIntsId":processIntsId, "processKey":processKey,"taskState":taskState,"taskId":taskId,
        	"taskName":taskName,"businessTypeId":businessTypeId,"businessKey":businessKey},
        	function(){loadjs();});
}

function modifyQueryEntrustData(){
	$("#entrustContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryEntrustTaskData.page?"+$("#querystring").val()+" #entrustContent",function(){loadjs()});
}

// 签收任务
function signTask(taskId,SuspensionState) {
	
	if (SuspensionState == '2'){
		$.dialog.alert("当前流程已被挂起,不能签收！",function(){});
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
				$.dialog.alert("流程实例签收出错："+data,function(){});
			}else {
				modifyQueryEntrustData();
			}
		}
	 });
}

// 处理任务
function doTask(taskId,SuspensionState,processInstId,taskState){
	
	if (SuspensionState == '2'){
		$.dialog.alert("当前流程已被挂起,不能处理！"+data,function(){});
		return ;
	}
	
	var processKey = $("#processKey").val();
	
	var url="<%=request.getContextPath()%>/workflow/taskManage/toDealTask.page?processKey="+ processKey
			+"&processInstId="+processInstId+"&taskId="+taskId+"&taskState="+taskState;
	
	$.dialog({ title:'任务处理',width:1100,height:620, content:'url:'+url});
	
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
	
	<!-- 
	<pg:empty actual="${processKey}">
		<sany:menupath menuid="entrustTask"/>
	</pg:empty>
	 -->
	 	
		<div id="rightContentDiv">
			
			<div id="searchblock">
			
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				
				<div class="search_box">
					<form id="entrustForm" name="entrustForm">
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
											<th>签收状态：</th>
											<td>
												<select id="taskState" name="taskState" class="select1" style="width: 125px;">
												    <option value="0" selected>全部</option>
													<option value="1">未签收</option>
													<option value="2">已签收</option>
												</select>
											</td>
											<th>业务类型：</th>
											<td>
												<select class="easyui-combotree" id='businessType' name="businessType" required="false"
														style="width: 120px;">
											</td>
											<td style="text-align:center" rowspan="2" >
												<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
												<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
												<input type="reset" id="reset" style="display:none"/>
											</td>
										</tr>
										<tr>
											<th>流程实例ID：</th>
											<td><input id="processIntsId" name="processIntsId" type="text" class="w120"/></td>
											<th>业务主题：</th>
											<td><input id="businessKey" name="businessKey" type="text" class="w120"/></td>
											<th>流程key：</th>
											<td><input id="processKey" name="processKey" type="text" class="w120" value="${processKey}"
												<pg:notempty actual="${processKey}" > disabled</pg:notempty>/>
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
				<div class="rightbtn"></div>
					
				<strong>委托任务列表</strong>
			</div>
			
			<div id="entrustContainer" style="overflow:auto"></div>
		</div>
	</div>
</body>
</html>
