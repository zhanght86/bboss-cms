<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：流程实例列表
作者：谭湘
版本：1.0
日期：2014-05-08
 --%>	

<div id="customContent">
	<pg:empty actual="${processInsts}" >
		<div class="nodata">
		<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
	</pg:empty> 
	
	<pg:notempty actual="${processInsts}">
	   <pg:pager scope="request" data="processInsts" desc="true" isList="false" containerid="instanceContainer" selector="customContent">
	   
	   	<pg:param name="wf_key"/>
	   	<pg:param name="wf_Inst_Id"/>
	   	<pg:param name="wf_start_time1"/>
	   	<pg:param name="wf_start_time2"/>
	   	<pg:param name="wf_end_time1"/>
	   	<pg:param name="wf_end_time2"/>
	   	<pg:param name="wf_state"/>
	   	<pg:param name="wf_versions"/>
	   	<pg:param name="wf_business_key"/>
	   	<pg:param name="businessTypeId"/>
	    <pg:param name="wf_app_name"/>
	   	
		<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
		<div id="changeColor1">
		 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb" sytle="background-color: red;">
	        <pg:header>
	            <th align=center><input id="CKA" name="CKA" type="checkbox" onClick="checkAll('CKA','CK')"></th>
	       		<th>业务主题</th>
	       		<th>流程key</th>
	       		<th>业务类型</th>
	       		<th>版本</th>
	       		<th>流程实例ID</th>
	       		<th>父流程实例ID</th>
	       		<th>发起人</th>
	       		<th>当前节点</th>
	       		<th>处理人</th>
	       		<th>签收人</th>
	       		<th>开启时间</th>
	       		<th>结束时间</th>
	       		<th>耗时</th>
	       		<th>状态</th>
	       		<th>备注</th>
	       		<th><pg:message code="sany.pdp.common.operation"/></th>
	       	</pg:header>
	
	      <pg:list >
	      	<pg:equalandlower length="taskList" value="1">
		   		<tr>
		   			<td class="td_center" >
		            	<input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="ID_" />"/>
		                <input id="PROC_DEF_ID_" type="hidden" name="PROC_DEF_ID_" value="<pg:cell colName="PROC_DEF_ID_" />"/>
		                <input id="PROC_INST_ID_" type="hidden" name="PROC_INST_ID_" value="<pg:cell colName="PROC_INST_ID_" />"/>
		                <input id="END_TIME" type="hidden" name="END_TIME" value="<pg:cell colName="END_TIME_" />"/>
		            </td>
		            <td ><pg:cell colName="BUSINESS_KEY_" /></td> 
		            <td ><pg:cell colName="KEY_" /></td> 
		            <td ><pg:cell colName="BUSINESS_NAME" /></td> 
		            <td ><pg:cell colName="VERSION_" /></td>
		            <td ><pg:cell colName="PROC_INST_ID_" /></td>  
		            <td ><pg:cell colName="SUPER_PROCESS_INSTANCE_ID_" /></td> 
		        	<td ><pg:cell colName="START_USER_ID_NAME" /></td>  
		           	<td >
		           		<span style=" color: purple;">
		           			<pg:notnull colName="END_TIME_" >
		           				流程结束
		           			</pg:notnull>
						    <pg:null colName="END_TIME_" >
						    	<pg:list colName="taskList" position="0" >
						           	<pg:cell colName="NAME_"/></br>
						        </pg:list>
						    </pg:null>	
						 </span>
		           	</td>
		           	<td>
		           		<span style=" color: purple;">
				           	<pg:notempty colName="taskList" >
						       <pg:list colName="taskList" position="0">
								  <pg:cell colName="USER_ID_NAME" />
						       </pg:list>
					        </pg:notempty>	
				        </span>
		           	</td>
		           	<td>
		           		<span style=" color: purple;">
				           	<pg:notempty colName="taskList" >
					           	<pg:list colName="taskList" position="0">
					           		<pg:cell colName="ASSIGNEE_NAME" />
						        </pg:list>
					        </pg:notempty>
					    </span>
		           	</td>
		            <td><pg:cell colName="START_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>    
		            <td><pg:cell colName="END_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
		            <td><pg:cell colName="DURATION_" /></td>
		            <td>
		            	<pg:notnull colName="END_TIME_" >
		           			<span style=" color: blue;">完成</span>
		           		</pg:notnull>
		           		<pg:null colName="END_TIME_" >
		           			<pg:equal colName="SUSPENSION_STATE_" value="1">
		           				进行中
		           			</pg:equal>
		           			<pg:equal colName="SUSPENSION_STATE_" value="2">
		           				<span style=" color: red;">挂起</span>
		           			</pg:equal>
		           		</pg:null>
		            </td>
		            <td>
		            	<pg:notnull colName="END_TIME_" >
		            		<pg:empty colName="DELETE_REASON_" >
		           				正常完成
		            		</pg:empty>
		            		<pg:notempty colName="DELETE_REASON_" >
		           				<pg:cell colName="DELETE_REASON_" />
		            		</pg:notempty>
		           		</pg:notnull>
		           		<pg:null colName="END_TIME_" >
		           			<pg:cell colName="DELETE_REASON_" />
		           		</pg:null>
		            </td>
		            
		            <td class="td_center">
		            	<pg:notempty colName="SUSPENSION_STATE_" >
			             	<pg:equal colName="SUSPENSION_STATE_" value="1">
			                	<a href="javascript:void(0)" onclick="suspendProcess('<pg:cell colName="PROC_INST_ID_" />')">挂起</a>|
							</pg:equal>
							<pg:equal colName="SUSPENSION_STATE_" value="2">
			                	<a href="javascript:void(0)" onclick="activateProcess('<pg:cell colName="PROC_INST_ID_" />')">激活</a>|
							</pg:equal>
							<a href="javascript:void(0)" onclick="toUdpNodeAssignee('<pg:cell colName="KEY_" />','<pg:cell colName="PROC_INST_ID_" />')">调整</a>|
						</pg:notempty>
		                <a href="javascript:void(0)" onclick="viewDetailInfo('<pg:cell colName="PROC_INST_ID_" />')">详情</a>
		            </td>    
		        </tr>
		 	</pg:equalandlower>
		 	<pg:upper length="taskList" value="1">
		 		<tr>
		   			<td class="td_center" rowspan="<pg:size colName="taskList"/>">
		            	<input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="ID_" />"/>
		                <input id="PROC_DEF_ID_" type="hidden" name="PROC_DEF_ID_" value="<pg:cell colName="PROC_DEF_ID_" />"/>
		                <input id="PROC_INST_ID_" type="hidden" name="PROC_INST_ID_" value="<pg:cell colName="PROC_INST_ID_" />"/>
		                <input id="END_TIME" type="hidden" name="END_TIME" value="<pg:cell colName="END_TIME_" />"/>
		            </td>
		            <td rowspan="<pg:size colName="taskList"/>"><pg:cell colName="BUSINESS_KEY_" /></td> 
		            <td rowspan="<pg:size colName="taskList"/>"><pg:cell colName="KEY_" /></td> 
		            <td rowspan="<pg:size colName="taskList"/>"><pg:cell colName="BUSINESS_NAME" /></td> 
		            <td rowspan="<pg:size colName="taskList"/>"><pg:cell colName="VERSION_" /></td>
		            <td rowspan="<pg:size colName="taskList"/>"><pg:cell colName="PROC_INST_ID_" /></td>  
		            <td rowspan="<pg:size colName="taskList"/>"><pg:cell colName="SUPER_PROCESS_INSTANCE_ID_" /></td> 
		        	<td rowspan="<pg:size colName="taskList"/>"><pg:cell colName="START_USER_ID_NAME" /></td>       
					<pg:notempty colName="taskList" >
						<pg:list colName="taskList" position="0" >
				           	<td ><span style=" color: purple;"><pg:cell colName="NAME_"/></span></td>
				           	<td><span style=" color: purple;"><pg:cell colName="USER_ID_NAME"/></span></td>
				           	<td><span style=" color: purple;"><pg:cell colName="ASSIGNEE_NAME" /></span></td>
			           	</pg:list>
		           	</pg:notempty>
		            <td rowspan="<pg:size colName="taskList"/>"><pg:cell colName="START_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>    
		            <td rowspan="<pg:size colName="taskList"/>"><pg:cell colName="END_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
		            <td rowspan="<pg:size colName="taskList"/>"><pg:cell colName="DURATION_" /></td>
		            <td rowspan="<pg:size colName="taskList"/>">
		           	 	<pg:notnull colName="END_TIME_" >
		           			<span style=" color: blue;">完成</span>
		           		</pg:notnull>
		           		<pg:null colName="END_TIME_" >
		           			<pg:equal colName="SUSPENSION_STATE_" value="1">
		           				进行中
		           			</pg:equal>
		           			<pg:equal colName="SUSPENSION_STATE_" value="2">
		           				<span style=" color: red;">挂起</span>
		           			</pg:equal>
		           		</pg:null>
		            </td>
		            <td rowspan="<pg:size colName="taskList"/>">
		            	<pg:notnull colName="END_TIME_" >
		            		<pg:empty colName="DELETE_REASON_" >
		           				正常完成
		            		</pg:empty>
		            		<pg:notempty colName="DELETE_REASON_" >
		           				<pg:cell colName="DELETE_REASON_" />
		            		</pg:notempty>
		           		</pg:notnull>
		           		<pg:null colName="END_TIME_" >
		           			<pg:cell colName="DELETE_REASON_" />
		           		</pg:null>
		            </td>
		            
		            <td class="td_center" rowspan="<pg:size colName="taskList"/>">
		            	<pg:notempty colName="SUSPENSION_STATE_" >
			             	<pg:equal colName="SUSPENSION_STATE_" value="1">
			                	<a href="javascript:void(0)" onclick="suspendProcess('<pg:cell colName="PROC_INST_ID_" />')">挂起</a>|
							</pg:equal>
							<pg:equal colName="SUSPENSION_STATE_" value="2">
			                	<a href="javascript:void(0)" onclick="activateProcess('<pg:cell colName="PROC_INST_ID_" />')">激活</a>|
							</pg:equal>
							<a href="javascript:void(0)" onclick="toUdpNodeAssignee('<pg:cell colName="KEY_" />','<pg:cell colName="PROC_INST_ID_" />')">调整</a>|
						</pg:notempty>
		                <a href="javascript:void(0)" onclick="viewDetailInfo('<pg:cell colName="PROC_INST_ID_" />')">详情</a>
		            </td>    
		        </tr>
		        
		        
		         <pg:list colName="taskList" start="1">
			        <tr><input id="CK" type="hidden" name="CK" value=""/>
			           	<td><span style=" color: purple;"><pg:cell colName="NAME_"/></span></td>
			           	<td><span style=" color: purple;"><pg:cell colName="USER_ID_NAME" /></span></td>
			           	<td><span style=" color: purple;"><pg:cell colName="ASSIGNEE_NAME" /></span></td>
			        </tr>
		        </pg:list>
			</pg:upper>
		 </pg:list>
	    </table>
	    </div>
		<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
	
	    </pg:pager>
	</pg:notempty >

</div>	
