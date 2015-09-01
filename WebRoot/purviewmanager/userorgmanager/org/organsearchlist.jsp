<%
/**
 * <p>Title: 机构查询</p>
 * <p>Description: 机构查询页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%         
  			String orgId = request.getParameter("orgId");
			String remark5 = request.getParameter("remark5");
			String orgName = request.getParameter("ORG_NAME");
			String orgnumber = request.getParameter("orgnumber");
			String orgcreator = request.getParameter("orgcreator");
			String isEffective = request.getParameter("isEffective");
			if (remark5 == null)
				remark5 = "";
			if (orgnumber == null)
				orgnumber = "";
			if (orgcreator == null)
			    orgcreator = "";
			if (isEffective == null)
			    isEffective = "";
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
	</head>
	<body>
		<div>
			<form name="Org" action="" method="post">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
					<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.purviewmanager.tag.OrgSearchList" keyName="OrgList" />
					<pg:pager maxPageItems="15" scope="request" data="OrgList" isList="false">
					<pg:empty actual="${OrgList}" evalbody="true">
					<pg:yes>
						<div class="nodata">
						<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
					</pg:yes>
					<pg:no>
					<tr>
							<!--设置分页表头-->
							<th>
								<pg:message code="sany.pdp.role.organization.name"></pg:message>
							</th>
							<input class="text" type="hidden" name="selectId">
							<th>
								<pg:message code="sany.pdp.role.organization.number"></pg:message>
							</th>
							<th>
								<pg:message code="sany.pdp.role.organization.sort.number"></pg:message>
							</th>
							<th>
								<pg:message code="sany.pdp.role.organization.description"></pg:message>
							</th>
                         <th>
								<pg:message code="sany.pdp.role.organization.creater"></pg:message>
							</th>

						</tr>
						<pg:param name="orgName" />
						<pg:param name="code" />
						<pg:param name="remark5" />
						<pg:param name="orgnumber" />
						<pg:param name="orgcreator"  />
						<pg:param name="isEffective"  />

						<!--list标签循环输出每条记录-->
						<pg:list>
						<%
						   String owner_Id=(String)dataSet.getString("creator");
						   
						   UserManager usermanager=SecurityDatabase.getUserManager();
					        User user=usermanager.getUserById(owner_Id);
					       String username="";
					       String userrealname="";
					       if(user != null){
					       		username=user.getUserName();
					       		userrealname=user.getUserRealname();	
					       }
					      
						%>
							<tr onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" onDBLclick="">

								<td height="20px" align="left" class="tablecells">
									<!--<pg:null colName="remark5">
										<pg:cell colName="orgName" />
									</pg:null>
									<pg:notnull colName="remark5">
										<pg:equal colName="remark5" value="">
											<pg:cell colName="orgName" />
										</pg:equal>
										<pg:notequal colName="remark5" value="">
											<pg:cell colName="orgName" />
										</pg:notequal>
									</pg:notnull>
									modified by hilary on 20101101,for display the orgname not the orgremak5
									-->
									<pg:cell colName="orgName" defaultValue=" " />
								</td>
								<td height="20px" align=left class="tablecells">
									<pg:cell colName="orgnumber" defaultValue=" " />
								</td>
								<td height="20px" align=left class="tablecells">
									<pg:cell colName="orgSn" defaultValue=" " />
								</td>
								<td height="20px" align=left class="tablecells">
										<pg:cell colName="orgdesc" defaultValue=" " />
								</td>
                                    <td height="20px" align=left class="tablecells">
									<%=username%>【<%=userrealname%>】
								</td>
							</tr>
						</pg:list>
					</pg:no>	
					</pg:empty> 
					 
						
				<tr><td colspan="10"><div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div></td></tr>
					</pg:pager>
				</table>
			</form>
		</div>
	</body>

</html>

