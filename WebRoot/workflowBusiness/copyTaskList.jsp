<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<link href="${pageContext.request.contextPath}/html3/stylesheet/basic.css" rel="stylesheet" type="text/css" />

<pg:empty actual="${hiCopyTaskList}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 

<pg:notempty actual="${hiCopyTaskList}" >
   <pg:pager scope="request" data="hiCopyTaskList" desc="true" isList="false" >
   
   <pg:param name="actinstid"/>
   
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
        <pg:header>
			<th>已阅时间</th>
       		<th>已阅人</th>
       		<th>节点名称</th>
       	</pg:header>	

      <pg:list >
   		<tr >
   			<td><pg:cell colName="copytime" dateformat="yyyy-MM-dd HH:mm:ss"/></td> 
            <td><pg:cell colName="coperCNName" /></td> 
    		<td><pg:cell colName="act_name" /></td>  
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
</pg:notempty> 


