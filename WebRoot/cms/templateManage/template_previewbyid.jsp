<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*,java.io.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.container.Template"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%
	//AccessControl accessControl = AccessControl.getInstance();
	//accessControl.checkAccess(request,response);
	
	String siteId = request.getParameter("siteId");
	String tplId = request.getParameter("tplId");

	if(tplId == null || "".equals(tplId))
	{
		%>
		<SCRIPT LANGUAGE="JavaScript">
			alert("模板不存在！");
			window.close();
		</SCRIPT>
		<%
		return ;
	}

	SiteManager  siteManager = new SiteManagerImpl();
	TemplateManager tplimpl = new TemplateManagerImpl();

	Template template = tplimpl.getTemplateInfo(tplId);
	String uri = (template.getTemplatePath().equals("")?"":(template.getTemplatePath() + "/")) + template.getTemplateFileName();
	String abUri = siteManager.getSiteAbsolutePath(siteId) + "\\_template\\" + uri;
	
	//将 \\ 转换成 /		
	abUri = abUri.replaceAll("\\\\","/") ;
	
	boolean existFlag = false;
	if(new File(abUri).exists())
		existFlag = true;
	
	String pageUrl = "";
	try{
		if(existFlag){
				String pathContext = rootpath + "/cms/siteResource/" + com.frameworkset.platform.cms.util.CMSUtil.getSite(siteId).getSiteDir() + "/_template/" + uri;
				pageUrl = request.getContextPath() + "/" + uri;
				%>
					<script language = "javascript">
						document.location.href = "<%=pathContext%>";
					</script>
				<%
			}
		if(!existFlag){
			%>
			<script language = "javascript">
				alert("文件不存在！！");
				window.close();
			</script>
			<%
		}		
	}catch(Exception de){
		de.printStackTrace();
		%>
		<script language="javascript">
			alert("操作失败!!");
			window.close();
		</script>
		<%
	}
%>