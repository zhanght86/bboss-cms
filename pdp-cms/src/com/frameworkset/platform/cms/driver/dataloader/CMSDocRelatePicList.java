package com.frameworkset.platform.cms.driver.dataloader;

import java.util.List;

import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

/**
 * <p>
 * Title: CMSDocRelatePicList.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @Date 2012-8-15 上午11:47:14
 * @author biaoping.yin
 * @version 1.0.0
 */
public class CMSDocRelatePicList extends CMSBaseListData{
	/**
	 * 文档分页获取方法
	 */
	protected ListInfo getDataList(String sortKey, boolean desc, long offSet,
			int pageItemsize) {
		/**
		 * 如果存在发布上下文，则表示为发布过程中调用本方法
		 */
		if(context != null)
		{
			ContentContext contentcontext = (ContentContext)context;
			//文档的相关图片
			try {
				ListInfo datas = context.getDriverConfiguration()
						.getCMSService().getDocumentManager()
						.getPicturesOfDocument(contentcontext.getDocument(),offSet,
								pageItemsize);
				
				return datas;
			} catch (NumberFormatException e) {
				context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
			} catch (DocumentManagerException e) {
				context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
			} catch (DriverConfigurationException e) {
				context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
			}
		}
		
	
		return new ListInfo();
		
	}
	
	/**
	 * 文档列表获取方法
	 */
	protected ListInfo getDataList(String sortKey, boolean desc) {
		/**
		 * 如果存在发布上下文，则表示为发布过程中调用本方法
		 */
		if(context != null)
		{
			ContentContext contentcontext = (ContentContext)context;
			//文档的相关图片
			try {
				List pics = context.getDriverConfiguration()
						.getCMSService().getDocumentManager()
						.getPicturesOfDocument(contentcontext.getDocument());
				ListInfo datas = new ListInfo();
				datas.setDatas(pics);
				return datas;
			} catch (NumberFormatException e) {
				context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
			} catch (DocumentManagerException e) {
				context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
			} catch (DriverConfigurationException e) {
				context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
			}
		}
		
	
		return new ListInfo();
	}

}
