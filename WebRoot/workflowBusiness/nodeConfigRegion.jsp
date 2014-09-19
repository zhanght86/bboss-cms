<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

   <pg:notempty actual="${actList}"> 
   		
   		<input type="hidden" id="actListSize" name="actListSize" value="<pg:size requestKey="actList"/>"/>
   		
   		<div class="title_1">审批节点</div>
		<table id="protable" border="0" cellpadding="0" cellspacing="0" class="sany_table">
			<tr>
				<th>节点名</th>
				<th>处理人员</th>
				<th>处理方式</th>
				<th>节点描述</th>
				<th>处理工时(小时)</th>
				<th>节点状态</th>
			</tr>
			
        	<pg:list requestKey="actList">
	            <tr id="protr<pg:rowid increament="1" />" >
		            <input type="hidden" name="actId" id="actId<pg:rowid increament="1" />" nvl="<pg:cell colName="isEdit" />"
		               value="<pg:cell colName="actId" />"  con="<pg:rowid increament="1" />" />
		             <input type="hidden" name="actName" id="actName<pg:rowid increament="1" />" value="<pg:cell colName="actName" />"/>
		             <input type="hidden" name="candidateName" id="candidateName<pg:rowid increament="1" />" value="<pg:cell colName="candidateName" />" /> 
		             <input type="hidden" name="realName" id="realName<pg:rowid increament="1" />" value="<pg:cell colName="realName" />" />
		             <input type="hidden" name="isValid" id="isValid<pg:rowid increament="1" />" value="<pg:cell colName="isValid" />"/>
		             <input type="hidden" name="isEdit" id="isEdit<pg:rowid increament="1" />" value="<pg:cell colName="isEdit" />"/>
		             <input type="hidden" name="isEditAfter" id="isEditAfter<pg:rowid increament="1" />" value="<pg:cell colName="isEditAfter" />"/>
		             <input type="hidden" name="isAuto" id="isAuto<pg:rowid increament="1" />" value="<pg:cell colName="isAuto" />"/>
		             <input type="hidden" name="isAutoAfter" id="isAutoAfter<pg:rowid increament="1" />" value="<pg:cell colName="isAutoAfter" />"/>
		             <input type="hidden" name="isRecall" id="isRecall<pg:rowid increament="1" />" value="<pg:cell colName="isRecall" />"/>
		             <input type="hidden" name="isCancel" id="isCancel<pg:rowid increament="1" />" value="<pg:cell colName="isCancel" />"/>
		             <input type="hidden" name="isDiscard" id="isDiscard<pg:rowid increament="1" />" value="<pg:cell colName="isDiscard" />"/>
		             <input type="hidden" name="isMulti" id="isMulti<pg:rowid increament="1" />" value="<pg:cell colName="isMulti" />"/>
		             <input type="hidden" name="isMultiDefault" id="isMultiDefault<pg:rowid increament="1" />" value="<pg:cell colName="isMultiDefault" />"/>
		             <input type="hidden" name="isSequential" id="isSequential<pg:rowid increament="1" />" value="<pg:cell colName="isSequential" />"/>
		             <input type="hidden" name="isCopy" id="isCopy<pg:rowid increament="1" />" value="<pg:cell colName="isCopy" />"/>
		             <input type="hidden" name="nodeDescribe" id="nodeDescribe<pg:rowid increament="1" />" value="<pg:cell colName="nodeDescribe" />"/>
		             <input type="hidden" name="nodeWorkTime" id="nodeWorkTime<pg:rowid increament="1" />" value="<pg:cell colName="nodeWorkTime" />"/>
		             <input type="hidden" name="taskUrl" id="taskUrl<pg:rowid increament="1" />" value="<pg:cell colName="taskUrl" />"/>
		             <input type="hidden" name="bussinessControlClass" id="bussinessControlClass<pg:rowid increament="1" />" value="<pg:cell colName="bussinessControlClass" />"/>
		             <input type="hidden" name="approveType" id="approveType<pg:rowid increament="1" />" value="<pg:cell colName="approveType" />"/>
			      <td>
				      <pg:cell colName="actName" /> 
			      </td>
			      <td>
				      <span id="realnames<pg:rowid increament="1" />">
					     <pg:cell colName="realName" />
				      </span> 
					  <a href="javascript:setCandidate(<pg:rowid increament="1" />)" name="chooseUsersa" style="display: none;"><font color="#0a70ed">[选择]</font></a>
				      <span class="required" style="display: none;">*</span>
			      </td>
			      <td>审批：
			      	  <pg:equal colName="approveType" value="0" >单实例  串行</pg:equal>
		       		  <pg:equal colName="approveType" value="10" >多实例  串行</pg:equal>
		       		  <pg:equal colName="approveType" value="20" >多实例  并行</pg:equal>
		       		  
		       		  <!-- 
				   	<select name="approveType" id="approveType<pg:rowid increament="1"/>" >
				      	<pg:equal colName="isMultiDefault" value="0">
				      	<option <pg:equal colName="approveType" value="0">selected</pg:equal> value="0">单实例</option>
				      	</pg:equal>
				      	<option <pg:equal colName="approveType" value="10">selected</pg:equal> value="10">多实例  串行</option>
				      	<option <pg:equal colName="approveType" value="20">selected</pg:equal> value="20">多实例  并行</option>
				  	</select>
				  	 -->
			      </td>
			      <td>
				      <pg:empty colName="nodeDescribe">&nbsp;</pg:empty>
				      <pg:notempty colName="nodeDescribe"><pg:cell colName="nodeDescribe" /></pg:notempty>
			      </td>
			      <td>
			      	  <pg:empty colName="nodeWorkTime">&nbsp;</pg:empty>
				      <pg:notempty colName="nodeWorkTime"><pg:cell colName="nodeWorkTime" /></pg:notempty>
				  </td>
			      <td id="zx<pg:cell colName="actId" />">未执行</td>
		       </tr>
    		</pg:list>
	    </table>
    </pg:notempty>
