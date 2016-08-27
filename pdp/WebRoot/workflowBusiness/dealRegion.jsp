<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<pg:notempty actual="${pagestate}">
	<pg:notin actual="${pagestate}" scope="1,2,5,6">
	
		<pg:in actual="${pagestate}" scope="4,7">
			<div class="title_1">审批</div>
		</pg:in>
		
		<pg:equal actual="${pagestate}" value="3">
			<pg:true actual="${task.isRecall eq 1 or task.isDiscarded eq 1}">
			   <div class="title_1">审批</div>
			</pg:true>
		</pg:equal>
		
		<table width="600" border="0" cellpadding="0" cellspacing="0" class="sany_table2">
			<pg:beaninfo requestKey="task" >
				<input type="hidden" name="nowtaskId"  value="<pg:cell colName="taskId" />" />
				<input type="hidden" name="proInsId" value="<pg:cell colName="instanceId" />" />
				<input type="hidden" name="businessKey" value="<pg:cell colName="businessKey" />" />
				<input type="hidden" name="nowTaskFromUser" id="nowTaskFromUser"  value="" />
				<input type="hidden" name="nowTaskToUser" id="nowTaskToUser"  value="" />
				<input type="hidden" name="proOrderId" id="proOrderId" value="" />
				
				<%-- 查看状态，不显示通过，转办，驳回功能--%>
				
				<pg:in actual="${pagestate}" scope="4,7">
					<tr><th width="100"><span class="required" >*</span>处理结果</th>
				</pg:in>
				
				<pg:equal actual="${pagestate}" value="3">
				
					<pg:true actual="${task.isRecall eq 1 or task.isDiscarded eq 1}">
						<tr><th width="100"><span class="required" >*</span>处理结果</th>
					</pg:true>
					
				</pg:equal>
				
				<pg:notequal actual="${pagestate}" value="3" >
					<td>
					
					<input name="operateType" type="radio" value="pass" />通过
					
					<input name="operateType" type="radio" value="turnto" />转办
					
					<pg:equal colName="isCancel" value="1">
						<input name="operateType" type="radio" value="reject" />驳回 
					</pg:equal>
				
					<pg:equal colName="isDiscard" value="1">
						<input name="operateType" type="radio" value="toend" />废弃
					</pg:equal>
					
					<pg:equal actual="${pagestate}" value="7" >
						<pg:equal colName="isRecall" value="1">
							<input name="operateType" type="radio" value="recall" />撤回
						</pg:equal>
					</pg:equal>
					
					</td>
				</pg:notequal>
				
				<pg:equal actual="${pagestate}" value="3" >
				
					<pg:true actual="${task.isDiscarded eq 1 or task.isRecall eq 1}">
						<td>
					</pg:true>
				
					<pg:equal colName="isDiscarded" value="1">
						<input name="operateType" type="radio" value="toend" />废弃
					</pg:equal>
				
					<pg:equal colName="isRecall" value="1">
						<input name="operateType" type="radio" value="recall" />撤回
					</pg:equal>
						
					<pg:true actual="${task.isDiscarded eq 1 or task.isRecall eq 1}">
						</td>
					</pg:true>	
					
				</pg:equal>
				
				<pg:in actual="${pagestate}" scope="4,7"></tr></pg:in>
				
				<pg:equal actual="${pagestate}" value="3">
					<pg:true actual="${task.isRecall eq 1 or task.isDiscarded eq 1}">
						</tr>
					</pg:true>
				</pg:equal>
				
				<tr id="rejectto" style="display: none">
					<th>驳回到：</th>
					<td > 
						<select name="rejectToActId" id="rejectToActId">
							<pg:list requestKey="backActNodeList">
								<option value="<pg:cell colName="actId"/>"><pg:cell colName="actName"/></option>
							</pg:list>
						</select>
						<input type="hidden" name="toActName" value="" />
						<input type="checkbox" name="isReturn" value="1" />驳回的节点通过后直接返回本节点
					</td>
				</tr>
				
				<tr id="delegateTr" style="display: none">
					<th><span class="required" >*</span>转办</th>
					<td >
						<input type="hidden" name="delegateUser" id="delegateUser" value="" />
						<input type="text" name="delegateUserName" id="delegateUserName" value="" size="50" readonly="readonly"/>
						<a href="javascript:delegateUsers('')" name="delegateUsersa">[选择]</a>
					</td>
				</tr>
				
				<%-- 提交人查看，当前节点不可被撤销；第三方查看；流程结束查看；不显示处理意见--%>
				<pg:equal actual="${pagestate}" value="3">
					<pg:true actual="${task.isRecall eq 1 or task.isDiscarded eq 1}">
					   	<tr>
							<th>处理意见</th>
							<td><textarea name="dealReason" class="textarea1 h_80" id="dealReason"></textarea></td>
						</tr>
					</pg:true>
				</pg:equal>
				
				<pg:notequal actual="${pagestate}" value="3">
					<tr>
						<th>处理意见</th>
						<td><textarea name="dealReason" class="textarea1 h_80" id="dealReason"></textarea></td>
					</tr>
				</pg:notequal>
			</pg:beaninfo>
		</table>
		
	</pg:notin>
</pg:notempty>