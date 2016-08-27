<%
/*
 * <p>Title: 角色用户组列表查询</p>
 * <p>Description:角色用户组列表查询</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-4-14
 * @author liangbing.tao
 * @version 1.0
 */
%>
<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>



<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>


<%@ page import="com.frameworkset.platform.security.AccessControl"%>




<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String roleId = request.getParameter("roleId");	
	request.setAttribute("action","orgjob");
	
	String remark5 = request.getParameter("remark5");
	String jobName = request.getParameter("jobName");
	//String creator = request.getParameter("creator");
	
	jobName = jobName == null ? "" : jobName ;
	remark5 = remark5 == null ? "" : remark5 ;
	
	
%>

<html>
	<head>
		<title>属性容器</title>
		<script language="javascript">
			function clearInfo()
			{
				document.orgjobForm.remark5.value = '';
				document.orgjobForm.jobName.value = '';
			}
			function querySub(){
				var remark5 = document.orgjobForm.remark5.value;
				var jobName = document.orgjobForm.jobName.value;
				document.orgjobForm.action = "roleOrgJobQuery.jsp?jobName="+jobName+"&remark5="+remark5+"&type=orgjob&roleId=<%=roleId%>";
				document.orgjobForm.submit();
			}
			
			function doreset() {
				document.all.remark5.value = "";
				document.all.jobName.value = "";
		   	}
		</script>
		
		<body>	
			<div class="mcontent">
				<div id="searchblock">
					<div  class="search_top">
			    		<div class="right_top"></div>
			    		<div class="left_top"></div>
	      			</div>
	      			<div class="search_box">
	      				<form name="orgjobForm" method="post" >
	      					<table width="98.6%" border="0" cellspacing="0" cellpadding="0">
	      						<tr>
			      					<td class="left_box"></td>
			      					<td>
			      						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
			      							<tr>
			      								<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.name"/>：</th>
												<td><input type="text" name="remark5" value="<%=remark5%>"  class="w120" /></td>
			      								<th><pg:message code="sany.pdp.personcenter.person.post.name"/>：</th>
			      								<td><input type="text" name="jobName" value="<%=jobName%>" class="w120" / ></td>
			      								<th>&nbsp;</th>
			      								<td>
			      									<a href="javascript:void(0)" class="bt_1" id="search"  onclick="querySub()"><span><pg:message code="sany.pdp.common.operation.search"/></span> </a>
			      									<a href="javascript:void(0)" class="bt_2" id="reset1" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span> </a>
			      									<input type="reset" id="reset" style="display: none" />
			      								</td>
			      							</tr>
			      						</table>
			      					</td>
			      					<td class="right_box"></td>
			      				</tr>
	      					</table>
	      				</form>
	      			</div>
	      			<div class="search_bottom">
						<div class="right_bottom"></div>
						<div class="left_bottom"></div>
					</div>
				</div>
				
				<div id="changeColor">
					<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.purviewmanager.tag.RoleGrantSearchList" keyName="RoleGrantSearchList" />
					<pg:pager maxPageItems="15" id="RoleGrantSearchList" scope="request" data="RoleGrantSearchList" isList="false">
						<pg:param name="roleId"/>
						<pg:param name="type" value="orgjob"/>
						<pg:param name="jobName"/>
						<pg:param name="rmark5"/>
						
						<pg:equal actual="${RoleGrantSearchList.itemCount}" value="0" >
							<div class="nodata">
							<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
						</pg:equal>
						<pg:notequal actual="${RoleGrantSearchList.itemCount}"  value="0">
							<table width="98.7%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb" >
								<pg:header>
									<th><pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.org.name"/></th>
									<th><pg:message code="sany.pdp.personcenter.person.post.name"/></th>
								</pg:header>
								<pg:list>
									<tr>	      	
										<td>
											<pg:cell colName="remark5" defaultValue=""/>
										</td>							
										<td >
											<pg:cell colName="jobName" defaultValue="" />
										</td>
									</tr>
								</pg:list>
							</table>
							<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
						</pg:notequal>
					</pg:pager>
				</div>
			</div>
		
	</body>
</html>
