<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	class="stable" id="tb">
	<pg:header>
		<th>所属节点</th>
		<th>参数名称</th>
		<th>参数值</th>
		<th>参数描述</th>
		<th>是否可修改</th>
	</pg:header>
	<pg:list autosort="false" requestKey="vrList">
		<input type="hidden" id="node_id" name="node_id"
			value="<pg:cell colName='id'/>" />
		<tr>
			<td><pg:cell colName="node_key"></pg:cell></td>
			<td><pg:cell colName="param_name"></pg:cell></td>
			<td><pg:cell colName="param_value"></pg:cell></td>
			<td><pg:cell colName="param_des"></pg:cell></td>
			<td> 
				<pg:equal colName="is_edit_param" value="1">否</pg:equal>
				<pg:equal colName="is_edit_param" value="0">是</pg:equal>
			</td>
		</tr>
	</pg:list>
</table>
