<%
/*
 * <p>Title: 用户管理机构列表</p>
 * <p>Description: 用户管理机构列表</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-22
 * @author liangbing.tao
 * @version 1.0
 */
%>



<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>

<%@page import="com.frameworkset.platform.security.AccessControl"%>


<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);

	String userId = request.getParameter("userId");
%>

<html>
<head>
<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
	<script language="JavaScript" src="../../scripts/common.js" type="text/javascript"></script>		
	<script language="JavaScript" src="../../../include/pager.js" type="text/javascript"></script>
	<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
</head>
<body>
	<div id="changeColor">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table3" id="tb">	
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.UserManagerOrgList" keyName="OrgList"/>
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="10"
				  scope="request"  
				  data="OrgList" 
				  isList="false">
				  <pg:equal actual="${OrgList.itemCount}" value="0" >
				 	
						<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
						
				</pg:equal> 
				<pg:notequal actual="${OrgList.itemCount}"  value="0">
			      <pg:header >
			      <!--设置分页表头-->
			      	<th><pg:message code="sany.pdp.role.organization.name"/></th>
			      	<th><pg:message code="sany.pdp.role.organization.number"/></th>
			      	<th><pg:message code="sany.pdp.role.organization.sort.number"/></th>
			      	<th><pg:message code="sany.pdp.role.organization.description"/></th>
			      	
			     
			     </pg:header>
			      <pg:param name="userId"/>
			      <pg:param name="orgName"/>
			      <pg:param name="remark5"/>
			      <pg:param name="orgnumber"/>		      
			      <!--list标签循环输出每条记录-->			      
			      <pg:list>	
			      		<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" onDBLclick="" >	      				
										
							<td  align=left class="tablecells">
							<pg:null colName="remark5"><pg:cell colName="orgName"/></pg:null>
							<pg:notnull colName="remark5">
							<pg:equal colName="remark5" value=""><pg:cell colName="orgName"/></pg:equal>
							<pg:notequal colName="remark5" value=""><pg:cell colName="remark5"/></pg:notequal>
							</pg:notnull></td>
							<td  align=left class="tablecells"><pg:cell colName="orgnumber" defaultValue=" "/></td>
							<td  align=left class="tablecells"><pg:cell colName="orgSn" defaultValue=" "/></td>							
							<td  align=left class="tablecells">
							<pg:cell colName="orgdesc" defaultValue=" "/>
							
							</td>							
																																		    					    							    				    		
					  	</tr>			      		
			      </pg:list>
</table>
<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div>
        </pg:notequal>			   	      
		</pg:pager>
</div>
</body>
</html>
