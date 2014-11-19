<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
描述：流程控制参数
作者：谭湘
版本：1.0
日期：2014-09-15
 --%>	
<style type="text/css">
  .table6 td{
	border:0;  
  }
</style>

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb2">
	<pg:header>
		<th>节点KEY</th>
		<th>节点名称</th>
		<th>处理工时<br/>(小时)</th>
		<th>节点描述</th>
		<th>待办URL</th>
		<th>业务处理类</th>
		<th>控制参数</th>
	</pg:header>

	<pg:list requestKey="nodeControlParamList">
		<tr class="replaceTr" >
			<td>
			<input <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
			type="hidden" name="NODE_KEY" id="NODE_KEY" value="<pg:cell colName="NODE_KEY"/>"/><pg:cell colName="NODE_KEY"/>
			</td>
			<td>
			<input <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
			type="hidden" name="NODE_NAME" id="NODE_NAME" value="<pg:cell colName="NODE_NAME"/>"/><pg:cell colName="NODE_NAME"/>
			</td>
			<td >
				<input <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
				type="text" name="DURATION_NODE" value="<pg:cell colName="DURATION_NODE"/>" class="input1 w50" onkeyup="chkPrice(this);" onblur="chkLast(this)" onpaste="javascript: return false;"/>
			</td>
			<td>
				<textarea <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
					rows="4" cols="80" id="NODE_DESCRIBE" name="NODE_DESCRIBE"
				 style="width: 200px;font-size: 12px;height:50px;" maxlength="200"><pg:cell colName="NODE_DESCRIBE"/></textarea>
			</td>
			<td>
				<textarea <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
				rows="4" cols="80" id="TASK_URL" name="TASK_URL"
				 style="width: 200px;font-size: 12px;height:50px;" maxlength="200"><pg:cell colName="TASK_URL"/></textarea>
			</td>
			<td>
				<textarea <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
				rows="4" cols="80" id="BUSSINESSCONTROLCLASS" name="BUSSINESSCONTROLCLASS" maxlength="200"
				style="width: 200px;font-size: 12px;height:50px;" ><pg:cell colName="BUSSINESSCONTROLCLASS"/></textarea>
			</td>
			<td>
				<table border="0" cellpadding="0" cellspacing="0" class="table6" >
					<tr>
					<%--<td><input <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
						type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_VALID" id="IS_VALID" value="1" <pg:equal colName="IS_VALID" value="1">checked</pg:equal>/>是否有效</td> --%>
						<td><input <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
						type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_EDIT" id="IS_EDIT" value="1" <pg:equal colName="IS_EDIT" value="1">checked</pg:equal>/>可修改 </td>
						<td><input type="checkbox" <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
						name="<pg:cell colName='NODE_KEY'/>_IS_CANCEL" id="IS_CANCEL" value="1" <pg:equal colName="IS_CANCEL" value="1">checked</pg:equal>/>可驳回</td>
						<td><input <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
						type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_EDITAFTER" id="IS_EDITAFTER" value="1" <pg:equal colName="IS_EDITAFTER" value="1">checked</pg:equal>/>可修改后续节点</td>
					</tr>
					<tr>
						<td><input <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
						type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_RECALL" id="IS_RECALL" value="1" <pg:equal colName="IS_RECALL" value="1">checked</pg:equal>/>可被撤回 </td>
						<td><input <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
						type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_DISCARD" id="IS_DISCARD" value="1" <pg:equal colName="IS_DISCARD" value="1">checked</pg:equal>/>可废弃</td>
						<td><input <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
						type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_AUTOAFTER" id="IS_AUTOAFTER" value="1" <pg:equal colName="IS_AUTOAFTER" value="1">checked</pg:equal>/>后续节点自动审批</td>
					</tr>
					<tr>
						<td><input <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
						type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_DISCARDED" id="IS_DISCARDED" value="1" <pg:equal colName="IS_DISCARDED" value="1">checked</pg:equal>/>可被废弃 </td>
						<td><input <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
						type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_MULTI" id="IS_MULTI" value="1" 
							<pg:equal colName="IS_MULTI" value="1">checked</pg:equal> 
							<pg:equal colName="IS_MULTI_DEFAULT" value="1">checked disabled </pg:equal>
							onclick="changeNodeTypeName('<pg:cell colName='NODE_KEY'/>')"/>多实例
						</td>
						<td>
						<input <pg:equal actual="${task.TASK_DEF_KEY_}" expressionValue="{NODE_KEY}">disabled</pg:equal>
						type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_SEQUENTIAL" id="IS_SEQUENTIAL" value="1" 
							<pg:equal colName="IS_SEQUENTIAL" value="1">checked</pg:equal> onclick="changeNodeTypeName('<pg:cell colName='NODE_KEY'/>')"/>串行
						<input type="hidden" name="<pg:cell colName='NODE_KEY'/>_IS_COPY" 
						id="IS_COPY" <pg:equal colName="IS_COPY" value="1">value="1"</pg:equal>/>
						<input disabled	type="checkbox" value="1" <pg:equal colName="IS_COPY" value="1">checked</pg:equal>/>可抄送
						</td>
					</tr>
				</table>
				
			</td>
		</tr>
	</pg:list>
</table>

<script language="javascript">
var api = frameElement.api, W = api.opener;

$(document).ready(function() {
	initNodeTypeName();
});

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

function initNodeTypeName() {
	<pg:list requestKey="nodeControlParamList">
		<pg:notin colName="NODE_TYPE" scope="startEvent,endEvent,serviceTask">
			changeNodeTypeName("<pg:cell colName='NODE_KEY'/>");
		</pg:notin>
	</pg:list>
}

</script>

