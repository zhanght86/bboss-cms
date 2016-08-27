<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：已读抄送任务列表
版本：1.0
日期：2014-11-17
 --%>	

<div id="hiCopyContent">
<pg:empty actual="${hiCopyTaskList}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${hiCopyTaskList}" >
   <pg:pager scope="request" data="hiCopyTaskList" desc="true" isList="false" containerid="hiCopyContainer" selector="hiCopyContent">
   
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
       	 	<th>抄送任务ID</th>
			<th>业务主题</th>
       		<th>流程key</th>
       		<th>流程实例ID</th>
       		<th>活动任务ID</th>
       		<th>节点名称</th>
			<th>已阅人</th>
			<th>抄送时间</th>
			<th>已阅时间</th>
			<th>操作</th>
       	</pg:header>	

      <pg:list >
   		<tr >
   			<td><pg:cell colName="id" /></td>       
            <td><pg:cell colName="businesskey" /></td> 
    		<td><pg:cell colName="process_key" /></td> 
    		<td><pg:cell colName="process_id" /></td>  
    		<td><pg:cell colName="act_instid" /></td>
    		<td><pg:cell colName="act_name" /></td>
    		<td><pg:cell colName="coperCNName" /></td> 
    		<td><pg:cell colName="copytime" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
    		<td><pg:cell colName="readtime" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
    		<td class="td_center">
            	<a href="javascript:void(0)" id="viewTaskDetailInfo" onclick="viewDetailInfo('<pg:cell colName="process_id" />')">详情</a>
            </td>     
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
   </pg:notempty> 

</div>		
