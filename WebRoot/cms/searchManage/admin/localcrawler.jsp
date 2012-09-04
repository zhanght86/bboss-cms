<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.bean.*"%>
<%@ page import="com.frameworkset.platform.cms.util.CMSUtil"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title>索引管理</title>
</head>
<body>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	//indexFlag为0表示站内索引，为1表示对站点频道进行索引
	String indexFlag = request.getParameter("indexFlag");
	
	boolean lockd;
	String alertStr = "恭喜，您已经启动后台建索引线程！";
	
	CMSSearchManager sm = new CMSSearchManager();
	if("0".equals(indexFlag)){			//对整个站点开始索引
		String siteId = request.getParameter("siteId");
		//对整个站点目录建立索引
		int indexId = sm.isAllSiteIndexExist(siteId);
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
		
		
	}else if("1".equals(indexFlag)){	//批量站点频道索引
		String cmsIndexIds[] = request.getParameterValues("siteIndexID");
		String tmp = "";	
		for(int i=0;i<cmsIndexIds.length;i++){
			CMSSearchIndex cmsIndex = sm.getIndex(cmsIndexIds[i]);
			
			//判断索引文件库是否已经上锁
			lockd = sm.isIndexLocked(cmsIndex);
			if(lockd == true){
				if(tmp.length()>0)
					tmp = tmp + "," + cmsIndex.getIndexName();
				else 
					tmp = cmsIndex.getIndexName();
			}else{
			
				//启动线程检索对站点建立索引
				new CMSCrawlerThread(cmsIndex).start();
			}
		}
		
		if(tmp.length()>0)
			alertStr = "对不起，索引：" + tmp + " 正在建立中，请稍后重试这些索引！";
			
	}else{						//单个站点频道索引
		String siteId = request.getParameter("siteId");
		String indexId = request.getParameter("indexId");
				
		CMSSearchIndex cmsIndex = sm.getIndex(indexId);
		
		//判断索引文件库是否已经上锁
		lockd = sm.isIndexLocked(cmsIndex);
		if(lockd == true)
			alertStr = "对不起，正在建索引中:" + cmsIndex.getIndexName() + "，请稍后重试！";
		else
			//启动线程检索对站点频道建立索引
			new CMSCrawlerThread(cmsIndex).start();
	}
	
	out.println("<script language='javascript'>alert('" + alertStr + "');</script>");
	if(alertStr.indexOf("恭喜") != -1)
		out.println("<script language='javascript'>parent.document.location.reload();</script>");
%>
</body>
</html>
