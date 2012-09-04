package com.frameworkset.platform.cms.driver.context.impl;

import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor;

public class BatchContextImpl extends CMSContextImpl {

	public BatchContextImpl(String siteID, String[] publisher, PublishObject publishObject, PublishMonitor monitor) {
		super(siteID, publisher, publishObject, monitor);
		this.setNeedRecordRefObject(false);
	}
}
