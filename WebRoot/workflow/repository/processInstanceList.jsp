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
		<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
		<div id="changeColor">
		 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb" sytle="background-color: red;">
	        <pg:header>
	            <th align=center><input id="CKA" name="CKA" type="checkbox" onClick="checkAll('CKA','CK')"></th>
	       		<th>业务主键</th>
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
	       		<th><pg:message code="sany.pdp.common.operation"/></th>
	       	</pg:header>	
	
	      <pg:list >
	   		<tr>
	   			<td class="td_center">
	            	<input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="ID_" />"/>
	                <input id="PROC_DEF_ID_" type="hidden" name="PROC_DEF_ID_" value="<pg:cell colName="PROC_DEF_ID_" />"/></td>
	                <input id="PROC_INST_ID_" type="hidden" name="PROC_INST_ID_" value="<pg:cell colName="PROC_INST_ID_" />"/></td>
	                <input id="END_TIME" type="hidden" name="END_TIME" value="<pg:cell colName="END_TIME_" />"/></td>
	            <td><pg:cell colName="BUSINESS_KEY_" /></td> 
	            <th><pg:cell colName="VERSION_" /></th>
	            <td><pg:cell colName="PROC_INST_ID_" /></td>  
	            <td>
			    	<a href="javascript:void(0)" id="viewProcessInfo" onclick="viewDetailInfo('<pg:cell colName="SUPER_PROCESS_INSTANCE_ID_" />')">
			           <pg:cell colName="SUPER_PROCESS_INSTANCE_ID_" />
			        </a>
	            </td> 
	        	<td><pg:cell colName="START_USER_ID_" /></td>       
	           	<td>
		           	<pg:empty colName="taskList" >
				           		流程结束
				    </pg:empty>
				    <pg:notempty colName="taskList" >
				    	<pg:list colName="taskList">
				           	<pg:cell index="1" colName="NAME_"/></br>
				        </pg:list>
				    </pg:notempty>	
	           	</td>
	           	<td>
		           	<pg:notempty colName="taskList" >
				       <pg:list colName="taskList">
						   <pg:cell index="1" colName="USER_ID_"/></br>
				       </pg:list>
			        </pg:notempty>	
	           	</td>
	           	<td>
		           	<pg:notempty colName="taskList" >
			           	<pg:list colName="taskList">
						   <pg:cell index="1" colName="ASSIGNEE_"/></br>
				        </pg:list>
			        </pg:notempty>
	           	</td>
	            <td><pg:cell colName="START_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>    
	            <td><pg:cell colName="END_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
	            <td><pg:cell colName="DURATION_" /></td>
	            <td>
	            	<pg:empty colName="SUSPENSION_STATE_" >
	            		<pg:notempty colName="DELETE_REASON_" >
	           				<pg:cell colName="DELETE_REASON_" />
	            		</pg:notempty>
	            		<pg:empty colName="DELETE_REASON_" >
	           				<span style=" color: blue;">完成</span>
	            		</pg:empty>
	           		</pg:empty>
	           		<pg:notempty colName="SUSPENSION_STATE_" >
	           			<pg:equal colName="SUSPENSION_STATE_" value="1">
	           				激活
	           			</pg:equal>
	           			<pg:equal colName="SUSPENSION_STATE_" value="2">
	           				<span style=" color: red;">挂起</span>
	           			</pg:equal>
	           		</pg:notempty>
	            </td>
	            
	            <td class="td_center">
	            	<pg:notempty colName="SUSPENSION_STATE_" >
		             	<pg:equal colName="SUSPENSION_STATE_" value="1">
		                	<a href="javascript:void(0)" id="viewProcessInfo" onclick="suspendProcess('<pg:cell colName="PROC_INST_ID_" />')">挂起</a>|
						</pg:equal>
						<pg:equal colName="SUSPENSION_STATE_" value="2">
		                	<a href="javascript:void(0)" id="viewProcessInfo" onclick="activateProcess('<pg:cell colName="PROC_INST_ID_" />')">激活</a>|
						</pg:equal>
					</pg:notempty>
	                <a href="javascript:void(0)" id="viewProcessInfo" onclick="viewDetailInfo('<pg:cell colName="PROC_INST_ID_" />')">查看详情</a>
	            </td>    
	        </tr>
		 </pg:list>
	    </table>
	    </div>
		<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
	
	    </pg:pager>
	</pg:notempty >

</div>	