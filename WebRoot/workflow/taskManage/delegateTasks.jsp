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
				<td width="250px;" align="center" >
					转派人
					<input type="hidden" class="input1 w120" id="delegate_from_users_id" />
					<input type="text" class="input1 w120" id="delegate_from_users_name" />
					<a href="javascript:openChooseUsers('delegate_from')">选择</a>
				</td>
			</tr>
			<tr>
				<td width="250px;" align="center" >
					转派给
					<input type="hidden" class="input1 w120" id="delegate_to_users_id" />
					<input type="text" class="input1 w120" id="delegate_to_users_name" />
					<a href="javascript:openChooseUsers('delegate_to')">选择</a>
				</td>
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

//选择用户
function openChooseUsers(node_key){
	var url = "<%=request.getContextPath()%>/workflow/config/toChooseUserPage.page?&node_key="+node_key;
	$.dialog({ id:'nodeInfoIframe', title:'选择用户',width:1000,height:650, content:'url:'+url}); 
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
			data: {"processKey":"${param.processKey}","fromuser":fromuser,"touser":touser},
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
