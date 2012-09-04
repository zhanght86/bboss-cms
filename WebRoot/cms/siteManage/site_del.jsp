<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%
    AccessControl control = AccessControl.getInstance();
    control.checkAccess(request,response);//

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

    SiteManager smi=new SiteManagerImpl();
	//boolean ret = smi.deleteSite(Integer.parseInt(request.getParameter("siteId")),path+"/");
	boolean ret = smi.logDeleteSite(request.getParameter("siteId"),request,response);
	//删除站点重新加载缓冲
	//SiteCacheManager.getInstance().reset();
%>
 <%if(ret==true){%>
	<script language="javascript">
		alert("恭喜,站点删除成功!");
		parent.window.returnValue = true;
		//parent.window.open("../main.jsp","perspective_content");
		
		close();
	 </script>
  <%}%>
  
  <%if(ret==false){%>
	<script language="javascript">
	   alert("抱歉,站点删除失败!");
	      
	 </script>
  <%}%> 