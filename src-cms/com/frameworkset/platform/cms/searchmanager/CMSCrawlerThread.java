package com.frameworkset.platform.cms.searchmanager;

import com.frameworkset.platform.cms.searchmanager.bean.CMSSearchIndex;


public class CMSCrawlerThread extends Thread implements java.io.Serializable {
	
	private CMSSearchIndex index;
	private String contextpath;
	public CMSCrawlerThread(CMSSearchIndex index,String contextpath) {
		super();
		this.index = index;
		this.contextpath = contextpath;
	}
	
	public void run(){
		try {
			//启动站内检索
			CMSSearchManager.startCrawler(index,contextpath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
