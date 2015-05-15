<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<script type="text/javascript">
<% 
try{
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);	
	String pathContext = request.getParameter("pathContext");
	String uri = request.getParameter("uri");	
	String name = request.getParameter("fileName");
	String type = request.getParameter("type");
	String coverFlag = request.getParameter("coverFlag");
	
	if(pathContext==null || pathContext.trim().length()==0){
		throw new Exception("没有提供资源的上下文路径,无法管理资源.");
	}
	
	File parentFolder = null;
	if(uri!=null && uri.trim().length()!=0){
		parentFolder = new File(pathContext,uri);
	}else{
		parentFolder = new File(pathContext);
	}
	
	
	if(name==null || name.trim().length()==0){
		throw new Exception("没有文件(或文件夹)的名称,无法创建文件(或文件夹).");
	}
	File newFile = new File(parentFolder.getAbsolutePath(),name);	
	if(newFile.exists()){
	    if(coverFlag==null)throw new Exception("同名文件(或文件夹)已经存在,请给文件(或文件夹)重新命名!");
	    newFile.delete();
	}
	String msg = null;
	if("1".equals(type)){
		newFile.getParentFile().mkdirs();
		boolean b = newFile.createNewFile();
		msg = b?"新建文件成功!":"新建文件失败!";	
	}else{
		boolean b = newFile.mkdirs();
		msg = b?"新建文件夹成功!":"新建文件夹失败!";			
	}
	
%>
	alert("<%=msg%>");
	parent.win.reloadView();
	<%if(!"1".equals(type)){%>
		window.dialogArguments.open("navigator_content.jsp","perspective_toolbar");
	<%}%>
	top.close();

<%	
}catch(Exception e){
%>
	alert("<%=e.getMessage()%>");
	//top.close();
<%	e.printStackTrace();
}
%>
</script>	
</body>
</html>