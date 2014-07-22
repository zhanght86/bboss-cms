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

<body class="easyui-layout">
<form name="submitForm" id="submitForm" method="post">
	
	<div region="west" split="true" title="模板类型" style="width: 220px; padding1: 1px; overflow: hidden;">
		<div class="easyui-accordion" fit="true" border="false">
		
			<div id="org_tree" title="组织结构" selected="true" style="overflow: auto;"></div>
			<div id="bussinesstree" title="业务类型" style="padding: 10px; overflow: auto;"></div>
			<div title="通用配置" style="padding: 10px; overflow: auto;">
				<a href="javascript:query('','0','通用')" name="0">通用配置</a>
			</div>
		</div>
	</div>
	
	<div region="center" style="overflow: hidden;" >
		<div class="easyui-layout" fit="true" style="background: #ccc;">
			<div region="north" style="height: 80px;" title="基本信息" split="false">
				<table border="0" cellpadding="0" cellspacing="0" class="table2" >
					<tr>
						<th width="60">业务主题:</th>
						<td width="400"><input type="text" id="businessKey" name="businessKey" value=""/></td>
					</tr>
				</table>
			</div>
			
			<div region="center" title="节点配置 " style="overflow: hidden;">
				<div>
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
						<pg:header>
							<th>节点KEY</th>
							<th>节点名称</th>
							<th>待办人</th>
							<th>待办组</th>
							<th>节点类型</th>
							<th>处理工时/小时</th>
							<!-- <th>提醒次数</th> -->
						</pg:header>
						
						<input type="hidden" id="processKey" name="processKey" value="${process_key}"/>
						
						<pg:list autosort="false" requestKey="nodeInfoList">
							<pg:notin colName="node_type" scope="startEvent,endEvent,serviceTask">
								<tr class="replaceTr">
									<input type="hidden" id="<pg:cell colName='node_key'/>_users_id" 
										name="candidate_users_id" value="" />
									<input type="hidden" id="<pg:cell colName='node_key'/>_groups_id" 
										name="candidate_groups_id" value="" />
									<input type="hidden" name="node_id" value="<pg:cell colName='id'/>"/>
									<input type="hidden" name="node_key" value="<pg:cell colName='node_key'/>" />
									<td><pg:cell colName="node_key"></pg:cell></td>
									<td><pg:cell colName="node_name"></pg:cell></td>
									<td>
										<input type="text" class="input1 w150" 
											id="<pg:cell colName='node_key'/>_users_name" 
											name="candidate_users_name" value="" />
										<a href="javascript:openChooseUsers('<pg:cell colName='node_key'/>')">选择</a>
									</td>
									<td>
										<input type="text" class="input1 w150"
											id="<pg:cell colName='node_key'/>_groups_name" 
											name="candidate_groups_name" value=""/>
										<a href="javascript:openChooseGroups('<pg:cell colName='node_key'/>')">选择</a>
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
											<select name="isMulti">
												<option value="1" <pg:equal colName="isMulti" value="1">selected</pg:equal>>串行</option>
												<option value="2" <pg:equal colName="isMulti" value="2">selected</pg:equal>>并行</option>
											</select>
										</pg:notequal>
									</td>
									<td >
										<input type="text" value="<pg:cell colName="duration_node"/>" name="duration_node" class="input1 w50" onkeyup="chkPrice(this);" onblur="chkLast(this)" onpaste="javascript: return false;"/>
									</td>
									<!-- 
									<td >
										<input type="text" name="noticenum" style="width: 50px;" value="<pg:notequal colName="noticenum" value="0"><pg:cell colName="noticenum"/></pg:notequal>" onpaste="javascript: return false;"
											onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" 
											onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
									</td>
									 -->
								</tr>
							</pg:notin>
						</pg:list>
					</table>
				</div>
			
				<div id="paramconig">
					<div class="title_box">
						<div class="rightbtn">
							<a href="javascript:addTr()" class="bt_small" id="addButton"><span>新增</span></a>
						</div>
					</div>
					<div id="nodevariableContainer">
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb1">
							<pg:header>
								<th>所属节点</th>
								<th>参数名称</th>
								<th>参数值</th>
								<th>参数描述</th>
								<th>是否可修改</th>
								<th>操作</th>
							</pg:header>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div region="south" split="false" style="height:50px;background:#efefef;">
		<div class="btnarea">
			<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="doCandidateSubmit()"><span>确定</span></a> 
		</div>
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
// 新增参数配置行
function addTr(){
	var trHtml = "<tr class='replaceTr'><td><select name='node_id' id='node_id'>";
	<pg:list requestKey="nodeInfoList">
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
		dataType:"json",			
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

//添加模板
function query(businessId,businessType,treename){
		
		$.ajax({

			url: "<%=request.getContextPath()%>/workflow/repository/getConfigTempleInfo.page",
			type: "post",
			data :{"processKey":'${process_key}',"business_type":businessType,"business_id":businessId},
			dataType:"json",			
			success: function(data){
				var nodeConfigList = data.nodeConfigList;
				var nodeVariableList = data.nodeVariableList;
				var configHtml = '';
				var variableHtml = '';
				
				$(".replaceTr").remove();
				
				if (nodeConfigList != null){
					<%-- 节点代办配置 --%>
					for(var i=0; i< nodeConfigList.length; i++){
						var nodekey = nodeConfigList[i].node_key == null ? '':nodeConfigList[i].node_key;
						var node_name = nodeConfigList[i].node_name == null ? '':nodeConfigList[i].node_name;
						var node_id = nodeConfigList[i].node_id == null ? '':nodeConfigList[i].node_id;
						var candidate_users_name=nodeConfigList[i].candidate_users_name == null ? '':nodeConfigList[i].candidate_users_name;
						var candidate_users_id=nodeConfigList[i].candidate_users_id == null ? '':nodeConfigList[i].candidate_users_id;
						var candidate_groups_name=nodeConfigList[i].candidate_groups_name == null ? '':nodeConfigList[i].candidate_groups_name;
						var candidate_groups_id=nodeConfigList[i].candidate_groups_id == null ? '':nodeConfigList[i].candidate_groups_id;
						var duration_node = nodeConfigList[i].duration_node == null ? '':nodeConfigList[i].duration_node;
						var noticenum = nodeConfigList[i].noticenum == 0 ? '':nodeConfigList[i].noticenum;
						var isMulti = nodeConfigList[i].isMulti ;
						var node_type = nodeConfigList[i].node_type ;
						
						if (node_type !='startEvent' && node_type !='endEvent' && node_type !='serviceTask'){
						
							configHtml +="<tr class='replaceTr'>";
							configHtml +="<input type='hidden' id='"+nodekey+"_users_id' ";
							configHtml +="name='candidate_users_id' value='"+candidate_users_id+"'/>";
							configHtml +="<input type='hidden' id='"+nodekey+"_groups_id' ";
							configHtml +="name='candidate_groups_id' value='"+candidate_groups_id+"' />";
							configHtml +="<input type='hidden' name='node_id' value='"+node_id+"'/>";
							configHtml +="<input type='hidden' name='node_key' value='"+nodekey+"'/>";
							configHtml +="<td>"+nodekey+"</td>";
							configHtml +="<td>"+node_name+"</td>";
							configHtml +="<td><input type='text' class='input1 w150' ";
							configHtml +="id='"+nodekey+"_users_name' ";
							configHtml +="name='candidate_users_name' value='"+candidate_users_name+"'/>";
							configHtml +="<a href=\"javascript:openChooseUsers('"+nodekey+"')\">选择</a></td>";
							configHtml +="<td><input type='text' class='input1 w150' ";
							configHtml +="id='"+nodekey+"_groups_name' ";
							configHtml +="name='candidate_groups_name' value='"+candidate_groups_name+"'/>";
							configHtml +="<a href=\"javascript:openChooseGroups('"+nodekey+"')\">选择</a></td>";
							
							configHtml +="<td >";
							if (isMulti == '0'){
								configHtml +="<span><input type='hidden' name='isMulti' value='0' />单实例";
								if (node_type=='userTask'){
									configHtml +=" 人工任务";
								}else if (node_type=='mailTask') {
									configHtml +=" 邮件任务";
								}
								configHtml +="</span>";
							}else {
								configHtml +="多实例";
								if (node_type=='userTask'){
									configHtml +=" 人工任务 ";
								}else if (node_type=='mailTask') {
									configHtml +=" 邮件任务 ";
								}
								configHtml +="<select name='isMulti'>";
								configHtml +="<option value='1' ";
								if (isMulti == '1') {
									configHtml +="selected";
								}
								configHtml +=">串行</option>";
								configHtml +="<option value='2' ";
								if (isMulti == '2') {
									configHtml +="selected";
								}
								configHtml +=">并行</option>";
								configHtml +="</select>";
							}
							configHtml +="</td>";
							
							configHtml +="<td><input type='text' value='"+duration_node+"' ";
							configHtml +="name='duration_node' style='width: 50px;' onkeyup='chkPrice(this);' ";
							configHtml +="onblur='chkLast(this)' onpaste='javascript: return false;'/></td></tr>";
							<%-- 
							configHtml +="<td><input type='text' name='noticenum' style='width: 50px;' ";
							configHtml +="value='" +noticenum+ "' onpaste='javascript: return false;' ";
							configHtml +="onkeyup=\"if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}";
							configHtml +="else{this.value=this.value.replace(/\D/g,'')}\" ";
							configHtml +="onafterpaste=\"if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}";
							configHtml +="else{this.value=this.value.replace(/\D/g,'')}\" /></td></tr> ";
							--%>
						}
						
					}
					$("#tb").append(configHtml);
				} 
				
				if (nodeVariableList != null){
					<%-- 节点参数配置 --%>
					for(var i=0; i< nodeVariableList.length; i++){
						var node_id = nodeVariableList[i].node_id == null ? '':nodeVariableList[i].node_id;
						var node_name = nodeVariableList[i].node_name == null ? '':nodeVariableList[i].node_name;
						var param_name = nodeVariableList[i].param_name == null ? '':nodeVariableList[i].param_name;
						var param_value = nodeVariableList[i].param_value == null ? '':nodeVariableList[i].param_value;
						var param_des = nodeVariableList[i].param_des == null ? '':nodeVariableList[i].param_des;
						var is_edit_param = nodeVariableList[i].is_edit_param == null ? '':nodeVariableList[i].is_edit_param;
						
						variableHtml+= "<tr class='replaceTr'><td><select name='node_id' id='node_id'>";
						<pg:list requestKey="nodeInfoList">
						variableHtml+="<option value='<pg:cell colName='id'/>'";
						if ("<pg:cell colName='id'/>" == node_id) {
							variableHtml+="selected ";
						}
						variableHtml+="><pg:cell colName='node_name'/></option>";
						</pg:list>
						variableHtml+="</select></td>";
						variableHtml+="<td><input type='text' class='input1 w20' name='param_name' class='checkClass' value='"+param_name+"'/></td>";
						variableHtml+="<td><input type='text' class='input1 w20' name='param_value' value='"+param_value+"'/></td>";
						variableHtml+="<td><input type='text' class='input1 w200' name='param_des' value='"+param_des+"'/></td>";
						variableHtml+="<td><select name='is_edit_param'><option value='0' selected>是</option><option value='1'>否</option></select></td>";
						variableHtml+="<td><a href='javascript:void(0);' class='bt'>删除</a></td></tr>";
					}
					$("#tb1").append(variableHtml);
					$(".bt").click(function(){
						$(this).parent('td').parent('tr').remove();
					});
				}
			}
		});
}

function chkPrice(obj){
	obj.value = obj.value.replace(/[^\d.]/g,""); 
	//必须保证第一位为数字而不是. 
	obj.value = obj.value.replace(/^\./g,""); 
	//保证只有出现一个.而没有多个. 
	obj.value = obj.value.replace(/\.{2,}/g,"."); 
	//小数点后面保留一位小数
	obj.value = obj.value.replace(/\.\d\d/g,"."); 
	//保证.只出现一次，而不能出现两次以上 
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$","."); 
} 

function chkLast(obj){ 
	// 如果出现非法字符就截取掉 
	if(obj.value.substr((obj.value.length - 1), 1) == '.') {
		obj.value = obj.value.substr(0,(obj.value.length - 1)); 
	}
}

$(document).ready(function() {
	
	$("#org_tree").load("../taskConfig/task_config_org_tree.jsp");
	$("#bussinesstree").load("../businesstype/businessTypeTree.jsp");
	
	$(".bt").click(function(){
		$(this).parent('td').parent('tr').remove();
	});
	
});
</script>
</html>