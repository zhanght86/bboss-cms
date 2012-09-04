
<%@ page language="java" import="java.util.*,com.frameworkset.util.StringUtil,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.manager.*" pageEncoding="UTF-8"%>
<%
	String action = StringUtil.replaceNull(request.getParameter("action"));


	String parentId = StringUtil.replaceNull(request.getParameter("parentId"));
	String orgId=request.getParameter("orgId");
	String resName = request.getParameter("resName");
		OrgManager orgManager = SecurityDatabase.getOrgManager();
		Organization org = orgManager.getOrgById(orgId);

		if (orgId.equals("0")) {
			org = new Organization();
			org.setOrgName("机构树");
		}
	
		session.setAttribute("Organization", org);
		
		String redirect = "orginfo.jsp";
		if(parentId.equals("") || parentId.equals("0") || parentId.equals("null"))
		{

			redirect = "location.href = \"" + request.getContextPath() + "/sysmanager/orgmanager/orginfo.jsp?orgId="+ orgId +"&action="+ action +"&parentId="+ parentId +"&resName="+ resName +"\";";
		}
		else
			redirect = "window.parent.location.href = \"" + request.getContextPath() + "/sysmanager/orgmanager/orginfo.jsp?orgId="+ orgId +"&action="+ action +"&parentId="+ parentId +"&resName="+ resName +"\";";
%>

<script language="javascript">
	window.dialogArguments.parent.location.href = window.dialogArguments.parent.location.href;
	window.close();
</script>


