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
		
		<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
		<div id="changeColor">
		 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
	        <pg:header>
	            <th align=center><input id="CKA" name="CKA" type="checkbox" onClick="checkAll('CKA','CK')"></th>
	       		<th>流程实例ID</th>
	       		<th>版本</th>
	       		<th>发起人</th>
	       		<th>当前节点</th>
	       		<th>签收人</th>
	       		<th>开启时间</th>
	       		<th>结束时间</th>
	       		<th>备注</th>
	       		<th><pg:message code="sany.pdp.common.operation"/></th>
	       	</pg:header>	
	
	      <pg:list >
	   		<tr >
	   			<td class="td_center">
	            	<input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="ID_" />"/>
	                <input id="PROC_DEF_ID_" type="hidden" name="PROC_DEF_ID_" value="<pg:cell colName="PROC_DEF_ID_" />"/></td>
	                <input id="PROC_INST_ID_" type="hidden" name="PROC_INST_ID_" value="<pg:cell colName="PROC_INST_ID_" />"/></td>
	                <input id="END_TIME" type="hidden" name="END_TIME" value="<pg:cell colName="END_TIME_" />"/></td>
	            <td><pg:cell colName="PROC_INST_ID_" /></td>  
	            <th><pg:cell colName="VERSION_" /></th>
	        	<td><pg:cell colName="START_USER_ID_" /></td>       
	           	<td><pg:cell colName="NAME_"/></td>
	           	<td><pg:cell colName="ASSIGNEE_"/></td>
	            <td><pg:cell colName="START_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>    
	            <td><pg:cell colName="END_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
	            <td class="toolTip"  title="<pg:cell colName="REMARK"/>"><pg:cell colName="REMARK" maxlength="10" replace="..."/></td>
	            <td class="td_center">
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