<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager,
				com.frameworkset.platform.sysmgrcore.manager.OrgManager,
				com.frameworkset.platform.sysmgrcore.entity.Organization"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User,
				java.util.List,
				java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%><%
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkManagerAccess(request,response);

			String roleId = (String) request.getParameter("roleId");
			List allGrantOrg = null;
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			StringBuffer allGrantOrgSql = new StringBuffer()
			.append("select * from td_sm_organization where org_id in(")
			.append("select org_id from td_sm_orgrole where org_id in(")
			.append("select dd.org_id from (select o.*,1 as com_level from td_sm_Organization o ")
			.append(" union select o.*,2 as com_level from td_sm_Organization o where ")
			.append(" o.parent_Id='").append(0)
			.append("') dd ) and role_id='")
			.append(roleId).append("')");
			
			allGrantOrg = orgManager.getOrgListBySql(allGrantOrgSql.toString());
			request.setAttribute("allGrantOrg",allGrantOrg);
			%>
			<select multiple style="width:100%" " size="18">
									<pg:list requestKey="allGrantOrg">

										<option value="<pg:cell colName="orgId"/>">
											<pg:null colName="remark5">
												<pg:cell colName="orgName" />
											</pg:null>
											<pg:notnull colName="remark5">
												<pg:equal colName="remark5" value="">
													<pg:cell colName="remark5" />
												</pg:equal>
												<pg:notequal colName="remark5" value="">
													<pg:cell colName="remark5" />
												</pg:notequal>
											</pg:notnull>
										</option>
									</pg:list>
			</select>
