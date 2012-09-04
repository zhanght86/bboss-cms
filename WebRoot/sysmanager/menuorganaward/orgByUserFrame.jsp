<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<html>
<head>
<title>资源与机构可见操作授予</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>

</head> 
<frameset name="urFrame" value="" rows="*,250" border="1" framespacing="2" >
	<frame src="orgTree.jsp" name="orgTree" id="orgTree" marginWidth=0 marginHeight=0 />
	<frame src="userList.jsp" name="userList" id="userList" marginWidth=0 marginHeight=0 />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

