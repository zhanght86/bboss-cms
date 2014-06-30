<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：模板数据
作者：谭湘
版本：1.0
日期：2014-06-09
 --%>	

<div id="templeContent">
<pg:empty actual="${listInfo}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${listInfo}" >
   <pg:pager scope="request"  data="listInfo" desc="true" isList="false" containerid="templeContainer" selector="templeContent">
   
   	<pg:param name="templeTitle"/>
	<pg:param name="templeType"/>
	<pg:param name="createTime1"/>
	<pg:param name="createTime2"/>
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
            <th align=center><input id="CKA" name="CKA" type="checkbox" 
							onClick="checkAll('CKA','CK')"></th>
			<th>模板名称</th>
       		<th>模板类型</th>
       		<th>创建时间</th>
       		<th>创建人</th>
       		<th>修改时间</th>
       		<th>操作</th>
       	</pg:header>	

      <pg:list >
   		<tr >
	        <td class="td_center">
                <input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="templeId" />"/>
    		<td class="td_center"><pg:cell colName="templeTitle" /></td> 
    		<td class="td_center">
    			<pg:equal colName="templeType" value="0">短信</pg:equal>
    			<pg:equal colName="templeType" value="1">邮件</pg:equal>
    		</td>       
       		<td class="td_center"><pg:cell colName="createTime" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
       		<td class="td_center"><pg:cell colName="creator" /></td>
       		<td class="td_center"><pg:cell colName="lastUpdatetime" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
            <td class="td_center">
            	<a href="javascript:void(0)" id="editTempleInfo" onclick="editTemple('<pg:cell colName="templeId" />','0')">修改</a>|
            	<a href="javascript:void(0)" id="viewTempleDetailInfo" onclick="editTemple('<pg:cell colName="templeId" />','1')">详情</a>
            </td>    
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
   </pg:notempty> 

</div>		
