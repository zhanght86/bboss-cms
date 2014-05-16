<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：实时任务数据
作者：谭湘
版本：1.0
日期：2014-05-06
 --%>	

<div id="customContent">
<pg:empty actual="${listInfo}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${listInfo}" >
   <pg:pager scope="request"  data="listInfo" desc="true" isList="false" containerid="ontimeContainer" selector="customContent">
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
            <th align=center><input id="CKA" name="CKA" type="checkbox" 
							onClick="checkAll('CKA1','CK')"></th>
			<th>任务ID</th>
       		<th>流程实例ID</th>
       		<th>任务名称</th>
       		<th>签收人</th>
       		<th>任务到达时间</th>
       		<th>操作</th>
       	</pg:header>	

      <pg:list >
   		<tr >
   		        <td class="td_center">
                    <input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="ID_" />"/>
        		<td><pg:cell colName="ID_" /></td> 
        		<td><pg:cell colName="PROC_INST_ID_" /></td>       
           		<td><pg:cell colName="NAME_"/></td>
           		<td><pg:cell colName="ASSIGNEE_"/></td>
           		<td><pg:cell colName="CREATE_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
                <td class="td_center">
	                <pg:equal colName="state" value="1">
	                	<a href="javascript:void(0)" id="viewTaskDetailInfo" onclick="signTask('<pg:cell colName="ID_" />')">签收</a>|
	                	<a href="javascript:void(0)" id="viewTaskDetailInfo" onclick="doTask('<pg:cell colName="ID_" />','<pg:cell colName="state" />')">处理</a>|
					</pg:equal>
					<pg:equal colName="state" value="2">
	                	<a href="javascript:void(0)" id="viewTaskDetailInfo" onclick="doTask('<pg:cell colName="ID_" />','<pg:cell colName="state" />')">处理</a>|
					</pg:equal>
                	<a href="javascript:void(0)" id="viewTaskDetailInfo" onclick="viewDetailInfo('<pg:cell colName="PROC_INST_ID_" />')">查看详情</a>
                </td>    
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
   </pg:notempty> 

</div>		
