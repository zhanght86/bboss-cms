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
							<legend><strong>处理人配置</strong></legend>
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
									<pg:header>
										<th>节点KEY</th>
										<th>节点名称</th>
										<th>待办人</th>
										<!-- <th>待办组</th> -->
										<th>节点类型</th>
									</pg:header>
										
									<pg:list autosort="false" requestKey="nodeList">
										<pg:equal colName="node_type" value="userTask">
											<tr>
												<input type="hidden" id="<pg:cell colName='node_key'/>" 
													name="node_key" value="<pg:cell colName='node_key'></pg:cell>" />
												<input type="hidden" name="is_copy" value="<pg:cell colName='is_copy'/>" />
												
												<input type="hidden" id="<pg:cell colName='node_key'/>_users_id" 
													name="node_users_id" value="<pg:cell colName='node_users_id'/>" />
													
												<input type="hidden" id="<pg:cell colName='node_key'/>_users_name" 
													name="node_users_name" value="<pg:cell colName='node_users_name'/>" />
													
												<input type="hidden" id="<pg:cell colName='node_key'/>_groups_id" 
													name="node_groups_id" value="<pg:cell colName='node_groups_id'/>" />
													
												<input type="hidden" id="<pg:cell colName='node_key'/>_org_id" 
													name="node_orgs_id" value="<pg:cell colName='node_orgs_id'/>" />
													
												<input type="hidden" id="<pg:cell colName='node_key'/>_org_name" 
													name="node_orgs_name"value="<pg:cell colName='node_orgs_name'/>" />	
													
													
												<td><pg:cell colName="node_key"></pg:cell></td>
												<td><pg:cell colName="node_name"></pg:cell></td>
												<td>
													<pg:empty colName="node_orgs_name">
														<pg:notempty colName="node_users_name">
															<textarea rows="8" cols="50" id="<pg:cell colName='node_key'/>_all_names"  
															style="width: 600px;" readonly 
															><pg:cell colName='node_users_name'/></textarea>
														</pg:notempty>
														<pg:empty colName="node_users_name">
															<textarea rows="8" cols="50" id="<pg:cell colName='node_key'/>_all_names"  
															style="width: 600px;" readonly></textarea>
														</pg:empty>
													</pg:empty>
													<pg:notempty colName="node_orgs_name">
														<pg:notempty colName="node_users_name">
															<textarea rows="8" cols="50" id="<pg:cell colName='node_key'/>_all_names" 
															style="width: 600px;" readonly 
															><pg:cell colName='node_users_name'/>,<pg:cell colName='node_orgs_name'/></textarea>
														</pg:notempty>
														<pg:empty colName="node_users_name">
															<textarea rows="8" cols="50" id="<pg:cell colName='node_key'/>_all_names" 
															style="width: 600px;" readonly 
															><pg:cell colName='node_orgs_name'/></textarea>
														</pg:empty>
													</pg:notempty>
													<a href="javascript:openChooseUsers('<pg:cell colName="node_key"/>')">选择</a>
													<a href="javascript:emptyChoose('<pg:cell colName="node_key"/>','1')">清空</a>
												</td>
												<%-- <td>
													<input type="text" class="input1 w200"
														id="" name="" value=""/>
													<a href="javascript:openChooseGroups('<pg:cell colName="node_key"/>')">选择</a>
													<a href="javascript:emptyChoose('<pg:cell colName="node_key"/>','2')">清空</a>
												</td> --%>
												<td >
													<span id="<pg:cell colName='node_key'/>_nodeTypeName"><pg:cell colName="nodeTypeName"/></span>
												</td>
											</tr>
										</pg:equal>
									</pg:list>
								</table>
						</fieldset>
						
						<fieldset >
							<legend><strong>控制参数</strong></legend>
							
							<%@ include file="/workflow/repository/nodeControlParam.jsp"%> 
							
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
										<tr>
											<td><input type="hidden" name="variable_param_name" value="<pg:cell colName='param_name'/>" /><pg:cell colName="param_name"/></td>
											<td><input type="text"   name="variable_param_value" value="<pg:cell colName='param_value'/>" class="input1 w20" /></td>
											<td><a href="javascript:void(0)" onclick="javascript:delVariable(this,'<pg:cell colName='id'/>');"><span>删除</span></a></td>
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
							
							<td width="300px;" align="center" id="cancelTd">
								<table>
									<tr>
										<td rowspan="2">
											<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="rejectToPreTask()"><span>驳回</span></a>
										</td>
										<td >
											<input type="checkbox" id="rejectedtype" name="rejectedtype" value="1"/>通过后直接返回本节点
										</td>
									</tr>
									<tr>
										<td>
											<input type="hidden" name="toActName" value="" />
											<select id="rejectToActId" name="rejectToActId" style=" width: 200px;">
												<pg:list requestKey="backActNodeList">
													<option value="<pg:cell colName="node_key"/>" ><pg:cell colName="node_name"/></option>
												</pg:list>
											</select>
										</td>
									</tr>
								</table>
							</td>
							
							<td width="250px;" align="center" >
								<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="delegateTask()"><span>转办</span></a>
								<input type="hidden" class="input1 w120" id="delegate_users_id" />
								<input type="text" class="input1 w120" id="delegate_users_name" />
								<a href="javascript:openChooseUsers('delegate')">选择</a>
							</td>
							
							<td width="100px;" align="center" id="discardTd">
								<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="discardTask()"><span>废弃</span></a>
							</td>
							
							<td width="100px;" align="center" id="recallTd">
								<a href="javascript:void(0)" class="bt_1" id="recallButton" onclick="recallTask()"><span>撤销</span></a>
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
	
	//选择拨回到某个节点，将节点Key参数赋值
	$("#rejectToActId").change( function() {
		$("input[name=toActName]").val($("select[id=rejectToActId] option:selected").text());
	}); 
	
	//是否有废弃功能
	if ("${task.isDiscard}"=="1" || "${isAdmin}" == "true"){
		$("#discardTd").show();
	}else{
		$("#discardTd").hide();
	}
	//是否有驳回功能
	if ("${task.isCancel}"=="1" || "${isAdmin}" == "true"){
		$("input[name=toActName]").val($("select[id=rejectToActId] option:selected").text());
		$("#cancelTd").show();
	}else {
		$("#cancelTd").hide();
	}
	//是否显示撤销功能
	if ("${isAdmin}" == "true"){
		$("#recallTd").show();
	}else{
		$("#recallTd").hide();
	}
	
	initNodeTypeName();
});

//清空选择
function emptyChoose(id,type){
	if (type=='1') {//清空用户或组
		$("#"+id+"_users_id").val('');
		$("#"+id+"_users_name").val('');
		$("#"+id+"_org_id").val('');
		$("#"+id+"_org_name").val('');
		$("#"+id+"_all_names").val('');
	}else {//清空组
		$("#"+id+"_groups_id").val('');
		$("#"+id+"_groups_name").val('');
	}
}

//选择用户
function openChooseUsers(node_key){
	//alert(node_key);
	var url = "<%=request.getContextPath()%>/workflow/config/toChooseUserPage.page?"
			+"process_key=${processKey}&users="+$('#'+node_key+'_users_id').val()
			+"&user_realnames="+encodeURI(encodeURI($('#'+node_key+'_users_name').val()))
			+"&org_id="+$('#'+node_key+'_org_id').val()
			+"&org_name="+encodeURI(encodeURI($('#'+node_key+'_org_name').val()))
			+"&all_names="+encodeURI(encodeURI($('#'+node_key+'_all_names').val()))
			+"&node_key="+node_key;
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
		$.dialog.alert("请选择转办处理人！",function(){});
		return;
	} 
	
	if (currentUser == userid) {
		$.dialog.alert("不能转办给自己！",function(){});
		return;
	}
	
	$.dialog.confirm('确定将任务转办给'+$("#delegate_users_name").val()+'？', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/taskManage/delegateTask.page",
			data: {"taskId":'${task.ID_}',"changeUserId":userid,"processIntsId":'${task.PROC_INST_ID_}',
				"processKey":'${processKey}',"createUser":'${createUser}',"entrustUser":'${entrustUser}',
				"completeReason":$.trim($("#completeReason").val())},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data != 'success') {
					$.dialog.alert("转办任务出错:"+data,function(){});
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
					$.dialog.alert("通过任务出错:"+data,function(){});
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
		$.dialog.alert("请填写废弃原因，在处理意见中填写！",function(){});
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
					$.dialog.alert("废弃任务出错:"+data,function(){});
				}else {
					W.modifyQueryData('${sysid}');
					api.close();	
				}
			}	
		 });	
	},function(){});   
}

//撤销任务
function recallTask() {
	
	var cancelTaskReason = $.trim($("#completeReason").val());
	
	$.dialog.confirm('确定撤销任务吗？', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/taskManage/cancelTask.page",
			data: {"processKey":'${processKey}',"processId":'${task.PROC_INST_ID_}',
				   "taskId":'${task.ID_}',"cancelTaskReason":cancelTaskReason},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data != 'success') {
					$.dialog.alert("撤销任务出错:"+data,function(){});
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
					$.dialog.alert("驳回任务出错:"+data,function(){});
				}else {
					W.modifyQueryData('${sysid}');
					api.close();	
				}
			}	
		 });	
	},function(){});   
	
}

//改变节点类型名称
function changeNodeTypeName(nodekey){
	var ismulti = $("input[name="+nodekey+"_IS_MULTI]").is(":checked");
	var isSquential = $("input[name="+nodekey+"_IS_SEQUENTIAL]").is(":checked");
	
	if (ismulti && isSquential) {
		$("#"+nodekey+"_nodeTypeName").html("多实例  串行");
	}else if (ismulti && !isSquential) {
		$("#"+nodekey+"_nodeTypeName").html("多实例  并行");
	}else {
		$("#"+nodekey+"_nodeTypeName").html("单实例  串行");
	}
} 

function initNodeTypeName() {
	<pg:list requestKey="nodeList">
		<pg:notin colName="node_type" scope="startEvent,endEvent,serviceTask">
			changeNodeTypeName("<pg:cell colName='node_key'/>");
		</pg:notin>
	</pg:list>
}

// 删除变量参数
function delVariable(obj,variableId) {
	if (variableId != '') {
		$.dialog.confirm('确定要删除参数吗？', function(){
			
			$.ajax({
		 	 	type: "POST",
				url : "<%=request.getContextPath()%>/workflow/taskManage/delVariable.page",
				data: {"variableId":variableId},
				dataType : 'json',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
					if (data != 'success') {
						$.dialog.alert("删除变量参数出错:"+data,function(){});
					}else {
						$(obj).parent('td').parent('tr').remove();	
					}
				}	
			 });	
		},function(){});   
	}else {
		$(obj).parent('td').parent('tr').remove();
	}
}

function addTr(){
	var trHtml="";
	trHtml+="<tr><td><input type='text' class='input1 w20' name='variable_param_name' class='checkClass'/></td>";
	trHtml+="<td><input type='text' class='input1 w20' name='variable_param_value'/></td>";
	trHtml+="<td><a href='javascript:void(0);' onclick=\"delVariable(this,'')\">删除</a></td></tr>";
	$("#tb1").append(trHtml);
}

</script>
</head>
</html>
