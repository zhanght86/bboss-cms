<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：实时任务数据
作者：谭湘
版本：1.0
日期：2014-05-06
 --%>	

<div id="demoContent">
<pg:empty actual="${listInfo}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 

<pg:notempty actual="${listInfo}" >
   <pg:pager scope="request" data="listInfo" desc="true" isList="false" containerid="demoContainer" selector="demoContent">
   
   	<pg:param name="businessKey"/>
	<pg:param name="processKey"/>
   
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
		 <table width="100%" border="0" cellpadding="0" cellspacing="0"  class="list_table">
		     <pg:header>
				<th>序号</th>
		   		<th>业务单号</th>
		   		<th>提交人</th>
				<th>提交日期</th>
				<th>当前节点</th>
				<th>可处理人</th>
		   		<th>操作</th>
		     </pg:header>	
	
		     <pg:list >
		   		<tr >
		   			<td><pg:rowid increament="1"/></td> 
		            <td><pg:cell colName="title" /></td> 
		    		<td>
			    		<pg:empty colName="senderName">&nbsp;</pg:empty>
			    		<pg:notempty colName="senderName"><pg:cell colName="senderName" /></pg:notempty>
		    		</td> 
		    		<td>
			    		<pg:empty colName="createTime">&nbsp;</pg:empty>
			    		<pg:notempty colName="createTime"><pg:cell colName="createTime" dateformat="yyyy-MM-dd HH:mm:ss"/></pg:notempty>
		    		</td>      
		            <td>
			            <pg:empty colName="nodeName">&nbsp;</pg:empty>
			    		<pg:notempty colName="nodeName"><pg:cell colName="nodeName" /></pg:notempty>
		            </td> 
		            <td>
			            <pg:empty colName="assigneeName">&nbsp;</pg:empty>
			    		<pg:notempty colName="assigneeName"><pg:cell colName="assigneeName" /></pg:notempty>
		            </td> 
		            <td class="td_center">
		            	<a href="javascript:void(0)" onclick="dealBusiness('<pg:cell colName="businessKey" />','<pg:cell colName="businessState" />')">
		   				<pg:equal colName="businessState" value="0">申请</pg:equal>         	
		            	<pg:equal colName="businessState" value="1">处理</pg:equal>         
		            	<pg:equal colName="businessState" value="2">查看</pg:equal>  
		            	</a>
		            </td>    
		        </tr>
			 </pg:list>
	      </table>
    </div>
    
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
</pg:notempty> 

</div>		
