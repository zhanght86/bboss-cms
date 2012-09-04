<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager,
				com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator,
				com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl,
				com.frameworkset.platform.sysmgrcore.manager.JobManager,
				com.frameworkset.platform.config.ConfigManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.Organization,com.frameworkset.platform.sysmgrcore.entity.Job"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ldap.OrgManagerImpl" %>

<%
		AccessControl accesscontroler = AccessControl.getInstance();
	    accesscontroler.checkAccess(request, response);
		String userId = accesscontroler.getUserID();
		String action = StringUtil.replaceNull(request.getParameter("action"),"");
		String orgId = StringUtil.replaceNull(request.getParameter("orgId"),"");
		String parentId = StringUtil.replaceNull(request.getParameter("parentId"),"0");
		if(parentId.equals(""))parentId="0";
		
		//判断当前登陆用户能否管理当前机构
		OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
		boolean butFlag = orgAdministrator.userAdminOrg(userId,orgId);
		
		OrgManager orgManager = SecurityDatabase.getOrgManager();		
		Organization org = orgManager.getOrgById(orgId);
		String remark1 = org.getChargeOrgId();
		String remark2 = org.getSatrapJobId();
		String orgname = "";
		String jobname = "";
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
			remark3 = "有效";
		}
		else
		{
			remark3 = "无效";
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
 
		if(org_level.equals("1")){org_level="省级";}
		else if(org_level.equals("2")){org_level="市州级";}
		else if(org_level.equals("3")){org_level="县区级";}
		else if(org_level.equals("4")){org_level="科所级";}
		else if(org_level.equals("0")){org_level="内设科室";}
		
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
  <title>属性容器</title> 
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="javaScript" src="../../validateForm.js"></script> 
<script language="JavaScript">


var action="<%=action%>";

if(action=="save")
getNavigatorContent().location.href ="<%=rootpath%>/sysmanager/orgmanager/navigator_content.jsp?anchor=<%=parentId%>&expand=<%=parentId%>&request_scope=request&selectedNode=<%=orgId%>"; 
if(action=="update")
getNavigatorContent().location.href ="<%=rootpath%>/sysmanager/orgmanager/navigator_content.jsp?anchor=<%=parentId%>&collapse=<%=parentId%>&request_scope=request&selectedNode=<%=orgId%>";
function updateAfter(){
getNavigatorContent().location.href ="<%=rootpath%>/sysmanager/orgmanager/navigator_content.jsp?anchor=<%=parentId%>&expand=<%=parentId%>&request_scope=request&selectedNode=<%=orgId%>"; 
}

function tranorg(){
	parent.location.href="<%=rootpath%>/sysmanager/orgmanager/orgtran.jsp?orgId=<%=orgId%>";        
}

function createorg()
{
	//document.forms[0].action="<%=rootpath%>/orgmanager/org.do?method=newsubOrg&orgId=<%=orgId%>";
	getPropertiesContent().location.href="<%=rootpath%>/orgmanager/org.do?method=newsubOrg&orgId=<%=orgId%>";
	document.forms[0].submit();
}
function deleteyorg(){
    if(window.confirm("你确定要删除吗？(删除将连带删除子机构并且不可以再恢复)")){
		document.forms[0].action="<%=rootpath%>/orgmanager/org.do?method=deleteOrg";
		document.forms[0].submit();
	}
}
function modifyorg(){
	var orgId= document.forms[0].orgId.value;	
	document.forms[0].action="<%=rootpath%>/orgmanager/org.do?method=modifyOrgInfo&orgId="+orgId;
	document.forms[0].submit();
}
</script>
<link href="../css/tab.winclassic.css" rel="stylesheet" type="text/css">
<%@ include file="/include/css.jsp"%>
<link href="../css/contentpage.css" rel="stylesheet" type="text/css">
</head>

<body class="contentbodymargin" onLoad="<%=action.equals("update")?"updateAfter()":""%>">
<div id="contentborder">
<form name="form2" action="" method="post"  >	
<pg:beaninfo sessionKey="Organization">
	      
		 <input type="hidden"  name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>"/>
		 <input type="hidden"  name="parentId" value="<pg:cell colName="parentId"  defaultValue=""/>" />		
		 <input type="hidden"  name="children" value="<pg:cell colName="children"  defaultValue=""/>" />		 
		 <input type="hidden"  name="creator" value="<pg:cell colName="creator"  defaultValue=""/>" />
		 <input type="hidden"  name="creatingtime" value="<pg:cell colName="creatingtime" dateformat="yyyy-MM-dd"  defaultValue="<%=new java.util.Date()%>"/>">
		 <input type="hidden"  name="path" value="<pg:cell colName="path"  defaultValue=""/>" />
		 <input type="hidden"  name="code" value="<pg:cell colName="code"  defaultValue=""/>" />
		 
<table width="100%" border="0" cellpadding="0" cellspacing="1" class="thin">
  <tr >    
    <td align="center" class="detailtitle">机构编号</td>
    <td class="detailcontent"><input name="orgnumber" type="text" readonly="true"
				 validator="string" cnname="机构编号" value="<pg:cell colName="orgnumber"  defaultValue=""/>"></td>	
	<td align="center" class="detailtitle">机构名称</td>
    <td class="detailcontent"><input name="orgName" type="text" readonly="true"
				 validator="string" cnname="机构名称" value="<pg:cell colName="orgName"  defaultValue=""/>"></td>	   
  </tr>
  <tr>
    <td align="center" class="detailtitle">机构排序号</td>
    <td class="detailcontent"><input name="orgSn" type="text" readonly="true"
				  value="<pg:cell colName="orgSn"  defaultValue=""/>"></td>	
	<td align="center" class="detailtitle">机构描述</td>
    <td class="detailcontent"><input name="orgdesc" type="text" readonly="true"
				  value="<pg:cell colName="orgdesc"  defaultValue=""/>"></td>	   
  </tr>
  <tr  >
    <td align="center" class="detailtitle">简拼</td>
    <td class="detailcontent"><input name="jp" type="text" readonly="true"
				  value="<pg:cell colName="jp"  defaultValue=""/>"></td>	
	<td align="center" class="detailtitle">全拼</td>
    <td class="detailcontent"><input name="qp" type="text" readonly="true"
				  value="<pg:cell colName="qp"  defaultValue=""/>"></td>	   
  </tr> 
 <tr>
    <td align="center" class="detailtitle">机构有效标志</td>
	<td class="detailcontent">
		<input name="remark3" type="text" readonly="true" value="<%=remark3%>">
	</td>
 	<td align="center" class="detailtitle">机构显示名称</td>
    <td class="detailcontent"><input name="remark5" type="text" readonly="true"
				 validator="string" cnname="机构显示名称" value="<pg:cell colName="remark5"  defaultValue=""/>"></td>	
	<input type=hidden name=ispartybussiness value=0>
 </tr>
 
 <tr>
    <td align="center" class="detailtitle">是否直属局</td>
    <td class="detailcontent"><input name="isdirectlyparty" type="text" readonly="true"
				  value="<%=isdirectlyparty%>"></td>	
	<td align="center" class="detailtitle">是否涉外局</td>
    <td class="detailcontent"><input name="isforeignparty" type="text" readonly="true"
				  value="<%=isforeignparty%>"></td>	   
  </tr>
  <tr>
    <td align="center" class="detailtitle">是否稽查局</td>
    <td class="detailcontent"><input name="isjichaparty" type="text" readonly="true"
				  value="<%=isjichaparty%>"></td>	
	<td align="center" class="detailtitle">是否直接管户单位</td>
    <td class="detailcontent"><input name="isdirectguanhu" type="text" readonly="true"
				  value="<%=isdirectguanhu%>"></td>	   
  </tr> 
  
 <tr>	
	<!-- <td align="center" class="detailtitle">业务机构</td>
	<td class="detailcontent">
	<input name="gw" type="text" readonly="true"
				  value="<%=ispartybussiness%>">
	</td>   -->
 	
 
<!-- <tr>     
 	<td align="center" class="detailtitle">主管处室</td>
    <td class="detailcontent"><input name="zgcs" type="text" readonly="true"
				  value="<%=orgname%>"></td>	
	<td align="center" class="detailtitle">主管岗位</td>
    <td class="detailcontent"><input name="gw" type="text" readonly="true"
				  value="<%=jobname%>"></td>	   
</tr> -->
<input type=hidden name=chargeOrgId value=1>
<input type=hidden name=chargejobName value=1>
     
 	<!-- <td align="center" class="detailtitle">级次</td>
    <td class="detailcontent"><input name="jc" type="text" readonly="true"
				  value="<pg:cell colName="layer"  defaultValue=""/>"></td>	-->
	
	<td align="center" class="detailtitle">机构级别</td>
    <td class="detailcontent">
		<input name="org_level" type="text" readonly="true" value="<%=org_level%>">
	</td>  
 		
	          

<!-- 按级显示主管机构 -->
<%
if(org.getOrg_level().equals("1"))
{
%>
	<td align="center" class="detailtitle">行政区码</td>
    <td class="detailcontent">
    	<input name="org_xzqm" type="text" readonly="true" value="<pg:cell colName="org_xzqm"  defaultValue=""/>">
    </td>	   
</tr>
<%
}
else if(org.getOrg_level().equals("2"))
{
%>  
	<td align="center" class="detailtitle">所属省局</td>
    <td class="detailcontent"><input type="text" readonly="true" value=<%=province%>></td>  
</tr>
<tr>
	<td align="center" class="detailtitle">行政区码</td>
    <td class="detailcontent">
    	<input name="org_xzqm" type="text" readonly="true" value="<pg:cell colName="org_xzqm"  defaultValue=""/>">
    </td>
    <td align="center" class="detailtitle"></td>
    <td class="detailcontent">
    </td>
</tr>
<%
}
else if(org.getOrg_level().equals("3"))
{
%>
	<td align="center" class="detailtitle">所属省局</td>
    <td class="detailcontent"><input type="text" readonly="true" value=<%=province%>></td>
    </tr>
    <tr>
	<td align="center" class="detailtitle">所属市州局</td>
    <td class="detailcontent"><input type="text" readonly="true" value=<%=city%>></td>
    <td align="center" class="detailtitle">行政区码</td>
    <td class="detailcontent">
    	<input name="org_xzqm" type="text" readonly="true" value="<pg:cell colName="org_xzqm"  defaultValue=""/>">
    </td>	
</tr>
<%
}
else if(org.getOrg_level().equals("4"))
{
%>
	<td align="center" class="detailtitle">所属省局</td>
    <td class="detailcontent"><input type="text" readonly="true" value=<%=province%>></td>
</tr>
<tr>
	<td align="center" class="detailtitle">所属市州局</td>
    <td class="detailcontent"><input type="text" readonly="true" value=<%=city%>></td> 
    <td align="center" class="detailtitle">所属县区局</td>
    <td class="detailcontent"><input type="text" readonly="true" value=<%=country%>></td>
</tr>
<tr>
	<td align="center" class="detailtitle">行政区码</td>
    <td class="detailcontent">
    	<input name="org_xzqm" type="text" readonly="true" value="<pg:cell colName="org_xzqm"  defaultValue=""/>">
    </td>
    <td align="center" class="detailtitle"></td>
    <td class="detailcontent">
    </td>
</tr>
<%
}
else if(org.getOrg_level().equals("0"))
{
%> 
	<td align="center" class="detailtitle">所属省局</td>
    <td class="detailcontent"><input type="text" readonly="true" value=<%=province%>></td>
    </tr>
    <tr>
	<td align="center" class="detailtitle">所属市州局</td>
    <td class="detailcontent"><input type="text" readonly="true" value=<%=city%>></td>   

 
    <td align="center" class="detailtitle">所属县区局</td>
    <td class="detailcontent"><input type="text" readonly="true" value=<%=country%>></td>
    </tr>
    <tr>
    <td align="center" class="detailtitle">所属科所局</td>
    <td class="detailcontent"><input type="text" readonly="true" value=<%=mini%>></td>
    <td align="center" class="detailtitle">行政区码</td>
    <td class="detailcontent">
    	<input name="org_xzqm" type="text" readonly="true" value="<pg:cell colName="org_xzqm"  defaultValue=""/>">
    </td>
</tr>
<%
}
%>
  <tr>     
      <td align="center" colspan="4">
          	<%
          	String t_org_id = orgId;
          	if(orgId.equals("0"))
          	{
          		t_org_id = "00";
          	}
			%> 
			
           <%
	          if(accesscontroler.checkPermission("orgunit","orgupdate",AccessControl.ORGUNIT_RESOURCE)){
	       %>  	
           	  <input name="Submit1" type="button" class="input" value="修改" onclick="modifyorg()">
           <%} %> 
        
         <%if(!orgId.equals("0") && isProvinceAdminTag ){ %>   
          
           <%
	          //if(accesscontroler.checkPermission(t_org_id,AccessControl.DELETE_ORG,AccessControl.ORGUNIT_RESOURCE)){
	          if(accesscontroler.isAdmin()){
	       %>                  
              <input name="Submit2" type="button" class="input" value="删除" onclick="deleteyorg()"> 
           <%} %>  
            <%
	          if(accesscontroler.checkPermission("orgunit","tranorg",AccessControl.ORGUNIT_RESOURCE))
	          {
	       %>     
              <input name="Submit4" type="button" class="input" value="转移" onclick="tranorg()"> 
            <%}
            
            //if (accesscontroler.checkPermission(t_org_id,"addsuborg", AccessControl.ORGUNIT_RESOURCE))
            if(accesscontroler.isAdmin())
            {%>	 			
            	<input  name="Submit" type="button" class="input"  value="建子机构" onclick="createorg()"> 
            <%}
          }%>       
      </td> 
  </tr>
</table>	
</pg:beaninfo>
</form>
</div>
</body>
</html>
