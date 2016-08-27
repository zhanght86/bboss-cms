<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<pg:notempty actual="${taskHistorList}"> 
	
	<input type="hidden" id="hiTaskSzie" name="hiTaskSzie" value="<pg:size requestKey="taskHistorList"/>"/>
	
	<div class="title_1">审批记录</div>
	
	<table id="hiprotable" border="0" cellpadding="0" cellspacing="0" class="sany_table">
			<tr>
				<th>审批时间</th>
				<th>节点名称</th>
				<th style="width: 20%">处理人</th>
				<th>操作类型</th>
				<th>处理原因</th>
				<th>备注</th>
			</tr>
			
		<pg:list requestKey="taskHistorList">
			<tr id="<pg:cell colName="ID_" />">
				<input type="hidden" name="hiActId" id="hiActId<pg:rowid increament="1" />" 
			       value="<pg:cell colName="TASK_DEF_KEY_" />" />
			       
				<td><pg:cell colName="END_TIME_"  dateformat="yyyy-MM-dd HH:mm:ss"/></td>
				<td><pg:cell colName="ACT_NAME_" /></td>
				<td>
					<pg:equal colName="IS_AUTO_COMPLETE" value="2" evalbody="true" >
						<pg:yes>
							<pg:notempty colName="ASSIGNEE_NAME">
								已阅：<pg:cell colName="ASSIGNEE_NAME" /> 
								<a href="javascript:viewCopyTaskInfo('<pg:cell colName="ID_" />')" name="" ><font id="copytaskFont" color="#0a70ed">[明细]</font></a>
							</pg:notempty>
						</pg:yes>
						<pg:no>
							<pg:cell colName="ASSIGNEE_NAME" />
						</pg:no>
					</pg:equal>
					
				<%-- 	<pg:equal colName="IS_AUTO_COMPLETE" value="2">
						<pg:notempty colName="ASSIGNEE_NAME">
							已阅：<pg:cell colName="ASSIGNEE_NAME" /> 
							<a href="javascript:viewCopyTaskInfo('<pg:cell colName="ID_" />')" name="" ><font id="copytaskFont" color="#0a70ed">[明细]</font></a>
						</pg:notempty>
					</pg:equal>
					<pg:notequal colName="IS_AUTO_COMPLETE" value="2">
						<pg:cell colName="ASSIGNEE_NAME" />
					</pg:notequal> --%>
				</td>
				<td>
					<pg:empty colName="BUSSINESS_OP" evalbody="true">
						<pg:yes>
							&nbsp;
						</pg:yes>
						<pg:no>
							<pg:cell colName="BUSSINESS_OP" />
						</pg:no>
					</pg:empty>
				<%-- 	<pg:empty colName="BUSSINESS_OP" >&nbsp;</pg:empty>
					<pg:notempty colName="BUSSINESS_OP" >
						<pg:cell colName="BUSSINESS_OP" />
					</pg:notempty> --%>
				</td>
				<td>
					<pg:empty colName="BUSSINESS_REMARK" evalbody="true">
						<pg:yes>
							&nbsp;
						</pg:yes>
						<pg:no>
							<pg:cell colName="BUSSINESS_REMARK" />
						</pg:no>
					</pg:empty>
					<%-- <pg:empty colName="BUSSINESS_REMARK" >&nbsp;</pg:empty>
					<pg:notempty colName="BUSSINESS_REMARK" >
						<pg:cell colName="BUSSINESS_REMARK" />
					</pg:notempty> --%>
				</td>
				<td>
					<pg:equal colName="IS_AUTO_COMPLETE" value="1" evalbody="true">
						<pg:yes>
							系统自动完成任务
						</pg:yes>
						<pg:no>
							<pg:cell colName="DELETE_REASON_" />
						</pg:no>
					</pg:equal>
					
					<%-- <pg:notequal colName="IS_AUTO_COMPLETE" value="1">
			    		<pg:cell colName="DELETE_REASON_" />
		    		</pg:notequal>
		    		<pg:equal colName="IS_AUTO_COMPLETE" value="1">
			    		系统自动完成任务
		    		</pg:equal> --%>
				</td>
			</tr>
		</pg:list>
	</table>
	
</pg:notempty>

