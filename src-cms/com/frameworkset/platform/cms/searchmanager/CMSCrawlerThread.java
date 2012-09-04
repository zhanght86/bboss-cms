package com.frameworkset.platform.cms.searchmanager;

import com.frameworkset.platform.cms.searchmanager.bean.CMSSearchIndex;


public class CMSCrawlerThread extends Thread implements java.io.Serializable {
	
	private CMSSearchIndex index;
	
	public CMSCrawlerThread(CMSSearchIndex index) {
		super();
		this.index = index;
	}
	
	public void run(){
		try {
			//启动站内检索
			CMSSearchManager.startCrawler(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
