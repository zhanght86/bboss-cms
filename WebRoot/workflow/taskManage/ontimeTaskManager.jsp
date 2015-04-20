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
<script type='text/javascript' src="${pageContext.request.contextPath}/include/autocomplete/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/include/autocomplete/jquery.autocomplete.css" />

<script type="text/javascript">

$(document).ready(function() {
	
	$("#wait").hide();
	
	$("#entrustwait").hide();
	
	$("#copywait").hide();
	
	queryList();  
	
	queryEntrustList();
	
	queryCopyList();
       		
    $("#queryForm #businessType").combotree({
   		url:"<%=request.getContextPath()%>/workflow/businesstype/showComboxBusinessTree.page"
   	});
    
    $("#entrustForm #businessType").combotree({
   		url:"<%=request.getContextPath()%>/workflow/businesstype/showComboxBusinessTree.page"
   	});
    
    <%-- 自动匹配用户名称--%>
	 var url="<%=request.getContextPath()%>/workflow/taskManage/getUserPageList.page";
		$("#queryForm #assignee").autocomplete(url , {
	         minChars:1,//自动完成激活之前填入的最小字符 
	         width:120,
	         max:20,// 最多显示多少条记录
	         dataType:"json",
	         // 扩展字段
	         extraParams: {    
	        	 assigneeName: function(){return $("#queryForm #assignee").val()}
	         },
	         scroll : true,//是否显示滚动条
	         matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示 
	         cacheLength : 30, //缓存结果队列长度 
	         valuefiled:"user_realname",// 默认显示实体哪个属性
	         //下拉列表格式   
	         formatItem: function(row, i, max) {
	         	return "<I>"+row.user_realname+"("+row.user_name+")</I>";
	         },
	         //与这些相匹配
	         formatMatch: function(row, i, max) {
	              return row;
	         }
	     }).result(function(event,row,formatted){//通过result函数可进对数据进行其他操作
	     		$("#queryForm #assignee").val(row.user_name);
	     });
           	
});

// 转派任务
function delegateTasks () {
	var url="<%=request.getContextPath()%>/workflow/taskManage/delegateTasksLog.jsp?processKey=${processKey}";
	$.dialog({ title:'转派任务',width:1100,height:600, content:'url:'+url,maxState:true});
}

//查看委托关系
function getEntrustInfo(){
	var url="<%=request.getContextPath()%>/workflow/taskManage/viewEntrustInfo.page";
	$.dialog({ title:'查看[${currentAccountName}]授权信息',width:1100,height:620, content:'url:'+url});
}
       
//加载实时任务列表数据  
function queryList(){
	var processIntsId = $("#queryForm #processIntsId").val();
	var processKey = $("#queryForm #processKey").val();
	var taskState = $("#queryForm #taskState").val();
	var taskName = $("#queryForm #taskName").val();
	var taskId = $("#queryForm #taskId").val();
	var assignee = $("#queryForm #assignee").val();
	var businessTypeId = $("#queryForm #businessType").combotree('getValue');
	var businessKey = $("#queryForm #businessKey").val();
	var appName = $("#queryForm #appName").val();
	
    $("#ontimeContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryOntimeTaskData.page #customContent", 
    	{"processIntsId":processIntsId, "processKey":processKey,"taskState":taskState,"taskId":taskId,
    	"taskName":taskName,"businessTypeId":businessTypeId,"businessKey":businessKey,"appName":appName,
    	"assignee":assignee},
    	function(){
    		//if($("#isEmptyData1").val()){
			//	$("#ontimeDiv").show();
			//}
    	});
    
}

function modifyQueryData(){
	$("#ontimeContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryOntimeTaskData.page?"+$("#querystring").val()+" #customContent",function(){loadjs()});
	$("#entrustContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryEntrustTaskData.page?"+$("#querystring").val()+" #entrustContent",function(){loadjs()});
}

// 签收任务
function signTask(taskId,SuspensionState,processKey) {
	
	if (SuspensionState == '2'){
		$.dialog.alert("当前流程已被挂起,不能签收！"+data,function(){});
		return ;
	}
	
	$.ajax({
 	 	type: "POST",
		url : "<%=request.getContextPath()%>/workflow/taskManage/signTask.page",
		data :{"taskId":taskId,"processKey":processKey},
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
		success : function(data){
			if (data != 'success') {
				$.dialog.alert("流程实例签收出错："+data,function(){});
			}else {
				modifyQueryData();
			}
		}
	 });
}

// 处理任务
function doTask(taskId,SuspensionState,processInstId,taskState,processKey){
	
	if (SuspensionState == '2'){
		$.dialog.alert("当前流程已被挂起,不能处理！",function(){});
		return ;
	}
	
	var url="<%=request.getContextPath()%>/workflow/taskManage/toDealTask.page?processKey="+ processKey
			+"&processInstId="+processInstId+"&taskId="+taskId+"&taskState="+taskState;
	
	$.dialog({ title:'任务处理',width:1100,height:700, content:'url:'+url});
	
}

//查看流程实例详情
function viewDetailInfo(processInstId) {
	var url="<%=request.getContextPath()%>/workflow/taskManage/viewTaskDetailInfo.page?processInstId="+processInstId;
	$.dialog({ title:'明细查看',width:1100,height:700, content:'url:'+url,maxState:true});
}
	
function doreset(){
	$("#reset").click();
}

//加载委托任务列表数据  
function queryEntrustList(){
	var processIntsId = $("#entrustForm #processIntsId").val();
	var processKey = $("#entrustForm #processKey").val();
	var taskState = $("#entrustForm #taskState").val();
	var taskName = $("#entrustForm #taskName").val();
	var taskId = $("#entrustForm #taskId").val();
	var businessTypeId = $("#entrustForm #businessType").combotree('getValue');
	var businessKey = $("#entrustForm #businessKey").val();
	var assignee = $("#entrustForm #assignee").val();
	var appName = $("#entrustForm #appName").val();
	
    $("#entrustContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryEntrustTaskData.page #entrustContent", 
        	{"processIntsId":processIntsId, "processKey":processKey,"taskState":taskState,"taskId":taskId,"appName":appName,
        	"taskName":taskName,"businessTypeId":businessTypeId,"businessKey":businessKey,"assignee":assignee},
        	function(){
        			//if($("#isEmptyData").val()){
					//	$("#entrustDiv").show();//有委托任务就显示委托任务DIV
        			//}
        	});
}

//签收委托任务
function signEntrustTask(taskId,SuspensionState) {
	
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
				modifyQueryData();
			}
		}
	 });
}

//处理委托任务
function doEntrustTask(taskId,SuspensionState,processInstId,taskState,processKey,createUser,entrustUser){
	
	if (SuspensionState == '2'){
		$.dialog.alert("当前流程已被挂起,不能处理！",function(){});
		return ;
	}
	
	var url="<%=request.getContextPath()%>/workflow/taskManage/toDealTask.page?processKey="+ processKey
			+"&processInstId="+processInstId+"&taskId="+taskId+"&taskState="+taskState
			+"&createUser="+createUser+"&entrustUser="+entrustUser;
	
	$.dialog({ title:'任务处理',width:1100,height:700, content:'url:'+url});
	
}

//查看流程实例详情
function viewEntrustDetailInfo(processInstId) {
	var url="<%=request.getContextPath()%>/workflow/taskManage/viewTaskDetailInfo.page?processInstId="+processInstId;
	$.dialog({ title:'明细查看',width:1100,height:620, content:'url:'+url});
}

function doEntrustReset(){
	$("#entrustReset").click();
}

// 手动发送消息
function sendMess(taskId,processKey,taskState,sentType){

	var title='';
	
	if (sentType == '1') {
		title = '确定发送预警提醒信息吗？';
	}else {
		title = '确定发送超时提醒信息吗？';
	}
	
	$.dialog.confirm(title, function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/taskManage/sendMess.page",
			data: {"taskId":taskId,"processKey":processKey,"taskState":taskState,"sentType":sentType},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data != 'success') {
					$.dialog.alert("手动发送信息出错:"+data,function(){});
				}else {
					modifyQueryData();
				}
			}
		 });
	},function(){});  
}

//加载抄送任务列表数据  
function queryCopyList(){
    $("#copyContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryCopyTaskData.page #copyContent", 
       {"process_key":"${processKey}"},function(){});
}

function modifyCopyTaskData(){
	$("#copyContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryCopyTaskData.page?"+$("#querystring").val()+" #copyContent",function(){loadjs()});
}

// 完成抄送任务
function viewCopyTask(processInstId,id){
	
	var url="<%=request.getContextPath()%>/workflow/taskManage/viewCopyTask.page?processInstId="+processInstId+"&copyId="+id;
	$.dialog({ close:modifyCopyTaskData,title:'明细查看',width:1100,height:700, content:'url:'+url,maxState:true});
	
}

</script>
</head>

<body>
	<div class="mcontent" style="width:98%;margin:0 auto;overflow:auto;">
	
	<pg:empty actual="${processKey}">
		<sany:menupath menuid="ontimeTask"/>
	</pg:empty>
		
		<div id="rightContentDiv">
			
			<%--实时任务div --%>
			<div id="ontimeDiv" >
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
												
												<th>处理人：</th>
												<td>
													<pg:true actual="${isAdmin}" evalbody="true">
														<pg:yes>
															<input id="assignee" name="assignee" type="text" class="w120" />
														</pg:yes>
														<pg:no>
															<input id="assignee" name="assignee" type="text"
																class="w120" value="${currentAccount}" disabled/>
														</pg:no>
													</pg:true>
												</td>
											</tr>
											
											<pg:empty actual="${processKey}" >
											<tr>
												<th>应用：</th>
												<td><input id="appName" name="appName" type="text" class="w120"/></td>
											</tr>
											</pg:empty>
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
						<pg:true actual="${isAdmin}">
							<a href="javascript:void(0)" class="bt_small" onclick="delegateTasks();"><span>转派任务</span></a>
						</pg:true>
						<a href="javascript:void(0)" class="bt_small" onclick="getEntrustInfo();"><span>流程授权查看</span></a>
					</div>
						
					<strong>实时任务列表</strong>
					<img id="wait" src="<%=request.getContextPath()%>/common/images/wait.gif" />				
				</div>
				
				<div id="ontimeContainer" style="overflow:auto"></div>
			
			</div>
			
			<br/>
			
			<%--委托任务div --%>
			<div id="entrustDiv" >
				<div id="searchblock" >
					
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
												<td style="text-align:center" rowspan="3" >
													<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryEntrustList()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
													<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doEntrustReset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
													<input type="reset" id="entrustReset" style="display:none"/>
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
												<td><input id="assignee" name="assignee" type="text" class="w120"/></td>
											</tr>
											<pg:empty actual="${processKey}" >
											<tr>
												<th>应用：</th>
												<td><input id="appName" name="appName" type="text" class="w120"/></td>
											</tr>
											</pg:empty>
										</table>
									</td>
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
					<img id="entrustwait" src="<%=request.getContextPath()%>/common/images/wait.gif" />				
				</div>
					
				<div id="entrustContainer" style="overflow:auto"></div>
			
			</div>
			
			<%--抄送任务div --%>
			<div id="copyDiv" >
			<!-- 	<div id="searchblock" >
					
					<div class="search_top">
						<div class="right_top"></div>
						<div class="left_top"></div>
					</div>
					
					<div class="search_box">
						<form id="copyForm" name="copyForm">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
							</table>
						</form>
					</div>
					
					<div class="search_bottom">
						<div class="right_bottom"></div>
						<div class="left_bottom"></div>
					</div>
				</div> -->
				
				<div class="title_box">
					<div class="rightbtn"></div>
						
					<strong>抄送任务列表</strong>
					<img id="copywait" src="<%=request.getContextPath()%>/common/images/wait.gif" />				
				</div>
				
				<div id="copyContainer" style="overflow:auto"></div>
			
			</div>
			
		</div>
			
	</div>
	
</body>
</html>
