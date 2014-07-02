<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>修改业务类别信息</title>
<%@ include file="/common/jsp/css.jsp"%>
</head>
<body>
	<pg:beaninfo requestKey="businessType">
	<div class="form_box">
		<form id="addForm" name="addForm" method="post">
		<input type="hidden" value="<pg:cell colName='businessId'/>" id="businessId" name="businessId"/>
		<input type="hidden" value="<pg:cell colName='parentId'/>" id="parentId" name="parentId"/>
			<fieldset>
				<legend>业务类别基本信息</legend>
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
					<tr>
						<th>业务类别码：</th>
						<td width=140px><input id="businessCode" name="businessCode"
							type="text" value="<pg:cell colName="businessCode"/>"
							class="w120 input_default easyui-validatebox" required="true"
							maxlength="100" /><font color="red">*</font></td>
					</tr>
					<tr>
						<th>业务类别名：</th>
						<td><input id="businessName" name="businessName" type="text"
							value="<pg:cell colName="businessName"/>" class="w120 input_default easyui-validatebox"
							required="true" maxlength="100" /><font color="red">*</font></td>
					</tr>
					<tr>
						<tr>
						<th>父节点：</th>
						<td><select class="easyui-combotree" id='parentBusinessType' required="true"
									style="width: 120px;"><font color="red">*</font></td>
					</tr>
					</tr>
					<tr>
						<th>状态</th>
						<td>
							<select id="useFlag" name="useFlag"  class="select1" style="width: 125px;">
								<option value="0" <pg:equal colName="useFlag" value="0">selected</pg:equal>>失效</option>
								<option value="1" <pg:equal colName="useFlag" value="1">selected</pg:equal>>启用</option> 						
							</select>
						</td>
					</tr>
					<tr>
						<th>备注：</th>
						<td><input id="remark" name="remark" type="text" value="<pg:cell colName="remark"/>"
							class="w120" maxlength="100" /></td>
					</tr>
				</table>
			</fieldset>
			<div class="btnarea">
				<a href="javascript:void(0)" class="bt_1" id="addButton"
					onclick="dosubmit()"><span>修改</span></a> <a
					href="javascript:void(0)" class="bt_2" id="resetButton"
					onclick="doreset()"><span>重置</span></a> <a
					href="javascript:void(0)" class="bt_2" id="resetButton"
					onclick="closeDlg()"><span>退出</span></a> <input type="reset"
					id="reset" style="display: none;" />
			</div>
		</form>
	</div>
	</pg:beaninfo>
</body>
<script language="javascript">
	$(document).ready(function () {
		$("#businessIdShow").attr("disabled", true);
		$("#parentBusinessType").combotree({
			url:"showComboxBusinessTree.page"
			});
		$("#parentBusinessType").combotree('setValue',$('#parentId').val());
	});
	
	var api = frameElement.api, W = api.opener;
	
	function dosubmit() {
		$("#parentId").val($("#parentBusinessType").combotree('getValue'));
		if($("#parentId").val()==$("#businessId").val()){
			alert("不能选择自己作为父节点");
			return;
		}
		$.ajax({
			type : "POST",
			url : "update.page",
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
					alert("修改业务类别失败："+responseText);
				}
			}
		});
	}
	function doreset() {
		$("#reset").click();
	}
</script>