<%@ page language="java" pageEncoding="utf-8"%>
<%
	AccessControl control = AccessControl.getAccessControl();

	request.setAttribute("userId", control.getUserID()); //1
	request.setAttribute("userName", control.getUserName()); //系统管理员
	request.setAttribute("userAccount", control.getUserAccount()); //admin
	System.out.println(control.getUserID()+","+control.getUserName()+","+control.getUserAccount());
%>
<script type="text/javaScript"> 
	$(document).ready(function(){
		
		$("form").data("user", { userId: "${userId}", userName: "${userName}" });
		$("form").data("coordinateObj", { id: "${coordinateObj.id}" });
		$("form").data("pagestate", "${pagestate}");  
		$("form").data("assignees", "${assignees}");

		//如果是初始提交页面，加载相关数据
		<pg:in actual="${pagestate}" scope="1,2">workflowaddInit();</pg:in>
		<pg:equal actual="${pagestate}" value="3">showRecall();showTemp();</pg:equal>

	});
	
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/sanyadm/script/dateUtils.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/sanyadm/script/stringUtils.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/sanyadm/workflow/js/workflow.js" ></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/sanyadm/workflow/js/workflowCandidate.js" ></script>
<ul style="display: none">
	<pg:notin actual="${pagestate}" scope="5,6">
		<table cellpadding="0" cellspacing="0" border="0" class="stable" width="100%"  style="margin-top:10px;">
		
			<pg:equal actual="${pagestate}" value="3">
				<tr id="recalltr" style="display: none;">
				  <th width="128">处理结果</th>
				  <td><input name="operateType" type="radio" value="recall" />撤回</td>
				</tr>
			</pg:equal>
			
			<pg:in actual="${pagestate}" scope="2,4">
				<tr>
					<th width="128">处理结果</th>
					<td>
						<pg:notequal actual="${isRecallPayment}" value="Yes">
						<input name="operateType" type="radio" value="pass" />通过
						</pg:notequal>
					    <pg:equal actual="${pagestate}" value="4">   <!-- 审批中审批人查看 -->
					        <c:if test="${usertask == 'usertask2' || usertask == 'usertask3'}">
							    <input name="operateType" type="radio" value="reject" />驳回 
						    </c:if>	
						    <input name="operateType" type="radio" value="turnTo" />转办
					    </pg:equal> 
					     <pg:equal actual="${pagestate}" value="2"> <!-- 提交人节点查看 -->
					         <input name="operateType" type="radio" value="toEnd" />废弃
						</pg:equal>
					</td>
					</tr>
					<tr id="rejectto" style="display: none">
						<th>驳回到：</th>
						<td style="padding-bottom: 2px"> <select name="rejectToActId" id="rejectToActId">
									<option value="usertask1">
										1.申请人
									</option>
						</select><input type="hidden" name="toActName" value="" readonly="readonly"/>
						&nbsp;&nbsp;<!-- <input type="checkbox" name="isReturn" value="1" />驳回的节点通过后直接返回本节点 -->
						</td>
					</tr>
					<tr id="delegateTr" style="display: none">
						<th>转办</th>
						<td style="padding-bottom: 2px"><input type="hidden" name="delegateUser" id="delegateUser" value="" />
						<input type="text" name="delegateUserName" id="delegateUserName" value="" size="50" readonly="readonly"/>
						<a href="javascript:delegateUsers('')" name="delegateUsersa">[选择]</a>
						<span class="requiredstar" style="color: red;display: none;">*</span>
						</td>
					</tr>
				</pg:in>
			
				<pg:notin actual="${pagestate}" scope="5,6">
				<tr>
					<th width="128">处理意见</th>
					<td><textarea name="remark" id="remark"  style="width: 90%"></textarea></td>
				</tr>
				</pg:notin>
		</table>
	</pg:notin>
	
	<div class="a_d_title">
		<ul class="tab" id="menu3">
			<li><a href="javascript:void(0)" class="current"
						onclick="setTabC(3,0)"><span>审批记录</span></a></li>
			 <li><a href="javascript:void(0)" onclick="setTabC(3,1)"><span>流程图</span></a></li>
		</ul>
	</div>
			
	<div id="main3">
		<ul>
		   <pg:notempty actual="${actList}"> 
				<table id="protable"  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" style="margin-top:10px;">
					  <pg:beaninfo requestKey="userTask">
						<input type="hidden" name="nowtaskId"  value="<pg:cell colName="taskId" />" />
						<input type="hidden" name="nowTaskKey" value="<pg:cell colName="taskDefKey" />" />
						<input type="hidden" name="proInsId" value="<pg:cell colName="instanceId" />" />
						<input type="hidden" name="businessKey" value="<pg:cell colName="businessKey" />" />
						<input type="hidden" name="proOrderId" id="proOrderId" value="<pg:cell colName="proOrderId" />" />
					  </pg:beaninfo>
					  
					<input id="processKey" type="hidden" value="${processKey }"/>
						<tr>
							<th width="10%">节点名</th>
							<th width="20%">处理人员</th>
							<th width="10%">处理方式</th>
							<th width="40%">节点描述</th>
							<th width="10%">节点状态</th>
						</tr>
						
					<input type="hidden" id="tempnum" name="tempnum" value=""/>
			        	<pg:list requestKey="actList">
				            <tr id="protr<pg:rowid increament="1" />"  class="tr2" >
					            <input type="hidden" name="actId" id="actId<pg:rowid increament="1" />"  
					               value="<pg:cell colName="actId" />"  nvl="<pg:cell colName="editAfter" />"  con="<pg:rowid increament="1" />" />
					             <input type="hidden"  name="autoApprove" id="autoApprove<pg:rowid increament="1" />" value="<pg:cell colName="autoApprove" />"/>
					             <input type="hidden" name="candidateName" id="candidateName<pg:rowid increament="1" />" value="<pg:cell colName="candidateName" />" /> 
					             <input type="hidden" name="realName" id="realName<pg:rowid increament="1" />" value="<pg:cell colName="realName" />" />
					             <input type="hidden" name="approveType" 	id="approveType<pg:rowid increament="1" />" value="<pg:cell colName="approveType" />" />
					             <input type="hidden" name="canRecall" id="canRecall<pg:cell colName="actId" />" value="<pg:cell colName="canRecall" />"/>
					             <input type="hidden" name="actName" id="actName<pg:cell colName="actId" />" value="<pg:cell colName="actName" />"/>
					             <input type="hidden" name="canEdit" id="canEdit<pg:cell colName="actId" />" value="<pg:cell colName="canEdit" />"/>
					             <input type="hidden" name="describe" id="describe<pg:cell colName="actId" />" value="<pg:cell colName="describe" />"/>
					             <input type="hidden" name="editAfter" id="editAfter<pg:cell colName="actId" />" value="<pg:cell colName="editAfter" />"/>
						       <td>
							        <pg:cell colName="actName" /> 
						       </td>
						       <td>
							       <span id="realnames<pg:rowid increament="1" />">
								     <pg:cell colName="realName" />
							       </span> 
							       <pg:equal colName="canEdit" value="10" >
									  <a href="javascript:setCandidate(<pg:rowid increament="1" />)" name="chooseUsersa" style="display: none;"><font color="#0a70ed">[选择]</font></a>
							       </pg:equal>
							       <span class="requiredstar" style="color: red;display: none;">*</span>
						      </td>
						      <td>审批：
						       <pg:equal colName="approveType" value="10" >串行</pg:equal>
						       <pg:equal colName="approveType" value="20" >并行</pg:equal>
						      </td>
						      <td>
						      <pg:equal colName="actId" value="usertask1"><pg:cell colName="describe" /> </pg:equal>
						      <pg:equal colName="actId" value="usertask2">系统自动带出申请人的直接领导，若为空或有误 ，请用户自行修改</pg:equal>
						      <pg:equal colName="actId" value="usertask3">对会议室进行调度，调度通过则预订成功</pg:equal>
						      <pg:equal colName="actId" value="usertask4">会议完成后登记实际会议结束时间和产生的其他费用</pg:equal>
						      </td>
						      <td id="zx<pg:cell colName="actId" />">未执行</td>
					       </tr>
			    		</pg:list>
			    </table>
		    </pg:notempty>
			<pg:list requestKey="proLogList">
				<pg:cell colName="operateDate" maxlength="19"/>&nbsp;&nbsp;&nbsp;
				<pg:cell colName="taskName" />&nbsp;&nbsp;&nbsp;
				<pg:cell colName="operateName" />&nbsp;&nbsp;&nbsp;
				<pg:cell colName="operateDescription" />&nbsp;&nbsp;&nbsp;
				<pg:cell colName="operateRemark" /><br>
			</pg:list>
	
			<br>
		</ul>
		
		<ul style="display: none">	
		  <div class="module ta pd_10 mr_10">
		  	<img src="${pageContext.request.contextPath}/workflow/getProccessPicByProcessKey.page?processKey=${processKey}" />
		  </div> 
       </ul>
       
	</div>
	
   	<pg:list requestKey="hisActList">
		<script type="text/javaScript">
			selectNode('<pg:cell colName="actId" />','已执行');
		</script>
	</pg:list>
</ul>
