<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.workflowmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%

	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	
	String workflowId = request.getParameter("workflowId");
	String[] nodeIds = request.getParameter("nodeIds").split(",");
	
	WorkflowManager workflowManager = new WorkflowManagerImpl();

	boolean flag = workflowManager.addWorkflowNodes(workflowId,nodeIds);
	
	//smi.logCreateChannel(chnlobj,request,response);
	if(flag)
	{
%>
	<script language="javascript">
		alert("操作成功!");
		//var str = window.dialogArguments.location.href;
		//var strArray = str.slice(0,str.indexOf("?"));
		//window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
		window.close();
		</script>
<%	}else{%>
		<script language="javascript">
		alert('操作出错！');
		//防止二次提交
		//parent.window.document.all("button")[0].disabled = false;
		//parent.window.document.all("button")[1].disabled = false;
		</script>
<%}%>
