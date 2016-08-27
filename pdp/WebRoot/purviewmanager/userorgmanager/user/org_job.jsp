<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="java.util.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.UserJobRole" %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	
	String userId = request.getParameter("userId");
	String roleId = request.getParameter("roleId");
	RoleManager roleManager = SecurityDatabase.getRoleManager();
	List orgJob = null;
	if(userId != null && roleId != null && !userId.equals("") && !roleId.equals("")){
		orgJob = roleManager.getOrgJobList(userId, roleId);
	}
%>
	<SCRIPT LANGUAGE="JavaScript">
		var sel = parent.document.all.userjobList;
		if(sel)
		{
			var count = sel.options.length;
			for(var i=0;i<=count;i++)
			{
				sel.options.remove(0);
			}
		}
		
		//alert("总个数:"+sel.options.length)
	</SCRIPT>

<%
	for(int i = 0; orgJob!=null && i < orgJob.size(); i++){
		UserJobRole u = (UserJobRole)orgJob.get(i);
		String orgName = u.getOrgName();
		String jobName = u.getJobName();
		if(orgName != null && jobName != null){
%>
			<SCRIPT LANGUAGE="JavaScript">
				//alert(sel.options.length)
				var oOption = parent.document.createElement("OPTION");
				sel.options.add(oOption);
				oOption.innerText = "<%=orgName%>-<%=jobName%>";
				oOption.value = "<%= u.getRoleId() %>";
			</SCRIPT>
<%
		}
	}
%>

