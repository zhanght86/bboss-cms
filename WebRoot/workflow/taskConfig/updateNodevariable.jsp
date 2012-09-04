<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>参数配置</title>
<%@ include file="/common/jsp/css.jsp"%>
</head>
<body>
	<div class="form">
			<form id="addNodevariableFrom" name="addNodevariableFrom" method="post">
				<input type="hidden" value="${nodevariable.business_id }" name="business_id" id="business_id"/>
				<input type="hidden" value="${nodevariable.business_type }" name="business_type" id="business_type"/>
				<input type="hidden" value="${nodevariable.id}" name="id" id="id"/>
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
					<tr>
						<th width=85px >
							参数名称：
						</th>
						<td width="140px">
							<input id="param_name" name="param_name" type="text" value="${nodevariable.param_name}"
								class="w120 input_default easyui-validatebox" required="true" disabled
								maxlength="10" /><font color="red">*</font>
						</td>
						<th width="85px" >
							参数值：
						</th>
						<td width="140px" >
							<input id="param_value" name="param_value" type="text" value="${nodevariable.param_value}"
								class="w120 input_default easyui-validatebox" required="true"
								maxlength="10" /><font color="red">*</font>
						</td>
						<th width="85px" >
							所属节点：
						</th>
						<td width="140px" >
							<input id="node_name" name="node_name" type="text" value="${nodevariable.node_name}"
								class="w120 input_default" required="true" disabled
								maxlength="10" />
						</td>
						<!-- <th width="85px" >
							参数类型：
						</th>
						<td width="140px" >
							<select id="param_type" name="param_type">
								<option value="String">String</option>
								<option value="long">Long</option>
								<option value="double">Double</option>
								<option value="boolean">Boolean</option>
							</select>
						</td> -->
					</tr>
					<tr>
					<th>
							参数描述：
						</th>
						<td width="140px" >
							<textarea id="param_des" name="param_des"  >${nodevariable.param_des}</textarea>
						</td>
					</tr>
				</table>			
			<div class="btnarea" >
				<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="dosubmit()"><span>增加</span></a>
				<!-- <a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span>重置</span></a> -->
				<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span>退出</span></a>
				<input type="reset" id="reset" style="display: none;" />
			</div>
			</form>
		</div>
</body>
<script language="javascript">
var api = frameElement.api, W = api.opener;
function dosubmit(){
	var param_type_el = document.getElementsByName("param_type");
	$.post('addNodevariable.page', {
		id : addNodevariableFrom.id.value,
		business_id : addNodevariableFrom.business_id.value,
		business_type : addNodevariableFrom.business_type.value,
		param_name : addNodevariableFrom.param_name.value,
		param_value : addNodevariableFrom.param_value.value,
		node_id:$("#node_id option:selected").val(),
		param_des:addNodevariableFrom.param_des.value
		
	}, function(data) {
		if (data == 'success') {
			//去掉遮罩
			unblockUI();
			W.$.dialog.alert("保存成功",function(){
				api.close();
				W.loadNodevariableData();
			},api);
		}
		else if(data == 'exist'){
			W.$.dialog.alert("已存在此参数配置",function(){
			},api);
		}
		else if(data == 'error'){
			W.$.dialog.alert("系统异常",function(){
			},api);
		}
	});
}
</script>
</html>