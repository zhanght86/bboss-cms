<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
描述：开启流程实例配置
作者：谭湘
版本：1.0
日期：2014-07-24
 --%>	
<style type="text/css">
  .table6 td{
	border:0;  
  }
</style>

<div class="tabbox">
	<ul class="tab" id="menu1">
		<li><a href="javascript:void(0)" class="current" onclick="setTab(1,0)"><span>处理人配置</span></a></li>
		<li><a href="javascript:void(0)" onclick="setTab(1,1)"><span>控制变量配置</span></a></li>
		<li><a href="javascript:void(0)" onclick="setTab(1,2)"><span>参数配置</span></a></li>
	</ul>
</div>

<div id="main1" >
	<ul id="tab1" style="display:block;">
		<div id="handlerConfig" >
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
				<pg:header>
					<th>节点KEY</th>
					<th>节点名称</th>
					<th>待办人</th>
					<th>待办组</th>
					<th>节点类型</th>
				</pg:header>
				
				<input type="hidden" id="processKey" name="processKey" value="${processKey}"/>
				
				<pg:list requestKey="nodeConfigList">
					<pg:notin colName="node_type" scope="startEvent,endEvent,serviceTask">
						<tr class="replaceTr">
						
							<input type="hidden" id="<pg:cell colName='node_key'/>_users_id" 
								name="candidate_users_id" value="<pg:cell colName='candidate_users_id'/>" />
							<input type="hidden" id="<pg:cell colName='node_key'/>_groups_id" 
								name="candidate_groups_id" value="<pg:cell colName='candidate_groups_id'/>" />
							<input type="hidden" name="node_id" value="<pg:cell colName='id'/>"/>
							<input type="hidden" name="node_key" value="<pg:cell colName='node_key'/>" />
							<td><pg:cell colName="node_key"></pg:cell></td>
							<td><pg:cell colName="node_name"></pg:cell></td>
							<td>
								<input type="text" class="input1 w200" readonly
									id="<pg:cell colName='node_key'/>_users_name" 
									name="candidate_users_name" value="<pg:cell colName='candidate_users_name'/>" />
								<a href="javascript:openChooseUsers('<pg:cell colName='node_key'/>')">选择</a>
								<a href="javascript:emptyChoose('<pg:cell colName="node_key"/>','1')">清空</a>
							</td>
							<td>
								<input type="text" class="input1 w200" readonly
									id="<pg:cell colName='node_key'/>_groups_name" 
									name="candidate_groups_name" value="<pg:cell colName='candidate_groups_name'/>"/>
								<a href="javascript:openChooseGroups('<pg:cell colName='node_key'/>')">选择</a>
								<a href="javascript:emptyChoose('<pg:cell colName="node_key"/>','2')">清空</a>
							</td>
							<td >
								<span id="<pg:cell colName='node_key'/>_nodeTypeName"><pg:cell colName="nodeTypeName"/></span>
							</td>
						</tr>
					</pg:notin>
				</pg:list>
			</table>
		</div>
	</ul>
	
	<ul id="tab2" style="display: none;">
		<div id="controlParamConfig" >
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb2">
				<pg:header>
					<th>节点KEY</th>
					<th>处理工时<br/>(小时)</th>
					<th>节点描述</th>
					<th>待办URL</th>
					<th>业务处理类</th>
					<th>控制参数</th>
				</pg:header>
			
				<pg:list requestKey="nodeControlParamList">
					<tr class="replaceTr">
						<td>
						<input type="hidden" name="NODE_KEY" id="NODE_KEY" value="<pg:cell colName="NODE_KEY"/>"/><pg:cell colName="NODE_KEY"/>
						</td>
						<td >
							<input type="text" name="DURATION_NODE" value="<pg:cell colName="DURATION_NODE"/>" class="input1 w50" onkeyup="chkPrice(this);" onblur="chkLast(this)" onpaste="javascript: return false;"/>
						</td>
						<td>
							<textarea rows="4" cols="80" id="NODE_DESCRIBE" name="NODE_DESCRIBE"
							 style="width: 200px;font-size: 12px;height:50px;" maxlength="200"><pg:cell colName="NODE_DESCRIBE"/></textarea>
						</td>
						<td>
							<textarea rows="4" cols="80" id="TASK_URL" name="TASK_URL"
							 style="width: 200px;font-size: 12px;height:50px;" maxlength="200"><pg:cell colName="TASK_URL"/></textarea>
						</td>
						<td><input type="text" class="input1 w100" name="BUSSINESSCONTROLCLASS" id="BUSSINESSCONTROLCLASS" value="<pg:cell colName="BUSSINESSCONTROLCLASS"/>"/></td>
						<td>
							<table border="0" cellpadding="0" cellspacing="0" class="table6">
								<tr>
									<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_VALID" id="IS_VALID" value="1" <pg:equal colName="IS_VALID" value="1">checked</pg:equal>/>是否有效</td>
									<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_EDIT" id="IS_EDIT" value="1" <pg:equal colName="IS_EDIT" value="1">checked</pg:equal>/>可修改 </td>
									<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_CANCEL" id="IS_CANCEL" value="1" <pg:equal colName="IS_CANCEL" value="1">checked</pg:equal>/>可驳回</td>
									<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_EDITAFTER" id="IS_EDITAFTER" value="1" <pg:equal colName="IS_EDITAFTER" value="1">checked</pg:equal>/>可修改后续节点</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_AUTO" id="IS_AUTO" value="1" <pg:equal colName="IS_AUTO" value="1">checked</pg:equal>/>自动审批</td>
									<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_RECALL" id="IS_RECALL" value="1" <pg:equal colName="IS_RECALL" value="1">checked</pg:equal>/>可被撤回 </td>
									<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_DISCARD" id="IS_DISCARD" value="1" <pg:equal colName="IS_DISCARD" value="1">checked</pg:equal>/>可废弃</td>
									<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_AUTOAFTER" id="IS_AUTOAFTER" value="1" <pg:equal colName="IS_AUTOAFTER" value="1">checked</pg:equal>/>后续节点自动审批</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_COPY" id="IS_COPY" value="1" <pg:equal colName="IS_COPY" value="1">checked</pg:equal>/>可抄送</td>
									<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_MULTI" id="IS_MULTI" value="1" 
										<pg:equal colName="IS_MULTI" value="1">checked</pg:equal> 
										<pg:equal colName="IS_MULTI_DEFAULT" value="1">checked disabled </pg:equal>
										onclick="changeNodeTypeName('<pg:cell colName='NODE_KEY'/>')"/>多实例
									</td>
									<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_SEQUENTIAL" id="IS_SEQUENTIAL" value="1" 
										<pg:equal colName="IS_SEQUENTIAL" value="1">checked</pg:equal> onclick="changeNodeTypeName('<pg:cell colName='NODE_KEY'/>')"/>串行
									</td>
									<td>&nbsp;</td>
								</tr>
							</table>
							
						</td>
					</tr>
				</pg:list>
			</table>
		</div>
	</ul>

	<ul id="tab3" style="display: none;">
		<div id="paramConfig">
			<div class="title_box">
				<div class="rightbtn">
					<a href="javascript:addTr()" class="bt_small" id="addButton"><span>新增</span></a>
				</div>
			</div>
			<div id="nodevariableContainer">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb3">
					<pg:header>
						<th>所属节点</th>
						<th>参数名称</th>
						<th>参数值</th>
						<th>参数描述</th>
						<th>操作</th>
					</pg:header>
				</table>
			</div>
		</div>
	</ul>
</div>

<script language="javascript">
$(document).ready(function() {
	initNodeTypeName();
});

//新增参数配置行
function addTr(){
	var trHtml = "<tr class='replaceTr'><td><select name='node_id' id='node_id'>";
	<pg:list requestKey="nodeConfigList">
		trHtml+="<option value='<pg:cell colName="id"/>'><pg:cell colName="node_name"/></option>";
	</pg:list>
	trHtml+="</select></td>";
	trHtml+="<td><input type='text' class='input1 w20' name='param_name' class='checkClass'/></td>";
	trHtml+="<td><input type='text' class='input1 w20' name='param_value'/></td>";
	trHtml+="<td><input type='text' class='input1 w200' name='param_des'/></td>";
	trHtml+="<td><a href='javascript:void(0);' class='bt'>删除</a></td></tr>";
	$("#tb3").append(trHtml);
	$(".bt").click(function(){
		$(this).parent('td').parent('tr').remove();
	});
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
	<pg:list requestKey="nodeConfigList">
		<pg:notin colName="node_type" scope="startEvent,endEvent,serviceTask">
			changeNodeTypeName("<pg:cell colName='node_key'/>");
		</pg:notin>
	</pg:list>
}
</script>
