<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" %>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="java.util.*"%>
<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    
    String docId = request.getParameter("docId");
    docId = docId==null?"":docId;
    String docname = request.getParameter("docName");
    docname = docname==null?"":docname;
    
    DocumentManagerImpl impl = new DocumentManagerImpl();
    Document doc = impl.getPartDocInfoById(docId);
       
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String ordertime = "";//formatter.format(doc.getOrdertime());
    if(doc.getOrdertime() != null) {
        ordertime = formatter.format(doc.getOrdertime());
    }else if(doc.getDocwtime() != null){
        ordertime = formatter.format(doc.getDocwtime());
    }
    
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
<style type="text/css">
<!--
body {
	background-color: #EEEEEE;
}
.STYLE7 {color:#003398; font-size: 12px;}
#Layer1 {
	position:absolute;
	left:0px;
	top:0px;
	width:113px;
	height:127px;
	z-index:1;
}
.STYLE8 {color: #0000FF}

-->
</style>
<body>
<body>
	<form name="myform" mothod="post" target="updateordertime">
		<input type="hidden" name="docId" value="<%=docId%>"/>
		<input type="hidden" name="refresh" value=""/>
		<table>
			<tr>
				<td>
					文档名称：<input type="text" name="title" class="cms_text" value="<%=docname%>" readonly="true" size="21"/>
				<td>
			</tr>
			<tr>
				<td>
					排序时间：<input type="text" name="ordertime" class="cms_text" value="<%=ordertime%>" size="21" readonly/>
					<input type="button" class="Channel_5wordsBtn" value="时间" onclick="showdatetime(document.all('ordertime'))">
				</td>
			</tr>
			<td height="30px"></td>
			<tr>
				<td align="center">
					<input type="button" class="cms_button" value="确定" onclick="subform()">&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" class="cms_button" value="取消" onclick="window.close()">
				</td>
			</tr>
		</table>
	</form>
	<iframe name="updateordertime" width="0" height="0"></iframe>
</body>
<script>
	function subform(){	
		myform.action = "update_ordertime_do.jsp";
		//document.all("updateordertime").src = "update_ordertime_do.jsp";
		myform.submit();
	}
	window.onunload = function closeit(){
	    window.returnValue = "";
	    if(document.all("refresh").value == "ok"){
	        window.returnValue = "ok";
	    }	    
	    window.close();
	}
    function setReturnValue(){
        document.all("refresh").value = "ok";
        window.close();
    }
</script>
</html>
