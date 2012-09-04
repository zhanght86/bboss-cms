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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.CollectionUtils;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.util.io.Resource;
import org.frameworkset.util.io.UrlResource;

import com.frameworkset.platform.cms.container.Container;
import com.frameworkset.platform.cms.container.ContainerImpl;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.bean.DocumentCondition;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

/**
 * @author gw_hel
 * 文档服务控制器
 */
public class DocumentController {

	private DocumentManager documentManager;

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

		ListInfo newsList = documentManager.queryDocumentsByPublishTimeAndKeywords(condition, offset, pagesize);

		if (!CollectionUtils.isEmpty(newsList.getDatas())) {
			Container container = new ContainerImpl();
			String siteName = ((Document) newsList.getDatas().get(0)).getSiteName();
			container.init(siteName, request, request.getSession(), response);

			for (Object obj : newsList.getDatas()) {
				Document document = (Document) obj;
				String docpuburl = container.getPublishedDocumentUrl(document);
				document.setDocpuburl(docpuburl);
			}
		}

		request.setAttribute("newsList", newsList);

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
}
