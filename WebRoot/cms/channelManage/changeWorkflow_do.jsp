<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
try {
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);


	String workFlowFrom = request.getParameter("workFlowFrom");
	if(workFlowFrom==null || workFlowFrom.trim().length()==0){
		workFlowFrom = "0";
	}
	String channelId = request.getParameter("channelId");
	if(channelId == null || channelId.trim().length()==0){
		throw new ChannelManagerException("频道id为null,不知道要更新那个频道的流程");
	}
	String workflow = request.getParameter("workFlow");
	if(workflow==null || workflow.trim().length()==0){
		workflow = "0";
	}

	ChannelManager cm = new ChannelManagerImpl();
	cm.changeWorkflow(channelId,Integer.parseInt(workflow),Integer.parseInt(workFlowFrom));
%>
<script language="javascript">
	alert("编辑频道流程成功!");
	parent.window.returnValue = true;
	parent.close();
</script>
<%}catch (ChannelManagerException e) {
		e.printStackTrace();
%>
<script language="javascript">
	alert('编辑频道流程出错,请检查!原因是:\n<%=e.getMessage().replaceAll("\n", "").replaceAll("'","")%>');
</script>
<%
}
%>
