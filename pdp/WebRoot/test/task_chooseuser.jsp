<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>物料领用申请</title>
		<%@ include file="/common/jsp/css.jsp"%>
	</head>
	<body>
<div class="form_box">
	<form name="applyForm" method="post">
	<input type="hidden" value="${taskId}" name="taskId" id="taskId"/>
	<input type="hidden" value="" name="username" id="username"/>
	<fieldset>
		<legend>选择用户</legend>
		<table border="0" cellpadding="0" cellspacing="0" class="table4">
				<tr>
					<th width=85px>转派用户：</th>
					<td width=200px>
						<select name="username" onchange="setUser()" >
							<option value="applyuser">applyuser</option>
							<option value="centeruser1">centeruser1</option>
							<option value="centeruser2">centeruser2</option>
							<option value="deptuser">deptuser</option>
							<option value="craftuser">craftuser</option>
							<option value="storageuser">storageuser</option>
						</select>
					</td>
				</tr>
		</table>
		<div class="btnarea">
			<a href="javascript:void(0)" class="bt_1" id="addButton"
				onclick="delegateTask()"><span>提交</span></a> <a
		</div>
	</fieldset>
	</form>
</div>
<script language="javascript">
var api = frameElement.api, w = api.opener;
function setUser(){
	$('#username').val($("select option:selected").val());
}
function delegateTask(){
	var taskId = $('#taskId').val();
	var userId = $('#username').val();
	$.post('delegateTask.page',
			{taskId:taskId,user:userId},
			function (data){
				if(data=='success'){
					//去掉遮罩
					unblockUI();
					w.$.dialog.alert("转派成功",function(){
						api.close();
						w.toUserList();
						},api);
				}
				})
}
</script>
</body>