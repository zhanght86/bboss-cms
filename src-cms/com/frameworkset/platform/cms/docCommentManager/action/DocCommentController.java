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

import com.frameworkset.platform.cms.docCommentManager.DocComment;
import com.frameworkset.platform.cms.docCommentManager.DocCommentManager;
import com.frameworkset.platform.cms.docCommentManager.DocCommentManagerException;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

/**
 * @author gw_hel
 * 文档服务控制器
 */
public class DocCommentController {

	private DocCommentManager docCommentManager;

	/**
	 * 展示文档评论
	 * @param docId 文档ID
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return String
	 * @throws Exception
	 */
	public String showDocumentCommentList(int docId, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// 获得该文档所在的频道评论开关
		int channelCommentSwitch = docCommentManager.getDocCommentSwitch(String.valueOf(docId), "chnl");
		if (channelCommentSwitch != 0) {
			// 首先判断频道的评论开关
			request.setAttribute("commentSwitch", channelCommentSwitch);
		} else {
			int documentCommentSwitch = docCommentManager.getDocCommentSwitch(String.valueOf(docId), "doc");
			request.setAttribute("commentSwitch", documentCommentSwitch);
		}

		request.setAttribute("docId", docId);

		return "path:showDocumentCommentList";
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

		request.setAttribute("docCommentList", docCommentList);
		request.setAttribute("docId", docId);

		return "path:queryDocumentCommentList";
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
	String addNewComment(int docId, String commentUser, String isGuest, String docComment, int status, HttpServletRequest request)
			throws Exception {

		DocComment docCommentBean = new DocComment();

		if (Boolean.parseBoolean(isGuest)) {
			docCommentBean.setUserName("__quest");
		} else {
			docCommentBean.setUserName(URLDecoder.decode(commentUser, "UTF-8"));
		}

		docCommentBean.setDocId(docId);

		if (!StringUtil.isEmpty(docComment)) {
			docCommentBean.setDocComment(URLDecoder.decode(docComment, "UTF-8"));
		}

		docCommentBean.setUserIP(request.getRemoteAddr());
		docCommentBean.setStatus(status);

		try {
			docCommentManager.addOneComment(docCommentBean);
		} catch (DocCommentManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getLocalizedMessage();
		}

		return "success";
	}
}