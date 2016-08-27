<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<html>
<head>
<title>选择机构下的用户</title>
<script language="javascript">
	var api = frameElement.api, W = api.opener;
</script>
</head> 
<frameset name="urFrame" value="" cols="200,*" border="1" framespacing="2" >
	<frame src="orgTree.jsp" name="orgTree" id="orgTree" marginWidth=0 marginHeight=0 />
	<!-- frame src="sreachUser.jsp" name="sreachUser" id="sreachUser" marginWidth=0 marginHeight=0/-->
	<frame src="userList.jsp" name="userList" id="userList" marginWidth=0 marginHeight=0 />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

