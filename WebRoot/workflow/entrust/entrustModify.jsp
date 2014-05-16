<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流程授权修改</title>
<%@ include file="/common/jsp/css.jsp"%>
<style type="text/css">
<!--
  body{
  	font-size:12px;
  	line-height:25px;
  }
  td{
  	font-size:12px;
  	line-height:25px;
  }
  th{
  	font-size:12px;
  	line-height:25px;
  }

-->
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
</head>
<script type="text/javascript">

var selectObj = "";

function selectEntrustUser(selectUser){
	
	selectObj = selectUser;
	
	$.dialog({
		  id : 'selectEntrustUser',
		  title : '被授权人选择',
		  width : 800,
		  height : 420,
		  content : 'url:' + "<%=request.getContextPath()%>/sysmanager/user/userquery_help_choose.jsp"
	  });
}

function chooseEntrustUser(entrust_user, entrust_user_show){
	
	if("entrustUser" == selectObj){
		
		$("#entrust_user_name").val(entrust_user_show);
		
		$("#entrust_user").val(entrust_user);
		
	}
	
	if("createUser" == selectObj){
		
		$("#create_user_name").val(entrust_user_show);
		
		$("#create_user").val(entrust_user);
		
	}
}

function selectEntrustProcdef(){
	
	if(porcData != null && porcData.length > 0){
		
		var chooseData = new Array();
		
		for(var i=0; i<porcData.length; i++){
			
			var data = porcData[i];
			
			if(tableData.indexOf(data.proc_id+"##")>=0){
				chooseData.push(data);
			}else{
				continue;
			}
		}
		porcData = chooseData;
	}
	
	$.dialog({
		  id : 'selectEntrustProcdef',
		  title : '授权流程选择',
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
		
		tableData = "";
		
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
				"\t\t<td>"+ data.wf_app_name +"</td>\n" + 
				"\t\t<td><a href='#' onclick='delProcRow(this)' >删除</a></td>\n" + 
				"</tr>";

			tableRow += row;
		}
		
		$("tr[name=procdef_row]").remove();
		
		$("#selectProcdefTable").append(tableRow);
		
		setSelectProcdefMsg();
	}
}

function delProcRow(delDoc){
	$(delDoc).parent().parent().remove();
	var removeProc = $(delDoc).parent().parent().find("input[name=procdef_id]").val()+"##";
	tableData = tableData.replace(removeProc,"");
	
	setSelectProcdefMsg();
}

function setSelectProcdefMsg(){
	
	if($("tr[name=procdef_row]") == null || $("tr[name=procdef_row]").length == 0){
		$("#entrust_type").val("全部委托");
		$("#selectProcdefMsg").show();
	}else{
		$("#entrust_type").val("选择流程委托");
		$("#selectProcdefMsg").hide();
	}
	
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
				<legend>流程授权信息</legend>
				<table border="0" cellpadding="0" cellspacing="0" class="table4" width="100%">
				<pg:beaninfo requestKey="wfEntrust" >
					<tr>
						<th>被授权人：</th>
						<td width="70%"><input id="entrust_user_name" name="entrust_user_name"
							type="text" value="<pg:cell colName="entrust_user_name" defaultValue="" />" style="width:175px;"
							class="w120 input_default easyui-validatebox" required="true"
							maxlength="100" readonly /><input type="hidden" name="entrust_user" id="entrust_user" value="<pg:cell colName="entrust_user" defaultValue="" />" />
							<font color="red">*</font><a href="javascript:void(0)" class="bt_1" id="changeButton"
							onclick="selectEntrustUser('entrustUser')"><span>被授权人选择</span></a></td>
					</tr>
					<tr>
						<th>授权人：</th>
						<td><input id="create_user_name" name="create_user_name" type="text" style="width:175px" readonly
							value="<pg:cell colName="create_user_name" defaultValue="" />" class="w120 input_default easyui-validatebox"
							required="true" maxlength="100" readonly /><font color="red">*</font>
							<%if(AccessControl.getAccessControl().isAdmin()){ %><a href="javascript:void(0)" class="bt_1" id="changeButton"
							onclick="selectEntrustUser('createUser')"><span>授权人选择</span></a><%} %>
							<input type="hidden" name="create_user" id="create_user" value="<pg:cell colName="create_user" defaultValue="" />" />
							<input type="hidden" name="id" id="id" value="<pg:cell colName="id" defaultValue="" />" /></td>
					</tr>
					<tr id="secret_tr">
						<th>授权开始时间：</th>
						<td id="secret_td" ><input id="start_date" name="start_date" type="text"
							value="<pg:cell colName="start_date" dateformat="yyyy-MM-dd" />" class="Wdate" required="true" readonly  style="width:175px;"
							maxlength="100" onclick="WdatePicker()" /></td>
					</tr>
					<tr id="re_secret_tr">
						<th>授权结束时间：</th>
						<td ><input id="end_date" name="end_date" type="text"
							value="<pg:cell colName="start_date" dateformat="yyyy-MM-dd" />" class="Wdate" required="true" readonly style="width:175px;"
							maxlength="100" onclick="WdatePicker()" /></td>
					</tr>
					</pg:beaninfo>
					<pg:beaninfo requestKey="entrustRelation" >
					<tr id="selectProcdefTR">
						<th vAlign="top">授权流程选择：</th>
						<td id="selectProcdefTD"><a href="javascript:void(0)" class="bt_1" id="changeButton"
							onclick="selectEntrustProcdef()"><span>流程选择</span></a><font color="red" id="selectProcdefMsg">未选择流程默认授权所有流程</font><br/>
						<table id="selectProcdefTable" border="0" cellpadding="0" width="80%" cellspacing="0" class="stable">
							<tr>
								<th>流程名称</th>
								<th>应用系统</th>
								<th>操作</th>
							</tr>
							<pg:equal colName="entrust_type" value="选择流程委托" >
							<pg:list requestKey="entrustProcRelationList">
								<tr name="procdef_row">
									<td><pg:cell colName="procdef_name" defaultValue="" /><input type='hidden' name='procdef_id' value="<pg:cell colName="procdef_id" defaultValue="" />" /></td>
									<td><pg:cell colName="wf_app_name" defaultValue="" /></td>
									<td><a href='#' onclick='delProcRow(this)' >删除</a></td>
								</tr>
								<script language="javascript">
									tableData += "<pg:cell colName="procdef_id" defaultValue="" />" + "##";
									var data = {"proc_id":"<pg:cell colName="procdef_id" defaultValue="" />",
											    "proc_name":"<pg:cell colName="procdef_name" defaultValue="" />", 
											    "business_name":"<pg:cell colName="business_name" defaultValue="" />", 
											    "wf_app_name":"<pg:cell colName="wf_app_name" defaultValue="" />"}; 
									porcData.push(data);
								</script>
							</pg:list>
							</pg:equal>
						</table>
						<input type="hidden" name="entrust_type" id="entrust_type" value="<pg:cell colName="entrust_type" defaultValue="" />" />
						</td>
					</tr>
					<script language="javascript">
					     if('<pg:cell colName="entrust_type" defaultValue="" />' != '全部委托'){
					    	 $("#selectProcdefMsg").hide();
					     }
					</script>
					<tr>
						<th>描述：</th>
						<td><textarea id="entrust_desc" name="entrust_desc" class="w120 input_default" rows="2" style="width:285px;"><pg:cell colName="entrust_desc" defaultValue="" /></textarea>
							<input type="hidden" name="sts" id="sts" value="有效" /></td>
					</tr>
					<tr>
						<td colspan='2'>
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tr>
							<td style="width:20%;text-align:right;" vAlign="top" ><font color="red">注意：</font></td>
							<td align="left"><font color="red">授权不支持多级授权。举例，张三授权给李四，同个时间段李四授权给王五，这时需要张三审批的流程是不能转交给王五审批，还是李四审批。</font>
							</tr>
							</table>
						</td>
					</tr>
				</pg:beaninfo>
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
</body>
<script language="javascript">
	var api = frameElement.api, W = api.opener;
	
	function dosubmit() {
		
		var start = $("#start_date").val();
		
		var end = $("#end_date").val();
		
		if((start != "" && end == "" ) || (start == "" && end != "") ){
			
			alert("请选择授权开始时间和授权结束时间");
			
			return;
		}
		if(start != "" && end != "" ){
			
			var startDate = new Date(Date.parse(start.replace(/-/g,"/")));
			
			var endDate = new Date(Date.parse(end.replace(/-/g,"/")));
			
			if(endDate < startDate){
				
				alert("授权开始时间不能大于授权结束时间");
				
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
		
		$.ajax({
			type : "POST",
			url : "validateSaveWfEntrust.page",
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
				
				if(responseText != null){
					
					if(responseText.validateResult == "success"){
						saveData();
					}else{
						unblockUI();
						$.dialog.alert('已经授权的流程不能再次授权给其他人');
					}
					
				}else{
					unblockUI();
					$.dialog.alert('相同的流程已经授权');
				}
				
			}
		});
	}
	
	function saveData(){
		$.ajax({
			type : "POST",
			url : "saveWfEntrust.page",
			data : formToJson("#addForm"),
			dataType : 'json',
			async : false,
			beforeSend : function(XMLHttpRequest) {
				
				XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
			success : function(responseText) {
				//去掉遮罩
				unblockUI();
				
				if (responseText == "success") {
					W.$.dialog.alert("修改成功", function() {
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
	
</script>