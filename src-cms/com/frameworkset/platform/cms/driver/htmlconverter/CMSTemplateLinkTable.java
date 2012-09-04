package com.frameworkset.platform.cms.driver.htmlconverter;

import java.util.Iterator;

import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;

/**
 * <p>
 * Title: CMSTemplateLinkTable.java
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
 * @Date 2012-7-17 上午11:27:15
 * @author biaoping.yin
 * @version 1.0.0
 */
public class CMSTemplateLinkTable  {
	private CmsLinkTable templateLinkTable ;
	public CMSTemplateLinkTable(CmsLinkTable templateLinkTable)
	{
		super();
		this.templateLinkTable = templateLinkTable;
	}
	
	public CMSLink addLink(CMSLink link,Context context) {
		if(link.isParserAndDistribute())
		{
			return templateLinkTable.addLink(link);
		}
		else
		{
			context.getPublishMonitor().addSuccessMessage("页面中带变量或者标签的地址，忽略记录分发:" + link.getOriginHref(),context.getPublisher());
			return link;
		}
	}

	public void destroy() {
		templateLinkTable.destroy();
		
	}

	public CmsLinkTable getTemplateLinkTable() {
		return templateLinkTable;
	}

	public Iterator iterator() {
		// TODO Auto-generated method stub
		return templateLinkTable.iterator();
	}

	public int size() {
		// TODO Auto-generated method stub
		return this.templateLinkTable.size();
	}

}
