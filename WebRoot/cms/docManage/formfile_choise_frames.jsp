<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
%>
<html>
	<frameset cols="40%,*" border=0>
	<frame frameborder=0  noResize scrolling="yes" marginWidth=0 name="formtree" src="formTree.jsp"></frame>
	<frame frameborder=0  noResize scrolling="yes" marginWidth=0 name="formfilelist" src="form_files_list.jsp"></frame>
	</frameset>
</html>