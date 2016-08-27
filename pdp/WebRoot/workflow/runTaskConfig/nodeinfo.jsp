<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<div class="tabbox" id="tabbox">
	<ul class="tab" id="menu0">
		<li><a href="javascript:void(0)" class="current"
			onclick="setTab(0,0)"><span>待办配置</span></a></li>
		<li><a href="javascript:void(0)" onclick="setTab(0,1)"><span>参数配置</span></a></li>
	</ul>
</div>
<div id="main0">
	
	
	<ul id="tab1" style="display: block;">
		<div>
			<pg:notempty actual="${activitiNodeNotExist}"><div align="left">${activitiNodeNotExist}</div></pg:notempty>
			
			<form name="submitForm" method="post">
				
				<table border="0" cellspacing="0" cellpadding="0" width="100%"
					class="table4">
					<pg:beaninfo requestKey="nodeInfo">
					<tr>
					
						<th width=150px>节点名称：</th>
						<td width=400px align="left">${nodeInfo.node_name }<input type="hidden" value="<pg:cell colName="id"/>" name="id"
					id="nodeinfoId" /></td>
						<th width=150px>节点ID：</th>
						<td width=400px align="left">${nodeInfo.node_key}</td>

					</tr>
					</pg:beaninfo>
					<pg:beaninfo requestKey="activitiNodeCandidate">
					<tr>
						<th width=150px>待办用户： <input type="hidden"
					value="<pg:cell colName="id"/>" name="nodeCandidateId"
					id="nodeCandidateId" /> <input type="hidden"
					value="<pg:cell colName="candidate_users_id"/>"
					id="candidate_users_id" name="candidate_users_id" /> <input
					type="hidden" value="<pg:cell colName="candidate_groups_id"/>"
					id="candidate_groups_id" name="candidate_groups_id" /></th>
						<td width=250px align="left"><textarea
								id="candidate_users_name" name="candidate_users_name"
								
									onclick="openChooseUsers()"
								readOnly><pg:cell colName="candidate_users_name"/></textarea>
						</td>
					</tr>
					<tr>
						<th width=150px>待办组：</th>
						<td width=250px align="left"><textarea
								id="candidate_groups_name" name="candidate_groups_name"
								onclick="openChooseGroups()"
								 readOnly><pg:cell colName="candidate_groups_name"/></textarea>
						</td>

					</tr>
					</pg:beaninfo>
				</table>
				<div class="btnarea">
					<a href="javascript:void(0)" class="bt_1" id="addButton"
						onclick="dosubmit()"><span>确定</span></a> 
				</div>
			</form>
			
		</div>
	</ul>

	<ul id="tab2">
		<div id="paramconig">
			<table border="0" cellspacing="0" cellpadding="0" width="100%"
				class="table4">
				<tr>
					<th width=150px>节点名称：</th>
					<td width=400px align="left">${nodeInfo.node_name }</td>
					<th width=150px>节点ID：</th>
					<td width=400px align="left">${nodeInfo.node_key}</td>
				</tr>
			</table>
			<div class="title_box">
				<div class="rightbtn">
				<a href="javascript:toAddNodevariable()" class="bt_small" id="addButton"><span>新增</span></a>
				</div>
				
				<strong>节点参数配置</strong>
			</div>
			<div id="nodevariableContainer">
			
			</div>
		</div>
	</ul>
</div>


<script type="text/javascript">
var api = frameElement.api;
function openChooseUsers(){
	var url = "<%=request.getContextPath()%>/workflow/config/toChooseUserPage.page?groups="+$("#candidate_groups_id").val()+"&users="+$("#candidate_users_id").val()+"&nodeCandidateId="+$("#nodeCandidateId").val()+"&nodeinfoId="+$("#nodeinfoId").val();
	$.dialog({ id:'nodeInfoIframe', title:'选择用户',width:1000,height:650, content:'url:'+url}); 
	
}

function openChooseGroups(){
	var url = "<%=request.getContextPath()%>/workflow/config/toChooseGroupPage.page?users="+$("#candidate_users_id").val()+"&groups="+$("#candidate_groups_id").val()+"&nodeCandidateId="+$("#nodeCandidateId").val()+"&nodeinfoId="+$("#nodeinfoId").val();
	$.dialog({ id:'nodeInfoIframe', title:'选择用户组',width:700,height:400, content:'url:'+url}); 
	}

	function dosubmit() {
		$.post($("#submitMethod").val(), {
			id : submitForm.nodeCandidateId.value,
			node_id : submitForm.id.value,
			org_id : $("#orgId").val(),
			candidate_users_id : submitForm.candidate_users_id.value,
			candidate_users_name : submitForm.candidate_users_name.value,
			candidate_groups_id : submitForm.candidate_groups_id.value,
			candidate_groups_name : submitForm.candidate_groups_name.value
		}, function(data) {
			if (data == 'success') {
				//去掉遮罩
				unblockUI();
				alert("保存成功");
				//$.dialog.alert("保存成功",function(){api.close();},api);
			}
		});
	}
	
	function toAddNodevariable(){
		var url="<%=request.getContextPath()%>/workflow/config/toAddNodevariable.page?node_id="+$("#nodeinfoId").val()+"&org_id="+$("#orgId").val();
		$.dialog({
			id : 'nodevariableIframe',
			title : '添加参数配置',
			width : 700,
			height : 250,
			content : 'url:' + url
		});
	}
	
	function toUpdateNodevariable(id){
		var url="<%=request.getContextPath()%>/workflow/config/toUpdateNodevariable.page?id="+id;
		$.dialog({
			id : 'nodevariableIframe',
			title : '修改参数配置',
			width : 700,
			height : 250,
			content : 'url:' + url
		});
	}
	
	function deleteNodevariable(id){
		$.dialog.confirm('确定要删除记录吗？，删除后将不可恢复', function(){
            	$.ajax({
			 	 	type: "POST",
					url : "deleteNodevariable.page",
					data :{"id":id},
					dataType : 'json',
					async:false,
					beforeSend: function(XMLHttpRequest){
						 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
						},
					success : function(data){
				 		loadNodevariableData();
				 		//alert("成功删除记录");
					}	
				 });
             },function(){
             		
             });         
	}
	function loadNodevariableData(){
		$("#nodevariableContainer").load("queryNodevariable.page #nodevariableContent",{node_id:$("#nodeinfoId").val(),org_id:$("#orgId").val()},function(){
			loadjs();
		}); 
	}
	$(document).ready(function() {
		$("#orgNameSpan").attr("innerHTML",'${orgName}>>');
		<pg:empty actual="${activitiNodeNotExist}">
			loadNodevariableData();
		</pg:empty>
		
		if($("#taskId").val()){
			$("#tabbox").attr("style","display:none");
		}
	   });
</script>
</html>
