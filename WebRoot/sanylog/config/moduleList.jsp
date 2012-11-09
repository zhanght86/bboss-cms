<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<div id="customContent">
<pg:equal actual="${moduleList.totalSize}" value="0" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:equal> 
<pg:notequal actual="${moduleList.totalSize}"  value="0">
   <pg:pager scope="request"  data="moduleList" desc="true" isList="false" containerid="custombackContainer" selector="customContent">
	
	<pg:param name="appId"/>
	<pg:param name="appName"/>
	<pg:param name="moduleId"/>
	<pg:param name="moduleName"/>
	
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
       		<th>系统编号</th>
       		<th>系统名称</th>
       		<th>模块编号</th>
       		<th>模块名称</th>
       		<th>模块隶属</th>
       		<th>模块URL</th>
       		<th>状态</th>
       		<th>操作</th>
       	</pg:header>	                                                                                                                                                                                                                                                                                                                                                                                                                                                                       

      <pg:list autosort="false">
   		<tr>
                <td class="td_center"><pg:cell colName="appId"/></td> 
                <td><pg:cell colName="appName"/></td>
                <td class="td_center"><pg:cell colName="moduleId"/></td> 
                <td><pg:cell colName="moduleName"/></td>
                <td><pg:cell colName="moduleCode"/></td>
                <td><pg:cell colName="modulePath"/></td>
                <td>
                	<pg:equal colName="useFlag" value="0"><div align="center" style='color:#FF0000'>禁用中</div></pg:equal>
                	<pg:equal colName="useFlag" value="1"><div align="center" style='color:#00FF00'>启用中</div></pg:equal>
                </td>
                <td class="td_center">
                	<a href="javascript:void(0)" id="del" onclick="javascript:modify('<pg:cell colName="autoId" />', '1')">启用</a>
                	<a href="javascript:void(0)" id="del" onclick="javascript:modify('<pg:cell colName="autoId" />', '0')">禁用</a>
                </td>
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div>

    </pg:pager>
    </pg:notequal >
</div>		
