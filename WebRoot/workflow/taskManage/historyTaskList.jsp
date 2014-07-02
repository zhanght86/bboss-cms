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
       		<th>耗时</th>
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
       		<td><pg:cell colName="ASSIGNEE_"/></td>
       		<td><pg:cell colName="START_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
            <td><pg:cell colName="END_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td> 
            <td><pg:cell colName="DURATION_NODE" /></td> 
            <td>
            	<pg:notempty colName="isOverTime" >
		           <pg:equal colName="isOverTime" value="0">
		           		<pg:cell colName="DURATION_" />
		           </pg:equal>
		           <pg:equal colName="isOverTime" value="1">
		           		<span style="color: red;"><pg:cell colName="DURATION_" /></span>
		           	</pg:equal>
		        </pg:notempty>
		       	<pg:empty colName="isOverTime" >
		       		<pg:cell colName="DURATION_" />
		       	</pg:empty>
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
