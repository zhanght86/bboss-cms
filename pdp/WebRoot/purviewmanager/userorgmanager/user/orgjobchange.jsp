
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogGetNameById"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*"%>

<%
		//---------------START--用户组管理写操作日志
		AccessControl control = AccessControl.getInstance();
		control.checkManagerAccess(request,response);
		String operContent="";        
        String operSource=control.getMachinedID();
        String openModle="机构管理";
        String userName = control.getUserAccount();//getUserName();
        String description="";
        LogManager logManager = SecurityDatabase.getLogManager(); 		
		//---------------END
		//多个jobId
		String mutijobId = request.getParameter("jobId");
		String orgId = request.getParameter("orgId");
		String jobSn = request.getParameter("jobSn");
		String[] arr_jobid = mutijobId.split(",");
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		String jobName_log = "";
		String orgName_log = LogGetNameById.getOrgNameByOrgId(orgId);			
		if(jobSn == null)//递归 把岗位设置到子机构
		{
			//--用户组管理写操作日志	
			operContent="递归添加机构及岗位的关系,岗位："+jobName_log+"　机构："+orgName_log; 						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          
			//--	end	 --
			String jobIds = request.getParameter("jobIds");
			orgManager.addSubOrgjob(orgId,jobIds.split(","));		   
		}
		else if(jobSn.equals("-1"))//删除岗位
			{
			//--用户组管理写操作日志	
			operContent="删除机构及岗位的关系,岗位："+jobName_log+"　机构："+orgName_log; 						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          		
			//--	end	 --
			orgManager.deleteOrgjob(orgId,arr_jobid);
		}
		else if(jobSn.equals("-2"))//递归删除岗位
			{
			//--用户组管理写操作日志	
			operContent="递归删除机构及岗位的关系,岗位："+jobName_log+"　机构："+orgName_log; 						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          		
			//--	end	 --
			orgManager.deleteSubOrgjob(orgId,arr_jobid);
		}
		else{//增加岗位
			//--用户组管理写操作日志	
			operContent="添加机构及岗位的关系,岗位："+jobName_log+"　机构："+orgName_log; 						
			description="";
			logManager.log(control.getUserAccount() ,operContent,openModle,operSource,description);          
			//--	end	 --
		    orgManager.addOrgjob(orgId,arr_jobid,jobSn);
		}
%>
<script>
    window.onload = function prompt()
    {
        alert("操作成功!");
        
        parent.divProcessing.style.display="none";
        
        parent.document.all("button1").disabled = false;
		parent.document.all("button2").disabled = false;
		parent.document.all("button3").disabled = false;
		parent.document.all("button4").disabled = false;
		
		//parent.document.all("button11").disabled = false;
		//parent.document.all("button22").disabled = false;
		//parent.document.all("button33").disabled = false;
		//parent.document.all("button44").disabled = false;
		
		parent.document.all("butn").disabled = false;
    }
</script>
