<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="customContent">
<pg:equal actual="${browserCounterDataList.totalSize}" value="0" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}/html/images/no_data.jpg"/></div>
</pg:equal> 
<pg:notequal actual="${browserCounterDataList.totalSize}"  value="0">
   <pg:pager scope="request"  data="browserCounterDataList" desc="true" isList="false" containerid="custombackContainer" selector="customContent">
	
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
       		<th>文档名称</th>
       		<th>频道名称</th>
       		<th>站点名称</th>
       		<th>浏览页面地址</th>
       		<th>浏览来源地址</th>
       		<th>浏览器类型</th>
       		<th>用户</th>
       		<th>用户IP地址</th>
       		<th>浏览时间</th>
       	</pg:header>	

      <pg:list autosort="false">
   		<tr>
                <td><span class="toolTip" title="<pg:cell colName="docName"/>"><pg:cell colName="docName" maxlength="40" replace="..."/></span></td>
                <td><span class="toolTip" title="<pg:cell colName="channelName"/>"><pg:cell colName="channelName" maxlength="15" replace="..."/></span></td>
                <td><pg:cell colName="siteName"/></td>
        		<td><a href="<pg:cell colName="pageURL"/>" target="_blank"><span class="toolTip" title="<pg:cell colName="pageURL"/>"><pg:cell colName="pageURL" maxlength="20" replace="..."/></span></a></td>  
        		<td><a href="<pg:cell colName="referer"/>" target="_blank"><span class="toolTip" title="<pg:cell colName="referer"/>"><pg:cell colName="referer" maxlength="20" replace="..."/></span></a></td>  
        		<td><pg:cell colName="browserType" /></td>  
        		<td><pg:cell colName="browserUser" /></td>  
        		<td><pg:cell colName="browserIp" /></td>  
        		<td><pg:cell colName="browserTime"  dateformat="yyyy-MM-dd  HH:mm:ss"/></td>  
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
</pg:notequal>
</div>		
