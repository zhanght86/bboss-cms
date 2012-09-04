<%
/**
  *	设置一个文档为置顶文档
  * 如果该文档已经被置顶，则return
  */
%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    
    String channelid = request.getParameter("channelid");
	String siteid = request.getParameter("siteid");
	
	String docid = request.getParameter("docid");
	DocumentManager dm = new DocumentManagerImpl();

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = new Date();
	String starttime = format.format(date);
	date.setDate(date.getDate() + 7);
	String endtime = format.format(date);
	
	if(dm.ArrangeDocExist(docid))
	{
%>
	<SCRIPT LANGUAGE="JavaScript">
		alert("该文档已经为置顶！");
		window.close();
	</SCRIPT>
<%
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
<SCRIPT LANGUAGE="JavaScript">
	//
	function subform()
	{
		if(myform.starttime.value=="")
		{
			alert("请选择开始时间！");
			return false;
		}
		if(myform.endtime.value=="")
		{
			alert("请选择结束时间！");
			return false;
		}
		if(myform.endtime.value<=myform.starttime.value)
		{
			alert("结束时间不能小于开始时间！");
			return false;
		}
		myform.method = "post";
		myform.action = "docArrange_add_submit.jsp?siteid=<%=siteid%>&channelid=<%=channelid%>";
		myform.submit();
	}
	//取文档name
	function init()
	{
		var docid = "<%=docid%>";
		var docrows = window.dialogArguments.docListTable.rows.length;
		for(var i=0;i<docrows;i++)
		{
			if(window.dialogArguments.docListTable.rows[i].id == docid)
				myform.docname.value = window.dialogArguments.docListTable.rows[i].cells[3].innerText;
		}
		var d = new Date();
		var t = d.getYear()+""+(d.getMonth()+1)+d.getDate()+d.getHours()+d.getMinutes()+d.getMilliseconds();
	}
</SCRIPT>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>设置置顶文档</title>
</head>

<body onload="init()">
	<form name="myform" target="addarrange">
		<input type="hidden" name="documentId" value="<%=docid%>"/>
		<table>
			<tr>
				<td>
					文档名称：<input type="text" name="docname" class="cms_text" readonly="true"/>
				<td>
				<td></td>
			</tr>
			<tr>
				<td>
					开始时间：<input type="text" name="starttime" class="cms_text" value="<%=starttime%>" readonly/>
					<input type="button" class="input" value="时间" onclick="showdatetime(document.all('starttime'))">
				</td>
				<td>
					结束时间：<input type="text" name="endtime" class="cms_text" value="<%=endtime%>" readonly/>
					<input type="button" class="input" value="时间" onclick="showdatetime(document.all('endtime'))">
				</td>
			</tr>
			<tr>
				<td align="right">
					<input type="button" name="ok" value="确定" onclick="subform();" class="cms_button"/>
				</td>
				<td align="left">
					<input type="button" name="cl" value="取消" onclick="window.close();" class="cms_button"/>
				</td>
			</tr>
		</table>
	</form>
	<iframe name="addarrange" width="0" height="0">
	</iframe>
</body>
</html>