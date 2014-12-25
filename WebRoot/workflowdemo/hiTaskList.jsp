<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<pg:notempty actual="${taskHistorList}"> 
	
	<input type="hidden" id="hiTaskSzie" name="hiTaskSzie" value="<pg:size requestKey="taskHistorList"/>"/>
	
	<div class="title_1" id="logStyle">审批记录 
		<pg:true actual="${filterLog}" evalbody="true">
			<pg:yes>
				<a href="javascript:showLogStyle('0')"><font color="#0a70ed">[不合并重复记录]</font></a>
			</pg:yes>
			<pg:no>
				<a href="javascript:showLogStyle('1')"><font color="#0a70ed">[合并重复记录]</font></a>
			</pg:no>
		</pg:true>
	</div>
	
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
					
				</td>
			</tr>
		</pg:list>
	</table>
	
</pg:notempty>

