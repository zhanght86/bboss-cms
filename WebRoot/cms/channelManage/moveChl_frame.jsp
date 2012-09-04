<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	//String channelid = request.getParameter("channelid");
	String siteid = request.getParameter("siteid");
%>
<script language="javascript">
	
</script>
<html>
	<head>
		<title>.::频道移动::::::....</title>
	</head>
	<frameset rows="80%,*" border=0>
		<frameset cols="40%,20%,*" border=0>
			<frame frameborder=0  noResize scrolling="yes" marginWidth=0 name="from_chl" src="moveChlTree_from.jsp?siteid=<%=siteid%>"></frame>
			<frame frameborder=0  noResize scrolling="no" marginWidth=0 name="middle_chl" src="moveChl_static.htm"></frame>
			<frame frameborder=0  noResize scrolling="yes" marginWidth=0 name="to_chl" src="moveChlTree_to.jsp?siteid=<%=siteid%>"></frame>
		</frameset>
		<frame frameborder=0  noResize scrolling="no" marginWidth=0 name="chn_slt_ok" src="moveChl_submit.jsp" ></frame>
	</frameset>
</html>