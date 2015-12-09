<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
try {
	AccessControl control = AccessControl.getAccessControl();

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	Site site = new Site();
	site.setCreateUser(Long.parseLong(control.getUserID()));

	site.setDbName(request.getParameter("dbName"));
	site.setLocalPublishPath(request.getParameter("localpublishpath"));
	String workFlowFrom = request.getParameter("workFlowFrom");
	if ("1".equals(workFlowFrom)) {
		site.setWorkflowIsFromParent(1);
		site.setWorkflow(0);
	} else {
		String workflow = request.getParameter("workFlow");
		if (workflow != null && !"".equals(workflow)) {
			site.setWorkflow(Integer.parseInt(workflow));
			site.setWorkflowIsFromParent(0);
		}else{
			site.setWorkflowIsFromParent(1);
			site.setWorkflow(0);
		}
	}
	site.setStatus(Integer.parseInt(request.getParameter("siteStatus")));
	site.setFtpFolder(request.getParameter("ftpFolder"));
	site.setFtpIp(request.getParameter("ftpIp"));
	site.setFtpPassword(request.getParameter("ftpPassword"));
	
	String ftpPort = request.getParameter("ftpPort");
	if (ftpPort != null && !"".equals(ftpPort)) {
		site.setFtpPort(Short.parseShort(ftpPort));
	}

	site.setFtpUser(request.getParameter("ftpUser"));

	site.setIndexFileName(request.getParameter("indexFileName"));

	site.setName(request.getParameter("siteName"));

	String parentSiteId = request.getParameter("parentSiteId");
	if (parentSiteId != null && !"".equals(parentSiteId)) {
		site.setParentSiteId(Integer.parseInt(parentSiteId));
	}

	site.setPath(application.getRealPath("/"));

	String publishDestination = request.getParameter("publishDestination");
	if (publishDestination != null && !"".equals(publishDestination)) {
		site.setPublishDestination(Integer.parseInt(publishDestination));
	}
	//分发方式设置
	String distributeManners = request.getParameter("distributeManners");
	site.setDistributeManners(distributeManners);

	site.setSecondName(request.getParameter("secondName"));

	site.setSiteDesc(request.getParameter("siteDesc"));

	site.setWebHttp(request.getParameter("webHttp"));
	
	String order = request.getParameter("order");
	if(order!=null && order.trim().length()!=0){
		site.setSiteOrder(Integer.parseInt(order));
	}else{
		site.setSiteOrder(0);
	}
	
	//保存索引参数
	site.setIndex_day(request.getParameter("index_day"));
	site.setIndex_level(request.getParameter("index_level"));
	site.setIndex_time(request.getParameter("index_time"));
	
	SiteManager smi = new SiteManagerImpl();
	smi.logCreateSite(site,request, response);
%>
<script language="javascript">
	alert("新建站点成功!");
	parent.window.returnValue = true;
	parent.close();
</script>
<%
} catch (SiteManagerException e) {
	e.printStackTrace();
%>
<script language="javascript">
	alert('新建站点出错,请检查!原因是:\n<%=e.getMessage().replaceAll("\n", "").replaceAll("'","")%>');
	//防止二次提交
	parent.window.document.all("button")[0].disabled = false;
	parent.window.document.all("button")[1].disabled = false;
</script>
<%
}
%>
