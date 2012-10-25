<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="customContent">
<pg:equal actual="${browserCounterDataList.totalSize}" value="0" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}/html/images/no_data.jpg"/></div>
</pg:equal> 
<pg:notequal actual="${browserCounterDataList.totalSize}"  value="0">
   <pg:pager scope="request"  data="browserCounterDataList" desc="true" isList="false" containerid="custombackContainer" selector="customContent">
	<pg:param name="siteId"/>
	<pg:param name="channelId"/>
	<pg:param name="docName"/>
	<pg:param name="browserType"/>
	<pg:param name="browserIp"/>
	<pg:param name="startTime"/>
	<pg:param name="endTime"/>
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
        	<th>应用名称</th> 	
       		<th>模块名称</th>
       		<th>页面名称</th>
       		<th>浏览页面地址</th>
       		<th>浏览来源地址</th>
       		<th>浏览器类型</th>
       		<th>用户</th>
       		<th>用户IP地址</th>
       		<th>浏览时间</th>
       	</pg:header>	

      <pg:list autosort="false">
   		<tr>
   		        <td><pg:cell colName="siteName"/></td>
                <td><pg:cell colName="channelName"/></td>
                <td><pg:cell colName="docName"/></td>  
        		<td><pg:cell colName="pageURL" maxlength="40" replace="..."/></td>  
        		<td><pg:cell colName="referer" maxlength="40" replace="..."/></td>  
        		<td><pg:cell colName="browserType" /></td>  
        		<td><pg:cell colName="browserUser" /></td>  
        		<td><pg:cell colName="browserIp" /></td>  
        		<td><pg:cell colName="browserTime"  dateformat="yyyy-MM-dd  HH:mm:ss"/></td>  
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
</pg:notequal>
</div>		
