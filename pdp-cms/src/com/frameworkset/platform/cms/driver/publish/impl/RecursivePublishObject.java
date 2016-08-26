package com.frameworkset.platform.cms.driver.publish.impl;

import java.util.Iterator;
import java.util.Set;

import com.frameworkset.platform.cms.driver.config.DocumentStatus;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.RecursiveContext;
import com.frameworkset.platform.cms.driver.context.impl.CMSContextImpl;
import com.frameworkset.platform.cms.driver.context.impl.RecursiveContextImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestContext;
import com.frameworkset.platform.cms.driver.publish.PubObjectReference;
import com.frameworkset.platform.cms.driver.publish.PublishException;
import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.RecursivePublishManager;
import com.frameworkset.platform.cms.util.AttributeKeys;
import com.frameworkset.platform.cms.util.CMSUtil;

/**
 * 递归发布对象
 * 执行递归发布的功能，对于递归发布的任务往往需要异步执行
 * <p>Title: RecursivePublishObject</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-6-18 11:07:24
 * @author biaoping.yin
 * @version 1.0
 */
public class RecursivePublishObject extends PublishObject implements java.io.Serializable {
	private RecursiveContext recursiveContext;
	private String siteid;
	/**
	 * 递归发布的信息列表
	 * List<PubObjectReference>
	 */
    protected  Set recursivePubObjects;
    public RecursivePublishObject(
    		 CMSRequestContext requestContext, 
    		 String siteid,
    		 Set recursivePubObjects,
			 String[] publisher,
			 boolean isRoot,
			  boolean[] local2ndRemote 	,
			  int[] distributeManners)
    {
    	super(requestContext, null,
				local2ndRemote 	,
				  distributeManners ,isRoot,publisher);
    	this.siteid = siteid;
    	this.recursivePubObjects = recursivePubObjects;
    	this.isRecursive = true;
    	
    }
    
    /**
     * 如果是子发布任务，则调用本构建器来发布相关的发布对象，往往需要异 步处理
     * @param requestcontext
     * @param recursivePubObjects 递归发布的信息
     * @param context
     */
    public RecursivePublishObject(CMSRequestContext requestcontext, Set recursivePubObjects, Context context) {
		super(requestcontext,context);
		this.recursivePubObjects = recursivePubObjects;
	}
    

	public void doPublish() throws PublishException {
		if(recursivePubObjects == null)
			return;
		Iterator it = recursivePubObjects.iterator();
		for(;it.hasNext();)
		{
			PubObjectReference ref = (PubObjectReference)it.next();
			String site = ref.getPubSite();
			String siteid = "";
			try {
				siteid = CMSUtil.getSiteCacheManager().getSiteByEname(site).getSiteId() + "";
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			/**
			 * 相关的站点发布
			 */
			if(ref.getPubobjectType() == RecursivePublishManager.PUBOBJECTTYPE_SITE)
			{
				
				PublishObject publishObject = new SitePublishObject(requestContext,
																	new String[] {ref.getPubobject()},
																	this.context,
																	new int[] {PublishObject.PUBLISH_SITE_INDEX});
				
				publishObject.setMaxPublishDepth(1);
				publishObject.setRecursive(true);

				context.getDriverConfiguration().getPublishEngine().publish(publishObject);
								
			}			
			else if(ref.getPubobjectType() == RecursivePublishManager.PUBOBJECTTYPE_CHANNEL)
			{
				PublishObject channelPublishObject = new ChannelPublishObject(requestContext,
															new String[]{siteid,ref.getPubobject()},
															this.context ,
															new int[] {PublishObject.PUBLISH_CHANNEL_INDEX});
				channelPublishObject.setMaxPublishDepth(1);
				channelPublishObject.setRecursive(true);
				context.getDriverConfiguration()
						.getPublishEngine()
						.publish(channelPublishObject);
			}
			
			else if(ref.getPubobjectType() == RecursivePublishManager.PUBOBJECTTYPE_CHANNELDOCUMENT)
			{
				PublishObject channelPublishObject = new ChannelPublishObject(this.requestContext,
																			  new String[]{siteid,
																				ref.getPubobject()},context,new int[] {PublishObject.PUBLISH_CHANNEL_CONTENT_PUBLISHED});
				channelPublishObject.setMaxPublishDepth(1);
				channelPublishObject.setRecursive(true);
				context.getDriverConfiguration()
						.getPublishEngine()
						.publish(channelPublishObject);
				
			}
			
			else if(ref.getPubobjectType() == RecursivePublishManager.PUBOBJECTTYPE_PAGE)
			{
				PublishObject pagePublishObject = new PagePublishObject(this.requestContext,
																			  new String[]{siteid,
																				ref.getPubobject()},context,new int[] {PublishObject.PUBLISH_CHANNEL_CONTENT});
				pagePublishObject.setMaxPublishDepth(1);
				pagePublishObject.setRecursive(true);
				context.getDriverConfiguration()
						.getPublishEngine()
						.publish(pagePublishObject);
				
			}
			else if(ref.getPubobjectType() == RecursivePublishManager.PUBOBJECTTYPE_DOCUMENT) //联动文档发布需要判断文档是否是已发布状态或者正在发布状态
			{
				try
				{
					String status = CMSUtil.getCMSDriverConfiguration().getCMSService().getDocumentManager().getDocStatus(Integer.parseInt(ref.getPubobject())) + ""; 
					
					if(status.equals(DocumentStatus.PUBLISHED.getStatus()))
					{
						PublishObject docPublishObject = new ContentPublishObject(this.requestContext,
								  new String[]{siteid,
									ref.getPubobject()},context);
						docPublishObject.setMaxPublishDepth(1);
						docPublishObject.setRecursive(true);
						context.getDriverConfiguration()
							.getPublishEngine()
							.publish(docPublishObject);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			//待扩展的其他相关的发布对象
			else if(ref.getPubobjectType() == RecursivePublishManager.PUBOBJECTTYPE_OTHER)
			{
//				PublishObject channelPublishObject = new ChannelPublishObject(this.requestContext,
//																			  new String[]{siteid,
//																				ref.getPubobject()},context,new int[] {PublishObject.PUBLISH_CHANNEL_CONTENT});
//				context.getDriverConfiguration()
//						.getPublishEngine()
//						.publish(channelPublishObject);
				
			}
			
			
			
		}
		
	}

	public Context buildContext() throws PublishException {
		if(this.parentContext == null)
		{
			if(this.siteid == null)
				siteid = this.requestContext.getRequest().getParameter(AttributeKeys.CMSSITEREQUESTKEY);			
			/**
			 * 构建频道的父上下文
			 */
			this.parentContext = new CMSContextImpl(siteid,publisher,this,null);
			parentContext.setEnableRecursive(this.enableRecursive());
			parentContext.setClearFileCache(this.isClearFileCache());
			recursiveContext = new RecursiveContextImpl(siteid,parentContext,this,monitor);
		}
		else
		{
			recursiveContext = new RecursiveContextImpl(parentContext.getSiteID(),this.parentContext,
					this,parentContext.getPublishMonitor().createSubPublishMonitor());
		}
		/**
		 * 设置递归发布的最大深度
		 */
		recursiveContext.setMaxPublishDepth(1);
		/**
		 * 设置当前发布对象的发布深度
		 */
		recursiveContext.setCurrentPublishDepth(0);		
		return recursiveContext ;		
	}
	
	

	public void initScriptlet() throws PublishException {
		
		
	}

	protected String getId() {		
		if(this.recursivePubObjects != null && this.recursivePubObjects.size() > 0)
		{
			StringBuffer buffer = new StringBuffer();
			Iterator it = recursivePubObjects.iterator();
			boolean flag = false;
			while(it.hasNext())
			{
				PubObjectReference ref = (PubObjectReference)it.next();
				if(!flag)
				{
					buffer.append(ref.toString());
					flag = true;
				}
				else
				{
					buffer.append("|").append(ref.toString());
				}
			}
			return buffer.toString();
				
		}
		
		return "";
	}

	

	public void recordRecursivePubObj(String refobj, int reftype, String site) {
		
		
	}

	public Set getRecursivePubObject() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void delteRefObjects() {
		
		
	}



}
