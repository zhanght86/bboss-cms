<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	int userId = Integer.parseInt(accesscontroler.getUserID());
	
	int docId = Integer.parseInt(request.getParameter("docId"));
	int desStatus = Integer.parseInt(request.getParameter("desStatus"));
	int channelId = Integer.parseInt(request.getParameter("channelId"));
	String siteId = request.getParameter("siteId");
	
	
	DocumentManager idm = new DocumentManagerImpl();
	ChannelManager cmi=new ChannelManagerImpl();
	List list =  new ArrayList();
	if(desStatus == 2)
		list = cmi.getAuditorList(channelId+"");
	else if(desStatus == 11)
		list = cmi.getPublisherList(channelId+"");
	int[] executers = new int[list.size()];
	for(int i=0;i<list.size();i++){
		User temp =(User)list.get(i);
		executers[i] = temp.getUserId().intValue();
	}
	list.toArray();
	try{
		idm.updateDocumentStatus(docId,desStatus);
		//对于处于待审待发以及返工状态的文档，为所有拥有权限的人分发任务
		if(desStatus==2 || desStatus==4 || desStatus==11){
			idm.addOneTask(docId,0,desStatus,userId,executers);
		}
		if(desStatus == 5){
			//初始化
			//WEBPublish publish = new WEBPublish();
			//publish.init(request,response,pageContext,accesscontroler);
			//PublishCallBack callback = new PublishCallBackImpl();
			//publish.setPublishCallBack(callback);
			//递归发布文档
			//publish.recursivePubObjectOfDocument(siteId,idm.getPartDocInfoById(docId+""),RecursivePublishManager.ACTIONTYPE_ARCHIVE);
			int action = RecursivePublishManager.ACTIONTYPE_ARCHIVE;
		%>
			<html>
			<body>
			<iframe name="recursiveFrame" src="recursiveHandle.jsp?siteId=<%=siteId%>&docId=<%=docId%>&action=<%=action%>"></iframe>
			</body>
			</html>
		<%
		}
		
		%>
			<script language = "javascript">
				alert("操作成功！");
				window.close();
				var str = window.dialogArguments.location.href;
				var end = str.indexOf("?");
				var strArray;
				if(end != -1)
				 	strArray= str.slice(0,end);
				else
					strArray = str;
				window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
			</script>	
		<%
	}catch(Exception e){
		e.printStackTrace();
	%>
		<script language="javascript">
			alert("数据库操作失败！");
		</script>
	<%
	}
%>