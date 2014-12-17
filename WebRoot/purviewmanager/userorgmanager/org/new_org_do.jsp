<%
/**
 * <p>Title: 新增一级机构</p>
 * <p>Description: 新增一级机构提交处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.util.StringUtil
				,com.frameworkset.platform.security.AccessControl
				,com.frameworkset.platform.sysmgrcore.entity.Organization
				,com.frameworkset.common.poolman.DBUtil
				,com.frameworkset.platform.sysmgrcore.manager.OrgManager
				,com.frameworkset.platform.sysmgrcore.manager.JobManager
				,com.frameworkset.platform.sysmgrcore.entity.Orgjob
				,com.frameworkset.platform.sysmgrcore.entity.Job
				,com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator
				,com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl
				,com.frameworkset.platform.sysmgrcore.entity.User
				,com.frameworkset.platform.sysmgrcore.manager.LogManager
				,java.util.List
				,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase
				,com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<script language="javascript">
	var api = parent.frameElement.api, W = api.opener;
</script>					
<%
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkManagerAccess(request,response);

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
			
			String orgName=org.getOrgName();
			String remark5=org.getRemark5();
			
			boolean tag = true;
			String notice = RequestContextUtils.getI18nMessage("sany.pdp.add.failed", request);
			
	         DBUtil db = new DBUtil();
	        //查看机构编号是否存在(全局唯一)
	        //baowen.liu
	        db.executeSelect("select orgnumber from TD_SM_ORGANIZATION where orgnumber='"+ org.getOrgnumber() + "'");
			if (db.size() > 0) {
				tag = false;
				notice = RequestContextUtils.getI18nMessage("sany.pdp.organization.number.exist", request);
			}
			
			//查看机构名称是否存在(全局唯一)
			//baowen.liu
			db.executeSelect("select org_name from TD_SM_ORGANIZATION where org_name='"+ orgName+"'");
			if( db.size()>0){
			    
			    tag = false;
				notice = RequestContextUtils.getI18nMessage("sany.pdp.organization.name.exist", request);
			
			}
			//查看机构显示名称是否存在(同级唯一)
			//baowen.liu
	        db.executeSelect("select remark5 from TD_SM_ORGANIZATION where parent_id='0' and remark5='"+remark5+"'");
			if( db.size()>0){
			    
			    tag = false;
				notice = RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.add.dispalyname.same", request);
		
			}
	         	
			
			if(tag){
			
				OrgManager orgManager = SecurityDatabase.getOrgManager();
				JobManager jobManager = SecurityDatabase.getJobManager();
				org.setOrgId(null);
	
				// 吴卫雄添加：为了实现与LDAP同步时将当前机构添加到父机构的 member 属性下
				if (org.getParentId() != null && org.getParentId().length() > 0) {
					Organization parentOrg = orgManager.getOrgById(org
							.getParentId());
					org.setParentOrg(parentOrg);
				} else
					org.setParentId("0");
				// 吴卫雄添加结束
	
				boolean r = orgManager.insertOrg(org);
				if (!r) {
					tag = false;
				}
				else
				{
					org = orgManager.getOrgByName(org.getOrgName());
	
					// 王卓添加，添加机构时添加一个待岗岗位		
					Orgjob oj = new Orgjob();
					Job job = new Job();
					job.setJobId("1");
					oj.setJob(job);
					oj.setJobSn(Integer.valueOf("999"));
					Organization org1 = new Organization();
					org1.setOrgId(org.getOrgId());
					request.setAttribute("orgId", org.getOrgId());
					oj.setOrganization(org1);
		
					//保存机构和岗位关系,方法中已经判断机构和岗位的关系是否存在,如果存在就是更新,否则就是新增
					orgManager.storeOrgjob(oj);
		
					//--记日志-----------------
					
					String operContent = "";
					String operSource = accesscontroler.getMachinedID();
					String openModle = "机构管理";
					String userName = accesscontroler.getUserName();
					String description = "";
					LogManager logManager = SecurityDatabase.getLogManager();
					operContent = userName + " 新增了机构 " + org.getOrgName();
					description = "";
					logManager.log(accesscontroler.getUserAccount() ,
							operContent, openModle, operSource, description);
		
					
				}
			}
			if(tag)
			{
				%>
				<script>
				W.$.dialog.alert('<pg:message code="sany.pdp.userorgmanager.org.add.success"/>', function(){
					W.location.reload();
					api.close();
				}, api);
				</script>
				<%
			}
			else
			{
			%>
				<script>
				W.$.dialog.alert("<%=notice%>", function() {
					W.location.reload();
					api.close();
				}, api);
				</script>
			<%
			}
		%>
<script>
    window.onload = function prompt(){
    }
</script>
