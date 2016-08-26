/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.frameworkset.platform.cms.driver.callback;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.Tag;

import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.common.tag.html.CMSListTag;
import com.frameworkset.common.tag.pager.DataInfo;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.PagineContext;
import com.frameworkset.platform.cms.driver.dataloader.CMSBaseListData;
import com.frameworkset.platform.cms.driver.dataloader.CMSDataLoadException;
import com.frameworkset.platform.cms.driver.dataloader.CMSDetailDataLoader;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.platform.cms.driver.jsp.ContextInf;

/**
 * <p>PlatformCMSCallBack.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月26日
 * @author biaoping.yin
 * @version 1.0
 */
public class PlatformCMSCallBack implements CMSCallBack {
	private CMSServletRequest cmsrequest;
	private ContextInf cmscontext ;
	public PlatformCMSCallBack(CMSServletRequest cmsrequest) {
		this.cmsrequest = cmsrequest;
		if(cmsrequest != null)
			cmscontext = cmsrequest.getContext();
		
	}

	@Override
	public String initContext(int maxPateItems) {
		String offsetParam = null;
		PagineContext context = (PagineContext)cmscontext;
		if (!(context instanceof ContentContext)) /**
		 * 
		 * 如果当前环境是在发布文档，则文档中不允许进行概览分页，但是在项目中有的情况下用分页的属性来获取文档的头几条 这种情况是不允许的
		 */
		{
			context.setMaxPageItems(maxPateItems);
			offsetParam = context.getOffset() + "";
		} else {
			context.getPublishMonitor().addFailedMessage(
					"文档细览页面中的概览标签不允许将isList设置为false",
					context.getPublisher());
		}
		return offsetParam;

	}

	@Override
	public void initContextData(long offset, int newPageCount, long totalsize) {
		PagineContext context = (PagineContext)cmscontext;
		if (context != null && !(context instanceof ContentContext)) {
			context.setOffset(offset);
			/**
			 * 如果当前的页码和offset有变化需要更新context中的值
			 */
			context.setCurrentPageNumber( newPageCount);
			context.setTotalSize(totalsize);
		} else if (context instanceof ContentContext) {
			// context.getPublishMonitor().addFailedMessage("文档细览页面中的概览标签不允许将isList设置为false",context.getPublisher());
		}
		
	}

	@Override
	public DataInfo getCMSDataInfo(Tag tag,HttpServletRequest request,String sortKey, boolean desc, long offset,
			int maxPageItems, boolean listMode) {
		CMSListTag cmsListTag = (CMSListTag) tag;
		CMSBaseListData dataInfo = CMSTagUtil
				.getCMSBaseListData(cmsListTag.getDatatype());
		/**
		* 最新cms需要放开注释的代码 
		* https://github.com/bbossgroups/bboss-cms.git
		* */
		Map<String,Object> params = cmsListTag.getParams();
		if(params != null)
		{
			if(!params.containsKey("doctype") && cmsListTag.getDocType() != null)
			{
				params.put("doctype", cmsListTag.getDocType() );
			}
			else 
			{
				
			}
			dataInfo.setOutlineInfo(cmsListTag.getSite(), cmsListTag
					.getChannel(), cmsListTag.getCount(), params);

		
		}
		else
		{
			dataInfo.setOutlineInfo(cmsListTag.getSite(), cmsListTag
					.getChannel(), cmsListTag.getCount(), cmsListTag.getDocType());
		}
		/**
		* 旧的cms使用以下代码，后续需要屏蔽dataInfo.setOutlineInfo(cmsListTag.getSite(), cmsListTag
				.getChannel(), cmsListTag.getCount());
		
		dataInfo.setOutlineInfo(cmsListTag.getSite(), cmsListTag
				.getChannel(), cmsListTag.getCount());*/
		if (cmsListTag.getDocumentid() != null)
			dataInfo.setDocumentid(cmsListTag.getDocumentid());

		dataInfo.initial(sortKey,  desc,  offset,
				 maxPageItems,  listMode, request);
		return dataInfo;
	}

	@Override
	public Object getCMSCellData() {
		// CMSDetailDataLoader dataLoader =
		// (CMSDetailDataLoader)request.getAttribute("dataset."
		// + cmsRequest.getContext().getID());
		Object data = null;
		CMSDetailDataLoader dataLoader = ((Context)cmsrequest.getContext())
				.getCMSDetailDataLoader();
		try {
			if (dataLoader == null)
				return null;
			data = dataLoader.getContent((ContentContext) cmsrequest
					.getContext());
		} catch (CMSDataLoadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

}
