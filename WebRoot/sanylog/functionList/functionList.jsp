<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="customContent">
<pg:equal actual="${datas.totalSize}" value="0" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}/html/images/no_data.jpg"/></div>
</pg:equal> 
<pg:notequal actual="${datas.totalSize}"  value="0">
   <pg:pager scope="request"  data="datas" desc="true" isList="false" containerid="custombackContainer" selector="customContent">
	<pg:param name="appId"/>
	<pg:param name="functionCode"/>
	<pg:param name="functionName"/>
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<!-- <span class="toolTip" title="<pg:cell colName="pageURL"/>"><pg:cell colName="pageURL" maxlength="100" replace="..."/></span> -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
        	<th>应用名称</th> 	
       		<th >功能名称</th>
       		<th >功能编码</th>
       		<th>预计使用人数(人/月)</th>
       		<th>预计使用频次(次/月)</th>
       		<th>耗费工时(小时)</th>
       		<th>删除</th>
       		<th>修改</th>
       	</pg:header>	

      <pg:list autosort="false">
   		<tr>
   		        <td><pg:cell colName="appName"/></td>
                <td><pg:cell colName="functionName"/></td>
                <td ><pg:cell colName="functionCode"/></td>  
        		<td><pg:cell colName="estimateUser" /></td>  
        		<td><pg:cell colName="estimateOper" /></td>  
        		<td><pg:cell colName="spentTime" /></td>
        		<td><a href="#" onclick="deleteRecord('<pg:cell colName="id" />')">删除</a></td>
        		<td><a href="#" onclick="modifyOrIncrementRecord('<pg:cell colName="id" />','modify')">修改</a></td>
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
</pg:notequal>
</div>		
