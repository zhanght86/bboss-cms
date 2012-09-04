<%@ page contentType="text/html; charset=UTF-8" language="java"%>

<%@ page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*" %>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.container.*" %>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	String isSite = request.getParameter("isSite");
	String siteId = request.getParameter("siteId");
	String type = request.getParameter("type");
	String tName = "";
	String tId = "";
	
	String channelName = request.getParameter("channelName");
	String channelId = request.getParameter("channelId");
	String outlineTemplateName = request.getParameter("outlineTemplateName");
	String otId = request.getParameter("otId");
	String detailTemplateName = request.getParameter("detailTemplateName");
	String dtId = request.getParameter("dtId");
	String action = request.getParameter("action");
	SiteManager siteManager = new SiteManagerImpl();
	
	
	if(null!=isSite && isSite.equals("is")){
		tId = request.getParameter("stId");
		tName = request.getParameter("stName");
		if(tId.equals("") || tId == null){
			Template t = siteManager.getIndexTemplate(siteId);
			//判断是否存在模板
			//liangbing.tao 2008-5-20
			if(t != null){
				tName = t.getName();
				tId = t.getTemplateId()+"";
				action = "search";
				channelName = siteManager.getSiteInfo(siteId).getName();
			}
		}
		//System.out.println("SID: "+siteId+"     tName: "+tName+"    type: "+type+"    action: "+action+"    channelName: "+channelName);
	} else {
		if(null!=outlineTemplateName && !outlineTemplateName.equals("")){
			tName = outlineTemplateName;
		} else if (null!=detailTemplateName && !detailTemplateName.equals("")) {
			tName = detailTemplateName;
		} else {
			tName = "当前没有选择任何模板";
		}
		
		if(null!=otId && !otId.equals("")) {
			tId = otId;
		} else if (null!=dtId && !dtId.equals("")) {
			tId = dtId;
		} else {
			tId = " " ;
		}
	}
	//System.out.println("SID: "+siteId+"     CID: "+channelId+"    type: "+type+"    action: "+action);
	String typeName = "";
	if(type.equals("0")) {
		typeName="首页模版";
	} else if (type.equals("1")) {
		typeName="概览模版";
	} else {
		typeName="细览模版";
	}
	
	String title = "";
	if(channelName == null) channelName = siteManager.getSiteInfo(siteId).getName();
	title = "【"+channelName+"】-- "+typeName+"设置";
%>
<TITLE><%=title%></TITLE>
<frameset rows="70,*" frameborder="no" border="0" framespacing="0">
	<frame src="channel_templateSet_queryFrame.jsp?siteId=<%=siteId%>&type=<%=type%>&action=<%=action%>&channelId=<%=channelId%>&typeName=<%=typeName%>&tName=<%=tName%>&tId=<%=tId%>&channelName=<%=channelName%>&isSite=<%=isSite%>" name="queryFrame" id="queryFrame" scrolling="YES" noresize>
	<frame src="channel_templateSet_list.jsp?siteId=<%=siteId%>&tId=<%=tId%>&type=<%=type%>&channelId=<%=channelId%>&typeName<%=typeName%>&action=<%=action%>&isSite=<%=isSite%>" name="templatelist" id="templatelist">
</frameset>
