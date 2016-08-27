<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/css.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<div class="form">
<form name="submitForm" method="post">
<input type="hidden" value="${object.id }" name="id" id="nodeinfoId"/>
<input type="hidden" value="${object.candidate_users_id }" id="candidate_users_id" name="candidate_users_id"/>
<input type="hidden" value="${object.candidate_groups_id }" id="candidate_groups_id" name="candidate_groups_id"/>
		<table border="0" cellspacing="0" cellpadding="0" width="100%"
			class="table4">
			<tr>
				<th width=150px>key：</th>
				<td width=400px align="left">${object.process_key }</td>
			</tr>
			<tr>
				<th width=150px>节点ID：</th>
				<td width=400px align="left">${object.node_id}</td>
				<th width=150px>节点名称：</th>
				<td width=400px align="left">${object.node_name }</td>

			</tr>
			<tr>
				<th width=150px>待办用户：</th>
				<td width=250px align="left">
				<textarea id="candidate_users_name" name="candidate_users_name" onclick="openChooseUsers()" readOnly>${object.candidate_users_name }</textarea>
				</td>
			</tr>
			<tr>
				<th width=150px>待办组：</th>
				<td width=250px align="left">
				<textarea id="candidate_groups_name" name="candidate_groups_name" onclick="openChooseGroups()">${object.candidate_groups_name }</textarea>
				</td>
				
			</tr>
		</table>
		<div class="btnarea" >
				<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="dosubmit()"><span>确定</span></a>
				<a href="javascript:void(0)" class="bt_2" id="cancelButton" onclick="docancel()"><span>退出</span></a>
			</div>
	</form>
</div>
<script type="text/javascript">
var api = frameElement.api, W = api.opener;
function openChooseUsers(){
	document.submitForm.action="../test/toChooseUserPage.page?users="+$("#candidate_users_id").val()+"&nodeinfoId="+$("#nodeinfoId").val();
	document.submitForm.submit();
	
}

function openChooseGroups(){
	document.submitForm.action="../test/toChooseGroupPage.page?groups="+$("#candidate_groups_id").val()+"&nodeinfoId="+$("#nodeinfoId").val();
	document.submitForm.submit();
}

function dosubmit()
{
	$.post('editActivitiNode.page',
			{id:submitForm.id.value,
			candidate_users_id:submitForm.candidate_users_id.value,
			candidate_users_name:submitForm.candidate_users_name.value,
			candidate_groups_id:submitForm.candidate_groups_id.value,
			candidate_groups_name:submitForm.candidate_groups_name.value
			},
			function (data){
				if(data=='success'){
					//去掉遮罩
					unblockUI();
					$.dialog.alert("保存成功",function(){api.close();},api);
				}
				});
	 }
function docancel(){
	api.close();
}
function doreset(){
	$("#reset").click();
}
</script>
</html>
