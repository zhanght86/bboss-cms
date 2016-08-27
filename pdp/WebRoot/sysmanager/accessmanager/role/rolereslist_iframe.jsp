<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ResManager,
				 com.frameworkset.platform.resource.ResourceManager,
				 com.frameworkset.platform.config.model.Operation"%>

<%
	ResourceManager resManager = new ResourceManager();
	String restypeId=request.getParameter("restypeId");
%>

<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../../include/pager.js" type="text/javascript"></script>
<%@ include file="/include/css.jsp"%>
<link href="../../css/contentpage.css" rel="stylesheet" type="text/css">
<link href="../../css/tab.winclassic.css" rel="stylesheet" type="text/css">
<table width="100%" border="0"  cellpadding="0" cellspacing="1" class="thin">
			<pg:listdata dataInfo="RoleResList" keyName="RoleResList"/>
			<!--分页显示开始,分页标签初始化-->
			<pg:pager maxPageItems="20" scope="request" data="RoleResList" isList="false">
			<tr class="labeltable_middle_td">
			<!--设置分页表头-->
				<td height='20' class="headercolor">资源标识</td>
				<td height='20' class="headercolor">资源描述</td>
				<td height='20' class="headercolor">操作类型</td>
			</tr>
			<pg:param name="roleId"/>
			<pg:param name="restypeId"/>
			<!--检测当前页面是否有记录-->
			<pg:notify>
			<tr height="18px" >
				<td class="detailcontent" colspan=100 align='center'>暂时没有资源</td>
			</tr>
			</pg:notify>			      
			<!--list标签循环输出每条记录-->			      
			<pg:list>	
			<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">	
				<td class="tablecells" align=left><pg:cell colName="resId" defaultValue=""/></td>	      				
				<td class="tablecells" align=left><pg:cell colName="resName" defaultValue=""/></td>																																	    					    							    				    																																    					    							    				    		
				<td class="tablecells" align=left colspan="2">
				 <pg:cell colName="opName" defaultValue=""/>
				</td>
			</tr>
			</pg:list>
			<tr height="18px" >
				<td class="detailcontent" colspan=4 align='center'><pg:index/><input type="hidden" name="queryString" value="<pg:querystring/>"></td>
			</tr>   			   	      
			</pg:pager>
</table>
