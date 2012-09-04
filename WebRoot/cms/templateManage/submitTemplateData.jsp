<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.container.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.security.authorization.AccessException"%>
<%
  	String exceptionMessage="";
	//调用权限验证接口
	AccessControl control = AccessControl.getInstance();
try 
  {
    String userName=request.getParameter("userName");
    String passWord=request.getParameter("passWord");
	control.logindw(request,null,userName,passWord,"0");  
	//control.checkAccess(request, response);
	String userId=control.getUserID();
	Template tplt = new Template();
	
	tplt.setCreateUserId(Long.parseLong(userId));

	String siteId = request.getParameter("siteId");

	
	if(siteId==null || siteId.trim().length()==0){
		exceptionMessage="模板与站点相关,不能没有站点id.";
	}

	tplt.setSiteId(Integer.parseInt(siteId));
	String name = request.getParameter("templateName");
	if(name==null || name.trim().length()==0){
		exceptionMessage="模板名字不能为空.";
	}
	tplt.setName(name);


	tplt.setDescription(request.getParameter("templateDesc"));


	String header = request.getParameter("templateHead");
	if(header==null || header.trim().length()==0){
		exceptionMessage="模板头不能为空.";
	}
	
	tplt.setHeader(header);

	tplt.setText(request.getParameter("content"));
	
	tplt.setType(Integer.parseInt(request.getParameter("templateType")));

	tplt.setIncreasePublishFlag(0);
	tplt.setTemplateFileName(request.getParameter("fileName"));
	
	tplt.setTemplatePath(request.getParameter("templatePath"));
	
	TemplateManagerImpl tm = new TemplateManagerImpl();
	tm.createTemplateofSite(tplt,Integer.parseInt(siteId));
	//注销登录
	control.checkAccess(request, response);
	control.logoutdw();
	}
	catch (TemplateManagerException e) 
	{
		exceptionMessage=e.getMessage();
	}
    catch (AccessException ex) {        
    	exceptionMessage="远程访问用户名和口令验证失败";
    }
    //catch (Exception ex) {
    	//exceptionMessage=ex.getMessage();
	//}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<form>
<%=exceptionMessage%> 
</form>
</body>
</html>