package com.frameworkset.platform.cms.usermanager.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;
/**
 * cms用户权限授予－－用户列表
 * @author zhuo.wang
 *用户状态，0：已删除，1：正申请，2：已注册，3：已停用
 */
public class UserList extends DataInfoImpl implements java.io.Serializable{

	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	            int maxPagesize) {
	    	
	        ListInfo listInfo = new ListInfo();
	        //当前用户有权限的所有机构
	        String orgids = getAllUsersByUserId();
	        //System.out.print(orgids + "///");
	        String userName = request.getParameter("userName");
	        String id = request.getParameter("id");
	        String userType = request.getParameter("userType");
	        if(userType==null||userType.equals("")){//默认为内部用户
	        	userType="0";
	        }
	        //System.out.println("userType="+userType);
	      
			DBUtil dbUtil = new DBUtil();
			DBUtil db = new DBUtil();
			
			
	
			try {
				if(id==null || id.equals("7"))
				{					
					StringBuffer hsql = new StringBuffer("select * from TD_SM_USER where user_type='"+userType).append("'");
					if(!this.accessControl.isAdmin())
					{
						hsql.append(" and user_id in (select user_id from td_sm_userjoborg where org_id in (" + orgids + ")) ");
					}
					hsql.append(" order by USER_REGDATE,user_id,DREDGE_TIME");
					dbUtil.executeSelect(hsql.toString(),(int)offset,maxPagesize);
					
				
					User user = null;
					List users = new ArrayList();
					for(int i = 0; i < dbUtil.size(); i ++)
					{
						user = new User();
						user.setUserId(new Integer(dbUtil.getInt(i,"user_id")));
						user.setUserName(dbUtil.getString(i,"user_name"));
						user.setUserRealname(dbUtil.getString(i,"user_realname"));
						user.setUserIsvalid(new Integer(dbUtil.getInt(i,"USER_ISVALID")));
						user.setDredgeTime(dbUtil.getString(i,"DREDGE_TIME"));
						//注册日期
						java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
	  					if(dbUtil.getDate(i,"USER_REGDATE")==null){
	  						user.setUser_Regdate("不详");
	  					}
	  					else
	  					{
	  						String regtime =  formatter.format(dbUtil.getDate(i,"USER_REGDATE"));
	  						user.setUser_Regdate(regtime);
	  					}
	  					//查询用户所属组织
	  					/*db.executeSelect("select org_name from td_sm_organization where org_id in "+
	  							          "( select org_id from td_sm_orguser where user_id="+dbUtil.getInt(i,"user_id")+")");
	  					*/
	  					//add by ge.tao
	  					String org = this.getOrgNameByUserId(dbUtil.getInt(i,"user_id"));
	  					user.setOrgs(org);
	  					
						users.add(user);
						
					}
					listInfo.setDatas(users);
				}
				else if(id.equals("6"))
				{					
					StringBuffer hsql = new StringBuffer("select * from TD_SM_USER where user_type='"+userType).append("'");
					if(!this.accessControl.isAdmin())
					{
						hsql.append(" and user_id in (select user_id from td_sm_userjoborg where org_id in (" + orgids + ")) ");
					}
					if (userName != null && userName.length() > 0) {
						hsql.append(" and user_Name  like '%" + userName + "%' ");
					}
					hsql.append(" order by USER_REGDATE,user_id,DREDGE_TIME");
					dbUtil.executeSelect(hsql.toString(),(int)offset,maxPagesize);
					
				
					User user = null;
					List users = new ArrayList();
					for(int i = 0; i < dbUtil.size(); i ++)
					{
						user = new User();
						user.setUserId(new Integer(dbUtil.getInt(i,"user_id")));
						user.setUserName(dbUtil.getString(i,"user_name"));
						user.setUserRealname(dbUtil.getString(i,"user_realname"));
						user.setUserIsvalid(new Integer(dbUtil.getInt(i,"USER_ISVALID")));
						user.setDredgeTime(dbUtil.getString(i,"DREDGE_TIME"));
						//注册日期
						java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
	  					if(dbUtil.getDate(i,"USER_REGDATE")==null){
	  						user.setUser_Regdate("不详");
	  					}
	  					else
	  					{
	  						String regtime =  formatter.format(dbUtil.getDate(i,"USER_REGDATE"));
	  						user.setUser_Regdate(regtime);
	  					}
	  					//查询用户所属组织
	  					/*db.executeSelect("select org_name from td_sm_organization where org_id in "+
	  							          "( select org_id from td_sm_orguser where user_id="+dbUtil.getInt(i,"user_id")+")");
	  					*/
	  					//add by ge.tao
	  					String org = this.getOrgNameByUserId(dbUtil.getInt(i,"user_id"));
	  					user.setOrgs(org);
	  					
						users.add(user);
						
					}
					listInfo.setDatas(users);
				}
				else if(id.equals("0"))
				{
					StringBuffer hsql = new StringBuffer("select * from TD_SM_USER where user_type='"+userType+"' and  USER_ISVALID=0 ");
					if(!this.accessControl.isAdmin())
					{
						hsql.append(" and user_id in (select user_id from td_sm_userjoborg where org_id in (" + orgids + ")) ");
					}
					hsql.append(" order by USER_REGDATE,user_id,DREDGE_TIME");
					//System.out.println("已删除：" + hsql.toString());
					dbUtil.executeSelect(hsql.toString(),(int)offset,maxPagesize);
					
					User user = null;
					List users = new ArrayList();
					for(int i = 0; i < dbUtil.size(); i ++)
					{
						user = new User();
						user.setUserId(new Integer(dbUtil.getInt(i,"user_id")));
						user.setUserName(dbUtil.getString(i,"user_name"));
						user.setUserRealname(dbUtil.getString(i,"user_realname"));
						user.setUserIsvalid(new Integer(dbUtil.getInt(i,"USER_ISVALID")));
						user.setDredgeTime(dbUtil.getString(i,"DREDGE_TIME"));
						//注册日期
						java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
	  					if(dbUtil.getDate(i,"USER_REGDATE")==null){
	  						user.setUser_Regdate("不详");
	  					}
	  					else
	  					{
	  						String regtime =  formatter.format(dbUtil.getDate(i,"USER_REGDATE"));
	  						user.setUser_Regdate(regtime);
	  					}
//	  					查询用户所属组织
	  					String org = this.getOrgNameByUserId(dbUtil.getInt(i,"user_id"));
	  					user.setOrgs(org);
	  					
						users.add(user);
						
					}
					listInfo.setDatas(users);
				}
				else if(id.equals("1"))
				{
					StringBuffer hsql = new StringBuffer("select * from TD_SM_USER where user_type='"+userType+"' and  USER_ISVALID=1 ");
					
					if(!this.accessControl.isAdmin())
					{
						hsql.append(" and user_id in (select user_id from td_sm_userjoborg where org_id in (" + orgids + ")) ");
					}
					hsql.append(" order by USER_REGDATE,user_id,DREDGE_TIME");
					//System.out.println("新申请：" + hsql.toString());
					dbUtil.executeSelect(hsql.toString(),(int)offset,maxPagesize);
					
					User user = null;
					List users = new ArrayList();
					for(int i = 0; i < dbUtil.size(); i ++)
					{
						user = new User();
						user.setUserId(new Integer(dbUtil.getInt(i,"user_id")));
						user.setUserName(dbUtil.getString(i,"user_name"));
						user.setUserRealname(dbUtil.getString(i,"user_realname"));
						user.setUserIsvalid(new Integer(dbUtil.getInt(i,"USER_ISVALID")));
						user.setDredgeTime(dbUtil.getString(i,"DREDGE_TIME"));
						//注册日期
						java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
	  					if(dbUtil.getDate(i,"USER_REGDATE")==null){
	  						user.setUser_Regdate("不详");
	  					}
	  					else
	  					{
	  						String regtime =  formatter.format(dbUtil.getDate(i,"USER_REGDATE"));
	  						user.setUser_Regdate(regtime);
	  					}
//	  					查询用户所属组织
//	  					db.executeSelect("select org_name from td_sm_organization where org_id in "+
//	  							          "( select org_id from td_sm_orguser where user_id="+dbUtil.getInt(i,"user_id")+")");
//	  					String org="";
//	  					if(db.size()<1){
//	  						org="不属于任何组织机构";
//	  					}
//	  					for(int j=0;j<db.size();j++){
//	  						org=org+db.getString(j,"org_name");
//	  					}
	  					String org = this.getOrgNameByUserId(dbUtil.getInt(i,"user_id"));
	  					user.setOrgs(org);
	  					
						users.add(user);
						
					}
					listInfo.setDatas(users);
				}
				else if(id.equals("2"))
				{
					StringBuffer hsql = new StringBuffer("select * from TD_SM_USER where user_type='"+userType+"' and  USER_ISVALID=2 ");
					
					if(!this.accessControl.isAdmin())
					{
						hsql.append(" and user_id in (select user_id from td_sm_userjoborg where org_id in (" + orgids + ")) ");
					}
					hsql.append(" order by USER_REGDATE,user_id,DREDGE_TIME");
					//System.out.println("已开通：" + hsql.toString());
					dbUtil.executeSelect(hsql.toString(),(int)offset,maxPagesize);
					
					User user = null;
					List users = new ArrayList();
					for(int i = 0; i < dbUtil.size(); i ++)
					{
						user = new User();
						user.setUserId(new Integer(dbUtil.getInt(i,"user_id")));
						user.setUserName(dbUtil.getString(i,"user_name"));
						user.setUserRealname(dbUtil.getString(i,"user_realname"));
						user.setUserIsvalid(new Integer(dbUtil.getInt(i,"USER_ISVALID")));
						user.setDredgeTime(dbUtil.getString(i,"DREDGE_TIME"));
						//注册日期
						java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
	  					if(dbUtil.getDate(i,"USER_REGDATE")==null){
	  						user.setUser_Regdate("不详");
	  					}
	  					else
	  					{
	  						String regtime =  formatter.format(dbUtil.getDate(i,"USER_REGDATE"));
	  						user.setUser_Regdate(regtime);
	  					}
//	  					查询用户所属组织
//	  					db.executeSelect("select org_name from td_sm_organization where org_id in "+
//	  							          "( select org_id from td_sm_orguser where user_id="+dbUtil.getInt(i,"user_id")+")");
//	  					String org="";
//	  					if(db.size()<1){
//	  						org="不属于任何组织机构";
//	  					}
//	  					for(int j=0;j<db.size();j++){
//	  						org=org+db.getString(j,"org_name");
//	  					}
	  					String org = this.getOrgNameByUserId(dbUtil.getInt(i,"user_id"));
	  					user.setOrgs(org);
	  					
						users.add(user);
						
					}
					listInfo.setDatas(users);
				}
				else if(id.equals("3"))
				{
					StringBuffer hsql = new StringBuffer("select * from TD_SM_USER where user_type='"+userType+"' and  USER_ISVALID=3 ");
					
					if(!this.accessControl.isAdmin())
					{
						hsql.append(" and user_id in (select user_id from td_sm_userjoborg where org_id in (" + orgids + ")) ");
					}
					hsql.append(" order by USER_REGDATE,user_id,DREDGE_TIME");
					//System.out.println("已停用：" + hsql.toString());
					dbUtil.executeSelect(hsql.toString(),(int)offset,maxPagesize);
					
					User user = null;
					List users = new ArrayList();
					for(int i = 0; i < dbUtil.size(); i ++)
					{
						user = new User();
						user.setUserId(new Integer(dbUtil.getInt(i,"user_id")));
						user.setUserName(dbUtil.getString(i,"user_name"));
						user.setUserRealname(dbUtil.getString(i,"user_realname"));
						user.setUserIsvalid(new Integer(dbUtil.getInt(i,"USER_ISVALID")));
						user.setDredgeTime(dbUtil.getString(i,"DREDGE_TIME"));
						//注册日期
						java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
	  					if(dbUtil.getDate(i,"USER_REGDATE")==null){
	  						user.setUser_Regdate("不详");
	  					}
	  					else
	  					{
	  						String regtime =  formatter.format(dbUtil.getDate(i,"USER_REGDATE"));
	  						user.setUser_Regdate(regtime);
	  					}
//	  					查询用户所属组织
//	  					db.executeSelect("select org_name from td_sm_organization where org_id in "+
//	  							          "( select org_id from td_sm_orguser where user_id="+dbUtil.getInt(i,"user_id")+")");
//	  					String org="";
//	  					if(db.size()<1){
//	  						org="不属于任何组织机构";
//	  					}
//	  					for(int j=0;j<db.size();j++){
//	  						org=org+db.getString(j,"org_name");
//	  					}
	  					String org = this.getOrgNameByUserId(dbUtil.getInt(i,"user_id"));
	  					user.setOrgs(org);
	  					
						users.add(user);
						
					}
					listInfo.setDatas(users);
				}
				listInfo.setTotalSize(dbUtil.getTotalSize());
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			return listInfo;
		}

		protected ListInfo getDataList(String arg0, boolean arg1) {
			return null;
		}
		
		private String getOrgNameByUserId(int userId){
			String org="不属于任何组织机构";
			DBUtil db = new DBUtil();
			String org_sql = "select getUserorgjobinfos("+userId+") as org_name from dual";
			try{
				db.executeSelect(org_sql);
				if(db.size()>0){
					for(int j=0;j<db.size();j++){
						//org=org+db.getString(j,"org_name");
						org = db.getString(j,"org_name");
						if(org.endsWith("、")){
							org = org.substring(0,org.length()-1);
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return org;
		}
		
		/**
		 * 获得用户所有有权限的机构的ids
		 * @param 
		 * @return
		 */
		private String getAllUsersByUserId()
		{
			String orgids = "";
			DBUtil db = new DBUtil();
			String org_sql = "select org_id from td_sm_organization";
			try
			{
				db.executeSelect(org_sql);
				if(db.size()>0)
				{
					for(int j=0;j<db.size();j++)
					{
						if(this.accessControl.checkPermission(db.getInt(j,"org_id") + "",
								AccessControl.WRITE_PERMISSION, AccessControl.ORGUNIT_RESOURCE))
							orgids += "'" + db.getInt(j,"org_id") + "',";
					} 
				}
				if(orgids.endsWith(","))
					orgids = orgids.substring(0,orgids.length() -1);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return orgids;
		}

}
