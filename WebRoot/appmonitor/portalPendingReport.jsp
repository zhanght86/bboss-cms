<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：Portal取待办检测报表
作者：许石玉
版本：1.0
日期：2012-02-08
 --%>	

<div id="customContent">
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
<pg:equal actual="${datas.totalSize}" value="0" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:equal> 
<pg:notequal actual="${datas.totalSize}"  value="0">
	<div id="changeColor">
	   <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" >
       <pg:header>
       <th>系统名称</th>
       <th>平均时长</th>
       <th>最大时长</th>
       </pg:header>  

 	    <pg:list requestKey="datas">
   		<tr>
      		<td><pg:cell colName="appName"/></td>
            <td class="td_right"><pg:cell colName="avgUseTime" numerformat="##.##"/></td>  
            <td class="td_right">
           		<pg:equalandupper colName="maxUseTime" value="3000"><font color="Red"> <pg:cell colName="maxUseTime" numerformat="##.##"/></font></pg:equalandupper>
	            <pg:lower colName="maxUseTime" value="3000"> <pg:cell colName="maxUseTime" numerformat="##.##"/></pg:lower>
            </td>  
        </tr>
		</pg:list>
    </table>
    </div>
</pg:notequal>
</div>		
