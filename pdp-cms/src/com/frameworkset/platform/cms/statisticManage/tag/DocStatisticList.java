package com.frameworkset.platform.cms.statisticManage.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.statisticManage.entity.DocStatistic;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 文档统计接口
 * <p>Title: DocStatisticList</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-8-14 17:09:31
 * @author biaoping.yin
 * @version 1.0
 */
public class DocStatisticList extends DataInfoImpl implements java.io.Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset, int maxPagesize) {
				
		// TODO Auto-generated method stub
		DBUtil db = new DBUtil();
		
		ListInfo listInfo = new ListInfo();
		StringBuffer sql = new StringBuffer();
		//新稿文档数
		int newNum=0;
		//已经发稿文档数
		int pubNum=0;
		//其他文档数
		int otherNum=0;
		
//		得到要查询的起止时间
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		//结束日期是没有时分秒的格式,能检索到结束日期当天的文档,weida
		if(endDate.length()<11)
		{
			endDate = endDate + " 23:59:59";
		}
		
		//得到要查询的用户ID
		String userId=request.getParameter("userId");
		//flag=1表示查询录稿数，flag=2表示查询审稿数，flag=3表示查询发稿数
		String flag=request.getParameter("flag");
		//得到频道ID
		String channelId=request.getParameter("channelId");
		
//	--------------第1种情况----------------------------根据频道ID来查询文档---------------------------------------------
		if(channelId!=null && !channelId.equals("")){
//			ROW_NUMBER() OVER ( ORDER   BY   cjrq ) rownum
			sql.append("select t.document_id,t.title,t.docsource_id,t.author,LENGTH(t.content) contentlength,t.channel_id,t.status,t.createuser,docwtime,")
			   .append("ROW_NUMBER() OVER ( ORDER   BY   docwtime desc) aaaaaa,")
			   .append(	"b.name statusname,c.srcname srcname,d.display_name display_name,e.user_name user_name ")
			   .append("from td_cms_document t left join TB_CMS_DOC_STATUS b on t.status = b.id left join td_cms_docsource c on t.docsource_id = c.docsource_id ")
			   .append(" left join td_cms_channel d on t.channel_id = d.channel_id ")
			   .append(" left join td_sm_user e on e.user_id = t.createuser ")
			   .append("where t.docwtime>=")
			   .append(DBUtil.getDBDate(startDate))
			   .append(" and t.docwtime<=")
			   .append(DBUtil.getDBDate(endDate))
			   .append(" and t.channel_id=")
			   .append(channelId);
			if("2".equals(flag)){//查询状态为审核的文档
				sql.append(" and t.status=3");
			}
			else if("3".equals(flag)){//查询状态为发布的文档
				sql.append(" and t.status=5");
			}
			try{
				//System.out.println(sql);
				db.executeSelectForOracle(sql.toString(),(int)offset,maxPagesize,"aaaaaa");
				List list = new ArrayList();
				DocStatistic docStatistic ;
				for(int i=0;i<db.size();i++){
					docStatistic = new DocStatistic();
					docStatistic.setDocumentId(db.getInt(i,"document_id"));
					docStatistic.setChannelId(db.getInt(i,"channel_id"));
					docStatistic.setTitle(db.getString(i,"title"));
					docStatistic.setAuthor(db.getString(i,"author"));
					docStatistic.setWordsNum(db.getInt(i,"contentlength"));
                    //设置撰写时间
					docStatistic.setWriteTime(db.getDate(i,"docwtime"));
//					根据状态得到状态名称
//					sql="select name from TB_CMS_DOC_STATUS where id="+ db.getInt(i,"status");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
//					if(db.getString(i))
						docStatistic.setStatus(db.getString(i,"statusname"));
//					}
//					根据来源ID查询来源名字
//					sql="select srcname from td_cms_docsource where docsource_id="+db.getInt(i,"docsource_id");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
						docStatistic.setSourceName(db.getString(i,"srcname"));
//					}
//					根据频道ID查询频道名字
//					sql="select name from td_cms_channel where channel_id="+db.getInt(i,"channel_id");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
						docStatistic.setChannelName(db.getString(i,"display_name"));
//					}
//					根据createuser得到createuser的姓名
//					db3.executeSelect("select user_name from td_sm_user where user_id="+db.getInt(i,"createuser"));
//					if(db3.size()>0){
					String user_name = db.getString(i,"user_name");
					if(user_name != null){
						docStatistic.setCreateuserName(db.getString(i,"user_name"));
					}
					else{
						docStatistic.setCreateuserName("已经删除");
					}
					list.add(docStatistic);
				}
				listInfo.setDatas(list);
				listInfo.setTotalSize(db.getTotalSize());
				
			}catch(Exception e){
				e.printStackTrace();
			}
			return listInfo;
			
		}		
	
		sql.setLength(0);
//		------------第2种情况--------------根据用户ID来查询审发文档----------------------------------------------------------
		if(flag!=null && !"1".equals(flag) && userId!=null && !userId.equals("")){
			
			sql.append("select distinct t.document_id,t.title,t.docsource_id,t.author,LENGTH(t.content) contentlength,t.channel_id,t.status,t.createuser,t.docwtime, ")
			.append("ROW_NUMBER() OVER ( ORDER   BY   docwtime desc) aaaaaa,")
			.append("b.name statusname,c.srcname srcname,d.display_name display_name,e.user_name user_name ")   
			.append("from td_cms_document t left join TB_CMS_DOC_STATUS b on t.status = b.id left join td_cms_docsource c on t.docsource_id = c.docsource_id ")
			   .append(" left join td_cms_channel d on t.channel_id = d.channel_id ")
			   .append(" left join td_sm_user e on e.user_id = t.createuser").append(" where t.document_id in (select doc_id from tl_cms_doc_oper_log where user_id=")
			   .append(userId)
			   .append(" and  oper_time>=").append(DBUtil.getDBDate(startDate))
			   .append(" and oper_time<=").append(DBUtil.getDBDate(endDate));
			if("2".equals(flag)){//表示统计审稿数
				sql.append(" and doc_oper_id=5");
			}
			else if("3".equals(flag)){//表示统计发稿数
				sql.append(" and doc_oper_id=20");
			}
			sql.append(")");
			//查询数据库
			try{
				
				db.executeSelectForOracle(sql.toString(),(int)offset,maxPagesize,"aaaaaa");
				List list = new ArrayList();
				DocStatistic docStatistic ;			
				for(int i=0;i<db.size();i++){
					docStatistic = new DocStatistic();
					docStatistic.setDocumentId(db.getInt(i,"document_id"));
					//根据文档ID查询文档信息					
					docStatistic.setTitle(db.getString(i,"title"));
					docStatistic.setChannelId(db.getInt(i,"channel_id"));
					docStatistic.setAuthor(db.getString(i,"author"));
					docStatistic.setWordsNum(db.getInt(i,"contentlength"));
                     //设置撰写时间
					docStatistic.setWriteTime(db.getDate(i,"docwtime"));
					//根据状态得到状态名称
//					sql="select name from TB_CMS_DOC_STATUS where id="+ db.getInt(0,"status");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
						docStatistic.setStatus(db.getString(i,"statusname"));
//					}
//						根据来源ID查询来源名字
//					sql="select srcname from td_cms_docsource where docsource_id="+db_src.getInt(0,"docsource_id");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
						docStatistic.setSourceName(db.getString(i,"srcname"));
//					}
//						根据频道ID查询频道名字
//					sql="select name from td_cms_channel where channel_id="+db_src.getInt(0,"channel_id");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
						docStatistic.setChannelName(db.getString(i,"display_name"));
//					}
//						根据createuser得到createuser的姓名
//					db3.executeSelect("select user_name from td_sm_user where user_id="+db_src.getInt(0,"createuser"));
					String user_name = db.getString(i,"user_name");
					if(user_name != null){
						docStatistic.setCreateuserName(db.getString(i,"user_name"));
					}else{
						docStatistic.setCreateuserName("已经删除");
					}
					list.add(docStatistic);
										
				}
				listInfo.setDatas(list);
				listInfo.setTotalSize(db.getTotalSize());
			}catch(Exception e){
				e.printStackTrace();
			}			
			return listInfo;
		}		
		
		//-----------第3种情况---------------------------------查询所有的或某一用户录入的文档------------------------------------------------------
		
		 
		try{
			
			//根据状态获取文档发稿数的sql语句
			String temp = "select count(t.document_id) from td_cms_document t where docwtime>="
				+DBUtil.getDBDate(startDate) + " and docwtime<=" + DBUtil.getDBDate(endDate);
			
			if(userId!=null && !userId.equals("")){
				temp += " and createuser=" + userId;
			}
			
			
			
//			根据状态统计新稿数和已发数
//			新稿
			db.executeSelect(temp+" and status=1");
			newNum = db.getInt(0,0);
//			已发
			db.executeSelect(temp+" and status=5");
			pubNum = db.getInt(0,0);
			
			
			sql.setLength(0);	
			sql.append("select t.document_id,t.title,t.docsource_id,t.author,LENGTH(t.content) contentlength,t.channel_id,t.status,t.createuser,t.docwtime,")
			.append("ROW_NUMBER() OVER ( ORDER   BY   docwtime desc) aaaaaa,")
			.append("b.name statusname,c.srcname srcname,d.display_name display_name,e.user_name user_name ")  
			.append(" from td_cms_document t left join TB_CMS_DOC_STATUS b on t.status = b.id left join td_cms_docsource c on t.docsource_id = c.docsource_id ")
			   .append(" left join td_cms_channel d on t.channel_id = d.channel_id ")
			   .append(" left join td_sm_user e on e.user_id = t.createuser").append(" where t.docwtime>=")
			   .append(DBUtil.getDBDate(startDate)).append(" and t.docwtime<=")
			   .append(DBUtil.getDBDate(endDate));
			if(userId!=null && !userId.equals("")){
				sql.append(" and t.createuser=").append(userId);
			}
			db.executeSelectForOracle(sql.toString(),(int)offset,maxPagesize,"aaaaaa");
			List list = new ArrayList();
			DocStatistic docStatistic ;
			
			
			for(int i=0;i<db.size();i++){
				docStatistic = new DocStatistic();
				docStatistic.setDocumentId(db.getInt(i,"document_id"));
				docStatistic.setChannelId(db.getInt(i,"channel_id"));
				docStatistic.setTitle(db.getString(i,"title"));
				//根据来源ID查询来源名字
//				sql="select srcname from td_cms_docsource where docsource_id="+db.getInt(i,"docsource_id");
//				db_src.executeSelect(sql);
//				if(db_src.size()>0){
					docStatistic.setSourceName(db.getString(i,"srcname"));
//				}
//				根据频道ID查询频道名字
//				sql="select name from td_cms_channel where channel_id="+db.getInt(i,"channel_id");
//				db_src.executeSelect(sql);
//				if(db_src.size()>0){
					docStatistic.setChannelName(db.getString(i,"display_name"));
//				}
				
				//根据状态得到状态名称				
//				sql="select name from TB_CMS_DOC_STATUS where id="+ db.getInt(i,"status");
//				db_src.executeSelect(sql);
				String statusname = db.getString(i,"statusname");
				if(statusname != null){
					docStatistic.setStatus(db.getString(i,"statusname"));
				}
				else{
					docStatistic.setStatus("状态名不存在");
				}
				////设置撰写时间
				docStatistic.setWriteTime(db.getDate(i,"docwtime"));
//			设置作者
				docStatistic.setAuthor(db.getString(i,"author"));
				//根据createuser得到createuser的姓名
//				db_src.executeSelect("select user_name from td_sm_user where user_id="+db.getInt(i,"createuser"));
				String user_name = db.getString(i,"user_name");
				if(user_name != null){
					docStatistic.setCreateuserName(db.getString(i,"user_name"));
				}else{
					docStatistic.setCreateuserName("已经删除");
				}
				//统计字数
				docStatistic.setWordsNum(db.getInt(i,"contentlength"));
				list.add(docStatistic);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
			//把新稿数和已发数保存到request中
			request.setAttribute("newNum",new Integer(newNum));
			request.setAttribute("pubNum",new Integer(pubNum));
			request.setAttribute("otherNum",new Integer(db.getTotalSize()-pubNum-newNum));
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		
//		DBUtil db = new DBUtil();
//		DBUtil db_src = new DBUtil();
//		DBUtil db3 = new DBUtil();
//		ListInfo listInfo = new ListInfo();
//		String sql="";
////		新稿文档数
//		int newNum=0;
//		//已经发稿文档数
//		int pubNum=0;
//		//其他文档数
//		int otherNum=0;
////		得到要查询的起止时间
//		String startDate = request.getParameter("startDate");
//		String endDate = request.getParameter("endDate");
//		//得到要查询的用户ID
//		String userId=request.getParameter("userId");
//		//flag=1表示查询录稿数，flag=2表示查询审稿数，flag=3表示查询发稿数
//		String flag=request.getParameter("flag");
//		//得到频道ID
//		String channelId=request.getParameter("channelId");
//		
////	--------------第1种情况----------------------------根据频道ID来查询文档---------------------------------------------
//		if(channelId!=null && !channelId.equals("")){
//			
//			sql="select document_id,title,docsource_id,author,content,channel_id,status,createuser,docwtime "+
//				"from td_cms_document "+
//				"where docwtime>="+db.getDBDate(startDate)+" and docwtime<="+db.getDBDate(endDate)+" and channel_id="+channelId;
//			if("2".equals(flag)){//查询状态为审核的文档
//				sql=sql+" and status=3";
//			}
//			else if("3".equals(flag)){//查询状态为发布的文档
//				sql=sql+" and status=5";
//			}
//			try{
//				//System.out.println(sql);
//				db.executeSelect(sql);
//				List list = new ArrayList();
//				DocStatistic docStatistic ;
//				for(int i=0;i<db.size();i++){
//					docStatistic = new DocStatistic();
//					docStatistic.setDocumentId(db.getInt(i,"document_id"));
//					docStatistic.setChannelId(db.getInt(i,"channel_id"));
//					docStatistic.setTitle(db.getString(i,"title"));
//					docStatistic.setAuthor(db.getString(i,"author"));
//					docStatistic.setWordsNum((int)db.getClob(i,"content").length());
//                    //设置撰写时间
//					docStatistic.setWriteTime(db.getDate(i,"docwtime").toString());
////					根据状态得到状态名称
//					sql="select name from TB_CMS_DOC_STATUS where id="+ db.getInt(i,"status");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
//						docStatistic.setStatus(db3.getString(0,"name"));
//					}
////					根据来源ID查询来源名字
//					sql="select srcname from td_cms_docsource where docsource_id="+db.getInt(i,"docsource_id");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
//						docStatistic.setSourceName(db3.getString(0,"srcname"));
//					}
////					根据频道ID查询频道名字
//					sql="select name from td_cms_channel where channel_id="+db.getInt(i,"channel_id");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
//						docStatistic.setChannelName(db3.getString(0,"name"));
//					}
////					根据createuser得到createuser的姓名
//					db3.executeSelect("select user_name from td_sm_user where user_id="+db.getInt(i,"createuser"));
//					if(db3.size()>0){
//						docStatistic.setCreateuserName(db3.getString(0,"user_name"));
//					}else{
//						docStatistic.setCreateuserName("已经删除");
//					}
//					list.add(docStatistic);
//				}
//				listInfo.setDatas(list);
//				//listInfo.setTotalSize(db.getTotalSize());
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			return listInfo;
//			
//		}		
//		
//		
////		------------第2种情况--------------根据用户ID来查询审发文档----------------------------------------------------------
//		if(flag!=null && !"1".equals(flag) && userId!=null && !userId.equals("")){			
//			sql="select doc_id from tl_cms_doc_oper_log where user_id="+userId+" and  oper_time>="+db.getDBDate(startDate)+" and oper_time<="+db.getDBDate(endDate);
//			if("2".equals(flag)){//表示统计审稿数
//				sql=sql+" and doc_oper_id=5";
//			}
//			else if("3".equals(flag)){//表示统计发稿数
//				sql=sql+" and doc_oper_id=20";
//			}
//			//查询数据库
//			try{
//				db.executeSelect(sql);
//				List list = new ArrayList();
//				DocStatistic docStatistic ;			
//				for(int i=0;i<db.size();i++){
//					docStatistic = new DocStatistic();
//					docStatistic.setDocumentId(db.getInt(i,"doc_id"));
//					//根据文档ID查询文档信息
//					sql = "select title,docsource_id,author,content,channel_id,status,createuser,docwtime "+
//					"from td_cms_document "+"where document_id="+db.getInt(i,"doc_id");
//					db_src.executeSelect(sql);
//					if(db_src.size()>0){
//						docStatistic.setTitle(db_src.getString(0,"title"));
//						docStatistic.setChannelId(db_src.getInt(0,"channel_id"));
//						docStatistic.setAuthor(db_src.getString(0,"author"));
//						docStatistic.setWordsNum((int)db_src.getClob(0,"content").length());
//                         //设置撰写时间
//						docStatistic.setWriteTime(db_src.getDate(0,"docwtime").toString());
//						//根据状态得到状态名称
//						sql="select name from TB_CMS_DOC_STATUS where id="+ db_src.getInt(0,"status");
//						db3.executeSelect(sql);
//						if(db3.size()>0){
//							docStatistic.setStatus(db3.getString(0,"name"));
//						}
////						根据来源ID查询来源名字
//						sql="select srcname from td_cms_docsource where docsource_id="+db_src.getInt(0,"docsource_id");
//						db3.executeSelect(sql);
//						if(db3.size()>0){
//							docStatistic.setSourceName(db3.getString(0,"srcname"));
//						}
////						根据频道ID查询频道名字
//						sql="select name from td_cms_channel where channel_id="+db_src.getInt(0,"channel_id");
//						db3.executeSelect(sql);
//						if(db3.size()>0){
//							docStatistic.setChannelName(db3.getString(0,"name"));
//						}
////						根据createuser得到createuser的姓名
//						db3.executeSelect("select user_name from td_sm_user where user_id="+db_src.getInt(0,"createuser"));
//						if(db3.size()>0){
//							docStatistic.setCreateuserName(db3.getString(0,"user_name"));
//						}else{
//							docStatistic.setCreateuserName("已经删除");
//						}
//						list.add(docStatistic);
//					}					
//				}
//				listInfo.setDatas(list);
//				//listInfo.setTotalSize(db.getTotalSize());
//			}catch(Exception e){
//				e.printStackTrace();
//			}			
//			return listInfo;
//		}		
//		
//		//-----------第3种情况---------------------------------查询所有的或某一用户录入的文档------------------------------------------------------
//		
//		 sql = "select document_id,title,docsource_id,author,content,channel_id,status,createuser,docwtime from td_cms_document where docwtime>="+db.getDBDate(startDate)+" and docwtime<="+db.getDBDate(endDate);
//		
//		
//		if(userId!=null && !userId.equals("")){
//			sql=sql+" and createuser="+userId;
//		}
//		try{
//			db.executeSelect(sql);
//			List list = new ArrayList();
//			DocStatistic docStatistic ;
//			
//			for(int i=0;i<db.size();i++){
//				docStatistic = new DocStatistic();
//				docStatistic.setDocumentId(db.getInt(i,"document_id"));
//				docStatistic.setChannelId(db.getInt(i,"channel_id"));
//				docStatistic.setTitle(db.getString(i,"title"));
//				//根据来源ID查询来源名字
//				sql="select srcname from td_cms_docsource where docsource_id="+db.getInt(i,"docsource_id");
//				db_src.executeSelect(sql);
//				if(db_src.size()>0){
//					docStatistic.setSourceName(db_src.getString(0,"srcname"));
//				}
////				根据频道ID查询频道名字
//				sql="select name from td_cms_channel where channel_id="+db.getInt(i,"channel_id");
//				db_src.executeSelect(sql);
//				if(db_src.size()>0){
//					docStatistic.setChannelName(db_src.getString(0,"name"));
//				}
////				根据状态统计新稿数和已发数
////				新稿
//				if(db.getInt(i,"status")==1){
//					newNum++;
//				}
//				
////				已发
//				if(db.getInt(i,"status")==5){
//					pubNum++;
//				}
//				
//				//根据状态得到状态名称
//				sql="select name from TB_CMS_DOC_STATUS where id="+ db.getInt(i,"status");
//				db_src.executeSelect(sql);
//				if(db_src.size()>0){
//					docStatistic.setStatus(db_src.getString(0,"name"));
//				}
//				else{
//					docStatistic.setStatus("状态名不存在");
//				}
//				////设置撰写时间
//				docStatistic.setWriteTime(db.getDate(i,"docwtime").toString());
////			设置作者
//				docStatistic.setAuthor(db.getString(i,"author"));
//				//根据createuser得到createuser的姓名
//				db_src.executeSelect("select user_name from td_sm_user where user_id="+db.getInt(i,"createuser"));
//				if(db_src.size()>0){
//					docStatistic.setCreateuserName(db_src.getString(0,"user_name"));
//				}else{
//					docStatistic.setCreateuserName("已经删除");
//				}
//				//统计字数
//				docStatistic.setWordsNum((int)db.getClob(i,"content").length());
//				list.add(docStatistic);
//			}
//			listInfo.setDatas(list);
//			//listInfo.setTotalSize(db.getTotalSize());
////			把新稿数和已发数保存到request中
//			request.setAttribute("newNum",new Integer(newNum));
//			request.setAttribute("pubNum",new Integer(pubNum));
//			request.setAttribute("otherNum",new Integer(db.size()-pubNum-newNum));
////			System.out.println("yyflist");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return listInfo;
		
		// TODO Auto-generated method stub
		DBUtil db = new DBUtil();
		
		ListInfo listInfo = new ListInfo();
		StringBuffer sql = new StringBuffer();
		//新稿文档数
		int newNum=0;
		//已经发稿文档数
		int pubNum=0;
		//其他文档数
		int otherNum=0;
		
//		得到要查询的起止时间
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		//结束日期是没有时分秒的格式,能检索到结束日期当天的文档,weida
		if(endDate.length()<11)
		{
			endDate = endDate + " 23:59:59";
		}
		
		//得到要查询的用户ID
		String userId=request.getParameter("userId");
		//flag=1表示查询录稿数，flag=2表示查询审稿数，flag=3表示查询发稿数
		String flag=request.getParameter("flag");
		//得到频道ID
		String channelId=request.getParameter("channelId");
		
//	--------------第1种情况----------------------------根据频道ID来查询文档---------------------------------------------
		if(channelId!=null && !channelId.equals("")){
//			ROW_NUMBER() OVER ( ORDER   BY   cjrq ) rownum
			sql.append("select t.document_id,t.title,t.docsource_id,t.author,LENGTH(t.content) contentlength,t.channel_id,t.status,t.createuser,docwtime,")
			   
			   .append(	"b.name statusname,c.srcname srcname,d.display_name display_name,e.user_name user_name ")
			   .append("from td_cms_document t left join TB_CMS_DOC_STATUS b on t.status = b.id left join td_cms_docsource c on t.docsource_id = c.docsource_id ")
			   .append(" left join td_cms_channel d on t.channel_id = d.channel_id ")
			   .append(" left join td_sm_user e on e.user_id = t.createuser ")
			   .append("where t.docwtime>=")
			   .append(DBUtil.getDBDate(startDate))
			   .append(" and t.docwtime<=")
			   .append(DBUtil.getDBDate(endDate))
			   .append(" and t.channel_id=")
			   .append(channelId);
			if("2".equals(flag)){//查询状态为审核的文档
				sql.append(" and t.status=3");
			}
			else if("3".equals(flag)){//查询状态为发布的文档
				sql.append(" and t.status=5");
			}
			try{
				//System.out.println(sql);
				db.executeSelect(sql.toString());
				List list = new ArrayList();
				DocStatistic docStatistic ;
				for(int i=0;i<db.size();i++){
					docStatistic = new DocStatistic();
					docStatistic.setDocumentId(db.getInt(i,"document_id"));
					docStatistic.setChannelId(db.getInt(i,"channel_id"));
					docStatistic.setTitle(db.getString(i,"title"));
					docStatistic.setAuthor(db.getString(i,"author"));
					docStatistic.setWordsNum(db.getInt(i,"contentlength"));
                    //设置撰写时间
					docStatistic.setWriteTime(db.getDate(i,"docwtime"));
//					根据状态得到状态名称
//					sql="select name from TB_CMS_DOC_STATUS where id="+ db.getInt(i,"status");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
//					if(db.getString(i))
						docStatistic.setStatus(db.getString(i,"statusname"));
//					}
//					根据来源ID查询来源名字
//					sql="select srcname from td_cms_docsource where docsource_id="+db.getInt(i,"docsource_id");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
						docStatistic.setSourceName(db.getString(i,"srcname"));
//					}
//					根据频道ID查询频道名字
//					sql="select name from td_cms_channel where channel_id="+db.getInt(i,"channel_id");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
						docStatistic.setChannelName(db.getString(i,"display_name"));
//					}
//					根据createuser得到createuser的姓名
//					db3.executeSelect("select user_name from td_sm_user where user_id="+db.getInt(i,"createuser"));
//					if(db3.size()>0){
					String user_name = db.getString(i,"user_name");
					if(user_name != null){
						docStatistic.setCreateuserName(db.getString(i,"user_name"));
					}
					else{
						docStatistic.setCreateuserName("已经删除");
					}
					list.add(docStatistic);
				}
				listInfo.setDatas(list);
//				listInfo.setTotalSize(db.getTotalSize());
				
			}catch(Exception e){
				e.printStackTrace();
			}
			return listInfo;
			
		}		
	
		sql.setLength(0);
//		------------第2种情况--------------根据用户ID来查询审发文档----------------------------------------------------------
		if(flag!=null && !"1".equals(flag) && userId!=null && !userId.equals("")){
			
			sql.append("select t.document_id,t.title,t.docsource_id,t.author,LENGTH(t.content) contentlength,t.channel_id,t.status,t.createuser,t.docwtime, ")
			
			.append("b.name statusname,c.srcname srcname,d.display_name display_name,e.user_name user_name ")   
			.append("from td_cms_document t left join TB_CMS_DOC_STATUS b on t.status = b.id left join td_cms_docsource c on t.docsource_id = c.docsource_id ")
			   .append(" left join td_cms_channel d on t.channel_id = d.channel_id ")
			   .append(" left join td_sm_user e on e.user_id = t.createuser").append(" where t.document_id in (select doc_id from tl_cms_doc_oper_log where user_id=")
			   .append(userId)
			   .append(" and  oper_time>=").append(DBUtil.getDBDate(startDate))
			   .append(" and oper_time<=").append(DBUtil.getDBDate(endDate));
			if("2".equals(flag)){//表示统计审稿数
				sql.append(" and doc_oper_id=5");
			}
			else if("3".equals(flag)){//表示统计发稿数
				sql.append(" and doc_oper_id=20");
			}
			sql.append(")");
			//查询数据库
			try{
				
				db.executeSelect(sql.toString());
				List list = new ArrayList();
				DocStatistic docStatistic ;			
				for(int i=0;i<db.size();i++){
					docStatistic = new DocStatistic();
					docStatistic.setDocumentId(db.getInt(i,"document_id"));
					//根据文档ID查询文档信息					
					docStatistic.setTitle(db.getString(i,"title"));
					docStatistic.setChannelId(db.getInt(i,"channel_id"));
					docStatistic.setAuthor(db.getString(i,"author"));
					docStatistic.setWordsNum(db.getInt(i,"contentlength"));
                     //设置撰写时间
					docStatistic.setWriteTime(db.getDate(i,"docwtime"));
					//根据状态得到状态名称
//					sql="select name from TB_CMS_DOC_STATUS where id="+ db.getInt(0,"status");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
						docStatistic.setStatus(db.getString(i,"statusname"));
//					}
//						根据来源ID查询来源名字
//					sql="select srcname from td_cms_docsource where docsource_id="+db_src.getInt(0,"docsource_id");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
						docStatistic.setSourceName(db.getString(i,"srcname"));
//					}
//						根据频道ID查询频道名字
//					sql="select name from td_cms_channel where channel_id="+db_src.getInt(0,"channel_id");
//					db3.executeSelect(sql);
//					if(db3.size()>0){
						docStatistic.setChannelName(db.getString(i,"display_name"));
//					}
//						根据createuser得到createuser的姓名
//					db3.executeSelect("select user_name from td_sm_user where user_id="+db_src.getInt(0,"createuser"));
					String user_name = db.getString(i,"user_name");
					if(user_name != null){
						docStatistic.setCreateuserName(db.getString(i,"user_name"));
					}else{
						docStatistic.setCreateuserName("已经删除");
					}
					list.add(docStatistic);
										
				}
				listInfo.setDatas(list);
//				listInfo.setTotalSize(db.getTotalSize());
			}catch(Exception e){
				e.printStackTrace();
			}			
			return listInfo;
		}		
		
		//-----------第3种情况---------------------------------查询所有的或某一用户录入的文档------------------------------------------------------
		
		 
		try{
			
			//根据状态获取文档发稿数的sql语句
			String temp = "select count(t.document_id) from td_cms_document t where docwtime>="
				+DBUtil.getDBDate(startDate) + " and docwtime<=" + DBUtil.getDBDate(endDate);
			
			if(userId!=null && !userId.equals("")){
				temp += " and createuser=" + userId;
			}
			
			
			
//			根据状态统计新稿数和已发数
//			新稿
			db.executeSelect(temp+" and status=1");
			newNum = db.getInt(0,0);
//			已发
			db.executeSelect(temp+" and status=5");
			pubNum = db.getInt(0,0);
			
			
			sql.setLength(0);	
			sql.append("select t.document_id,t.title,t.docsource_id,t.author,LENGTH(t.content) contentlength,t.channel_id,t.status,t.createuser,t.docwtime,")
			
			.append("b.name statusname,c.srcname srcname,d.display_name display_name,e.user_name user_name ")  
			.append(" from td_cms_document t left join TB_CMS_DOC_STATUS b on t.status = b.id left join td_cms_docsource c on t.docsource_id = c.docsource_id ")
			   .append(" left join td_cms_channel d on t.channel_id = d.channel_id ")
			   .append(" left join td_sm_user e on e.user_id = t.createuser").append(" where t.docwtime>=")
			   .append(DBUtil.getDBDate(startDate)).append(" and t.docwtime<=")
			   .append(DBUtil.getDBDate(endDate));
			if(userId!=null && !userId.equals("")){
				sql.append(" and t.createuser=").append(userId);
			}
			db.executeSelect(sql.toString());
			List list = new ArrayList();
			DocStatistic docStatistic ;
			
			
			for(int i=0;i<db.size();i++){
				docStatistic = new DocStatistic();
				docStatistic.setDocumentId(db.getInt(i,"document_id"));
				docStatistic.setChannelId(db.getInt(i,"channel_id"));
				docStatistic.setTitle(db.getString(i,"title"));
				//根据来源ID查询来源名字
//				sql="select srcname from td_cms_docsource where docsource_id="+db.getInt(i,"docsource_id");
//				db_src.executeSelect(sql);
//				if(db_src.size()>0){
					docStatistic.setSourceName(db.getString(i,"srcname"));
//				}
//				根据频道ID查询频道名字
//				sql="select name from td_cms_channel where channel_id="+db.getInt(i,"channel_id");
//				db_src.executeSelect(sql);
//				if(db_src.size()>0){
					docStatistic.setChannelName(db.getString(i,"display_name"));
//				}
				
				//根据状态得到状态名称				
//				sql="select name from TB_CMS_DOC_STATUS where id="+ db.getInt(i,"status");
//				db_src.executeSelect(sql);
				String statusname = db.getString(i,"statusname");
				if(statusname != null){
					docStatistic.setStatus(db.getString(i,"statusname"));
				}
				else{
					docStatistic.setStatus("状态名不存在");
				}
				////设置撰写时间
				docStatistic.setWriteTime(db.getDate(i,"docwtime"));
//			设置作者
				docStatistic.setAuthor(db.getString(i,"author"));
				//根据createuser得到createuser的姓名
//				db_src.executeSelect("select user_name from td_sm_user where user_id="+db.getInt(i,"createuser"));
				String user_name = db.getString(i,"user_name");
				if(user_name != null){
					docStatistic.setCreateuserName(db.getString(i,"user_name"));
				}else{
					docStatistic.setCreateuserName("已经删除");
				}
				//统计字数
				docStatistic.setWordsNum(db.getInt(i,"contentlength"));
				list.add(docStatistic);
			}
			listInfo.setDatas(list);
//			listInfo.setTotalSize(db.getTotalSize());
			//把新稿数和已发数保存到request中
			request.setAttribute("newNum",new Integer(newNum));
			request.setAttribute("pubNum",new Integer(pubNum));
			request.setAttribute("otherNum",new Integer(db.size()-pubNum-newNum));
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return listInfo;
	}

}
