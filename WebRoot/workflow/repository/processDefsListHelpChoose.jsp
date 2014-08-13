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
	<pg:param name="processChoose"/>
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
            <th align=center><input id="CKA" name="CKA" type="checkbox" 
							onClick="checkAll('CKA','CK');selectedSel();"></th>
			<th>应用系统</th>
       		<th>流程名称</th>
       	</pg:header>	

      <pg:list >
   		<tr >
	        <td class="td_center">
                <input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK');selectedSel();" value="<pg:cell colName="ID_" />"/>
                <input id="id" type="hidden" name="id" value="<pg:cell colName="DEPLOYMENT_ID_" />"/>
            	<input id="key" type="hidden" name="key" value="<pg:cell colName="KEY_" />"/>
                <input type="hidden" name="business_name" value="<pg:cell colName="business_name"/>" />
                <input type="hidden" name="proc_name" value="<pg:cell colName="NAME_" />" />
                <input type="hidden" name="wf_app_name" value="<pg:cell colName="wf_app_name"/>" />
            </td>
            <td><pg:cell colName="wf_app_name"/></td>   
            <td><pg:cell colName="NAME_" /></td>   
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
    </pg:pager>
    </pg:notequal >

</div>		
