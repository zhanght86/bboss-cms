<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：个人统计数据
作者：谭湘
版本：1.0
日期：2014-08-13
 --%>	

<div id="personalCountContent">
<pg:empty actual="${listInfo}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${listInfo}" >
   <pg:pager scope="request"  data="listInfo" desc="true" isList="false" containerid="personalCountContainer" selector="personalCountContent">
   
    <pg:param name="orgId"/>
    <pg:param name="realName"/>
    <pg:param name="count_start_time"/>
    <pg:param name="count_end_time"/>
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
			<th>所在部门</th>
       		<th>姓名</th>
       		<th>启动流程数</th>
       		<th>处理次数</th>
			<th>转办次数</th>
       		<th>被转办次数</th>
       		<th>委托次数</th>
       		<th>被委托次数</th>
       		<th>驳回次数</th>
       		<th>撤销次数</th>
       		<th>废弃次数</th>
       		<th>操作</th>
       	</pg:header>	

      <pg:list >
   		<tr >
            <td><pg:cell colName="orgName" /></td> 
    		<td><pg:cell colName="realName" /></td> 
    		<td><pg:cell colName="startNum" /></td>      
    		<td><pg:cell colName="dealNum" /></td>  
    		<td><pg:cell colName="delegateNum" /></td> 
       		<td><pg:cell colName="delegatedNum" /></td>
       		<td><pg:cell colName="entrustNum"/></td>
       		<td><pg:cell colName="entrustedNum"/></td>
       		<td><pg:cell colName="rejectNum"/></td>
       		<td><pg:cell colName="cancelNum"/></td>
       		<td><pg:cell colName="discardNum"/></td>
            <td class="td_center">
             	<a href="javascript:void(0)" id="viewDetailInfo" onclick="viewDetailInfo('<pg:cell colName="userName" />','<pg:cell colName="realName" />')">明细</a>|
            	<a href="javascript:void(0)" id="efficiencyAnalyse" onclick="efficiencyAnalyse('<pg:cell colName="realName" />','<pg:cell colName="startNum" />',
            	'<pg:cell colName="dealNum" />','<pg:cell colName="delegateNum" />','<pg:cell colName="delegatedNum" />',
            	'<pg:cell colName="entrustNum"/>','<pg:cell colName="entrustedNum"/>','<pg:cell colName="rejectNum"/>',
            	'<pg:cell colName="cancelNum"/>','<pg:cell colName="discardNum"/>')">效率分析</a>
            </td>    
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
   </pg:notempty> 

</div>		
