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
		<pg:param name="vtime"/>
		
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
        	<th>应用名称</th> 	
       		<th>模块名称</th>
       		<th>访问量</th>
       		<th>用户量</th>
       	</pg:header>	

      <pg:list autosort="false">
   		<tr>
                <td><pg:cell colName="appName"/></td>    
                <td><pg:cell colName="moduleName"/></td> 
                <td><pg:cell colName="vcount"/></td>
                <td><pg:cell colName="vcountUser"/></td>
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
</pg:notequal>
</div>		
