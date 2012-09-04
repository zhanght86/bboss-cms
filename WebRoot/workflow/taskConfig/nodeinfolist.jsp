<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/css.jsp"%>
<div id="nodeinfolistContent">
	<pg:pager scope="request" data="grouplist" desc="true" isList="false"
		containerid="nodeinfolistContainer"
		selector="nodeinfolistContent">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="stable" id="tb">
			<pg:header>
				<th>节点KEY</th>
				<th>节点名称</th>
				<th>待办人</th>
				<th>待办组</th>
				<th>操作</th>
			</pg:header>
			<pg:notify>
				<tr>
					<td colspan=15 style="text-align: center;"><img
						src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>" /></td>
				</tr>
			</pg:notify>
			<pg:list autosort="false">
				<tr id="<pg:cell colName="node_key" defaultValue="" />">
					<td><pg:cell colName="node_key"></pg:cell></td>
					<td><pg:cell colName="node_name"></pg:cell></td>
					<td><pg:cell colName="candidate_users_name"></pg:cell></td>
					<td><pg:cell colName="candidate_groups_name"></pg:cell></td>
					<td>
						<a href="#">选择</a>
					</td>
				</tr>
			</pg:list>
		</table>
		<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
	</pg:pager>
</div>