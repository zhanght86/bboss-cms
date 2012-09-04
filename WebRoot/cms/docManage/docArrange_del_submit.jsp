<%
/**
  *	撤消一个文档的置顶属性
  * action
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
	
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	
	DocumentManager dm = new DocumentManagerImpl();
	ChannelManager cm = new ChannelManagerImpl();
	SiteManager siteManager = new SiteManagerImpl();
	
	String channelid = request.getParameter("channelid");
	String siteid = request.getParameter("siteid");
	
	Site site = siteManager.getSiteInfo(siteid);
	Channel channel = cm.getChannelInfo(channelid);
	Document doc = null;//dm.getPartDocInfoById(docid);
	
	StringBuffer doctitles = new StringBuffer();
	String userid = accesscontroler.getUserID();
	String docid = request.getParameter("docid");

	String[] docids = docid.split(":");
	for( int i=0;i<docids.length;i++)
	{
		doc = dm.getPartDocInfoById(docids[i]);
		doctitles.append(doc.getSubtitle()).append(";");
	}
	//取消置顶
	boolean b = dm.delArrangeDoc(docids);
	//记录操作日志
	LogManager logManager = SecurityDatabase.getLogManager();
	logManager.log(accesscontroler.getUserAccount(),"取消置顶文档.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doctitles.toString(),"文档管理",request.getRemoteAddr(),"");
	/*
	try
	{
		//初始化发布引擎
	 	WEBPublish publish = new WEBPublish();
		publish.init(request,response,pageContext,accesscontroler);
		PublishCallBack callback = new PublishCallBackImpl();
		publish.setPublishCallBack(callback);
		
		publish.recursivePubObjectOfChannelAncester(siteid,channelid,RecursivePublishManager.RECURSIVE_PUB_DEL_ARRANGEDOC);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	*/
	%>
	<html>
		<body>
		<iframe name="recursiveFrame" src="recursiveHandle.jsp?siteId=<%=siteid%>&channelId=<%=channelid%>&action=<%=RecursivePublishManager.RECURSIVE_PUB_DEL_ARRANGEDOC%>"></iframe>
		</body>
	</html>
	<%
	
	if(b){%>
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("取消置顶文档成功！");
			
			try
			{
				window.parent.document.all("reload").click();
				var urlstr = window.parent.dialogArguments.parent.perspective_content.frames[1].frames[0].frames[1].frames[1].document.URL;
				window.parent.dialogArguments.parent.perspective_content.frames[1].frames[0].frames[1].frames[1].location.href = urlstr;
			}
			catch(e)
			{
				var str = window.parent.location.href;
				if(str.indexOf("?")==-1)
				{
					window.parent.location.href = window.parent.location.href;
				}
				else
				{
					try
					{
						var strArray = str.slice(0,str.indexOf("?"));
						window.parent.location.href = strArray+"?"+window.parent.document.all.queryString.value;
					}
					catch(e)
					{
						window.parent.location.href = window.parent.location.href;
					}
				}
				window.close();
			}
			
		</script>
	
	<%}else{%>
		
		<SCRIPT LANGUAGE="JavaScript"> 
			alert("取消置顶文档失败！");
			window.close();
		</script>
	<%
		}
	%>			