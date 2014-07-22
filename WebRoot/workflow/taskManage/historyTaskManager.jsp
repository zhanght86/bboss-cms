<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
	描述：历史任务管理
	作者：谭湘
	版本：1.0
	日期：2014-06-18
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>历史任务管理</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">

$(document).ready(function() {

	$("#wait").hide();
       		
	queryList();   
       		
    $('#delBatchButton').click(function() {
           delBatch();
    });
    
    $("#businessType").combotree({
   		url:"<%=request.getContextPath()%>/workflow/businesstype/showComboxBusinessTree.page"
   	});
           	
});
       
//加载实时任务列表数据  
function queryList(){
	var processIntsId = $("#processIntsId").val();
	var processKey = $("#processKey").val();
	var taskName = $("#taskName").val();
	var taskId = $("#taskId").val();
	var businessTypeId = $("#businessType").combotree('getValue');
	var businessKey = $("#businessKey").val();
	var createUser = $("#createUser").val();
	var entrustUser = $("#entrustUser").val();
	
    $("#historyContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryHistoryTaskData.page #customContent", 
    	{"processIntsId":processIntsId, "processKey":processKey,"taskId":taskId,"taskName":taskName,"businessTypeId":businessTypeId,
    	"businessKey":businessKey,"createUser":createUser,"entrustUser":entrustUser},
    	function(){loadjs();});
}

function modifyQueryData(){
	$("#historyContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryHistoryTaskData.page?"+$("#querystring").val()+" #customContent",function(){loadjs()});
}

//查看流程实例详情
function viewDetailInfo(processInstId) {
	var url="<%=request.getContextPath()%>/workflow/taskManage/viewTaskDetailInfo.page?processInstId="+processInstId;
	$.dialog({ title:'明细查看',width:1100,height:620, content:'url:'+url,maxState:true});
}
	
function doreset(){
	$("#reset").click();
}

// 撤销任务
function cancelTask(taskId,processId){
	
	var url="<%=request.getContextPath()%>/workflow/taskManage/cancelTask.jsp?processKey=${processKey}"
			+"&processId="+processId+"&taskId="+taskId;
    $.dialog({ id:'iframeNewId', title:'填写撤销原因',width:400,height:200, content:'url:'+url});  
	
    return;
    
	$.dialog.confirm('确定撤销任务吗？', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/taskManage/cancelTask.page",
			data: {"processKey":'${processKey}',"processId":processId,"taskId":taskId},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data != 'success') {
					alert("撤销任务出错:"+data);
				}else {
					modifyQueryData();
					api.close();	
				}
			}
		 });
	},function(){});  
}

</script>
</head>

<body>
	<div class="mcontent" style="width:98%;margin:0 auto;overflow:auto;">
	
	<pg:empty actual="${processKey}">
		<sany:menupath menuid="historyTask"/>
	</pg:empty>
		
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
											<th>业务类型：</th>
											<td>
												<select class="easyui-combotree" id='businessType' name="businessType" required="false"
														style="width: 120px;">
											</td>
											<th>被委托人：</th>
											<td>
												<input id="entrustUser" name="entrustUser" type="text" class="w120"/>
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
											<th>委托人：</th>
											<td>
												<input id="createUser" name="createUser" type="text" class="w120"
												<pg:false actual="${isAdmin}"> value="${currentAccount}" disabled</pg:false>
												/>
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
					
				<strong>历史任务列表</strong>
				<img id="wait" src="<%=request.getContextPath()%>/common/images/wait.gif" />				
			</div>
			
			<div id="historyContainer" style="overflow:auto"></div>
		</div>
	</div>
</body>
</html>
