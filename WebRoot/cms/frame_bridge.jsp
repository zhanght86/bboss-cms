<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.security.AccessControl,java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.SiteManager,
com.frameworkset.platform.cms.sitemanager.SiteManagerImpl,com.frameworkset.platform.cms.sitemanager.Site"%>
<%
	 AccessControl accesscontroler = AccessControl.getInstance();
     accesscontroler.checkAccess(request,response);
	 String siteid = request.getParameter("siteid");
	 //System.out.println("============"+siteid);
	 SiteManager sitemanager = new SiteManagerImpl();
	 Site site = sitemanager.getSiteInfo(siteid);
	 session.setAttribute("siteid",siteid);
	 com.frameworkset.platform.cms.CMSManager cmsManager  = new com.frameworkset.platform.cms.CMSManager();
	 cmsManager.init(request,session,response,accesscontroler);
	 cmsManager.updateCurrentSite(site);

	 
%>
    <script language="javascript">   
    //alert(window.parent.document.all.item("modulepath").options[window.parent.document.all.item("modulepath").selectedIndex].value)		
    	window.open(window.parent.document.all.item("modulepath").options[window.parent.document.all.item("modulepath").selectedIndex].value,'perspective_main');
    </script>

