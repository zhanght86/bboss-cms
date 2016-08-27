<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<div class="form_box">
	
	<form name="applyForm" method="post">
		<!--  class="collapsible"  收缩 -->
		<fieldset>
			<pg:beaninfo requestKey="taskInfo" >
			<legend>${task.name }</legend>
			<input type="hidden" value="${task.id }" name="task_id" id="task_id"/>
			<input type="hidden" value="${task.processInstanceId }" name="process_instance_id"/>
			<input type="hidden" value="${task.name }" name="task_name"/>
			</pg:beaninfo>
			<div class="fright" align="left"><a href="javascript:void(0)" onclick="delegateTask()" class="bt_small"><span>转办</span></a><a href="javascript:void(0)" onclick="claimTask()" class="bt_small"><span>签收</span></a>
			<table border="0" cellpadding="0" cellspacing="0" class="table4">
				<tr>
					<th width=85px>处理用户：</th>
					<td width=200px><input id="deal_user" name="deal_user" type="text" value="${username }" readOnly
								class="w120 input_default easyui-validatebox" required="true"
								maxlength="100" /><font color="red">*</font>
					</td>
					<th width=85px>处理时间：</th>
					<td width=200px><input id="deal_time" name="deal_time" type="text" value="${dealTime }"
								class="w120 input_default easyui-validatebox" required="true"
								maxlength="100" /><font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th width=85px>是否通过：</th>
					<td width=200px><input type="radio" value="true" name="is_pass"
						checked>通过</input> <input type="radio" value="false" name="is_pass">驳回</input>
					</td>
					<th width=85px></th>
					<td width=200px>
					</td>
				</tr>
				<tr>
					<th width=85px>处理意见：</th>
					<td width=200px><textarea id="deal_opinion" name="deal_opinion" class="tarea"></textarea></td>
				</tr>
			</table>
		</fieldset>
		<div class="btnarea">
			<a href="javascript:void(0)" class="bt_1" id="addButton"
				onclick="dosubmit()"><span>提交</span></a> <a
				href="javascript:void(0)" class="bt_2" id="resetButton"
				onclick="doreset()"><span>重置</span></a> <input type="reset"
				id="reset" style="display: none;" />
		</div>
	</form>
</div>
<script language="javascript">

function claimTask(){
	var taskId = $('#task_id').val();
	var userId = $('#deal_user').val();
	$.post('claimTask.page',
			{taskId:taskId,user:userId},
			function (data){
				if(data=='success'){
					alert("签收成功");
					}
				}
			);
}

function toUserList(){
	document.applyForm.action="../test/toUserListPage.page";
	document.applyForm.submit();
}


function delegateTask(){
	var taskId = $('#task_id').val();
	var url="<%=request.getContextPath()%>/test/toDelegateTask.page?taskId="+taskId;
	 $.dialog({ id:'iframeNewId', title:'选择用户',width:740,height:560, content:'url:'+url}); 
	
}

   function dosubmit()
   {
		document.applyForm.action="../test/operateTask.page";
		document.applyForm.submit();
   	 }
function doreset(){
	$("#reset").click();
}
</script>
