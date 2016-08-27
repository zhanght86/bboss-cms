<%
/*
 * <p>Title: 为岗位选择机构的保存处理页面</p>
 * <p>Description:为岗位选择机构的保存处理页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-25
 * @author baowen.liu
 * @version 1.0
 */
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById,
                 com.frameworkset.platform.sysmgrcore.manager.OrgManager,
                 com.frameworkset.platform.sysmgrcore.manager.JobManager,
                 com.frameworkset.platform.sysmgrcore.entity.Job,
                 com.frameworkset.platform.sysmgrcore.entity.Organization,
                 com.frameworkset.platform.sysmgrcore.entity.Orgjob"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>	
<%
	
		//---------------START--用户组管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle=RequestContextUtils.getI18nMessage("sany.pdp.jobmanage", request);
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager();
        OrgManager orgManager = SecurityDatabase.getOrgManager(); 		
		//---------------END
		String jobId = request.getParameter("jobId");
		String orgId = request.getParameter("orgId");
		//1为增加机构岗位关系，0为删除机构岗位关系
		String flag = request.getParameter("flag");
		
		String orgIds[] =orgId.split("\\,");
		//--用户组管理写操作日志	
		String orgNames_log = LogGetNameById.getOrgNamesByOrgIds(orgIds);
		String jobName_log = LogGetNameById.getJobNameByJobId(jobId);
		String[] arr_jobid = {jobId};
		if("0".equals(flag)){
			//System.out.println("----------------岗位编号："+jobId);
			operContent=RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.modify.job.organization", request)
			+","+RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.job.name", request)+"："+jobName_log
			+RequestContextUtils.getI18nMessage("sany.pdp.workflow.organization", request)+"："+orgNames_log; 	 						
			description="";
			logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);
			
			for(int i = 0; i < orgIds.length; i++){
				orgManager.deleteOrgjob(orgIds[i],arr_jobid);
			}
		}else{
		//System.out.println("----------------岗位编号："+jobId);
			operContent=RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.modify.job.organization", request)
			+","+RequestContextUtils.getI18nMessage("sany.pdp.jobmanage.job.name", request)+"："+jobName_log
			+RequestContextUtils.getI18nMessage("sany.pdp.workflow.organization", request)+"："+orgNames_log; 						
			description="";
			logManager.log(control.getUserAccount(),operContent,openModle,operSource,description);
			
			for(int i = 0; i < orgIds.length; i++){
				orgManager.addOrgjob(orgIds[i],arr_jobid,"1");
			}
		}
		
%>
<script>
	var api = parent.parent.frameElement.api, W = api.opener;
    window.onload = function prompt(){
    	W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
    }
</script>

