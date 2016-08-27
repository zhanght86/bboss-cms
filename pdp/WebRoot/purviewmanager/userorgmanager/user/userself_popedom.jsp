<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ include file="../../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@page import="com.frameworkset.platform.resource.ResourceManager" %>
<%@page import="com.frameworkset.platform.config.ResourceInfoQueue" %>
<%@page import="com.frameworkset.platform.config.model.ResourceInfo" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>


<tab:tabConfig />


		
<link rel="stylesheet" type="text/css" href="../../../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../../../sysmanager/css/tab.winclassic.css">
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<style type="text/css">
	.notice_STYLE{color: #FF0000;display : none }
    .STYLE1 {color: #FF0000}
</style>

<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.UserSelfResList" keyName="UserSelfResList" />
<!--分页显示开始,分页标签初始化-->
<pg:pager maxPageItems="13" id="UserSelfResList" scope="request" data="UserSelfResList" isList="false">
<pg:param name="userId" />
	<pg:param name="restype1" />
	<pg:param name="resname1" />
	
	<pg:equal actual="${UserSelfResList.itemCount}" value="0" >
						<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
			</pg:equal> 
			<pg:notequal actual="${UserSelfResList.itemCount}"  value="0">
		 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
			<pg:header>
	     <!--设置分页表头-->
		    <th><pg:message code="sany.pdp.role.resource.type.name"/></th>
		    <th><pg:message code="sany.pdp.role.resource.name"/></th>
			<th><pg:message code="sany.pdp.role.operation.id"/></th>
			</pg:header>
	
	<!--list标签循环输出每条记录-->
	<pg:list>								
		<tr>
			<td>
				<pg:cell colName="resTypeName" defaultValue="" />
			</td>
			<td>
				<pg:cell colName="resName" defaultValue="" />
			</td>	
			<td>
				<pg:cell colName="opName" defaultValue="" />
			</td>
		</tr>
	</pg:list>
	</table>
		<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div>
			</pg:notequal>
		</pg:pager>

</table>
