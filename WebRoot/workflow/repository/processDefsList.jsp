<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：台账列表信息设置
作者：许石玉
版本：1.0
日期：2012-02-08
 --%>	

<div id="customContent">
<pg:equal actual="${processDefs.totalSize}" value="0" >
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
			
       		
       		<th><pg:message code="sany.pdp.common.name"/></th>
       		<th><pg:message code="sany.pdp.workflow.process.key"/></th>
       		<th><pg:message code="sany.pdp.workflow.deploy.version"/></th>
       		<th><pg:message code="sany.pdp.workflow.deploy.name"/></th>
       		<th><pg:message code="sany.pdp.workflow.deploy.name"/></th>
       		<th><pg:message code="sany.pdp.workflow.deploy.time"/></th>
       		<th><pg:message code="sany.pdp.workflow.picture.resource.name"/></th>
       		<th><pg:message code="sany.pdp.workflow.business.type"/></th>
       		<th><pg:message code="sany.pdp.common.status"/></th>
       		<th><pg:message code="sany.pdp.common.operation"/></th>
       	</pg:header>	



      <pg:list autosort="false">
   		<tr onDblClick="processDefs('<pg:cell colName="ID_" />')">
   		        <td class="td_center">
                    <input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="ID_" />"/>
                    <input id="id" type="hidden" name="id" value="<pg:cell colName="DEPLOYMENT_ID_" />"/></td>
                    <input id="key" type="hidden" name="key" value="<pg:cell colName="KEY_" />"/></td>
                <td><pg:cell colName="NAME_" maxlength="8" replace="..."/></td>
        		<td><pg:cell colName="KEY_" /></td>       
                <td><span class="toolTip" title="<pg:cell colName="VERSION_"/>"><pg:cell colName="VERSION_" maxlength="8" replace="..."/></span></td>  
           		<td><pg:cell colName="DEPLOYMENT_NAME_" /></td>   
           		<td><pg:cell colName="DEPLOYMENT_TIME_"  dateformat="yyyy-MM-dd hh:mm:ss"/></td>
           		<td><pg:cell colName="RESOURCE_NAME_"/></td>	   
           		<td><pg:cell colName="DGRM_RESOURCE_NAME_"/></td>
           		<td><pg:cell colName="business_name"/></td>
           		 <td>
                	<pg:equal colName="SUSPENSION_STATE_" value="0"><pg:message code="sany.pdp.workflow.operation.open"/></pg:equal>
                	<pg:equal colName="SUSPENSION_STATE_" value="1"><pg:message code="sany.pdp.workflow.operation.close"/></pg:equal>
                </td>	   
                <td class="td_center">
                	<a href="javascript:void(0)" id="activateProcess" onclick="activateProcess('<pg:cell colName="ID_" />')" style="display: <pg:equal colName='SUSPENSION_STATE_' value='0'>none</pg:equal>;"><pg:message code="sany.pdp.workflow.operation.open"/></a>
                	<a href="javascript:void(0)" id="suspendProcess" onclick="suspendProcess('<pg:cell colName="ID_" />')" style="display: <pg:equal colName='SUSPENSION_STATE_' value='1'>none</pg:equal>;"><pg:message code="sany.pdp.workflow.operation.close"/></a>|
                	<a href="javascript:void(0)" id="viewProcessInfo" onclick="viewProcessInfo('<pg:cell colName="DEPLOYMENT_ID_" />')"><pg:message code="sany.pdp.workflow.operation.workflow.info"/></a>|
                	<a href="<%=request.getContextPath()%>/workflow/config/taskConfigMain.page?deploymentId=<pg:cell colName='DEPLOYMENT_ID_' />" id="viewOrgProcessInfo"><pg:message code="sany.pdp.workflow.operation.workflow.config"/></a>
                </td>    
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
    </pg:notequal >

</div>		
