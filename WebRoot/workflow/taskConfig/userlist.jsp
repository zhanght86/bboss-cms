<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<select name="select1" size="15" multiple="multiple" id="select1" onclick="showOrgInfo()"
	style="width: 220px; height: 400px">
	<pg:list requestKey="userList">
		<option value="<pg:cell colName='user_name'/>">
			<pg:cell colName="user_realname" />
		</option>
	</pg:list>
</select>
<pg:list requestKey="userList">
	<input type="hidden" value="<pg:cell colName='org_name'/>"
		id="<pg:cell colName='user_name'/>org_name" />
	<input type="hidden" value="<pg:cell colName='job_name'/>"
		id="<pg:cell colName='user_name'/>job_name" />
	<input type="hidden" value="<pg:cell colName='user_worknumber'/>"
		id="<pg:cell colName='user_name'/>user_worknumber" />
</pg:list>


