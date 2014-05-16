<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：历史任务数据
作者：谭湘
版本：1.0
日期：2014-05-06
 --%>	

<div id="customContent">
<pg:equal actual="${listInfo.totalSize}" value="0" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:equal> 
<pg:notequal actual="${processDefs.totalSize}"  value="0">
   <pg:pager scope="request"  data="processDefs" desc="true" isList="false" containerid="custombackContainer" selector="customContent">
	
	<pg:param name="processName"/>
	<pg:param name="deploymentName"/>
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
            <th align=center><input id="CKA" name="CKA" type="checkbox" 
							onClick="checkAll('CKA','CK')"></th>
       		<th>流程key</th>
       		<th>版本</th>
       		<th>业务类型</th>
       		<th>流程开启时间</th>
       		<th>流程结束时间</th>
       		<th>操作</th>
       	</pg:header>	

      <pg:list >
   		<tr onDblClick="processDefs('<pg:cell colName="ID_" />')">
   		        <td class="td_center">
                    <input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="ID_" />"/>
                    <input id="id" type="hidden" name="id" value="<pg:cell colName="DEPLOYMENT_ID_" />"/></td>
                    <input id="key" type="hidden" name="key" value="<pg:cell colName="KEY_" />"/></td>
        		<td><pg:cell colName="KEY_" /></td>       
                <td><span class="toolTip" title="<pg:cell colName="VERSION_"/>"><pg:cell colName="VERSION_" maxlength="8" replace="..."/></span></td>  
           		<td><pg:cell colName="business_name"/></td>
           		<td><pg:cell colName="DEPLOYMENT_TIME_"  dateformat="yyyy-MM-dd HH:mm:ss"/></td>
           		<td><pg:cell colName="RESOURCE_NAME_"/></td>	   
                <td class="td_center">
                	<a href="javascript:void(0)" id="viewProcessInfo" onclick="viewProcessInfo('<pg:cell colName="KEY_" />')">查看明细</a>
                </td>    
        </tr>
	 </pg:list>
    </table>
    </div>
    
    <div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
    </pg:notequal >

</div>		
