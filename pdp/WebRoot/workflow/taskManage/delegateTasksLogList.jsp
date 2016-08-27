<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="delegateContent">
<pg:empty actual="${listInfo}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${listInfo}" >
   <pg:pager scope="request"  data="listInfo" desc="true" isList="false" containerid="delegateContainer" selector="delegateContent">
   
    <pg:param name="processKey"/>
    <pg:param name="fromUser"/>
    <pg:param name="toUser"/>
    <pg:param name="delegateTime_from"/>
    <pg:param name="delegateTime_to"/>
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
		 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
	        <pg:header>
	       		<th>流程key</th>
	       		<th>流程实例ID</th>
				<th>任务ID</th>
	       		<th>转派人</th>
	       		<th>被转派人</th>
	       		<th>转派时间</th>
	       		<th>备注</th>
	       	</pg:header>	
	
	      	<pg:list >
		   		<tr >
		            <td><pg:cell colName="processKey" /></td> 
		    		<td><pg:cell colName="processId" /></td> 
		    		<td><pg:cell colName="taskId" /></td>      
		    		<td><pg:cell colName="fromUser" /></td>  
		    		<td><pg:cell colName="toUser" /></td> 
		       		<td><pg:cell colName="delegateTime" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
		       		<td><pg:cell colName="delegateRemark"/></td>
		        </tr>
		 	</pg:list>
	    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

   </pg:pager>
</pg:notempty> 

</div>		
