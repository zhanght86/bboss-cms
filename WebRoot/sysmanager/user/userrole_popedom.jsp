<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
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
<script language="JavaScript" src="<%=request.getContextPath()%>/public/datetime/calender_date.js" ></script>
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script language="javascript" src="../scripts/selectTime.js"></script>
<SCRIPT language="JavaScript" SRC="validateForm.js"></SCRIPT>
		
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
	<style type="text/css">
	    .notice_STYLE{color: #FF0000;display : none }
        .STYLE1 {color: #FF0000}
   </style>
	<table class="thin" cellspacing="1" cellpadding="0" width="100%">
    <pg:listdata dataInfo="UserResList" keyName="UserResList" />
    <!--分页显示开始,分页标签初始化-->
	<pg:pager maxPageItems="13" id="com.frameworkset.platform.sysmgrcore.web.tag.UserResList" scope="request" data="UserResList" isList="false">
		<tr>
	     <!--设置分页表头-->
		    <td class="headercolor">资源类型名称</td>
		    <td class="headercolor">资源名称</td>
			<td class="headercolor" height='30'>操作ID</td>
			<td class="headercolor">角色名称</td>														
		</tr>
		<pg:param name="userId" />
		<pg:param name="restype" />
		<pg:param name="resname" />
								
	
	    <!--检测当前页面是否有记录-->
		<pg:notify>
		<tr height="18px" class="labeltable_middle_tr_01">
			<td colspan=100 align='center'>
				角色暂缺
			</td>
		</tr>
		</pg:notify>
		<!--list标签循环输出每条记录-->
		<pg:list>								
		<tr onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" onclick="" >
			<td class="tablecells" nowrap="nowrap">
				<pg:cell colName="resTypeName" defaultValue="未知" />
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
		<tr height="18px" class="labeltable_middle_tr_01">
			<td colspan=4 align='center'>
				<pg:index />
			</td>						
		</tr>
		<input name="queryString" value="<pg:querystring/>" type="hidden">
							
	</pg:pager>
	</table>
	

							                       
