package com.frameworkset.platform.cms.driver.distribute.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.documentmanager.Attachment;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.driver.config.DocumentStatus;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.BatchContext;
import com.frameworkset.platform.cms.driver.context.CMSContext;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.PageContext;
import com.frameworkset.platform.cms.driver.context.TemplateContext;
import com.frameworkset.platform.cms.driver.distribute.Distribute;
import com.frameworkset.platform.cms.driver.distribute.DistributeDestination;
import com.frameworkset.platform.cms.driver.distribute.DistributeException;
import com.frameworkset.platform.cms.driver.distribute.IndexObject;
import com.frameworkset.platform.cms.driver.htmlconverter.CMSTemplateLinkTable;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.LinkTimestamp;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkTable;
import com.frameworkset.platform.cms.driver.htmlconverter.LinkCache;
import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor;
import com.frameworkset.platform.cms.searchmanager.CMSSearchManager;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.platform.cms.util.FtpUpfile;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.util.StringUtil;
/**
 * 
  * <p>Title: HTMLDistribute</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class HTMLDistribute extends Distribute  {
	protected List docattachements = null;
	
	
	/**
	 * 不一定会执行
	 */
	public void distribute() throws HTMLDistributeException {
//		if(!(context instanceof PageContext))
//			System.out.println("ss");

		try {
			
			/**
			 * 分发模板中的附件
			 */
			this.distributeAttachment();
			
			
			
			
			this.context.getPublishMonitor().setPublishStatus(
					PublishMonitor.DISTRIBUTE_COMPLETED);
			this.context.getPublishMonitor().addSuccessMessage("模板附件分发完成",context.getPublisher());
		} catch (IOException e) {
			this.context.getPublishMonitor().setPublishStatus(
					PublishMonitor.DISTRIBUTE_FAILED);
			context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
		}

		catch (Exception e) {
			
			this.context.getPublishMonitor().setPublishStatus(
					PublishMonitor.DISTRIBUTE_FAILED);
			// 异常情况怎么处理，特别是在所有的发布动作都已经执行完毕的情况下，文件处理出错
			context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
		}

	}

	/**
	 * 如果是文档的发布则修改文档的,事务的处理需要补充，肯定会执行
	 */
	public void after() {
		/**
		 * 分发上下文中包含的附件
		 */
//		if(context.isRootContext())
		{
			distributeContextAttacthments();
		}
		
//		 如果发布的是内容文档，则根据发布完成情况更新文档的状态
		if (context instanceof ContentContext) {
			//预览模式下不需要更新文档状态
			if(context.getPublishObject().getActionType() == PublishObject.ACTIONTYPE_PUBLISH)
			{
				TransactionManager tm = new TransactionManager(); 
				try
				{
					tm.begin();
					ContentContext contentctx = (ContentContext) context;
					
//					PreparedDBUtil db = new PreparedDBUtil();
//					db.preparedSelect(context.getDBName(), "select document_id from td_cms_document where document_id = ? for update nowait");
//					db.setInt(1, contentctx.getDocument().getDocument_id());
//					db.executePrepared();
					// 更新文档发布状态
					if (context.getPublishMonitor().isPublishCompleted() || context.getPublishMonitor().isDistributeCompleted()) 
					{
						try {
							context.getDriverConfiguration().getCMSService()
									.getDocumentManager().updateDocumentStatus(
											contentctx,
											DocumentStatus.PUBLISHED);
							context.getDriverConfiguration().getCMSService()
							.getDocumentManager().updateDocPublishTime(contentctx);
							
						} catch (DriverConfigurationException e) {
							context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
						} catch (DocumentManagerException e) {
							context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
						}
					}
					// 恢复文档发布之前的状态
					else {
						try {
							context.getDriverConfiguration().getCMSService()
									.getDocumentManager().updateDocumentStatus(
											contentctx,
											contentctx.getPreStatusOfContent());
						} catch (DriverConfigurationException e) {
							context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
						} catch (DocumentManagerException e) {
							context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
						}
					}
					
					
					
					try {
						
						//删除文件保存的先前临时状态信息
						context.getDriverConfiguration()
								.getCMSService()
								.getDocumentManager()
								.deleteOldStatus(contentctx.getContentid());
					} catch (DocumentManagerException e) {
						context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
					} catch (DriverConfigurationException e) {
						context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
					}
					tm.commit();
				}
				catch(Exception e)
				{
					context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
				}
				finally
				{
					tm.release();
				}
			}
		}
		
//		try
//		{
//			/**
//			 * 如果允许递归发布，将该发布任务所影响的其他发布对象添加到上下文中
//			 * 如果当前发布任务是根任务时，就会执行整个发布过程中记录的所有发布对象
//			 */
//			if(context.enableRecursive() && context.getPublishObject().getActionType() == PublishObject.ACTIONTYPE_PUBLISH)
//			{
//				context.addRecursivePubObjToContext();
//				/**
//				 * 如果是根任务，执行所有与本次任务中所有子任务相关对象的发布
//				 */
////				if(context.isRootContext())
//				{				
//					context.recursivePublish();
//				}
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
		 /**
		  * 如果发布任务回归到发布的起始位置，则处理发布结果
		  */
		if (context.isRootContext() 
				&& context.getActionType() == PublishObject.ACTIONTYPE_PUBLISH) { 
//				&& context.getPublishMonitor().isDistributeCompleted()) {

			/*
			 * 如果发布成功完成，则将保存在临时目录下的发布结果拷贝到正式目录 否则清空临时目录
			 */
			
			Set siteDestinations = context.getSiteDistributeDestinations();
			if(siteDestinations != null)
			{ 
				TransactionManager tm = new TransactionManager(); 
				try
				{
					tm.begin(tm.RW_TRANSACTION);
					Iterator iterator = siteDestinations.iterator();
					
					for(; iterator.hasNext(); )
					{
						DistributeDestination distributeDestination = (DistributeDestination)iterator.next();
						// 本地发布则直接拷贝生成的文件，远程发布则通过ftp上传发布生成的文件
						if(context.getLocal2ndRemote()[1])
						{
							// Ftp远程发布		
							FtpUpfile ftpUpfile = new FtpUpfile(); 
							ftpUpfile.upload(distributeDestination);
						}
						if(CMSUtil.enableIndex())
						{
							updateIndexs(context.getIndexObjects(),context);
						}
						if (context.getLocal2ndRemote()[0])
						{						
							FileUtil.moveSubFiles(distributeDestination.getPublishTemppath() + "/" + distributeDestination.getSite().getSiteDir(), 
									distributeDestination.getPublishRootPath());
						}
					}
					tm.commit();
				}
				catch(Exception e)
				{
					context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
				}
				finally
				{
					tm.release();
				}
				FileUtil.deleteFile(context.getPublishTemppath() );
			}
			
			
			

		}
	}
	
	private void distributeTemplateAttacthments()
	{
		/**
		 * 分发模板附件,
		 */		
		if(this.file == null)
			return;
		CMSTemplateLinkTable linkTable = this.file
				.getOrigineTemplateLinkTable();
		if (linkTable != null && linkTable.size() > 0) {
			Iterator links = linkTable.iterator();
			String templateRootPath = context.getRealTemplateRootPath();
			while (links.hasNext()) {
				CMSLink link = (CMSLink) links.next();
				if(!link.isDirectory())
				{
					if (!link.getOrigineLink().isInternal() )
						continue;
	//				if(link.isModified(newfileTimestamp))
					switch (link.getRelativeFilePathType()) {
						case CMSLink.TYPE_ABSOLUTE:
							break;
						case CMSLink.TYPE_IMAGES:
							;
						case CMSLink.TYPE_STYLE:
							;
						case CMSLink.TYPE_TEMPLATE:
							String realpath = this.removeParam(link.getRelativeFilePath());
							String sourcepath = CMSUtil.getPath(templateRootPath ,realpath);
							if(sourcepath.endsWith("Thumbs.db"))
								continue;
							String destinction = "";
							File source = new File(sourcepath);						
							if(!source.exists())
								continue;
							long lastModified = source.lastModified();
							boolean isModified = link.isModified(lastModified);
							if(context.getActionType() == PublishObject.ACTIONTYPE_PUBLISH)
							{					
								if(!isModified ) //如果文件没有修改过
								{
									if(!context.forcepublishLinks(link))
									{
										continue;
									}
								}
								destinction = CMSUtil.getDirectroy(this.context.getPublishTemppath() + "/" + context.getSiteDir(),
										realpath);
							}
							else
							{
								destinction = CMSUtil.getDirectroy(this.context.getPreviewRootPath(),
										realpath);
							}
							try {							
								FileUtil.copy(source, destinction);
								if(isModified )
								{
									link.setFileTimestamp(lastModified);
								}
								
							} catch (IOException e) {
								
								e.printStackTrace();
							}
						default:
							break;
					}
				}
				else
				{
					distributeDir(templateRootPath,link);
				}
			}
		}
	}
	/**
	 * 分发需要发布的制定目录下的文件
	 * @param templateRootPath
	 * @param dir
	 */
	private void distributeDir(String templateRootPath,CMSLink dir)
	{
		String sourcedir = CMSUtil.getPath(templateRootPath ,dir.getHref());
		distributeDir_(templateRootPath,sourcedir,dir);
	}
	/**
	 * 递归分发需要发布的指定目录下的文件，如果已经发布过的文件没有变化或者没有强制要求发布选项，则不再发布对应的文件
	 * @param templateRootPath
	 * @param dir
	 * @param dirLink
	 */
	private void distributeDir_(String templateRootPath,String dir,CMSLink dirLink)
	{
		
		String destinction = "";
		File dirFile = new File(dir);
		if(!dirFile.exists())
		{
			return ;
		}
		else if(dirFile.isDirectory())
		{
			File[] files = dirFile.listFiles(new FileFilter(){

				@Override
				public boolean accept(File pathname) {
					if(pathname.isDirectory())
					{
						if(pathname.getName().equals(".svn"))
							return false;
						else
							return true;
					}
					else
					{
						if(pathname.getName().equals("Thumbs.db"))
							return false;
						else
							return true;
					}
				}
				
			});
			for(int i = 0; i < files.length; i ++)
			{
				File source = files[i];			
				
				if(!source.exists())
					continue;
				String canonicalPath = null;
				try {
					canonicalPath = source.getCanonicalPath();
				} catch (IOException e) {
					context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
					continue;
				}
				
				if(source.isDirectory())
				{					
					distributeDir_(templateRootPath,canonicalPath,dirLink);
				}
				else
				{
					_handleFile(source,canonicalPath,templateRootPath,dirLink);
				}
			}
		}
		else
		{
			String canonicalPath = null;
			try {
				canonicalPath = dirFile.getCanonicalPath();
			} catch (IOException e) {
				context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());				
			}
			_handleFile(dirFile,canonicalPath,templateRootPath,dirLink);
		}
		
	}
	
	private void _handleFile(File source,String canonicalPath,String templateRootPath,CMSLink dirLink)
	{
		try {
			String destinction = null;
			String relativepath = canonicalPath.substring(templateRootPath.length());
			
			if(context.getActionType() == PublishObject.ACTIONTYPE_PUBLISH)
			{
				long lastModified = source.lastModified();
				boolean isModified = dirLink.isModified(lastModified,canonicalPath);						
				if(!isModified ) //如果文件没有修改过
				{
					if(!context.forcepublishLinks(dirLink))
					{
						return;
					}
				}
				destinction = CMSUtil.getDirectroy(this.context.getPublishTemppath() + "/" + context.getSiteDir() ,relativepath);
			}
			else
			{
				
				destinction = CMSUtil.getDirectroy(this.context.getPreviewRootPath(),
						relativepath);
			}
		
			FileUtil.copy(canonicalPath, destinction);
		} catch (IOException e) {
			context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
		}
	}
	private String removeParam(String path)
	{
		if(path == null)
			return path;
		int idx = path.lastIndexOf("?");
		if(idx < 0)
			return path;
		else
			return path.substring(0,idx);
			
	}
	private void distributeContextAttacthments()
	{
		/**
		 * 分发模板附件,发布过程当中通过接口或者标签记录的模板链接，静态的链接
		 */		
		LinkCache linkCache = CMSUtil.getLinkCache(context.getSite().getSecondName());
		CmsLinkTable linkTable = null;
		if(context.getTemplateLinkTable() != null)
			linkTable = context.getTemplateLinkTable().getTemplateLinkTable();
		
		if (linkTable != null && linkTable.size() > 0) {
			Iterator links = linkTable.iterator();
			String templateRootPath = context.getRealTemplateRootPath();
			while (links.hasNext()) {
				CMSLink link = (CMSLink) links.next();
				if(!link.isDirectory())
				{
					if (!link.getOrigineLink().isInternal())
						continue;
					switch (link.getRelativeFilePathType()) {
						case CMSLink.TYPE_ABSOLUTE:
							break;
						case CMSLink.TYPE_IMAGES:
							;
						case CMSLink.TYPE_STYLE:
							;
						case CMSLink.TYPE_TEMPLATE:
							String realpath = removeParam(link.getRelativeFilePath());
							String sourcepath = CMSUtil.getPath(templateRootPath ,realpath);
							String destinction = "";
							if(context.getActionType() == PublishObject.ACTIONTYPE_PUBLISH)
							{
								File source = new File(sourcepath);						
								if(!source.exists())
									continue;
								/**
								 * 这里判断缓存没有任何意义，因为动态记录的文件没有进行缓存处理，暂时注销
								 */
								
								long lastModified = source.lastModified();
								LinkTimestamp linkTimestamp = linkCache.cachTemplateLink(link, lastModified);
								boolean isModified = true;
								if(linkTimestamp != null )
								{
									isModified = linkTimestamp.isModified(lastModified);
								}
								if(!isModified ) //如果文件没有修改过
								{
									if(!context.forcepublishLinks(link))
									{
										continue;
									}
								}
								
								destinction = CMSUtil.getDirectroy(this.context.getPublishTemppath() + "/" + context.getSiteDir() ,realpath);
								try {
									FileUtil.copy(sourcepath, destinction);
									if(isModified )
									{
										if(linkTimestamp != null)
											linkTimestamp.setLastModifiedTime(lastModified);
									}
								} catch (IOException e) {
									context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
								}
							}
							else
							{
								destinction = CMSUtil.getDirectroy(this.context.getPreviewRootPath(),
										realpath);
								try {
									FileUtil.copy(sourcepath, destinction);
								} catch (IOException e) {
									context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
								}
							}
							
						default:
							break;
					}
				}
				else
				{
					distributeDir(templateRootPath,link);
				}
			}
		}
		
		/**
		 * 为什么需要处理一次
		 */
		if(context instanceof ContentContext)
		{
			ContentContext contentctx = (ContentContext)context;
			CmsLinkTable linkTable_ = contentctx.getContentLinkTable();
			if (linkTable_ != null && linkTable_.size() > 0) {
				Iterator links = linkTable_.iterator();
				String projectRootPath = context.getRealProjectRootPath();
				while (links.hasNext()) {
					CMSLink link = (CMSLink) links.next();
					String realpath  = this.removeParam(link.getRelativeFilePath());
					String sourcepath = CMSUtil.getPath(projectRootPath ,realpath);
					String destinction = "";
					if(context.getActionType() == PublishObject.ACTIONTYPE_PUBLISH)
					{
						File source = new File(sourcepath);						
						if(!source.exists())
							continue;
						/**
						 * 这里判断缓存没有任何意义，因为动态内容中引用的文件没有进行缓存处理，暂时注销
						 */
						
						long lastModified = source.lastModified();
						LinkTimestamp linkTimestamp = linkCache.cachContentLink(link, lastModified);
						boolean isModified = true;
						if(linkTimestamp != null )
						{
							isModified = linkTimestamp.isModified(lastModified);
						}
						if(!isModified ) //如果文件没有修改过
						{
							if(!context.forcepublishLinks(link))
							{
								continue;
							}
						}
						destinction = CMSUtil.getDirectroy(this.context.getPublishTemppath()  + "/" + context.getSiteDir() ,realpath);
						try {
							FileUtil.copy(sourcepath, destinction);
							if(isModified )
							{
								if(linkTimestamp != null)
									linkTimestamp.setLastModifiedTime(lastModified);
							}
						} catch (IOException e) {
							context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
							
						}
					}
					else
					{
						destinction = CMSUtil.getDirectroy(this.context.getPreviewRootPath(),realpath);
						try {
							FileUtil.copy(sourcepath, destinction);
						} catch (IOException e) {
							context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
							
						}
					}
					
				}
			}
		}
	}
	
	private String[] getIndexinfo(String uid)
	{
		return uid.split("\\|\\|");
	}
	
	/**
	 * 文档分发完成后更新索引库
	 * @param indexobjects<IndexObject>
	 * @param context
	 */
	protected void updateIndexs(Set indexobjects,Context context)
	{
		try{
			context.getPublishMonitor().addSuccessMessage("更新索引库开始，请稍等...",context.getPublisher());
			CMSSearchManager sm = new CMSSearchManager();
			Iterator it = indexobjects.iterator();
			while(it.hasNext())
			{
				IndexObject indexObject = (IndexObject)it.next();
				
				switch(indexObject.getType()){
					case IndexObject.DOCUMENT://文档细览页面
						try
						{
							String[] indexinfo  = this.getIndexinfo(indexObject.getUid());
							if(CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_LUCENE))
							{
								
								
								String siteId = indexinfo[0];
								String docId = indexinfo[2];
//								sm.deleteDocumetFromIndex(context.getRequestContext().getRequest(),docId,siteId);
								List attachmentList = CMSUtil.getCMSDriverConfiguration().getCMSService().getDocumentManager().
										getAllPublishedAttachmentOfDocument(context.getRequestContext().getRequest(),
																			indexObject.getDocument(),siteId,
																			docattachements,indexObject.getContentOrigineTemplateLinkTable());
								//关联整站索引和频道索引
								List indexList = sm.getIndexListOfCmsdocument(indexObject.getDocument(),siteId);
//								sm.deleteDocumetFromIndex(request,doc,siteId,attachmentList, indexList  );
								sm.addDocumetToIndex(context.getRequestContext().getRequest(),indexObject.getDocument(),siteId,attachmentList, indexList,true);
								
								
								context.getPublishMonitor().addSuccessMessage("更新索引库文件[site=" + siteId + ",docId=" + docId + "].",context.getPublisher());
							}
							if(CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_HAILIANG))
							{
								String destination = ConfigManager.getInstance().getConfigValue("cms.indexs.hailiang.rootpath");
								String[] temp  = (String[])indexObject.getIndexObject();
								if(destination == null || destination.equals(""))
								{
									context.getPublishMonitor().addFailedMessage("添加海量索引原始文件失败，没有指定海量的原始数据库路径",context.getPublisher());
									continue;
								}
								
//								destination = CMSUtil.getPath(destination,"site" + indexinfo[0]);
								destination = CMSUtil.getPath(destination,context.getSite().getSecondName());
								String source = CMSUtil.getPath(temp[0],temp[1]);
								
								destination = CMSUtil.getPath(destination,temp[1]);
								FileUtil.fileCopy(source,destination);
								
							}
							
						}
						catch(Exception e)
						{
							context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
						}
						break;
					case IndexObject.CHANNEL://频道首页
						break;
					case IndexObject.SITE://站点首页
						break;
						
					case IndexObject.PAGER://普通页面
						break;
					default:
						break;
				}
			}
			
			context.getPublishMonitor().addSuccessMessage("更新索引库结束",context.getPublisher());
		}
		catch(Exception e)
		{
			context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
		}
	}


	/**
	 * 分发模版和文档附件
	 * 
	 * @throws IOException
	 */
	private void distributeAttachment() throws IOException, DistributeException {
		if(context instanceof BatchContext)
		{
			
		}
		else if (context instanceof CMSContext) // 如果是站点发布,要发布站点首页模版对应的附件
		{

			// if(context.getPublishMonitor().containDistributeTemplate(templateid));
			// FileUtil.copy(this.file.getTemplateAttachementPath()
			// ,this.context.getPublishTemppath() + "/template_files");
			/*
			 * 发布站点首页模版的附件
			 */
			CMSContext cmscontext  = (CMSContext)context;
			if (!cmscontext.getPublishMonitor()
					.containDistributeTemplate(
							cmscontext.getIndexTemplate()
									.getTemplateId()
									+ "",
							"site:" + cmscontext.getSiteID())) {
					distributeTemplateAttacthments();
			}
		} 
		else if (context instanceof ContentContext) // 如果是文档发布，则发布文档本身对应的附件，文档细览模版对应的附件
		{
			try {
				
				ContentContext contentContext = (ContentContext) context;
				if(contentContext.isAggregation() )
						//|| contentContext.isPagintion())//为什么分页时不发布模板附件,yinbiaoping 注释于2008-1-17 下五17：48
					return;
				
				
				int contentid = Integer.parseInt(contentContext.getContentid());
				// FileUtil.copy(this.file.getAttachementPath(),this.context.getPublishTemppath());
				/**
				 * 发布文档的内容附件
				 */
				//文档的相关图片
				List pics = null;
				
				List attachments = null;
				TransactionManager tm = new TransactionManager();
				try
				{
					tm.begin(tm.RW_TRANSACTION);
					pics = context.getDriverConfiguration()
							.getCMSService().getDocumentManager()
							.getPicturesOfDocument(contentid);
				
					attachments = context.getDriverConfiguration()
							.getCMSService().getDocumentManager()
							.getRelationAttachmentsOfDocument(contentid);
					
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
				// String destinction = this.context.getPublishTemppath() + "/"
				// + context.getRendPath() + "/content_files" ;
				String srcpath = contentContext.getRealProjectPath() + "/content_files/";
				String destinction = "";
				if(context.getActionType() == PublishObject.ACTIONTYPE_PUBLISH)
					destinction = this.context.getPublishTemppath() + "/" + context.getSiteDir() + "/"
						+ context.getRendPath() + "/content_files";
				else
					destinction = this.context.getPreviewRootPath() +  "/"
					+ context.getRendPath() + "/content_files";
				String channelId = String.valueOf(contentContext.getDocument().getChanel_id());
				
				
				String relativePath = CMSUtil.getChannelCacheManager(contentContext.getSiteID()).getChannel(channelId).getChannelPath();
				if(pics != null && pics.size() > 0)
				{
					if(docattachements == null)
						docattachements = new ArrayList();
					
					
				}
				if(attachments != null && attachments.size() > 0)
				{
					if(docattachements == null)
						docattachements = new ArrayList();
					
				}
				for (int i = 0; pics != null && i < pics.size(); i++) {
					Attachment attachment = (Attachment) pics.get(i);
					String url = attachment.getUrl();
					// url = CMSUtil.getAppRootPath() + "/cms/siteResource/" +
					// sitedir + "/_webprj/" + relativePath + "/content_files/"
					// + url;
					url = CMSUtil.getPath(CMSUtil.getPath(relativePath, "content_files"), url);
					docattachements.add(url);
					
					FileUtil.copy(srcpath
							+ this.removeParam(attachment.getUrl()), destinction);
				}
				
				//文档的相关附件
				
				
				for (int i = 0; attachments != null && i < attachments.size(); i++) {
					Attachment attachment = (Attachment) attachments.get(i);
					String url = attachment.getUrl();
					// url = CMSUtil.getAppRootPath() + "/cms/siteResource/" +
					// sitedir + "/_webprj/" + relativePath + "/content_files/"
					// + url;
					url = CMSUtil.getPath(CMSUtil.getPath(relativePath, "content_files"), url);
					docattachements.add(url);
					FileUtil.copy(srcpath + this.removeParam(attachment.getUrl()), destinction);
				}
//              在distributeContextAttacthments方法中会发布文档内容附件，在此处先注销	，避免重复发布			
//				//文档内容附件
//				CmsLinkTable links = contentContext.getContentLinkTable();
//				Iterator it = links.iterator();
//				while(it.hasNext())
//				{
//					CMSLink link = (CMSLink)it.next();
//					FileUtil.copy(CMSUtil.getPath(CMSUtil.getAppRootPath() + "/" + context.getAbsoluteProjectRootPath(),
//												  link.getRelativeFilePath()), 
//								  destinction);
//				}
				
				
//
				/**
				 * 发布模版附件,内容细览模版的附件 如果当前频道下的细览模版已经发布过了,就不需要重新拷贝附件
				 */

				if (!context.getPublishMonitor()
						.containDistributeTemplate(
								contentContext.getDetailTemplate()
										.getTemplateId()
										+ "",
								"channeldetail:" + contentContext.getChannelid())) {
					distributeTemplateAttacthments();
//					distributeContextAttacthments();
				}

			} catch (IOException e) {
				context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
				throw e;

			} catch (DocumentManagerException e) {
				context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
				throw new DistributeException(e.getMessage());
			} catch (DriverConfigurationException e) {
				context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
				throw new DistributeException(e.getMessage());
			} catch (Exception e) {
				context.getPublishMonitor().addFailedMessage(StringUtil.exceptionToString(e), context.getPublisher());
				throw new DistributeException(e.getMessage());
			}

		} 
		else if (context instanceof ChannelContext) // 如果是频道发布，首先判断是否是发布频道所有的内容（包括频道概览模版，首页模版，频道包含的文档等等）
		{
			ChannelContext cmscontext  = (ChannelContext)context;
			if (cmscontext.isTemplateType() && 
					!cmscontext.getPublishMonitor()
					.containDistributeTemplate(
							cmscontext.getOutlineTemplate()
									.getTemplateId()
									+ "",
							"channeloutline:" + cmscontext.getChannelID())) {
				distributeTemplateAttacthments();
//				distributeContextAttacthments();
			}
		} 
		else if (context instanceof TemplateContext) // 如果是发布模版
		{
			distributeTemplateAttacthments();
		} 
		else if (context instanceof PageContext) // 如果是发布页面
		{
			PageContext cmscontext  = (PageContext)context;
			if (!cmscontext.getPublishMonitor()
					.containDistributeTemplate(
							cmscontext.getPagePath(),
							"page:" + cmscontext.getSiteID())) {
				distributeTemplateAttacthments();
//				distributeContextAttacthments();
			}
			
		}
	}
	
	
	


}


