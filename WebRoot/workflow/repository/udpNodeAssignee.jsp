<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>修改节点处理人</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script language="javascript">
var api = frameElement.api, W = api.opener;

//选择用户
function openChooseUsers(node_key){
	//alert(node_key);
	var url = encodeURI("<%=request.getContextPath()%>/workflow/config/toChooseUserPage.page?"
			+"process_key=${processKey}&users="+$('#'+node_key+'_users_id').val()
			+"&user_realnames="+$('#'+node_key+'_users_name').val()
			+"&org_id="+$('#'+node_key+'_org_id').val()
			+"&org_name="+$('#'+node_key+'_org_name').val()
			+"&all_names="+$('#'+node_key+'_all_names').val()
			+"&node_key="+node_key);
	$.dialog({ id:'nodeInfoIframe', title:'选择用户',width:1000,height:650, content:'url:'+url}); 
}

//清空选择
function emptyChoose(id,type){
	if (type=='1') {//清空用户或组
		$("#"+id+"_users_id").val('');
		$("#"+id+"_users_name").val('');
		$("#"+id+"_org_id").val('');
		$("#"+id+"_org_name").val('');
		$("#"+id+"_all_names").val('');
	}else {//清空组
		$("#"+id+"_groups_id").val('');
		$("#"+id+"_groups_name").val('');
	}
}

function dosubmit(){
	
	$.dialog.confirm('确定提交数据？', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/taskManage/udpNodeAssignee.page",
			data: formToJson("#updNodeFrom"),		
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
			success : function(data){
				if(data=="success"){
					api.close();
				}else{
					$.dialog.alert("修改后续节点处理人出错："+data,function(){},api);
				}
			}	
		 });
	},function(){});    
}

$(document).ready(function() {
	
});
	
</script>
</head>
	
<body>
<div class="form">
	<form id="updNodeFrom" name="updNodeFrom" method="post" >
		<input type="hidden" name="processId" value="${processId}"/>
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
			<pg:header>
				<th>节点KEY</th>
				<th>节点名称</th>
				<th>待办人</th>
			</pg:header>
				
			<pg:list requestKey="nodeList">
				<pg:equal colName="node_type" value="userTask">
					<tr>
					
						<pg:notin scope="${filterNode}" colName="node_key">
							<input type="hidden" id="<pg:cell colName='node_key'/>" 
								name="node_key" value="<pg:cell colName='node_key'></pg:cell>" />
							<input type="hidden" name="is_copy" value="<pg:cell colName='is_copy'/>" />
							
							<input type="hidden" id="<pg:cell colName='node_key'/>_users_id" 
								name="node_users_id" value="<pg:cell colName='node_users_id'/>" />
								
							<input type="hidden" id="<pg:cell colName='node_key'/>_users_name" 
								name="node_users_name" value="<pg:cell colName='node_users_name'/>" />
								
							<input type="hidden" id="<pg:cell colName='node_key'/>_org_id" 
								name="node_orgs_id" value="<pg:cell colName='node_orgs_id'/>" />
								
							<input type="hidden" id="<pg:cell colName='node_key'/>_org_name" 
								name="node_orgs_name"value="<pg:cell colName='node_orgs_name'/>" />	
						</pg:notin>
							
						<td><pg:cell colName="node_key"></pg:cell></td>
						<td><pg:cell colName="node_name"></pg:cell></td>
						<td <pg:in scope="${filterNode}" colName="node_key">disabled</pg:in>>
							<pg:empty colName="node_orgs_name">
								<pg:notempty colName="node_users_name">
									<textarea rows="8" cols="50" id="<pg:cell colName='node_key'/>_all_names"  
									style="width: 600px;" readonly 
									><pg:cell colName='node_users_name'/></textarea>
								</pg:notempty>
								<pg:empty colName="node_users_name">
									<textarea rows="8" cols="50" id="<pg:cell colName='node_key'/>_all_names"  
									style="width: 600px;" readonly></textarea>
								</pg:empty>
							</pg:empty>
							<pg:notempty colName="node_orgs_name">
								<pg:notempty colName="node_users_name">
									<textarea rows="8" cols="50" id="<pg:cell colName='node_key'/>_all_names" 
									style="width: 600px;" readonly 
									><pg:cell colName='node_users_name'/>,<pg:cell colName='node_orgs_name'/></textarea>
								</pg:notempty>
								<pg:empty colName="node_users_name">
									<textarea rows="8" cols="50" id="<pg:cell colName='node_key'/>_all_names" 
									style="width: 600px;" readonly 
									><pg:cell colName='node_orgs_name'/></textarea>
								</pg:empty>
							</pg:notempty>
							<pg:notin scope="${filterNode}" colName="node_key">
								<a href="javascript:openChooseUsers('<pg:cell colName="node_key"/>')" >选择</a>
								<a href="javascript:emptyChoose('<pg:cell colName="node_key"/>','1')">清空</a>
							</pg:notin>
						</td>
						<td >
							<span id="<pg:cell colName='node_key'/>_nodeTypeName"><pg:cell colName="nodeTypeName"/></span>
						</td>
					</tr>
				</pg:equal>
			</pg:list>
		</table>
		
		<div class="btnarea" >
			<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="dosubmit()"><span><pg:message code="sany.pdp.common.ok"/></span></a>
			<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.close"/></span></a>
		</div>
	</form>
</div>
</body>

</html>
