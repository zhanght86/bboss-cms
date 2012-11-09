<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<pg:null actual="${errormsg}">
	<select name="selectedApp" size="25" multiple="multiple"
		id="selectedApp" style="width: 220px; height: 340px">
		<pg:list requestKey="selectedAppList">
			<option value="<pg:cell colName='appId'/>">
				<pg:cell colName='appName' />(<pg:cell colName="appId" />)
			</option>
		</pg:list>
	</select>
</pg:null>
<pg:notnull actual="${errormsg}">
	<div>出错啦：${errormsg}</div>
</pg:notnull>
