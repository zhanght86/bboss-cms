<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<div>
<fieldset>
<legend>
	历史审批
</legend>
<div id="div1">
<pg:list requestKey="taskInfoList">
	<div class="form_box" onclick="slide('<pg:cell colName="id" />')">
		<fieldset>
			<legend>
				<pg:cell colName="task_name" />
			</legend>
			<table border="0" cellspacing="0" cellpadding="0" width="100%"
				class="table5"  id="<pg:cell colName='id' />">
				<tr>
					<th width=200px>处理人：</th>
					<td width=400px align="left"><pg:cell colName="deal_user" /></td>
					<th width=200px>处理时间：</th>
					<td width=400px align="left"><pg:cell colName="deal_time" /></td>
				</tr>
				<tr>
					<th width=200px>是否通过：</th>
					<td width=400px align="left">
						<pg:equal colName="is_pass" value="true">通过</pg:equal>
						<pg:equal colName="is_pass" value="false">驳回</pg:equal>
					</td>
				</tr>
				<tr>
					<th width=200px>处理意见：</th>
					<td width=400px align="left"><pg:cell colName="deal_opinion" /></td>
				</tr>
			</table>
		</fieldset>
	</div>
</pg:list>
</div>
</fieldset>
</div>
<script type="text/javascript">
function slide(id){
	$('#'+id).slideToggle("slow"); 
}
</script>
