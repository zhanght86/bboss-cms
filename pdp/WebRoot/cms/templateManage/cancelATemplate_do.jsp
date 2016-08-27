<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<% 
try{
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	String siteId = request.getParameter("siteId");
	String templateId = request.getParameter("templateId");
	if(siteId==null||siteId.trim().length()==0||templateId==null||templateId.trim().length()==0){
%>
		<script type="text/javascript">
			alert("请提供站点id和模板id.");
			top.close();
		</script>
<%		
		return;
	}
	boolean hasDelete = new TemplateManagerImpl().deleteTemplateofSite(Integer.parseInt(templateId),Integer.parseInt(siteId));

	if(hasDelete){
		%>
		<script type="text/javascript">
			alert("文件已经不是模板了!");
			var urlstr = parent.window.dialogArguments.location.href;
			parent.window.dialogArguments.location.href = urlstr;
			top.close();
		</script>	
		<%
	}else{
		%>
		<script type="text/javascript">
			alert("模板可能已经在使用了，没有取消成功!");
			var urlstr = parent.window.dialogArguments.location.href;
			parent.window.dialogArguments.location.href = urlstr;
			top.close();
		</script>	
		<%
	}
}catch(Exception e){
%>
	<script type="text/javascript">
		alert('取消文件作为模板发生异常');
		var urlstr = parent.window.dialogArguments.location.href;
		parent.window.dialogArguments.location.href = urlstr;
		top.close();
	</script>	
<%	e.printStackTrace();
	return;
}
%>

</body>
</html>