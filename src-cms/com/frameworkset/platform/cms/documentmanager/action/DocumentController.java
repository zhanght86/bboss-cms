/*
 * @(#)DocumentController.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.frameworkset.platform.cms.documentmanager.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.frameworkset.util.CollectionUtils;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.util.io.Resource;
import org.frameworkset.util.io.UrlResource;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManager;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.container.Container;
import com.frameworkset.platform.cms.container.ContainerImpl;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.customform.CustomFormManager;
import com.frameworkset.platform.cms.customform.CustomFormManagerImpl;
import com.frameworkset.platform.cms.docsourcemanager.Docsource;
import com.frameworkset.platform.cms.docsourcemanager.DocsourceManagerImpl;
import com.frameworkset.platform.cms.documentmanager.Attachment;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.cms.documentmanager.bean.ChannelNews;
import com.frameworkset.platform.cms.documentmanager.bean.DocAggregation;
import com.frameworkset.platform.cms.documentmanager.bean.DocRelated;
import com.frameworkset.platform.cms.documentmanager.bean.DocTemplate;
import com.frameworkset.platform.cms.documentmanager.bean.DocumentCondition;
import com.frameworkset.platform.cms.documentmanager.bean.NewsCondition;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkTable;
import com.frameworkset.platform.cms.driver.i18n.CmsEncoder;
import com.frameworkset.platform.cms.driver.publish.PublishCallBack;
import com.frameworkset.platform.cms.driver.publish.impl.PublishCallBackImpl;
import com.frameworkset.platform.cms.driver.publish.impl.RecursivePublishManagerImpl;
import com.frameworkset.platform.cms.driver.publish.impl.WEBPublish;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.util.RemoteFileHandle;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

/**
 * @author gw_hel
 * 文档服务控制器
 */
public class DocumentController {

	private DocumentManager documentManager;

	/**
	 * 获取新闻列表
	 * @param condition 查询条件
	 *	startTime-起止时间（可选）
	 *  endTime-结束时间（可选）
	 *  channel-频道（必填）
	 *  keywords-关键词 （标题）（可选）
	 *  docType-文档内部类型（可选） 
	 *  count-获取文档条数（可选，默认为10条）
	 *  site-对应的站点英文名称（必填）
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return List<Document>
	 * @throws Exception Exception
	 */
	public @ResponseBody(datatype="jsonp") ChannelNews getNewsList(NewsCondition condition,
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		if(condition.getChannel() == null || condition.getSite() == null)
			return null;
		ChannelNews news = new ChannelNews();
		Channel ch = CMSUtil.getChannelCacheManagerBySiteName(condition.getSite()).getChannelByDisplayName(condition.getChannel());
		if(ch == null)
			return null;
		condition.setChannelId((int)ch.getChannelId());
		if (!StringUtil.isEmpty(condition.getKeywords())) {
			condition.setKeywords(URLDecoder.decode(condition.getKeywords(), "UTF-8"));
		}
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			List<Document> newsList = documentManager.queryDocumentsByPublishTimeAndKeywords(condition);
	
			news.setNews(newsList);
			Container container = new ContainerImpl();
	//		String siteName = site.getSecondName();
			container.init(condition.getSite(), request, request.getSession(), response);
			news.setChannelIndex(container.getPublishedChannelUrlByDisplayName(condition.getChannel()));
			news.setSiteIndex(container.getPulishedSiteIndexUrlBySiteName(condition.getSite()));
			news.setSitedomain(container.getPulishedSiteDomainBySiteName(condition.getSite()));
			if (!CollectionUtils.isEmpty(newsList)) {
				
	
				for (Document document : newsList) {
					
					if(document.getDoctype()==Document.DOCUMENT_AGGRATION){
						List compositeDocs=documentManager.getPubAggrDocList(document.getDocument_id()+"");
						for(int i =0;i<compositeDocs.size();i++){
							DocAggregation docAggregation = (DocAggregation)compositeDocs.get(i);
							String docpuburl = container.getPublishedDocumentUrl(docAggregation.getIdbyaggr()+"");
							docAggregation.setDocpuburl(docpuburl);
						}
						document.setCompositeDocs(compositeDocs);
						
					}else{
						String docpuburl = container.getPublishedDocumentUrl(document);
						document.setDocpuburl(docpuburl);
					}
				}
			}
			tm.commit();
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			tm.release();
		}

		return news;
	}
	/**
	 * 展示新闻列表
	 * 
	 * @param sortKey 排序关键字
	 * @param desc 排序方式
	 * @param offset 偏移量
	 * @param pagesize 页面数据大小
	 * @param condition 查询条件
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return List<Document>
	 * @throws Exception Exception
	 */
	public String showNewsList(@PagerParam(name = PagerParam.SORT, defaultvalue = "title") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "15") int pagesize, DocumentCondition condition,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (!StringUtil.isEmpty(condition.getKeywords())) {
			condition.setKeywords(URLDecoder.decode(condition.getKeywords(), "UTF-8"));
		}
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			ListInfo newsList = documentManager.queryDocumentsByPublishTimeAndKeywords(condition, offset, pagesize);
	
			Site site = CMSUtil.getSite(condition.getSiteId()+"");
			if (!CollectionUtils.isEmpty(newsList.getDatas())) {
				List<Integer> countids = new ArrayList<Integer>();
				Map<Integer,Object> idx = new HashMap<Integer,Object>();
				Container container = new ContainerImpl();
				String siteName = site.getSecondName();
				container.init(siteName, request, request.getSession(), response);
	
				for (Object obj : newsList.getDatas()) {
					Document document = (Document) obj;
					
					if(document.getDoctype()==Document.DOCUMENT_AGGRATION){
						List compositeDocs=documentManager.getPubAggrDocList(document.getDocument_id()+"");
						for(int i =0;i<compositeDocs.size();i++){
							DocAggregation docAggregation = (DocAggregation)compositeDocs.get(i);
							String docpuburl = container.getPublishedDocumentUrl(docAggregation.getIdbyaggr()+"");
							docAggregation.setDocpuburl(docpuburl);
							countids.add(docAggregation.getAggrdocid());
							idx.put(docAggregation.getAggrdocid(), docAggregation);
						}
						document.setCompositeDocs(compositeDocs);
						
					}else{
						countids.add(document.getDocument_id());
						idx.put(document.getDocument_id(), document);
						String docpuburl = container.getPublishedDocumentUrl(document);
						document.setDocpuburl(docpuburl);
					}
				}
				documentManager.putDocCount(countids, idx);
			}
			request.setAttribute("DOCUMENT_AGGRATION",Document.DOCUMENT_AGGRATION);
			request.setAttribute("newsList", newsList);
			tm.commit();
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			tm.release();
		}

		

		return "path:showNewsList";
	}

	/**
	 * 展示专题列表
	 * 
	 * @param sortKey 排序关键字
	 * @param desc 排序方式
	 * @param offset 偏移量
	 * @param pagesize 页面数据大小
	 * @param condition 查询条件
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return List<Document>
	 * @throws Exception Exception
	 */
	public String showReportsList(@PagerParam(name = PagerParam.SORT, defaultvalue = "title") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "15") int pagesize, DocumentCondition condition,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (!StringUtil.isEmpty(condition.getKeywords())) {
			condition.setKeywords(URLDecoder.decode(condition.getKeywords(), "UTF-8"));
		}

		ListInfo reportsList = documentManager.queryDocumentsByPublishTimeAndKeywords(condition, offset, pagesize);

		if (!CollectionUtils.isEmpty(reportsList.getDatas())) {
			Container container = new ContainerImpl();
			String siteName = ((Document) reportsList.getDatas().get(0)).getSiteName();
			container.init(siteName, request, request.getSession(), response);

			for (Object obj : reportsList.getDatas()) {
				Document document = (Document) obj;
				String docpuburl = container.getPublishedDocumentUrl(document);
				document.setDocpuburl(docpuburl);
			}
		}

		request.setAttribute("reportsList", reportsList);

		return "path:showReportsList";
	}

	/**
	 * 展示视频列表
	 * 
	 * @param sortKey 排序关键字
	 * @param desc 排序方式
	 * @param offset 偏移量
	 * @param pagesize 页面数据大小
	 * @param condition 查询条件
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return List<Document>
	 * @throws Exception Exception
	 */
	public String showVideosList(@PagerParam(name = PagerParam.SORT, defaultvalue = "title") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "12") int pagesize, DocumentCondition condition,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		ListInfo videosList = documentManager.queryVideosByOrderType(condition, offset, pagesize);

		if (!CollectionUtils.isEmpty(videosList.getDatas())) {
			Container container = new ContainerImpl();
			String siteName = ((Document) videosList.getDatas().get(0)).getSiteName();
			container.init(siteName, request, request.getSession(), response);

			for (Object obj : videosList.getDatas()) {
				Document document = (Document) obj;
				String docpuburl = container.getPublishedDocumentUrl(document);
				document.setDocpuburl(docpuburl);
			}
		}

		request.setAttribute("videosList", videosList);

		return "path:showVideosList";
	}

	/**
	 * 展示视频排行榜列表
	 * 
	 * @param condition 查询条件
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return List<Document>
	 * @throws Exception Exception
	 */
	public String showVideosLadder(DocumentCondition condition, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ListInfo videosLadder = documentManager.queryVideosByOrderType(condition, 0, 2);

		if (!CollectionUtils.isEmpty(videosLadder.getDatas())) {
			Container container = new ContainerImpl();
			String siteName = ((Document) videosLadder.getDatas().get(0)).getSiteName();
			container.init(siteName, request, request.getSession(), response);

			for (Object obj : videosLadder.getDatas()) {
				Document document = (Document) obj;
				String docpuburl = container.getPublishedDocumentUrl(document);
				document.setDocpuburl(docpuburl);
			}
		}

		request.setAttribute("videosLadder", videosLadder.getDatas());

		return "path:showVideosLadder";
	}

	/**
	 * 下载文档
	 * @param url URL
	 * @param siteId 站点ID
	 * @param channelId 频道ID 
	 * @param docId 文档ID
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return Resource
	 * @throws Exception
	 */
	public @ResponseBody
	Resource downloadDocument(String siteId, String channelId, String docId, String url, HttpServletRequest request,
			HttpServletResponse response) {

		// FIXME 操作数据库增加计数，当前为操作数据库，需考虑将计数值保存在内存里定时和数据库同步，且在集群环境内存需同步
		// DownLoadCounter counter = Counter.getCounter("downloadCounter");
		// counter.setCounterId(UUID.randomUUID().toString());
		// counter.setCount(0L);
		// counter.setSiteId(Long.valueOf(siteId));
		// counter.setChannelId(Long.valueOf(channelId));
		// counter.setDocId(Long.valueOf(docId));
		// counter.setAttachPath(url);
		// counter.setDownloadIP(request.getRemoteAddr());
		//
		// counter.increment();
		

		try {
			return new UrlResource(new URL(url));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public @ResponseBody String previewDoc(HttpServletRequest request,HttpServletResponse response,PageContext pageContext) throws Exception
	{
		AccessControl accesscontroler = AccessControl.getAccessControl();
		
		String dprior = request.getParameter("documentprior");
		Document doc= new Document();

		ChannelManager cm = new ChannelManagerImpl();
		
		String siteId= request.getParameter("siteid");
		String chnlId = request.getParameter("channelId");

		doc.setChanel_id(Integer.parseInt(request.getParameter("channelId")));
		doc.setUser_id(Integer.parseInt(request.getParameter("userid")));
		doc.setCreateUser(Integer.parseInt(request.getParameter("userid")));
		doc.setTitle(request.getParameter("title"));
		doc.setTitlecolor(request.getParameter("titlecolor"));
		doc.setSubtitle(request.getParameter("subtitle"));
		doc.setPicPath(request.getParameter("picpath"));
		doc.setMediapath(request.getParameter("mediapath"));
	    doc.setPublishfilename(request.getParameter("publishfilename"));
	    doc.setDoc_class(request.getParameter("doc_class"));
	    String isnewdocsource=request.getParameter("isnewdocsource");
	   
	    if(isnewdocsource.equals("1"))//判定是否新的稿源
	    { 
	       //新的稿源名称
	       String docsourceName=request.getParameter("inputdocsource");
	       //获取稿源ID
	       Docsource docsource=new Docsource();
	       docsource.setSRCNAME(docsourceName);
	       DocsourceManagerImpl docsourcemanager=new DocsourceManagerImpl();
	       boolean createflag=false;
	       createflag=docsourcemanager.creatorDsrc(docsource);
	       if(createflag)
	       {
	      
	         docsource=docsourcemanager.getDsrcListBy(docsourceName);
	         doc.setDocsource_id(docsource.getDOCSOURCE_ID());
	       }
	       else
	       {
	          doc.setDocsource_id(0);
	       }
	    }
	    else{
		if(request.getParameter("docsource_id")==null){
			doc.setDocsource_id(0);
		}else{
			doc.setDocsource_id(Integer.parseInt(request.getParameter("docsource_id")));
		}
		}
		doc.setKeywords(request.getParameter("keywords"));
		if(request.getParameter("author")==null||("".equals(request.getParameter("author")))){
			doc.setAuthor("不详");
		}else{
			doc.setAuthor(request.getParameter("author"));
		}
		doc.setDocabstract("".equals(request.getParameter("docabstract"))?"无":request.getParameter("docabstract"));
		if("1".equals(request.getParameter("parentDetailTpl")))
		{
			doc.setDetailtemplate_id(0);
			doc.setParentDetailTpl("1");
		}
		else
		{
			if(request.getParameter("detailtemplate_id")!=null&&!"".equals(request.getParameter("detailtemplate_id")))
			{		
				doc.setDetailtemplate_id(Integer.parseInt(request.getParameter("detailtemplate_id")));
				
			}
			doc.setParentDetailTpl("0");
		}
		doc.setDoctype(Integer.parseInt(request.getParameter("doctype")));
		doc.setLinktarget(request.getParameter("linktarget"));
		doc.setDoc_level(Integer.parseInt(request.getParameter("doc_level")));//文档级别
		doc.setContent(request.getParameter("content")==null?"":request.getParameter("content"));
		//String[] ids = (request.getParameter("fieldids")).split("№");
		String[] values = (request.getParameter("extfieldvalues")).split("№");
		//String[] types = (request.getParameter("extfieldtypes")).split("№");
		String[] names = (request.getParameter("extfieldnames")).split("№");
		Map map = new HashMap();
		for(int i=0;i<names.length;i++)
		{
			map.put(names[i],values[i]);
		}
		doc.setDocExtField(map);System.out.println(cm.hasSetDetailTemplate(chnlId));
		if(cm.hasSetDetailTemplate(chnlId) && (Integer.parseInt(request.getParameter("doctype"))==0 || Integer.parseInt(request.getParameter("doctype"))==1)){		
			WEBPublish publish = new WEBPublish();
			publish.init(request,response,pageContext,accesscontroler);
			PublishCallBack callback = new PublishCallBackImpl();
			publish.setPublishCallBack(callback);

			publish.viewDocument(siteId,chnlId,doc);
			String viewUrl = callback.getViewUrl();
			if(viewUrl!=null){
				viewUrl = request.getContextPath() + "/" + viewUrl;
				return viewUrl;
//			%>
//				<script language="javascript">
//					try
//					{
//						var buttons = parent.document.getElementsByTagName("input");
//						for(var i=0;i<buttons.length;i++)
//						{
//							buttons[i].disabled = false;
//						}
//						parent.closewin();
//						window.open("<%=viewUrl%>");
//					}
//					catch(e)
//					{}
//				</script>
//			<%
			}else{
//			%>
//				<script language="javascript">
//					parent.win.alert("预览失败，可能当前频道未设置细览模板或者模板文件不存在！！");
//					var buttons = parent.document.getElementsByTagName("input");
//					for(var i=0;i<buttons.length;i++)
//					{
//						buttons[i].disabled = false;
//					}
//					parent.closewin();
//				</script>
//			<%
				return "fail";
			}
		}else{
//		%>
//			<script language="javascript">
//				parent.win.alert("预览失败，可能当前频道未设置细览模板、模板文件不存在或该文件无需发布！！");
//				var buttons = parent.document.getElementsByTagName("input");
//				for(var i=0;i<buttons.length;i++)
//				{
//					buttons[i].disabled = false;
//				}
//				parent.closewin();
//			</script>
//		<%
			return "fail";
		}
	}
	
	
	public @ResponseBody String addDocTPL(HttpServletRequest request,HttpServletResponse response,PageContext pageContext) throws Exception
	{
		AccessControl accesscontroler = AccessControl.getAccessControl();
	    String userid = accesscontroler.getUserID();
		
		String action = request.getParameter("action");
		String closeRefresh = request.getParameter("closeRefresh");
		if(closeRefresh==null)
			closeRefresh = "1";
		DocumentManager dm = new DocumentManagerImpl();	
		int successflag = 0;
		String closeWinFlag = "0";         //关闭窗口标志
		
		try{
			if("add".equals(action)){
				String docTplName = request.getParameter("docTplName");
				String docTplDescription = request.getParameter("docTplDescription");
				String tplcontent = request.getParameter("content");
				String channelId = request.getParameter("channelId");
				String channelIds = request.getParameter("channelIds");
				if((channelId==null || "".equals(channelId)) && channelIds!=null){
					if(channelIds.endsWith(","))
						channelId = channelIds.substring(0,channelIds.length()-1);
					else
						channelId = channelIds;
				}	
				DocTemplate doctpl = new DocTemplate();
				doctpl.setChnlId(Long.parseLong(channelId));
				doctpl.setCreateUser(Integer.parseInt(userid));
				doctpl.setDescription(docTplDescription);
				doctpl.setTplCode(tplcontent);
				doctpl.setTplName(docTplName);
				dm.addDocTPL(doctpl);
				successflag = 1;
				closeWinFlag = "1";
			}else if("delete".equals(action)){
				String[] docTplIds = request.getParameterValues("ID");
				if(docTplIds!=null){
					dm.deleteDocTPLs(docTplIds);
				}
				successflag = 1;
			}else if("update".equals(action)){
				String docTplId = request.getParameter("docTplId");
				String docTplName = request.getParameter("docTplName");
				String docTplDescription = request.getParameter("docTplDescription");
				String tplcontent = request.getParameter("content");
				String channelIds = request.getParameter("channelIds");
				System.out.println("docTplName:" + docTplName);
				System.out.println("docTplDescription:" + docTplDescription);
				System.out.println("tplcontent:" + tplcontent);
				System.out.println("channelIds:" + channelIds);
				String channelId = "";
				if(channelIds.endsWith(","))
					channelId = channelIds.substring(0,channelIds.length()-1);
				else
					channelId = channelIds;
				DocTemplate doctpl = new DocTemplate();
				doctpl.setDocTplId(Integer.parseInt(docTplId));
				doctpl.setChnlId(Long.parseLong(channelId));
				doctpl.setDescription(docTplDescription);
				doctpl.setTplCode(tplcontent);
				doctpl.setTplName(docTplName);
				dm.updateDocTPL(doctpl);
				successflag = 1;
				closeWinFlag = "1";	
			}	
		}catch(DocumentManagerException e){
			e.printStackTrace();
		}

		if(successflag == 1){
			return "success";
//			<script language = "javascript">
//				alert("文档模板操作成功！");
//				if("<%=closeRefresh%>"=="1"){
//					if("<%=closeWinFlag%>"=="1"){
//						window.close();
//						window.returnValue="cf";	
//					} else {
//						var str = parent.document.location.href;
//						var end = str.indexOf("?");
//						var strArray;
//						if(end != -1)
//							strArray= str.slice(0,end);
//						else
//							strArray = str;
//						parent.document.location.href = strArray+"?"+parent.document.all.queryString.value;
//					}
//				}
//			</script>
	
		}else if(successflag == 0){
	
			return "faile";
//			<script language="javascript">
//				alert("文档模板保存失败！");
//			</script>
	
		}
		return "faile";
	}
	public @ResponseBody String addDocument(HttpServletRequest request,HttpServletResponse response,PageContext pageContext) throws Exception
	{
		

//			response.setHeader("Cache-Control", "no-cache"); 
//			response.setHeader("Pragma", "no-cache"); 
//			response.setDateHeader("Expires", -1);  
//			response.setDateHeader("max-age", 0);
			
			AccessControl accesscontroler = AccessControl.getAccessControl();		    
		    String userId = accesscontroler.getUserID();
			
			String dprior = request.getParameter("documentprior");
			Document doc= new Document();

			ChannelManager cm = new ChannelManagerImpl();
			SiteManager siteManager = new SiteManagerImpl();
			LogManager logManager = null;
			TransactionManager tm = new TransactionManager();
			try
			{
				tm.begin();
			
				Site site = siteManager.getSiteInfo(request.getParameter("siteid"));
				Channel channel = cm.getChannelInfo(request.getParameter("channelId"));
	
				String sitedir = site.getSiteDir();//频道相对路径
				String relativePath = channel.getChannelPath();//站点相对路径
	
				doc.setChanel_id(Integer.parseInt(request.getParameter("channelId")));
				doc.setUser_id(Integer.parseInt(request.getParameter("userid")));
				doc.setCreateUser(Integer.parseInt(request.getParameter("userid")));
				doc.setTitle(request.getParameter("title"));
				doc.setTitlecolor(request.getParameter("titlecolor"));
				doc.setSubtitle(request.getParameter("subtitle"));
				doc.setSecondtitle(request.getParameter("secondtitle"));
				doc.setPicPath(request.getParameter("picpath"));
				doc.setMediapath(request.getParameter("mediapath"));
			    doc.setPublishfilename(request.getParameter("publishfilename"));
			    doc.setDoc_class(request.getParameter("doc_class"));
			    HashMap map = new HashMap();
			    map.put("ext_org",request.getParameter("ext_org"));
			    map.put("ext_index",request.getParameter("ext_index"));
			    map.put("ext_wh",request.getParameter("ext_wh"));
			    map.put("ext_class",request.getParameter("ext_class"));
			    map.put("ext_djh",request.getParameter("ext_djh"));
			    doc.setExtColumn(map);
			    if(request.getParameter("seq")!=null&&!(request.getParameter("seq").equals("")))
			    {
			      doc.setSeq(Integer.parseInt(request.getParameter("seq")));
			    }
			    else
			    {
			      doc.setSeq(999999);
			    }
			  
			    if(request.getParameter("isnew") != null)
			    {
			    	doc.setIsNew(1);
			    	if(request.getParameter("newpicpath") == null || "".equals(request.getParameter("newpicpath")))
			    		doc.setNewPicPath("image/new.gif");//使用默认值
			    	else
			    		doc.setNewPicPath(request.getParameter("newpicpath"));
			    }
			    else
			    {
			    	doc.setIsNew(0);
			    	doc.setNewPicPath(request.getParameter("newpicpath"));
			    }
			    //System.out.println(request.getParameter("publishfilename"));
			    String isnewdocsource=request.getParameter("isnewdocsource");
			    String docwtime = request.getParameter("docwtime");
			    String ordertime = request.getParameter("ordertime");
			    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				java.util.Date date = formatter.parse(docwtime);
				java.util.Date orderDate = formatter.parse(ordertime);
				doc.setDocwtime(date);
			    doc.setOrdertime(orderDate);
			    
			    String docsourceName=request.getParameter("inputdocsource");
			    
			    //去掉空格
			    if(docsourceName != null){
			    	docsourceName = docsourceName.trim();
			    }
			    
			    
			    DocsourceManagerImpl docsourcemanager=new DocsourceManagerImpl();
			    Docsource docsource_1=new Docsource();
			    docsource_1 = docsourcemanager.getDsrcListBy(docsourceName);
			    
			    if((docsource_1.getSRCNAME()==null)||(docsource_1.getSRCNAME().equals("")))//判定是否新的稿源
			    { 
			    
			       //新的稿源名称
			       
			       //System.out.println("new docsourceName is:"+docsourceName);
			       //获取稿源ID
			       Docsource docsource=new Docsource();
			       docsource.setSRCNAME(docsourceName);
			       
			       boolean createflag=false;
			       createflag=docsourcemanager.creatorDsrc(docsource);
			       if(createflag)
			       {
			      
			         docsource=docsourcemanager.getDsrcListBy(docsourceName);
			         doc.setDocsource_id(docsource.getDOCSOURCE_ID());
			         //System.out.println(docsource.getDOCSOURCE_ID());
			       }
			       else
			       {
			          doc.setDocsource_id(1);
			       }
			    }
			    else{
				   
					//if(request.getParameter("docsource_id")==null){
					//	doc.setDocsource_id(1);
					//}else{
					doc.setDocsource_id(docsource_1.getDOCSOURCE_ID());
						//System.out.println(request.getParameter("docsource_id"));
					//}
				}
				
				
				doc.setKeywords(request.getParameter("keywords"));
				if(request.getParameter("author")==null||("".equals(request.getParameter("author")))){
					doc.setAuthor("不详");
				}else{
					doc.setAuthor(request.getParameter("author"));
				}
				
				doc.setDocabstract("".equals(request.getParameter("docabstract"))?"无":request.getParameter("docabstract"));
				if("1".equals(request.getParameter("parentDetailTpl")))
				{
					Template detpl = (Template)cm.getDetailTemplateOfChannel(request.getParameter("channelId"));
					if(detpl != null )
					{
						doc.setDetailtemplate_id(detpl.getTemplateId());
					}
					doc.setParentDetailTpl("1");
				}
				else
				{
					if(request.getParameter("detailtemplate_id")!=null&&!"".equals(request.getParameter("detailtemplate_id")))
					{		
						
						doc.setDetailtemplate_id(Integer.parseInt(request.getParameter("detailtemplate_id")));
						
					}
					doc.setParentDetailTpl("0");
				}
				
			/*	if(request.getParameter("content")==null){
					doc.setContent("");
				}else{
					doc.setContent(request.getParameter("content"));
				}
			*/	doc.setDoctype(Integer.parseInt(request.getParameter("doctype")));
				doc.setLinktarget(request.getParameter("linktarget"));
				doc.setDoc_level(Integer.parseInt(request.getParameter("doc_level")));//文档级别
				DocumentManager dmi = new DocumentManagerImpl();
				int b = 0;
				boolean flag = true;
	
				if(Integer.parseInt(request.getParameter("doctype"))==0)
				{//只有普通文档才要分析content
				
					CmsLinkProcessor processor = new CmsLinkProcessor(request,relativePath,sitedir);
					processor.setHandletype(CmsLinkProcessor.PROCESS_EDITCONTENT);
					try {
						String content = processor.process(request.getParameter("content"),CmsEncoder.ENCODING_UTF_8);
						doc.setContent(content);
						b = dmi.creatorDoc(doc);//保存文档
						//保存日志
						//modify by xinwang.jiao 2007.07.25 替换成系统管理的日志接口。
						//dmi.recordDocOperate(b,doc.getUser_id(),"新增",0,"新增");
						logManager = SecurityDatabase.getLogManager();
						logManager.log(accesscontroler.getUserAccount(),"新增文档.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",com.frameworkset.util.StringUtil.getClientIP(request),"");
						
						CmsLinkTable linktable = processor.getExternalPageLinkTable();
						Iterator it = linktable.iterator();
						while(it.hasNext())
						{
							CmsLinkProcessor.CMSLink link = (CmsLinkProcessor.CMSLink)it.next();
							String remoteAddr = link.getOrigineLink().getHref();
							String contentPath = link.getRelativeFilePath();
							String localPath = pageContext.getServletContext().getRealPath("/") + "cms/siteResource/" + sitedir + "/_webprj/" + contentPath;
							try {
								RemoteFileHandle rf = new RemoteFileHandle(remoteAddr,localPath);
								flag = rf.download();
	
								//Attachment attachment = new Attachment();
									
								//attachment.setDocumentId(b);
								//attachment.setDescription(remoteAddr);
								//attachment.setUrl(contentPath.substring(contentPath.lastIndexOf("/") + 1,contentPath.length()));
								//attachment.setType(10);//文档内容中的图片等(remote的图片等)
								//attachment.setOriginalFilename(remoteAddr);
	
								//flag = dmi.createAttachment(attachment);
							} catch (Exception e) {
								System.out.println("取远程图片时出错！");
								e.printStackTrace();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else
				{
				
					doc.setContent(request.getParameter("content")==null?"":request.getParameter("content"));
					b = dmi.creatorDoc(doc);
					logManager = SecurityDatabase.getLogManager();
					logManager.log(accesscontroler.getUserAccount(),"新增文档.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",com.frameworkset.util.StringUtil.getClientIP(request),"");
				}
	
	
	
				
				if(b!=0)
				{
					//新增文档的相关文档 start
					if(request.getParameter("doclist")!=null&&!"".equals((String)request.getParameter("doclist")))
					{
						//System.out.println(request.getParameter("doclist"));
						String[] doclist = (request.getParameter("doclist")).split("№");
						int[] relatedDocIds = new int[doclist.length];
						if(doclist.length>0)//这个判断条件？
						{
							//新增聚合文档的相关 start 
							if(doc.getDoctype() == 3)
							{
								String[] titles = (request.getParameter("titles")).split("№");
								for(int i=0;i<doclist.length;i++)
								{
	
									DocAggregation aggrDoc = new DocAggregation();
	
									aggrDoc.setAggrdocid(b);
									aggrDoc.setSeq(i + 1);
									aggrDoc.setIdbyaggr(Integer.parseInt(doclist[i]));
									aggrDoc.setTitle(titles[i]);
									aggrDoc.setType("1");
	
									flag = dmi.addAggrDoc(aggrDoc);
								}
							}
							//新增聚合文档的相关 end
							else
							{
								for(int i=0;i<doclist.length;i++)
								{
									relatedDocIds[i] = Integer.parseInt(doclist[i]);
								}
								DocRelated docrelated = new DocRelated();
								docrelated.setDocId(b);
								docrelated.setOpUserId(Integer.parseInt(request.getParameter("userid")));
								flag = dmi.creatorDocRelated(docrelated,relatedDocIds);
							}
						}
					}
					////新增文档的相关文档 end
					//新增文档的附件 start 
					if(request.getParameter("url")!=null&&!"".equals((String)request.getParameter("url")))
					{
						String[] descriptions = ((String)request.getParameter("description")).split("№");
						String[] originalFilenames = ((String)request.getParameter("originalFilename")).split("№");
						String[] urls = ((String)request.getParameter("url")).split("№");
						if(urls.length>0)
						{
							for(int i=0;i<urls.length;i++)
							{
								Attachment attachment = new Attachment();
								
								attachment.setDocumentId(b);
								attachment.setDescription(descriptions[i]);
								attachment.setUrl(urls[i]);
								attachment.setType(2);//
								attachment.setOriginalFilename(originalFilenames[i]);
	
								flag = dmi.createAttachment(attachment);
							}
						}
					}
					//新增文档的附件 end
					//新增文档的图片 start 
					if(request.getParameter("url2")!=null&&!"".equals((String)request.getParameter("url2")))
					{
						String[] descriptions = ((String)request.getParameter("description2")).split("№");
						String[] originalFilenames = ((String)request.getParameter("originalFilename2")).split("№");
						String[] urls = ((String)request.getParameter("url2")).split("№");
						if(urls.length>0)
						{
							for(int i=0;i<urls.length;i++)
							{
								Attachment attachment = new Attachment();
								
								attachment.setDocumentId(b);
								attachment.setDescription(descriptions[i]);
								attachment.setUrl(urls[i]);
								attachment.setType(3);//
								attachment.setOriginalFilename(originalFilenames[i]);
	
								flag = dmi.createAttachment(attachment);
							}
						}
					}
					//新增文档的图片 end
					//save 扩展字段内容
					if(doc.getDoctype()==0)
					{
						String[] ids = (request.getParameter("fieldids")).split("№");
						String[] values = (request.getParameter("extfieldvalues")).split("№");
						String[] types = (request.getParameter("extfieldtypes")).split("№");
						CustomFormManager cf = new CustomFormManagerImpl();
						flag = cf.saveDocExtFieldValues(String.valueOf(b),ids,values,types);
					}
					//新增文档专题报道
					System.out.println(request.getParameter("specialsiteid"));
					if(request.getParameter("specialsiteid")!=null && !"".equals(request.getParameter("specialsiteid")))
					{
					  String[] specialsiteid=(request.getParameter("specialsiteid").split("&"));
					  String[] specialchannelid=(request.getParameter("sepcialchannelid").split("&"));
					  int[] channelids=new int[specialchannelid.length];
			          int[] siteids=new int[specialsiteid.length];
			          for(int specialsize=0;specialsize<siteids.length;specialsize++)
			          {
			             channelids[specialsize]=Integer.parseInt(specialchannelid[specialsize]);
			             siteids[specialsize]=Integer.parseInt(specialsiteid[specialsize]); 
			          }
			          int[] intDocid=new int[]{b};
					  dmi.copyDocs(request,intDocid,channelids,siteids,1,Integer.parseInt(userId));//1为新稿
					}
					//新增文档专题报道结束
	
				}
				
				//保存完文档后，进行送审、提交发布、立即发布等操作。
				//operFlag为10表示保存后将送审，为11表示保存后提交发布，为12表示保存后立即发布
				String operFlag = request.getParameter("flag");
				String pageUrl = ""; 
				if("10".equals(operFlag)){			//送审
					String[] auditors = request.getParameterValues("auditor");
					//System.out.println("auditors: "+ auditors.length);
					int[] intAuditors = null;
					if(auditors!=null&& auditors.length>0){
					 	intAuditors = new int[auditors.length];
						for(int i=0;i<auditors.length;i++){
							intAuditors[i] = Integer.parseInt(auditors[i]);
						}
					}
					int tanid = dmi.canTransition(b,2);
				    if(tanid >= 0){
					    dmi.deliverDoc(b,intAuditors,Integer.parseInt(userId),0); 
					    //dmi.recordDocOperate(b,Integer.parseInt(userId),"送审",tanid,"送审");
					    logManager = SecurityDatabase.getLogManager();
						logManager.log(accesscontroler.getUserAccount(),"文档送审.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",com.frameworkset.util.StringUtil.getClientIP(request),"");
					 }
				}else if("11".equals(operFlag)){			//提交发布
					String[] publishers = request.getParameterValues("publisher");
					int[] intPublishers = null;
					if(publishers!=null&& publishers.length>0){
					 	intPublishers = new int[publishers.length];
						for(int i=0;i<publishers.length;i++){
							intPublishers[i] = Integer.parseInt(publishers[i]);
						}
					}	
					//System.out.println("publisher: "+ publishers.length);
					int tranid = dmi.canTransition(b,11);
					if(tranid >=0){
						dmi.subPublishDoc(b,intPublishers,Integer.parseInt(userId),0);   
						//dmi.recordDocOperate(b,Integer.parseInt(userId),"提交发布",tranid,"提交发布");
						logManager = SecurityDatabase.getLogManager();
						logManager.log(accesscontroler.getUserAccount(),"文档提交发布.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",com.frameworkset.util.StringUtil.getClientIP(request),"");
					}
				}else if("12".equals(operFlag)){				//立即发布	
						//初始化发布引擎
					 	WEBPublish publish = new WEBPublish();
						publish.init(request,response,pageContext,accesscontroler);
						PublishCallBack callback = new PublishCallBackImpl();
						publish.setPublishCallBack(callback);
					
						int tranID = dmi.canTransition(b,5);   
						String chnlId = doc.getChanel_id()+"";
						int docType = doc.getDoctype();
						if(tranID >0){
							String siteId = request.getParameter("siteid");
							boolean[] publishWay = siteManager.getSitePublishDestination(siteId);
							int[] distributeManners = siteManager.getSiteDistributeManners(siteId);
							//if(docType == 0){biaoping.yin 注释 2007.10.20
								publish.publishDocument(siteId,chnlId,b+"",publishWay, distributeManners);
								pageUrl = callback.getPageUrl();
								flag = callback.getPublishMonitor().isAllFailed()==true?false:true;
								//biaoping.yin 注释开始
							//}else{biaoping.yin 注释 2007.10.20			
							//	dmi.updateDocumentStatus(b,5);biaoping.yin 注释 2007.10.20
							//	flag = true;biaoping.yin 注释 2007.10.20
							//}biaoping.yin 注释 2007.10.20
							//dmi.recordDocOperate(b,Integer.parseInt(userId),"发布",tranID,"发布");
							logManager = SecurityDatabase.getLogManager();
							logManager.log(accesscontroler.getUserAccount(),"文档发布.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",com.frameworkset.util.StringUtil.getClientIP(request),"");
						}
				}
				
				if(b!=0&&flag){
					tm.commit();
					return "success";
	//				<SCRIPT LANGUAGE="JavaScript"> 
	//					
	//					if(parent.window.confirm("操作成功！\n\n您还要继续采集吗？"))
	//					{
	//						parent.window.opener.parent.window.location.reload();
	//						window.close();
	//					}
	//					else
	//					{
	//						parent.window.opener.parent.window.opener.window.location.reload();
	//						parent.window.opener.parent.window.close();
	//						window.close();
	//					}
	//				</script>
				
				}else{
					tm.commit();
					return "fail";
	//				<SCRIPT LANGUAGE="JavaScript"> 
	//					parent.window.alert("操作失败！");
	//					var buttons = parent.window.opener.parent.document.getElementsByTagName("input");
	//					for(var i=0;i<buttons.length;i++)
	//					{
	//						buttons[i].disabled = false;
	//					}
	//					window.close();
	//					//parent.document.all.divProcessing.style.display="none";
	//				</script>
					
				
				
					//window.opener.location.href=window.opener.location.href
				}		
			}
			finally
			{
				tm.release();
			}
	}
	
	public @ResponseBody String updateDocument(HttpServletRequest request,HttpServletResponse response,PageContext pageContext) throws Exception
	{
		AccessControl accesscontroler = AccessControl.getAccessControl();
		TransactionManager tm = new TransactionManager();
		
		String userid = accesscontroler.getUserID();
		String channelname = request.getParameter("channelName");
		String siteid = request.getParameter("siteid");
		String channelId = request.getParameter("channelId");
		String docid = request.getParameter("docid");
		
		//是否提示成功，并关闭窗口（因为返工文档等保存后还要送审，所以无需关闭窗口）
		String closeFlag = request.getParameter("closeFlag");

		Document doc= new Document(); 

		ChannelManager cm = new ChannelManagerImpl();
		SiteManager siteManager = new SiteManagerImpl();
		LogManager logManager = null;
		try
		{
			tm.begin();
			Site site = siteManager.getSiteInfo(request.getParameter("siteid"));
			Channel channel = cm.getChannelInfo(request.getParameter("channelId"));
	
			String sitedir = site.getSiteDir();//频道相对路径
			String relativePath = channel.getChannelPath();//站点相对路径
	
			if(request.getParameter("status")==null){
				doc.setStatus(0);
			}else{
				doc.setStatus(Integer.parseInt(request.getParameter("status")));
			}
			doc.setDocument_id(Integer.parseInt(request.getParameter("docid")));
			doc.setTitle(request.getParameter("title"));
			doc.setTitlecolor(request.getParameter("titlecolor"));
			//doc.setDocumentprior(Integer.parseInt(request.getParameter("documentprior")));
			doc.setSubtitle(request.getParameter("subtitle"));
			doc.setSecondtitle(request.getParameter("secondtitle"));
			doc.setPicPath(request.getParameter("picpath"));
			doc.setMediapath(request.getParameter("mediapath"));
			doc.setDoc_class(request.getParameter("doc_class"));
			HashMap map = new HashMap();
		    map.put("ext_org",request.getParameter("ext_org"));
		    map.put("ext_index",request.getParameter("ext_index"));
		    map.put("ext_wh",request.getParameter("ext_wh"));
		    map.put("ext_class",request.getParameter("ext_class"));
		 	map.put("ext_djh",request.getParameter("ext_djh"));
		    doc.setExtColumn(map);
			if(request.getParameter("isnew") != null)
		    {
		    	doc.setIsNew(1);
		    	if(request.getParameter("newpicpath") == null || "".equals(request.getParameter("newpicpath")))
		    		doc.setNewPicPath("image/new.gif");//使用默认值
		    	else
		    		doc.setNewPicPath(request.getParameter("newpicpath"));
		    }
		    else
		    {
		    	doc.setIsNew(0);
		    	doc.setNewPicPath(request.getParameter("newpicpath"));
		    }
			//String isnewdocsource=request.getParameter("isnewdocsource");
			String docsourceName=request.getParameter("inputdocsource");
			
			//去掉空格
			if(docsourceName != null){
		    	docsourceName = docsourceName.trim();
		    }
			
		    DocsourceManagerImpl docsourcemanager=new DocsourceManagerImpl();
		    Docsource docsource_1=new Docsource();
		    docsource_1 = docsourcemanager.getDsrcListBy(docsourceName);
			if((docsource_1.getSRCNAME()==null)||(docsource_1.getSRCNAME().equals("")))//判定是否新的稿源
		    { 
		       //新的稿源名称
		       //String docsourceName=request.getParameter("inputdocsource");
		       //System.out.println("new docsourceName is:"+docsourceName);
		       //获取稿源ID
		       Docsource docsource=new Docsource();
		       docsource.setSRCNAME(docsourceName);
		       //DocsourceManagerImpl docsourcemanager=new DocsourceManagerImpl();
		       boolean createflag=false;
		       createflag=docsourcemanager.creatorDsrc(docsource);
		       if(createflag)
		       {
		           docsource=docsourcemanager.getDsrcListBy(docsourceName);
		           doc.setDocsource_id(docsource.getDOCSOURCE_ID());
		       }
		       else
		       {
		          doc.setDocsource_id(1);
		       }
		    }
		    else{
			//if(request.getParameter("docsource_id")==null){
			//	doc.setDocsource_id(1);
			//}else{
				doc.setDocsource_id(docsource_1.getDOCSOURCE_ID());
			//}
			}
			doc.setKeywords(request.getParameter("keywords"));
			if(request.getParameter("author")==null||("".equals(request.getParameter("author")))){
				doc.setAuthor("不详");
			}else{
				doc.setAuthor(request.getParameter("author"));
			}
			doc.setDocabstract("".equals(request.getParameter("docabstract"))?"无":request.getParameter("docabstract"));
			if("1".equals(request.getParameter("parentDetailTpl")))
			{
				//doc.setDetailtemplate_id(0);
				Template template = (Template)cm.getDetailTemplateOfChannel(request.getParameter("channelId"));
				if(template != null)
				{
					doc.setDetailtemplate_id(template.getTemplateId());
					
				}
				doc.setParentDetailTpl("1");
			}
			else
			{
				if(request.getParameter("detailtemplate_id")!=null&&!"".equals(request.getParameter("detailtemplate_id")))
				{	//System.out.println("else if:"+request.getParameter("detailtemplate_id"));
					doc.setDetailtemplate_id(Integer.parseInt(request.getParameter("detailtemplate_id")));
				}
				doc.setParentDetailTpl("0");
			}
			if(request.getParameter("doclevel")==null){
				doc.setDoclevel(0);
			}else{
				doc.setDoclevel(Integer.parseInt(request.getParameter("doclevel")));
			}
			//doc.setContent(request.getParameter("content"));
			doc.setDoctype(Integer.parseInt(request.getParameter("doctype")));
			doc.setLinktarget(request.getParameter("linktarget"));
			java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//doc.setCreateTime(sf.parse(request.getParameter("createtime")));
			doc.setDocwtime(sf.parse(request.getParameter("docwtime")));
			doc.setOrdertime(sf.parse(request.getParameter("ordertime")));
			if(request.getParameter("seq")!=null&&!(request.getParameter("seq").equals("")))
		    {
		      doc.setSeq(Integer.parseInt(request.getParameter("seq")));
		    }
		    else
		    {
		      doc.setSeq(999999);
		    }
			doc.setDoc_level(Integer.parseInt(request.getParameter("doc_level")));//文档级别
			doc.setPublishfilename(request.getParameter("publishfilename"));
			DocumentManager dmi = new DocumentManagerImpl();
			//获取原文档对象
			boolean isdeleterelation=false; //设置删除关联关系标记
			if(request.getParameter("docid")!=null && !"".equals(request.getParameter("docid"))){
			  Document oradocument=dmi.getDoc(request.getParameter("docid"));
			  System.out.println(oradocument.getStatus());
			  if(oradocument.getStatus()==5)//已发布
			  {//表示该文档已经发布
			  
			     int oratemplateid=oradocument.getDetailtemplate_id();
			     if(oratemplateid!=doc.getDetailtemplate_id() && !doc.getParentDetailTpl().equals("1"))
			     {
			     isdeleterelation=true ;
			     System.out.println("isdeleterelation1-----");
			     }
			  }
			}
			boolean b = false;
			boolean flag = true;
	
			//处理文档内容
			if(Integer.parseInt(request.getParameter("doctype"))==0)
			{//只有普通文档才要分析content
				CmsLinkProcessor processor = new CmsLinkProcessor(request,relativePath,sitedir);
				processor.setHandletype(CmsLinkProcessor.PROCESS_EDITCONTENT);
				try {
					String content = processor.process(request.getParameter("content"),CmsEncoder.ENCODING_UTF_8);
					doc.setContent(content);//
					b = dmi.updateDoc(doc);
					logManager = SecurityDatabase.getLogManager();
					logManager.log(accesscontroler.getUserAccount(),"修改文档.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",com.frameworkset.util.StringUtil.getClientIP(request),"");
					CmsLinkTable linktable = processor.getExternalPageLinkTable();
					Iterator it = linktable.iterator();
					while(it.hasNext())
					{
						CmsLinkProcessor.CMSLink link = (CmsLinkProcessor.CMSLink)it.next();
						String remoteAddr = link.getOrigineLink().getHref();
						String contentPath = link.getRelativeFilePath();
						String localPath = pageContext.getServletContext().getRealPath("/") + "cms/siteResource/" + sitedir + "/_webprj/" + contentPath;
						try {
							RemoteFileHandle rf = new RemoteFileHandle(remoteAddr,localPath);
							flag = rf.download();
	
							Attachment attachment = new Attachment();
								
							attachment.setDocumentId(Integer.parseInt(docid));
							attachment.setDescription(remoteAddr);
							attachment.setUrl(contentPath.substring(contentPath.lastIndexOf("/") + 1,contentPath.length()));
							attachment.setType(10);//文档内容中的图片等(remote的图片等)
							attachment.setOriginalFilename(remoteAddr);
	
							flag = dmi.createAttachment(attachment);
						} catch (Exception e) {
							System.out.println("取远程图片时出错！");
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else
			{
				doc.setContent(request.getParameter("content")==null?"":request.getParameter("content"));
				b = dmi.updateDoc(doc);
				logManager = SecurityDatabase.getLogManager();
				logManager.log(accesscontroler.getUserAccount(),"修改文档.文档标题:【" + site.getName() + " 站点，" + channel.getDisplayName() + " 频道】" + doc.getSubtitle(),"文档管理",com.frameworkset.util.StringUtil.getClientIP(request),"");
			}
	
			//更新文档的相关文档 start
			flag = dmi.deleteDocRelated(Integer.parseInt(request.getParameter("docid")));//先删除已有的信息
			if(request.getParameter("doclist")!=null&&!"".equals((String)request.getParameter("doclist")))
			{
				String[] doclist = (request.getParameter("doclist")).split("№");
				
				int[] relatedDocIds = new int[doclist.length];
				if(doclist.length>0)//这个判断条件？
				{
					//update 聚合文档的相关 start 
					if(doc.getDoctype() == 3)
					{
						dmi.delAggrDoc(Integer.parseInt(request.getParameter("docid")));
						String[] titles = (request.getParameter("titles")).split("№");
						for(int i=0;i<doclist.length;i++)
						{
	
							DocAggregation aggrDoc = new DocAggregation();
	
							aggrDoc.setAggrdocid(Integer.parseInt(request.getParameter("docid")));
							aggrDoc.setSeq(i + 1);
							aggrDoc.setIdbyaggr(Integer.parseInt(doclist[i]));
							aggrDoc.setTitle(titles[i]);
							aggrDoc.setType("1");
	
							flag = dmi.addAggrDoc(aggrDoc);
						}
					}
					//update 聚合文档的相关 end
					else
					{
						for(int i=0;i<doclist.length;i++)
						{
							relatedDocIds[i] = Integer.parseInt(doclist[i]);
						}
						
						DocRelated docrelated = new DocRelated();
						docrelated.setDocId(Integer.parseInt(request.getParameter("docid")));
						docrelated.setOpUserId(Integer.parseInt(userid));
						flag = dmi.creatorDocRelated(docrelated,relatedDocIds);
					}
				}
			}
			////更新文档的相关文档 end
			//更新文档的附件 start 
			//flag = dmi.deleteAttachment(Integer.parseInt(request.getParameter("docid")),2);
			if(request.getParameter("url")!=null&&!"".equals((String)request.getParameter("url")))
			{
				String[] descriptions = ((String)request.getParameter("description")).split("№");
				String[] originalFilenames = ((String)request.getParameter("originalFilename")).split("№");
				String[] urls = ((String)request.getParameter("url")).split("№");
				if(urls.length>0)
				{
					
					for(int i=0;i<urls.length;i++)
					{
						Attachment attachment = new Attachment();
						
						attachment.setDocumentId(Integer.parseInt(request.getParameter("docid")));
						attachment.setDescription(descriptions[i]);
						attachment.setUrl(urls[i]);
						attachment.setType(2);//
						attachment.setOriginalFilename(originalFilenames[i]);
	
						flag = dmi.createAttachment(attachment);
					}
				}
			}
			String tempurl = (request.getParameter("url"))==null?"":(request.getParameter("url"));
			String sqlurl = "";
			if(tempurl.length()>1)
			{
				sqlurl = "'" + (tempurl.substring(0,tempurl.length()-1)).replaceAll("№","','") + "'";
			}
			else
			{
				sqlurl = "'1'";
			}
			flag = dmi.UpdateAttachmentState(Integer.parseInt(request.getParameter("docid")),2,sqlurl);
			//更新文档的附件 end
			//更新文档的图片 start 
			//flag = dmi.deleteAttachment(Integer.parseInt(request.getParameter("docid")),3);
			if(request.getParameter("url2")!=null&&!"".equals((String)request.getParameter("url2")))
			{
				String[] descriptions = ((String)request.getParameter("description2")).split("№");
				String[] originalFilenames = ((String)request.getParameter("originalFilename2")).split("№");
				String[] urls = ((String)request.getParameter("url2")).split("№");
				if(urls.length>0)
				{
					
					for(int i=0;i<urls.length;i++)
					{
						Attachment attachment = new Attachment();
						
						attachment.setDocumentId(Integer.parseInt(request.getParameter("docid")));
						attachment.setDescription(descriptions[i]);
						attachment.setUrl(urls[i]);
						attachment.setType(3);//
						attachment.setOriginalFilename(originalFilenames[i]);
	
						flag = dmi.createAttachment(attachment);
					}
				}
			}
			String tempurl2 = (request.getParameter("url2"))==null?"":(request.getParameter("url2"));
			String sqlurl2 = "";
			if(tempurl2.length()>1)
			{
				sqlurl2 = "'" + (tempurl2.substring(0,tempurl2.length()-1)).replaceAll("№","','") + "'";
			}
			else
			{
				sqlurl2 = "'1'";
			}
			flag = dmi.UpdateAttachmentState(Integer.parseInt(request.getParameter("docid")),3,sqlurl2);
			//更新文档的图片 end
			//save 扩展字段内容
			if(doc.getDoctype()==0)
			{
				String[] ids = (request.getParameter("fieldids")).split("№");
				String[] values = (request.getParameter("extfieldvalues")).split("№");
				String[] types = (request.getParameter("extfieldtypes")).split("№");
				CustomFormManager cf = new CustomFormManagerImpl();
				flag = cf.saveDocExtFieldValues(docid,ids,values,types);
			}
			//新增文档专题报道
				if(request.getParameter("specialsiteid")!=null && !"".equals(request.getParameter("specialsiteid")))
				{
				  String[] specialsiteid=(request.getParameter("specialsiteid").split("&"));
				  String[] specialchannelid=(request.getParameter("sepcialchannelid").split("&"));
				  int[] channelids=new int[specialchannelid.length];
		          int[] siteids=new int[specialsiteid.length];
		          for(int specialsize=0;specialsize<siteids.length;specialsize++)
		          {
		             channelids[specialsize]=Integer.parseInt(specialchannelid[specialsize]);
		             siteids[specialsize]=Integer.parseInt(specialsiteid[specialsize]); 
		          }
		          int[] intDocid=new int[]{Integer.parseInt(request.getParameter("docid"))};
				  dmi.copyDocs(request,intDocid,channelids,siteids,1,Integer.parseInt(userid));//1为新稿
				}
				//新增文档专题报道结束
			//对于已发的文档，更改状态未待发
			//（不属于正常的流程状态更改，所以没经过是否有迁移的判断，同时为所有有发布权限的人分配任务）
			//：：：：：目前只针对已发文档。
			int curStatus = dmi.getDocStatus(Integer.parseInt(docid));
			//int docType = dmi.getDocType(Integer.parseInt(docid));
			ChannelManager cmi=new ChannelManagerImpl();
			if(curStatus==5){
				List list = cmi.getPublisherList(channelId);
				int[] executers = new int[list.size()];
				for(int i=0;i<list.size();i++){
					User user =(User)list.get(i);
					executers[i] = user.getUserId().intValue();
				}
				dmi.updateDocumentStatus(Integer.parseInt(docid),11);
				dmi.addOneTask(Integer.parseInt(docid),0,11,Integer.parseInt(userid),executers);
			}
			/*if(docType != 0 && curStatus==5){
				List list = cmi.getAuditorList(channelId);
				int[] executers = new int[list.size()];
				for(int i=0;i<list.size();i++){
					User user =(User)list.get(i);
					executers[i] = user.getUserId().intValue();
				}
				dmi.updateDocumentStatus(Integer.parseInt(docid),2);
				dmi.addOneTask(Integer.parseInt(docid),0,2,Integer.parseInt(userid),executers);
			}*/
			if(b&&flag){
			   if(isdeleterelation)//修改了模板
			   {
			     RecursivePublishManagerImpl imp =new RecursivePublishManagerImpl();
			     imp.deleteDocumentDetailRelation(request.getParameter("docid"),site);
			     System.out.println("isdeleterelation-----");
			   }
			   /*
				%>
				<SCRIPT LANGUAGE="JavaScript"> 
						<%if(closeFlag.equals("1")){%>
						parent.win.alert("修改文档成功!!!");
						var str = parent.window.dialogArguments.location.href;
						if(str.indexOf("?")==-1){
							parent.window.dialogArguments.location.href = parent.window.dialogArguments.location.href;
						}else{
							var strArray = str.slice(0,str.indexOf("?"));
							parent.window.dialogArguments.location.href = strArray+"?"+parent.window.dialogArguments.document.all.queryString.value;
						}
						parent.closewin();
						parent.window.close();
						<%}%>
				</script>*/
			   if(closeFlag.equals("100"))//编辑送审
			   {
				   DocumentManager docManager = new DocumentManagerImpl();
			    	String taskid = request.getParameter("taskid");
			    	String[] auditors = request.getParameterValues("auditor");
					//得到审核人的整型数组
					int[] intAuditors = null;
					if(auditors!=null&& auditors.length>0){
					 	intAuditors = new int[auditors.length];
						for(int i=0;i<auditors.length;i++){
							intAuditors[i] = Integer.parseInt(auditors[i]);
						}
					}
			    	int tanid = docManager.canTransition(Integer.parseInt(docid),2);
			    	if(tanid >= 0){
				    	docManager.deliverDoc(Integer.parseInt(docid),intAuditors,Integer.parseInt(userid),Integer.parseInt(taskid));
				    	docManager.recordDocOperate(Integer.parseInt(docid),Integer.parseInt(userid),"送审",tanid,"送审");
				    	tm.commit();
				    	return "success";
		/*%>			
					<script language = "javascript">
						alert("操作成功！");
						window.close();
						if(<%=flag%>==2){
							var str = window.dialogArguments.location.href;
							var end = str.indexOf("?");
							var strArray;
							if(end != -1)
								 strArray= str.slice(0,end);
							else
								strArray = str;
							window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
						}
					</script>
		<%*/
					}
					else{
						tm.commit();
						return "fail";
		/*%>
					<script language = "javascript">
					alert("操作失败！");
					window.close();
					</script>
		<%*/
					}
			   }
			   tm.commit();
			   return "success";
			}
			else
			{
				/*
				<%}else{
					%>
					<SCRIPT LANGUAGE="JavaScript"> 
						parent.win.alert("修改文档失败!!!");
						var buttons = parent.document.getElementsByTagName("input");
						for(var i=0;i<buttons.length;i++)
						{
							buttons[i].disabled = false;
						}
						parent.closewin();
					</script>
				<%
				}
				%>
				*/
				tm.commit();
				return "fail";
			}
			
		}
		finally
		{
			tm.release();
		}
		  
		
	}
	
	public @ResponseBody String deliverHandle(HttpServletRequest request,HttpServletResponse response,PageContext pageContext,HttpSession session) throws Exception
	{
		AccessControl accesscontroler = AccessControl.getAccessControl();
		String userId = accesscontroler.getUserID();
		//flag为1表示新稿文档的送审,为2表示返工文档的送审,3表继续送审
	    String flag = request.getParameter("flag"); 
		String[] auditors = request.getParameterValues("checkBoxOne");
		//得到审核人的整型数组
		int[] intAuditors = null;
		if(auditors!=null&& auditors.length>0){
		 	intAuditors = new int[auditors.length];
			for(int i=0;i<auditors.length;i++){
				intAuditors[i] = Integer.parseInt(auditors[i]);
			}
		}
		
		DocumentManager docManager = new DocumentManagerImpl();
		try{
			if(flag.equals("1")){
		    	String docidStr = request.getParameter("docidStr");  //频道列表中也只对一个文档送审
		    	Map tranIdMap = (HashMap)session.getAttribute("tranIdMap");
				session.removeAttribute("tranIdMap");
				//得到文档id数据,为字符串数组
				String[] docids = null;
				if(docidStr!= null){
					docids = docidStr.split(":");
				}
				//频道列表中也只对一个文档送审
				//int intDocid = Integer.parseInt(docids[0]);
				int[] intDocid = new int[docids.length];
				for(int i=0;i<docids.length;i++){
					intDocid[i] = Integer.parseInt(docids[i]);
				}
				//数组中值表示当前文档是否有任务。对于返工文档，值为返工任务id，若为新稿，值为0
				int[] taskids = docManager.hasTask(Integer.parseInt(userId),intDocid,2);
				//做呈送处理
				docManager.deliverDoc(intDocid,intAuditors,Integer.parseInt(userId),taskids);   
				docManager.recordDocOperate(intDocid,Integer.parseInt(userId),"送审",tranIdMap,"送审");
				return "success";
				/**
	%>
				<script language="javascript">
					alert("操作成功！");
					window.close();
				</script>
	<%*/
		    }
		    else if(flag.equals("2")||flag.equals("3")){//2 编辑送审
		    	String docid = request.getParameter("docid");  //单个文档id
		    	String taskid = request.getParameter("taskid");
		    	if(flag.equals("3")){
		    		String auditComment = request.getParameter("auditComment");
	    			//先审核
	    			int tranId3 = docManager.canTransition(Integer.parseInt(docid),3);
					if(tranId3 >= 0){
						docManager.audit(Integer.parseInt(docid),Integer.parseInt(taskid),Integer.parseInt(userId),auditComment,1);
						docManager.recordDocOperate(Integer.parseInt(docid),Integer.parseInt(userId),"审核",tranId3,"审核通过");
					}
		    	}
		    	int tanid = docManager.canTransition(Integer.parseInt(docid),2);
		    	if(tanid >= 0){
			    	docManager.deliverDoc(Integer.parseInt(docid),intAuditors,Integer.parseInt(userId),Integer.parseInt(taskid));
			    	docManager.recordDocOperate(Integer.parseInt(docid),Integer.parseInt(userId),"送审",tanid,"送审");
			    	return "success";
	/*%>			
				<script language = "javascript">
					alert("操作成功！");
					window.close();
					if(<%=flag%>==2){
						var str = window.dialogArguments.location.href;
						var end = str.indexOf("?");
						var strArray;
						if(end != -1)
							 strArray= str.slice(0,end);
						else
							strArray = str;
						window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
					}
				</script>
	<%*/
				}
				else{
					return "fail";
	/*%>
				<script language = "javascript">
				alert("操作失败！");
				window.close();
				</script>
	<%*/
				}
		    }
		    else
		    {
		    	return "success";
		    }
		}
		catch(Exception de){
			de.printStackTrace();
			return "fail";
	/*%>
			<script language="javascript">
				alert("数据库操作失败！");
				window.close();
			</script>
	<%*/
		}
	

	}
}
