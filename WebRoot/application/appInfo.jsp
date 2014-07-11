<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>变更应用</title>
<%@ include file="/common/jsp/css.jsp"%>
</head>
<script type="text/javascript">

</script>
<body>
	<div class="form_box">
		<form id="addForm" name="addForm" method="post">
			<fieldset>
				<legend>应用基本信息</legend>
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
				    <pg:beaninfo requestKey="wfApp" >
					<tr>
						<th>应用编号：</th>
						<td width=140px><pg:cell colName="system_id" defaultValue="" /></td>
					</tr>
					<tr>
						<th>应用名称：</th>
						<td><pg:cell colName="system_name" defaultValue="" /></td>
					</tr>
					<tr>
						<th>应用口令：</th>
						<td><pg:cell colName="system_secret_text" defaultValue="" /></td>
					</tr>
					<tr>
						<th>待办类型：</th>
						<td><%-- <pg:cell colName="pending_type" defaultValue="" /> --%>
						<pg:equal colName="pending_type" value="1">GW</pg:equal>
						<pg:equal colName="pending_type"  value="2">本地</pg:equal>
						<pg:equal colName="pending_type" value="3">其它库</pg:equal>
						</td>
					</tr>
					<tr>
						<th>启用待办：</th>
						<td><%-- <pg:cell colName="used" defaultValue="" /> --%>
						<pg:equal colName="pending_used"  value="0">关闭</pg:equal>
				        <pg:equal colName="pending_used" value="1">开启</pg:equal>
						</td>
					</tr>
					<tr>
						<th>待办URL：</th>
						<td><pg:cell colName="todo_url" defaultValue="" /></td>
					</tr>
					<tr>
						<th>应用URL：</th>
						<td><pg:cell colName="app_url" defaultValue="" /></td>
					</tr>
					<tr>
						<th>应用类型：</th>
						<td><pg:cell colName="app_mode_type" defaultValue="" /></td>
					</tr>
					</pg:beaninfo>
				</table>
			</fieldset>
			<div class="btnarea">
				<a
					href="javascript:void(0)" class="bt_2" id="resetButton"
					onclick="closeDlg()"><span>退出</span></a> <input type="reset"
					id="reset" style="display: none;" />
			</div>
		</form>
	</div>
</body>
<script language="javascript">
	
	function doreset() {
		$("#reset").click();
	}
	
</script>