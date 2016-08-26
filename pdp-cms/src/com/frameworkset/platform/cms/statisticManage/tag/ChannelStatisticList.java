package com.frameworkset.platform.cms.statisticManage.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.statisticManage.entity.ChannelStatistic;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class ChannelStatisticList extends DataInfoImpl implements java.io.Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset, int maxPagesize) {
		
		DBUtil db = new DBUtil();
		DBUtil channeldb = new DBUtil();
		ListInfo listInfo = new ListInfo();
		
		//得到要查询的起止时间
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		//结束日期是没有时分秒的格式,能检索到结束日期当天的文档,weida
		if(endDate.length()<11)
		{
			endDate = endDate + " 23:59:59";
		}
		
		//根据起止时间查询用户的录稿篇数
//		String sql = "select channel_id,count(*) as writeDocs from td_cms_document where docwtime>="+
//		             db.getDBDate(startDate)+" and docwtime<="+db.getDBDate(endDate)+" and channel_id in ( "+
//		             "select channel_id from td_cms_channel ) group by channel_id " ;
		String sql = "select c.channel_id,c.name,c.site_id,d.writeDocs,t.name sitename from td_cms_channel c left join td_cms_site t on c.site_id = t.site_id inner join (select channel_id,count(*) as writeDocs "+
				"from td_cms_document "+  
				"where docwtime>="+DBUtil.getDBDate(startDate)+" and docwtime<="+DBUtil.getDBDate(endDate)+"  group by channel_id ) d "+
				"on c.channel_id= d.channel_id order by c.site_id";
		System.out.println(sql);
		
		try{
			db.executeSelect(sql,offset,maxPagesize);			
			List list = new ArrayList();
			ChannelStatistic channelStatistic ;			
			for(int i=0;i<db.size();i++){
				channelStatistic = new ChannelStatistic();
				//根据频道ID得到频道名称
//				channeldb.executeSelect("select name,site_id  from td_cms_channel where channel_id="+db.getInt(i,"channel_id"));
//				if(channeldb.size()>0){
//					channelStatistic.setChannelName(channeldb.getString(0,"name"));	
////					根据频道ID得到所属站点名称
//					sql="select name  from td_cms_site where site_id="+channeldb.getInt(0,"site_id");
//					channeldb.executeSelect(sql);
//					if(channeldb.size()>0){
//						channelStatistic.setSiteName(channeldb.getString(0,"name"));					
//					}
//					else{
//						channelStatistic.setSiteName("已经被删除");
//					}
//				}	
				channelStatistic.setChannelName(db.getString(i,"name"));	
//				sql="select name  from td_cms_site where site_id="+db.getInt(i,"site_id");
//				channeldb.executeSelect(sql);
				String sitename = db.getString(i,"sitename");
				if(sitename != null){
					channelStatistic.setSiteName(sitename);					
				}
				else{
					channelStatistic.setSiteName("已经被删除");
				}
				channelStatistic.setChannelId(db.getInt(i,"channel_id"));
				channelStatistic.setWriteNum(db.getInt(i,"writeDocs"));
				//根据频道ID统计录入总字数
				sql="select SUM(LENGTH(content)) totalwords from td_cms_document where channel_id="+db.getInt(i,"channel_id")+
				" and docwtime>="+DBUtil.getDBDate(startDate)+" and docwtime<="+DBUtil.getDBDate(endDate);
				channeldb.executeSelect(sql);
				//审稿数
				int auditNum=0;
				//发稿数
				int publishNum=0;
				//总字数
				int totalWords=0;
				
//					totalWords=totalWords+(int)channeldb.getClob(j,"content").length();
				
				totalWords = channeldb.getInt(0,"totalwords");
				//根据频道id获取已审稿数
				sql="select count(document_id) auditNum from td_cms_document where channel_id="+db.getInt(i,"channel_id")+
				" and docwtime>="+DBUtil.getDBDate(startDate)+" and docwtime<="+DBUtil.getDBDate(endDate) + " and status=3";
				channeldb.executeSelect(sql);
				
				//如果状态为已审
				
				auditNum = channeldb.getInt(0,0);
//				如果状态为已发,获取频道下的发稿数
				sql="select count(document_id) auditNum from td_cms_document where channel_id="+db.getInt(i,"channel_id")+
				" and docwtime>="+DBUtil.getDBDate(startDate)+" and docwtime<="+DBUtil.getDBDate(endDate) + " and status=5";
				channeldb.executeSelect(sql);
				
				
				publishNum = channeldb.getInt(0,0);
				
				
				channelStatistic.setAuditNum(auditNum);
				channelStatistic.setPublishNum(publishNum);
				channelStatistic.setTotalWords(totalWords);
				
				list.add(channelStatistic);
			}
			
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
		}catch(Exception e){
			e.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		DBUtil db = new DBUtil();
		DBUtil channeldb = new DBUtil();
		ListInfo listInfo = new ListInfo();
		
		//得到要查询的起止时间
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		//结束日期是没有时分秒的格式,能检索到结束日期当天的文档,weida
		if(endDate.length()<11)
		{
			endDate = endDate + " 23:59:59";
		}
		
		//根据起止时间查询用户的录稿篇数
//		String sql = "select channel_id,count(*) as writeDocs from td_cms_document where docwtime>="+
//		             db.getDBDate(startDate)+" and docwtime<="+db.getDBDate(endDate)+" and channel_id in ( "+
//		             "select channel_id from td_cms_channel ) group by channel_id " ;
		String sql = "select c.channel_id,c.name,c.site_id,d.writeDocs,t.name sitename from td_cms_channel c left join td_cms_site t on c.site_id = t.site_id inner join (select channel_id,count(*) as writeDocs "+
				"from td_cms_document "+  
				"where docwtime>="+DBUtil.getDBDate(startDate)+" and docwtime<="+DBUtil.getDBDate(endDate)+"  group by channel_id ) d "+
				"on c.channel_id= d.channel_id order by c.site_id";
		//System.out.println(sql);
		
		try{
			db.executeSelect(sql);			
			List list = new ArrayList();
			ChannelStatistic channelStatistic ;			
			for(int i=0;i<db.size();i++){
				channelStatistic = new ChannelStatistic();
				//根据频道ID得到频道名称
//				channeldb.executeSelect("select name,site_id  from td_cms_channel where channel_id="+db.getInt(i,"channel_id"));
//				if(channeldb.size()>0){
//					channelStatistic.setChannelName(channeldb.getString(0,"name"));	
////					根据频道ID得到所属站点名称
//					sql="select name  from td_cms_site where site_id="+channeldb.getInt(0,"site_id");
//					channeldb.executeSelect(sql);
//					if(channeldb.size()>0){
//						channelStatistic.setSiteName(channeldb.getString(0,"name"));					
//					}
//					else{
//						channelStatistic.setSiteName("已经被删除");
//					}
//				}	
				channelStatistic.setChannelName(db.getString(i,"name"));	
//				sql="select name  from td_cms_site where site_id="+db.getInt(i,"site_id");
//				channeldb.executeSelect(sql);
				String sitename = db.getString(i,"sitename");
				if(sitename != null){
					//channelStatistic.setSiteName(channeldb.getString(0,"sitename"));	
					//modify by ge.tao
					channelStatistic.setSiteName(sitename);
				}
				else{
					channelStatistic.setSiteName("已经被删除");
				}
				channelStatistic.setChannelId(db.getInt(i,"channel_id"));
				channelStatistic.setWriteNum(db.getInt(i,"writeDocs"));
				//根据频道ID统计录入总字数
				sql="select SUM(LENGTH(content)) totalwords from td_cms_document where channel_id="+db.getInt(i,"channel_id")+
				" and docwtime>="+DBUtil.getDBDate(startDate)+" and docwtime<="+DBUtil.getDBDate(endDate);
				channeldb.executeSelect(sql);
				//审稿数
				int auditNum=0;
				//发稿数
				int publishNum=0;
				//总字数
				int totalWords=0;
				
//					totalWords=totalWords+(int)channeldb.getClob(j,"content").length();
				
				totalWords = channeldb.getInt(0,"totalwords");
				//根据频道id获取已审稿数
				sql="select count(document_id) auditNum from td_cms_document where channel_id="+db.getInt(i,"channel_id")+
				" and docwtime>="+DBUtil.getDBDate(startDate)+" and docwtime<="+DBUtil.getDBDate(endDate) + " and status=3";
				channeldb.executeSelect(sql);
				
				//如果状态为已审
				
				auditNum = channeldb.getInt(0,0);
//				如果状态为已发,获取频道下的发稿数
				sql="select count(document_id) auditNum from td_cms_document where channel_id="+db.getInt(i,"channel_id")+
				" and docwtime>="+DBUtil.getDBDate(startDate)+" and docwtime<="+DBUtil.getDBDate(endDate) + " and status=5";
				channeldb.executeSelect(sql);
				
				
				publishNum = channeldb.getInt(0,0);
				
				
				channelStatistic.setAuditNum(auditNum);
				channelStatistic.setPublishNum(publishNum);
				channelStatistic.setTotalWords(totalWords);
				
				list.add(channelStatistic);
			}
			
			listInfo.setDatas(list);
//			listInfo.setTotalSize(db.getTotalSize());
		}catch(Exception e){
			e.printStackTrace();
		}
		return listInfo;
	}

}
