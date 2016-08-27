<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/css.jsp"%>
<div id="chooseuserlistContent">
	<pg:pager scope="request" data="userlist" desc="true" isList="false"
		containerid="choooseuserlistContainer"
		selector="chooseuserlistContent">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="stable" id="tb">
			<pg:header>
				<th>登录名</th>
				<th>用户实名</th>
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
				<tr id="<pg:cell colName="user_name" defaultValue="" />" >
					<td class="tablecells" nowrap="nowrap"><pg:cell
							colName="user_name"></pg:cell></td>
					<td><pg:cell colName="user_realname"></pg:cell></td>
					<td>
						<a href="javascript:deleteChooseUser('<pg:cell colName="user_name" defaultValue="" />')">移除</a>
					</td>
				</tr>
			</pg:list>
		</table>
		<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
	</pg:pager>
</div>