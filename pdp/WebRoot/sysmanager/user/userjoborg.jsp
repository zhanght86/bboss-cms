<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager,com.frameworkset.platform.sysmgrcore.manager.OrgManager
				,com.frameworkset.platform.sysmgrcore.manager.JobManager,java.util.List" %>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager
				,java.util.ArrayList" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(pageContext);
	
	//String userId = (String)session.getAttribute("userId");
	String userId = request.getParameter("userId");
		if (userId == null) {
			out.println("用户不存在");
			return;
		} 
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(userId);
  	String userName= user.getUserRealname();
	
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		String orgid = request.getParameter("orgId");
		//获取当前的操作所在的机构
		Organization org = OrgCacheManager.getInstance().getOrganization(orgid);
		
		
		JobManager jobManager = SecurityDatabase.getJobManager();
			
		List existJob = jobManager.getJobList(org,user);//用户在当前机构下的岗位
		List allJob = jobManager.getJobList(org);	
		
		//权限过滤， 能进行岗位授予的岗位必须是：当前登录用户（包括部门管理员）必须对这些岗位有机构设置的权限。	
		List list1 = new ArrayList();
	    if(existJob != null)
	    {
	    	for(int i=0;i<existJob.size();i++)
	    	{
	    		Job job = (Job)existJob.get(i);
	    		if(accessControl.checkPermission(job.getJobId(), "orgset", AccessControl.JOB_RESOURCE))
	    		{
	    			list1.add(job);
	    		}
	    	}
	    }
	    List list2 = new ArrayList();
	    if(allJob != null)
	    {
	    	for(int i=0;i<allJob.size();i++)
	    	{
	    		Job job = (Job)allJob.get(i);
	    		if(accessControl.checkPermission(job.getJobId(), "orgset", AccessControl.JOB_RESOURCE))
	    		{
	    			list2.add(job);
	    		}
	    	}
	    }
		session.setAttribute("existJob", list1);
		session.setAttribute("allJob", list2);
%>
<html>
  <head>
  <title>用户【<%=userName%>】隶属岗位</title>
  </head>
<frameset rows="*" cols="100%" framespacing="3" frameborder="yes" border="0" >
<frame src="../user/changeJob_ajax.jsp?userId=<%=userId%>&orgId=<%=orgid%>" name="lusersys1" id="lusersys1">
<!--frame src="../user/rusersys_ajax.jsp" name="rusersys1" id="rusersys1"-->
</frameset>
</html>
