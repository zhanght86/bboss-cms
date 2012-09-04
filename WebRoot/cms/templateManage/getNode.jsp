<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%
try{ 
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);	
	String pathContext = (String)session.getAttribute("pathContext");
	if(pathContext==null || pathContext.trim().length()==0){
		return;
	}
	String uri = request.getParameter("uri");
	int level = Integer.parseInt(request.getParameter("level"));
	String result = FileUtil.getDirectoryNode(pathContext,uri,level); 
	out.println(result);
}catch(Exception e){
	
}
%>