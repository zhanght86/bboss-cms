<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：台账列表信息设置
作者：许石玉
版本：1.0
日期：2012-02-08
 --%>	

<div id="customContent">

<pg:errors/>

<pg:equal actual="${datas.totalSize}" value="0" >
	<div class="nodata" style="text-align: right;">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:equal> 
<pg:notequal actual="${datas.totalSize}"  value="0">
   <pg:pager scope="request"  data="datas" desc="false" isList="false" containerid="custombackContainer" selector="customContent">
	<pg:param name="appId"/>
	<pg:param name="monitorTimeBegin"/>
	<pg:param name="monitorTimeEnd"/>
	<pg:param name="sign"/>
	<pg:param name="useTime"/>
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable">
        <pg:header>
        <th>检测时间</th>
     	<th>系统名称</th>
     	<th>取值时长</th>	
       	</pg:header>	
		<pg:notify  >
		<tr style="background-color: white"><td colspan=3  class="td_center"><img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></td></tr>
		</pg:notify>
	      <pg:list autosort="false">
	   		<tr>
	            <td class="td_center"><input id="id" type="hidden" name="id" value="<pg:cell colName="id" />"/>
	            <pg:cell colName="monitorTime" dateformat='yyyy-MM-dd HH:mm:ss' /></td>
	            <td>
	            <pg:convert convertData="appInfo" colName="appId"/>
	            </td>   
	            <td class="td_right"><pg:equalandupper colName="useTime" value="3000"><font color="Red"> <pg:cell colName="useTime" /></font></pg:equalandupper>
	            <pg:lower colName="useTime" value="3000"> <pg:cell colName="useTime" /></pg:lower>
	            </td>
	        </tr>
		 </pg:list>		
    </table> 
    </div>
	
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index sizescope="10,20,50,100"/></div>
  </pg:pager>
</pg:notequal>
</div>		
