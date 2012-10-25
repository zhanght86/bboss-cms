<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="customContent">
<pg:equal actual="${operateCounterDataList.totalSize}" value="0" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}/html/images/no_data.jpg"/></div>
</pg:equal> 
<pg:notequal actual="${operateCounterDataList.totalSize}"  value="0">
   <pg:pager scope="request"  data="operateCounterDataList" desc="true" isList="false" containerid="custombackContainer" selector="customContent">
	<pg:param name="siteId"/>
		<pg:param name="moduleId"/>
		<pg:param name="pageName"/>
		<pg:param name="browserType"/>
		<pg:param name="operateIp"/>
		<pg:param name="operator"/>
		<pg:param name="startTime"/>
		<pg:param name="endTime"/>
		<pg:param name="operation"/>
		<pg:param name="operContent"/>
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
        	<th>应用名称</th> 	
       		<th>模块名称</th>
       		<th>页面名称</th>
       		<th>页面URL</th>
       		<th>操作人</th>
       		<th>操作类型</th>
       		<th>来源页面</th>
       		<th>操作时间</th>
       		<th>操作IP</th>
       		<th>浏览器类型</th>
       		<th>查看明细</th>
       	</pg:header>	

      <pg:list autosort="false">
   		<tr>
                <td><pg:cell colName="appName"/></td>    
                <td><pg:cell colName="moduleName"/></td> 
                <td><pg:cell colName="pageName"/></td>    
        		<td><pg:cell colName="pageURL" maxlength="40" replace="..."/></td>  
        		<td><pg:cell colName="operator" /></td>  
        		<td><pg:cell colName="operation" /></td>                          
        		<td><pg:cell colName="referer" maxlength="40" replace="..."/></td>                          
        		<td><pg:cell colName="operTime" dateformat="yyyy-MM-dd  HH:mm:ss"/></td>
        		<td><pg:cell colName="operateIp" /></td>                             
        		<td><pg:cell colName="browserType"  /></td>
        		<td><a href="#" onclick="checkOperateDetail('<pg:cell colName="operateId" />')">查看</a></td>  
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
</pg:notequal>
</div>		
