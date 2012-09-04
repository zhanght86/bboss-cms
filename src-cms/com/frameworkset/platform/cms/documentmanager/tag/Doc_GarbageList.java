package com.frameworkset.platform.cms.documentmanager.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class Doc_GarbageList extends DataInfoImpl implements java.io.Serializable
{

	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
             int maxPagesize) {
	  
	  		
			String title = request.getParameter("title");
			String doclevel = request.getParameter("doclevel");
			String status = request.getParameter("status");
			String user = request.getParameter("userid");
			String doctype = request.getParameter("doctype");
			String flag = request.getParameter("flag");
		 	String channelid = request.getParameter("channelId");
		 	request.setAttribute("channelId",channelid);
			String channelName = request.getParameter("channelName");
			request.setAttribute("channelName",channelName);
			String siteid = request.getParameter("siteid");
			request.setAttribute("siteid",siteid);
			
//			
//		 	System.out.println("recursion............"+channelid);
//			System.out.println("title............"+title);
//			System.out.println("doclevel............"+doclevel);
//			System.out.println("status............"+status);
//			System.out.println("doclevel............"+doclevel);
//			System.out.println("status............"+status);
//			System.out.println("flag............"+flag);
			
			ListInfo listInfo = new ListInfo();
			DBUtil dbUtil = new DBUtil();
			DBUtil db = new DBUtil();
			try {
				
				String sql="select * from  TD_CMS_DOCUMENT where channel_id ="+ channelid +" and  GARBAGE=1 ";
				if ((title == null && doclevel == null && status == null && user ==null && doctype == null)||flag.equals("2")){
				//	System.out.println("............"+sql);
					dbUtil.executeSelect(sql +"order by CREATETIME desc",(int)offset,maxPagesize);
					List list = new ArrayList();
					Document document;
					
					for (int i = 0; i < dbUtil.size(); i++) {
						document = new Document();
						document.setDocument_id(dbUtil.getInt(i,"document_id"));
						document.setTitle(dbUtil.getString(i,"title"));
						document.setSubtitle(dbUtil.getString(i,"SUBTITLE"));
						document.setStatus(dbUtil.getInt(i,"status"));
						db.executeSelect("select name from TB_CMS_DOC_STATUS where id="+ dbUtil.getInt(i,"status") +"");
						if(db.size()>0){
							document.setStatusname(db.getString(0,"name"));
						}else{
							document.setStatusname("状态不明");
						}
						document.setAuthor(dbUtil.getString(i,"AUTHOR"));
						document.setUser_id(dbUtil.getInt(i,"USER_ID"));
						document.setCreateTime(dbUtil.getDate(i,"CREATETIME"));
						document.setDoctype(dbUtil.getInt(i,"DOCTYPE"));
						document.setIslock(dbUtil.getInt(i,"ISLOCK"));
						String str="select USER_REALNAME from td_sm_user where user_id="+ dbUtil.getInt(i,"USER_ID") +"";
						db.executeSelect(str);
						if(db.size()>0){
							document.setUsername(db.getString(0,"USER_REALNAME"));
						}
						list.add(document);
					}
					listInfo.setDatas(list);
					listInfo.setTotalSize(dbUtil.getTotalSize());
					return listInfo;	
				}
				
				if (title != null && title.length() > 0) {
					sql = sql +" and title like '%"+ title +"%'";
				}
				if (doclevel != null && doclevel.length() > 0) {
					sql = sql +" and DOCLEVEL like '%"+ doclevel +"%'";
				}
				if (status != null && status.length() > 0) {
					sql = sql +" and status like '%"+ status +"%'";
				}
				if (user != null && user.length() > 0) {
				
					String struser ="select user_id from td_sm_user where USER_REALNAME='"+user+"'";
					db.executeSelect(struser);
					if(db.size()>0){
						sql = sql +" and user_id like '%"+ db.getInt(0,"user_id") +"%'";
					}
					
				}
				if (doctype != null && doctype.length() > 0) {
					sql = sql +" and doctype like '%"+ doctype +"%'";
				}
				//System.out.println("..."+sql);
				dbUtil.executeSelect(sql + "order by CREATETIME desc",(int)offset,maxPagesize);
				List list = new ArrayList();
				Document document;
				
				for (int i = 0; i < dbUtil.size(); i++) {
					document = new Document();
					document.setDocument_id(dbUtil.getInt(i,"document_id"));
					document.setTitle(dbUtil.getString(i,"title"));
					document.setSubtitle(dbUtil.getString(i,"SUBTITLE"));
					document.setStatus(dbUtil.getInt(i,"status"));
					db.executeSelect("select name from TB_CMS_DOC_STATUS where id="+ dbUtil.getInt(i,"status") +"");
					if(db.size()>0){
						document.setStatusname(db.getString(0,"name"));
					}else{
						document.setStatusname("状态不明");
					}
					document.setAuthor(dbUtil.getString(i,"AUTHOR"));
					document.setUser_id(dbUtil.getInt(i,"USER_ID"));
					document.setCreateTime(dbUtil.getDate(i,"CREATETIME"));
					document.setDoctype(dbUtil.getInt(i,"DOCTYPE"));
					document.setIslock(dbUtil.getInt(i,"ISLOCK"));
					String str="select USER_REALNAME from td_sm_user where user_id="+ dbUtil.getInt(i,"USER_ID") +"";
					db.executeSelect(str);
					if(db.size()>0){
						document.setUsername(db.getString(0,"USER_REALNAME"));
					}
					list.add(document);
				}
				listInfo.setDatas(list);
				listInfo.setTotalSize(dbUtil.getTotalSize());
      
       }catch(Exception e){
             e.printStackTrace();
       }
		return listInfo;	
 }	
      
      protected ListInfo getDataList(String arg0, boolean arg1) {
           
            return null;
      }
}