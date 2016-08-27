<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	String userId = request.getParameter("userId");
%>
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>

<table width="100%" border="0" cellpadding="0" cellspacing="1" class="thin">
		
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.UserManagerOrgList" keyName="OrgList"/>
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="10"
				  scope="request"  
				  data="OrgList" 
				  isList="false">
			      <tr >
			      <!--设置分页表头-->
			      	<td class="headercolor">机构名称</td>
			      	<td class="headercolor">机构编号</td>
			      	<td class="headercolor">机构排序号</td>
			      	<td class="headercolor">机构描述</td>
			      	
			     
			      </tr>
			      <pg:param name="userId"/>
			      <pg:param name="orgName"/>
			      <pg:param name="remark5"/>
			      <pg:param name="orgnumber"/>		      
				  <!--检测当前页面是否有记录-->
		       	  <pg:notify>
			      <tr height="18px" >
			      	<td colspan=100 align='center'>暂时没有可管理的部门</td>
			      </tr>
			      </pg:notify>			      
			      			    
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
							<pg:notnull colName="orgdesc"><pg:cell colName="orgdesc" defaultValue=" "/></pg:notnull>
							
							<pg:null colName="orgdesc">没有描述</pg:null>
							<pg:equal colName="orgdesc" value="">没有描述</pg:equal>
							</td>							
																																		    					    							    				    		
					  	</tr>			      		
			      </pg:list>
			   <tr height="18px" >
			      	<td colspan=100 align='center' class="detailcontent">
			      	
			      	<pg:index/><input type="hidden" name="queryString" value="<pg:querystring/>"></td>
        </tr>   			   	      
		</pg:pager>
		
</table>
