<%
/**
 * <p>Title: 机构岗位设置岗位顺序页面</p>
 * <p>Description: 机构岗位设置岗位顺序页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.action.OrgJobAction"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%
		//---------------START--用户组管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="机构管理";
        String userName = control.getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		
		String orgId = request.getParameter("orgId");
		
		//单个移动位置
		String jobId1 = request.getParameter("jobId1");
		String jobId2 = request.getParameter("jobId2");		
		String jobSn = request.getParameter("jobSn");
		
		//upall or downall
		String jobId3 = request.getParameter("jobId0");		
		if(jobId3 == null)
		{
			jobId3 = "";
		}
		String[] jobId =null;
		if(jobId3 != ""){
			jobId = jobId3.split(",");
		}
		
		//机构名字，日志记录使用
		String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);
		
		if(jobId != null && jobId.length > 0){
		
			//--用户组管理写操作日志	
			String jobNames_log = LogGetNameById.getJobNamesByJobIds(jobId);
			operContent="机构下岗位排序,机构："+orgName_log+",岗位新顺序："+jobNames_log; 						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          		
			//--	end	 --	
		    OrgJobAction.allOrgJobSnChangeAjax(orgId,jobId);
		}else{
			//--用户组管理写操作日志	
			String jobName1_log = LogGetNameById.getJobNameByJobId(jobId1);
			String jobName2_log = LogGetNameById.getJobNameByJobId(jobId2);
			operContent="机构下岗位排序,机构："+orgName_log+",将岗位:"+jobName1_log+"　调到岗位:"+jobName2_log+"　前面。"; 						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          		
			//--	end	 --	
			OrgJobAction.orgJobSnChangeAjax(orgId,jobId1,jobId2,jobSn);
		}
%>
<script>
    window.onload = function prompt(){
        parent.parent.parent.$.dialog.alert("操作成功",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
    }
</script>
