<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="customContent">
<pg:equal actual="${pageBrowserCounterGatherDataList.totalSize}" value="0" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}/html/images/no_data.jpg"/></div>
</pg:equal> 
<pg:notequal actual="${pageBrowserCounterGatherDataList.totalSize}"  value="0">
   <pg:pager scope="request"  data="pageBrowserCounterGatherDataList" desc="true" isList="false" containerid="custombackContainer" selector="customContent">
	<pg:param name="siteId"/>
	<pg:param name="channelId"/>
	<pg:param name="docName"/>
	<pg:param name="pageUrl"/>
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
       	 	<th>应用名称</th> 
       		<th>菜单路径</th>
       		<th style="display: none">页面名称</th>
       		<th>浏览页面地址</th>
       		<th>浏览次数</th>
       	</pg:header>	

      <pg:list autosort="false">
   		<tr>     
   		        <td align="center"><pg:cell colName="siteName"/></td>
                <td><pg:cell colName="channelName"/></td>
                <td style="display: none"><pg:cell colName="docName"/></td>
        		<td><span class="toolTip" title="<pg:cell colName="pageURL"/>"><pg:cell colName="pageURL" maxlength="50" replace="..."/></span></td>  
        		<td><pg:cell colName="browserCount" /></td>  
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
</pg:notequal>
</div>		
