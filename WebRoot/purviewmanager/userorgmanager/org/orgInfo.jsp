<%
/**
 * <p>Title: 机构信息查看</p>
 * <p>Description: 机构信息查看</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
 <%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager,
				com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator,
				com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl,
				com.frameworkset.platform.sysmgrcore.manager.JobManager,
				com.frameworkset.platform.sysmgrcore.manager.UserManager,
				org.frameworkset.web.servlet.support.RequestContextUtils,
				com.frameworkset.platform.config.ConfigManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.Organization,
				com.frameworkset.platform.sysmgrcore.entity.Job,
				com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ldap.OrgManagerImpl" %>

<%
		AccessControl accesscontroler = AccessControl.getInstance();
	    accesscontroler.checkManagerAccess(request,response);
		//是否出现  "是否税管员" 复选框
		boolean istaxmanager = ConfigManager.getInstance().getConfigBooleanValue("istaxmanager", false);
		String userId = accesscontroler.getUserID();
		String action = StringUtil.replaceNull(request.getParameter("action"),"");
		String orgId = StringUtil.replaceNull(request.getParameter("orgId"),"");
		String parentId = StringUtil.replaceNull(request.getParameter("parentId"),"0");
		if(parentId.equals(""))parentId="0";
		
		//判断是否是cms系统
		String subSystemId = accesscontroler.getCurrentSystemID();
		
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		session.setAttribute("Organization", orgManager.getOrgById(orgId));
		
		//判断当前登陆用户能否管理当前机构
		OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
		boolean butFlag = orgAdministrator.userAdminOrg(userId,orgId);
				
		Organization org = orgManager.getOrgById(orgId);
		String remark1 = org.getChargeOrgId();
		String remark2 = org.getSatrapJobId();
		String orgname = "";
		String jobname = "";
		
		//获取机构创建者ID
		String orgCreator = org.getCreator();
		UserManager userManager = SecurityDatabase.getUserManager();
		User user = userManager.getUserById(orgCreator);
		String userName = user.getUserName();
		String userRealName = user.getUserRealname();
		
		userName = userName == null ? "不详" : userName ;
		userRealName = userRealName == null ? "不详" : userRealName;
		
		String creatorMessage = userName + "【" + userRealName + "】" ;
		
		
		if(remark1==null || "null".equals(remark1) || "".equalsIgnoreCase(remark1)){
			orgname = "不详";
		}else{
			Organization org1 = orgManager.getOrgById(remark1);
			if(org1==null){
				orgname = "不详";
			}else{
				orgname = org1.getOrgName();	
			}
		}
		if(remark2==null || remark2.equals("null")){
			jobname = "不详";
		}else{
			JobManager jobManager = SecurityDatabase.getJobManager();
			Job job = jobManager.getJobById(remark2);
			if(job==null){
				jobname = "不详";
			}else{
				jobname = job.getJobName();
			}
		}
		String ispartybussiness = "";
		ispartybussiness = org.getIspartybussiness();
		if("1".equals(ispartybussiness)){
			ispartybussiness = "否";
		}
		if("0".equals(ispartybussiness)){
			ispartybussiness = "是";
		}
		
		String remark3 = org.getRemark3();
		if(remark3==null)
		{
			remark3="0";
		}
		if(remark3.equals("1"))
		{
			remark3 = RequestContextUtils.getI18nMessage("sany.pdp.valid", request);
		}
		else
		{
			remark3 = RequestContextUtils.getI18nMessage("sany.pdp.invalid", request);
		}
		
		String org_level = org.getOrg_level();
		
		String office = "";
		Organization myOrganization = (Organization)orgManager.orgBelongsOfficeDepartment(orgId);		
		if(myOrganization != null){office = myOrganization.getRemark5();}	
		String mini = "";
		myOrganization = (Organization)orgManager.orgBelongsMiniDepartment(orgId);		
		if(myOrganization != null){mini = myOrganization.getRemark5();}		
		String country = "";
		myOrganization = (Organization)orgManager.orgBelongsCountryDepartment(orgId);		
		if(myOrganization != null){country = myOrganization.getRemark5();}		
		String city = "";
		myOrganization = (Organization)orgManager.orgBelongsCityDepartment(orgId);		
		if(myOrganization != null){city = myOrganization.getRemark5();}		
		String province = "";
		myOrganization = (Organization)orgManager.orgBelongsProvinceDepartment(orgId);		
		if(myOrganization != null){province = myOrganization.getRemark5();}
				
		
		//是否允许省级部门管理员特权的开通开关,如果这个开关是true，则只有省级机构的管理员才拥有当前和下级机构的新建、转移和删除子机构的权限
		//                              如果这个开关是false，则任何级别的机构管理员都拥有当前和下级机构的新建、转移和删除子机构的权限
		boolean isProvinceAdminTag = false;
		if(ConfigManager.getInstance().getConfigBooleanValue("enableprovinceadmintequan", false))
		{
			if(myOrganization != null)
			{
				String provinceOrgId = myOrganization.getOrgId();
				isProvinceAdminTag = ( orgAdministrator.isOrgAdmin(userId, provinceOrgId) || accesscontroler.isAdmin());
			}
			else
			{
				isProvinceAdminTag = accesscontroler.isAdmin();
			}
		}	

		else
		{
			isProvinceAdminTag = true;
		}	
 
		if(org_level.equals("J")){org_level="集团";}
		else if(org_level.equals("G")){org_level="子公司";}
		else if(org_level.equals("1")){org_level="一级部门";}
		else if(org_level.equals("2")){org_level="二级部门";}
		else if(org_level.equals("3")){org_level="三级部门";}
		else if(org_level.equals("4")){org_level="四级部门";}
		else if(org_level.equals("5")){org_level="五级部门";}
		else if(org_level.equals("6")){org_level="六级部门";}
		
		String isdirectlyparty = "";
		if(org.getIsdirectlyparty().equals("1")){	isdirectlyparty = "是";	}
		else{	isdirectlyparty = "否";	}
		String isforeignparty = "";
		if(org.getIsforeignparty().equals("1")){	isforeignparty = "是";	}
		else{	isforeignparty = "否";	}
		String isjichaparty = "";
		if(org.getIsjichaparty().equals("1")){	isjichaparty = "是";	}
		else{	isjichaparty = "否";	}
		String isdirectguanhu = "";
		if(org.getIsdirectguanhu().equals("1")){	isdirectguanhu = "是";	}
		else{	isdirectguanhu = "否";	}
%>
<html>
<head>     
  <title>基本信息查看</title> 
<script language="JavaScript" src="../../scripts/common.js" type="text/javascript"></script>		
<script type="text/javascript" src="../../../html/js/commontool.js"></script>
</head>

<body class="contentbodymargin" onLoad="<%=action.equals("update")?"updateAfter()":""%>">
<div style="height: 10px">&nbsp;</div>
<div id="">
<form name="form2" action="" method="post"  >	
<pg:beaninfo sessionKey="Organization">
	      
		 <input type="hidden"  name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>"/>
		 <input type="hidden"  name="parentId" value="<pg:cell colName="parentId"  defaultValue=""/>" />		
		 <input type="hidden"  name="children" value="<pg:cell colName="children"  defaultValue=""/>" />		 
		 <input type="hidden"  name="creator" value="<pg:cell colName="creator"  defaultValue=""/>" />
		 <input type="hidden"  name="creatingtime" value="<pg:cell colName="creatingtime" dateformat="yyyy-MM-dd"  defaultValue="<%=new java.util.Date()%>"/>">
		 <input type="hidden"  name="path" value="<pg:cell colName="path"  defaultValue=""/>" />
		 <input type="hidden"  name="code" value="<pg:cell colName="code"  defaultValue=""/>" />
		 
<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table4">
  <tr >    
    <th><pg:message code="sany.pdp.role.organization.number"/>：</th>
    <td class="detailcontent"><input name="orgnumber" type="text" readonly="true"
				 validator="string" cnname="机构编号" value="<pg:cell colName="orgnumber"  defaultValue=""/>"></td>	
	<th><pg:message code="sany.pdp.role.organization.name"/>：</th>
    <td class="detailcontent"><input name="orgName" type="text" readonly="true"
				 validator="string" cnname="机构名称" value="<pg:cell colName="orgName"  defaultValue=""/>"></td>	   
  </tr>
  <tr>
    <th><pg:message code="sany.pdp.role.organization.sort.number"/>：</th>
    <td class="detailcontent"><input name="orgSn" type="text" readonly="true"
				  value="<pg:cell colName="orgSn"  defaultValue=""/>"></td>	
	<th><pg:message code="sany.pdp.role.organization.description"/>：</th>
    <td class="detailcontent"><input name="orgdesc" type="text" readonly="true"
				  value="<pg:cell colName="orgdesc"  defaultValue=""/>"></td>	   
  </tr>
  <tr  >
    <th><pg:message code="sany.pdp.simple.spell"/>：</th>
    <td class="detailcontent"><input name="jp" type="text" readonly="true"
				  value="<pg:cell colName="jp"  defaultValue=""/>"></td>	
	<th><pg:message code="sany.pdp.complete.spell"/>：</td>
    <td class="detailcontent"><input name="qp" type="text" readonly="true"
				  value="<pg:cell colName="qp"  defaultValue=""/>"></td>	   
  </tr> 
 <tr>
    <th><pg:message code="sany.pdp.role.organization.check"/>：</th>
	<td class="detailcontent">
		<input name="remark3" type="text" readonly="true" value="<%=remark3%>">
	</td>
 	<th><pg:message code="sany.pdp.show.name"/>：</th>
    <td class="detailcontent"><input name="remark5" type="text" readonly="true"
				 validator="string" cnname="机构显示名称" value="<pg:cell colName="remark5"  defaultValue=""/>"></td>	
	<input type=hidden name=ispartybussiness value=0>
 </tr>
	<tr>
		<th><pg:message code="sany.pdp.role.organization.creater"/>：</th>
	    <td class="detailcontent" colspan="3"><input type="text" readonly="true" value="<%=creatorMessage%>"></td>   
    </tr>
  
</table>	
</pg:beaninfo>
</form>
</div>
</body>
</html>
