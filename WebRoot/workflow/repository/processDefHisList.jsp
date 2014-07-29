<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/css.jsp"%>

<%--
描述：台账列表信息设置
作者：许石玉
版本：1.0
日期：2012-02-08
 --%>	

<div id="customContent"  style="overflow:auto">
  
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
			<th>ID</th>
       		<th><pg:message code="sany.pdp.workflow.deploy.catalogue"/></th>
       		<th><pg:message code="sany.pdp.common.name"/></th>
       		<th><pg:message code="sany.pdp.workflow.process.key"/></th>
       		<th><pg:message code="sany.pdp.workflow.deploy.version"/></th>
       		<th><pg:message code="sany.pdp.workflow.deploy.name"/></th>
       		<th><pg:message code="sany.pdp.workflow.deploy.time"/></th>
       		<th><pg:message code="sany.pdp.workflow.processdef.path"/></th>
       		<th><pg:message code="sany.pdp.workflow.picture.resource.name"/></th>
       		<th>操作</th>
       	</pg:header>	



<pg:empty actual="${processDefs }">
<tr><td colspan=15  style="text-align:center;" ><img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></td></tr>
</pg:empty>
<pg:notempty actual="${processDefs }">
      <pg:list requestKey="processDefs">

   		<tr >
                <td><pg:cell colName="ID_" /></td>        
                <td><span class="toolTip" title="<pg:cell colName="CATEGORY_"/>"><pg:cell colName="CATEGORY_" /></span></td>
                <td><pg:cell colName="NAME_" maxlength="8" replace="..."/></td>
        		<td><pg:cell colName="KEY_" /></td>       
                <td><pg:cell colName="VERSION_"/></td>  
           		<td><pg:cell colName="DEPLOYMENT_NAME_" /></td>   
           		<td><pg:cell colName="DEPLOYMENT_TIME_"  dateformat="yyyy-MM-dd HH:mm:ss"/></td>
           		<td><pg:cell colName="RESOURCE_NAME_"/></td>	   
           		<td><pg:cell colName="DGRM_RESOURCE_NAME_"/></td>	   	  
           		<td>
	           		<a href="javascript:void(0)" id="queryButton" onclick="viewProcessInfo('<pg:cell colName="KEY_" />','<pg:cell colName="VERSION_" />')">详情</a>|
	           		<a href="<%=request.getContextPath()%>/workflow/repository/downProcessXMLandPicZip.page?processKey=<pg:cell colName="KEY_" />&version=<pg:cell colName="VERSION_"/>" >下载</a>
	           	</td>
        </tr>
	 </pg:list>
	 </pg:notempty>
    </table>
    </div>
	
   
</div>		
