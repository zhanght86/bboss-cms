<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="customContent">
<%-- <pg:equal actual="${datas.size}" value="0" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}/html/images/no_data.jpg"/></div>
</pg:equal> 
<pg:notequal actual="${datas.size}"  value="0">
   <pg:pager scope="request"  data="datas" desc="true" isList="false" containerid="custombackContainer" selector="customContent">
	<pg:param name="siteId"/>
	<pg:param name="channelId"/>
	<pg:param name="docName"/>
	<pg:param name="pageUrl"/>
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
</pg:notequal> --%>
<div align="center">
			<strong style="font-size:20px; content:center;">${startTime}至${endTime}<font style="color:red">功能点使用人数统计表</font></strong>
	</div>
<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb"><!--   控制页面大小   -->
        <pg:header>
       	 	<th>系统名称</th> 
       		<th>1人</th>
       		<th>2-5人</th>
       		<th>6-10人</th>
       		<th>11-20人</th>
       		<th>21-35人</th>
       		<th>36-50人</th>
       		<th>大于50人</th>
       		<th>使用功能数</th>
       		<th>上线功能数</th>
       	</pg:header>	

      <pg:list autosort="false" requestKey="datas">
   		<tr>     
   		        <td ><pg:cell colName="appName"/></td>
                <td><pg:cell colName="scope1"/></td>
                <td><pg:cell colName="scope2"/></td>
                <td><pg:cell colName="scope3"/></td>
                <td><pg:cell colName="scope4"/></td>
                <td><pg:cell colName="scope5"/></td>
                <td><pg:cell colName="scope6"/></td>
                <td><pg:cell colName="scope7"/></td>
                <td><pg:cell colName="pagesActual"/></td>
                <td><pg:cell colName="pages"/></td>
        </tr>
	 </pg:list>
    </table>
    </div>
</div>		
