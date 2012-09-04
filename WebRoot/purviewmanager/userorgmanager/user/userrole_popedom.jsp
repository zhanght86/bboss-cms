<%
/*
 * <p>Title: 用户角色权限查询列表</p>
 * <p>Description: 用户角色权限查询列表</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-22
 * @author liangbing.tao
 * @version 1.0
 */
%>


<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
%>


<html>
<head>
	<tab:tabConfig />
		<script language="JavaScript" src="<%=request.getContextPath()%>/public/datetime/calender_date.js" ></script>
		<script language="JavaScript" src="../../scripts/common.js" type="text/javascript"></script>
		<script language="javascript" src="../../scripts/selectTime.js"></script>
		<SCRIPT language="JavaScript" SRC="../../../include/validateForm.js"></SCRIPT>
<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
		<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
		<style type="text/css">
		    .notice_STYLE{color: #FF0000;display : none }
	        .STYLE1 {color: #FF0000}
	   </style>
</head>	   
<body>	   
<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table3" id="tb">
    	<pg:listdata dataInfo="UserResList" keyName="UserResList" />
			<pg:pager maxPageItems="13" id="com.frameworkset.platform.sysmgrcore.web.tag.UserResList" scope="request" data="UserResList" isList="false">
	    <!--分页显示开始,分页标签初始化-->
			<pg:param name="userId" />
			<pg:param name="restype" />
			<pg:param name="resname" />
								
			<pg:equal actual="${UserResList.itemCount}" value="0" >
						<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
			</pg:equal> 
			<pg:notequal actual="${UserResList.itemCount}"  value="0">
			<pg:header>
	     <!--设置分页表头-->
		    <th><pg:message code="sany.pdp.role.resource.type.name"/></th>
		    <th><pg:message code="sany.pdp.role.resource.name"/></th>
			<th><pg:message code="sany.pdp.role.operation.id"/></th>
			<th><pg:message code="sany.pdp.role.name"/></th>														
		</pg:header>
			<!--list标签循环输出每条记录-->
			<pg:list>								
			<tr onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" onclick="" >
				<td class="tablecells" nowrap="nowrap">
					<pg:cell colName="resTypeName" defaultValue="" />
				</td>
				<td class="tablecells" nowrap="nowrap">
					<pg:cell colName="resName" defaultValue="" />
				</td>	
				<td class="tablecells" nowrap="nowrap" height='20'>
					<pg:cell colName="opName" defaultValue="" />
				</td>							
				<td class="tablecells" nowrap="nowrap" >
					<pg:cell colName="roleName" defaultValue="" />
				</td>
			</tr>
			</pg:list>
	</table>
	</div>
			<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div>
			</pg:notequal>
		</pg:pager>
</body>
</html>	

							                       
