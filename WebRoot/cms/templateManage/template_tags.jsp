<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="com.frameworkset.common.poolman.DBUtil" %>
<%@page import="java.sql.*" %>

<%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
	response.setHeader("Pragma","no-cache"); //HTTP 1.0 
	response.setDateHeader ("Expires", -1);
	//prevents caching at the proxy server
	response.setDateHeader("max-age", 0); 
%>

<html>
<head>
<STYLE TYPE="text/css">
	.labelTD {color: black;font-size:10pt;font-family:宋体;}
</STYLE>
<script src="js/tag.js"></script>

</head>
<body>
<SCRIPT LANGUAGE="JavaScript">
	<%
	try {
		DBUtil db = new DBUtil();
		String sql = "select * from TB_CMS_TPL_TAG order by order_no";
		db.executeSelect(sql);
		for (int i = 0; i < db.size(); i++) {
			String id = db.getString(i,"ID");
			String name = db.getString(i,"NAME");
			String thType = db.getString(i,"TYPE");
			String marker = db.getString(i,"MARKER");
			out.println("addTplTag("+id+",'"+name+"','"+thType+"','"+marker+"');");
		}

		sql = "select * from TB_CMS_TPL_TAG_COMPOSITE";
		db.executeSelect(sql);
		for (int i = 0; i < db.size(); i++) {
			int id = db.getInt(i,"COMPOSITE_TAG_ID");
			int subId = db.getInt(i,"INNER_TAG_ID");
			out.println(" addInnerTag ("+id+","+subId+");");
		}

	} catch (SQLException e) {
		e.printStackTrace();
	%>
		alert('获取标签列表出错!原因是:\n<%=e.getMessage().replaceAll("\n", "").replaceAll("'","")%>');	
	<%
		return;
	}
	%>
	document.write('<table id="TagNamesTable">');
	showTagName('取消选择',0);
	for(var i=0;i<tplTagArray.length;i++){
	
		showTagName(tplTagArray[i].name,i+1);
	}
	document.write('</table>');
	changeCurrentRow(0);
</SCRIPT>

	
</body>
</html>
