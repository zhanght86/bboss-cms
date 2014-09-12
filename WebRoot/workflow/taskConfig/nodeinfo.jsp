<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<div class="tabbox" id="tabbox">
	<ul class="tab" id="menu0">
		<li><a href="javascript:void(0)" class="current" onclick="setTab(0,0)"><span>处理人配置</span></a></li>
		<li><a href="javascript:void(0)" onclick="setTab(0,1)"><span>参数配置</span></a></li>
		<li><a href="javascript:void(0)" onclick="setTab(0,2)"><span>控制变量配置</span></a></li>
	</ul>
</div>

<div id="main0">
	
	<ul id="tab1" style="display: block;">
		<div>
			<pg:notempty actual="${activitiNodeNotExist}"><div align="left">${activitiNodeNotExist}</div></pg:notempty>
			
			<form name="submitForm" id="submitForm" method="post">
				
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
					<pg:header>
						<th>节点KEY</th>
						<th>节点名称</th>
						<th>待办人</th>
						<th>待办组</th>
					</pg:header>
					<pg:list autosort="false" requestKey="activitiNodeCandidateList">
					<pg:notin colName="node_type" scope="startEvent,endEvent,serviceTask">
						<input type="hidden" id="process_key" name="process_key" value="${process_key }"/>
						<input type="hidden"
							id="<pg:cell colName='node_key'/>_users_id" name="candidate_users_id"
							value="<pg:cell colName='candidate_users_id'></pg:cell>" />
						<input type="hidden"
							id="<pg:cell colName='node_key'/>_groups_id" name="candidate_groups_id"
							value="<pg:cell colName='candidate_groups_id'></pg:cell>" />
						<input type="hidden" id="node_id" name="node_id" value="<pg:cell colName='id'/>"/>
						<input type="hidden" name="business_id" value="<pg:cell colName='business_id'/>"/>
						<input type="hidden" name="business_type" value="<pg:cell colName='business_type'/>"/>
						<tr>
							<td><pg:cell colName="node_key"></pg:cell></td>
							<td><pg:cell colName="node_name"></pg:cell></td>
							<td><input type="text"
								id="<pg:cell colName='node_key'/>_users_name" name="candidate_users_name"
								value="<pg:cell colName='candidate_users_name'></pg:cell>"
								 class="input1 w200" readonly/>
								<a href="javascript:openChooseUsers('<pg:cell colName="node_key"/>')">选择</a>
								<a href="javascript:emptyChoose('<pg:cell colName="node_key"/>','1')">清空</a>
							</td>
							<td><input type="text"
								id="<pg:cell colName='node_key'/>_groups_name" name="candidate_groups_name"
								value="<pg:cell colName='candidate_groups_name'></pg:cell>"
								 class="input1 w200" readonly/>
								<a href="javascript:openChooseGroups('<pg:cell colName="node_key"/>')">选择</a>
								<a href="javascript:emptyChoose('<pg:cell colName="node_key"/>','2')">清空</a>
							</td>
						</tr>
						</pg:notin>
					</pg:list>
				</table>
				
				<div class="btnarea">
					<a href="javascript:void(0)" class="bt_1" id="addButton"
						onclick="doCandidateSubmit()"><span>确定</span></a> 
				</div>
			</form>
			
		</div>
	</ul>

	<ul id="tab2">
		<div id="paramconig">
			<div class="title_box">
				<div class="rightbtn">
					<a href="javascript:loadVariableResource()" class="bt_small" id="addButton"><span>加载配置资源</span></a>
					<a href="javascript:loadClearVariableResource()" class="bt_small" id="addButton"><span>加载配置资源清除原所有数据</span></a>
					<a href="javascript:addTr()" class="bt_small" id="addButton"><span>新增</span></a>
				</div>
				<strong>节点参数配置</strong>
			</div>
			<div id="nodevariableContainer">
				<form name="submitParamForm" id="submitParamForm" action="test2.page" method="post">
				<input type="hidden" id="business_id" name="business_id" value="${business_id }"/>
				<input type="hidden" id="business_type" name="business_type" value="${business_type }"/>
				<input type="hidden" id="process_key" name="process_key" value="${process_key }"/>
				<div id="nodevariableContent">
				</div>
				<div class="btnarea">
					<a href="javascript:void(0)" class="bt_1" id="addButton"
						onclick="doParamSubmit()"><span>确定</span></a> 
				</div>
				</form>
			</div>
		</div>
	</ul>
	
	<ul id="tab3">
	
		<div id="contralconig">
			<form name="contralForm" id="contralForm" method="post">
			
				<input type="hidden" id="business_id" name="business_id" value="${business_id }"/>
				<input type="hidden" id="business_type" name="business_type" value="${business_type }"/>
				<input type="hidden" id="process_key" name="process_key" value="${process_key }"/>
				
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
					<pg:header>
						<th>节点KEY</th>
						<th>节点名称</th>
						<th>处理工时/小时</th>
						<th>节点描述</th>
						<th>业务处理类</th>
						<th>待办URL</th>
						<th>操作</th>
					</pg:header>
					
					<pg:list autosort="false" requestKey="contralParamList">
						<tr>
							<td><pg:cell colName="NODE_KEY"/></td>
							<td><pg:cell colName="NODE_NAME"/></td>
							<td id="DURATION_NODE<pg:cell colName="NODE_KEY"/>">
							<pg:equal colName="DURATION_NODE" value="0">&nbsp;</pg:equal>
							<pg:notequal colName="DURATION_NODE" value="0">
								<pg:cell colName="DURATION_NODE"/>
							</pg:notequal>
							</td>
							<td id="NODE_DESCRIBE<pg:cell colName="NODE_KEY"/>"><pg:cell colName="NODE_DESCRIBE"/></td>
							<td id="BUSSINESSCONTROLCLASS<pg:cell colName="NODE_KEY"/>"><pg:cell colName="BUSSINESSCONTROLCLASS"/></td>
							<td id="TASK_URL<pg:cell colName="NODE_KEY"/>"><pg:cell colName="TASK_URL"/></td>
							<td>
							<a href="javascript:void(0)" onclick="updateNodeControlParam('<pg:cell colName="NODE_KEY" />')">设置</a>
							</td>
						</tr>
					</pg:list>
				</table>
					
			</form>
		</div>
	</ul>
	
</div>
<div id="sp" style="display:none">
		<div style="color:#99BBE8;background:#fafafa;padding:5px;">选择节点</div>
		<pg:list autosort="false" requestKey="nodeInfoList">
			<input type="radio"  name="lang" value="<pg:cell colName='id'/>"/><span><pg:cell colName="node_name"></pg:cell></span><br/>
		</pg:list>
</div>
<script type="text/javascript">

// 清空选择
function emptyChoose(id,type){
	if (type=='1') {//清空用户
		$("#"+id+"_users_id").val('');
		$("#"+id+"_users_name").val('');
	}else {//清空组
		$("#"+id+"_groups_id").val('');
		$("#"+id+"_groups_name").val('');
	}
}

function updateNodeControlParam(nodekey){
	 var url="<%=request.getContextPath()%>/workflow/config/toUpdateNodeControlParam.page?"
			 +"process_key=${process_key}&business_id=${business_id}&business_type=${business_type}"
			 +"&taskKey="+nodekey+"&pageType=fromTemplet";
	$.dialog({ id:'iframeNewId', title:'设置节点控制参数信息',width:580,height:600, content:'url:'+url});  
}

function setUser(node_key,users_id,users_name){
		$("#"+node_key+"_users_id").val(users_id);
		$("#"+node_key+"_users_name").val(users_name);
}

function setGroup(node_key,groups_id,groups_name){
		$("#"+node_key+"_groups_id").val(groups_id);
		$("#"+node_key+"_groups_name").val(groups_name);
}

var api = frameElement.api;
function openChooseUsers(node_key){
	//alert(node_key);
	var url = "<%=request.getContextPath()%>/workflow/config/toChooseUserPage.page?users="+$("#"+node_key+"_users_id").val()+"&node_key="+node_key+"&user_realnames="+$("#"+node_key+"_users_name").val();
	$.dialog({ id:'nodeInfoIframe', title:'选择用户',width:1000,height:650, content:'url:'+url}); 
	
}

function openMessTemple(templeId,templeType) {
	var title="";
	if (templeType == '0') {
		title = "短信模板";
	}else{
		title = "邮件模板";
	}
	var url = "<%=request.getContextPath()%>/workflow/taskConfig/chooseMessageTemple.jsp?templeType="+templeType+"&templeId="+templeId;
	$.dialog({ id:'nodeInfoIframe', title:title,width:900,height:500, content:'url:'+url});
}

function openChooseGroups(node_key){
	var url = "<%=request.getContextPath()%>/workflow/config/toChooseGroupPage.page?groups="
		+ $("#" + node_key + "_groups_id").val()
		+ "&node_key="
		+ node_key
		+ "&group_realnames="
		+ $("#" + node_key + "_groups_name").val();
	$.dialog({ id:'nodeInfoIframe', title:'选择用户组',width:700,height:400, content:'url:'+url}); 
	}

	
	function doCandidateSubmit(){
		$.ajax({

			url: "updateActivitiNodeCandidate.page",
			
			type: "post",
			
			data: formToJson("#submitForm"),			
			dataType:"json",			
			success: function(data){
			    alert(data);
			  }
			
			});
	}
	
	function doParamSubmit(){
		$.ajax({

			url: "submitNodeVariable.page",
			
			type: "post",
			
			data: formToJson("#submitParamForm"),			
			dataType:"json",			
			success: function(data){
			    alert(data);
			  }
			
			});
	}
	
	function loadNodevariableData(){
		$("#nodevariableContent").load("queryNodevariable.page?business_id="+$("#business_id").val()+"&business_type="+$("#business_type").val()+"&processKey="+$("#process_key").val());
	}
	
	function loadVariableResource(){
		$("#nodevariableContent").load("loadVariableResource.page?business_id="+$("#business_id").val()
		+"&business_type="+$("#business_type").val()+"&processKey="+$("#process_key").val());
	}
	
	function loadClearVariableResource(){
		$("#nodevariableContent").load("loadVariableResource.page?isClear=true&business_id="+$("#business_id").val()
		+"&business_type="+$("#business_type").val()+"&processKey="+$("#process_key").val());
	}
	
	function addTr(){
		var businessId = $("#business_id").val();
		var businessType = $("#business_type").val();
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
		//trHtml+="<input type='hidden' name='business_id' value='"+businessId+"'/>";
		//trHtml+="<input type='hidden' name='business_type' value='"+businessType+"'/>";
		$("#tb1").append(trHtml);
		$(".bt").click(function(){
			$(this).parent('td').parent('tr').remove();
		});
	}
	
	$(document).ready(function() {
		loadNodevariableData();
		
		if($("#taskId").val()){
			$("#tabbox").attr("style","display:none");
		};
		
	   });
	
</script>
</html>
