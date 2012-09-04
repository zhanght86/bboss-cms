package com.frameworkset.platform.cms.driver.context.impl;

import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.RecursiveContext;
import com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor;
import com.frameworkset.platform.cms.driver.publish.impl.RecursivePublishObject;

/**
 * 递归发布上下文
 * <p>Title: RecursiveContextImpl</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-6-18 11:43:46
 * @author biaoping.yin
 * @version 1.0
 */
public class RecursiveContextImpl extends BaseContextImpl implements RecursiveContext{
	
	public RecursiveContextImpl(String siteid,Context parentContext, RecursivePublishObject object, PublishMonitor monitor) {
		
		super(siteid,parentContext,object,monitor);
		this.setNeedRecordRefObject(false);
//		this.recursive = true;
	}
	
//	public String toString()
//	{
//		
//	}
	


}
