<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新增委托待办</title>
<%@ include file="/common/jsp/css.jsp"%>
<style type="text/css">
<!--
  body{
  	font-size:14px;
  	line-height:25px;
  }
  td{
  	font-size:14px;
  	line-height:25px;
  }
  th{
  	font-size:14px;
  	line-height:25px;
  }

-->
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
</head>
<script type="text/javascript">
function selectEntrustUser(){
	
	$.dialog({
		  id : 'selectEntrustUser',
		  title : '委托人选择',
		  width : 800,
		  height : 420,
		  content : 'url:' + "<%=request.getContextPath()%>/sysmanager/user/userquery_help_choose.jsp"
	  });
}

function chooseEntrustUser(entrust_user, entrust_user_show){
	
	$("#entrust_user_show").val(entrust_user_show);
	
	$("#entrust_user").val(entrust_user);
}

function selectEntrustProcdef(){
	$.dialog({
		  id : 'selectEntrustProcdef',
		  title : '委托流程选择',
		  width : 950,
		  height : 620,
		  content : 'url:' + "<%=request.getContextPath()%>/workflow/repository/workflowmanager_help_choose.jsp"
	  });
}

var tableData = "";

var porcData = new Array();

function chooseSelectedData(chooseData){
	
	if(chooseData != null){
		
		porcData = chooseData;
		
		var tableRow = "";
		
		for(var i=0; i<chooseData.length; i++){
			
			var data = chooseData[i];
			
			if(tableData.indexOf(data.proc_id+"##")>=0){
				continue;
			}else{
				tableData += data.proc_id+"##";
			}
			
			var row = 
				"<tr name='procdef_row'>\n" +
				"\t\t<td>"+ data.proc_name +"<input type='hidden' name='procdef_id' value='"+ data.proc_id +"' /></td>\n" + 
				"\t\t<td>"+ data.business_name +"</td>\n" + 
				"\t\t<td>"+ data.wf_app_name +"</td>\n" + 
				"\t\t<td><a href='#' onclick='delProcRow(this)' >删除</a></td>\n" + 
				"</tr>";

			tableRow += row;
		}
		
		$("tr[name=procdef_row]").remove();
		
		$("#selectProcdefTable").append(tableRow);
	}
}

function delProcRow(delDoc){
	$(delDoc).parent().parent().remove();
	var removeProc = $(delDoc).parent().parent().find("input[name=procdef_id]").val()+"##";
	tableData = tableData.replace(removeProc,"");
}

function selectProcdef(){
	if($("#entrust_type").val() == "选择流程委托"){
		$("#selectProcdefTR").show();
	}else{
		$("tr[name=procdef_row]").remove();
		$("#selectProcdefTR").hide();
	}
}

</script>
<body >
	<div class="form_box">
		<form id="addForm" name="addForm" method="post">
			<fieldset>
				<legend>委托待办信息</legend>
				<table border="0" cellpadding="0" cellspacing="0" class="table4" width="100%">
					<tr>
						<th>委托人：</th>
						<td width="70%"><input id="entrust_user_show" name="entrust_user_show"
							type="text" value="" style="width:175px;"
							class="w120 input_default easyui-validatebox" required="true"
							maxlength="100" /><input type="hidden" name="entrust_user" id="entrust_user" />
							<font color="red">*</font><a href="javascript:void(0)" class="bt_1" id="changeButton"
							onclick="selectEntrustUser()"><span>委托人选择</span></a></td>
					</tr>
					<tr>
						<th>归属人：</th>
						<td><input id="create_user_show" name="create_user_show" type="text" style="width:175px"
							value="<%=AccessControl.getAccessControl().getUserName() %>" class="w120 input_default easyui-validatebox"
							required="true" maxlength="100" /><font color="red">*</font>
							<input type="hidden" name="create_user" id="create_user" value="<%=AccessControl.getAccessControl().getUserAccount() %>" /></td>
					</tr>
					<tr id="secret_tr">
						<th>委托开始时间：</th>
						<td id="secret_td" ><input id="start_date" name="start_date" type="text"
							value="" class="Wdate"" readonly  style="width:175px;"
							maxlength="100" onclick="WdatePicker()" /></td>
					</tr>
					<tr id="re_secret_tr">
						<th>委托结束时间：</th>
						<td ><input id="end_date" name="end_date" type="text"
							value="" class="Wdate"" readonly style="width:175px;"
							maxlength="100" onclick="WdatePicker()" /></td>
					</tr>
					<tr>
						<th>委托类型：</th>
						<td><select id='entrust_type' name="entrust_type" required="true"
									style="width:175px;" onchange="selectProcdef();">
							<option value="选择流程委托">选择流程委托</option>
						    <option value="全部委托">全部委托</option>
							</select></td>
					</tr>
					<tr id="selectProcdefTR">
						<th vAlign="top">委托流程选择：</th>
						<td id="selectProcdefTD"><a href="javascript:void(0)" class="bt_1" id="changeButton"
							onclick="selectEntrustProcdef()"><span>流程选择</span></a><br/>
							<table id="selectProcdefTable" border="0" cellpadding="0" width="80%" cellspacing="0" class="table3">
							<tr>
								<td>流程</td>
								<td>应用</td>
								<td>业务类别</td>
								<td>操作</td>
							</tr>
						</table>
							</td>
					</tr>
					<tr>
						<th>描述：</th>
						<td><input id="entrust_desc" name="entrust_desc" type="text"
							value="" class="w120 input_default" style="width:285px;"
							maxlength="200" />
							<input type="hidden" name="sts" id="sts" value="有效" /></td>
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
		
		var start = $("#start_date").val();
		
		var end = $("#end_date").val();
		
		if((start != "" && end == "" ) || (start == "" && end != "") ){
			
			alert("请选择委托开始时间和委托结束时间");
			
			return;
		}
		if(start != "" && end != "" ){
			
			var startDate = new Date(Date.parse(start.replace(/-/g,"/")));
			
			var endDate = new Date(Date.parse(end.replace(/-/g,"/")));
			
			if(endDate < startDate){
				
				alert("委托开始时间不能大于委托结束时间");
				
				return;
			}
		}
		
		var validated = $("#addForm").form('validate');
		if(!validated){
			return;
		}
		
		if($("#entrust_type").val() == "选择流程委托"){
			if($("tr[name=procdef_row]").length <= 0){
				$.dialog.alert('请先选择流程');
				return;
			}
		}
		
		saveData();
	}
	
	function saveData(){
		$.ajax({
			type : "POST",
			url : "saveWfEntrust.page",
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
					w.$.dialog.alert(responseText, function() {
					}, api);
				}
			}
		});
	}
	
	function doreset() {
		
		$("#reset").click();
	}
	
</script>