<%@ page contentType="text/html; charset=UTF-8" %>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="../../css/navigator.css">
		<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
		<%@ include file="/include/css.jsp"%>		
	</head>
	<frameset name="orgTaxcodeMain"  rows="90,10" frameborder="no" border="0" framespacing="0" >
	    <frameset name="orgTaxcode"  cols="30,70" frameborder="no" border="0" framespacing="0" >
		    <frame name="dictType"  src="dicttypeList.jsp"  />
		    <frameset name="rightFrame"  rows="10,90" frameborder="no" border="0" framespacing="0" >
		        <frame name="orgSelect" src="orgSelect.jsp"  scrolling="no" noresize="noresize"  />
		        <frame name="dictDatas" src="org_dictdataShow.jsp"  scrolling="yes" noresize="noresize"  />
		    </frameset>
		</frameset>
		<frame name="orgTaxcodeOpt" src="orgTaxcodeOpt.jsp"  id="dictType" />
	</frameset>	
	<noframes>
	<body>
	</body>
	</noframes>
</html>
