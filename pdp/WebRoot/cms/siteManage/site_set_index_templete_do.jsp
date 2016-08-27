<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.util.*" %>
<%@page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@page import="com.frameworkset.platform.cms.container.Template" %>
<%@ page import="java.util.List"%>
<%@page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@page import="com.frameworkset.platform.cms.flowmanager.*" %>
<%@page import="com.frameworkset.common.poolman.DBUtil" %>
<%@page import="java.util.*" %>
<%@page import="com.frameworkset.platform.cms.driver.publish.impl.*" %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String siteId = request.getParameter("siteId");
	int indexTemplateId = Integer.parseInt(request.getParameter("ddIndexTemplate").trim());
	if(siteId == null || siteId.trim().length()==0){
	%>
		<script language="javascript" type="text/javascript">
			alert("站点id为空,无法编辑站点.");
			self.location.href = "about:blank";
		</script>
	<%
		return;
	}
	SiteManager sm = new SiteManagerImpl();
	Site site = sm.getSiteInfo(siteId) ;
	int oraIndexTemplateId=site.getIndexTemplateId();//获取原来站点的模板id
	
	System.out.println("oraIndexTemplateId---"+oraIndexTemplateId);
	site.setIndexTemplateId(indexTemplateId);	
	if(sm.updateSite(site))
	{
	  if(oraIndexTemplateId!=0 && indexTemplateId!= oraIndexTemplateId)
	  {
	    //如果不为空且修改前后模板不同,调用删除站点在递归发布关系表中的关系方法
	     RecursivePublishManagerImpl imp =new RecursivePublishManagerImpl();
	     imp.deletesiteRelation(siteId);
	  }
	%>
	<script language = "javascript">
		alert("编辑成功");	
		parent.updateParentForm();
		parent.close();
    </script>
	<%
	 
	}else
	{
		%>
	<script language = "javascript">
	alert("编辑失败了.");	
    </script>
	<%
	}
%>
