package com.frameworkset.platform.sysmgrcore.purviewmanager.tag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.RollbackException;

import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.OrgJobName;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.FunctionDB;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.ListInfo;

/**
 * 将角色授予用户、机构及用户组三个查询放在一个列表类进行处理
 * 
 * @author Administrator
 * 
 */

public class RoleGrantSearchList extends DataInfoImpl  {

	

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		String action = request.getParameter("type");

		if (action == null || "".equals(action)) {
			action = (String) request.getAttribute("action");
		}

		if (action != null && !"".equals(action)) {

			if (action.equals("user")) {
				return getUserListByRoleId(sortKey, desc, offset, maxPagesize);
			} else if (action.equals("org")) {
				return getOrgListByRoleId(sortKey, desc, offset, maxPagesize);
			} else if (action.equals("group")) {
				return getGroupListByRoleId(sortKey, desc, offset, maxPagesize);
			} else if (action.equals("orgjob")) {
				return getOrgjobListByRoleId(sortKey, desc, offset, maxPagesize);
			}
		}
		return null;
	}

	protected ListInfo getDataList(String sortKey, boolean desc) {
		return null;
	}

	

	

	/**
	 * 获取此角色所对应的用户列表查询
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param maxPagesize
	 * @return
	 */
	private ListInfo getUserListByRoleId(String sortKey, boolean desc,
			long offset, int maxPagesize) {

		String roleId = request.getParameter("roleId");
		String username = request.getParameter("userName");
		String userrealname = request.getParameter("userRealName");

		StringBuffer sb = new StringBuffer(
				"select b.user_id, b.user_name,b.user_realname,b.user_hometel,b.user_worktel,b.user_mobiletel1");
		// sb.append(",getuserorgjobinfos(b.user_id || '') as org_job,b.user_mobiletel2, b.user_email ");
		sb.append(",b.user_mobiletel2, b.user_email ");
		sb.append("from  td_sm_user b, td_sm_userrole c ");
		sb.append("where b.user_id = c.user_id and c.role_id = '");
		sb.append(roleId).append("' ");
		if (!super.accessControl.isAdmin()) {// 根据当前用户ID过滤用户可管理的机构下的用户的集合
			String curUserId = super.accessControl.getUserID();
			StringBuffer all_orgs = new StringBuffer()
					// 可管理机构集合
					.append(
							"select org_id from td_sm_organization where org_id in (")
					.append(
							"select distinct org.org_id from td_sm_organization org start with org.org_id in(")
					.append(
							"select o.org_id from td_sm_organization o, td_sm_orgmanager om where ")
					.append("o.org_id = om.org_id and om.user_id='").append(
							curUserId).append("') ").append(
							" connect by prior org.org_id = org.parent_id) ");
			sb
					.append(
							" and b.user_id IN(SELECT user_id FROM TD_SM_USERJOBORG WHERE org_id IN(")
					.append(all_orgs).append(")) ");
		}

		if (username != null && !"".equals(username)) {
			sb.append("and b.user_name like '%").append(username).append("%' ");
		}

		if (userrealname != null && !"".equals(userrealname)) {
			sb.append("and b.user_realname like '%").append(userrealname)
					.append("%'");
		}
		TransactionManager tm = new TransactionManager(); 
		DBUtil db = new DBUtil();
		try {
			tm.begin(TransactionManager.RW_TRANSACTION);
			ListInfo listinfo = new ListInfo();
			final List userList = new ArrayList();
			db.executeSelectWithRowHandler(sb.toString(), offset, maxPagesize,new NullRowHandler(){

				@Override
				public void handleRow(Record origine) throws Exception {
					String org_name = "";
					String ora_org_name = "";
					User user = new User();

					user.setUserId(new Integer(origine.getInt("USER_ID")));
					user.setUserName(origine.getString("USER_NAME"));
					user.setUserRealname(origine.getString("USER_REALNAME"));
					user.setUserHometel(origine.getString( "USER_HOMETEL"));
					user.setUserWorktel(origine.getString( "USER_WORKTEL"));
					user.setUserMobiletel1(origine.getString("USER_MOBILETEL1"));
					user.setUserMobiletel2(origine.getString("USER_MOBILETEL2"));
					user.setUserEmail(origine.getString( "USER_EMAIL"));

					// ora_org_name = db.getString(i, "org_job");
					int id = new Integer(origine.getInt("USER_ID"));
					ora_org_name = FunctionDB.getUserorgjobinfos(id);

					if (ora_org_name.endsWith("、")) {
						org_name = ora_org_name.substring(0, ora_org_name
								.length() - 1);
					} else {
						org_name = ora_org_name;
					}
					user.setOrgName(org_name);
					user.setJob_name(org_name);

					userList.add(user);
					
				}});
			listinfo.setTotalSize(db.getTotalSize());
			listinfo.setDatas(userList);
			
			tm.commit();
			return listinfo;
			

		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取角色所对应的机构列表查询
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param maxPagesize
	 * @return
	 */
	private ListInfo getOrgListByRoleId(String sortKey, boolean desc,
			long offset, int maxPagesize) {

		String roleId = request.getParameter("roleId");
		String org_name = request.getParameter("orgName");
		String remark5 = request.getParameter("remark5");
		// String creator = request.getParameter("creator");

		StringBuffer sb = new StringBuffer(
				"select a.org_id,a.orgnumber,a.org_name,a.remark5,a.creator,a.org_xzqm ")
				.append("from td_sm_organization a , td_sm_orgrole b  ")
				.append("where a.org_id = b.org_id and b.role_id ='").append(
						roleId).append("' ");
		if (!super.accessControl.isAdmin()) {// 查询出用户能管理的机构集合
			String curUserId = super.accessControl.getUserID();
			StringBuffer all_orgs = new StringBuffer()
					.append(
							"select org_id from td_sm_organization where org_id in (")
					.append(
							"select distinct org.org_id from td_sm_organization org start with org.org_id in(")
					.append(
							"select o.org_id from td_sm_organization o, td_sm_orgmanager om where ")
					.append("o.org_id = om.org_id and om.user_id='").append(
							curUserId).append("') ").append(
							" connect by prior org.org_id = org.parent_id) ");
			sb.append(" and b.org_id in(").append(all_orgs).append(")");
		}
		if (org_name != null && !org_name.equals("")) {
			sb.append("and a.org_name like '%").append(org_name).append("%' ");
		}

		if (remark5 != null && !remark5.equals("")) {
			sb.append("and a.remark5 like '%").append(remark5).append("%' ");
		}

		// if(creator != null && !creator.equals(""))
		// {
		// sb.append("and c.user_name like '%").append(creator).append("%'");
		// }

		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sb.toString(), offset, maxPagesize);
			if (db.size() > 0) {
				ListInfo listinfo = new ListInfo();
				List orgList = new ArrayList();

				listinfo.setTotalSize(db.getTotalSize());

				for (int i = 0; i < db.size(); i++) {
					Organization org = new Organization();

					org.setOrgId(db.getString(i, "org_id"));

					org.setOrgName(db.getString(i, "org_name"));
					org.setRemark5(db.getString(i, "remark5"));
					org.setOrg_xzqm(db.getString(i, "org_xzqm"));
					org.setOrgnumber(db.getString(i, "orgnumber"));
					org.setCreator(db.getString(i, "creator"));

					orgList.add(org);
				}

				listinfo.setDatas(orgList);

				return listinfo;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取角色所对应的用户组的查询列表
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param maxPagesize
	 * @return
	 */
	private ListInfo getGroupListByRoleId(String sortKey, boolean desc,
			long offset, int maxPagesize) {
		String roleId = request.getParameter("roleId");
		String groupName = request.getParameter("groupName");
		String groupDesc = request.getParameter("groupDesc");
		// String creator = request.getParameter("creator");

		StringBuffer sb = new StringBuffer(
				"select a.GROUP_ID ,a.GROUP_NAME,a.GROUP_DESC,a.OWNER_ID ")
				.append("from TD_SM_GROUP a , TD_SM_GROUPROLE b ");

		if (!super.accessControl.isAdmin()) {// 用户组关联角色查询，用户只能将自己创建的角色关联到自己创建的组上面
			sb
					.append(
							",td_sm_user c where a.GROUP_ID = b.GROUP_ID and b.ROLE_ID = '")
					.append(roleId).append("' ").append(
							" and c.USER_ID = a.OWNER_ID ");
		} else {
			sb.append("where a.GROUP_ID = b.GROUP_ID and b.ROLE_ID = '")
					.append(roleId).append("' ");
		}
		if (groupName != null && !groupName.equals("")) {
			sb.append("and a.GROUP_NAME like '%").append(groupName).append(
					"%' ");
		}

		if (groupDesc != null && !groupDesc.equals("")) {
			sb.append("and a.GROUP_DESC like '%").append(groupDesc)
					.append("%'");
		}

		// if(creator != null && !creator.equals(""))
		// {
		// sb.append("and c.USER_NAME like '%").append(creator).append("%'");
		// }

		DBUtil db = new DBUtil();
		try {

			db.executeSelect(sb.toString(), offset, maxPagesize);

			if (db.size() > 0) {
				ListInfo listInfo = new ListInfo();
				listInfo.setTotalSize(db.getTotalSize());
				List groupList = new ArrayList();
				for (int i = 0; i < db.size(); i++) {
					Group group = new Group();

					group.setGroupId(db.getInt(i, "group_id"));
					group.setGroupName(db.getString(i, "group_name"));
					group.setGroupDesc(db.getString(i, "group_desc"));
					group.setOwner_id(db.getInt(i, "owner_id"));
					groupList.add(group);
				}

				listInfo.setDatas(groupList);
				return listInfo;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private ListInfo getOrgjobListByRoleId(String sortKey, boolean desc,
			long offset, int maxPagesize) {
		String roleId = request.getParameter("roleId");
		String jobName = request.getParameter("jobName");
		String remark5 = request.getParameter("remark5");
		ListInfo listInfo = new ListInfo();
		StringBuffer queryOrgJob = new StringBuffer()
				.append("SELECT c.remark5, b.job_name,b.OWNER_ID ")
				.append(
						"FROM TD_SM_ORGJOBROLE a, TD_SM_JOB b, TD_SM_ORGANIZATION c ")
				.append(
						"WHERE a.job_id = b.job_id AND a.org_id = c.org_id and a.ROLE_ID='")
				.append(roleId).append("' ");
		if (!super.accessControl.isAdmin()) {// 查询出用户能管理的机构集合
			String curUserId = super.accessControl.getUserID();
			StringBuffer all_orgs = new StringBuffer()
					.append(
							"select org_id from td_sm_organization where org_id in (")
					.append(
							"select distinct org.org_id from td_sm_organization org start with org.org_id in(")
					.append(
							"select o.org_id from td_sm_organization o, td_sm_orgmanager om where ")
					.append("o.org_id = om.org_id and om.user_id='").append(
							curUserId).append("') ").append(
							" connect by prior org.org_id = org.parent_id) ");
			queryOrgJob.append(" and a.org_id in(").append(all_orgs)
					.append(")");
		}
		if (jobName != null && !"".equals(jobName)) {
			queryOrgJob.append(" and b.job_name like '%").append(jobName)
					.append("%' ");
		}
		if (remark5 != null && !"".equals(remark5)) {
			queryOrgJob.append(" and c.remark5 like '%").append(remark5)
					.append("%' ");
		}
		queryOrgJob.append(" order by c.remark5");
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(queryOrgJob.toString(), offset, maxPagesize);
			listInfo.setTotalSize(db.getTotalSize());
			OrgJobName orgJobName = null;
			List orgJobNameList = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				orgJobName = new OrgJobName();
				orgJobName.setRemark5(db.getString(i, "remark5"));
				orgJobName.setJobName(db.getString(i, "job_name"));
				orgJobNameList.add(orgJobName);
			}
			listInfo.setDatas(orgJobNameList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	
}
