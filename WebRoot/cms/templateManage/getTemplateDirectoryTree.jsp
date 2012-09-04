<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.security.authorization.AccessException"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<% 
	//调用权限验证接口
	AccessControl control = AccessControl.getInstance();
	String selectStr="",nodeStr="";   
	
	String userName = request.getParameter("userName");
    String passWord=request.getParameter("passWord");
    try
    {
		control.logindw(request,null,userName,passWord,"0");    
		String siteId = request.getParameter("siteId");
    	String fatherNode=request.getParameter("fatherNode");
    	String uri=fatherNode;
    	
		if(siteId==null && uri==null)
    	{
        	SiteManager siteimpl=new SiteManagerImpl();
			List sitelist = siteimpl.getSiteAllRuningList(control);
			for(int i=0;i<sitelist.size();i++)
			{
		    	Site currentSite=(Site)sitelist.get(i);
      			selectStr+="<option value='"+currentSite.getSiteId()+"'>"+currentSite.getName()+"</option>";
      		}
    	} 

    	uri = uri==null?"/":uri;
    	uri = uri.replaceAll("\\\\","/");
    	
		String pathContext = null;
		if(siteId!=null && siteId.trim().length()!=0){
		String temp = (new SiteManagerImpl()).getSiteAbsolutePath(siteId);
		if(temp!=null && temp.trim().length()!=0){
			pathContext = new File(temp,"_template").getAbsolutePath();
		}
		}
		if(fatherNode==null)
    	{fatherNode="";}
    	else
    	{fatherNode+="/";}
		if(pathContext!=null && pathContext.trim().length()!=0){
		List fileresources = (new FileManagerImpl()).getDirectoryResource(siteId,uri);
		java.util.Iterator it = fileresources.iterator();
		while(it.hasNext()){
				FileResource fr = (FileResource)it.next();
				nodeStr+="<mm:treenode name='"+fatherNode+fr.getName()+"' value='"+fr.getName()+"' state='collapsed' icon=0></mm:treenode>";
			}
    	}
		//注销登录
		control.checkAccess(request, response);
		control.logoutdw();
    }
    catch (AccessException ex) {        
    	selectStr="远程访问用户名和口令验证失败";
    }
%>
</head>
<body>
<form>
<%=selectStr%>
<%=nodeStr%>
</form>
</body>
</html>