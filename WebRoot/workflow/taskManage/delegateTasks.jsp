<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>转派任务</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
</head>
	
<body>
<div class="form">
	<form id="delegateFrom" name="delegateFrom" method="post" >
	
		<table border="0" cellpadding="0" cellspacing="0" class="table2" width="100%" >
			<tr >
				<th>转派人：</th>
				<td >
					<input type="hidden" class="input1 w120" id="delegate_from_users_id" />
					<input type="text" class="input1 w120" id="delegate_from_users_name" readonly/>
					<a href="javascript:openChooseUsers('delegate_from','all')">选择</a>
				</td>
				<td >&nbsp;</td>
			</tr>
			<tr>
				<th>转派给：</th>
				<td >
					<input type="hidden" class="input1 w120" id="delegate_to_users_id" />
					<input type="text" class="input1 w120" id="delegate_to_users_name" readonly/>
					<a href="javascript:openChooseUsers('delegate_to','1')">选择</a>
				</td>
				<td >&nbsp;</td>
			</tr>
			<tr>
				<th>流程提交人：</th>
				<td >
					<input type="hidden" class="input1 w120" id="submitUser_users_id" />
					<input type="text" class="input1 w120" id="submitUser_users_name" readonly/>
					<a href="javascript:openChooseUsers('submitUser','all')">选择</a>
				</td>
				<td >&nbsp;</td>
			</tr>
			<tr>
				<th vAlign="top">流程key：<input type="hidden" id="processKey" class="input1 w120" value="${param.processKey}" readonly /></th>
				<pg:notempty actual="${param.processKey}" evalbody="true">
					<pg:yes>
						<td>
							<table id="selectProcdefTable" border="0" cellpadding="0" width="80%" cellspacing="0" class="stable">
								<tr>
									<th>流程</th>
									<th>操作</th>
								</tr>
								<tr>
									<td>${param.processKey}</td>
									<td>删除</td>
								</tr>
							</table>
						</td>
						<td>&nbsp;</td>
					</pg:yes>
					<pg:no>
						<td>
							<table id="selectProcdefTable" border="0" cellpadding="0" width="80%" cellspacing="0" class="stable">
								<tr>
									<th>流程</th>
									<th>操作</th>
								</tr>
							</table>
						</td>
						<td ><a href="javascript:void(0)" onclick="javascript:toSelectProc()"><span>流程选择</span></a></td>
					</pg:no>
				</pg:notempty>
			</tr>
		</table>	
		
		<div class="btnarea" >
			<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="delegateTasks()"><span><pg:message code="sany.pdp.common.ok"/></span></a>
			<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.close"/></span></a>
			<input type="reset" id="reset" style="display: none;" />
		</div>
	</form>
</div>
</body>
<script language="javascript">
var api = frameElement.api, W = api.opener;

function delProcRow(delDoc){
	$(delDoc).parent().parent().remove();
	var removeProc = $(delDoc).parent().parent().find("input[name=pkey]").val()+",";
	var processKeys = $("#processKey").val();
	$("#processKey").val(processKeys.replace(removeProc,""));
} 

//选择用户
function openChooseUsers(node_key,alluser){
	if(alluser == 'all')
	{
		var url = "<%=request.getContextPath()%>/workflow/config/toChooseUserPage.page?alluser=true&node_key="+node_key ;
		$.dialog({ id:'nodeInfoIframe', title:'选择用户',width:1000,height:650, content:'url:'+url});
	}
	else
	{
		var url = "<%=request.getContextPath()%>/workflow/config/toChooseUserPage.page?alluser=false&node_key="+node_key ;
		$.dialog({ id:'nodeInfoIframe', title:'选择用户',width:1000,height:650, content:'url:'+url});
	}
}

//选择流程
function toSelectProc() {
	
	var url="<%=request.getContextPath()%>/workflow/taskManage/selectProcess.jsp";
	
	$.dialog({ id:'iframeNewId1', title:'选择流程',width:300,height:400, content:'url:'+url});  
	
}

//转办
function delegateTasks(){
	var fromuser = $.trim($("#delegate_from_users_id").val());
	var touser = $.trim($("#delegate_to_users_id").val());
	if (fromuser == '' || touser == ''){
		$.dialog.alert("转派关系不能为空！",function(){});
		return;
	} 
	
	if (fromuser == touser) {
		$.dialog.alert("转派人与被转派人不能是同一个人！",function(){});
		return;
	}
	
	$.dialog.confirm('确定将['+$("#delegate_from_users_name").val()+']的任务转派给['+$("#delegate_to_users_name").val()+'] ？', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/taskManage/delegateTasks.page",
			data: {"processKeys":$("#processKey").val(),"fromuser":fromuser,"touser":touser,"startUser":$("#submitUser_users_id").val()},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data != 'success') {
					$.dialog.alert("转派任务出错:",function(){});
				}else {
					W.modifyQueryData();
					api.close();	
				}
			}
		 });
	},function(){});   
}

$(document).ready(function() {
	
});
	
</script>
</html>
