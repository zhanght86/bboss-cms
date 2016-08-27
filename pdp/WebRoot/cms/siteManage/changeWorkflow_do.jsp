<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
try {
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String workFlowFrom = request.getParameter("workFlowFrom");
	if(workFlowFrom==null || workFlowFrom.trim().length()==0){
		workFlowFrom = "0";
	}
	String siteId = request.getParameter("siteId");
	String workflow = request.getParameter("workFlow");
	if(workflow==null || workflow.trim().length()==0){
		workflow = "0";
	}

	SiteManager smi = new SiteManagerImpl();
	smi.changeWorkflow(siteId,Integer.parseInt(workflow),Integer.parseInt(workFlowFrom));
%>
<script language="javascript">
	alert("编辑站点流程成功!");
	parent.updateParentForm();
	parent.close();
</script>
<%}catch (SiteManagerException e) {
		e.printStackTrace();
%>
<script language="javascript">
	alert('编辑站点流程出错,请检查!原因是:\n<%=e.getMessage().replaceAll("\n", "").replaceAll("'","")%>');
</script>
<%
}
%>
