<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：流程授权列表信息设置
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
	
	<pg:param name="entrust_user"/>
	<pg:param name="create_user"/>
	<pg:param name="sts"/>
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
            <th align="center">&nbsp;&nbsp;</th>
       		<th>授权给</th>
       		<th>授权人</th>
       		<th>生效时间</th>
       		<th>失效时间</th>
       		<th>创建时间</th>
       		<th>授权范围</th>
       		<th>状态</th>
       		<th>操作</th>
       	</pg:header>	

      <pg:list >
   		<tr>
	        <td class="td_center">
                <input type="radio" name="entrustInfoRadio" value='<pg:cell colName="id"/>'/>&nbsp;&nbsp;<pg:rowid increament="1" offset="false"/>
            </td> 
            <td><pg:cell colName="entrust_user_name" /></td>
    		<td><pg:cell colName="create_user_name" /></td>
    		<td><pg:cell colName="start_date" dateformat="yyyy-MM-dd HH:mm:ss" /></td>
    		<td><pg:cell colName="end_date" dateformat="yyyy-MM-dd HH:mm:ss" /></td>
    		<td><pg:cell colName="create_date" dateformat="yyyy-MM-dd HH:mm:ss" /></td>
    		<td>
    			<pg:equal colName="wf_entrust_type" value="选择流程委托"><a href="javascript:void(0)" onclick="viewEntrustInfo('<pg:cell colName="id"/>');">部分委托</a></pg:equal>
    			<pg:equal colName="wf_entrust_type" value="全部委托">全部委托</pg:equal>
    		</td>    
       		<td><pg:cell colName="sts" /></td>
       		<td>
       			<pg:equal colName="sts" value="有效"><a href="javascript:void(0)" onclick="unUseEntrustInfo('<pg:cell colName="id"/>','失效');">失效</a></pg:equal>
       			<pg:equal colName="sts" value="失效"><a href="javascript:void(0)" onclick="unUseEntrustInfo('<pg:cell colName="id"/>','有效');">生效</a></pg:equal>
       		</td>
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
    </pg:notequal >

</div>		
