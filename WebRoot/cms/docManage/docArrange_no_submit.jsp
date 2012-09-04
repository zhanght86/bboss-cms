<%
/**
  *	设定该频道下所有置顶文档的权重
  * 按置顶文档的排列顺序的先后决定权重，权重NO越大的排前
  * 通过置顶文档列表的“上移”，“下移”来改变排列顺序
  */
%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.bean.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.LogManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>

<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	
	DocumentManager dm = new DocumentManagerImpl();
	ChannelManager cm = new ChannelManagerImpl();
	SiteManager siteManager = new SiteManagerImpl();
	
	String channelid = request.getParameter("channelid");
	String siteid = request.getParameter("siteid");
	
	Site site = siteManager.getSiteInfo(siteid);
	Channel channel = cm.getChannelInfo(channelid);
	
	String userid = accesscontroler.getUserID();
	String docid = request.getParameter("docid");
	
	String[] docids = docid.split(":");
	
	//更新置顶顺序
	boolean b = dm.updateArrangeDocOrderNo(docids);
	//记录操作日志
	LogManager logManager = SecurityDatabase.getLogManager();
	logManager.log(accesscontroler.getUserAccount(),"更新置顶文档顺序.频道:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】","文档管理",request.getRemoteAddr(),"");
	/*
	try
	{
		//初始化发布引擎
	 	WEBPublish publish = new WEBPublish();
		publish.init(request,response,pageContext,accesscontroler);
		PublishCallBack callback = new PublishCallBackImpl();
		publish.setPublishCallBack(callback);
		
		publish.recursivePubObjectOfChannelAncester(siteid,channelid,RecursivePublishManager.RECURSIVE_PUB_CHANGE_ARRANGEDOCNO);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	*/
	%>
	<html>
		<body>
		<iframe name="recursiveFrame" src="recursiveHandle.jsp?siteId=<%=siteid%>&channelId=<%=channelid%>&action=<%=RecursivePublishManager.RECURSIVE_PUB_CHANGE_ARRANGEDOCNO%>"></iframe>
		</body>
	</html>
	<%
	
	if(b){%>
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("保存文档置顶顺序成功！");
			window.parent.document.all("reload").click();
			//window.close();
		</script>
	
	<%}else{%>
		
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("保存文档置顶顺序失败！");
			//window.close();
		</script>
	<%
		}
	%>			