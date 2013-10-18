package com.frameworkset.platform.cms.documentmanager.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLParams;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.cms.documentmanager.bean.CitedDocument;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.ListInfo;

public class CitedDocumentList extends DataInfoImpl implements java.io.Serializable {

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize) {
		String channelid = request.getParameter("channelid");
		
		String docName = request.getParameter("docName");
		String chnlName = request.getParameter("chnlName");
		
		String citeUserid = request.getParameter("citeUserid");
		String srcChnlid = request.getParameter("srcChnlid");
		String citeTimeBgin = request.getParameter("citeTimeBgin");
		String citeTimeEnd = request.getParameter("citeTimeEnd");
		String queryFlag = request.getParameter("queryFlag");
		
		ListInfo listInfo = new ListInfo();
		DocumentManagerImpl docManager = new DocumentManagerImpl();
		String sql;
		try{
			if(channelid !=null && !channelid.equals("")){
				
//				sql = "select a.* from td_cms_chnl_ref_doc a where a.chnl_id = " + channelid;
//				if("part".equals(queryFlag)){
//					sql = "select distinct a.* from td_cms_chnl_ref_doc a,td_cms_document b,td_cms_channel c " +
//							"where a.chnl_id = " + channelid;
//					if(docName != null && docName.length()>0)
//						sql += " and b.isdeleted!=1 and a.doc_id=b.document_id and b.subtitle like '%" + docName +"%' ";
//					if(chnlName != null && chnlName.length()>0)
//						sql += " and c.status = 0 and a.doc_id = c.channel_id and c.display_name like '%" + chnlName +"%'";
//					if(citeUserid != null && citeUserid.length()>0)
//						sql += " and a.op_user_id =" + citeUserid ;
//					if(srcChnlid != null && srcChnlid.length()>0)			//文档源频道查询
//						sql += " and b.isdeleted!=1 and a.doc_id=b.document_id and b.channel_id = " + srcChnlid;
//					if(citeTimeBgin != null && citeTimeBgin.length()>0)
//						sql += " and a.op_time >=to_date('"+citeTimeBgin+"','yyyy-mm-dd')";
//					if(citeTimeEnd != null && citeTimeEnd.length()>0)
//						sql += " and a.op_time < to_date('"+citeTimeEnd+"','yyyy-mm-dd')+1";
//				}
//				sql +=" order by a.op_time desc";
//				listInfo = docManager.getCitedDocList(sql,(int)offset,maxPagesize);
				listInfo = getCitedDocList( offset, maxPagesize);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return listInfo;
	}
	
	// 获取引用文档列表
		public ListInfo getCitedDocList( long offset, int maxItem) throws DocumentManagerException {
			ListInfo listInfo = new ListInfo();
			
//			DBUtil db1 = new DBUtil();
			
			String channelid = request.getParameter("channelid");
			
			String docName = request.getParameter("docName");
			String chnlName = request.getParameter("chnlName");
			
			String citeUserid = request.getParameter("citeUserid");
			String srcChnlid = request.getParameter("srcChnlid");
			String citeTimeBgin = request.getParameter("citeTimeBgin");
			String citeTimeEnd = request.getParameter("citeTimeEnd");
			String queryFlag = request.getParameter("queryFlag");
			
			
			DocumentManagerImpl docManager = new DocumentManagerImpl();
			try
			{
				String sql;
				
					if(channelid !=null && !channelid.equals("")){
						ConfigSQLExecutor executor = new ConfigSQLExecutor("com/frameworkset/platform/cms/documentmanager/document.xml");
						SQLParams params = new SQLParams();
						params.addSQLParam("queryFlag", queryFlag, SQLParams.STRING);
						params.addSQLParam("channelid", Integer.parseInt(channelid), SQLParams.INT);
						if("part".equals(queryFlag)){
							
						
							if(docName != null && docName.length()>0)
							{
								params.addSQLParam("docName", "%" + docName +"%", SQLParams.STRING);
//								sql += " and b.isdeleted!=1 and a.doc_id=b.document_id and b.subtitle like '%" + docName +"%' ";
							}
							if(chnlName != null && chnlName.length()>0)
							{
								params.addSQLParam("chnlName", "%" + chnlName +"%", SQLParams.STRING);
//								sql += " and c.status = 0 and a.doc_id = c.channel_id and c.display_name like '%" + chnlName +"%'";
							}
							if(citeUserid != null && citeUserid.length()>0)
							{
								params.addSQLParam("citeUserid", citeUserid, SQLParams.STRING);
//								sql += " and a.op_user_id =" + citeUserid ;
							}
							if(srcChnlid != null && srcChnlid.length()>0)			//文档源频道查询
							{
								params.addSQLParam("srcChnlid", Integer.parseInt(srcChnlid), SQLParams.INT);
//								sql += " and b.isdeleted!=1 and a.doc_id=b.document_id and b.channel_id = " + srcChnlid;
							}
							if(citeTimeBgin != null && citeTimeBgin.length()>0)
							{
								params.addSQLParam("citeTimeBgin", citeTimeBgin, SQLParams.STRING);
//								sql += " and a.op_time >=to_date('"+citeTimeBgin+"','yyyy-mm-dd')";
							}
							if(citeTimeEnd != null && citeTimeEnd.length()>0)
							{
								params.addSQLParam("citeTimeEnd", citeTimeEnd, SQLParams.STRING);
//								sql += " and a.op_time < to_date('"+citeTimeEnd+"','yyyy-mm-dd')+1";
							}
						}
						final List<CitedDocument> list = new ArrayList<CitedDocument>();
						listInfo = executor.queryListInfoBeanByNullRowHandler(new NullRowHandler(){

							@Override
							public void handleRow(Record db)
									throws Exception {
								CitedDocument citedDoc = new CitedDocument();
								int curChannelId = db.getInt( "chnl_id");
								int userId = db.getInt( "op_user_id");
								int citeType = db.getInt( "citetype");
								int docId = db.getInt( "doc_id"); // 此docId可能为文档id，也可能为频道id，由citeType决定
								int srcSiteId = db.getInt( "site_id");
								citedDoc.setUserId(userId);
								citedDoc.setCurChannelId(curChannelId);
								citedDoc.setDocid(docId);
								citedDoc.setCiteTime(db.getDate( "op_time"));
								citedDoc.setCiteType(citeType);
								citedDoc.setSrcSiteId(srcSiteId);
								list.add(citedDoc);
								
								
							}
							
						}, "getCitedDocList", offset, maxItem, params);
						listInfo.setDatas(list);
//						sql = "select a.* from td_cms_chnl_ref_doc a where a.chnl_id = " + channelid;
//						if("part".equals(queryFlag)){
//							sql = "select distinct a.* from td_cms_chnl_ref_doc a,td_cms_document b,td_cms_channel c " +
//									"where a.chnl_id = " + channelid;
//							if(docName != null && docName.length()>0)
//								sql += " and b.isdeleted!=1 and a.doc_id=b.document_id and b.subtitle like '%" + docName +"%' ";
//							if(chnlName != null && chnlName.length()>0)
//								sql += " and c.status = 0 and a.doc_id = c.channel_id and c.display_name like '%" + chnlName +"%'";
//							if(citeUserid != null && citeUserid.length()>0)
//								sql += " and a.op_user_id =" + citeUserid ;
//							if(srcChnlid != null && srcChnlid.length()>0)			//文档源频道查询
//								sql += " and b.isdeleted!=1 and a.doc_id=b.document_id and b.channel_id = " + srcChnlid;
//							if(citeTimeBgin != null && citeTimeBgin.length()>0)
//								sql += " and a.op_time >=to_date('"+citeTimeBgin+"','yyyy-mm-dd')";
//							if(citeTimeEnd != null && citeTimeEnd.length()>0)
//								sql += " and a.op_time < to_date('"+citeTimeEnd+"','yyyy-mm-dd')+1";
//						}
//						sql +=" order by a.op_time desc";
					
					for (int i = 0; i < list.size(); i++) {
						CitedDocument citedDoc = list.get(i);
						int curChannelId = citedDoc.getCurChannelid();
						int userId = citedDoc.getUserId();
						int citeType = citedDoc.getCiteType();
						int docId = citedDoc.getDocid(); // 此docId可能为文档id，也可能为频道id，由citeType决定
						int srcSiteId = citedDoc.getSrcSiteId();
						
//						String sqltemp = "select name from td_cms_site where site_id = " + srcSiteId;
//						db1.executeSelect(sqltemp);
//						if (db1.size() > 0)
							citedDoc.setSrcSiteName(CMSUtil.getSiteCacheManager().getSite(srcSiteId+"").getName());
	
						if (citeType == 0) { // 引用文档
							String sql1 = "select a.subtitle as docName,b.display_name as srcChannelName,"
									+ "c.display_name as curChannelName,d.user_name as citeUserName,e.name as statusName "
									+ "from td_cms_document a,td_cms_channel b,td_cms_channel c,"
									+ "td_sm_user d,tb_cms_doc_status e " + "where a.isdeleted=0 and a.document_id = ?"
									+ " and a.channel_id = b.channel_id and b.status=0 " + "and c.channel_id = ?"
									+ " and d.user_id = ? and e.id = a.status";
							PreparedDBUtil db1 = new PreparedDBUtil(); 
							db1.preparedSelect(sql1);
							db1.setInt(1, docId);
							db1.setInt(2, curChannelId);
							db1.setInt(3, userId);
							db1.executePrepared();
							if (db1.size() > 0) 
							{
								citedDoc.setDocName(db1.getString(0, "docName"));
								citedDoc.setSrcChannelName(db1.getString(0, "srcChannelName"));
								citedDoc.setCiteUserName(db1.getString(0, "citeUserName"));
								citedDoc.setCurChannelName(db1.getString(0, "curChannelName"));
								citedDoc.setStatusName(db1.getString(0, "statusName"));
//								list.add(citedDoc);
							}
						} else { // 引用频道
							String sql1 = "select a.display_name as srcChannelName,b.user_name as citeUserName,"
									+ "c.display_name as curChannelName "
									+ "from td_cms_channel a,td_sm_user b,td_cms_channel c " + "where a.channel_id = ?" 
									+ " and a.status =0 and b.user_id = ? and c.channel_id = ?" ;
							PreparedDBUtil db1 = new PreparedDBUtil(); 
							db1.preparedSelect(sql1);
							db1.setInt(1, docId);
							db1.setInt(2, userId);
							db1.setInt(3, curChannelId);
							db1.executePrepared();
							if (db1.size() > 0) {
								citedDoc.setDocName(db1.getString(0, "srcChannelName"));
								citedDoc.setSrcChannelName(db1.getString(0, "srcChannelName"));
								citedDoc.setCiteUserName(db1.getString(0, "citeUserName"));
								citedDoc.setCurChannelName(db1.getString(0, "curChannelName"));
								citedDoc.setStatusName("");
//								list.add(citedDoc);
							}
						}
	
					}
					
				
			}
			return listInfo;
		}
		catch(Exception e)
		{
			throw new DocumentManagerException("",e);
		}
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}