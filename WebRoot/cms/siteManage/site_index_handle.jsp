<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.bean.*"%>
<html>
<head>
<title>站点管理－索引管理</title>
</head>
<body>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	boolean lockd;
	String alertStr = "恭喜，后台建索引线程已经启动！";
	
	CMSSearchManager sm = new CMSSearchManager();
	String action = request.getParameter("action");
	String siteId = request.getParameter("siteId");
	int indexId = sm.isAllSiteIndexExist(siteId);
		
	if(action.equals("rebuild")){		
		//对整个站点目录建立索引		
		if(indexId != -1){
			CMSSearchIndex cmsIndex = sm.getIndex(indexId+"");
			//判断索引文件库是否已经上锁
			lockd = sm.isIndexLocked(cmsIndex);
			if(lockd == true)
				alertStr = "对不起，正在建索引中:" + cmsIndex.getIndexName() + "，请稍后重试！";
			else
				//启动线程检索对整站建立索引
				new CMSCrawlerThread(cmsIndex).start();
		}else{
			alertStr = "对不起，当前站点还没有整站索引记录，请先增加后再开始索引！";
		}
	}
	if(action.equals("deleteFiles")){
		alertStr = "恭喜，删除索引文件成功！";
		try{
			sm.deleteIndexFiles(new String[]{String.valueOf(indexId)});
		}catch(Exception e){}		
	}

	out.println("<script language='javascript'>alert('" + alertStr + "');</script>");
	if(alertStr.indexOf("恭喜") != -1)
		out.println("<script language='javascript'>parent.document.location.reload();</script>");
%>
</body>
</html>
