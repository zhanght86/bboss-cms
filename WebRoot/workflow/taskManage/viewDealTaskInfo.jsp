<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
描述：处理记录明细
作者：谭湘
版本：1.0
日期：2014-07-24
 --%>	

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table3" id="tb">
	<pg:header>
       	<th>节点名</th>
       	<th>任务到达时间</th>
       	<th>任务签收时间</th>
       	<th>任务处理时间</th>
       	<th>处理工时</th>
       	<th>工时规则</th>
       	<th>耗时</th>
       	<th>预警时间</th>
       	<th>预警提醒状况</th>
       	<th>超时时间</th>
       	<th>超时提醒状况</th>
       	<th>处理人</th>
       	<th>签收人</th>
       	<th>已阅人</th>
       	<th>处理操作</th>
       	<th>处理意见</th>
       	<th>备注</th>
      </pg:header>	
	<pg:list requestKey="taskHistorList">
		<pg:in colName="ACT_TYPE_" scope="startEvent,endEvent,userTask">
		   	<tr height="25px">
		    	<td><pg:cell colName="ACT_NAME_" /></td>     
		    	<td><pg:cell colName="START_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>  
		    	<td><pg:cell colName="CLAIM_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>   
		    	<td><pg:cell colName="END_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>  
		    	<td><pg:cell colName="DURATION_NODE" /></td>
	       		<td>
	       			<pg:equal colName="IS_CONTAIN_HOLIDAY" value="0">
			           	不考虑节假日/作息时间
			        </pg:equal>
	       			<pg:equal colName="IS_CONTAIN_HOLIDAY" value="1">
			           	考虑节假日,不考虑作息时间
			        </pg:equal>
			        <pg:equal colName="IS_CONTAIN_HOLIDAY" value="2">
			           	考虑节假日/作息时间
			        </pg:equal>
	       		</td>
	       		<td><pg:cell colName="DURATION_" /></td>
	       		<td>
	       			<pg:notempty colName="ALERTTIME" >
	       				<span 
	       				<pg:equal colName="isAlertTime" value="1">style=" color: red";</pg:equal>
	       				>
			          		<pg:cell colName="ALERTTIME" dateformat="yyyy-MM-dd HH:mm:ss"/>
			          	</span>
			        </pg:notempty>
			    </td>
			    <td>
			    	<pg:notempty colName="ALERTTIME" >
				    	<pg:convert convertData="advanceSendMap" colName="advancesend"/> 
			    	</pg:notempty>
			    </td>
	       		<td>
	       			<pg:notempty colName="OVERTIME" >
	       				<span 
	       				<pg:equal colName="isOverTime" value="1">style=" color: red";</pg:equal>
	       				>
			           		<pg:cell colName="OVERTIME" dateformat="yyyy-MM-dd HH:mm:ss"/>
			           	</span>
			        </pg:notempty>
			    </td>
			    <td>
			    	<pg:notempty colName="OVERTIME" >
				    	<pg:convert convertData="overtimeSendMap" colName="overtimesend"/> 
			    	</pg:notempty>
			    </td>
		    	<td>
	    			<pg:null colName="delegateTaskList" >
		       			<pg:cell colName="USER_ID_NAME"/>
	       			</pg:null>
	       			<pg:notnull colName="delegateTaskList" >
				     	<pg:list colName="delegateTaskList" >
						  <pg:cell colName="FROM_USER_NAME"/>
						     委托给
						  <pg:cell colName="TO_USER_NAME"/>
						  [<pg:cell colName="CHANGETIME" dateformat="yyyy-MM-dd HH:mm:ss"/>]
						  <br/>
				       </pg:list>
			        </pg:notnull>	
		    	</td>  
		    	<td>
					<pg:cell colName="ASSIGNEE_NAME"/>
				</td>  
				<td>
					<pg:cell colName="readedCopyTaskNames"/>
				</td>  
		    	<td>
					 <pg:empty colName="BUSSINESS_OP" >&nbsp;</pg:empty>
					 <pg:notempty colName="BUSSINESS_OP" >
					 	<pg:cell colName="BUSSINESS_OP" />
					 </pg:notempty>
				</td> 
				<td>
					 <pg:empty colName="BUSSINESS_REMARK" >&nbsp;</pg:empty>
					 <pg:notempty colName="BUSSINESS_REMARK" >
					 	<pg:cell colName="BUSSINESS_REMARK" />
					 </pg:notempty>
				</td> 
				<td>
		    	 <pg:empty colName="DELETE_REASON_" >
		    	 	<pg:equal colName="IS_AUTO_COMPLETE" value="0">
			    	 	<pg:equal colName="ACT_TYPE_" value="startEvent">流程开启</pg:equal>
			    	 	<pg:equal colName="ACT_TYPE_" value="endEvent">流程结束</pg:equal>
			    		<pg:cell colName="ACT_NAME_" />
		    		</pg:equal>
		    		<pg:equal colName="IS_AUTO_COMPLETE" value="1">
			    		系统自动完成任务
			    		<pg:notempty colName="AUTO_HANDLER">
			    			<br/> 业务处理类：<pg:cell colName="AUTO_HANDLER" />
			    		</pg:notempty>
		    		</pg:equal>
		    	 </pg:empty>
		    	 <pg:notempty colName="DELETE_REASON_" >
		    		<pg:cell colName="DELETE_REASON_" />
		    	 </pg:notempty>
		    	</td> 
		    </tr>
	    </pg:in>
	 </pg:list>
</table>
