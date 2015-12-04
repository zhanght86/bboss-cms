<%@page import="com.frameworkset.orm.transaction.TransactionManager"%>
<%
/**
  *	Action设置一个文档为置顶文档
  * 
  */
%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.bean.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*"%>

<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	AccessControl accesscontroler = AccessControl.getAccessControl();
	TransactionManager tm = new TransactionManager();
	DocumentManager dm = new DocumentManagerImpl();
	ChannelManager cm = new ChannelManagerImpl();
	SiteManager siteManager = new SiteManagerImpl();
	int docstatus = -1;
	String siteid = null;
	String channelid = null;
	boolean b = false;
	try
	{
	
		tm.begin();
		  channelid = request.getParameter("channelid");
		  siteid = request.getParameter("siteid");
		
		String userid = accesscontroler.getUserID();
		String docid = request.getParameter("documentId");
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		
		Site site = siteManager.getSiteInfo(siteid);
		Channel channel = cm.getChannelInfo(channelid);
		Document doc = dm.getPartDocInfoById(docid);
	
		ArrangeDoc ad = new ArrangeDoc();
		ad.setStartTime(starttime);
		ad.setEndTime(endtime);
		ad.setDocumentId(Integer.parseInt(docid));
		ad.setOpUser(Integer.parseInt(userid));
		ad.setChannelid(Integer.parseInt(channelid));
		//新增置顶
		  b = dm.addArrangeDoc(ad);
		docstatus = dm.getDocStatus(Integer.parseInt(docid));
		tm.commit();
		LogManager logManager = SecurityDatabase.getLogManager();
		logManager.log(accesscontroler.getUserAccount(),"新增置顶文档.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",com.frameworkset.util.StringUtil.getClientIP(request),"");
	}
	finally
	{
		tm.release();
	}
	//记录操作日志
	
	
	if(docstatus == 5)//只有已发的文档才要递归
	{
		/*
		try
		{
			//初始化发布引擎
		 	WEBPublish publish = new WEBPublish();
			publish.init(request,response,pageContext,accesscontroler);
			PublishCallBack callback = new PublishCallBackImpl();
			publish.setPublishCallBack(callback);
			
			publish.recursivePubObjectOfChannelAncester(siteid,channelid,RecursivePublishManager.RECURSIVE_PUB_ADD_ARRANGEDOC);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		*/
	%>
	<html>
		<body>
		<iframe name="recursiveFrame" src="recursiveHandle.jsp?siteId=<%=siteid%>&channelId=<%=channelid%>&action=<%=RecursivePublishManager.RECURSIVE_PUB_ADD_ARRANGEDOC%>"></iframe>
		</body>
	</html>
	<%
	}
	
	if(b){%>
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("设置置顶文档成功！");
			var str = window.dialogArguments.location.href;
			var end = str.indexOf("?");
			var strArray;
			if(end != -1)
				strArray= str.slice(0,end);
			else
				strArray = str;
			window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
			window.close();
		</script>
	
	<%}else{%>
		
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("设置置顶文档失败！");
			window.close();
		</script>
		
	<%
		}
	%>			