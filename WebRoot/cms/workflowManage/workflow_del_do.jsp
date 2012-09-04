<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.frameworkset.platform.cms.workflowmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%

	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);
	
	String[] workflowIds = request.getParameterValues("checkBoxOne");

	StringBuffer errormsg = new StringBuffer();
	String siteCite = "";//new StringBuffer();//
	String channelCite = "";//new StringBuffer();//
	String documentCite = "";//new StringBuffer();//
	List list = new ArrayList();
	
	WorkflowManager workflowManager = new WorkflowManagerImpl();
	
	for(int i = 0;i < workflowIds.length;i ++)
	{
		Map map = workflowManager.getWorkflowCiteInfo(workflowIds[i]);
		
		siteCite = (String)map.get("siteCite");
		channelCite = (String)map.get("channelCite");
		documentCite = (String)map.get("documentCite");

		if(!siteCite.equals("") || !channelCite.equals("") || !documentCite.equals(""))
		{
			errormsg.append((String)map.get("title") + ",");
		}
		else
		{
			list.add(workflowIds[i]);
		}
	}

	if(list.size() != workflowIds.length)
	{
		workflowIds = new String[list.size()];
		for(int i = 0;i < list.size();i ++)
		{
			workflowIds[i] = (String)list.get(i);
		}
	}
	
	try
	{
		boolean flag = workflowManager.delWorkflow(workflowIds);
		
		//smi.logCreateChannel(chnlobj,request,response);
		if(flag)
		{
			if(errormsg.toString().equals(""))
			{
%>
		<script language="javascript">
			alert("删除流程成功!");
			var str = parent.document.location.href;
			var strArray = str.slice(0,str.indexOf("?"));
			parent.document.location.href = strArray+"?"+parent.document.all.queryString.value;
			window.close();
		</script>
<%		}else{%>
			<script language="javascript">
				alert("流程“<%=errormsg.toString()%>”已被站点、频道、文档引用，不能删除！");
				var str = parent.document.location.href;
				var strArray = str.slice(0,str.indexOf("?"));
				parent.document.location.href = strArray+"?"+parent.document.all.queryString.value;
				window.close();
			</script>
<%		}}else{%>
			<script language="javascript">
				alert('删除流程出错！');
				//防止二次提交
				//parent.window.document.all("button")[0].disabled = false;
				//parent.window.document.all("button")[1].disabled = false;
			</script>
<%
		}
	}
	catch(Exception e)
	{
%>
	<script language="javascript">
		alert('删除流程出错！');
		//防止二次提交
		//parent.window.document.all("button")[0].disabled = false;
		//parent.window.document.all("button")[1].disabled = false;
	</script>
<%
	}
%>
