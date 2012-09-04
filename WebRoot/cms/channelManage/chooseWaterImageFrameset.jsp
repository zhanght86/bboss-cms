<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(pageContext);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	//fileFlag为"1"表示首页文件的选择，为null或""表示为图片的选择
	String fileFlag = request.getParameter("fileFlag");
	fileFlag = fileFlag == null?"pic":fileFlag;//默认为选择图片

	String title = "选择图片";
	if(fileFlag!=null && fileFlag.equals("1"))
	{
		title = "选择首页文件";
	}
	if(fileFlag!=null && fileFlag.equals("media"))
	{
		title = "选择多媒体文件";
	}
	if(fileFlag!=null && fileFlag.equals("file"))
	{
		title = "选择文件";
	}
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=title%></title>
<script type="text/javascript">
	var theOpener = window.dialogArguments;
</script>
</head>
<frameset rows="25,*" cols="*" frameborder="yes" border="1" framespacing="0">
	<frame src="chooseWaterImageTools.jsp?fileFlag=<%=fileFlag%>" name="ToolsFrm" scrolling="no" noresize>
	<frameset cols="200,*" frameborder="no" border="0" framespacing="0">
		<frame src="chooseWaterImageTree.jsp?fileFlag=<%=fileFlag%>" name="TreeFrm" scrolling="auto" noresize>
		<frame src="chooseWaterImageList.jsp?fileFlag=<%=fileFlag%>" name="ImageListFrm" scrolling="auto">
		
	</frameset>
</frameset>
<noframes>
</noframes>
</html>
