<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.bean.*"%>
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
	if("1".equals(indexFlag)){	//批量站外站点索引
		String cmsIndexIds[] = request.getParameterValues("webIndexID");
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
				new CMSCrawlerThread(cmsIndex,request.getContextPath()).start();	
			}
		}
		
	if(tmp.length()>0)
		alertStr = "对不起，索引：" + tmp + " 正在建立中，请稍后重试这些索引！";
		
	}else{						//单个站外站点索引
		String siteId = request.getParameter("siteId");
		String indexId = request.getParameter("indexId");
				
		CMSSearchIndex cmsIndex = sm.getIndex(indexId);
		
		//判断索引文件库是否已经上锁
		lockd = sm.isIndexLocked(cmsIndex);
		if(lockd == true)
			alertStr = "对不起，正在建索引中:" + cmsIndex.getIndexName() + "，请稍后重试！";
		else
			//启动线程检索对站点建立索引
			new CMSCrawlerThread(cmsIndex,request.getContextPath()).start();
	}
	
	out.println("<script language='javascript'>alert('" + alertStr + "');</script>");
	//if(alertStr.contains("恭喜"))
	//contains函数报错,jdk版本问题,weida
	if(alertStr.indexOf("恭喜") != -1)
		out.println("<script language='javascript'>parent.document.location.reload();</script>");
%>
</body>
</html>
