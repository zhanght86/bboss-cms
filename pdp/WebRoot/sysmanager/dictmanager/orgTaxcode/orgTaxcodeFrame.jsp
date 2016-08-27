<%@ page contentType="text/html; charset=UTF-8" %>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="../../css/navigator.css">
		<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
		<%@ include file="/include/css.jsp"%>		
	</head>
	<frameset name="orgTaxcodeMain"  rows="10,90" frameborder="no" border="0" framespacing="0" >
	    <frameset name="orgTaxcode"  cols="40,60" frameborder="no" border="0" framespacing="0" >
		    <frame name="dictType"  src="dicttypeList.jsp"  scrolling="no" noresize="noresize" />
		    <frame name="orgSelect" src="orgSelect.jsp"  scrolling="no" noresize="noresize"  />
		</frameset>
		<frame name="dictDatas" src="org_dictdataShow.jsp"  scrolling="no" noresize="noresize"  />
	</frameset>	
	<noframes>
	<body>
	</body>
	
	</noframes>
</html>
