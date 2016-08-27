<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="stable" id="tb1">
	<pg:header>
		<th>所属节点</th>
		<th>参数名称</th>
		<th>参数值</th>
		<th>参数描述</th>
		<th>是否可修改</th>
		<th>操作</th>
	</pg:header>
	<pg:list autosort="false" requestKey="nodevariablelist">
		<tr>
			<td><pg:cell colName="node_name"></pg:cell>
				<input type="hidden" id="node_id" name="variable_node_id" value="<pg:cell colName='node_id'/>" />
				<input type="hidden" name="variable_param_name" value="<pg:cell colName='param_name'/>" />
			</td>
			<td><pg:cell colName="param_name"></pg:cell>
			</td>
			<td><input type="text" class="input1 w20" name="variable_param_value" value="<pg:cell colName='param_value'/>" /></td>
			<td><input type="text" class="input1 w200" name="variable_param_des" value="<pg:cell colName='param_des'/>" /></td>
			<td><select name="variable_is_edit_param">
					<pg:equal colName="is_edit_param" value="0">
						<option value="0" selected>是</option>
						<option value="1">否</option>
					</pg:equal>
					<pg:equal colName="is_edit_param" value="1">
						<option value="0">是</option>
						<option value="1" selected>否</option>
					</pg:equal>
			</select></td>
			<td><a href="javascript:void(0);" class="bt" ><span>删除</span></a>
			</td>
		</tr>
	</pg:list>
</table>

<script type="text/javascript">
$(document).ready(function() {
	$(".bt").click(function(){
		$(this).parent('td').parent('tr').remove();
	});
});
</script>
