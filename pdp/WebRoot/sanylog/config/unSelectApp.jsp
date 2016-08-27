<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<pg:null actual="${errormsg}">
	<select name="unSelectApp" size="25" multiple="multiple"
		id="unSelectApp" style="width: 220px; height: 340px">
		<pg:list requestKey="unSelectAppList">
			<option value="<pg:cell colName='appId'/>">
				<pg:cell colName='appName' />(<pg:cell colName="appId" />)
			</option>
		</pg:list>
	</select>
</pg:null>
<pg:notnull actual="${errormsg}">
	<div>出错啦：${errormsg}</div>
</pg:notnull>
