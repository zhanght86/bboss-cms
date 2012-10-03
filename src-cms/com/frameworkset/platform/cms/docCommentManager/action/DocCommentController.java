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
package com.frameworkset.platform.cms.docCommentManager.action;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.container.Container;
import com.frameworkset.platform.cms.container.ContainerImpl;
import com.frameworkset.platform.cms.docCommentManager.CommentResult;
import com.frameworkset.platform.cms.docCommentManager.DocComment;
import com.frameworkset.platform.cms.docCommentManager.DocCommentManager;
import com.frameworkset.platform.cms.docCommentManager.DocCommentManagerException;
import com.frameworkset.platform.cms.docCommentManager.NComentList;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

/**
 * @author gw_hel
 * 文档服务控制器
 */
public class DocCommentController {

	private DocCommentManager docCommentManager;
	
	public String showAllComments(String siteId,int docId, long channelId, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Container container = new ContainerImpl();
		container.initWithSiteid(siteId, request, request.getSession(false), response);
		
		TransactionManager tm = new TransactionManager(); 
		try
		{
			
			tm.begin(tm.RW_TRANSACTION);
			// 获得该文档所在的频道评论开关
			int channelCommentSwitch = docCommentManager.getDocCommentSwitch(String.valueOf(docId), "chnl");
			if (channelCommentSwitch != 0) {
				// 首先判断频道的评论开关
				request.setAttribute("commentSwitch", channelCommentSwitch);
			} else {
				int documentCommentSwitch = docCommentManager.getDocCommentSwitch(String.valueOf(docId), "doc");
				request.setAttribute("commentSwitch", documentCommentSwitch);
			}
			
			//获得该文档所在频道的评论审核开关
			Integer aduitSwitchFlag = docCommentManager.getChannelCommentAduitSwitch((int)channelId);
			if (aduitSwitchFlag != null) {
				request.setAttribute("aduitSwitchFlag", aduitSwitchFlag);
			} else {
				request.setAttribute("aduitSwitchFlag", 1);
			}
			Document doc = CMSUtil.getCMSDriverConfiguration()
					  .getCMSService()
					  .getDocumentManager()
					  .getPartDocInfoById(docId+"");
			String docurl = container.getPublishedDocumentUrl(doc);
			request.setAttribute("docId", docId);
			request.setAttribute("channelId", channelId);
			request.setAttribute("docurl", docurl);
			request.setAttribute("docTitle", doc.getTitle());
			request.setAttribute("doc", doc);

			return "path:showAllComments";
		}
		finally
		{
			tm.releasenolog();
		}
	}

	/**
	 * 展示文档评论
	 * @param docId 文档ID
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return String
	 * @throws Exception
	 */
	public String showDocumentCommentList(int docId, long channelId, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		TransactionManager tm = new TransactionManager(); 
		try
		{
			
			tm.begin(tm.RW_TRANSACTION);// 获得该文档所在的频道评论开关
			int channelCommentSwitch = docCommentManager.getDocCommentSwitch(String.valueOf(docId), "chnl");
			if (channelCommentSwitch != 0) {
				// 首先判断频道的评论开关
				request.setAttribute("commentSwitch", channelCommentSwitch);
			} else {
				int documentCommentSwitch = docCommentManager.getDocCommentSwitch(String.valueOf(docId), "doc");
				request.setAttribute("commentSwitch", documentCommentSwitch);
			}
			
			//获得该文档所在频道的评论审核开关
			Integer aduitSwitchFlag = docCommentManager.getChannelCommentAduitSwitch((int)channelId);
			if (aduitSwitchFlag != null) {
				request.setAttribute("aduitSwitchFlag", aduitSwitchFlag);
			} else {
				request.setAttribute("aduitSwitchFlag", 1);
			}
	
			request.setAttribute("docId", docId);
			request.setAttribute("total", docCommentManager.getTotalCommnet( docId));
			request.setAttribute("channelId", channelId);
	
			return "path:showDocumentCommentList";
		}
		finally
		{
			tm.releasenolog();
		}
	}

	/**
	 * 查询文档评论列表
	 * 
	 * @param sortKey 排序关键字
	 * @param desc 排序方式
	 * @param offset 偏移量
	 * @param pagesize 页面数据大小
	 * @param docId 文档ID
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return String
	 * @throws Exception Exception
	 */
	public String queryDocumentCommentList(@PagerParam(name = PagerParam.SORT, defaultvalue = "title") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize, int docId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		ListInfo docCommentList = docCommentManager.getCommnetList(docId, (int) offset, pagesize);
		request.setAttribute("total", docCommentList.getTotalSize());
		request.setAttribute("docCommentList", docCommentList);
		request.setAttribute("docId", docId);

		return "path:queryDocumentCommentList";
	}
	
	
	/**
	 * 查询文档前n条评论和评论总数
	 * 
	 * @param sortKey 排序关键字
	 * @param desc 排序方式	 
	 * @param docId 文档ID
	 * @param HttpServletRequest request
	 * @return String
	 * @throws Exception Exception
	 */
	public @ResponseBody(datatype="jsonp") NComentList getDocumentCommentNList(int docId,int n) {

		
		try {
			NComentList docCommentList = docCommentManager.getCommnetList(docId, n);
			return docCommentList;
		} catch (Exception e) {
			return null;
		}	
		
	}
	

	/**
	 * 展示所有该文档的评论
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param docId
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String showAllCommentList(@PagerParam(name = PagerParam.SORT, defaultvalue = "title") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "50") int pagesize, int docId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		ListInfo docCommentList = docCommentManager.getCommnetList(docId, (int) offset, pagesize);
		
		request.setAttribute("docCommentList", docCommentList);
		request.setAttribute("docId", docId);

		return "path:showAllCommentList";
	}

	/**
	 * 添加新评论
	 * @param docId 文档ID
	 * @param commentUser 评论用户名
	 * @param isGuest 是否是匿名用户
	 * @param docComment 评论内容
	 * @param status 评论状态
	 * @param request HttpServletRequest
	 * @return String
	 * @throws Exception
	 */
	public @ResponseBody(datatype = "jsonp")
	CommentResult addNewComment(long channelId, int docId, String commentUser, String isGuest, String docComment, HttpServletRequest request)
			throws Exception {

		DocComment docCommentBean = new DocComment();

		if (StringUtil.isEmpty(commentUser)) {
			if( Boolean.parseBoolean(isGuest))
				docCommentBean.setUserName("__quest");
		} else {
			docCommentBean.setUserName(URLDecoder.decode(commentUser, "UTF-8"));
		}

		docCommentBean.setDocId(docId);

		if (!StringUtil.isEmpty(docComment)) {
			docCommentBean.setDocComment(URLDecoder.decode(docComment, "UTF-8"));
		}

		docCommentBean.setUserIP(request.getRemoteAddr());
		
		//默认即可发布
		int status = 1;
		
		//获取频道的评论审核开关
		Integer aduitSwitchFlag = docCommentManager.getChannelCommentAduitSwitch((int)channelId);
		if (aduitSwitchFlag != null) {
			//如果评论为开通状态，则评论的状态为待审核，否则为即可发布状态
			status = aduitSwitchFlag == 0 ? 2 : 1;
		}
		docCommentBean.setStatus(status);
		CommentResult result = new CommentResult();
		result.setAduitSwitchFlag(aduitSwitchFlag+ "");
		try {
			docCommentManager.addOneComment(docCommentBean);
			result.setMsg("success");
		} catch (DocCommentManagerException e) {
			result.setMsg("failed");
			result.setError(StringUtil.exceptionToString(e));
		}

		return result;
	}
}
