<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

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
				<th width="30%">节点描述</th>
				<th width="10%">处理工时(小时)</th>
				<th width="10%">节点状态</th>
			</tr>
				
			<input type="hidden" id="tempnum" name="tempnum" value=""/>
			
        	<pg:list requestKey="actList">
	            <tr id="protr<pg:rowid increament="1" />" >
		            <input type="hidden" name="actId" id="actId<pg:rowid increament="1" />"  
		               value="<pg:cell colName="actId" />"  nvl="<pg:cell colName="editAfter" />"  con="<pg:rowid increament="1" />" />
		             <input type="hidden"  name="autoApprove" id="autoApprove<pg:rowid increament="1" />" value="<pg:cell colName="autoApprove" />"/>
		             <input type="hidden" name="candidateName" id="candidateName<pg:rowid increament="1" />" value="<pg:cell colName="candidateName" />" /> 
		             <input type="hidden" name="realName" id="realName<pg:rowid increament="1" />" value="<pg:cell colName="realName" />" />
		             <input type="hidden" name="canRecall" id="canRecall<pg:cell colName="actId" />" value="<pg:cell colName="canRecall" />"/>
		             <input type="hidden" name="isValid" id="isValid<pg:cell colName="actId" />" value="<pg:cell colName="isValid" />"/>
		             <input type="hidden" name="actName" id="actName<pg:cell colName="actId" />" value="<pg:cell colName="actName" />"/>
		             <input type="hidden" name="canEdit" id="canEdit<pg:cell colName="actId" />" value="<pg:cell colName="canEdit" />"/>
		             <input type="hidden" name="describe" id="describe<pg:cell colName="actId" />" value="<pg:cell colName="describe" />"/>
		             <input type="hidden" name="editAfter" id="editAfter<pg:cell colName="actId" />" value="<pg:cell colName="editAfter" />"/>
		             <input type="hidden" name="nodeWorkTime" id="nodeWorkTime<pg:cell colName="actId" />" value="<pg:cell colName="nodeWorkTime" />"/>
			       <td>
				        <pg:cell colName="actName" /> 
			       </td>
			       <td>
				       <span id="realnames<pg:rowid increament="1" />">
					     <pg:cell colName="realName" />
				       </span> 
				       <pg:equal colName="canEdit" value="1" >
						  <a href="javascript:setCandidate(<pg:rowid increament="1" />)" name="chooseUsersa" ><font color="#0a70ed">[选择]</font></a>
				       </pg:equal>
			      </td>
			      <td>审批：
			      	<pg:equal colName="canEdit" value="0">
			      		<input type="hidden" name="approveType" value="<pg:cell colName="approveType" />" />
			       		<pg:in colName="approveType" scope="0,10" >串行</pg:in>
			       		<pg:equal colName="approveType" value="20" >并行</pg:equal>
			       		<pg:equal colName="approveType" value="30" >抄送</pg:equal>
			       	</pg:equal>
			       	<pg:equal colName="canEdit" value="1">
			       		<select name="approveType">
							<option value="10" <pg:equal colName="approveType" value="10">selected</pg:equal>>串行</option>
							<option value="20" <pg:equal colName="approveType" value="20">selected</pg:equal>>并行</option>
							<option value="30" <pg:equal colName="approveType" value="30">selected</pg:equal>>抄送</option>
						</select>
			       	</pg:equal>
			      </td>
			      <td><pg:cell colName="describe" /></td>
			      <td><pg:cell colName="nodeWorkTime" /></td>
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
  	<img src="${pageContext.request.contextPath}/workflowBusiness/business/getProccessPic.page?processKey=${processKey}" />
</ul>

