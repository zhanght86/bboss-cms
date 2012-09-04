<%
/**
  *	调整已设置的置顶文档的置顶起止时间
  * 
  */
%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.bean.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
	
	String channelid = request.getParameter("channelid");
	String siteid = request.getParameter("siteid");
	
	String docid = request.getParameter("docid");
	DocumentManager dm = new DocumentManagerImpl();
	ArrangeDoc arrangeDoc = dm.getArrangeDoc(docid);
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
	//提交
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
		myform.method="post";
		myform.action="docArrange_update_submit.jsp?siteid=<%=siteid%>&channelid=<%=channelid%>";
		myform.submit();
	}
	//取文档name
	function init()
	{
		var docid = "<%=docid%>";
		var docrows = window.dialogArguments.docArrangeListTable.rows.length;
		for(var i=0;i<docrows;i++)
		{
			if(window.dialogArguments.docArrangeListTable.rows[i].id == docid)
				myform.docname.value = window.dialogArguments.docArrangeListTable.rows[i].cells[1].innerText;
		}
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
					开始时间：<input type="text" name="starttime" value="<%=arrangeDoc.getStartTime()%>" class="cms_text" readonly/>
					<input type="button" class="input" value="时间" onclick="showdatetime(document.all('starttime'))">
				</td>
				<td>
					结束时间：<input type="text" name="endtime" value="<%=arrangeDoc.getEndTime()%>" class="cms_text" readonly/>
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