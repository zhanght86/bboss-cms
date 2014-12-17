<% 
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@page import="com.frameworkset.platform.config.ConfigManager"%><%
/**
 * <p>Title: 修改机构信息页面</p>
 * <p>Description: 修改机构信息页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager,com.frameworkset.platform.sysmgrcore.manager.JobManager"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.Organization,com.frameworkset.platform.sysmgrcore.entity.Job"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@page import= "com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.frameworkset.platform.sysmgrcore.purviewmanager.GenerateServiceFactory"%>

   
	
<%
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		
		//是否出现  "是否税管员" 复选框
		boolean istaxmanager = ConfigManager.getInstance().getConfigBooleanValue("istaxmanager", false);
		String isExist = "false";
		if ( request.getAttribute("isExist") != null){
			isExist = "true";
		}
		
		//判断是否是cms系统
		String subSystemId  = control.getCurrentSystemID();
		
		String orgId = StringUtil.replaceNull(request.getParameter("orgId"),"");
	      
	     //判断登陆用户是否属于当前机构,是则禁止修改机构信息(除机构显示名称外)
	     //2008-4-8 baowen.liu
		OrgManager orgManager1 = new OrgManagerImpl();
		String isDisabled = "";
		
		List orgs = orgManager1.getOrgListOfUser(control.getUserID());
		if(!control.isAdmin() && orgs.size()>0){
			for (Iterator iter = orgs.iterator(); iter.hasNext();){
			      Organization org1= (Organization) iter.next();
				    if(org1.getOrgId().equals(orgId)){
				    	isDisabled = "disabled='true'";
				    	
				    }
			 }		
		}
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		Organization org = orgManager.getOrgById(orgId);
		request.setAttribute("updateOrgInfo", org);
			
		String orgNumber = org.getOrgnumber();
		
		String remark1 = org.getChargeOrgId();
		String remark2 = org.getSatrapJobId();
		String orgname;
		String ispartybussiness = org.getIspartybussiness();
		String jobname ;//
		
		//获取机构创建者ID
		String orgCreator = org.getCreator();
		UserManager userManager = SecurityDatabase.getUserManager();
		User user = userManager.getUserById(orgCreator);
		String userName = user.getUserName();
		String userRealName = user.getUserRealname();
		
		userName = userName == null ? "不详" : userName ;
		userRealName = userRealName == null ? "不详" : userRealName;
		String creatorMessage = userName + "【" + userRealName + "】" ;
		
		
		
		if(remark1==null || "null".equals(remark1) || "".equals(remark1)){
			orgname = "不详";
		}else{
			Organization org1 = orgManager.getOrgById(remark1);
			if(org1==null){
				orgname = "不详";
			}else{
				orgname = org1.getOrgName();	
			}
		}
	
		if(remark2==null || "null".equals(remark2) || "".equals(remark2)){
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
		String parentOrgId = org.getParentId();
		String parent_org_level = "666";
		Organization parentOrg = orgManager.getOrgById(parentOrgId);
		if(parentOrg!=null)
		{
			parent_org_level = parentOrg.getOrg_level();
		}
		if(parent_org_level == null)
		{
			parent_org_level = "666";
		}
		
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
		
		
		//默认生成的机构编码
		String orgnumber = "";
		int len = GenerateServiceFactory.getOrgNumberGenerateService().getOrgNumberLen();
		//机构是否存在层级关系
		boolean orgNumberHiberarchy = GenerateServiceFactory.getOrgNumberGenerateService().enableOrgNumberGenerate();
		int parentOrgNumberLength = 0;
		int maxOrgNumberLength = 0;
		String parentOrgNumber = "";  
		if(!parentOrgId.equals("0")){
			parentOrgNumber = parentOrg.getOrgnumber();
			if(orgNumberHiberarchy){
				orgnumber = GenerateServiceFactory.getOrgNumberGenerateService().generateOrgNumber(parentOrg);
			}else{
				orgnumber = parentOrgNumber;
			}
			//父机构编号长度
			parentOrgNumberLength = parentOrgNumber.length();
			maxOrgNumberLength = parentOrgNumberLength + len;
		}
%>
<html>
<head>     
<title>基本信息修改</title>
<style type="text/css">
			<!--
			.style1 {color: #CC0000}
			-->
		</style>
<script language="javaScript" src="../../scripts/validateForm.js"></script>
<script type="text/javascript" src="../../../include/jquery-1.4.2.min.js"></script>
<link href="../../../html/stylesheet/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../../../html/js/commontool.js"></script>
<script type="text/javascript" src="../../../html/js/dialog/lhgdialog.js?self=false"></script>
<script language="JavaScript">
	
	var api = frameElement.api, W = api.opener;
	
	//焦点放在第一个text
	function init(){
	   
		form2.orgnumber.focus();
	}
	
	function trim(string){
		  var temp="";
		  string = ''+string;
		  splitstring = string.split(" ");
		  for(i=0;i<splitstring.length;i++){
		    temp += splitstring[i];
		  } 
		  return temp;
	}
	
	function enable(){
		form2.isjichaparty.disabled = true ;
		form2.remark3.disabled = true ;
		form2.isforeignparty.disabled = true ;
		form2.isdirectlyparty.disabled = true;
		form2.isdirectguanhu.disabled = true;
		form2.org_level.disabled = true;
		
		form2.orgnumber.disabled = true;
		form2.orgName.disabled = true;
		form2.orgSn.disabled = true;
		form2.orgdesc.disabled = true;
		form2.jp.disabled = true;
		form2.qp.disabled = true;
		form2.org_xzqm.disabled = true;
	}
	
	function modifyorg(){
	
		//form2.isjichaparty.disabled = false ;
		//form2.remark3.disabled = false ;
		//form2.isforeignparty.disabled = false ;
		//form2.isdirectlyparty.disabled = false ;
		//form2.isdirectguanhu.disabled = false ;
		//form2.org_level.disabled = false ;
		
		//form2.orgnumber.disabled = false ;
		//form2.orgName.disabled = false ;
		//form2.orgSn.disabled = false ;
		//form2.orgdesc.disabled = false ;
		//form2.jp.disabled = false ;
		//form2.qp.disabled = false ;
		//form2.org_xzqm.disabled = false ;
		
		var orgnumber = form2.orgnumber.value;
		var orgName = form2.orgName.value;
		var orgSn = form2.orgSn.value;
		var orgdesc = form2.orgdesc.value;
		var jp = form2.jp.value;
		var qp = form2.qp.value;		
		var remark5 = form2.remark5.value;
		var layer = form2.layer.value;
		//var org_xzqm_obj = form2.org_xzqm;
		
		if (trim(orgnumber).length == 0 ){
	    $.dialog.alert("<pg:message code='sany.pdp.role.check.organization.number.invalid'/>！"); 
		return false;
		}
		if(orgnumber.search(/[^0-9A-Za-z]/g) !=-1){
		$.dialog.alert("<pg:message code='sany.pdp.role.check.organization.name.invalid'/>!");
		form2.orgnumber.focus();
		return false;
		}
		if (trim(orgName).length == 0 ){
	    $.dialog.alert("<pg:message code='sany.pdp.role.check.organization.name.invalid'/>！"); 
		return false;
		} 
		if (trim(orgSn).length == 0 ){
	    $.dialog.alert("<pg:message code='sany.pdp.role.check.organization.sort.number.invalid'/>！"); 
		return false;
		}
		if(orgSn.search(/[^0-9]/g) !=-1){
		$.dialog.alert("<pg:message code='sany.pdp.role.check.organization.sort.number.invalid'/>!");
		form2.orgSn.focus();
		return false;
		}
		if (trim(remark5).length == 0 ){
	    $.dialog.alert("<pg:message code='sany.pdp.check.organization.show.name.invalid'/>"); 
		return false;
		}
		if(jp.search(/[^A-Za-z]/g) !=-1){
		$.dialog.alert("<pg:message code='sany.pdp.check.organization.simple.spell.invalid'/>!");
		form2.jp.focus();
		return false;
		}
		if(qp.search(/[^A-Za-z]/g) !=-1){
		$.dialog.alert("<pg:message code='sany.pdp.check.organization.complete.spell.invalid'/>!");
		form2.qp.focus();
		return false;
		}   
		if(orgnumber.length>100)
		{
			$.dialog.alert("<pg:message code='sany.pdp.organiztion.number.long'/>!");
			return;
		}
		if(orgName.length>40)
		{
			$.dialog.alert("<pg:message code='sany.pdp.organization.name.long'/>!");
			return;
		}
		if(orgdesc.length>300)
		{
			$.dialog.alert("<pg:message code='sany.pdp.organiztion.number.long'/>!");
			return;
		}
		if(jp.length>40)
		{
			$.dialog.alert("<pg:message code='sany.pdp.simple.spell.long'/>!");
			return;
		}
		if(qp.length>40)
		{
			$.dialog.alert("<pg:message code='sany.pdp.complete.spell.long'/>!");
			return;
		}
		
		
		
	//if(org_xzqm_obj != null)
	//	{
			
			//if (trim(org_xzqm).length == 0 )
			//{
		    	//alert("行政区码不能为空！"); 
				//return false;
			//}
		//}
		//else{
			
			//	alert("行政区码为必填项!");
				//return;
			//}
		 
		var orgId= document.forms[0].orgId.value;
		
		//document.all.saveButton.disabled = true;
		//document.all.resetButton.disabled = true;
		//document.all.backButton.disabled = true;
	  	document.forms[0].action = "modifyorg_do.jsp";
		document.forms[0].target = "hiddenFrame";
		document.forms[0].submit();
		
	}
	
	function checkOrgNumber(obj){
		var val = obj.value;
		var cruLength = val.length;
		var maxLength = "<%=maxOrgNumberLength%>";
		var subOrg = val.substring(0,<%=parentOrgNumberLength%>);
		if(subOrg != "<%=parentOrgNumber%>"){
			$.dialog.alert("机构编码前几位必须与父机构编码相同\n父机构编码为：<%=parentOrgNumber%>");
			obj.value = "<%=orgnumber%>";
			obj.focus();
			return;
		}
		if(cruLength != maxLength){
			$.dialog.alert("机构编码长度只能是父机构编码长度加<%=len%>位！\n该机构编码长度只能是"+maxLength+"位数字");
			obj.value = val.substring(0,maxLength);
			obj.focus();
			return;
		}
		
	}
</script>
<%
	

%>
</head>

<body class="contentbodymargin" <%if(isDisabled.equals("")) {%> onload="init()" <%}%>>
<div style="height: 10px">&nbsp;</div>
<div id="" >
<center>
<form name="form2" action="" method="post"  >	
<pg:beaninfo requestKey="updateOrgInfo">
	      
		 <input type="hidden"  name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>"/> 
		 <input type="hidden"  name="parentId" value="<pg:cell colName="parentId"  defaultValue=""/>" />
		 <input type="hidden"  name="orgNumber" value="<%=orgNumber%>" /> 
		 <input type="hidden"  name="children" value="<pg:cell colName="children"  defaultValue=""/>" /> 
		 <input type="hidden"  name="creator" value="<pg:cell colName="creator"  defaultValue=""/>" /> 
		 <input type="hidden"  name="creatingtime" value="<pg:cell colName="creatingtime" dateformat="yyyy-MM-dd"  defaultValue="<%=new java.util.Date()%>"/>"> 
		 <input type="hidden"  name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" /> 
		 <input type="hidden"  name="remark2" value="<pg:cell colName="remark2"  defaultValue=""/>" /> 
		 <input type="hidden"  name="path" value="<pg:cell colName="path"  defaultValue=""/>" /> 
		 <input type="hidden"  name="code" value="<pg:cell colName="code"  defaultValue=""/>" /> 
		 <input type="hidden"  name="oldOrgname" value="<pg:cell colName="orgName"  defaultValue=""/>" />
		 <input type="hidden"  name="oldremark5" value="<pg:cell colName="remark5"  defaultValue=""/>" />
		 <input type=hidden name=layer value=<pg:cell colName="layer"  defaultValue=""/>>
		 <input type=hidden name=chargeOrgId value=1>
		 <input type=hidden name=chargejobName value=1>
		<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table4">
  <tr >    
    <th><pg:message code="sany.pdp.role.organization.number"/>：</th>
    <td class="detailcontent"><input name="orgnumber" <%=isDisabled%> type="text" 
				 validator="string" cnname="机构编号" value="<pg:cell colName="orgnumber"  defaultValue=""/>"
				 <%if(orgNumberHiberarchy && parentOrgId.equals("0")){
				  		out.print("readonly='true' ");
				  	}else if(orgNumberHiberarchy){
				  		//out.print("onblur='checkOrgNumber(this)'");
				  	}
				   %>></td>	
	<th><pg:message code="sany.pdp.role.organization.name"/>：</th>
    <td class="detailcontent"><input name="orgName" <%=isDisabled%> type="text" 
				 validator="string" cnname="机构名称" value="<pg:cell colName="orgName"  defaultValue=""/>"></td></tr>
  <tr>
    <th><pg:message code="sany.pdp.role.organization.sort.number"/>：</th>
    <td class="detailcontent"><input name="orgSn" <%=isDisabled%> type="text"  readonly
				  value="<pg:cell colName="orgSn"  defaultValue=""/>"></td>	
	<th><pg:message code="sany.pdp.role.organization.description"/>：</th>
    <td class="detailcontent"><input name="orgdesc" <%=isDisabled%> type="text" 
				  value="<pg:cell colName="orgdesc"  defaultValue=""/>"></td></tr>
  <tr>
    <th><pg:message code="sany.pdp.simple.spell"/>：</th>
    <td class="detailcontent"><input name="jp" <%=isDisabled%> type="text" 
				  value="<pg:cell colName="jp"  defaultValue=""/>"></td>	
	<th><pg:message code="sany.pdp.complete.spell"/>：</th>
    <td class="detailcontent"><input name="qp" <%=isDisabled%> type="text" 
				  value="<pg:cell colName="qp"  defaultValue=""/>"></td>
 </tr>
	<tr>
    <th><pg:message code="sany.pdp.role.organization.check"/>：</th>
	<td class="detailcontent">
		<select name="remark3" <%=isDisabled%> >
			<option label="" value="0" 
			<pg:equal colName="remark3" value="0">selected </pg:equal>><pg:message code="sany.pdp.invalid"/>
			<option label="" value="1" 
			<pg:equal colName="remark3" value="1">selected </pg:equal>><pg:message code="sany.pdp.valid"/>
    	</select>
	</td>
	<th><pg:message code="sany.pdp.show.name"/>：</th>
    <td class="detailcontent"><input name="remark5" type="text" 
				 validator="string" cnname="机构显示名称" value="<pg:cell colName="remark5"  defaultValue=""/>"></td>
	</tr>
	<input type=hidden name=ispartybussiness value=0>
  </table></pg:beaninfo>
           <div align="center">              	
      <a class="bt_1" onclick="modifyorg()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a> 
      <a class="bt_2" onclick="reset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
     
      </div>
</form>
</center></div>
<div id=divProcessing style="width:200px;height:30px;position:absolute;left:200px;top:260px;display:none">
			<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
				<tr>
					<td bgcolor=#3A6EA5>
						<marquee align="middle" behavior="alternate" scrollamount="5">
							<font color=#FFFFFF><pg:message code="sany.pdp.common.operation.processing"/></font>
						</marquee>
					</td>
				</tr>
			</table>
		</div>
</body>
<iframe name="hiddenFrame" width=0 height=0 frameborder=0></iframe>
</html>
