<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：历史任务数据
作者：谭湘
版本：1.0
日期：2014-06-18
 --%>	

<div id="customContent">
<pg:empty actual="${listInfo}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${listInfo}" >
   <pg:pager scope="request"  data="listInfo" desc="true" isList="false" containerid="historyContainer" selector="customContent">
   
    <pg:param name="processIntsId"/>
    <pg:param name="processKey"/>
    <pg:param name="taskId"/>
    <pg:param name="taskName"/>
    <pg:param name="businessTypeId"/>
    <pg:param name="businessKey"/>
    <pg:param name="createUser"/>
    <pg:param name="entrustUser"/>
    <pg:param name="appName"/>
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
        	<!--  
            <th align=center><input id="CKA" name="CKA" type="checkbox" 
							onClick="checkAll('CKA','CK')"></th>
			-->
			<th>业务主题</th>
       		<th>流程key</th>
       		<th>业务类型</th>
       		<th>流程实例ID</th>
			<th>任务ID</th>
       		<th>任务名称</th>
       		<th>处理人</th>
       		<th>任务到达时间</th>
       		<th>任务完成时间</th>
       		<th>处理工时</th>
       		<th>工时规则</th>
       		<th>耗时</th>
       		<th>预警时间</th>
       		<th>预警提醒状况</th>
       		<th>超时时间</th>
       		<th>超时提醒状况</th>
       		<th>操作</th>
       	</pg:header>	

      <pg:list >
   		<tr >
   			<!-- 
   		        <td class="td_center">
                    <input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="ID_" />"/>
            -->
            <td><pg:cell colName="BUSINESS_KEY_" /></td> 
    		<td><pg:cell colName="KEY_" /></td> 
    		<td><pg:cell colName="BUSINESS_NAME" /></td>      
    		<td><pg:cell colName="PROC_INST_ID_" /></td>  
    		<td><pg:cell colName="ID_" /></td> 
       		<td><pg:cell colName="NAME_"/></td>
       		<td>
   				<pg:null colName="delegateTaskList">
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
       		<td><pg:cell colName="START_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
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
            <td class="td_center">
            	<a href="javascript:void(0)" id="viewTaskDetailInfo" onclick="viewDetailInfo('<pg:cell colName="PROC_INST_ID_" />')">详情</a>
            </td>     
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
   </pg:notempty> 

</div>		
