/*
 * Created on 2006-6-16
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * @author ok
 * 用户管理 机构下的用户排序
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserListSn extends DataInfoImpl implements Serializable{
	private Logger log = Logger.getLogger(UserListSn.class);

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {

		String userName = request.getParameter("userName");
		String userRealname = request.getParameter("userRealname");		
		String flag = request.getParameter("flag");
		String orgId = request.getParameter("orgId");
		//是否税管员
		String taxmanager = request.getParameter("taxmanager");
		/*是否递归*/
		String recursion = request.getParameter("intervalType");		
		recursion = recursion==null?"":recursion;
		ListInfo listInfo = new ListInfo();		

		String subsql = "select org_id from TD_SM_ORGANIZATION o start with o.ORG_ID='";
		subsql += orgId;
		subsql += "' connect by prior o.ORG_ID=o.PARENT_ID";

		try {

			UserManager userManager = SecurityDatabase.getUserManager();
			StringBuffer sb_user = new StringBuffer();
			if (flag == null && userName == null && userRealname == null
					&& recursion == null) {//一般的查询
				sb_user
						.append("select a.*,b.x,b.y,b.org_id from td_sm_user a inner join (");
				sb_user
						.append(" select user_id,min(job_sn) x,min(same_job_user_sn) y,org_id from td_sm_userjoborg ");
				sb_user.append(" where org_id='" + orgId + "' ");
				sb_user.append(" group by user_id,org_id");
				sb_user.append(" ) b on a.user_id=b.user_id ");
			} else if (flag != null && flag.equals("1")) //
			{
				sb_user
						.append("select a.*,b.x,b.y,b.org_id from td_sm_user a inner join (");
				sb_user
						.append(" select user_id,min(job_sn) x,min(same_job_user_sn) y,org_id from td_sm_userjoborg ");
				sb_user.append(" where org_id='" + orgId + "' ");
				sb_user.append(" group by user_id,org_id");
				sb_user.append(" ) b on a.user_id=b.user_id ");
				if (userName != null && userName != "") {
					sb_user
							.append("and a.user_name like '%" + userName
									+ "%' ");
				}
				if (userRealname != null && userRealname != "") {
					sb_user.append("and a.user_realname like '%" + userRealname
							+ "%'");
				}
				if (taxmanager != null && taxmanager != "") {
					if("1".equals(taxmanager)){
						sb_user.append("and a.istaxmanager='1' ");
					}
					if("0".equals(taxmanager)){
						sb_user.append("and (a.istaxmanager is null or a.istaxmanager<>'1') ");
					}
					if("3".equals(taxmanager)){
					}
				}
			} else if ((flag != null && flag.equals("2"))
					|| (flag != null && flag.equals("3"))) {
				sb_user
						.append("select a.*,b.x,b.y,b.org_id from td_sm_user a inner join (");
				sb_user
						.append(" select user_id,min(job_sn) x,min(same_job_user_sn) y,org_id from td_sm_userjoborg ");
				sb_user.append(" where org_id='" + orgId + "' ");
				sb_user.append(" group by user_id,org_id");
				sb_user.append(" ) b on a.user_id=b.user_id ");
			}

			else {
				sb_user
						.append("select a.*,b.x,b.y,b.org_id from td_sm_user a inner join (");
				sb_user
						.append(" select user_id,min(job_sn) x,min(same_job_user_sn) y,org_id from td_sm_userjoborg ");
				sb_user.append(" where org_id='" + orgId + "' ");
				sb_user.append(" group by user_id,org_id");
				sb_user.append(" ) b on a.user_id=b.user_id ");
				if (userName != null && userName != "") {
					sb_user
							.append("and a.user_name like '%" + userName
									+ "%' ");
				}
				if (userRealname != null && userRealname != "") {
					sb_user.append("and a.user_realname like '%" + userRealname
							+ "%' ");
				}
				if (taxmanager != null && taxmanager != "") {
					if("1".equals(taxmanager)){
						sb_user.append("and a.istaxmanager='1' ");
					}
					if("0".equals(taxmanager)){
						sb_user.append("and (a.istaxmanager is null or a.istaxmanager<>'1') ");
					}
					if("3".equals(taxmanager)){
					}
				}
			}
			sb_user.append("order by y,b.user_id");

			log.warn("用户管理 机构下的用户排序 不递归---------------------------"+ sb_user.toString());
            
			/*递归查询*/
			if (recursion != null && recursion != "" && recursion.equals("1")) {
				StringBuffer rec_user = new StringBuffer();	
				StringBuffer sub_user = new StringBuffer();				
				if (userName != null && userName != "") {
					sub_user.append("and t.user_name like '%" + userName + "%' ");
				}
				if (userRealname != null && userRealname != "") {
					sub_user.append("and t.user_realname like '%" + userRealname + "%' ");
				}
				if (taxmanager != null && taxmanager != "") {
					if("1".equals(taxmanager)){
						sub_user.append("and t.istaxmanager='1' ");
					}
					if("0".equals(taxmanager)){
						sub_user.append("and (t.istaxmanager is null or t.istaxmanager<>'1') ");
					}
					if("3".equals(taxmanager)){
					}
				}
				String temp = new UserSearchList().getRecOrderUserSql(orgId,sub_user.toString());
				rec_user.append(temp);
				log.warn("用户管理 机构下的用户排序 递归---------------------------"+ rec_user.toString());
				listInfo = userManager.getUserList(rec_user.toString(),	(int) offset, maxPagesize);
				return listInfo;
			}

			listInfo = userManager.getUserList(sb_user.toString(),(int) offset, maxPagesize);
			return listInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
