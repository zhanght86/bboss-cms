<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/css.jsp"%>

<div id="customContent">


	<table width="100%" border="0" cellpadding="0" cellspacing="0"
		class="stable" id="tb">
		<pg:list autosort="false" requestKey="nodelist">

			<pg:header>
				<th>key</th>
				<th>节点ID</th>
				<th>节点名称</th>
				<th>操作</th>
			</pg:header>
			<pg:notify>
				<tr>
					<td colspan=15 style="text-align: center;"><img
						src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>" /></td>
				</tr>
			</pg:notify>

			<tr>
				<td><pg:cell colName="process_key" /></td>
				<td><pg:cell colName="node_id" /></td>
				<td><pg:cell colName="node_name" /></td>
				<td><a href="<%=request.getContextPath()%>/test/showActivitiNodeEdit.page?nodeId=<pg:cell colName='id' />" id="nodelist">配置</a></td>
			</tr>
		</pg:list>
	</table>
</div>

</div>
