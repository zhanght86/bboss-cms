<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/css.jsp"%>
<form id="nodeinfoForm" name="nodeinfoForm" method="post">
<div id="nodeinfolistContent">
	<pg:pager scope="request" data="nodeListInfo" desc="true" isList="true"
		containerid="nodeinfolistContainer"
		selector="nodeinfolistContent">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="stable" id="tb">
			<pg:header>
				<th>节点KEY</th>
				<th>节点名称</th>
				<th>待办人</th>
				<th>待办组</th>
			</pg:header>
			<pg:notify>
				<tr>
					<td colspan=15 style="text-align: center;"><img
						src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>" /></td>
				</tr>
			</pg:notify>
			<pg:list autosort="false" requestKey="nodeListInfo">
				<input type="hidden" id="<pg:cell colName='node_key'/>_users_id" value="<pg:cell colName='candidate_users_id'></pg:cell>" />
				<input type="hidden" id="<pg:cell colName='node_key'/>_groups_id" value="<pg:cell colName='candidate_groups_id'></pg:cell>" />
				<tr>
					<td><pg:cell colName="node_key"></pg:cell></td>
					<td><pg:cell colName="node_name"></pg:cell></td>
					<td><input type="text" id="<pg:cell colName='node_key'/>_users_name" value="<pg:cell colName='candidate_users_name'></pg:cell>" disabled class="input1 w200"/><a href="javascript:openChooseUsers('<pg:cell colName="node_key"/>')">选择</a></td>
					<td><input type="text" id="<pg:cell colName='node_key'/>_groups_name" value="<pg:cell colName='candidate_groups_name'></pg:cell>" disabled class="input1 w200"/><a href="javascript:openChooseGroups('<pg:cell colName="node_key"/>')">选择</a></td>
				</tr>
			</pg:list>
		</table>
	</pg:pager>
</div>
</form>