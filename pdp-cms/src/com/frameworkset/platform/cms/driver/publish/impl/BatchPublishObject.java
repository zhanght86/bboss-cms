package com.frameworkset.platform.cms.driver.publish.impl;

import java.util.Set;

import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.impl.BatchContextImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestContext;
import com.frameworkset.platform.cms.driver.publish.PublishException;
import com.frameworkset.platform.cms.driver.publish.PublishObject;

/**
 * 批处理发布对象
  * <p>Title: BatchPublishObject</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-4-9 17:51:02
 * @author biaoping.yin
 * @version 1.0
 */ 
public class BatchPublishObject extends PublishObject {
	/**
	 * 批量发布对象二维数组
	 * 封装多个文档的站点，频道，和文档id信息
	 * 例如:{
	 * 			{siteid,channelid,docid},
	 * 			{siteid,channelid,docid},
	 * 			{siteid,channelid,docid},
	 *          ...
	 *     }
	 */
	protected String[][] batchObjects;
	protected int publishObjectType;
	public BatchPublishObject(CMSRequestContext requestContext, 
							 int[] publishScope,
							 String[][] batchObjects,
							 String[] publisher,
							 boolean isRoot,
							  boolean[] local2ndRemote 	,
							  int[] distributeManners ) {
		super(requestContext, publishScope,
				local2ndRemote 	,
				  distributeManners ,isRoot,publisher);
		this.batchObjects = batchObjects;
//		this.isRoot = isRoot;
	}

	public void doPublish() throws PublishException {
		if(this.publishObjectType == PublishObject.OBJECT_DOCUMENT)
		{
			for(int i = 0; i < this.batchObjects.length; i ++)
			{
				PublishObject contentPublishObject = new ContentPublishObject(this.requestContext,
						  new String[] {this.batchObjects[i][0],
										this.batchObjects[i][1],
										this.batchObjects[i][2]},
						  this.context);
				
				contentPublishObject.setMaxPublishDepth(this.maxPublishDepth);
				try {
					this.requestContext.getDriverConfiguration()
									   .getPublishEngine()
									   .publish(contentPublishObject);
					
					
				} catch (PublishException e) {
					e.printStackTrace();
				}
			}
		}
		else if(this.publishObjectType == PublishObject.OBJECT_CHANNEL)
		{
			for(int i = 0; i < this.batchObjects.length; i ++)
			{
				PublishObject contentPublishObject = new ChannelPublishObject(this.requestContext,
						  new String[] {this.batchObjects[i][0],
										this.batchObjects[i][1],
										this.batchObjects[i][2]},
						  this.context);
				contentPublishObject.setMaxPublishDepth(this.maxPublishDepth);
				
				try {
					this.requestContext.getDriverConfiguration()
									   .getPublishEngine()
									   .publish(contentPublishObject);
				} catch (PublishException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	public Context buildContext() throws PublishException {
		BatchContextImpl parentContext = new BatchContextImpl(batchObjects[0][0],
				publisher, this,monitor);
		parentContext.setEnableRecursive(this.enableRecursive());
		parentContext.setClearFileCache(this.isClearFileCache());
		return parentContext;		
	}

	public void initScriptlet() throws PublishException {
		
		
	}
	private String id;
	protected String getId() {
		if(id != null)
			return id;
		// TODO Auto-generated method stub
		StringBuffer ids = new StringBuffer();
		for(int i = 0; i < this.batchObjects.length; i ++)
		{
			
			String[] docinfo = this.batchObjects[i];
			if(i != 0)
				ids.append(",");
			ids.append("site-").append(docinfo[0]).append("channel-").append(docinfo[1]).append("document-").append(docinfo[2]);
		}
		id = ids.toString();
		return id;
	}
	
	/**
	 * 判断发布的文档是否时批处理发布
	 * @return
	 */
	public boolean isBatchPublish()
	{
		return true;
	}

	public int getPublishObjectType() {
		return publishObjectType;
	}

	public void setPublishObjectType(int publishObjectType) {
		this.publishObjectType = publishObjectType;
	}
	
	protected void publishPages()
	{
		
	}


	public void recordRecursivePubObj(String refobj, int reftype, String site) {
		
		
	}

	public Set getRecursivePubObject() {
		
		return null;
	}

	protected void delteRefObjects() {
		
		
	}



	

}
