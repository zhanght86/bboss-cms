
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	AccessControl control = AccessControl.getAccessControl();
	String displayNameInput=request.getParameter("displayNameInput");
	String displayValueInput=request.getParameter("displayValueInput");
	String userNames = request.getParameter("userNames")==null?"":request.getParameter("userNames");

	String fieldName = request.getParameter("fieldName");
	String isUnique=request.getParameter("isUnique");
	String fileTextName=request.getParameter("fileTextName");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>选择人</title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<script language="javascript">
var api = frameElement.api, W = api.opener;

function setUser(users)
{
	 W.$.dictionary.setSelectUser(users,'<%=fieldName%>','<%=fileTextName%>',<%=isUnique%>);  
}
</script>
</head> 
<frameset name="frame1" cols="0,30,70" frameborder="no" border="0" framespacing="0" >
  <frame src="frame_bridge.jsp" name="frame_bridge" scrolling="No" noresize="noresize" id="frame_bridge" />
  <frame src="orgTree.jsp" name="orgTree" id="orgTree" />
  <frame src="userList.jsp?displayNameInput=<%=displayNameInput%>&displayValueInput=<%=displayValueInput%>&userNames=<%=userNames%>" name="userList" scrolling="No" noresize="noresize" id="userList" />  
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

