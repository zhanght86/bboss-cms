<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/css.jsp"%>
<div id="choosegrouplistContent">
	<pg:pager scope="request" data="grouplist" desc="true" isList="false"
		containerid="chooosegrouplistContainer"
		selector="choosegrouplistContent">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="stable" id="tb">
			<pg:header>
				<th>用户组名称</th>
				<th>用户组描述</th>
				<th>操作</th>
			</pg:header>
			<pg:notify>
				<tr>
					<td colspan=15 style="text-align: center;"><img
						src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>" /></td>
				</tr>
			</pg:notify>
			<pg:param name="userName" />
			<pg:list autosort="false">
				<tr id="<pg:cell colName="group_name" defaultValue="" />">
					<td class="tablecells" nowrap="nowrap"><pg:cell colName="group_name"></pg:cell></td>
					<td><pg:cell colName="group_desc"></pg:cell></td>
					<td>
						<a href="javascript:deleteChooseGroup('<pg:cell colName="group_name" defaultValue="" />')">移除</a>
					</td>
				</tr>
			</pg:list>
		</table>
		<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
	</pg:pager>
</div>