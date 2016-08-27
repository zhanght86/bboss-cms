<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.container.*"%>
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

	String templateId = request.getParameter("templateId");
 	if(templateId == null || templateId.trim().length()==0){
		throw new Exception("请提供模板id");
	}

	TemplateManager tm = new TemplateManagerImpl();
	Template tplt = tm.getTemplateInfo(templateId);
	
	String templateName = request.getParameter("templateName");
	if(templateName==null||templateName.trim().length()==0){
		throw new Exception("请提供模板名称!");
	}
	tplt.setName(templateName);
	
	String templateDesc = request.getParameter("templateDesc");
	if(templateDesc==null||templateDesc.trim().length()==0){
		throw new Exception("请提供模板简短描述!");
	}
	tplt.setDescription(templateDesc);
	
	String templateType = request.getParameter("templateType");
	tplt.setType(Integer.parseInt(templateType));
	
	
	int styleId = Integer.parseInt(request.getParameter("templateStyle"));
	tplt.setStyle(styleId);

	new TemplateManagerImpl().updateTemplate(tplt);
	%>
	
	<script language="javascript">
		alert('编辑模板成功');
		var urlstr = parent.window.dialogArguments.location.href;
		parent.window.dialogArguments.location.href = urlstr;
		top.close();
	</script>
<%}catch(Exception e){
	out.println("<script language=\"javascript\">");
	out.println("alert('"+e+"');");
	out.println("</script>");
	e.printStackTrace();
}%>
</body>
</html>