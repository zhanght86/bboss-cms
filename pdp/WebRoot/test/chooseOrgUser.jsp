<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/sysmanager/base/scripts/panes.jsp"%>
<html>
<head>
<title>选择机构下的用户</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<script type="text/javascript" src="js/jquery-1.5.2.min.js"></script>
		<script type="text/javascript" src="js/commontool.js"></script>	
</head> 
<form name="submitForm" method="post">
<input type="hidden" value="${usernames}" id="usernames" name="usernames"/>
<input type="hidden" value="${nodeinfoId}" id="nodeinfoId" name="nodeId"/>
</form>
<frameset name="urFrame" value="" cols="180,*" border="1" framespacing="3" >
	<frame src="orgTree.jsp" name="orgTree" id="orgTree" marginWidth=0 marginHeight=0 />
	<!-- frame src="sreachUser.jsp" name="sreachUser" id="sreachUser" marginWidth=0 marginHeight=0/-->
	<frame src="userList.jsp?usernames=${usernames }" name="userList" id="userList" marginWidth=0 marginHeight=0 />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

