<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>修改节点信息(扩展表)</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">


</script>
</head>
	
<body>
<div class="form">
	<form id="updateNodeFrom" name="updateNodeFrom" method="post" >
		<pg:beaninfo requestKey="nodeInfo">
			<table border="0" cellpadding="0" cellspacing="0" class="table2" width="100%" >
				<input type="hidden" id="process_key" name="process_key" value="<pg:cell colName="process_key"/>"/>
				<input type="hidden" id="node_key" name="node_key" value="<pg:cell colName="node_key"/>"/>
				<input type="hidden" id="node_type" name="node_type" value="<pg:cell colName="node_type"/>"/>
				<input type="hidden" id="node_name" name="node_name" value="<pg:cell colName="node_name"/>"/>
				
				<tr >
					<th width="100px" >节点KEY：</th>
					<td width="220px"><pg:cell colName="node_key"/></td>
				</tr>
				<tr >
					<th width="100px" >节点类型：</th>
					<td width="220px"><pg:cell colName="node_type"/></td>
				</tr>
				<tr >
					<th width="100px" >节点名称：</th>
					<td width="220px"><pg:cell colName="node_name"/></td>
				</tr>
				<tr >
					<th width="100px" >节点描述：</th>
					<td width="220px"><textarea rows="8" cols="50" id="node_describe" name="node_describe"  
							class="w120" style="width: 280px;font-size: 12px;height:40px;" maxlength="200"><pg:cell colName="node_describe"/></textarea></td>
				</tr>
			</table>			
		</pg:beaninfo>
		
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
	 
	$.dialog.confirm('确定要修改信息吗？', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/config/updateNodeInfo.page",
			data: formToJson("#updateNodeFrom"),
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
			success : function(data){
				if(data=="success"){
					api.close();
		 			W.$("#describe"+$("#node_key").val()).text($("#node_describe").val());
				}else{
					$.dialog.alert("修改流程信息出错"+data,function(){},api);
				}
			}	
		 });
	},function(){});    
}

</script>
</html>
