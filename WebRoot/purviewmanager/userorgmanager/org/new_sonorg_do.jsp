<%
/**
 * <p>Title: 新增子机构</p>
 * <p>Description: 新增子机构处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
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
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl
				,com.frameworkset.platform.sysmgrcore.entity.Organization
				,com.frameworkset.common.poolman.DBUtil
				,com.frameworkset.platform.sysmgrcore.manager.OrgManager
				,com.frameworkset.platform.sysmgrcore.manager.LogManager
				,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase
				,com.frameworkset.util.StringUtil,
				 org.frameworkset.web.servlet.support.WebApplicationContextUtils,
				 org.frameworkset.spi.support.MessageSource,
                 org.frameworkset.web.servlet.support.RequestContextUtils"%>
<%      
			AccessControl control = AccessControl.getInstance();
				control.checkManagerAccess(request,response);
			Organization org = new Organization();
			org.setOrgdesc(StringUtil.replaceNull(request.getParameter("orgdesc")));
			org.setChargeOrgId(StringUtil.replaceNull(request.getParameter("chargeOrgId")));
			org.setChildren(StringUtil.replaceNull(request.getParameter("children")));
			org.setCode(StringUtil.replaceNull(request.getParameter("code")));
			org.setCreator(StringUtil.replaceNull(request.getParameter("creator")));
			org.setIsdirectguanhu(StringUtil.replaceNull(request.getParameter("isdirectguanhu")));
			org.setIsforeignparty(StringUtil.replaceNull(request.getParameter("isforeignparty")));
			org.setIsjichaparty(StringUtil.replaceNull(request.getParameter("isjichaparty")));
			org.setIspartybussiness(StringUtil.replaceNull(request.getParameter("ispartybussiness")));
			org.setIsdirectlyparty(StringUtil.replaceNull(request.getParameter("isdirectlyparty")));
			org.setJp(StringUtil.replaceNull(request.getParameter("jp")));
			org.setLayer(StringUtil.replaceNull(request.getParameter("layer")));
			org.setOrg_level(StringUtil.replaceNull(request.getParameter("org_level")));
			org.setOrg_xzqm(StringUtil.replaceNull(request.getParameter("org_xzqm")));			
			org.setOrgId(StringUtil.replaceNull(request.getParameter("orgId")));
			org.setOrgName(StringUtil.replaceNull(request.getParameter("orgName")));
			org.setSatrapJobId(StringUtil.replaceNull(request.getParameter("satrapJobId")));
			org.setRemark5(StringUtil.replaceNull(request.getParameter("remark5")));
			org.setRemark4(StringUtil.replaceNull(request.getParameter("remark4")));
			org.setRemark3(StringUtil.replaceNull(request.getParameter("remark3")));
			org.setRemark2(StringUtil.replaceNull(request.getParameter("remark2")));
			org.setRemark1(StringUtil.replaceNull(request.getParameter("remark1")));
			org.setQp(StringUtil.replaceNull(request.getParameter("qp")));
			org.setPath(StringUtil.replaceNull(request.getParameter("path")));
			org.setParentId(StringUtil.replaceNull(request.getParameter("parentId")));
			org.setOrgSn(StringUtil.replaceNull(request.getParameter("orgSn")));
			org.setOrgnumber(StringUtil.replaceNull(request.getParameter("orgnumber")));
			
			String orgname=org.getOrgName();
			
			boolean tag = true;
			String notice = RequestContextUtils.getI18nMessage("sany.pdp.add.failed", request);//"<pg:message code='sany.pdp.add.failed'/>";
			
			DBUtil db = new DBUtil();
			//查看机构编号是否存在,全局唯一
			//baowen.liu
			db.executeSelect("select orgnumber from TD_SM_ORGANIZATION where orgnumber='"+ org.getOrgnumber()+"'");
			if(db.size()>0){
				tag = false;
				notice = RequestContextUtils.getI18nMessage("sany.pdp.organization.number.exist", request);
			}
				
			//查看机构名称是否存在,全局唯一
 			//baowen.liu
			db.executeSelect("select org_name from TD_SM_ORGANIZATION where org_name='"+ orgname+"'");
			if( db.size()>0){
			    tag = false;
			    notice =  notice = RequestContextUtils.getI18nMessage("sany.pdp.organization.name.exist", request);
			}
			
			if(tag){	
				OrgManager orgManager = SecurityDatabase.getOrgManager();
				//org.setOrgId(null);
	
				//吴卫雄添加：为了实现与LDAP同步时将当前机构添加到父机构的 member 属性下
				String parentId = org.getParentId(); 
				if (parentId != null && parentId.length() > 0) {
					//Organization parentOrg = orgManager.getOrgById(parentId);
					//org.setParentOrg(parentOrg);
				} else{
					org.setParentId("0");
				}
				//－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－
			
				boolean r = orgManager.insertOrg(org);
				if(!r)//机构是否添加成功
				{
					tag = false;
				}
				else
				{
					//--记日志---------
					MessageSource messageSource = WebApplicationContextUtils.getWebApplicationContext();
					String operContent="";        
			        String operSource=control.getMachinedID();
			        String openModle=messageSource.getMessage("sany.pdp.organization.manage",RequestContextUtils.getRequestContextLocal(request));
			        String userName = control.getUserName();
			        String description="";
			        LogManager logManager = SecurityDatabase.getLogManager(); 		
					operContent=userName+messageSource.getMessage("sany.pdp.role.organization.new.child",RequestContextUtils.getRequestContextLocal(request))+org.getOrgName(); 
					 description="";
			        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);       
					//----------------------
					
				}
			
      		}
			if(tag)
			{
				%>
				<script>
				var api = parent.frameElement.api, W = api.opener;
				W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				</script>
				<%
			}
			else
			{
			%>
				<script>
				var api = parent.frameElement.api, W = api.opener;
				W.$.dialog.alert("<%=notice%>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				</script>
			<%
			}
		%>
<script>
    window.onload = function prompt(){
        //parent.divProcessing.style.display="none";
        //parent.document.getElementById("saveButton").disabled = false;
		//parent.document.getElementById("resetButton").disabled = false;
		//parent.document.getElementById("backButton").disabled = false;
    }
</script>
