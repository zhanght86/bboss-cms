<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<pg:notin actual="${pagestate}" scope="1,2,5,6">

	<pg:in actual="${pagestate}" scope="4,7">
	<div class="title_1">审批</div>
	</pg:in>
	
	<pg:equal actual="${pagestate}" value="3">
		<pg:notempty colName="isRecall">
			<pg:equal colName="isRecall" value="1">
				<div class="title_1">审批</div>
			</pg:equal>
		</pg:notempty>
	</pg:equal>
	
	<table width="600" border="0" cellpadding="0" cellspacing="0" class="sany_table2">
		<pg:beaninfo requestKey="task" >
			<input type="hidden" name="nowtaskId"  value="<pg:cell colName="taskId" />" />
			<input type="hidden" name="nowTaskKey" value="<pg:cell colName="taskDefKey" />" />
			<input type="hidden" name="proInsId" value="<pg:cell colName="instanceId" />" />
			<input type="hidden" name="businessKey" value="<pg:cell colName="businessKey" />" />
			<input type="hidden" name="nowTaskFromUser" id="nowTaskFromUser"  value="" />
			<input type="hidden" name="nowTaskToUser" id="nowTaskToUser"  value="" />
			<input type="hidden" name="proOrderId" id="proOrderId" value="" />
			
			<%-- 查看状态，不显示通过，转办，驳回，废弃功能--%>
			<pg:notin actual="${pagestate}" scope="5,6">
			<tr>
				<pg:in actual="${pagestate}" scope="4,7">
				<th width="100"><span class="required" >*</span>处理结果</th>
				</pg:in>
				
				<pg:equal actual="${pagestate}" value="3">
					<pg:notempty colName="isRecall">
						<pg:equal colName="isRecall" value="1">
							<th width="100"><span class="required" >*</span>处理结果</th>
						</pg:equal>
					</pg:notempty>
				</pg:equal>
				
				<td>
			
				<pg:notequal actual="${pagestate}" value="3" >
				<input name="operateType" type="radio" value="pass" />通过
				
				<input name="operateType" type="radio" value="turnTo" />转办
				
				<pg:notempty colName="isCancel">
					<pg:equal colName="isCancel" value="1">
						<input name="operateType" type="radio" value="reject" />驳回 
					</pg:equal>
				</pg:notempty>
				
				<pg:notempty colName="isDiscard">
					<pg:equal colName="isDiscard" value="1">
						<input name="operateType" type="radio" value="toEnd" />废弃
					</pg:equal>
				</pg:notempty>
				</pg:notequal>
				
				<pg:in actual="${pagestate}" scope="3,7" >
				<pg:notempty colName="isRecall">
					<pg:equal colName="isRecall" value="1">
						<input name="operateType" type="radio" value="recall" />撤回
					</pg:equal>
				</pg:notempty>
				</pg:in>
				
				</td>
			</tr>
			</pg:notin>
			
			<tr id="rejectto" style="display: none">
				<th>驳回到：</th>
				<td > 
					<select name="rejectToActId" id="rejectToActId" onchange="setActName()">
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
				<pg:notempty colName="isRecall">
				<pg:equal colName="isRecall" value="1">
					<tr>
						<th>处理意见</th>
						<td><textarea name="dealReason" class="textarea1 h_80" id="dealReason"></textarea></td>
					</tr>
				</pg:equal>
				</pg:notempty>
			</pg:equal>
			
			<pg:notin actual="${pagestate}" scope="3,5,6">
				<tr>
					<th>处理意见</th>
					<td><textarea name="dealReason" class="textarea1 h_80" id="dealReason"></textarea></td>
				</tr>
			</pg:notin>
		
		</pg:beaninfo>
	</table>
</pg:notin>
