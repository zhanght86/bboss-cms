<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.searchmanager.*"%>
<%@ page import="com.frameworkset.platform.config.ConfigManager"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="java.io.File"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	DocumentManager dm = new DocumentManagerImpl();
	CMSSearchManager sm = new CMSSearchManager();
	String action = request.getParameter("action");
	
	//config.manager中的检索联动开关
	if(CMSUtil.enableIndex())
	{
	
	//移动文档时删除索引
	if(action.equals("moveDoc")){
		Object new_docIds = session.getAttribute("docIds_moveDoc");
		session.removeAttribute("docIds_moveDoc");
		Object new_siteId = session.getAttribute("siteId_moveDoc");
		session.removeAttribute("siteId_moveDoc");
		int[] docIds = null;
		if(new_docIds != null){
			docIds = (int[])new_docIds;
		}
		String siteId = null;
		if(new_siteId != null){
			siteId = (String)new_siteId;		
		}
		try{
			for(int i=0;i<docIds.length;i++){
				//lucene全文检索
				if(CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_LUCENE)){
					sm.deleteDocumetFromIndex(request, docIds[i]+"", siteId);
				}
				//海量全文检索
				if(CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_HAILIANG)){
					CMSUtil.deleteIndexForHL(docIds[i]+"", siteId);
				}			
				
			}
		}catch(Exception e){}
	}
	
	//回收文档时删除索引
	if(action.equals("garDoc")){
		Object new_docIds = session.getAttribute("docIds_garDoc");
		session.removeAttribute("docIds_garDoc");
		Object new_siteId = session.getAttribute("siteId_garDoc");
		session.removeAttribute("siteId_garDoc");
		int[] docIds = null;
		if(new_docIds != null){
			docIds = (int[])new_docIds;
		}
		String siteId = null;
		if(new_siteId != null){
			siteId = (String)new_siteId;		
		}
		try{
			for(int i=0;i<docIds.length;i++){
				if(CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_LUCENE)){
					sm.deleteDocumetFromIndex(request, docIds[i]+"", siteId);
				}
				if(CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_HAILIANG)){
					CMSUtil.deleteIndexForHL(docIds[i]+"", siteId);
				}						
			}
		}catch(Exception e){}
	}
	
	//撤发文档时删除索引
	if(action.equals("drawDoc")){
		Object new_docId = session.getAttribute("docId_drawDoc");
		session.removeAttribute("docId_drawDoc");
		Object new_siteId = session.getAttribute("siteId_drawDoc");
		session.removeAttribute("siteId_drawDoc");
		String docId = null;
		if(new_docId != null){
			docId = (String)new_docId;
		}
		String siteId = null;
		if(new_siteId != null){
			siteId = (String)new_siteId;		
		}
		try{		
			if(CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_LUCENE)){
				sm.deleteDocumetFromIndex(request, docId, siteId);
			}
			if(CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_HAILIANG)){
				CMSUtil.deleteIndexForHL(docId, siteId);			
			}				
		}catch(Exception e){}
	}
	
	//已发文档归档时删除索引
	if(action.equals("pigDoc")){
		Object new_docIds = session.getAttribute("docIds_pigDoc");
		session.removeAttribute("docIds_pigDoc");
		Object new_siteId = session.getAttribute("siteId_pigDoc");
		session.removeAttribute("siteId_pigDoc");
		String[] docIds = null;
		if(new_docIds != null){
			docIds = (String[])new_docIds;
		}
		String siteId = null;
		if(new_siteId != null){
			siteId = (String)new_siteId;		
		}
		try{
			for(int i=0;i<docIds.length;i++){	
				if(CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_LUCENE)){
					sm.deleteDocumetFromIndex(request, docIds[i], siteId);
				}
				if(CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_HAILIANG)){
					CMSUtil.deleteIndexForHL(docIds[i], siteId);		
				}						
			}
		}catch(Exception e){}
	}
	
	}
%>