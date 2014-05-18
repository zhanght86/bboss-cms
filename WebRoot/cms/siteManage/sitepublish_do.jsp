<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.driver.publish.*,java.util.List,
				com.frameworkset.platform.cms.driver.publish.impl.*,
				com.frameworkset.platform.cms.driver.distribute.DistributeManager,
				com.frameworkset.platform.cms.sitemanager.*,
				com.frameworkset.platform.security.AccessControl"%>
<script language="javascript">
	function hideMarquee(){
		if(parent.document.getElementById("waiting_marquee"))
			parent.document.getElementById("waiting_marquee").style.display="none"; 
		if(parent.document.getElementById("publishButton"))
			parent.document.getElementById("publishButton").disabled="none";
		if(parent.document.getElementById("closeButton"))
			parent.document.getElementById("closeButton").disabled="";   
		if(parent.document.getElementById("increamentall"))
			parent.document.getElementById("increamentall").disabled="";
		if(parent.document.getElementById("increamentno"))
			parent.document.getElementById("increamentno").disabled="";  
		if(parent.document.getElementById("clearCacheYes"))
			parent.document.getElementById("clearCacheYes").disabled="";
		if(parent.document.getElementById("clearCacheNo"))
			parent.document.getElementById("clearCacheNo").disabled="";  
		if(parent.document.getElementById("PUBLISH_SITE_INDEX"))
			parent.document.getElementById("PUBLISH_SITE_INDEX").disabled="";
		if(parent.document.getElementById("PUBLISH_CHANNEL_INDEX"))
			parent.document.getElementById("PUBLISH_CHANNEL_INDEX").disabled="";
		if(parent.document.getElementById("PUBLISH_CHANNEL_RECINDEX"))
			parent.document.getElementById("PUBLISH_CHANNEL_RECINDEX").disabled="";  
		if(parent.document.getElementById("PUBLISH_CHANNEL_CONTENT"))
			parent.document.getElementById("PUBLISH_CHANNEL_CONTENT").disabled="";
		if(parent.document.getElementById("PUBLISH_CHANNEL_RECCONTENT"))
			parent.document.getElementById("PUBLISH_CHANNEL_RECCONTENT").disabled="";
        if(parent.document.getElementById("isRecordMsg1"))
            parent.document.getElementById("isRecordMsg1").disabled="";
        if(parent.document.getElementById("isRecordMsg2"))
            parent.document.getElementById("isRecordMsg2").disabled="";
	}
</script>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String actionType = request.getParameter("actionType");
	String siteId = request.getParameter("siteId");//站点id
	String channelId = request.getParameter("channelId");//频道id

	SiteManager sm = new SiteManagerImpl();

	//发布范围数组
	String[] publishScopeStr = request.getParameter("publishScope").split(",");
	int[] publishScope = new int[publishScopeStr.length];
	for(int i=0;i<publishScopeStr.length;i++)
	{
		publishScope[i] = Integer.parseInt(publishScopeStr[i]);
	}
	//本地发布和远程发布标识boolean二维数组
	//定义发布目的地:0:本地 1:远程 2:远程本地
	boolean[] local2ndRemote = sm.getSitePublishDestination(siteId);
	
	//标识增量发布，还是完全发布
	String increamentStr = request.getParameter("increament");
	boolean increament = false;
	if("true".equals(increamentStr))
			increament = true;
	if("false".equals(increamentStr))
			increament = false;
	
	//是否清除缓存
	boolean isClearCache = Boolean.parseBoolean(request.getParameter("clearCache"));
	
	//分发方式数组
	int[] distributeManners = sm.getSiteDistributeManners(siteId);
    String uuid = request.getParameter("uuid");
    String isRecordValue = request.getParameter("isRecordValue");
    
    
	WEBPublish publish = new WEBPublish();
	publish.init(request,response,pageContext,accessControl);
	
	//对APPPublish设值
	((APPPublish)publish).setClearFileCache(isClearCache);
	
	PublishCallBack callback = new PublishCallBackImpl();
	publish.setPublishCallBack(callback);
    
    //外部注入monitor
    PublishMonitor monitor = PublishMonitor.createPublishMonitor();
    monitor.setUuid(uuid);
    //外部控制是否添加日志
    if("false".equalsIgnoreCase(isRecordValue)){
        monitor.setNotRecordMsg(true);
        //System.out.println("monitor----------------"+monitor.isNotRecordMsg());
    }else{
        monitor.setNotRecordMsg(false);
    }
    publish.setMonitor(monitor);
    
    boolean successFlag = false;
	String pageUrl = "";
	String alertmsg = "";
    String typemsg = "";
	if(actionType == null || actionType.equals("publish"))
	{
		if("null".equals(channelId))
		{
			try
			{
				publish.publishSite(siteId,//站点id
								    publishScope,//发布范围数组，将各种发布范围进行组合
								    local2ndRemote, //本地发布和远程发布标识boolean二维数组，boolean[0]的值区分是否本地发布，boolean[1]区分是否远程ftp发布
								    increament,//标识增量发布，还是完全发布
								    distributeManners);//存放发布结果的分发方式数组，可以同时有多种方式，包括DistributeManager.HTML_DISTRIBUTE,DistributeManager.RSS_DISTRIBUTE,DistributeManager.MAIL_DISTRIBUTE
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
		    //alertmsg = "发布成功，要查看站点首页吗？";
            alertmsg = "站点发布成功，要查看首页吗？";
            if("false".equalsIgnoreCase(isRecordValue)){
                alertmsg = "发布成功，要查看站点首页吗？";
            }
            typemsg = "站点";
		}
		else
		{
			publish.publishChannel(siteId,//站点id 
								   channelId,//频道id
								   publishScope,//发布范围数组，将各种发布范围进行组合
								   local2ndRemote,//本地发布和远程发布标识boolean二维数组，boolean[0]的值区分是否本地发布，boolean[1]区分是否远程ftp发布
								   increament,//标识是否增量发布
								   distributeManners);//存放发布结果的分发方式数组，可以同时有多种方式，包括DistributeManager.HTML_DISTRIBUTE,DistributeManager.RSS_DISTRIBUTE,DistributeManager.MAIL_DISTRIBUTE
			//alertmsg = "发布成功，要查看频道首页吗？";
            alertmsg = "频道发布成功!";
            if("false".equalsIgnoreCase(isRecordValue)){
                alertmsg = "发布成功，要查看频道首页吗？";
            }
            typemsg = "频道";
		}
		System.out.println("callback.getPageUrl():" + callback.getPageUrl());
		pageUrl = callback.getPageUrl();
		successFlag = callback.getPublishMonitor().isAllFailed()==true?false:true;
	}
	StringBuffer msgs = new StringBuffer();
	if(monitor != null && !monitor.isNotRecordMsg())
    {
	    List newestMsg = monitor.getAllSuccessMessages();			  
	    for(int i=0;newestMsg != null && i<newestMsg.size();i++){
	    	String m = newestMsg.get(i).toString();
	    	 m = m.replaceAll("\"","\'");
			    m = m.replaceAll("“","\'");
			    m = m.replaceAll("、",", "); 
			    m = m.replaceAll("（"," ("); 
			    m = m.replaceAll("）",") "); 
			    m = m.replaceAll("\"","' ");
	        msgs.append(m+"<br>");
	   } 
	    monitor.clearMSGS();
    }
	if(successFlag==true){
        //session.setAttribute("pageUrl"+uuid,pageUrl);
	%>
		<div id="msg"><%=msgs.toString() %></div>
		<script language="javascript">
            hideMarquee();
            if("<%=isRecordValue%>"=="false"){
    			var see=confirm("<%=alertmsg%>");
    			if(see==true){
    			    try
    			    {
    					window.open("<%=pageUrl%>");
    				}
    				catch(e)
    				{
    					alert("对不起，打开页面[<%=pageUrl%>]报错：请检查站点的域名是否设置正确！");
    				}
    			}
    			parent.window.close();
                
            }else{
               
               
                var infomsg = "<a href=<%=pageUrl%> onclick='window.close()'><%=typemsg%>首页:<%=pageUrl%></a>"
                parent.document.all("linkInfo").innerHTML = infomsg;
                parent.document.getElementById("closeButton").disabled="";
                parent.document.all("publish_info").innerText = document.getElementById('msg').innerText; 
            }
            
		</script>
	<%
	}
	else
	{
		%>
		<div id="msg"><%=msgs.toString() %></div>
		<script language="javascript">
			hideMarquee();            
			if("<%=isRecordValue%>"=="true"){
				parent.document.all("publish_info").innerText = document.getElementById('msg').innerText; 
			}
			alert("<%=typemsg%>发布失败！");
		</script>
		<%
	}
	
	
%>