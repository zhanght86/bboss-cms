<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：委托待办列表信息设置
版本：1.0
日期：2014-05-05
 --%>	

<div id="customContent">
<pg:equal actual="${entrustList.totalSize}" value="0" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:equal> 
<pg:notequal actual="${entrustList.totalSize}"  value="0">
   <pg:pager scope="request"  data="entrustList" desc="true" isList="false" containerid="custombackContainer" selector="customContent">
	
	<pg:param name="processName"/>
	<pg:param name="deploymentName"/>
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
            <th align="center">&nbsp;&nbsp;</th>
       		<th>委托人</th>
       		<th>归属人</th>
       		<th>生效时间</th>
       		<th>失效时间</th>
       		<th>创建时间</th>
       		<th>状态</th>
       		<th>操作</th>
       	</pg:header>	

      <pg:list >
   		<tr>
	        <td class="td_center">
                <input type="radio" name="entrustInfoRadio" value='<pg:cell colName="id"/>'/>&nbsp;&nbsp;<pg:rowid increament="1" offset="false"/>
            </td> 
            <td><pg:cell colName="entrust_user" /></td>
    		<td><pg:cell colName="create_user" /></td>
    		<td><pg:cell colName="start_date" dateformat="yyyy-MM-dd HH:mm:ss" /></td>
    		<td><pg:cell colName="end_date" dateformat="yyyy-MM-dd HH:mm:ss" /></td>
    		<td><pg:cell colName="create_date" dateformat="yyyy-MM-dd HH:mm:ss" /></td>    
       		<td><pg:cell colName="sts" /></td>
       		<td>
       			<pg:equal colName="sts" value="有效"><a href="#" onclick="unUseEntrustInfo('<pg:cell colName="id"/>','失效');">失效</a></pg:equal>
       			<pg:equal colName="sts" value="失效"><a href="#" onclick="unUseEntrustInfo('<pg:cell colName="id"/>','有效');">生效</a></pg:equal>
       		</td>
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
    </pg:notequal >

</div>		
