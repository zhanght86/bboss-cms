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
	
<body>

	<form name="submitForm" id="submitForm" method="post">
	
		<input type="hidden" id="taskId" name="taskId" value="${task.id}" />
		<input type="hidden" id="taskState" name="taskState" value="${taskState}" />
	
		<fieldset >
			<legend><strong>基本信息</strong></legend>
			<pg:beaninfo requestKey="task">
				<table border="0" cellpadding="0" cellspacing="0" class="table4" >
					<tr>
						<th width="60"><strong>业务主题:</strong></th>
						<td width="100">流程测试</td>
						<th width="60"><strong>任务名称:</strong></th>
						<td width="150"><pg:cell colName="name" /></td>
						<th width="60"><strong>任务ID:</strong></th>
						<td width="300"><pg:cell colName="id" /></td>
						<th width="100"><strong>流程实例ID:</strong></th>
						<td width="300"><pg:cell colName="processInstanceId" /></td>
					</tr>
					<tr>
						<th width="60"><strong>处理人:</strong></th>
						<td width="100"><pg:cell colName="assignee" /></td>
						<th width="60"><strong>签收人:</strong></th>
						<td width="150"><pg:cell colName="assignee" /></td>
						<th width="60"><strong>到达时间:</strong></th>
						<td width="300"><pg:cell colName="createTime" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
					</tr>
				</table>
			</pg:beaninfo>
		</fieldset>
		
		<fieldset >
			<legend><strong>节点处理人配置</strong></legend>
			<div>
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
					<pg:header>
						<th>节点KEY</th>
						<th>节点名称</th>
						<th>待办人</th>
						<th>待办组</th>
					</pg:header>
						
					<pg:list autosort="false" requestKey="nodeList">
					<pg:in colName="node_type" scope="userTask">
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
						</tr>
					</pg:in>
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
						<%-- <th>所属节点</th>--%>
						<th>参数名称</th>
						<th>参数值</th>
						<%-- 
						<th>参数描述</th>
						<th>是否可修改</th>
						--%>
						<th>操作</th>
					</pg:header>
					
					<pg:list autosort="false" requestKey="nodevariableList">
						<input type="hidden" name="param_name" value="<pg:cell colName='param_name'/>" />
						
						<tr>
						 
						<%--<td><pg:cell colName="node_name"></pg:cell></td>--%>
							<td><pg:cell colName="param_name"></pg:cell></td>
							
							<td><input type="text" class="input1 w20"
								value="<pg:cell colName='param_value'/>" name="param_value"/></td>
						<%--<td><input type="text" class="input1 w200"
								value="<pg:cell colName='param_des'/>" name="param_des"/></td>
							<td><select name="is_edit_param">
									<pg:equal colName="is_edit_param" value="0">
										<option value="0" selected>是</option>
										<option value="1">否</option>
									</pg:equal>
									<pg:equal colName="is_edit_param" value="1">
										<option value="0">是</option>
										<option value="1" selected>否</option>
									</pg:equal>
							</select></td>
							--%>
							<td><a href="javascript:void(0);" class="bt"><span>删除</span></a>
							</td>
						</tr>
					</pg:list>
				</table>
			</div>
		</fieldset>
		
		<fieldset >
			<legend></legend>
			<div>
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="tb4" >
					<tr>
						<th><strong>处理意见</strong></th>
						<td>
							<textarea rows="10" cols="50" id="completeReason" name="completeReason"  
							class="w120" style="width: 280px;font-size: 12px;height:50px;" maxlength="200"></textarea>
						</td>
						<th><strong>节点流向:</strong></th>
						<td>
							<select id="taskDefKey" name="taskDefKey" class="select1" style="width: 125px;">
								<option value="" selected>默认节点</option>
								<pg:list autosort="false" requestKey="nextNodeList">
									<option value="<pg:cell colName="node_key"/>" >
									<pg:cell colName="node_name"/></option>
								</pg:list>
							</select>
						</td>
					</tr>
				</table>
			</div>
		</fieldset>
		
		<div class="btnarea">
			<a href="javascript:void(0)" class="bt_2" id="closeButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
			<a href="javascript:void(0)" class="bt_2" id="addButton" onclick="rejectToPreTask()"><span>驳回</span></a>
			<a href="javascript:void(0)" class="bt_2" id="addButton" onclick="exeTask()"><span>处理</span></a>
		</div>
		
	</form>
</body>
<script type="text/javascript">
var api = frameElement.api, W = api.opener;
$(document).ready(function() {
	$(".bt").click(function(){
		$(this).parent('td').parent('tr').remove();
	});
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

//执行处理
function exeTask(){
	
	//var taskId = $("#taskId").val();
	//var taskState = $("#taskState").val();
	
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
				alert("处理任务出错:"+data);
			}else {
				W.modifyQueryData();
				api.close();	
			}
		}
	 });
}

//驳回任务
function rejectToPreTask(){
	
	//var taskId = $("#taskId").val();
	
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
				W.modifyQueryData();
				api.close();	
			}
		}	
	 });
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
