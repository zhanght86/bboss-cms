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
package com.frameworkset.platform.cms.driver.htmlconverter;

import java.util.HashMap;
import java.util.Map;

import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.LinkTimestamp;

/**
 * <p>Title: LinkCache.java</p>
 *
 * <p>Description: 站点模板资源发布缓存器</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-9-29
 * @author biaoping.yin
 * @version 1.0
 */
public class LinkCache {
	private Map<String,LinkTimestamp> templatelinkTimestamps = new HashMap<String,LinkTimestamp>();
	public LinkTimestamp cachTemplateLink(CMSLink link,long lastTimestamp)
	{
		String key = link.getRelativeFilePath();
		LinkTimestamp  linkTimestamp = this.templatelinkTimestamps.get(key);
		if(linkTimestamp != null)
		{
			return linkTimestamp;
		}
		synchronized(templatelinkTimestamps)
		{
			linkTimestamp = this.templatelinkTimestamps.get(key);
			if(linkTimestamp != null)
			{
				return linkTimestamp;
			}
			linkTimestamp = new LinkTimestamp(key,lastTimestamp);
			this.templatelinkTimestamps.put(key, linkTimestamp);
			linkTimestamp = null;
			
			
		}
		return linkTimestamp;
		
	}
	
	private Map<String,LinkTimestamp> contentlinkTimestamps = new HashMap<String,LinkTimestamp>();
	public LinkTimestamp cachContentLink(CMSLink link,long lastTimestamp)
	{
		String key = link.getRelativeFilePath();
		LinkTimestamp  linkTimestamp = this.contentlinkTimestamps.get(key);
		if(linkTimestamp != null)
		{
			return linkTimestamp;
		}
		synchronized(contentlinkTimestamps)
		{
			linkTimestamp = this.contentlinkTimestamps.get(key);
			if(linkTimestamp != null)
			{
				return linkTimestamp;
			}
			linkTimestamp = new LinkTimestamp(key,lastTimestamp);
			this.contentlinkTimestamps.put(key, linkTimestamp);
			linkTimestamp = null;
			
			
		}
		return linkTimestamp;
		
	}

}
