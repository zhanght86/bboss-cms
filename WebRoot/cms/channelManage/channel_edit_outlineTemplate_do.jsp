<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.util.StringOperate"%>
<%
try{
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String siteName = request.getParameter("siteName");
	String siteId   = request.getParameter("siteId");
	String channelId = request.getParameter("channelId");
	String channelName = request.getParameter("channelName");
	String ddIndexTemplate = request.getParameter("outlineTemplateId");
 
	ChannelManager cm = new ChannelManagerImpl();
	
    cm.updateChannelOutputTemplateId(Integer.parseInt(channelId),Integer.parseInt(ddIndexTemplate));
 
	%>
	<script language="javascript">
		alert("更改模板成功!");
		parent.updateParentForm();
		parent.close();
		</script>
<%	}catch(Exception e){%>
		<script language="javascript">
		alert('更改模板出错,请检查!原因是:\n<%=e.getMessage().replaceAll("\n","").replaceAll("'","")%>');
		</script>
<%}%>