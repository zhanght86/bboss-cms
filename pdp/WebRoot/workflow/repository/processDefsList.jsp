<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：台账列表信息设置
作者：许石玉
版本：1.0
日期：2012-02-08
 --%>	

<div id="customContent">
<pg:empty actual="${processDefs}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${processDefs}" >
   <pg:pager scope="request"  data="processDefs" desc="true" isList="false" containerid="custombackContainer" selector="customContent">
	
	<pg:param name="processName"/>
	<pg:param name="deploymentName"/>
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
            <th align=center><input id="CKA" name="CKA" type="checkbox" 
							onClick="checkAll('CKA','CK')"></th>
			
       		<th>ID</th>
       		<th><pg:message code="sany.pdp.common.name"/></th>
       		<th><pg:message code="sany.pdp.workflow.process.key"/></th>
       		<th><pg:message code="sany.pdp.workflow.deploy.version"/></th>
       		<th><pg:message code="sany.pdp.workflow.deploy.name"/></th>
       		<th><pg:message code="sany.pdp.workflow.deploy.time"/></th>
       		<th><pg:message code="sany.pdp.workflow.processdef.path"/></th>
       		<th><pg:message code="sany.pdp.workflow.picture.resource.name"/></th>
       		<th><pg:message code="sany.pdp.workflow.business.type"/></th>
       		<th>消息提醒</th>
       		<!-- <th>节假日</th> -->
       		<th><pg:message code="sany.pdp.common.status"/></th>
       		<th><pg:message code="sany.pdp.common.operation"/></th>
       	</pg:header>	

      <pg:list >
   		<tr onDblClick="processDefs('<pg:cell colName="ID_" />')">
   		        <td class="td_center">
                    <input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="ID_" />"/>
                    <input id="id" type="hidden" name="id" value="<pg:cell colName="DEPLOYMENT_ID_" />"/></td>
                    <input id="key" type="hidden" name="key" value="<pg:cell colName="KEY_" />"/></td>
                <td><pg:cell colName="ID_" /></td>    
                <td><pg:cell colName="NAME_" /></td>
        		<td><pg:cell colName="KEY_" /></td>       
                <td><span class="toolTip" title="<pg:cell colName="VERSION_"/>"><pg:cell colName="VERSION_" maxlength="8" replace="..."/></span></td>  
           		<td><pg:cell colName="DEPLOYMENT_NAME_" /></td>   
           		<td><pg:cell colName="DEPLOYMENT_TIME_"  dateformat="yyyy-MM-dd HH:mm:ss"/></td>
           		<td><pg:cell colName="RESOURCE_NAME_"/></td>	   
           		<td><pg:cell colName="DGRM_RESOURCE_NAME_"/></td>
           		<td><pg:cell colName="business_name"/></td>
           		<td>
           		<!-- 
           			<input type="checkbox" name="<pg:cell colName="DEPLOYMENT_ID_" />_a" disabled <pg:notempty colName="messagetempleid" >checked</pg:notempty>/>短信
           			<input type="checkbox" name="<pg:cell colName="DEPLOYMENT_ID_" />_b" disabled <pg:notempty colName="emailtempleid" >checked</pg:notempty>/>邮件
           		-->
           			<pg:notempty colName="messagetempleid" >
           				<a href="javascript:openMessTemple('<pg:cell colName="messagetempleid" />','0')"><pg:cell colName="messagetempletitle" /></a>
					</pg:notempty>
					<pg:notempty colName="emailtempleid" >
						<a href="javascript:openMessTemple('<pg:cell colName="emailtempleid" />','1')"><pg:cell colName="emailtempletitle" /></a>
					</pg:notempty>
					<a href="javascript:openSetMessTemple('<pg:cell colName="KEY_" />')">设置</a>
           		</td>
           		<!-- 
           		<td>
                	<input type="radio" name="<pg:cell colName="DEPLOYMENT_ID_" />_a" value="0" <pg:equal colName="IS_CONTAIN_HOLIDAY" value="0">checked</pg:equal> onclick="setHoliday('<pg:cell colName="KEY_" />',this.value)"/>剔除
                	<input type="radio" name="<pg:cell colName="DEPLOYMENT_ID_" />_b" value="1" <pg:equal colName="IS_CONTAIN_HOLIDAY" value="1">checked</pg:equal> onclick="setHoliday('<pg:cell colName="KEY_" />',this.value)"/>包含
                </td>
                 --> 
           		<td>
                	<pg:equal colName="SUSPENSION_STATE_" value="1"><pg:message code="sany.pdp.workflow.operation.open"/></pg:equal>
                	<pg:equal colName="SUSPENSION_STATE_" value="2"><pg:message code="sany.pdp.workflow.operation.close"/></pg:equal>
                </td>   
                <td class="td_center">
                	<a href="javascript:void(0)" id="activateProcess" onclick="activateProcess('<pg:cell colName="ID_" />')" style="display: <pg:equal colName='SUSPENSION_STATE_' value='1'>none</pg:equal>;"><pg:message code="sany.pdp.workflow.operation.open"/></a>
                	<a href="javascript:void(0)" id="suspendProcess" onclick="suspendProcess('<pg:cell colName="ID_" />')" style="display: <pg:equal colName='SUSPENSION_STATE_' value='2'>none</pg:equal>;"><pg:message code="sany.pdp.workflow.operation.close"/></a>|
                	<a href="javascript:void(0)" id="viewProcessInfo" onclick="viewProcessInfo('<pg:cell colName="KEY_" />')"><pg:message code="sany.pdp.workflow.operation.workflow.info"/></a>|
                	<a href="<%=request.getContextPath()%>/workflow/config/taskConfigMain.page?processKey=<pg:cell colName="KEY_" />&deploymentId=<pg:cell colName='DEPLOYMENT_ID_' />" id="viewOrgProcessInfo"><pg:message code="sany.pdp.workflow.operation.workflow.config"/></a>|
                	<a href="javascript:void(0)" id="viewProcessInfo" onclick="viewTaskInfo('<pg:cell colName="KEY_" />')">任务管理</a>|
                	<a href="<%=request.getContextPath()%>/workflow/repository/downProcessXMLandPicZip.page?processKey=<pg:cell colName="KEY_" />&version=<pg:cell colName="VERSION_"/>" >下载</a>
                </td>    
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
    </pg:pager>
  </pg:notempty>  

</div>		
