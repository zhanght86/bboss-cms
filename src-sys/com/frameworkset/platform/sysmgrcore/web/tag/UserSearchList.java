package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.frameworkset.persitent.util.SQLUtil;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.FunctionDB;
import com.frameworkset.platform.sysmgrcore.web.struts.form.UserInfoForm;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author feng.jing
 * @version 1.0
 */
public class UserSearchList extends DataInfoImpl implements Serializable{
   
    private static final Logger log = Logger.getLogger(UserSearchList.class);
    
    public static SQLUtil sqlUtilInsert = SQLUtil.getInstance("org/frameworkset/insert.xml");

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
        	return sortUser1(request,"0", offset,
        			 maxPagesize);
        	
        	

	}
	
//	public void sortUser(String parent_id,List userList) {
//    	String isRecursive = request.getParameter("isRecursive");
//    	String userName = request.getParameter("userName");		
//		String userRealname = request.getParameter("userRealname");
//		String advQuery = (String) request.getAttribute("advQuery");		
//		
//  		DBUtil db_org = new DBUtil();  		
//  		try {
//  			// 取出直接下级机构，按jorg_sn,orgnumber排序
//  			db_org.executeSelect("select org_id from TD_SM_ORGANIZATION where parent_id='"
//  				+ parent_id + "' order by org_sn,orgnumber");
//
//  			for (int i = 0; i < db_org.size(); i++) {
//  				//
//  				String org_id = db_org.getString(i, "org_id");
//  				DBUtil db_user = new DBUtil();
//  				StringBuffer sb_user = new StringBuffer();
//  				sb_user.append("select b.user_id, b.user_name, b.user_realname, b.USER_HOMETEL,");
//  				sb_user.append("b.USER_WORKTEL, b.USER_MOBILETEL1, ");
//  				sb_user.append("getUserorgjobinfos(b.user_id || '') as org_job, ");
//  				sb_user.append("b.USER_MOBILETEL2, b.USER_EMAIL from v_user_one_org_one_job a  ");
//  				sb_user.append("inner join td_sm_user b on a.user_id = b.user_id ");
//  				sb_user.append("where org_id ='" + org_id + "' ");
//  				//添加查询条件
//  				if(advQuery == null){
//  					advQuery = request.getParameter("advQuery");
//  				}
//  				advQuery = advQuery==null?"":advQuery;
//  				HttpSession session = request.getSession();
//  				UserInfoForm userInfoForm = (UserInfoForm) session.getAttribute("advUserform");
//  				if(userInfoForm == null){
//  					userInfoForm = (UserInfoForm) request.getAttribute("advUserform");
//  				}
//  				if(userInfoForm == null){
//  					userInfoForm = new UserInfoForm();
//  				}
//  				if (advQuery.equals("1")) {
//					if(userInfoForm.getUserName() != null && userInfoForm.getUserName().length()>0){
//						sb_user.append(" and b.user_name like '%" + userInfoForm.getUserName().trim()+ "%' ");
//					}
//					if(userInfoForm.getUserRealname() != null && userInfoForm.getUserRealname().length()>0){
//						sb_user.append(" and b.user_realname like '%" + userInfoForm.getUserRealname().trim()+ "%'");
//					}
//					if(userInfoForm.getUserWorktel() != null && userInfoForm.getUserWorktel().length()>0){
//						sb_user.append(" and b.USER_WORKTEL like '%" + userInfoForm.getUserWorktel().trim()+ "%' ");
//					}
//					if(userInfoForm.getUserSex() != null && userInfoForm.getUserSex().length()>0 && (!userInfoForm.getUserSex().equals("-100"))){
//						sb_user.append(" and b.USER_SEX = '" + userInfoForm.getUserSex().trim()+ "' ");
//					}
//					if(userInfoForm.getHomePhone() != null && userInfoForm.getHomePhone().length()>0){
//						sb_user.append(" and b.USER_HOMETEL like '%" + userInfoForm.getHomePhone().trim()+ "%' ");
//					}
//					if(userInfoForm.getMail() != null && userInfoForm.getMail().length()>0){
//						sb_user.append(" and b.USER_EMAIL like '%" + userInfoForm.getMail().trim()+ "%' ");
//					}
//					if(userInfoForm.getMobile() != null && userInfoForm.getMobile().length()>0){
//						sb_user.append(" and b.USER_MOBILETEL1 like '%" + userInfoForm.getMobile().trim()+ "%' ");
//					}
//					if(userInfoForm.getUserPinyin() != null && userInfoForm.getUserPinyin().length()>0){
//						sb_user.append(" and b.USER_PINYIN like '%" + userInfoForm.getUserPinyin().trim()+ "%' ");
//					}
//					if(userInfoForm.getUserMobiletel2() != null && userInfoForm.getUserMobiletel2().length()>0){
//						sb_user.append(" and b.USER_MOBILETEL2 like '%" + userInfoForm.getUserMobiletel2().trim()+ "%' ");
//					}
//					if(userInfoForm.getUserType() != null && userInfoForm.getUserType().length()>0 && (!userInfoForm.getUserType().equals("-100"))){
//						sb_user.append(" and b.USER_TYPE = '" + userInfoForm.getUserType().trim()+ "' ");
//					}
//					if(userInfoForm.getPostalCode() != null && userInfoForm.getPostalCode().length()>0){
//						sb_user.append(" and b.USER_POSTALCODE like '%" + userInfoForm.getPostalCode().trim()+ "%' ");
//					}
//					if(userInfoForm.getUserFax() != null && userInfoForm.getUserFax().length()>0){
//						sb_user.append(" and b.USER_FAX like '%" + userInfoForm.getUserFax().trim()+ "%' ");
//					}
//					if(userInfoForm.getUserOicq() != null && userInfoForm.getUserOicq().length()>0){
//						sb_user.append(" and b.USER_OICQ  like '%" + userInfoForm.getUserOicq().trim()+ "%' ");
//					}
//					
//					if(userInfoForm.getUserBirthday() != null && userInfoForm.getUserBirthday().length()>0){
//						sb_user.append(" and b.USER_BIRTHDAY = to_date('" + userInfoForm.getUserBirthday().trim()+"','YYYY-MM-DD')");
//					}
//					
//					if(userInfoForm.getUserAddress() != null && userInfoForm.getUserAddress().length()>0){
//						sb_user.append(" and b.USER_ADDRESS like '%" + userInfoForm.getUserAddress().trim()+ "%' ");
//					}
//					
//					if(userInfoForm.getUserLogincount() > 0){
//						sb_user.append(" and b.USER_LOGINCOUNT = " + userInfoForm.getUserLogincount());							
//					}
//					
//					if(userInfoForm.getUserIsvalid()!= -100)
//						sb_user.append(" and b.USER_ISVALID = " + userInfoForm.getUserIsvalid());
//					
//					if(userInfoForm.getUserRegdate() != null && userInfoForm.getUserRegdate().length()>0){
//						sb_user.append("and b.USER_REGDATE = to_date('" + userInfoForm.getUserRegdate()+"','YYYY-MM-DD')");
//					}
//					
//					if(userInfoForm.getShortMobile() != null && userInfoForm.getShortMobile().length()>0){
//						sb_user.append(" and b.REMARK2 like '%" + userInfoForm.getShortMobile().trim()+ "%' ");
//					}
//  				} 
//  				//一般查询
//  				else {
//  					if (userName != null && userName != "") {
//  						sb_user.append("and b.user_name like '%" + userName.trim() + "%' ");
//  					}
//  					if (userRealname != null && userRealname != "") {
//  						sb_user.append("and b.user_realname like '%"+ userRealname.trim() +"%'");
//  					}
//  				}
//  				
//  				sb_user.append("order by job_sn,SAME_JOB_USER_SN");
//
//  				// 取出一个机构的用户，按job_sn,SAME_JOB_USER_SN排序
//  				//log.warn("sql---------userSearchList.sql-----------"+sb_user.toString());
//  				db_user.executeSelect(sb_user.toString());
//  				//
//  				for (int j = 0; j < db_user.size(); j++) {
//  					User user = new User();
//  					user.setUserId(new Integer(db_user.getInt(j, "user_id")));
//  					user.setUserName(db_user.getString(j, "user_name"));
//  					user.setUserRealname(db_user.getString(j, "user_realname"));
//  					user.setUserHometel(db_user.getString(j, "USER_HOMETEL"));
//  					user.setUserWorktel(db_user.getString(j, "USER_WORKTEL"));
//  					user.setUserMobiletel1(db_user.getString(j,
//  							"USER_MOBILETEL1"));
//  					user.setUserMobiletel2(db_user.getString(j,
//  							"USER_MOBILETEL2"));
//  					user.setUserEmail(db_user.getString(j, "USER_EMAIL"));
//  					user.setOrgName(db_user.getString(j, "org_job"));
//  					user.setJob_name(db_user.getString(j, "org_job"));
//  					userList.add(user);
//  				}
//  				// 递归调用
//  				//if("true".equalsIgnoreCase(isRecursive))
//  				    sortUser(org_id,userList);
//  			}
//  		} catch (SQLException e) {
//  			e.printStackTrace();
//  		}
//  		
//  	}
	
	public static List<User> getSearchUser(HttpServletRequest request,String parent_id)
	{
		ListInfo listInfo = sortUser1(request,parent_id,-1,-1);
		if(listInfo != null)
			return listInfo.getDatas();
		return new ArrayList<User>();
	}
	public static ListInfo sortUser1(HttpServletRequest request,String parent_id,long offset,
			int maxPagesize) {
		
    	String userName = request.getParameter("userName");		
		String userRealname = request.getParameter("userRealname");
		String advQuery = (String) request.getAttribute("advQuery");
		String userOrgType = request.getParameter("userOrgType");
		
		if(userName == null)userName="";
		if(userRealname == null)userRealname="";
		if(userOrgType == null)userOrgType="";
		if((userOrgType == null) || (userOrgType == ""))userOrgType="hasMainOrg";
		
		if(advQuery == null){
			advQuery = request.getParameter("advQuery");
		}
		advQuery = advQuery==null?"":advQuery;
		userName = userName==null?"":userName;
		userRealname = userRealname==null?"":userRealname;
		HttpSession session = request.getSession();
		UserInfoForm userInfoForm = (UserInfoForm) session.getAttribute("advUserform");
		if(userInfoForm == null){
			userInfoForm = (UserInfoForm) request.getAttribute("advUserform");
		}
		if(userInfoForm == null){
			userInfoForm = new UserInfoForm();
		}
		StringBuffer  sb_user = new StringBuffer();
		if (advQuery.equals("1")) {
			if(userInfoForm.getUserName() != null && userInfoForm.getUserName().length()>0){				
				sb_user.append(" and t.user_name like '%" + userInfoForm.getUserName().trim()+ "%' ");
			}
			if(userInfoForm.getUserRealname() != null && userInfoForm.getUserRealname().length()>0){
				sb_user.append("  and t.user_realname like '%" + userInfoForm.getUserRealname().trim()+ "%'");
			}
			if(userInfoForm.getUserWorktel() != null && userInfoForm.getUserWorktel().length()>0){
				sb_user.append(" and t.USER_WORKTEL like '%" + userInfoForm.getUserWorktel().trim()+ "%' ");
			}
			if(userInfoForm.getUserSex() != null && userInfoForm.getUserSex().length()>0 && (!userInfoForm.getUserSex().equals("-100"))){
				sb_user.append(" and t.USER_SEX = '" + userInfoForm.getUserSex().trim()+ "' ");
			}
			if(userInfoForm.getHomePhone() != null && userInfoForm.getHomePhone().length()>0){
				sb_user.append(" and t.USER_HOMETEL like '%" + userInfoForm.getHomePhone().trim()+ "%' ");
			}
			if(userInfoForm.getMail() != null && userInfoForm.getMail().length()>0){
				sb_user.append(" and t.USER_EMAIL like '%" + userInfoForm.getMail().trim()+ "%' ");
			}
			if(userInfoForm.getMobile() != null && userInfoForm.getMobile().length()>0){
				sb_user.append(" and t.USER_MOBILETEL1 like '%" + userInfoForm.getMobile().trim()+ "%' ");
			}
			if(userInfoForm.getUserPinyin() != null && userInfoForm.getUserPinyin().length()>0){
				sb_user.append(" and t.USER_PINYIN like '%" + userInfoForm.getUserPinyin().trim()+ "%' ");
			}
			if(userInfoForm.getUserMobiletel2() != null && userInfoForm.getUserMobiletel2().length()>0){
				sb_user.append(" and t.USER_MOBILETEL2 like '%" + userInfoForm.getUserMobiletel2().trim()+ "%' ");
			}
			if(userInfoForm.getUserType() != null && userInfoForm.getUserType().length()>0 && (!userInfoForm.getUserType().equals("-100"))){
				sb_user.append(" and t.USER_TYPE = '" + userInfoForm.getUserType().trim()+ "' ");
			}
			if(userInfoForm.getPostalCode() != null && userInfoForm.getPostalCode().length()>0){
				sb_user.append(" and t.USER_POSTALCODE like '%" + userInfoForm.getPostalCode().trim()+ "%' ");
			}
			if(userInfoForm.getUserFax() != null && userInfoForm.getUserFax().length()>0){
				sb_user.append(" and t.USER_FAX like '%" + userInfoForm.getUserFax().trim()+ "%' ");
			}
			if(userInfoForm.getUserOicq() != null && userInfoForm.getUserOicq().length()>0){
				sb_user.append(" and t.USER_OICQ  like '%" + userInfoForm.getUserOicq().trim()+ "%' ");
			}
			
			if(userInfoForm.getUserBirthday() != null && userInfoForm.getUserBirthday().length()>0){
				sb_user.append(" and t.USER_BIRTHDAY = to_date('" + userInfoForm.getUserBirthday().trim()+"','YYYY-MM-DD')");
			}
			
			if(userInfoForm.getUserAddress() != null && userInfoForm.getUserAddress().length()>0){
				sb_user.append(" and t.USER_ADDRESS like '%" + userInfoForm.getUserAddress().trim()+ "%' ");
			}
			
			if(userInfoForm.getUserLogincount() > 0){
				sb_user.append(" and t.USER_LOGINCOUNT = " + userInfoForm.getUserLogincount());							
			}
			
			if(userInfoForm.getUserIsvalid()!= -100)
				sb_user.append(" and t.USER_ISVALID = " + userInfoForm.getUserIsvalid());
			
			if(userInfoForm.getUserRegdate() != null && userInfoForm.getUserRegdate().length()>0){
				sb_user.append("and t.USER_REGDATE = to_date('" + userInfoForm.getUserRegdate()+"','YYYY-MM-DD')");
			}
			
			if(userInfoForm.getShortMobile() != null && userInfoForm.getShortMobile().length()>0){
				sb_user.append(" and t.REMARK2 like '%" + userInfoForm.getShortMobile().trim()+ "%' ");
			}
			} 
			//一般查询
			else {
				if (userName != null && userName.trim().length() > 0) {
					sb_user.append(" and t.user_name like '%" + userName.trim() + "%' ");
				}
				if (userRealname != null && userRealname.trim().length() > 0) {
					sb_user.append(" and t.user_realname like '%"+ userRealname.trim() +"%'");
				}
			}
		
//		DBUtil db_user = new DBUtil();
		PreparedDBUtil pe = new PreparedDBUtil();
		if(userOrgType.equalsIgnoreCase("hasMainOrg")){//有主机构用户查询
			AccessControl control = AccessControl.getAccessControl();
			
			String curUserId = control.getUserID();
			
			String org_job;
			
			String sql_ = sqlUtilInsert.getSQL("usersearchlist_sortUser_simple");
			
			
			StringBuffer sql = new StringBuffer().append(sql_).append(sb_user);
//			.append(" select bb.same_job_user_sn,getUserorgjobinfos(t.user_id || '') as org_job,  ")
//			.append(" t.*, a.org_id,a.org_sn from  ")
//			.append(" (select rownum as num,org_sn, a.org_id  from td_sm_organization a start with a.parent_id= '0'  ")
//			.append(" connect by prior a.org_id = a.parent_id order siblings by a.org_sn) a, ")
//			.append(" ( select min(tmp.same_job_user_sn) as same_job_user_sn,tmp.org_id,tmp.user_id from (  ")
//			.append(" (select  ujo.* from td_sm_userjoborg ujo,TD_SM_ORGUSER ou where ")
//			.append(" ou.org_id=ujo.org_id  and ou.user_id=ujo.user_id  ) ")
//			.append(" )tmp group by  tmp.user_id ,tmp.org_id   ")
//			.append("  ) bb, ")
//			.append(" td_sm_user t  ")
//			.append(" where a.org_id=bb.org_id ");
            ListInfo listinfo = new ListInfo();
            
            UserManager userManager = SecurityDatabase.getUserManager();
            try {
                listinfo = userManager.getUserList(sql.toString(), (int) offset, maxPagesize);
            } catch (ManagerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            };
//				listinfo.setTotalSize(pe.getTotalSize());
//				for (int j = 0; j < pe.size(); j++) {
//					User user = new User();
//					user.setUserId(new Integer(pe.getInt(j, "user_id")));
//					user.setUserName(pe.getString(j, "user_name"));
//					user.setUserRealname(pe.getString(j, "user_realname"));
//					user.setUserHometel(pe.getString(j, "USER_HOMETEL"));
//					user.setUserWorktel(pe.getString(j, "USER_WORKTEL"));
//					user.setUserMobiletel1(pe.getString(j,
//							"USER_MOBILETEL1"));
//					user.setUserMobiletel2(pe.getString(j,
//							"USER_MOBILETEL2"));
//					user.setUserEmail(pe.getString(j, "USER_EMAIL"));
//					
////					ora_org_name = db_user.getString(j, "org_job");
//					
//					ora_org_name = FunctionDB.getUserorgjobinfos(pe.getInt(j, "user_id"));
//					
//					if(ora_org_name.endsWith("、")){
//						org_name = ora_org_name.substring(0,ora_org_name.length()-1);
//					}else{
//						org_name = ora_org_name;
//					}
//					user.setOrgName(org_name);
//					user.setJob_name(org_name);
//					userList.add(user); 
//				}
//				
////				System.out.println("有主机构用户查询----------------------"+sql.toString());
//				listinfo.setDatas(userList);
            return listinfo;
			
		}else if(userOrgType.equalsIgnoreCase("noMainOrg")){//没有主机构
				AccessControl control = AccessControl.getInstance();
				control.checkAccess(session);
				String curUserId = control.getUserID();
//				StringBuffer sql = new StringBuffer()	
//				.append(" select bb.same_job_user_sn,getUserorgjobinfos(t.user_id || '') as org_job,  ")
//				.append(" t.*, a.org_id,a.org_sn from  ")
//				.append(" (select rownum as num,org_sn, a.org_id  from td_sm_organization a start with a.parent_id= '0'  ")
//				.append(" connect by prior a.org_id = a.parent_id order siblings by a.org_sn) a, ")
//				.append(" ( select min(tmp.same_job_user_sn) as same_job_user_sn,tmp.org_id,tmp.user_id from (  ")
//				.append(" (select ujo.* from td_sm_userjoborg ujo where ujo.user_id not in (select ou.user_id from TD_SM_ORGUSER ou)) ")
//				.append(" )tmp group by  tmp.user_id ,tmp.org_id   ")
//				.append("  ) bb, ")
//				.append(" td_sm_user t  ")
//				.append(" where a.org_id=bb.org_id");
				
				StringBuffer sql = new StringBuffer().append(sqlUtilInsert.getSQL("usersearchlist_sortUser1"));	
				
				if(!control.isAdmin()){//过滤用户查询。根据用户可管理的机构条件过滤
//					sql.append(" and a.org_id in(select distinct org.org_id from td_sm_organization org ")
//						.append(" start with org.org_id in(select o.org_id from td_sm_organization o, td_sm_orgmanager om ")
//						.append(" where o.org_id = om.org_id  and om.user_id = '").append(curUserId).append("') ")
//						.append("connect by prior org.org_id = org.parent_id)");
					String concat_ = DBUtil.getDBAdapter().concat(" org_tree_level","'|%' ");
					sql.append(" and a.org_id in")
					.append("(select distinct t.org_id from td_sm_organization t where t.org_tree_level like ")
					.append("(select ")
					.append(concat_)
					.append(" from TD_SM_ORGANIZATION where org_id =")
					.append("(select c.org_id from TD_SM_ORGANIZATION c,td_sm_orgmanager om  where c.org_id = om.org_id and om.user_id ='" +curUserId+"')")
					.append(")")
					.append(" or t.org_id in")
					.append("(select c.org_id from TD_SM_ORGANIZATION c,td_sm_orgmanager om  where c.org_id = om.org_id and om.user_id ='" +curUserId+"')");
				}
				sql.append(" and bb.user_id=t.user_id  ")
					.append(sb_user.toString())
					.append(" order by bb.same_job_user_sn,t.user_id ");
				try {
					if(offset != -1)
						pe.preparedSelect(sql.toString(), offset,maxPagesize);
					else
						pe.preparedSelect(sql.toString());
					pe.executePrepared();
//					pe.executeSelect(sql.toString(), offset,
//							maxPagesize);
					String org_name = "";
					String ora_org_name = "";
				    ListInfo listinfo = new ListInfo();
				    List userList = new ArrayList();
					listinfo.setTotalSize(pe.getTotalSize());
					for (int j = 0; j < pe.size(); j++) {
						User user = new User();
						user.setUserId(new Integer(pe.getInt(j, "user_id")));
						user.setUserName(pe.getString(j, "user_name"));
						user.setUserRealname(pe.getString(j, "user_realname"));
						user.setUserHometel(pe.getString(j, "USER_HOMETEL"));
						user.setUserWorktel(pe.getString(j, "USER_WORKTEL"));
						user.setUserMobiletel1(pe.getString(j,
								"USER_MOBILETEL1"));
						user.setUserMobiletel2(pe.getString(j,
								"USER_MOBILETEL2"));
						user.setUserEmail(pe.getString(j, "USER_EMAIL"));
						
//						ora_org_name = db_user.getString(j, "org_job");
						
						ora_org_name = FunctionDB.getUserorgjobinfos(pe.getInt(j, "user_id"));
						
						if(ora_org_name.endsWith("、")){
							org_name = ora_org_name.substring(0,ora_org_name.length()-1);
						}else{
							org_name = ora_org_name;
						}
						user.setOrgName(org_name);
						user.setJob_name(org_name);
						userList.add(user); 
					}
					
//					System.out.println("没有主机构的用户查询----------------------"+sql.toString());
					listinfo.setDatas(userList);
					return listinfo;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(userOrgType.equalsIgnoreCase("dis")){//离散用户查询
					StringBuffer sql = new StringBuffer()	
					.append("select user_id,user_name,user_realname,USER_MOBILETEL1 from td_sm_user  t where 1=1 ")
					.append(sb_user.toString())
					.append("and t.user_id in (select user1_.USER_ID from td_sm_user user1_ where not exists( ")
					.append("select userjoborg1_.user_id from td_sm_userjoborg userjoborg1_ where user1_.USER_ID= userjoborg1_.user_id)) order by user_id");

					
//					.append(" and t.user_id in (select user1_.USER_ID from td_sm_user user1_ minus select userjoborg1_.user_id from td_sm_userjoborg userjoborg1_)");
					try {
						if(offset != -1)
							pe.preparedSelect(sql.toString(), offset,maxPagesize);
						else
							pe.preparedSelect(sql.toString());
//						pe.executeSelect(sql.toString(), offset,
//								maxPagesize);
						pe.executePrepared();
						String org_name = "";
						String ora_org_name = "";
					    ListInfo listinfo = new ListInfo();
					    List userList = new ArrayList();
						listinfo.setTotalSize(pe.getTotalSize());
						for (int j = 0; j < pe.size(); j++) {
							User user = new User();
							user.setUserId(new Integer(pe.getInt(j, "user_id")));
							user.setUserName(pe.getString(j, "user_name"));
							user.setUserRealname(pe.getString(j, "user_realname"));
							user.setUserMobiletel1(pe.getString(j,
									"USER_MOBILETEL1"));
							user.setOrgName("离散用户");
							userList.add(user); 
						}
						System.out.println("离散用户查询----------------------"+sql.toString());
						listinfo.setDatas(userList);
						return listinfo;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}//离散用户查询
		try{
			String org_name = "";
			String ora_org_name = "";
		    ListInfo listinfo = new ListInfo();
		    List userList = new ArrayList();
			listinfo.setTotalSize(pe.getTotalSize());
			for (int j = 0; j < pe.size(); j++) {
				User user = new User();
				user.setUserId(new Integer(pe.getInt(j, "user_id")));
				user.setUserName(pe.getString(j, "user_name"));
				user.setUserRealname(pe.getString(j, "user_realname"));
				user.setUserHometel(pe.getString(j, "USER_HOMETEL"));
				user.setUserWorktel(pe.getString(j, "USER_WORKTEL"));
				user.setUserMobiletel1(pe.getString(j,
						"USER_MOBILETEL1"));
				user.setUserMobiletel2(pe.getString(j,
						"USER_MOBILETEL2"));
				user.setUserEmail(pe.getString(j, "USER_EMAIL"));
				
//				ora_org_name = db_user.getString(j, "org_job");
				ora_org_name = FunctionDB.getUserorgjobinfos(pe.getInt(j, "user_id"));
				
				if(ora_org_name.endsWith("、")){
					org_name = ora_org_name.substring(0,ora_org_name.length()-1);
				}else{
					org_name = ora_org_name;
				}
				user.setOrgName(org_name);
				user.setJob_name(org_name);
				userList.add(user); 
			}
			
			
			listinfo.setDatas(userList);
			return listinfo;
			
			
		}catch(Exception e){
			e.printStackTrace();
		} 
		return null;
	}  
	
	public String getRecOrderUserSql(String orgId,String wherestr){
		int oid = 0;
		try {
			oid = Integer.parseInt(orgId);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		StringBuffer sql = new StringBuffer()
			.append(" select  bb.same_job_user_sn,getUserorgjobinfos(t.user_id || '') as org_job,  ")
			.append(" t.*, a.org_id,a.org_sn from  ")
			.append(" (select rownum as num,org_sn, a.org_id  from td_sm_organization a start with a.org_id='")
			.append(oid)
			.append("' connect by prior a.org_id = a.parent_id order siblings by a.org_sn) a, ")
			.append(" ( select min(tmp.same_job_user_sn) as same_job_user_sn,tmp.org_id,tmp.user_id from (  ")
			.append(" (select ujo.* from td_sm_userjoborg ujo where ujo.user_id  in (select ou.user_id from TD_SM_ORGUSER ou)) ")
			.append(" )tmp group by  tmp.user_id ,tmp.org_id   ")
			.append("  ) bb, ")
			.append(" td_sm_user t  ")
			.append(" where a.org_id=bb.org_id and bb.user_id=t.user_id  ")
			.append(wherestr)
			.append(" order by a.num,bb.same_job_user_sn,t.user_id ");
		log.warn(sql.toString());
		return sql.toString();
	}	
	
	
	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
	
	/**
	 * 在机构下查找一个用户,包括机构下的子机构
	 * @param userId
	 * @param orgId
	 * @return
	 */
	public boolean isUserOrg(String userId,String orgId){
		if(userId.equals("1"))return false;
		OrgManager orgmanager = new OrgManagerImpl();
		try{
			Organization org = orgmanager.getOrgById(orgId);
			List orgList = new OrgManagerImpl().getChildOrgList(org,true);
			DBUtil db = new DBUtil();
			String sql = "";
			for(int i=0;i<orgList.size();i++){
				Organization o = (Organization)orgList.get(i);
				sql = " select * from td_sm_userjoborg where user_id = '"+userId+"' and org_id = '"+o.getOrgId()+"' ";
				db.executeSelect(sql);
				if(db.size()>0) return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

}
