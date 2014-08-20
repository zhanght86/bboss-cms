<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>查看流程效率分析</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
	
<body>
<div class="">
<pg:empty actual="${ProcessCounter}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${ProcessCounter}" >
	<div class="mcontent">
		<pg:beaninfo requestKey="ProcessCounter">
			<form id="viewForm" name="viewForm">
				<table border="1" cellpadding="0" cellspacing="0" class="tb2">
					<tr height="25px">
						<th width="100"><strong>办结条数：</strong></th>
						<td width="100"><pg:cell colName="passNum"/></td>
						<td>注：正常完成流程数</td>
					</tr>
					<tr height="25px">
						<th width="100"><strong>平均办结工时：</strong></th>
						<td width="100">
							<pg:notempty colName="avgWorkTime"><pg:cell colName="avgWorkTime"/>小时</pg:notempty>
							<pg:empty colName="avgWorkTime">&nbsp;</pg:empty>
						</td>
						<td>注：完成流程时间之和/完成流程数</td>
					</tr>
					<tr height="25px">
						<th width="100"><strong>平均办理人次：</strong></th>
						<td width="100"><pg:cell colName="avgPassNum"/></td>
						<td>注：完成流程的处理人数之和/完成流程数</td>
					</tr>
					<tr height="25px">
						<th width="100"><strong>平均转办次数：</strong></th>
						<td width="100"><pg:cell colName="avgDelegateNum"/></td>
						<td>注：完成流程中的转办人次之和/完成流程数</td>
					</tr>
					<tr height="25px">
						<th width="100"><strong>平均驳回次数：</strong></th>
						<td width="100"><pg:cell colName="avgRejectNum"/></td>
						<td>注：完成流程中的驳回次数之和/完成流程数</td>
					</tr>
					<tr height="25px">
						<th width="100"><strong>平均撤销次数：</strong></th>
						<td width="100"><pg:cell colName="avgCancelNum"/></td>
						<td>注：完成流程中的撤销次数之和/完成流程数</td>
					</tr>
					<tr height="25px">
						<th width="100"><strong>平均废弃次数：</strong></th>
						<td width="100"><pg:cell colName="avgDiscardNum"/></td>
						<td>注：完成流程中的废弃次数之和/完成流程数</td>
					</tr>
				</table>
			</form>
		</pg:beaninfo>
  	</div>
  	
	<div class="btnarea" >
		<a href="javascript:void(0)" class="bt_2" id="closeButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
	</div>
</pg:notempty>	
</div>
</body>
<script type="text/javascript">
var api = frameElement.api, W = api.opener;
$(document).ready(function() {
});
</script>
</head>
</html>
