<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<pg:notempty actual="${taskHistorList}"> 
	
	<input type="hidden" id="hiTaskSzie" name="hiTaskSzie" value="<pg:size requestKey="taskHistorList"/>"/>
	
	<div class="title_1">审批记录</div>
	<table id="protable" border="0" cellpadding="0" cellspacing="0" class="sany_table">
		<pg:list requestKey="taskHistorList">
			<tr>
				<input type="hidden" name="hiActId" id="hiActId<pg:rowid increament="1" />" 
			       value="<pg:cell colName="TASK_DEF_KEY_" />" />
			       
				<td><pg:cell colName="END_TIME_"  dateformat="yyyy-MM-dd HH:mm:ss"/></td>
				<td><pg:cell colName="NAME_" /></td>
				<td><pg:cell colName="ASSIGNEE_NAME" /></td>
				<td>
					<pg:empty colName="DELETE_REASON_" >
						<pg:equal colName="IS_AUTO_COMPLETE" value="1">
			    		系统自动完成任务
		    			</pg:equal>
		    		</pg:empty>
		    		<pg:notempty colName="DELETE_REASON_" >
						<pg:equal colName="IS_AUTO_COMPLETE" value="0">
				    		<pg:cell colName="DELETE_REASON_" />
			    		</pg:equal>
		    		</pg:notempty>
				</td>
			</tr>
		</pg:list>
	</table>
</pg:notempty>

