<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
try {
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	String channelId = request.getParameter("channelId");
	if(channelId == null || channelId.trim().length()==0){
		throw new ChannelManagerException("频道id为null!");
	}

	ChannelManager cm = new ChannelManagerImpl();
	cm.changeIndexPic(channelId,request.getParameter("outlinePicture"));
%>
<script language="javascript">
	alert("编辑频道概览图片成功!");
	parent.window.returnValue = true;
	parent.close();
</script>
<%}catch (ChannelManagerException e) {
		e.printStackTrace();
%>
<script language="javascript">
	alert('编辑频道概览图片出错,请检查!原因是:\n<%=e.getMessage().replaceAll("\n", "").replaceAll("'","")%>');
</script>
<%
}
%>
