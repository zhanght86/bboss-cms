<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
描述：删除流程实例原因
作者：谭湘
版本：1.0
日期：2014-05-09
 --%>	
 <%
  request.setAttribute("ids", request.getParameter("ids"));
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>删除流程实例信息</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">


</script>
</head>
	
<body>
<div class="form">
	<form id="delProcessInstFrom" name="delProcessInstFrom" method="post" >
		
		<table border="0" cellpadding="0" cellspacing="0" class="table2" width="100%" >
			<tr >
				<th width="100px" rowspan="2">删除方式：</th>
				<td width="220px">
					<input type="radio" name="deleteType" value="1" checked onclick="showDiv()">逻辑删除(结束流程)</input>
				</td>
			</tr>
			<tr>
				<td width="220px">
					<input type="radio" name="deleteType" value="2" onclick="showDiv()">物理删除(先结束流程，再删除流程)</input>
				</td>
			</tr>
			
			<tr id="showDiv" style="display:none;">
				<th width="100px">删除原因：</th>
				<td width="220px">
					<textarea rows="8" cols="50" id="deleteReason" name="deleteReason"  
						class="w120" style="width: 280px;font-size: 12px;height:40px;" maxlength="200"></textarea>
				</td>
			</tr>
		</table>			
		
		<div class="btnarea" >
			<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="dosubmit()"><span><pg:message code="sany.pdp.common.ok"/></span></a>
			<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.close"/></span></a>
			<input type="reset" id="reset" style="display: none;" />
		</div>
	</form>
</div>
</body>
<script language="javascript">
var api = frameElement.api, W = api.opener;
function dosubmit(){
	var delType = $("input[name=deleteType]:checked").val();
	var deleteReason = $("#deleteReason").val();
	
	//逻辑删除，要判断是否已经删除过
	if ( delType == '1') { 
		var isDelete = "";//流程完成，删除才有备注，用来判断是否能删除
		W.$("#tb tr:gt(0)").each(function() {
			if (W.$(this).find("#CK").get(0).checked == true) {
				if (W.$(this).find("#END_TIME").val() != '') {
					isDelete = "false";
					return;
				}
		     }
		});
		    
		if (isDelete == "false") {
		    $.dialog.alert('已完成的流程不能删除');
		    return;
		}
		
		if(deleteReason ==''){
			alert("请填写删除原因");
			return;
		}
	}
	 
	$.dialog.confirm('确定要删除记录吗？删除后将不可恢复', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/repository/delPorcessInstance.page",
			data :{"deleteReason":deleteReason,"processInstIds":'${ids}',"delType":delType},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
			success : function(data){
				if(data=="success"){
					api.close();
		 			W.queryList();
				}else{
					$.dialog.alert("流程实例删除出错"+data,function(){},api);
				}
			}	
		 });
	},function(){});    
}

// 删除原因div显示控制
function showDiv(){
	
	if ($("input[name=deleteType]:checked").val() == '1'){
		$("#showDiv").show();
	}else {
		$("#deleteReason").val('');
		$("#showDiv").hide();
	}
}

$(document).ready(function() {
	
	showDiv();
});
	
</script>
</html>
