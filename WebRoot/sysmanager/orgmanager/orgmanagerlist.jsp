<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager,com.frameworkset.platform.sysmgrcore.manager.OperManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role"%>
<%@ page import="java.util.List,java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<% 
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	String orgId=request.getParameter("orgId");	

	
	
%>
<html > 
      

<head>				
	<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
    <script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
    
    <script language="JavaScript" >
    
    </script>
	<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
</head>
<body class="contentbodymargin">
<div id="contentborder">
		
		<FORM name="userForm" method="post" action="">			    
		   	<INPUT name="orgId" value="<%=orgId%>" type="hidden">
		   	<INPUT name="aa" value="事件" type="hidden">
			<TABLE width="100%" border="0" cellpadding="0" cellspacing="1" class="thin">
			<TR><TD class="detailtitle" align="center" colspan="8"><br><B>部门管理员列表</B></TD></TR>
				<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.OrgManagerList" keyName="OrgManagerList" />
				<!--分页显示开始,分页标签初始化-->
				<!-- 有 form="userForm" 分页会出现错误 -->
				<pg:pager maxPageItems="5" promotion="true" id="OrgManagerList" scope="request" data="OrgManagerList" isList="false">
				<pg:param name="orgId"/>  
						  <!--检测当前页面是否有记录-->					      			    
					      <!--list标签循环输出每条记录-->			      
					      <TR class="labeltable_middle_td">
					      <!--设置分页表头-->
										      
					      	<td class="headercolor">登录名</td>	
					      	<INPUT class="text" type="hidden" name="selectId">
					      	
					      	<td class="headercolor">用户名</td>
					      	<td class="headercolor" colspan="2">性别</td>
							<td class="headercolor">所在机构以及岗位</td>	
					      	
					      </TR><pg:param name="orgId" /><pg:notify>
					      <TR height="18px">
					      	<TD class="detailcontent" colspan="100" align="center">暂时没有部门管理员</TD>
					      </TR>
					      </pg:notify><pg:list>	
					      		<TR class="labeltable_middle_tr_01" onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">	      				
									<TD class="tablecells" align="left"><pg:cell colName="userName" defaultValue="" /></TD>
									<TD class="tablecells" align="left"><pg:cell colName="userRealname" defaultValue="" /></TD>																											    					    							    				    		
							  		<TD class="tablecells" align="left" colspan="2">
							  			<pg:equal colName="userSex" value="F">女</pg:equal>
							  			<pg:equal colName="userSex" value="M">男</pg:equal>																										    					    							    				    		
							  			<pg:equal colName="userSex" value="-1">未知</pg:equal>
							  		</TD>			
									<TD class="tablecells">
										<pg:cell colName="orgName" defaultValue="" />
									</TD>							  		
							  	</TR>			      		
					      </pg:list>
					   <TR height="18px">
					      	<TD class="detailcontent" colspan="5" align="center" nowrap="nowrap">
					      	 
					      	<pg:index /><INPUT type="hidden" name="queryString" value="<pg:querystring/>"></TD>
					   </TR>   			   	      
				</pg:pager>
				
		   </TABLE>	
		</FORM>	
</div>
</body>
</html>
