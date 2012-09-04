package com.frameworkset.platform.cms.statisticManage.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.statisticManage.entity.UserStatistic;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class UserStatisticList extends DataInfoImpl implements java.io.Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset, int maxPagesize) {
		// TODO Auto-generated method stub
		DBUtil db = new DBUtil();
		DBUtil userdb = new DBUtil();
		ListInfo listInfo = new ListInfo();
		//得到要查询的起止时间
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		//结束日期是没有时分秒的格式,能检索到结束日期当天的文档,weida
		if(endDate.length()<11)
		{
			endDate = endDate + " 23:59:59";
		}
		String strwhere = "";
		String user_name = request.getParameter("user_name");
		if(user_name!=null&&!user_name.equals(""))
		{
			strwhere = " and user_name like '%"+user_name+"%'";
		}
		String sql = "";
		
		//根据起止时间查询已经采审发的用户
		//String sql = "select createuser,count(*) as writeDocs from td_cms_document where docwtime>="+
		//             db.getDBDate(startDate)+" and docwtime<="+db.getDBDate(endDate)+" and createuser in ( "+
		//             "select user_id from td_sm_user ) group by createuser";
//		StringBuffer sqlstr = new  StringBuffer()
//	    .append("select user_id,user_name,user_realname,ROW_NUMBER() OVER ( ORDER   BY   user_regdate desc) aaaaaa,user_isvalid from td_sm_user ")
//    	.append(" where user_id not in ")
//    	.append( "((select createuser as user_id  from td_cms_document where docwtime>=to_date('")
//    	.append(startDate).append(" 00:00:00','yyyy-mm-dd hh24:mi:ss') ")
////    	.append(" and docwtime<=to_date('"+endDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss' )")	weida
//    	.append(" and docwtime<=to_date('"+endDate+"','yyyy-mm-dd hh24:mi:ss' )")
//    	.append(  ") union ")
//    	.append( "(select user_id from tl_cms_doc_oper_log where oper_time>=to_date('")
//    	.append(startDate).append(" 00:00:00','yyyy-mm-dd hh24:mi:ss') ")
////    	.append(" and oper_time<=to_date('"+endDate+" 23:59:59','yyyy-mm-dd hh24:mi:ss' )")	weida
//    	.append(" and oper_time<=to_date('"+endDate+"','yyyy-mm-dd hh24:mi:ss' )")
//    	.append(" and (doc_oper_id=20 or doc_oper_id=5)))");
		//根据用户ID查询用户信息
		//String sql_user;
//		根据用户ID查询用户角色信息
		//String sql_userRole;
		//角色名
		//System.out.println("sqlstr--------------------------------"+sqlstr.toString());
		
		//没用采集的用户也要统计出来
		StringBuffer sqlstr = new  StringBuffer()
	    .append("select user_id,user_name,user_realname,ROW_NUMBER() OVER ( ORDER   BY   user_regdate desc) aaaaaa,user_isvalid from td_sm_user ")
	    .append(" where user_isvalid=2 and user_type='0' "+strwhere);
		String roleName="";
		try{
			db.executeSelectForOracle(sqlstr.toString(),offset,maxPagesize,"aaaaaa");
			
			List list = new ArrayList();
			UserStatistic userStatistic ;
			for(int i=0;i<db.size();i++){
				userStatistic = new UserStatistic();
				userStatistic.setUserId(db.getInt(i,"user_id"));
				userStatistic.setUserName(db.getString(i,"user_name"));
				userStatistic.setUserRealName(db.getString(i,"user_realname"));
				userStatistic.setUserStatus(db.getInt(i,"user_isvalid"));
//				根据用户ID查询用户角色信息
				
				List roles = SecurityDatabase.getRoleManager().getAllRoleList(db.getInt(i,"user_id") + "");
				roleName="";
				StringBuffer b = new StringBuffer();
				boolean flag = false;
				//可能用户有多个角色
				for(int j=0;roles != null && j<roles.size();j++){
					Role r = (Role)roles.get(j);
					if(flag)
						b.append(",").append(r.getRoleName());
					else
					{
						flag = true;
						b.append(r.getRoleName());
					}
					
				}				
				roleName = b.toString();
				userStatistic.setRole(roleName);
				//根据用户ID得到录稿篇数,录稿总字数
				sql = "select count(document_id) writedocs,SUM(LENGTH(content)) contentlength  from td_cms_document where createuser="+db.getInt(i,"user_id")+
				      " and docwtime>="+DBUtil.getDBDate(startDate)+" and docwtime<="+DBUtil.getDBDate(endDate);
				System.out.println(sql);
				userdb.executeSelect(sql);
				if(userdb.size()>0){
					userStatistic.setWriteDocs(userdb.getInt(0,"writedocs"));
					//统计录稿总字数
					int num=userdb.getInt(0,"contentlength");
//					for(int j=0;j<userdb.size();j++){						
//							num += userdb.getInt(j,"contentlength");
//					}
					userStatistic.setTotalWords(num);
				}				
				//统计审稿篇数
				sql = "select count(user_id) as auditDocs from tl_cms_doc_oper_log where user_id="+db.getInt(i,"user_id")+
				      " and doc_oper_id=5 and oper_time>="+DBUtil.getDBDate(startDate)+" and oper_time<="+DBUtil.getDBDate(endDate);
				userdb.executeSelect(sql);
				if(userdb.size()>0){
					userStatistic.setAuditDocs(userdb.getInt(0,"auditDocs"));
				}
				//统计发稿篇数
				sql = "select count(user_id) as publishDocs from tl_cms_doc_oper_log where user_id="+db.getInt(i,"user_id")+
					" and doc_oper_id=20 and oper_time>="+DBUtil.getDBDate(startDate)+" and oper_time<="+DBUtil.getDBDate(endDate);
				userdb.executeSelect(sql);
				if(userdb.size()>0){
					userStatistic.setPublishDocs(userdb.getInt(0,"publishDocs"));
				}				
				list.add(userStatistic);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
		}catch(Exception e){
			e.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
//		 TODO Auto-generated method stub
		DBUtil db = new DBUtil();
		DBUtil userdb = new DBUtil();
		ListInfo listInfo = new ListInfo();
		//得到要查询的起止时间
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		//结束日期是没有时分秒的格式,能检索到结束日期当天的文档,weida
		if(endDate.length()<11)
		{
			endDate = endDate + " 23:59:59";
		}
		
		String sql = "";
		
		//根据起止时间查询已经采审发的用户
		//String sql = "select createuser,count(*) as writeDocs from td_cms_document where docwtime>="+
		//             db.getDBDate(startDate)+" and docwtime<="+db.getDBDate(endDate)+" and createuser in ( "+
		//             "select user_id from td_sm_user ) group by createuser";
		StringBuffer sqlstr = new  StringBuffer()
		    .append("select user_id,user_name,user_realname,ROW_NUMBER() OVER ( ORDER   BY   user_regdate desc) aaaaaa,user_isvalid from td_sm_user ")
        	.append(" where user_isvalid=2");
		System.out.println(sqlstr.toString());
        	//.append( "(select createuser from td_cms_document where docwtime>=")
        //	.append(DBUtil.getDBDate(startDate))
        	//.append(" and docwtime<="+DBUtil.getDBDate(endDate)+") ")
        	//.append(  "or user_id in ")
        	//.append( "(select user_id from tl_cms_doc_oper_log where oper_time>=")
        	//.append(DBUtil.getDBDate(startDate))
        	//.append(" and oper_time<="+DBUtil.getDBDate(endDate))
        	//.append(" and (doc_oper_id=20 or doc_oper_id=5))");
		//根据用户ID查询用户信息
		//String sql_user;
//		根据用户ID查询用户角色信息
		//String sql_userRole;
		//角色名
		String roleName="";
		try{
			db.executeSelect(sqlstr.toString());
			
			List list = new ArrayList();
			UserStatistic userStatistic ;
			
			for(int i=0;i<db.size();i++){
				userStatistic = new UserStatistic();
				userStatistic.setUserId(db.getInt(i,"user_id"));
				userStatistic.setUserName(db.getString(i,"user_name"));
				userStatistic.setUserRealName(db.getString(i,"user_realname"));
				userStatistic.setUserStatus(db.getInt(i,"user_isvalid"));
//				根据用户ID查询用户角色信息
				
				List roles = SecurityDatabase.getRoleManager().getAllRoleList(db.getInt(i,"user_id") + "");
				roleName="";
				StringBuffer b = new StringBuffer();
				boolean flag = false;
				//可能用户有多个角色
				for(int j=0;roles != null && j<roles.size();j++){
					Role r = (Role)roles.get(j);
					if(flag)
						b.append(",").append(r.getRoleName());
					else
					{
						flag = true;
						b.append(r.getRoleName());
					}
					
				}				
				roleName = b.toString();
				userStatistic.setRole(roleName);
				//根据用户ID得到录稿篇数,录稿总字数
				sql = "select count(document_id) writedocs,SUM(LENGTH(content)) contentlength  from td_cms_document where createuser="+db.getInt(i,"user_id")+
				      " and docwtime>="+DBUtil.getDBDate(startDate)+" and docwtime<="+DBUtil.getDBDate(endDate);
				//System.out.println(sql);
				userdb.executeSelect(sql);
				if(userdb.size()>0){
					userStatistic.setWriteDocs(userdb.getInt(0,"writedocs"));
					//统计录稿总字数
					int num=userdb.getInt(0,"contentlength");
//					for(int j=0;j<userdb.size();j++){						
//							num += userdb.getInt(j,"contentlength");
//					}
					userStatistic.setTotalWords(num);
				}				
				//统计审稿篇数
				sql = "select count(user_id) as auditDocs from tl_cms_doc_oper_log where user_id="+db.getInt(i,"user_id")+
				      " and doc_oper_id=5 and oper_time>="+DBUtil.getDBDate(startDate)+" and oper_time<="+DBUtil.getDBDate(endDate);
				userdb.executeSelect(sql);
				if(userdb.size()>0){
					userStatistic.setAuditDocs(userdb.getInt(0,"auditDocs"));
				}
				//统计发稿篇数
				sql = "select count(user_id) as publishDocs from tl_cms_doc_oper_log where user_id="+db.getInt(i,"user_id")+
					" and doc_oper_id=20 and oper_time>="+DBUtil.getDBDate(startDate)+" and oper_time<="+DBUtil.getDBDate(endDate);
				userdb.executeSelect(sql);
				if(userdb.size()>0){
					userStatistic.setPublishDocs(userdb.getInt(0,"publishDocs"));
				}				
				list.add(userStatistic);
			}
			listInfo.setDatas(list);
//			listInfo.setTotalSize(db.getTotalSize());
		}catch(Exception e){
			e.printStackTrace();
		}
		return listInfo;
	}

}
