/*
 * Created on 2006-6-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.util.ListInfo;

/**
 * 电话本查询数据获取接口
 * <p>Title: TelListSn</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-8-8 18:00:36
 * @author biaoping.yin
 * @version 1.0
 */
public class TelListSn extends DataInfoImpl implements Serializable{
//	private List userList;
	private static final Logger log = LoggerFactory.getLogger(TelListSn.class);

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		
		List userLi = new ArrayList();
        String orderMethod = ConfigManager.getInstance().getConfigValue("cms.userOrder.orderMethod");
		if(orderMethod != null && orderMethod.trim().length()>0){        	
			return sortUser1("0", offset,
       			 maxPagesize);
        }else{
		    //缺省的排序(递归)方式
        	sortUser("0",userLi);
        }		
		return super.pagerList(userLi,(int)offset,maxPagesize);
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}	
	/**
	 * 递归
	 * @param parent_id
	 * @return 
	 * TelListSn.java
	 * @author: ge.tao
	 */
	private void sortUser(String parent_id,List userList) {
		
		DBUtil db_org = new DBUtil();
		String userName = request.getParameter("userName");
		String userRealname = request.getParameter("userRealname");
		
		try {
			// 取出直接下级机构，按jorg_sn,orgnumber排序
			db_org.executeSelect("select org_id from TD_SM_ORGANIZATION where parent_id='"
				+ parent_id + "' order by org_sn,orgnumber");

			for (int i = 0; i < db_org.size(); i++) {
				//
				String org_id = db_org.getString(i, "org_id");
				DBUtil db_user = new DBUtil();
				StringBuffer sb_user = new StringBuffer();
				sb_user.append("select b.remark3,b.user_id, b.user_name, b.user_realname, b.USER_HOMETEL,");
				sb_user.append("b.USER_WORKTEL, b.USER_MOBILETEL1, ");
				sb_user.append("b.USER_MOBILETEL2, b.USER_EMAIL from v_user_one_org_one_job a ");
				sb_user.append("inner join td_sm_user b on a.user_id = b.user_id ");
				sb_user.append("where org_id ='" + org_id + "' ");
				if (userName != null && userName != "") {
					sb_user.append("and b.user_name like '%" + userName.trim() + "%' ");
				}
				if (userRealname != null && userRealname != "") {
					sb_user.append("and b.user_realname like '%"+ userRealname.trim() +"%'");
				}
				sb_user.append("order by job_sn,SAME_JOB_USER_SN");

				// 取出一个机构的用户，按job_sn,SAME_JOB_USER_SN排序
				//log.warn("sql-------telListsn.sql-----------------"+sb_user.toString());
				System.out.println(sb_user.toString());
				db_user.executeSelect(sb_user.toString());
				//
				for (int j = 0; j < db_user.size(); j++) {
					User user = new User();
					user.setUserId(new Integer(db_user.getInt(j, "user_id")));
					user.setUserName(db_user.getString(j, "user_name"));
					user.setUserRealname(db_user.getString(j, "user_realname"));
					user.setUserHometel(db_user.getString(j, "USER_HOMETEL"));
					user.setUserWorktel(db_user.getString(j, "USER_WORKTEL"));
					user.setUserMobiletel1(db_user.getString(j,
							"USER_MOBILETEL1"));
					user.setUserMobiletel2(db_user.getString(j,
							"USER_MOBILETEL2"));
					user.setUserEmail(db_user.getString(j, "USER_EMAIL"));
					user.setRemark3(db_user.getString(j, "remark3"));
					userList.add(user);
				}
				// 递归调用
				sortUser(org_id,userList);
			}
		} catch (SQLException e) {
//			return null;
		}
//		return userList;
	}
	
	private ListInfo sortUser1(String parent_id,long offset,
			int maxPagesize) {
		
		String userName = request.getParameter("userName");
		String userRealname = request.getParameter("userRealname");
		StringBuffer sb_user = new StringBuffer();
		if (userName != null && userName != "") {
			sb_user.append("and t.user_name like '%" + userName.trim() + "%' ");
		}
		if (userRealname != null && userRealname != "") {
			sb_user.append("and t.user_realname like '%"+ userRealname.trim() +"%'");
		}
		
		
		/*
        select bb.same_job_user_sn,getUserorgjobinfos(c.user_id || '') as org_job,
        c.*, a.org_id,a.org_sn from 
        (select rownum as num,org_sn, a.org_id  from td_sm_organization a start with a.parent_id= 0 
        connect by prior a.org_id = a.parent_id order siblings by a.org_sn) a,
        (select ujo.* from td_sm_userjoborg ujo,TD_SM_ORGUSER ou where ou.org_id=ujo.org_id
        and ou.user_id=ujo.user_id  ) bb,
        td_sm_user c 
        where a.org_id=bb.org_id and bb.user_id=c.user_id 
        order by a.num,bb.same_job_user_sn 
		 */
//		StringBuffer sql = new StringBuffer()
//			.append(" select bb.same_job_user_sn,getUserorgjobinfos(t.user_id || '') as org_job, ")
//			.append(" t.*, a.org_id,a.org_sn from  ")
//			.append(" (select rownum as num,org_sn, a.org_id  from td_sm_organization a start with a.parent_id= '0' ")
//			.append(" connect by prior a.org_id = a.parent_id order siblings by a.org_sn) a, ")
//			.append(" ( select min(tmp.same_job_user_sn) as same_job_user_sn,tmp.org_id,tmp.user_id from (  ")
//			.append(" (select  ujo.* from td_sm_userjoborg ujo,TD_SM_ORGUSER ou where ")
//			.append(" ou.org_id=ujo.org_id  and ou.user_id=ujo.user_id  ) ")
//			.append(" )tmp group by  tmp.user_id ,tmp.org_id   ")
//			.append("  ) bb, ")
//		    .append(" td_sm_user t where a.org_id=bb.org_id and bb.user_id=t.user_id  ")
//		    .append(sb_user.toString())
//		    .append(" order by a.num,bb.same_job_user_sn,t.user_id  ");
		StringBuffer sql = new StringBuffer()
		.append(" select bb.same_job_user_sn,t.*,a.org_id,a.org_sn from (select  t.org_sn, t.org_id ")
		.append(" from td_sm_organization t where t.org_tree_level like '0|%')a,  ")
		.append(" (select min(tmp.same_job_user_sn) as same_job_user_sn,tmp.org_id,tmp.user_id ")
		.append("  from (select ujo.* from td_sm_userjoborg ujo, TD_SM_ORGUSER ou ")
		.append(" where ou.org_id = ujo.org_id and ou.user_id = ujo.user_id) tmp  ")
		.append(" group by tmp.user_id, tmp.org_id) bb,  td_sm_user t  ")
		.append(" where a.org_id = bb.org_id and bb.user_id = t.user_id ")
	    .append(sb_user.toString())
	    .append(" order by  bb.same_job_user_sn, t.user_id ");
		
//		System.out.println(sql.toString());
		DBUtil db_user = new DBUtil();
		log.warn("电话本查询----------------------"+sql.toString());
		try {
			db_user.executeSelect(sql.toString(),offset,maxPagesize);
			ListInfo listinfo = new ListInfo();
			listinfo.setTotalSize(db_user.getTotalSize());
			List userList = new ArrayList();
			//
			for (int j = 0; j < db_user.size(); j++) {
				User user = new User();
				user.setUserId(new Integer(db_user.getInt(j, "user_id")));
				user.setUserName(db_user.getString(j, "user_name"));
				user.setUserRealname(db_user.getString(j, "user_realname"));
				user.setUserHometel(db_user.getString(j, "USER_HOMETEL"));
				user.setUserWorktel(db_user.getString(j, "USER_WORKTEL"));
				user.setUserMobiletel1(db_user.getString(j,
						"USER_MOBILETEL1"));
				user.setUserMobiletel2(db_user.getString(j,
						"USER_MOBILETEL2"));
				user.setUserEmail(db_user.getString(j, "USER_EMAIL"));
				user.setRemark3(db_user.getString(j, "remark3"));
				userList.add(user);
			}
			listinfo.setDatas(userList);
			return  listinfo;
		} catch (SQLException e) {
			return null;
		}
		
	}

	public static void main(String[] args) {
		TelListSn ts = new TelListSn();
		List l = null;

		for (int i = 0; i < l.size(); i++) {
			User user = new User();
			user = (User) l.get(i);
			System.out.println(user.getUserId() + "   " + user.getUserName()
					+ "   " + user.getUserRealname());

		}

	}

}
