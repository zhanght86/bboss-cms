<%
/*
 * <p>Title: 用户查询页面</p>
 * <p>Description: 用户查询页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-20
 * @author liangbing.tao
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil" %>
<%@ page import="com.frameworkset.platform.config.ConfigManager" %>


<%
 			AccessControl accesscontroler = AccessControl.getInstance();
	        accesscontroler.checkManagerAccess(request,response);
	        
	        String userId = accesscontroler.getUserID();
	        DBUtil db = new DBUtil();
	        
		    db.executeSelect("select count(1) from td_sm_user");
		    
		    String usercount = String.valueOf(db.getInt(0, 0));
		    
		    String userOrgType = (String)request.getParameter("userOrgType");
		    String userName = (String)request.getParameter("userName");
		    String userRealname = (String)request.getParameter("userRealname");
		    
		    if(userOrgType == null)
		    {
		    	userOrgType = "";
		    }
		    if(userName == null)
		    {
		    	userName = "";
		    }
		    if(userRealname == null)
		    {
		    	userRealname = "";
		    }
%>

<html >
	<head>				
		<tab:tabConfig/>
			<title>属性容器</title>
<%@ include file="/include/css.jsp"%>
		 
			<SCRIPT language="javascript">	
				function actionOnmouseover(e)
				{	
					e.style.backgroundColor = "#8080FF";
				}
			
				function actionOnmouseup(e)
				{
					e.style.backgroundColor = "#BEC0C2";
				}
			
			
				function queryUser()
				{	
					var userOrgType = document.all("userOrgType").value;
					userList.action="../user/userquery_content_tab.jsp?userOrgType="+userOrgType+"&userId=<%=userId%>";
					userList.submit();	
				}
				
				function queryUserInfo()
				{
					var userOrgType = document.all("userOrgType").value;
					var userName = document.all("userName").value;
					var userRealname = document.all("userRealname").value;
					//userList.action="../user/ireport/showJasperReport.jsp?userOrgType="+userOrgType+"&userName="+userName+"&userRealname="+userRealname;
					//userList.submit();
					var url = "../user/ireport/showJasperReport.jsp?userOrgType="+userOrgType+"&userName="+userName+"&userRealname="+userRealname;
					window.open(url);
					//window.showModalDialog(url,window,"dialogWidth:"+(620)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
				}
				
				function clearQueryInfo()
				{
					userList.userName.value = '';
					userList.userRealname.value = '';
				}
		</SCRIPT>	
	</head>
	<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.UserSearchList" keyName="UserSearchList" />
	<!--分页显示开始,分页标签初始化-->
	<pg:pager maxPageItems="15" id="UserSearchList" scope="request" data="UserSearchList" isList="false">
	
	<body>
		<form name="userList" method="post" >
			<table cellspacing="1" cellpadding="0" border="0" width=100% class="thin">
				<input type="hidden" name="userOrgType" value="hasMainOrg" />
 				<tr valign='top'>
   					<td height='30'valign='middle' colspan="3" nowrap>
   						<strong><pg:message code="sany.pdp.workflow.user.list"/>
							<div align="right"><pg:message code="sany.pdp.user.count"/>：<!-- <%=usercount%> -->
								<pg:rowcount/>
							</div>
       					</strong>	
   					</td>
 				</tr>
 				<tr >           		
					<td width="40%" align="center">
						<pg:message code="sany.pdp.user.login.name"/>：<input type="text" name="userName" value="<%=userName%>">
					</td>
					<td width="30%">
						<pg:message code="sany.pdp.user.real.name"/>：<input type="text" name="userRealname" value="<%=userRealname%>">
					</td>
					<td width="30%" align="center">
						<input name="search" type="button" class="input" value="<pg:message code='sany.pdp.common.operation.search'/>" onClick="queryUser()">&nbsp;&nbsp;
						<input name="clearInfo" type="button" class="input" value="<pg:message code='sany.pdp.common.operation.reset'/>" onclick="clearQueryInfo()">&nbsp;&nbsp;   
						<input name="mimeograph" type="button" class="input" value="<pg:message code='sany.pdp.common.print'/>" onClick="queryUserInfo()">
					</td>
 				</tr>
 			</table>
     			<hr width=100%>
				<table cellspacing="1" cellpadding="0" border="0" width=100% class="thin">
					<tr>
						<!--设置分页表头-->								
						<td height='30' width="20%" class="headercolor"  ><pg:message code="sany.pdp.user.login.name"/>	</td>
						<td height='30' width="20%" class="headercolor"  ><pg:message code="sany.pdp.user.real.name"/></td>
						<td height='30' width="*" class="headercolor"  ><pg:message code="sany.pdp.workflow.organization"/></td>
						<td height='30' width="17%" class="headercolor"  ><pg:message code="sany.pdp.user.telephone"/></td>
					</tr>
						
					<pg:param name="orgId"/>
					<pg:param name="userName"/>
					<pg:param name="userRealname"/>
					<pg:param name="advQuery" />
					<pg:param name="userId" />
					<pg:param name="job_name"/>	
					<pg:param name="userOrgType"/>						
						
                      
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr height="18px" class="labeltable_middle_tr_01">
							<td colspan=100 align='center' height='20'>
							</td>
						</tr>
					</pg:notify>

					<!--list标签循环输出每条记录-->
					<pg:list>
						<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'">
							<td height='20' class="tablecells" nowrap="nowrap">
								<pg:cell colName="userName" defaultValue="" />
							</td>
							<td height='20' class="tablecells" nowrap="nowrap">
								<pg:cell colName="userRealname" defaultValue="" />
							</td>	
							<td height='20' class="tablecells" >
								<pg:cell colName="orgName" defaultValue="" />
							</td>
							<td height='20' class="tablecells" nowrap="nowrap" >
								<pg:notnull colName="userMobiletel1"><pg:cell colName="userMobiletel1" defaultValue=" "/></pg:notnull>
							</td>		
						</tr>
					</pg:list>
					
					<tr height="30px" class="labeltable_middle_tr_01">
						<td colspan=5 align='center'>
							 <pg:index />
						</td>
					</tr>
				<input name="queryString" value="<pg:querystring/>" type="hidden">
			 </table>
		</form>
	</body>
	</pg:pager>
</html>
