<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
	描述：开启流程实例
	作者：gw_tanx
	版本：1.0
	日期：2012-05-13
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>开启流程实例</title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
</head>

<body>

	<div class="tabbox" id="tabbox">
		<ul class="tab" id="menu0">
			<li><a href="javascript:void(0)" class="current"
				onclick="setTab(0,0)"><span>待办配置</span></a></li>
			<li><a href="javascript:void(0)" onclick="setTab(0,1)"><span>参数配置</span></a></li>
		</ul>
	</div>
	
	<form name="submitForm" id="submitForm" method="post">
	业务主键：
	<input type="text" id="businessKey" name="businessKey" value=""/>
		
	<div id="main0">
		<ul id="tab1" style="display: block;">
		<div>
		<pg:notempty actual="${activitiNodeNotExist}"><div align="left">${activitiNodeNotExist}</div></pg:notempty>
					
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
				<pg:header>
					<th>节点KEY</th>
					<th>节点名称</th>
					<th>待办人</th>
					<th>待办组</th>
				</pg:header>
					<input type="hidden" id="processKey" name="processKey" value="${process_key }"/>
					<input type="hidden" name="business_id" value="<pg:cell colName='business_id'/>"/>
					<input type="hidden" name="business_type" value="<pg:cell colName='business_type'/>"/>
				<pg:list autosort="false" requestKey="activitiNodeCandidateList">
					<tr>
						<input type="hidden" id="<pg:cell colName='node_key'/>_users_id" 
							name="candidate_users_id" value="<pg:cell colName='candidate_users_id'></pg:cell>" />
						<input type="hidden" id="<pg:cell colName='node_key'/>_groups_id" 
							name="candidate_groups_id" value="<pg:cell colName='candidate_groups_id'></pg:cell>" />
						<input type="hidden" id="node_id" name="node_id" value="<pg:cell colName='id'/>"/>
						<input type="hidden" id="node_key" name="node_key" 
							value="<pg:cell colName='node_key'></pg:cell>" />
						<td><pg:cell colName="node_key"></pg:cell></td>
						<td><pg:cell colName="node_name"></pg:cell></td>
						<td>
							<input type="text" class="input1 w200" 
								id="<pg:cell colName='node_key'/>_users_name" 
								name="candidate_users_name" 
								value="<pg:cell colName='candidate_users_name'></pg:cell>" />
							<a href="javascript:openChooseUsers('<pg:cell colName="node_key"/>')">选择</a>
						</td>
						<td>
							<input type="text" class="input1 w200"
								id="<pg:cell colName='node_key'/>_groups_name" 
								name="candidate_groups_name" 
								value="<pg:cell colName='candidate_groups_name'></pg:cell>"/>
							<a href="javascript:openChooseGroups('<pg:cell colName="node_key"/>')">选择</a>
						</td>
					</tr>
				</pg:list>
			</table>
			
		</div>
	</ul>
		
	<ul id="tab2">
		<div id="paramconig">
			<div class="title_box">
				<div class="rightbtn">
					<a href="javascript:addTr()" class="bt_small" id="addButton"><span>新增</span></a>
				</div>
				<strong>节点参数配置</strong>
			</div>
			<div id="nodevariableContainer">
				<input type="hidden" id="business_id" name="business_id" value="${business_id }"/>
				<input type="hidden" id="business_type" name="business_type" value="${business_type }"/>
				<input type="hidden" id="process_key" name="process_key" value="${process_key }"/>
				<div id="nodevariableContent"></div>
			</div>
		</div>
	</ul>
	</div>
	<div class="btnarea">
		<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="doCandidateSubmit()"><span>确定</span></a> 
	</div>
	</form>
</body>

<script language="javascript">
var api = frameElement.api, W = api.opener;
// 选择用户
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
// 加载节点参数配置界面
function loadNodevariableData(){
	$("#nodevariableContent").load("<%=request.getContextPath()%>/workflow/taskManage/taskvariableList.jsp");
}
// 新增参数配置行
function addTr(){
	var trHtml = "<tr><td><select name='node_id' id='node_id'>";
	<pg:list  requestKey="nodeInfoList">
		trHtml+="<option value='<pg:cell colName="id"/>'><pg:cell colName="node_name"/></option>";
	</pg:list>
	trHtml+="</select></td>";
	trHtml+="<td><input type='text' class='input1 w20' name='param_name' class='checkClass'/></td>";
	trHtml+="<td><input type='text' class='input1 w20' name='param_value'/></td>";
	trHtml+="<td><input type='text' class='input1 w200' name='param_des'/></td>";
	trHtml+="<td><select name='is_edit_param'><option value='0' selected>是</option><option value='1'>否</option></select></td>";
	trHtml+="<td><a href='javascript:void(0);' class='bt'>删除</a></td></tr>";
	$("#tb1").append(trHtml);
	$(".bt").click(function(){
		$(this).parent('td').parent('tr').remove();
	});
}

function doCandidateSubmit(){
	$.ajax({

		url: "<%=request.getContextPath()%>/workflow/repository/startPorcessInstance.page",
		
		type: "post",
		
		data: formToJson("#submitForm"),			
		datatype:"json",			
		success: function(data){
			if (data != 'success') {
				alert("流程实例开启出错："+data);
			}else {
				W.queryList();
				api.close();	
			}
		  }
	});
}

$(document).ready(function() {
	
	loadNodevariableData();
	
});
</script>
</html>