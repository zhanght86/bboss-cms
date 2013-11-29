<%@page import="com.frameworkset.common.poolman.SQLExecutor"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
/**
 * <p>Title: 机构转移页面</p>
 * <p>Description: 机构转移处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl
				,com.frameworkset.platform.sysmgrcore.entity.Organization
				,com.frameworkset.common.poolman.DBUtil
				,com.frameworkset.platform.sysmgrcore.manager.OrgManager
				,com.frameworkset.platform.sysmgrcore.manager.LogManager
				,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase
				,com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
				
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<script language="javascript">
	var api = parent.frameElement.api, W = api.opener;
</script>	

<%
		//boolean tag = false;
		AccessControl control = AccessControl.getInstance();
				control.checkManagerAccess(request,response);
		String tag = "0";
		boolean flag=true;
		String notice = RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.transfer.fail.admin", request);
		
		String parentId = request.getParameter("parentId");
		String orgId = request.getParameter("orgId");
		Organization org = OrgCacheManager.getInstance().getOrganization(orgId);
		
		String remark5 = org==null?"":org.getRemark5();
		OrgManager orgManger = SecurityDatabase.getOrgManager();
		//机构转移时判断是否有显示名称同名的机构
		//baowen.liu
		
		int size = SQLExecutor.queryObject(int.class,"select count(remark5) from TD_SM_ORGANIZATION org where PARENT_ID=? and remark5=?",parentId,remark5);
		if( size> 0 ){
		  notice = RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.transfer.name.exist", request);
		  flag=false;
		}
		
		if(flag){
			
			
			flag = orgManger.tranOrg(orgId,parentId);
			
			if(flag){
				tag = "2";
				//记录日志
				//---------------START--
				
				String operContent="";        
		        String operSource=control.getMachinedID();
		        String openModle=RequestContextUtils.getI18nMessage("sany.pdp.organization.manage", request);
		        String userName = control.getUserName();
		        LogManager logManager = SecurityDatabase.getLogManager(); 
		        operContent = RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.transfer.to.otherorg", new Object[] {userName, LogGetNameById.getOrgNameByOrgId(orgId), LogGetNameById.getOrgNameByOrgId(parentId)}, request);
		        logManager.log(control.getUserAccount() ,operContent,openModle,operSource,""); 
				//---------------END
			}
        
		}	
		if(tag.equals("1"))
		{
			%>
			<script>
			    parent.alertfun('<pg:message code="sany.pdp.userorgmanager.org.transfer.to.self.no"/>');
			</script>
			<%
		}
		else if(tag.equals("2"))
		{
		%>
			<script>
			parent.alertfun('<pg:message code="sany.pdp.userorgmanager.org.transfer.success"/>');
			    //parent.flashSelf("<%=orgId%>","<%=parentId%>");
			</script>
		<%
		}
		else
		{
		%>
			<script>
			parent.alertfun("<%=notice%>");
			</script>
		<%
		}
	%>

