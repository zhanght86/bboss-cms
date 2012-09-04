<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*,java.io.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request,response);
	
	String siteId = request.getParameter("siteId");
	String action = request.getParameter("action");
	String uri = request.getParameter("uri");

	SiteManager  siteManager = new SiteManagerImpl();
	String abUri = siteManager.getSiteAbsolutePath(siteId) + "\\_template\\" + uri;

	//将 \\ 转换成 /		
	abUri = abUri.replaceAll("\\\\","/") ;

	boolean existFlag = false;
	if(new File(abUri).exists())
		existFlag = true;
	
	boolean[] publishWay = siteManager.getSitePublishDestination(siteId);
	int[] distributeManners = siteManager.getSiteDistributeManners(siteId);
	String pageUrl = "";
	try{
		if(action.equals("publish") && existFlag){
			WEBPublish publish = new WEBPublish();
			publish.init(request,response,pageContext,accessControl);
			PublishCallBack callback = new PublishCallBackImpl();
			publish.setPublishCallBack(callback);
			
			publish.publishPage(siteId,uri,publishWay,distributeManners);
			//callback.getPublishMonitor().isPublishCompleted();
			//callback.getPublishMonitor().isAllFailed();
			//callback.getPublishMonitor()
			pageUrl = callback.getPageUrl();
			if(pageUrl!=null){
			%>
				<script language = "javascript">
					if(confirm("恭喜，发布成功！要查看发布文件吗？")){
						window.open("<%=pageUrl%>");
					}
				</script>
			<%
			}else{
				out.println("<script language='javascript'>alert('对不起，发布失败')</script>");
			}
		}
		else if(action.equals("preview") && existFlag){
				String pathContext = rootpath + "/cms/siteResource/" + com.frameworkset.platform.cms.util.CMSUtil.getSite(siteId).getSiteDir() + "/_template/" + uri;
				pageUrl = request.getContextPath() + "/" + uri;
				%>
					<script language = "javascript">
						window.open("<%=pathContext%>");
					</script>
				<%
			}
		if(!existFlag){
			%>
			<script language = "javascript">
				alert("文件不存在！！");
			</script>
			<%
		}		
	}catch(Exception de){
		de.printStackTrace();
		%>
		<script language="javascript">
			alert("操作失败!!");
		</script>
		<%
	}
%>