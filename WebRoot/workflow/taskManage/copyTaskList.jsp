<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：抄送任务列表
版本：1.0
日期：2014-11-14
 --%>	

<div id="copyContent">
<pg:empty actual="${copyTaskList}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${copyTaskList}" >
   <pg:pager scope="request" data="copyTaskList" desc="true" isList="false" containerid="copyContainer" selector="copyContent">
   
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
       		<th>抄送对象</th>
			<th>对象名称</th>
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
    		<td>
    		<pg:equal colName="copertype" value="0">用户</pg:equal>
    		<pg:equal colName="copertype" value="1">部门</pg:equal>
    		</td>  
    		<td><pg:cell colName="coperCNName" /></td>  
    		<td><a href="javascript:void(0)" id="" onclick="viewCopyTask('<pg:cell colName="process_id" />','<pg:cell colName="id" />')">查看</a></td>
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
   </pg:notempty> 

</div>		
