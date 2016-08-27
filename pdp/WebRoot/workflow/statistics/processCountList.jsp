<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：流程统计数据
作者：谭湘
版本：1.0
日期：2014-08-13
 --%>	

<div id="processCountContent">
<pg:empty actual="${listInfo}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${listInfo}" >
   <pg:pager scope="request"  data="listInfo" desc="true" isList="false" containerid="processCountContainer" selector="processCountContent">
   
    <pg:param name="app"/>
    <pg:param name="count_start_time"/>
    <pg:param name="count_end_time"/>
    <pg:param name="businessType"/>
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
			<th>流程名称</th>
       		<th>总数</th>
       		<th>通过</th>
       		<th>待审</th>
			<th>驳回</th>
       		<th>撤销</th>
       		<th>废弃</th>
       		<th>操作</th>
       	</pg:header>	

      <pg:list >
   		<tr >
            <td><pg:cell colName="processName" /></td> 
    		<td><pg:cell colName="processNum" /></td> 
    		<td><pg:cell colName="passNum" /></td>      
    		<td><pg:cell colName="waitNum" /></td>  
    		<td><pg:cell colName="rejectNum" /></td> 
       		<td><pg:cell colName="cancelNum" /></td>
       		<td><pg:cell colName="discardNum"/></td>
            <td class="td_center">
             	<a href="javascript:void(0)" id="viewDetailInfo" onclick="viewDetailInfo('<pg:cell colName="processKey" />','<pg:cell colName="processName" />')">明细</a>|
            	<a href="javascript:void(0)" id="efficiencyAnalyse" onclick="efficiencyAnalyse('<pg:cell colName="processKey" />','<pg:cell colName="processName" />')">效率分析</a>
            </td>    
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
   </pg:notempty> 

</div>		
