package com.frameworkset.platform.cms.statisticManage.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.statisticManage.entity.UserStatistic;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class HdUserStatisticList extends DataInfoImpl implements java.io.Serializable{

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
	    .append("select a.user_name,a.user_realname,a.user_id,a.user_isvalid,ROW_NUMBER() OVER ( ORDER   BY   a.user_regdate desc) aaaaaa,b.org_id from td_sm_user a left join td_sm_orguser b on a.user_id=b.user_id where user_isvalid=2 and user_type='0' "+strwhere);
		try{
			db.executeSelectForOracle(sqlstr.toString(),offset,maxPagesize,"aaaaaa");
			String roleName = "";
			List list = new ArrayList();
			UserStatistic userStatistic = null;
			if(db.size()>0)
			{
				for(int i=0;i<db.size();i++)
				{
					userStatistic = new UserStatistic();
					userStatistic.setUserName(db.getString(i,"user_name"));
					userStatistic.setUserRealName(db.getString(i,"user_realname"));
					userStatistic.setUserStatus(db.getInt(i,"user_isvalid"));
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
					userStatistic.setOrg_id(db.getString(i,"org_id"));
					list.add(userStatistic);
				}
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
		String strwhere = "";
		String user_name = request.getParameter("user_name");
		if(user_name!=null&&!user_name.equals(""))
		{
			strwhere = " and user_name='"+user_name+"'";
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
	    .append("select a.*,b.org_id from td_sm_user a left join td_sm_orguser b on a.user_id=b.user_id where user_isvalid=2 and user_type='0' "+strwhere);
		try{
			db.executeSelect(sqlstr.toString());
			String roleName = "";
			List list = new ArrayList();
			UserStatistic userStatistic = null;
			if(db.size()>0)
			{
				for(int i=0;i<db.size();i++)
				{
					userStatistic.setUserName(db.getString(i,"user_name"));
					userStatistic.setUserRealName(db.getString(i,"user_realname"));
					userStatistic.setUserStatus(db.getInt(i,"user_isvalid"));
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
					userStatistic.setOrg_id(db.getString(i,"org_id"));
					list.add(userStatistic);
				}
			}
			listInfo.setDatas(list);
//			listInfo.setTotalSize(db.getTotalSize());
		}catch(Exception e){
			e.printStackTrace();
		}
		return listInfo;
	}

}
