<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新增业务类别</title>
<%@ include file="/common/jsp/css.jsp"%>
</head>
<script type="text/javascript">
			$(document).ready(function() {	
				$("#parentBusinessType").combotree({
					url:"showComboxBusinessTree.page"
					});
			});
</script>
<body>
	<div class="form_box">
		<form id="addForm" name="addForm" method="post">
			<fieldset>
				<legend>业务类别基本信息</legend>
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
					<tr>
						<th>业务类别码：</th>
						<td width=140px><input id="businessCode" name="businessCode"
							type="text" value=""
							class="w120 input_default easyui-validatebox" required="true"
							maxlength="100" /><font color="red">*</font></td>
					</tr>
					<tr>
						<th>业务类别名：</th>
						<td><input id="businessName" name="businessName" type="text"
							value="" class="w120 input_default easyui-validatebox"
							required="true" maxlength="100" /><font color="red">*</font></td>
					</tr>
					<tr>
						<th>父节点：</th>
						<td><select class="easyui-combotree" id='parentBusinessType' name="parentId" required="false"
									style="width: 120px;"></td>
					</tr>
					<tr>
						<th>备注：</th>
						<td><input id="remark" name="remark" type="text" value=""
							class="w120" maxlength="100" /></td>
					</tr>
					<input id="useFlag" name="useFlag" type="hidden" value="1"/>
				</table>
			</fieldset>
			<div class="btnarea">
				<a href="javascript:void(0)" class="bt_1" id="addButton"
					onclick="dosubmit()"><span>增加</span></a> <a
					href="javascript:void(0)" class="bt_2" id="resetButton"
					onclick="doreset()"><span>重置</span></a> <a
					href="javascript:void(0)" class="bt_2" id="resetButton"
					onclick="closeDlg()"><span>退出</span></a> <input type="reset"
					id="reset" style="display: none;" />
			</div>
		</form>
	</div>
</body>
<script language="javascript">
	var api = frameElement.api, W = api.opener;
	
	function dosubmit() {
		$.ajax({
			type : "POST",
			url : "addType.page",
			data : formToJson("#addForm"),
			dataType : 'json',
			async : false,
			beforeSend : function(XMLHttpRequest) {
				var validated = $("#addForm").form('validate');
				if (validated) {
					blockUI();
					XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				} else {
					return false;
				}
			},
			success : function(responseText) {
				//去掉遮罩
				unblockUI();
				
				if (responseText == "success") {
					W.queryList();
					api.close();
				} else {
					$.dialog.alert("新增业务类别失败："+responseText,function(){});
				}
			}
		});
	}
	function doreset() {
		$("#reset").click();
	}
</script>