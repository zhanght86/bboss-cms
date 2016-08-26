package com.frameworkset.platform.cms.searchmanager;

import java.util.*;

import com.frameworkset.platform.cms.searchmanager.bean.CMSSearchIndex;

/**
 * 启动线程建立索引
 * 对数据库中td_cms_site_search表的没一个索引记录建立索引
 * @author Administrator
 *
 */
public class CMSSearchTask extends TimerTask implements java.io.Serializable {
	private String contextpath;
	public CMSSearchTask(String contextpath)
	{
		super();
		this.contextpath = contextpath;
		
	}
	public void run() {
		CMSSearchManager searchManager = new CMSSearchManager();
		try{
			Calendar calender = new GregorianCalendar();
			int curDayOfMonth = calender.get(Calendar.DAY_OF_MONTH);
			int curDayOfweek = calender.get(Calendar.DAY_OF_WEEK);
			int curHour = calender.get(Calendar.HOUR_OF_DAY); 
			 
			int cuerminite = calender.get(Calendar.MINUTE);
			if(cuerminite == 0){				//整点索引
				List indexList = searchManager.getIndexList();
				//遍历每一个索引，
				for(int i=0;i<indexList.size();i++){
					CMSSearchIndex index = (CMSSearchIndex)indexList.get(i);
					//System.out.println("索引：" + index.getId());
					
					int level = index.getLever();
					switch(level){
						case 0:			//每周一次
							if(index.getDay() == curDayOfweek && index.getTime() == curHour){
								new CMSCrawlerThread(index,contextpath).start();
							}
							break;
						case 1:			//每月一次
							if(index.getDay() == curDayOfMonth && index.getTime() == curHour){
								new CMSCrawlerThread(index,contextpath).start();
							}
							break;
						case 2:			//每天
							if(index.getTime() == curHour){
								new CMSCrawlerThread(index,contextpath).start();
							}
							break;					
					}					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
