<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.driver.publish.*,
				com.frameworkset.platform.cms.driver.publish.impl.*,
				com.frameworkset.platform.cms.driver.distribute.DistributeManager,
				com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String siteId = request.getParameter("siteId");//站点id
	String channelId = request.getParameter("channelId");//频道id

	WEBPublish publish = new WEBPublish();
	publish.init(request,response,pageContext,accessControl);
	PublishCallBack callback = new PublishCallBackImpl();
	publish.setPublishCallBack(callback);
	
	if("".equals(channelId)||channelId==null)
	{
		publish.viewSite(siteId);
	}
	else
	{
		publish.viewChannel(siteId,channelId);
	}
	String viewUrl = "../../" + callback.getViewUrl();
	%>
	<script language="javascript">
		parent.window.close();
		window.open("<%=viewUrl%>");
	</script>