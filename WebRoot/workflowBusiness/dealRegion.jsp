<%@ page language="java" pageEncoding="utf-8"%>

	<table cellpadding="0" cellspacing="0" border="0" class="stable" width="100%"  style="margin-top:10px;">
		
		<tr id="recalltr" style="display: none">
		  <th width="128">处理结果</th>
		  <td><input name="operateType" type="radio" value="recall" />撤回</td>
		</tr>
		
		<tr style="display: none">
			<th width="128">处理结果</th>
			<td>
				<input name="operateType" type="radio" value="pass" />通过
				<input name="operateType" type="radio" value="reject" />驳回 
				<input name="operateType" type="radio" value="turnTo" />转办
			    <input name="operateType" type="radio" value="toEnd" />废弃
			</td>
		</tr>
		
		<tr id="rejectto" style="display: none">
			<th>驳回到：</th>
			<td > 
			<select name="rejectToActId" id="rejectToActId">
				<option value="usertask1">
					1.申请人
				</option>
			</select>
			<input type="hidden" name="toActName" value="" readonly="readonly"/>
			</td>
		</tr>
		
		<tr id="delegateTr" style="display: none">
			<th>转办</th>
			<td>
			<input type="hidden" name="delegateUser" id="delegateUser" value="" />
			<input type="text" name="delegateUserName" id="delegateUserName" value="" size="50" readonly="readonly"/>
			<a href="javascript:delegateUsers('')" name="delegateUsersa">[选择]</a>
			</td>
		</tr>
		
		<tr style="display: none">
			<th width="128">处理意见</th>
			<td><textarea name="remark" id="remark"  style="width: 90%"></textarea></td>
		</tr>
	</table>
