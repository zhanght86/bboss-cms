<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/css.jsp"%>
<div id="userlistContent">
	<pg:pager scope="request" data="userList" desc="true" isList="false"
		containerid="userlistContainer" selector="userlistContent">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="stable" id="tb">
			<pg:header>
				<th>工号</th>
				<th>登录名</th>
				<th>用户实名</th>
				<th>部门</th>
				<th>岗位</th>
				<th>操作</th>
			</pg:header>
			<pg:notify>
				<tr>
					<td colspan=15 style="text-align: center;"><img
						src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>" /></td>
				</tr>
			</pg:notify>
			<pg:param name="user_worknumber" />
			<pg:param name="user_realname" />
			<pg:param name="user_name" />
			<pg:param name="org_id" />
			<pg:list autosort="false">
				<tr>
					<td><pg:cell colName="user_worknumber"></pg:cell></td>
					<td class="tablecells" nowrap="nowrap"><pg:cell
							colName="user_name"></pg:cell></td>
					<td><pg:cell colName="user_realname"></pg:cell></td>
					<td><pg:cell colName="org_name"></pg:cell></td>
					<td><pg:cell colName="job_name"></pg:cell></td>
					<td><a
						href="javascript:addUserChoose('<pg:cell colName="user_name"/>','<pg:cell colName="user_realname"/>')">添加</a>
					</td>
				</tr>
			</pg:list>
		</table>
		<div class="pages">
			<input type="hidden" value="<pg:querystring/>" id="querystring" />
			<pg:index tagnumber="5" sizescope="10,20,50,100" />
		</div>
	</pg:pager>
</div>