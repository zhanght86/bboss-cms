<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
描述：处理任务
作者：谭湘
版本：1.0
日期：2014-05-22
 --%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>处理任务</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
	
<body class="">

	<form name="submitForm" id="submitForm" method="post">
		<input type="hidden" id="sysid" name="sysid" value="${sysid}" />
		<input type="hidden" id="taskId" name="taskId" value="${task.ID_}" />
		<input type="hidden" id="processIntsId" name="processIntsId" value="${task.PROC_INST_ID_}" />
		<input type="hidden" id="processKey" name="processKey" value="${processKey}" />
		<input type="hidden" id="taskState" name="taskState" value="${taskState}" />
		<input type="hidden" id="createUser" name="createUser" value="${createUser}" />
		<input type="hidden" id="entrustUser" name="entrustUser" value="${entrustUser}" />
		<input type="hidden" id="currentUser" name="currentUser" value="${currentUser}" />
	
		<fieldset >
			<legend><strong>基本信息</strong></legend>
			<pg:beaninfo requestKey="task">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table4" >
					<tr>
						<th width="100"><strong>业务主题:</strong></th>
						<td width="150"><pg:cell colName="BUSINESS_KEY_" /></td>
						<th width="100"><strong>任务名称:</strong></th>
						<td width="150"><pg:cell colName="NAME_" /></td>
						<th width="100"><strong>任务ID:</strong></th>
						<td width="300"><pg:cell colName="ID_" /></td>
						<th width="100"><strong>签收人:</strong></th>
						<td width="150"><pg:cell colName="ASSIGNEE_NAME" /></td>
					</tr>
					<tr>
						<th width="100"><strong>到达时间:</strong></th>
						<td width="150"><pg:cell colName="START_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
						<th width="100"><strong>流程状态:</strong></th>
						<td width="150">
							<pg:equal colName="SUSPENSION_STATE_" value="1">进行中</pg:equal>
							<pg:equal colName="SUSPENSION_STATE_" value="2">挂起</pg:equal>
						</td>
						<th width="100"><strong>流程实例ID:</strong></th>
						<td width="300"><pg:cell colName="PROC_INST_ID_" /></td>
					</tr>
					<tr>
						<th width="100"><strong>处理人:</strong></th>
						<td colspan="7"><pg:cell colName="USER_ID_NAME" /></td>
					</tr>
				</table>
			</pg:beaninfo>
		</fieldset>
		
		<fieldset >
			<legend><strong></strong></legend>
			
			<div class="">
				<div class="tabbox">
					<ul class="tab" id="menu1">
						<li><a href="javascript:void(0)" class="current" onclick="setTab(1,0)"><span>执行图</span></a></li>
						<li><a href="javascript:void(0)" onclick="setTab(1,1)"><span>参数配置</span></a></li>
						<li><a href="javascript:void(0)" onclick="setTab(1,2)"><span>处理记录</span></a></li>
					</ul>
				</div>
				
				<div id="main1" >
					<ul id="tab1" style="display:block;">
						<div title="执行图" style="padding:10px;overflow-y:auto; overflow-x:auto;"> 
							<img id="pic" src="${pageContext.request.contextPath}/workflow/repository/getProccessActivePic.page?processInstId=${task.PROC_INST_ID_}" />
						</div>
					</ul>
					
					<ul id="tab2" style="display: none;">
						<fieldset>
							<legend><strong>节点处理人配置</strong></legend>
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
									<pg:header>
										<th>节点KEY</th>
										<th>节点名称</th>
										<th>待办人</th>
										<th>待办组</th>
										<th>节点类型</th>
										<th>处理工时</th>
									</pg:header>
										
									<pg:list autosort="false" requestKey="nodeList">
									<pg:notin colName="node_type" scope="startEvent,endEvent,serviceTask">
										<tr>
											<input type="hidden" id="<pg:cell colName='node_key'/>" 
												name="node_key" value="<pg:cell colName='node_key'></pg:cell>" />
											<input type="hidden" id="<pg:cell colName='node_key'/>_users_id" 
												name="node_users_id" value="<pg:cell colName='node_users_id'></pg:cell>" />
											<td><pg:cell colName="node_key"></pg:cell></td>
											<td><pg:cell colName="node_name"></pg:cell></td>
											<td>
												<input type="text" class="input1 w200" 
													id="<pg:cell colName='node_key'/>_users_name" 
													name="node_users_name" value="<pg:cell colName='node_users_name'/>" />
												<a href="javascript:openChooseUsers('<pg:cell colName="node_key"/>')">选择</a>
											</td>
											<td>
												<input type="text" class="input1 w200"
													id="" name="" value=""/>
												<a href="javascript:openChooseGroups('<pg:cell colName="node_key"/>')">选择</a>
											</td>
											<td >
												<pg:equal colName="isMulti" value="0">
													<span>
														<input type="hidden" name="isMulti" value="0" />单实例
														<pg:equal colName="node_type" value="userTask">
															人工任务
														</pg:equal>
														<pg:equal colName="node_type" value="mailTask">
															邮件任务
														</pg:equal>
													</span>
												</pg:equal>
												<pg:notequal colName="isMulti" value="0">
														多实例
													<pg:equal colName="node_type" value="userTask">
														人工任务
													</pg:equal>
													<pg:equal colName="node_type" value="mailTask">
														邮件任务
													</pg:equal>
													<pg:notequal actual="${task.TASK_DEF_KEY_}" expressionValue="{node_key}"  >
													<select name="isMulti">
														<option value="1" <pg:equal colName="isMulti" value="1">selected</pg:equal>>串行</option>
														<option value="2" <pg:equal colName="isMulti" value="2">selected</pg:equal>>并行</option>
													</select>
													</pg:notequal>
												</pg:notequal>
											</td>
											<td >
												<span><pg:cell colName="DURATION_NODE"/></span>
											</td>
										</tr>
									</pg:notin>
									</pg:list>
								</table>
							
						</fieldset>
						
						<fieldset >
							<legend><strong>系统参数</strong></legend>
							<div>
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" >
									<pg:list autosort="false" requestKey="sysvariableList">
										<tr>
											<td><pg:cell colName="param_name"></pg:cell></td>
											<td><pg:cell colName="param_value"></pg:cell></td>
										</tr>
									</pg:list>
								</table>
							</div>
						</fieldset>
					
						<fieldset >
							<legend><strong>参数配置</strong></legend>
							<div class="rightbtn">
									<a href="javascript:addTr()" class="bt_small" id="addButton"><span>新增</span></a>
							</div>
							<div>
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="stable" id="tb1">
									<pg:header>
										<th>参数名称</th>
										<th>参数值</th>
										<th>操作</th>
									</pg:header>
									
									<pg:list autosort="false" requestKey="nodevariableList">
										<input type="hidden" name="param_name" value="<pg:cell colName='param_name'/>" />
										<tr>
											<td><pg:cell colName="param_name"></pg:cell></td>
											<td><input type="text" class="input1 w20"
												value="<pg:cell colName='param_value'/>" name="param_value"/></td>
											<td><a href="javascript:void(0);" class="bt"><span>删除</span></a>
											</td>
										</tr>
									</pg:list>
								</table>
							</div>
						</fieldset>
					</ul>
					
					<ul id="tab3" style="display: none;">
						<%@ include file="viewDealTaskInfo.jsp"%> 
					</ul>
				</div>
			</div>
			</fieldset>
			
			<fieldset >
				<legend><strong>处理意见</strong></legend>
			
				<div>
					<textarea rows="10" cols="50" id="completeReason" name="completeReason"  
					class="w120" style="width: 100%;font-size: 12px;height:50px;" maxlength="200"></textarea>
				</div>
			
				<div class="btnarea" id="dealButtonDiv" >
					<table width="100%" border="0" cellpadding="0" cellspacing="0" >
						<tr>
							<td width="250px;" align="center">
								<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="exeTask()"><span>通过</span></a>
								<select id="taskDefKey" name="taskDefKey" class="select1" >
									<option value="" selected>默认节点</option>
									<pg:list autosort="false" requestKey="nextNodeList">
										<option value="<pg:cell colName="node_key"/>" >
										<pg:cell colName="node_name"/></option>
									</pg:list>
								</select>
							</td>
							<td width="250px;" align="center">
								<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="rejectToPreTask()"><span>驳回</span></a>
								<select id="rejectedtype" name="rejectedtype" style=" width: 160px;">
									<option value="0" selected>上一个任务对应的节点</option>
									<option value="1" >当前节点的上一个节点</option>
								</select>
							</td>
							<td width="250px;" align="center">
								<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="delegateTask()"><span>转办</span></a>
								<input type="hidden" class="input1 w120" id="delegate_users_id" />
								<input type="text" class="input1 w120" id="delegate_users_name" />
								<a href="javascript:openChooseUsers('delegate')">选择</a>
							</td>
							<td width="250px;" align="center">
								<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="discardTask()"><span>废弃</span></a>
							</td>
						</tr>
					</table>
				</div>
			</fieldset>

	</form>
</body>
<script type="text/javascript">
var api = frameElement.api, W = api.opener;
$(document).ready(function() {
	$(".bt").click(function(){
		$(this).parent('td').parent('tr').remove();
	});
	
	
	if (${suspended == true}) {
		$("#dealButtonDiv").hide();
	}
});

//选择用户
function openChooseUsers(node_key){
	var url = "<%=request.getContextPath()%>/workflow/config/toChooseUserPage.page?users="+$("#"+node_key+"_users_id").val()+"&node_key="+node_key+"&user_realnames="+$("#"+node_key+"_users_name").val();
	$.dialog({ id:'nodeInfoIframe', title:'选择用户',width:1000,height:650, content:'url:'+url}); 
}
// 选择组
function openChooseGroups(node_key){
	var url = "<%=request.getContextPath()%>/workflow/config/toChooseGroupPage.page?groups="
		+ $("#" + node_key + "_groups_id").val()
		+ "&node_key="
		+ node_key
		+ "&group_realnames="
		+ $("#" + node_key + "_groups_name").val();
	$.dialog({ id:'nodeInfoIframe', title:'选择用户组',width:700,height:400, content:'url:'+url}); 
}

// 转办
function delegateTask(){
	var userid = $.trim($("#delegate_users_id").val());
	var currentUser = $.trim($("#currentUser").val());
	if (userid == ''){
		alert("请选择转办处理人！");
		return;
	} 
	
	if (currentUser == userid) {
		alert("不能转办给自己！");
		return;
	}
	
	$.dialog.confirm('确定将任务转办给'+$("#delegate_users_name").val()+'？', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/taskManage/delegateTask.page",
			data: {"taskId":'${task.ID_}',"changeUserId":userid,"processIntsId":'${task.PROC_INST_ID_}',
				"processKey":'${processKey}',"createUser":'${createUser}',"entrustUser":'${entrustUser}'},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data != 'success') {
					alert("转办任务出错:"+data);
				}else {
					W.modifyQueryData('${sysid}');
					api.close();	
				}
			}
		 });
	},function(){});   
}

//通过
function exeTask(){
	
	$.dialog.confirm('确定要通过任务吗？', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/taskManage/completeTask.page",
			data: formToJson("#submitForm"),
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data != 'success') {
					alert("通过任务出错:"+data);
				}else {
					W.modifyQueryData('${sysid}');
					api.close();	
				}
			}
		 });
	},function(){});   

}

//废弃任务
function discardTask() {
	
	var deleteReason = $.trim($("#completeReason").val());
	if (deleteReason == ''){
		alert("请填写废弃原因，在处理意见中填写！");
		return;
	} 
	
	$.dialog.confirm('确定要废弃吗？', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/taskManage/discardTask.page",
			data: {"processInstIds":'${task.PROC_INST_ID_}',"deleteReason":deleteReason,
				"taskId":'${task.ID_}',"processKey":'${processKey}'},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data != 'success') {
					alert("废弃任务出错:"+data);
				}else {
					W.modifyQueryData('${sysid}');
					api.close();	
				}
			}	
		 });	
	},function(){});   
}

//驳回任务
function rejectToPreTask(){
	
	$.dialog.confirm('确定要驳回吗？', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/taskManage/rejectToPreTask.page",
			data: formToJson("#submitForm"),
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data != 'success') {
					alert("驳回任务出错:"+data);
				}else {
					W.modifyQueryData('${sysid}');
					api.close();	
				}
			}	
		 });	
	},function(){});   
	
}

<%-- 
function checkNode(){
	if ($("#nodeto").attr("checked") == "checked") {
		$("#taskKey").show();
	}else {
		$("#taskKey").val('');
		$("#taskKey").hide();
	}
}
--%>

function addTr(){
	<%--
	var trHtml = "<tr><td><select name='node_id' id='node_id'>";
	<pg:list requestKey="nodeList">
		<pg:notin colName="node_type" scope="startEvent,endEvent">
			trHtml+="<option value='<pg:cell colName="id"/>'><pg:cell colName="node_name"/></option>";
		</pg:notin>
	</pg:list>
	trHtml+="</select></td>";
	--%>
	var trHtml="";
	trHtml+="<tr><td><input type='text' class='input1 w20' name='param_name' class='checkClass'/></td>";
	trHtml+="<td><input type='text' class='input1 w20' name='param_value'/></td>";
	<%--
	trHtml+="<td><input type='text' class='input1 w200' name='param_des'/></td>";
	trHtml+="<td><select name='is_edit_param'><option value='0' selected>是</option><option value='1'>否</option></select></td>";
	--%>
	trHtml+="<td><a href='javascript:void(0);' class='bt'>删除</a></td></tr>";
	$("#tb1").append(trHtml);
	$(".bt").click(function(){
		$(this).parent('td').parent('tr').remove();
	});
}

</script>
</head>
</html>
