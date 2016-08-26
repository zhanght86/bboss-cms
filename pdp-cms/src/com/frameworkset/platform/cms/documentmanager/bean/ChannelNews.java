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
package com.frameworkset.platform.cms.documentmanager.bean;

import java.util.List;

import com.frameworkset.platform.cms.documentmanager.Document;

/**
 * <p>Title: ChannelNews.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-10-9
 * @author biaoping.yin
 * @version 1.0
 */
public class ChannelNews {
	private List<Document> news;
	private String channelIndex;
	private String siteIndex;
	private String sitedomain;
	public List<Document> getNews() {
		return news;
	}
	public void setNews(List<Document> news) {
		this.news = news;
	}
	public String getChannelIndex() {
		return channelIndex;
	}
	public void setChannelIndex(String channelIndex) {
		this.channelIndex = channelIndex;
	}
	public String getSiteIndex() {
		return siteIndex;
	}
	public void setSiteIndex(String siteIndex) {
		this.siteIndex = siteIndex;
	}
	public String getSitedomain() {
		return sitedomain;
	}
	public void setSitedomain(String sitedomain) {
		this.sitedomain = sitedomain;
	}

}
