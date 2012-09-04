<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/css.jsp"%>
<div id="chooseuserlistContent">
	<pg:pager scope="request" data="chooseuserlist" desc="true" isList="false"
		containerid="chooseuserlistContainer" selector="chooseuserlistContent">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="stable" id="tb">
			<pg:notify>
				<tr>
					<td colspan=15 style="text-align: center;"><img
						src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>" /></td>
				</tr>
			</pg:notify>
			<pg:param name="user_worknumber" />
			<pg:param name="user_realname" />
			<pg:param name="user_name" />
			<pg:param name="usernames" />
			<pg:list autosort="false">
				<tr>
					<td class="tablecells" nowrap="nowrap"><pg:cell colName="user_worknumber"></pg:cell></td>
					<td class="tablecells" nowrap="nowrap"><pg:cell
							colName="user_name"></pg:cell></td>
					<td><pg:cell colName="user_realname"></pg:cell></td>
					<td><pg:cell colName="org_name"></pg:cell></td>
					<td><pg:cell colName="job_name"></pg:cell></td>
					<td><a
						href="javascript:deleteChooseUser('<pg:cell colName="user_name" />','<pg:cell colName="user_realname"/>')">移除11</a>
					</td>
				</tr>
			</pg:list>
		</table>
		<div class="pages">
			<input type="hidden" value="<pg:querystring/>" id="querystring" />
			<pg:index tagnumber="3" sizescope="3,5" />
		</div>
	</pg:pager>
</div>