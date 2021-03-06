<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新增业务类别</title>
<%@ include file="/common/jsp/css.jsp"%>
</head>
<script type="text/javascript">

function getSystemSecret(){
	$.ajax({
		type : "POST",
		url : "getSystemSecret.page",
		async : false,
		success : function(responseText) {
			$("#system_secret").val(responseText);
			$("#re_system_secret").val(responseText);
		}
	});
}

function picRefApp(){
	$.dialog({
		id : 'picRefApp',
		title : appName+'应用图片选择',
		width : 450,
		height : 320,
		content : 'url:' + "<%=request.getContextPath()%>/application/picRefApp.page?appInfoId="+appInfoId
	});
}

</script>
<body >
	<div class="form_box">
		<form id="addForm" name="addForm" method="post">
			<fieldset>
				<legend>应用基本信息</legend>
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
					<tr>
						<th>应用编号：</th>
						<td width=350px><input id="system_id" name="system_id"
							type="text" value="" style="width: 240px;"
							class="input_default easyui-validatebox" required="true"
							maxlength="100" /><font color="red">*</font></td>
					</tr>
					<tr>
						<th>应用名称：</th>
						<td width=350px><input id="system_name" name="system_name" type="text"
							value="" class="input_default easyui-validatebox" style="width: 240px;"
							required="true" maxlength="100" /><font color="red">*</font></td>
					</tr>
					<tr id="secret_tr">
						<th>应用口令：</th>
						<td id="secret_td" style="width:350px;"><input id="system_secret" name="system_secret" type="text"
							value="" class="input_default easyui-validatebox" style="width: 240px;"
							required="true" maxlength="100" /><font color="red">*</font><a href="javascript:void(0)" class="bt_1" id="changeButton"
							onclick="getSystemSecret()"><span id="system_seq_span">生成口令</span></a></td>
					</tr>
					<tr id="re_secret_tr">
						<th>重复口令：</th>
						<td style="width:350px;"><input id="re_system_secret" name="re_system_secret" type="text"
							value="" class="input_default easyui-validatebox" style="width: 240px;"
							required="true" maxlength="100" /><font id="re_secret_font" color="red">*</font></td>
					</tr>
					<tr>
						<th>待办来源：</th>
						<td><!-- <input id="pending_type" name="pending_type" type="text"
							value="" class="w120 input_default easyui-validatebox"
							required="true" maxlength="100" /> -->
							
							<select id="pending_type" name="pending_type"  maxlength="50" >
							<option value="1">GW</option>
							<option value="2">本地</option>
							<option value="3">其它库</option>
							</select>
							
							<font color="red">*</font></td>
					</tr>
					
					<tr>
						<th>启用待办：</th>
						<td><!-- <input id="used" name="used" type="text"
							value="" class="w120 input_default easyui-validatebox"
							required="true" maxlength="100" /> -->
							<select id="pending_used" name="pending_used"  maxlength="50" >
							<option value="0">关闭</option>
							<option value="1">启用</option>
							</select>
							
							<font color="red">*</font></td>
					</tr>
					<tr>
						<th>待办URL：</th>
						<td><input id="todo_url" name="todo_url" type="text"
							value="" class="input_default easyui-validatebox" style="width: 240px;"
							required="true" maxlength="200" /><font color="red">*</font></td>
					</tr>
					<tr>
						<th>应用URL：</th>
						<td><input id="app_url" name="app_url" type="text"
							value="" class="input_default easyui-validatebox" style="width: 240px;"
							required="true" maxlength="200" /><font color="red">*</font></td>
					</tr>
					<tr>
						<th>部署类型：</th>
						<td><select id='app_mode_type' name="app_mode_type" required="true"
									style="width: 120px;">
						    <option value="中央库应用">中央库应用</option>
						    <option value="独立库应用">独立库应用</option>
						    <option value="第三方应用">第三方应用</option>
							</select></td>
					</tr>
					<tr>
						<th>应用图片：</th>
						<td><input type="text" id="pic_name" name="pic_name" style="width: 240px;"
						value="<pg:cell colName="pic_name" />"/>
						<a href="javascript:void(0)" class="bt_small" id="refButton" onclick="javascript:picRefApp()"><span>选择</span></a>
						</td>
					</tr>
					<tr>
						<th>票据时间：</th>
						<td>
						<input type="text" name="tickettime" value="-2" style="width: 240px;"
						onkeyup="chkPrice(this);" onblur="chkLast(this)" onpaste="javascript: return false;"/>毫秒(-2为默认值，表示读取系统全局配置的时间，一般默认为2天) 
						</td>
					</tr>
					<tr>
						<th>票据签名：</th>
						<td><!-- <input id="pending_type" name="pending_type" type="text"
							value="" class="w120 input_default easyui-validatebox"
							required="true" maxlength="100" /> -->
							
							<select id="needsign" name="needsign"   >
							<option value="1">签名</option>
							<option value="0">不签名</option>
							 
							</select>
							
							<font color="red">*</font></td>
					</tr>
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
		
		if($("#system_secret_text").length >0 && $("#system_secret_text").val() == ""){
			
			alert("应用口令不为空");
			
			return;
		}
		
		if($("#re_secret_tr").length > 0){
			
			if($("#system_secret").val() == ""){
				
				alert("应用口令不为空");
				
				return;
			}
			
			if($("#system_secret").val() != $("#re_system_secret").val()){
				
				$("#re_secret_font").append("两次口令不一致");
				
				return;
			}else{
				$("#re_secret_font").html("*");	
			}
		}else{
			$("#re_secret_font").html("*");
		}
		
		$.ajax({
			type : "POST",
			url : "validateWfApp.page",
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
				
				if (responseText == "success") {
					saveData();
				} else {
					unblockUI();
					alert("应用编号和应用名称重复");
				}
			}
		});
	}
	
	function saveData(){
		$.ajax({
			type : "POST",
			url : "saveWfApp.page",
			data : formToJson("#addForm"),
			dataType : 'json',
			async : false,
			beforeSend : function(XMLHttpRequest) {
				var validated = $("#addForm").form('validate');
				if (validated) {
					XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				} else {
					return false;
				}
			},
			success : function(responseText) {
				//去掉遮罩
				unblockUI();
				
				if (responseText == "success") {
					W.$.dialog.alert("添加成功", function() {
						W.queryList();
						api.close();
					}, api);
				} else {
					W.$.dialog.alert(responseText, function() {
					}, api);
				}
			}
		});
	}
	
	function doreset() {
		$("#reset").click();
	}
	
	function chkPrice(obj){
		obj.value = obj.value.replace(/[^\d]/g,""); 
		//必须保证第一位为数字而不是. 
	} 

	function chkLast(obj){ 
		// 如果出现非法字符就截取掉 
		if(obj.value.substr((obj.value.length - 1), 1) == '.') {
			obj.value = obj.value.substr(0,(obj.value.length - 1)); 
		}
	}
	
</script>