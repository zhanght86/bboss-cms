package com.frameworkset.platform.cms.driver.publish.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestContext;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestDispatcher;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestDispatcherImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequestImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSServletResponse;
import com.frameworkset.platform.cms.driver.jsp.CMSServletResponseImpl;
import com.frameworkset.platform.cms.driver.publish.NotSupportPublishTypeException;
import com.frameworkset.platform.cms.driver.publish.PublishCallBack;
import com.frameworkset.platform.cms.driver.publish.PublishEngine;
import com.frameworkset.platform.cms.driver.publish.PublishException;
import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishException;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishManager;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authorization.AuthRole;

/**
 * 
  * <p>Title: APPPublish</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class APPPublish {
	
	/**
	 * 访问控制对象
	 */
	protected AccessControl accessControl;

	
	
	
	/**
	 * 标识当前发布的对象，包括：
	 *    站点
	 *    频道
	 *    文档
	 *    页面
	 *  4种
	 */
	protected int publishObjectType = PublishObject.OBJECT_SITE;
	
	/**
	 * 执行的操作类型：
	 * 0－发布
	 * 1-预览
	 */
	protected int actionType = 0;
	
	
	/**
	 * 标识当前的发布是增量发布还是完全发布，
	 * 本变量只针对频道和站点的发布有用
	 */
	protected boolean increament = true;
	
	/**
	 * 内容范围定义
	 */
	protected int[] publishScope = new int[] {PublishObject.PUBLISH_SITE_INDEX};
	
	
	/**
	 * 发布范围定义
	 * 		增量式－适用于站点、频道
	 * 		全部－适用于站点、频道
	 * 		首页－适用于站点
	 * 		概览首页－使用于频道
	 *      单片文档-适用于文档
	 */
	
	
	protected CMSRequestContext requestContext;
	
	/**
	 * 发布回调接口
	 */
	protected PublishCallBack publishCallBack;
	
	/**
	 * 设置全局的发布深度
	 */
	protected int maxPublishDepth = -1;
	
	
	/**
	 * 远程和本地发布选项
	 */
	protected boolean[] local2ndRemote = new boolean[] {true,false};
	
	/**
	 * 区分发布任务是否是异步还是同步执行
	 * false为同步执行
	 * true为异步执行
	 */
	protected boolean synchronize = true;
	
	/**
	 * 发布分发方式
	 */
	protected int[] distributeManners;



	private Document viewdocument;	
	
	private boolean enableRecursive = true;
	
	private boolean clearFileCache = false;


	/**
	 * 设置强制执行已发布文档的递归发布标识为true
	 */
	private boolean forceStatusPublished = false;
	
	/**
	 * 内部类调用
	 */
    private PublishMonitor monitor = null;
    private StringBuffer msgs = new StringBuffer();
	/**
	 * 发布站点 
	 * @param siteid
	 * @param publishScope  描述如下：	
	 *	 * 站点首页
	 *	 
	 *	public static final int  PUBLISH_SITE_INDEX = 1;
	 *	
	 *	
	 *	 * 频道首页
	 *	 
	 *	public static final int  PUBLISH_CHANNEL_INDEX = 2;
	 *	
	 *	
	 *	 * 频道内容
	 *	 
	 *	public static final int  PUBLISH_CHANNEL_CONTENT = 3;
	 *	
	 *	
	 *	 * 发布文档
	 *	
	 *	public static final int  PUBLISH_DOCUMENT = 4;
	 *
	 *	 * 发布频道、子频道下的内容
	 *  public static final int  PUBLISH_CHANNEL_RECCONTENT = 5;
	 *
	 *	 * 发布单个页面
	 *	 
	 *	public static final int  PUBLISH_PAGE = 6;
	 *	
	 *	 * 发布频道首页及子频道首页
	 *	 
	 *	public static final int  PUBLISH_CHANNEL_RECINDEX = 7;
	 *	
	 * @param increament 增量发布还是完整发布
	 * @param isRemote
	 */
	public void publishSite(String siteid,
							int[] publishScope,
							boolean[] local2ndRemote,
							boolean increament,
							int[] distributeManners)
	{
		try {
			this.actionType = PublishObject.ACTIONTYPE_PUBLISH;
			this.local2ndRemote = local2ndRemote;
			this.distributeManners = distributeManners; 
			this.increament = increament;
			this.publishScope = publishScope;
			this.publishObjectType = PublishObject.OBJECT_SITE;
			this.invokePublish(new String[]{siteid});
		} catch (NotSupportPublishTypeException e) {
			
			e.printStackTrace();
		} catch (PublishException e) {
			
			e.printStackTrace();
		}
	}
	
	/**
	 * 发布站点 
	 * @param siteid
	 * @param publishScope 
	 * @param increament 增量发布还是完整发布
	 * @param isRemote
	 */
	public void viewSite(String siteid)
	{
		try {
			this.actionType = PublishObject.ACTIONTYPE_VIEW;
			this.local2ndRemote = PublishObject.DEFAULT_PUBLISH_LOCAL;
			this.distributeManners = new int[]{0}; 
			this.increament = true;
			this.publishObjectType = PublishObject.OBJECT_SITE;
			this.invokePublish(new String[]{siteid});
		} catch (NotSupportPublishTypeException e) {
			
			e.printStackTrace();
		} catch (PublishException e) {
			
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 发布频道
	 * @param siteid
	 * @param channelid
	 * @param publishScope  描述如下：	
	 *	 * 站点首页
	 *	 
	 *	public static final int  PUBLISH_SITE_INDEX = 1;
	 *	
	 *	
	 *	 * 频道首页
	 *	 
	 *	public static final int  PUBLISH_CHANNEL_INDEX = 2;
	 *	
	 *	
	 *	 * 频道内容
	 *	 
	 *	public static final int  PUBLISH_CHANNEL_CONTENT = 3;
	 *	
	 *	
	 *	 * 发布文档
	 *	
	 *	public static final int  PUBLISH_DOCUMENT = 4;
	 *
	 *	 * 发布频道、子频道下的内容
	 *  public static final int  PUBLISH_CHANNEL_RECCONTENT = 5;
	 *
	 *	 * 发布单个页面
	 *	 
	 *	public static final int  PUBLISH_PAGE = 6;
	 *	
	 *	 * 发布频道首页及子频道首页
	 *	 
	 *	public static final int  PUBLISH_CHANNEL_RECINDEX = 7;
	 *
	 * @param isRemote
	 * @param increament 增量发布还是完全发布
	 * @param distributeManners
	 */
	public void publishChannel(String siteid,String channelid,
							   int[] publishScope,
							   boolean[] local2ndRemote,
							   boolean increament,
							   int[] distributeManners)
	{
		try {
			this.local2ndRemote = local2ndRemote;
			this.distributeManners = distributeManners; 
			this.increament = increament;
			this.publishScope = publishScope;
			this.actionType = PublishObject.ACTIONTYPE_PUBLISH;
			this.publishObjectType = PublishObject.OBJECT_CHANNEL;
			this.invokePublish(new String[] {siteid,channelid});
		} catch (NotSupportPublishTypeException e) {
			
			e.printStackTrace();
		} catch (PublishException e) {
			
			e.printStackTrace();
		}
	}
	
	public void setMonitor(PublishMonitor monitor)
	{
		this.monitor = monitor;
	}
	
	
	
	/**
	 * 发布频道
	 * @param siteid
	 * @param channelid
	 * @param publishScope
	 * @param isRemote
	 * @param increament 增量发布还是完全发布
	 * @param distributeManners
	 */
	public void viewChannel(String siteid,String channelid)
	{
		try {
			this.local2ndRemote = PublishObject.DEFAULT_PUBLISH_LOCAL;
			this.distributeManners = new int[] {0};  
			this.increament = true;
			this.publishScope = new int[] {PublishObject.PUBLISH_CHANNEL_INDEX};
			this.actionType = PublishObject.ACTIONTYPE_VIEW;
			this.publishObjectType = PublishObject.OBJECT_CHANNEL;
			this.invokePublish(new String[] {siteid,channelid});
		} catch (NotSupportPublishTypeException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 预览文档
	 * @param siteid
	 * @param channelid
	 * @param documentid
	 * @param publishScope
	 * @param isRemote
	 * @param distributeManners
	 */
	public void viewDocument(String siteid,String channelid,String documentid)
	{
		try {
			this.actionType = PublishObject.ACTIONTYPE_VIEW;
			this.publishScope =new int[]{ PublishObject.PUBLISH_DOCUMENT};
			this.local2ndRemote = PublishObject.DEFAULT_PUBLISH_LOCAL;
			this.publishObjectType = PublishObject.OBJECT_DOCUMENT;
			this.distributeManners = new int[] {0}; 
			this.increament = true;
			invokePublish(new String[] {siteid,channelid,documentid});
			
		} catch (NotSupportPublishTypeException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 预览正在采集的文档时调用本方法
	 * @param siteid
	 * @param channelid
	 * @param document
	 */
	public void viewDocument(String siteid,String channelid,Document document)
	{
		try {
			this.actionType = PublishObject.ACTIONTYPE_VIEW;
			this.publishScope =new int[]{ PublishObject.PUBLISH_DOCUMENT};
			this.local2ndRemote = PublishObject.DEFAULT_PUBLISH_LOCAL;
			this.publishObjectType = PublishObject.OBJECT_DOCUMENT;
			this.distributeManners = new int[] {0}; 
			this.increament = true;
			this.viewdocument = document;
			invokePublish(new String[] {siteid,channelid,System.currentTimeMillis() + ""});
			
		} catch (NotSupportPublishTypeException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发布文档
	 * @param siteid
	 * @param channelid
	 * @param documentid
	 * @param publishScope
	 * @param isRemote
	 * @param isRemote
	 * @param distributeManners
	 */
	public void publishDocument(String siteid,String channelid,String documentid,
			
			boolean[] local2ndRemote,
			
			int[] distributeManners)
	{
		try {
			this.publishObjectType = PublishObject.OBJECT_DOCUMENT;
			this.actionType = PublishObject.ACTIONTYPE_PUBLISH;
			this.publishScope = new int[] {PublishObject.PUBLISH_DOCUMENT};
			this.local2ndRemote = local2ndRemote;
			this.distributeManners = distributeManners; 
			invokePublish(new String[] {siteid,channelid,documentid});
			
		} catch (NotSupportPublishTypeException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 批量发布文档
	 * @param siteid
	 * @param channelid
	 * @param documentid
	 * @param publishScope
	 * @param isRemote
	 * @param isRemote
	 * @param distributeManners
	 */
	public void publishBatchDocument(String siteid,String channelid,String documentids[],
			
			boolean[] local2ndRemote,
			
			int[] distributeManners)
	{
		try {
			this.publishObjectType = PublishObject.OBJECT_DOCUMENT;
			this.actionType = PublishObject.ACTIONTYPE_PUBLISH;
			this.publishScope = new int[] {PublishObject.PUBLISH_DOCUMENT};
			this.local2ndRemote = local2ndRemote;
			this.distributeManners = distributeManners; 
			String[][] params = new String[documentids.length][];
			for(int i = 0; i < params.length; i ++ )
			{
				params[i] = new String[] {siteid,channelid,documentids[i]};
				
				
			}
			invokeBatchPublish(params);
			
		} catch (NotSupportPublishTypeException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 批量发布文档 所有文档都在同一个站点下的 
	 * @param String[][] documentids 
	 * @param boolean[] local2ndRemote 
	 * @param int[] distributeManners
	 * 批量发布对象二维数组
	 * documentids封装多个文档的站点，频道，和文档id信息
	 * 例如:{
	 * 			{siteid,channelid,docid},
	 * 			{siteid,channelid,docid},
	 * 			{siteid,channelid,docid},
	 *          ...
	 *     }
	 */
	public void publishBatchDocument(String documentids[][],
			
			boolean[] local2ndRemote,
			
			int[] distributeManners)
	{
		try {
			this.publishObjectType = PublishObject.OBJECT_DOCUMENT;
			this.actionType = PublishObject.ACTIONTYPE_PUBLISH;
			this.publishScope = new int[] {PublishObject.PUBLISH_DOCUMENT};
			this.local2ndRemote = local2ndRemote;
			this.distributeManners = distributeManners; 
			
			invokeBatchPublish(documentids);
			
		} catch (NotSupportPublishTypeException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 批量发布文档 所有文档 不在同一个站点下的 
	 * @param documentids 
	 * APPPublish.java
	 * @author: ge.tao
	 */
	public void publishBatchDocument(int documentids[][]){
		try {
			this.publishObjectType = PublishObject.OBJECT_DOCUMENT;
			this.actionType = PublishObject.ACTIONTYPE_PUBLISH;
			this.publishScope = new int[] {PublishObject.PUBLISH_DOCUMENT};
//			this.local2ndRemote = local2ndRemote;
//			this.distributeManners = distributeManners; 
			String[][] params = new String[documentids.length][];
			for(int i = 0; i < params.length; i ++ )
			{
				params[i] = new String[] {documentids[i][0] + "",documentids[i][1] + "",documentids[i][2] + ""};
				
				
			}
			invokeBatchPublish(params);
			
		} catch (NotSupportPublishTypeException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 发布页面
	 * @param siteid
	 * @param pagerUrl
	 * @param isRemote
	 * @param distributeManners
	 */
	public void publishPage(String siteid,String pagerUrl,boolean[] local2ndRemote,int[] distributeManners)
	{
		this.actionType = PublishObject.ACTIONTYPE_PUBLISH;
		this.publishScope = new int[] {PublishObject.PUBLISH_PAGE};
		this.local2ndRemote = local2ndRemote;
		this.increament = false;
		this.publishObjectType = PublishObject.OBJECT_PAGE;
		this.distributeManners = distributeManners;
		
		try {
			String pateType = CMSUtil.getPageType(pagerUrl) + "";
			
			this.invokePublish(new String[]{siteid,pagerUrl,pateType});
		} catch (NotSupportPublishTypeException e) {
			
			e.printStackTrace();
		} catch (PublishException e) {
			
			e.printStackTrace();
		}
		
	}
	

	/**
	 * 发布目录
	 * @param siteid
	 * @param pagerUrl
	 * @param isRemote
	 * @param distributeManners
	 */
	public void publishDirectory(String siteid,String pagerUrl,boolean[] local2ndRemote,int[] distributeManners)
	{
		this.actionType = PublishObject.ACTIONTYPE_PUBLISH;
		this.publishScope = new int[] {PublishObject.PUBLISH_PAGE};
		this.local2ndRemote = local2ndRemote;
		this.increament = false;
		this.publishObjectType = PublishObject.OBJECT_PAGE;
		this.distributeManners = distributeManners;
		
		try {
			String pateType = CMSLink.TYPE_DIRECTORY + "";
			
			this.invokePublish(new String[]{siteid,pagerUrl,pateType});
		} catch (NotSupportPublishTypeException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 发布页面
	 * @param siteid
	 * @param pagerUrl
	 * @param isRemote
	 * @param distributeManners
	 */
	public void viewPage(String siteid,String pagerUrl,boolean[] local2ndRemote,int[] distributeManners)
	{
		this.actionType = PublishObject.ACTIONTYPE_VIEW;
		this.publishScope = new int[] {PublishObject.PUBLISH_PAGE};
		this.local2ndRemote = local2ndRemote;
		this.increament = true;
		this.publishObjectType = PublishObject.OBJECT_PAGE;
		this.distributeManners = distributeManners;
		try {
			this.invokePublish(new String[]{siteid,pagerUrl});
		} catch (NotSupportPublishTypeException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获取当前发布人员的信息
	 * @return String[] {userid,userinfo,roles};
	 */
	private String[] getPublisher()
	{
		/**
		 * 获取发布人员的信息
		 */
		String userId = this.accessControl.getUserID();
		String userAccount = this.accessControl.getUserAccount();
		String userName = accessControl.getUserName();
		AuthRole[] roles = AccessControl.getAllRoleofUser(userAccount);
		StringBuffer roles_ = new StringBuffer();
		boolean flag = false;
		for(int i = 0; roles != null && i < roles.length; i ++)
		{
			if(flag)
				roles_.append(",").append(roles[i].getRoleName());
			else
			{
				roles_.append(roles[i].getRoleName());
				flag = true;
			}
		}
		String[] publisher = new String[] {userId,
										   userAccount.concat("[").concat(userName).concat("]"),
										   roles_.toString()};
		return publisher;
	}
	/**
	 * 调用发布处理
	 * @param type
	 * @param params
	 * @throws NotSupportPublishTypeException
	 * @throws PublishException
	 */
	private void invokePublish(String[] params) throws NotSupportPublishTypeException, PublishException
	{
		PublishObject publishObject = null;
		
		PublishEngine publishEngine = this.requestContext
		   .getDriverConfiguration()
		   .getPublishEngine();
		String[] publisher = getPublisher();
		switch(publishObjectType)
		{
			case PublishObject.OBJECT_SITE:
			
				publishObject = new SitePublishObject(requestContext,
													  publishScope,
													  params,
													  publisher,
													  true,
													  local2ndRemote 	,
													  distributeManners );
				publishObject.setIncreament(this.increament);
				break;
			case PublishObject.OBJECT_CHANNEL://发布频道
				
				publishObject = new ChannelPublishObject(requestContext,publishScope,params,publisher,true,
						local2ndRemote 	,
						  distributeManners );
				
//				publishEngine.publish(publishObject,publishCallBack);
				publishObject.setIncreament(this.increament);
				break;
				
			case PublishObject.OBJECT_DOCUMENT:
				if(this.viewdocument == null)
				{
					publishObject = new ContentPublishObject(requestContext,publishScope,params,publisher,true,
							local2ndRemote 	,
														  distributeManners );
				}
				else
				{
					publishObject = new ContentPublishObject(requestContext,publishScope,params,publisher,true,
							local2ndRemote 	,
							distributeManners,
							viewdocument );
				}
//				publishEngine.publish(publishObject,publishCallBack);
				break;
			case PublishObject.OBJECT_PAGE:
				publishObject = new PagePublishObject(requestContext,publishScope,params,publisher,true,
						local2ndRemote 	,
													  distributeManners );
				break;
			default:
				throw new NotSupportPublishTypeException("未受支持的发布类型：type=" + this.publishScope);
				
				
				
		}		
		if(publishObject != null)
		{
			
			publishObject.setActionType(this.actionType);
			publishObject.setSynchronized(this.isSynchronized());
			if(actionType == PublishObject.ACTIONTYPE_PUBLISH)
				publishObject.setMaxPublishDepth(this.maxPublishDepth);
			else //如果是预览模式则只需将发布深度置为2即可
				publishObject.setMaxPublishDepth(1);
			publishObject.forceStatusPublished(this.forceStatusPublished);
			if(monitor != null)
			{
				publishObject.setMonitor(this.monitor);
			}
			publishObject.setEnableRecursive(this.enableRecursive);
			publishObject.setClearFileCache(this.isClearFileCache());
			
			publishEngine.publish(publishObject,publishCallBack);
			
		}
		
	}
	
	
	
	
	/**
	 * 调用批处理发布操作
	 * @param batchObjects
	 * 批量发布对象二维数组
	 * 封装多个文档的站点，频道，和文档id信息
	 * 例如:{
	 * 			{siteid,channelid,docid},
	 * 			{siteid,channelid,docid},
	 * 			{siteid,channelid,docid},
	 *          ...
	 *     }
	 *	 
	 * @throws NotSupportPublishTypeException
	 * @throws PublishException
	 */
	private void invokeBatchPublish(String[][] batchObjects) throws NotSupportPublishTypeException, PublishException
	{
		try
		{
			BatchPublishObject publishObject = null;
			
			PublishEngine publishEngine = this.requestContext
			   .getDriverConfiguration()
			   .getPublishEngine();
			String[] publisher = getPublisher();
			
			publishObject = new BatchPublishObject(requestContext,
					  publishScope,
					  batchObjects,
					  publisher,
					  true,
					  local2ndRemote 	, 
					  distributeManners);
	//		switch(publishObjectType)
	//		{
	//			case PublishObject.OBJECT_SITE:
	//			
	//				publishObject = new SitePublishObject(requestContext,
	//													  publishScope,
	//													  params,
	//													  publisher,
	//													  true,
	//													  local2ndRemote 	,
	//													  distributeManners );
	//				publishObject.setIncreament(this.increament);
	//				break;
	//			case PublishObject.OBJECT_CHANNEL://发布频道
	//				
	//				publishObject = new ChannelPublishObject(requestContext,publishScope,params,publisher,true,
	//						local2ndRemote 	,
	//						  distributeManners );
	//				
	////				publishEngine.publish(publishObject,publishCallBack);
	//				publishObject.setIncreament(this.increament);
	//				break;
	//				
	//			case PublishObject.OBJECT_DOCUMENT:
	//				publishObject = new ContentPublishObject(requestContext,publishScope,params,publisher,true,
	//						local2ndRemote 	,
	//														  distributeManners );
	////				publishEngine.publish(publishObject,publishCallBack);
	//				break;
	//			case PublishObject.OBJECT_PAGE:
	//				publishObject = new PagePublishObject(requestContext,publishScope,params,publisher,true,
	//						local2ndRemote 	,
	//													  distributeManners );
	//				break;
	//			default:
	//				throw new NotSupportPublishTypeException("未受支持的发布类型：type=" + this.publishScope);
					
					
					
	//		}
	//		if(publishObject != null)
			{
				if(monitor != null)
				{
					publishObject.setMonitor(this.monitor);
				}
				publishObject.setMaxPublishDepth(this.maxPublishDepth);
				publishObject.setActionType(this.actionType);
				publishObject.setPublishObjectType(this.publishObjectType);
				publishObject.setSynchronized(this.isSynchronized());
				publishObject.setMaxPublishDepth(this.maxPublishDepth);
				publishObject.forceStatusPublished(this.forceStatusPublished);
				if(publishCallBack == null)
				{
					publishCallBack = new PublishCallBackImpl();
				}
				publishObject.setEnableRecursive(this.enableRecursive);
				publishObject.setClearFileCache(this.isClearFileCache());
				publishEngine.publish(publishObject,publishCallBack);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}



	public void setPublishCallBack(PublishCallBack publishCallBack) {
		this.publishCallBack = publishCallBack;
	}

	

	public boolean isSynchronized() {
		return synchronize;
	}

	public void setSynchronized(boolean synchronize) {
		this.synchronize = synchronize;
	}
	
	/**
	 * 由于发布时的故障（比如意外中止）使文档最终状态变为“正在发布中”，系统需要提供
	 * 针对这类文件状态恢复到先前状态的功能，
	 * 但是状态变为“正在发布中”的文档可能确实正在发布，这时是不能进行恢复的，对于哪些文档能恢复
	 * 哪些不能，需要调用本方法来进行判断 
	 * @param publishingDocumentid
	 * @return
	 */
	public static boolean isRealPublishing(String publishingDocumentid)
	{
		return false;
	}
	
//	
//	/**
//	 * 递归发布频道对应的发布对象,在频道嵌套递归发布时调用
//	 * @param context 嵌套递归发布的上下文
//	 * @param refchannel 需要发布的频道
//	 */
//	public void recursivePubObjectOfChannel(Context context, String refchannel) throws RecursivePublishException{
//		
//	}
	
	/**
	 * 当频道的信息发生变化时，发布频道相关的发布对象
	 * 
	 * @param refchannel
	 */
	public void recursivePubObjectOfChannel(String siteid,String refchannel,int actiontype) throws RecursivePublishException{
		try {
			Set publishObjects = CMSUtil.getCMSDriverConfiguration()
										 .getCMSService()
										 .getRecursivePublishManager()
										 .getAllPubObjectsOfChannel(new DefaultContextImpl(siteid),refchannel);
			if(publishObjects == null || publishObjects.size() == 0)
				return;
			
			PublishEngine publishEngine = this.requestContext
			   .getDriverConfiguration()
			   .getPublishEngine();
			String[] publisher = getPublisher();
			
			PublishObject publishObject = new RecursivePublishObject(requestContext, 
															         siteid,
														    		 publishObjects,
																	 publisher,
																	 true,
																	  local2ndRemote 	,
																	  distributeManners);
			if(this.publishCallBack == null)
			{
				PublishCallBack callback = new PublishCallBackImpl();
				setPublishCallBack(callback);
			}
			publishObject.setIncreament(true);
			publishObject.setActionType(PublishObject.ACTIONTYPE_PUBLISH);
			publishObject.setSynchronized(false);	
			publishObject.forceStatusPublished(this.forceStatusPublished);
			if(monitor != null)
			{
				publishObject.setMonitor(this.monitor);
			}
			publishObject.setEnableRecursive(this.enableRecursive);
			publishObject.setClearFileCache(this.isClearFileCache());
			publishEngine.publish(publishObject,publishCallBack);		
			
		} catch (RecursivePublishException e) {
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 当频道的信息发生变化时，发布频道相关的发布对象
	 * 
	 * @param refchannel
	 */
	public void recursivePubObjectOfChannelAncester(String siteid,String refchannelancester,int actiontype) throws RecursivePublishException{
		try {
			Set publishObjects = CMSUtil.getCMSDriverConfiguration()
										 .getCMSService()
										 .getRecursivePublishManager()
										 .getAllPubObjectsOfChannelAncester(new DefaultContextImpl(siteid),refchannelancester);
			if(publishObjects == null || publishObjects.size() == 0)
				return;
			
			PublishEngine publishEngine = this.requestContext
			   .getDriverConfiguration()
			   .getPublishEngine();
			String[] publisher = getPublisher();
			

			PublishObject publishObject = new RecursivePublishObject(requestContext, 
					 siteid,
		    		 publishObjects,
					 publisher,
					 true,
					  local2ndRemote 	,
					  distributeManners);
			if(this.publishCallBack == null)
			{
				PublishCallBack callback = new PublishCallBackImpl();
				setPublishCallBack(callback);
			}
			if(monitor != null)
			{
				publishObject.setMonitor(this.monitor);
			}
			publishObject.setIncreament(true);
			publishObject.setActionType(PublishObject.ACTIONTYPE_PUBLISH);
			publishObject.setSynchronized(true);
			publishObject.forceStatusPublished(this.forceStatusPublished);
			publishObject.setEnableRecursive(this.enableRecursive);
			publishObject.setClearFileCache(this.isClearFileCache());
			publishEngine.publish(publishObject,publishCallBack);		
					
		} catch (RecursivePublishException e) {
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		}
		
	}
	
//	/**
//	 * 递归发布文档对应的发布对象,用于嵌套的递归发布
//	 * @param context 外部嵌套的发布上下文
//	 * @param refdocument
//	 * @parma actiontype 对文档采取的操作
//	 */
//	public void recursivePubObjectOfDocument(Context context, String refdocument, String actiontype) throws RecursivePublishException{
//		
//	}
	
	
	
	/**
	 * 删除ftp或者本地发布文件
	 * 获得 ftp/本地 的文档路径:相对路径+ 发布后的文件名
	 * @param docId
	 */
	public static void deletePublishFile(String siteid,int docId,
											HttpServletRequest request,
											HttpServletResponse response) throws Exception {
		DocumentManager docImpl = new DocumentManagerImpl();
		docImpl.deletePubDocAttFiles(request,docId,siteid);
	}
	
	public static boolean isNull(String str){
		boolean flag = false;
		if(str == null || str.trim().length()<=0){
			flag = true;
		}
		return flag;
	}

	public int getMaxPublishDepth() {
		return maxPublishDepth;
	}

	public void setMaxPublishDepth(int maxPublishDepth) {
		this.maxPublishDepth = maxPublishDepth;
	}
	
	/**
	 * 撤销发布，撤销发布,没有封装递归发布，因为递归发布太耗时
	 * @param siteid
	 * @param refdocument
	 * @param actiontype
	 * @throws Exception
	 */
	public void withdrawPublish(String siteid,String refdocument) throws Exception{
		/*更新文档状态*/
		DocumentManager dm = new DocumentManagerImpl();
		if(refdocument!=null && refdocument.length()>0)
			dm.withdrawPublish(Integer.parseInt(refdocument));
		
		deletePublishFile(siteid,Integer.parseInt(refdocument),this.requestContext.getRequest(),this.requestContext.getResponse());
		
	}
	
	/**
	 * 递归发布文档对应的发布对象,
	 * 
	 * @param refdocument
	 * @parma actiontype 对文档采取的操作
	 */
	public void recursivePubObjectOfDocument(String siteid,Document document, int actiontype) throws RecursivePublishException{
		try {
			Set publishObjects = null;
			if(actiontype != RecursivePublishManager.ACTIONTYPE_DELETE)
			{
				publishObjects = CMSUtil.getCMSDriverConfiguration()
									 .getCMSService()
									 .getRecursivePublishManager()
									 .getAllPubObjectsOfDocument(new DefaultContextImpl(siteid),document);
			}
			else
			{
			
				
				publishObjects = CMSUtil.getCMSDriverConfiguration()
											 .getCMSService()
											 .getRecursivePublishManager()
											 .getAllPubObjectsOfDocument(new DefaultContextImpl(siteid),document.getDocument_id() + "");
			}
			if(publishObjects == null || publishObjects.size() == 0)
			{
				return;
			}
			PublishEngine publishEngine = this.requestContext
			   .getDriverConfiguration()
			   .getPublishEngine();
			String[] publisher = getPublisher();
			PublishObject publishObject = new RecursivePublishObject(requestContext, 
																	 siteid,
														    		 publishObjects,
																	 publisher,
																	 true,
																	  local2ndRemote 	,
																	  distributeManners);
			if(this.publishCallBack == null)
			{
				PublishCallBack callback = new PublishCallBackImpl();
				setPublishCallBack(callback);
			}
			if(monitor != null)
			{
				publishObject.setMonitor(this.monitor);
			}
			publishObject.setIncreament(true);
			publishObject.forceStatusPublished(this.forceStatusPublished);
			publishObject.setActionType(PublishObject.ACTIONTYPE_PUBLISH);
			publishObject.setSynchronized(true);	
			publishObject.setEnableRecursive(this.enableRecursive);
			publishObject.setClearFileCache(this.isClearFileCache());
			publishEngine.publish(publishObject,publishCallBack);		
			
		} catch (RecursivePublishException e) {
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		}
	}
	
	public void recursivePubObjectOfDocument(String siteid, String refdocument, int actiontype) {
		
		try {
			Set publishObjects = null;
			
				
			publishObjects = CMSUtil.getCMSDriverConfiguration()
										 .getCMSService()
										 .getRecursivePublishManager()
										 .getAllPubObjectsOfDocument(new DefaultContextImpl(siteid),refdocument);
			if(publishObjects == null || publishObjects.size() == 0)
				return;
			PublishEngine publishEngine = this.requestContext
			   .getDriverConfiguration()
			   .getPublishEngine();
			String[] publisher = getPublisher();
			PublishObject publishObject = new RecursivePublishObject(requestContext, 
																	 siteid,
														    		 publishObjects,
																	 publisher,
																	 true,
																	  local2ndRemote 	,
																	  distributeManners);
			if(this.publishCallBack == null)
			{
				PublishCallBack callback = new PublishCallBackImpl();
				setPublishCallBack(callback);
			}
			publishObject.setIncreament(true);
			publishObject.forceStatusPublished(this.forceStatusPublished);
			publishObject.setActionType(PublishObject.ACTIONTYPE_PUBLISH);
			publishObject.setSynchronized(true);
			if(monitor != null)
			{
				publishObject.setMonitor(this.monitor);
			}
			publishObject.setEnableRecursive(this.enableRecursive);
			publishObject.setClearFileCache(this.isClearFileCache());
			publishEngine.publish(publishObject,publishCallBack);		
			/**
			 * 如果操作执行的是文档的物理删除操作,则在文档的递归发布完成后，
			 * 必须将文档在发布元素引用关系表中的数据删除，包括：
			 * 1.文档引用其他元素的数据
			 * 2.其他发布对象引用文档的数据
			 */
			if(actiontype == RecursivePublishManager.ACTIONTYPE_DELETE)
			{
				Site site = CMSUtil.getSiteCacheManager().getSite(siteid);
				CMSUtil.getCMSDriverConfiguration()
				 .getCMSService()
				 .getRecursivePublishManager().removeAllPubObjectOfRefelement(site,refdocument,RecursivePublishManager.REFOBJECTTYPE_DOCUMENT + "");
				CMSUtil.getCMSDriverConfiguration()
				 .getCMSService()
				 .getRecursivePublishManager().deleteRefObjectsOfPubobject(site,refdocument,RecursivePublishManager.PUBOBJECTTYPE_DOCUMENT);
			}
		} catch (RecursivePublishException e) {
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 批量文档递归发布
	 * @param siteid
	 * @param refdocuments
	 * @param actiontype
	 */
	public void recursivePubObjectOfDocuments(String siteid,String[] refdocuments, int actiontype) throws Exception{
//		throw new Exception("****尚未实现****");
		try {
			Set publishObjects = null;
			publishObjects = CMSUtil.getCMSDriverConfiguration()
										 .getCMSService()
										 .getRecursivePublishManager()
										 .getAllPubObjectsOfDocuments(new DefaultContextImpl(siteid),refdocuments);
			if(publishObjects == null || publishObjects.size() == 0)
				return;
			
			PublishEngine publishEngine = this.requestContext
			   .getDriverConfiguration()
			   .getPublishEngine();
			String[] publisher = getPublisher();
			PublishObject publishObject = new RecursivePublishObject(requestContext, 
																	 siteid,
														    		 publishObjects,
																	 publisher,
																	 true,
																	  local2ndRemote 	,
																	  distributeManners);
			if(this.publishCallBack == null)
			{
				PublishCallBack callback = new PublishCallBackImpl();
				setPublishCallBack(callback);
			}
			publishObject.setIncreament(true);
			publishObject.setActionType(PublishObject.ACTIONTYPE_PUBLISH);
			publishObject.setSynchronized(true);
			publishObject.forceStatusPublished(this.forceStatusPublished);
			if(monitor != null)
			{
				publishObject.setMonitor(this.monitor);
			}
			publishObject.setEnableRecursive(this.enableRecursive);
			publishObject.setClearFileCache(this.isClearFileCache());
			publishEngine.publish(publishObject,publishCallBack);		
			
			/**
			 * 如果操作执行的是文档的物理删除操作,则在文档的递归发布完成后，
			 * 必须将文档在发布元素引用关系表中的数据删除，包括：
			 * 1.文档引用其他元素的数据
			 * 2.其他发布对象引用文档的数据
			 */
			if(actiontype == RecursivePublishManager.ACTIONTYPE_DELETE)
			{
				Site site = CMSUtil.getSiteCacheManager().getSite(siteid);
				CMSUtil.getCMSDriverConfiguration()
				 .getCMSService()
				 .getRecursivePublishManager().removeAllPubObjectOfRefelement(site,refdocuments,RecursivePublishManager.REFOBJECTTYPE_DOCUMENT + "");
				CMSUtil.getCMSDriverConfiguration()
				 .getCMSService()
				 .getRecursivePublishManager().deleteRefObjectsOfPubobject(site,refdocuments,RecursivePublishManager.PUBOBJECTTYPE_DOCUMENT);
			}
			
		} catch (RecursivePublishException e) {
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 批量文档递归发布
	 * @param siteid 注意当前操作的站点id和每个文档对应的id可能不一致，在以后的版本中再完善
	 * @param refdocuments
	 * @param actiontype
	 */
	public void recursivePubObjectOfDocuments(String siteid,List refdocuments, int actiontype) throws Exception{
//		throw new Exception("****尚未实现****");
		try {
			Set publishObjects = null;
			publishObjects = CMSUtil.getCMSDriverConfiguration()
										 .getCMSService()
										 .getRecursivePublishManager()
										 .getAllPubObjectsOfDocuments(new DefaultContextImpl(siteid),refdocuments);
			if(publishObjects == null || publishObjects.size() == 0)
				return;
			
			PublishEngine publishEngine = this.requestContext
			   .getDriverConfiguration()
			   .getPublishEngine();
			String[] publisher = getPublisher();
			PublishObject publishObject = new RecursivePublishObject(requestContext, 
																	 siteid,
														    		 publishObjects,
																	 publisher,
																	 true,
																	  local2ndRemote 	,
																	  distributeManners);
			if(this.publishCallBack == null)
			{
				PublishCallBack callback = new PublishCallBackImpl();
				setPublishCallBack(callback);
			}
			publishObject.setIncreament(true);
			publishObject.setActionType(PublishObject.ACTIONTYPE_PUBLISH);
			publishObject.setSynchronized(true);	
			publishObject.forceStatusPublished(this.forceStatusPublished);
			if(monitor != null)
			{
				publishObject.setMonitor(this.monitor);
			}
			publishObject.setEnableRecursive(this.enableRecursive);
			publishObject.setClearFileCache(this.isClearFileCache());
			publishEngine.publish(publishObject,publishCallBack);	
			/**
			 * 如果操作执行的是文档的物理删除操作,则在文档的递归发布完成后，
			 * 必须将文档在发布元素引用关系表中的数据删除，包括：
			 * 1.文档引用其他元素的数据
			 * 2.其他发布对象引用文档的数据
			 */
			if(actiontype == RecursivePublishManager.ACTIONTYPE_DELETE)
			{
				Site site = CMSUtil.getSiteCacheManager().getSite(siteid);
				CMSUtil.getCMSDriverConfiguration()
				 .getCMSService()
				 .getRecursivePublishManager().removeAllPubObjectOfRefelement(site,refdocuments,RecursivePublishManager.REFOBJECTTYPE_DOCUMENT + "");
				CMSUtil.getCMSDriverConfiguration()
				 .getCMSService()
				 .getRecursivePublishManager().deleteRefObjectsOfPubobject(site,refdocuments,RecursivePublishManager.PUBOBJECTTYPE_DOCUMENT);
			}
		} catch (RecursivePublishException e) {
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			e.printStackTrace();
		} catch (PublishException e) {
			e.printStackTrace();
		}
	}

	public void setEnableRecursive(boolean enableRecursive) {
		this.enableRecursive = enableRecursive;
	}
	
	/**
	 * 判断当前发布任务是否允许递归发布
	 * @return
	 */
	public boolean enableRecursive()
	{
		return this.enableRecursive;
	}
	
	/**
	 * 将参数追加到url的后面
	 * @param jspurl
	 * @param parameters
	 * @return
	 */
	public String buildUrl(String jspurl,Map parameters)
	{
		if(jspurl == null)
			return null;
		if(parameters.isEmpty())
			return jspurl;
		int idx = jspurl.indexOf('?');
		if(idx == -1)
		{
			StringBuffer buffer = new StringBuffer(jspurl);
			buffer.append("?");
			
			Set entrys = parameters.entrySet();
			Iterator it = entrys.iterator();
			boolean flag = false;
			while(it.hasNext())
			{
				if(flag)
				{
					Map.Entry entry = (Map.Entry)it.next();
					buffer.append("&").append(entry.getKey()).append("=").append(entry.getValue());
				}
				else
				{
					Map.Entry entry = (Map.Entry)it.next();
					buffer.append(entry.getKey()).append("=").append(entry.getValue());
					flag = true;
				}
			}
			return buffer.toString();
			
		}
		else
		{
			StringBuffer buffer = new StringBuffer(jspurl);
			Set entrys = parameters.entrySet();
			Iterator it = entrys.iterator();
			while(it.hasNext())
			{
				Map.Entry entry = (Map.Entry)it.next();
				buffer.append("&").append(entry.getKey()).append("=").append(entry.getValue());
				
			}
			return buffer.toString();
		}
		
	}
	
	public String getJspUri(
			String jspurl)
	{
		if(jspurl == null || jspurl.length() == 0)
			return "";
		int idx = jspurl.indexOf('?');
		if(idx == -1)
			return jspurl;
		return jspurl.substring(0,idx);
	}
	
	public String getHtmlUri(String uri)
	{
		int idx = uri.lastIndexOf(".");
		if(idx == -1)
		{
			uri = uri + ".htm";
			return uri;
			
		}
		else
		{
			uri = uri.substring(0,idx);
			uri = uri + ".htm"; 
			return uri;
		}
			
	}
	
	/**
	 * 根据map中的参数将动态页面发布成静态页面，页面的参数保存在parameters中
	 
	 * 例如/chenzhoumenhu/index.jsp
	 * 会变为/chenzhoumenhu/index.htm
	 * 假设绝对路径为d:/workspace/cms/creatorcms
	 * @param jspurl
	 * @param parameters
	 */
	
	public void publishJsp(String jspurl,Map parameters)
	{
		String uri = getHtmlUri(this.getJspUri(jspurl));
		
		
		
		String path = CMSUtil.getPath(CMSUtil.getAppRootPath() ,uri);
		jspurl = this.buildUrl(jspurl,parameters);
		RequestDispatcher dispatcher = requestContext.getRequest()
		.getRequestDispatcher(jspurl);
		CMSRequestDispatcher cmsDispatcher = new CMSRequestDispatcherImpl(dispatcher);
		CMSServletRequest request = new CMSServletRequestImpl(requestContext
				.getRequest(), requestContext.getPageContext());
		CMSServletResponse response = new CMSServletResponseImpl(requestContext
				.getResponse());
		
		try {
			cmsDispatcher.commoninclude(request, response);
			StringBuffer out = response.getInternalBuffer().getBuffer();
			FileUtil.saveFile(path,out.toString(),CMSUtil.getCharset());
			
		}
		catch(Exception e)
		{
			
		}
	}
	
	/**
	 * 将动态页面转换为静态页面
	 * 例如/chenzhoumenhu/index.jsp
	 * 会变为/chenzhoumenhu/index.htm
	 * 假设绝对路径为d:/workspace/cms/creatorcms
	 * @param jspurl
	 */
	public void publishJsp(String jspurl)
	{
		String uri = getHtmlUri(this.getJspUri(jspurl));
		
		
		
		String path = CMSUtil.getPath(CMSUtil.getAppRootPath() ,uri);
//		jspurl = this.buildUrl(jspurl,parameters);
		RequestDispatcher dispatcher = requestContext.getRequest()
		.getRequestDispatcher(jspurl);
		CMSRequestDispatcher cmsDispatcher = new CMSRequestDispatcherImpl(dispatcher);
		CMSServletRequest request = new CMSServletRequestImpl(requestContext
				.getRequest(), requestContext.getPageContext());
		CMSServletResponse response = new CMSServletResponseImpl(requestContext
				.getResponse());
		
		try {
			cmsDispatcher.commoninclude(request, response);
			StringBuffer out = response.getInternalBuffer().getBuffer();
			FileUtil.saveFile(path,out.toString(),CMSUtil.getCharset());
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置强制执行已发布文档的递归发布标识
	 * 为true时将执行已经发布文档的递归发布操作
	 * 为false时不执行，缺省为false
	 * @param forceStatusPublished
	 */
	public void forceStatusPublished(boolean forceStatusPublished)
	{
		this.forceStatusPublished = forceStatusPublished;
	}

	public boolean isClearFileCache() {
		return clearFileCache;
	}

	public void setClearFileCache(boolean clearFileCache) {
		this.clearFileCache = clearFileCache;
	}
	
//	public String IntervalGetNewestMsg(final PublishMonitor ext_monitor){
//				
//		//内部类
//        TimerTask task = new TimerTask() {  
//        	public void run() {            	
//            	List newestMsg = ext_monitor.getNewestMessages();
//            	for(int i=0;i<newestMsg.size();i++){
//            		msgs.append(newestMsg.get(i));
//                }
//                //System.exit(0);   
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(task, 0, 500);        
//		return msgs.toString();
//	}

	
}