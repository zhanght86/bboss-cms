<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>查看个人效率分析</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
	
<body>
<div class="">
<pg:empty actual="${PersonalCounter}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${PersonalCounter}" >
	<div class="mcontent">
		<pg:beaninfo requestKey="PersonalCounter">
			<form id="viewForm" name="viewForm">
				<table border="1" cellpadding="0" cellspacing="0" class="tb2">
					<tr height="25px">
						<th width="100"><strong>启动效率：</strong></th>
						<td width="100">
							<pg:notempty colName="startEff"><pg:cell colName="startEff"/>小时</pg:notempty>
							<pg:empty colName="startEff">&nbsp;</pg:empty>
						</td>
						<td>注：统计日期内用户平均多少小时开启流程</td>
					</tr>
					<tr height="25px">
						<th width="100"><strong>处理效率：</strong></th>
						<td width="100">
							<pg:notempty colName="dealEff"><pg:cell colName="dealEff"/>小时</pg:notempty>
							<pg:empty colName="dealEff">&nbsp;</pg:empty>
						</td>
						<td>注：统计日期内用户平均多少小时处理任务</td>
					</tr>
					<tr height="25px">
						<th width="100"><strong>转办效率：</strong></th>
						<td width="100">
							<pg:notempty colName="delegateEff"><pg:cell colName="delegateEff"/>小时</pg:notempty>
							<pg:empty colName="delegateEff">&nbsp;</pg:empty>
						</td>
						<td>注：统计日期内用户平均多少小时转办任务</td>
					</tr>
					<tr height="25px">
						<th width="100"><strong>驳回效率：</strong></th>
						<td width="100">
							<pg:notempty colName="rejectEff"><pg:cell colName="rejectEff"/>小时</pg:notempty>
							<pg:empty colName="rejectEff">&nbsp;</pg:empty>
						</td>
						<td>注：统计日期内用户平均多少小时驳回任务</td>
					</tr>
					<tr height="25px">
						<th width="100"><strong>撤销效率：</strong></th>
						<td width="100">
							<pg:notempty colName="cancelEff"><pg:cell colName="cancelEff"/>小时</pg:notempty>
							<pg:empty colName="cancelEff">&nbsp;</pg:empty>
						</td>
						<td>注：统计日期内用户平均多少小时撤销任务</td>
					</tr>
					<tr height="25px">
						<th width="100"><strong>废弃效率：</strong></th>
						<td width="100">
							<pg:notempty colName="discardEff"><pg:cell colName="discardEff"/>小时</pg:notempty>
							<pg:empty colName="discardEff">&nbsp;</pg:empty>
						</td>
						<td>注：统计日期内用户平均多少小时废弃任务</td>
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
