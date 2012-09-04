/*
 * Created on 2006-3-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * @author ok
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class UserList extends DataInfoImpl implements Serializable{

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.String,
	 *      boolean, long, int)
	 */
	private Logger logger = Logger.getLogger(UserList.class.getName());

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
//		ListInfo listInfo = new ListInfo();
//		String userName = request.getParameter("userName");
//		String userRealname = request.getParameter("userRealname");
//		String orgId = request.getParameter("orgId");
//		String advQuery = (String) request.getAttribute("advQuery");
//		if (advQuery == null) {
//			advQuery = "";
//		}
//		UserInfoForm userInfoForm = (UserInfoForm) request
//				.getAttribute("advUserform");
//		// 高级查询
//		if (advQuery.equals("1")) {
//			try{				
//				StringBuffer hsql = new StringBuffer(
//				"from User u where 1=1 ");
//				if(userInfoForm.getUserName() != null && userInfoForm.getUserName().length()>0){
//					hsql.append(" and u.userName like '%" + userInfoForm.getUserName()
//							+ "%' ");
//				}
//				if(userInfoForm.getUserRealname() != null && userInfoForm.getUserRealname().length()>0){
//					hsql.append(" and u.userRealname like '%" + userInfoForm.getUserRealname()
//							+ "%' ");
//				}
//				if(userInfoForm.getUserWorktel() != null && userInfoForm.getUserWorktel().length()>0){
//					hsql.append(" and u.userWorktel like '%" + userInfoForm.getUserWorktel()
//							+ "%' ");
//				}
//				if(userInfoForm.getUserSex() != null && userInfoForm.getUserSex().length()>0){
//					hsql.append(" and u.userSex = '" + userInfoForm.getUserSex()
//							+ "' ");
//				}
//				if(userInfoForm.getHomePhone() != null && userInfoForm.getHomePhone().length()>0){
//					hsql.append(" and u.userHometel like '%" + userInfoForm.getHomePhone()
//							+ "%' ");
//				}
//				if(userInfoForm.getMail() != null && userInfoForm.getMail().length()>0){
//					hsql.append(" and u.userEmail like '%" + userInfoForm.getMail()
//							+ "%' ");
//				}
//				if(userInfoForm.getMobile() != null && userInfoForm.getMobile().length()>0){
//					hsql.append(" and u.userMobiletel1 like '%" + userInfoForm.getMobile()
//							+ "%' ");
//				}
//				if(userInfoForm.getUserPinyin() != null && userInfoForm.getUserPinyin().length()>0){
//					hsql.append(" and u.userPinyin like '%" + userInfoForm.getUserPinyin()
//							+ "%' ");
//				}
//				if(userInfoForm.getUserMobiletel2() != null && userInfoForm.getUserMobiletel2().length()>0){
//					hsql.append(" and u.userMobiletel2 like '%" + userInfoForm.getUserMobiletel2()
//							+ "%' ");
//				}
//				if(userInfoForm.getUserType() != null && userInfoForm.getUserType().length()>0){
//					hsql.append(" and u.userType = '" + userInfoForm.getUserType()
//							+ "' ");
//				}
//				if(userInfoForm.getPostalCode() != null && userInfoForm.getPostalCode().length()>0){
//					hsql.append(" and u.userPostalcode like '%" + userInfoForm.getPostalCode()
//							+ "%' ");
//				}
//				if(userInfoForm.getUserFax() != null && userInfoForm.getUserFax().length()>0){
//					hsql.append(" and u.userFax like '%" + userInfoForm.getUserFax()
//							+ "%' ");
//				}
//				if(userInfoForm.getUserOicq() != null && userInfoForm.getUserOicq().length()>0){
//					hsql.append(" and u.userOicq like '%" + userInfoForm.getUserOicq()
//							+ "%' ");
//				}
//				
//				if(userInfoForm.getUserBirthday() != null && userInfoForm.getUserBirthday().length()>0){
//					hsql.append(" and u.userBirthday = to_date('" + userInfoForm.getUserBirthday()+"','YYYY-MM-DD')");
//				}
//				if(userInfoForm.getUserAddress() != null && userInfoForm.getUserAddress().length()>0){
//					hsql.append(" and u.userAddress like '%" + userInfoForm.getUserAddress()
//							+ "%' ");
//				}
//				if(userInfoForm.getUserLogincount() > 0){
//					hsql.append(" and u.userLogincount = " + userInfoForm.getUserLogincount());							
//				}
//				
//				hsql.append(" and u.userIsvalid = " + userInfoForm.getUserIsvalid());
//				
//				if(userInfoForm.getUserRegdate() != null && userInfoForm.getUserRegdate().length()>0){
//					hsql.append(" and u.userRegdate = to_date('" + userInfoForm.getUserRegdate()+"','YYYY-MM-DD')");
//				}
//				
//				if(userInfoForm.getShortMobile() != null && userInfoForm.getShortMobile().length()>0){
//					hsql.append(" and u.remark2 like '%" + userInfoForm.getShortMobile()
//							+ "%' ");
//				}
//				UserManager userManager = SecurityDatabase.getUserManager();
//				
//				List list = null;
//				Organization org = new Organization();
//				org.setOrgId(orgId);
//				PageConfig pageConfig = userManager.getPageConfig();
//				pageConfig.setPageSize(maxPagesize);
//				pageConfig.setStartIndex((int) offset);
//				//list = userManager.getUserList(hsql.toString()+ " order by u.userSn asc");
//				listInfo.setTotalSize(pageConfig.getTotalSize());
//
//				listInfo.setDatas(list);
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
//		} 
//		//一般查询和机构列表
//		else {
//			 
//			// 查询
//			if ((userName != null && userName.length() > 0)
//					|| (userRealname != null && userRealname.length() > 0)) {
//				try {
//					StringBuffer hsql = new StringBuffer(
//							"from User u where 1=1");
//					if (userName != null && userName.length() > 0) {
//						hsql.append(" and u.userName like '%" + userName
//								+ "%' ");
//					}
//					if (userRealname != null && userRealname.length() > 0) {
//						hsql.append(" and u.userRealname like '%"
//								+ userRealname + "%'");
//					}
//					hsql
//							.append(" and u.userId in (select ujo.id.userId from Userjoborg ujo where ujo.id.orgId = '"
//									+ orgId + "')");
//					UserManager userManager = SecurityDatabase.getUserManager();
//					JobManager jobManager = SecurityDatabase.getJobManager();
//					List list = null;
//					Organization org = new Organization();
//					org.setOrgId(orgId);
//					PageConfig pageConfig = userManager.getPageConfig();
//					pageConfig.setPageSize(maxPagesize);
//					pageConfig.setStartIndex((int) offset);
//					//list = userManager.getUserList(hsql.toString()+ "order by u.userSn asc");
//					// 景峰添加
//					for (int i = 0; list != null && i < list.size(); i++) {
//						User tuser = (User) list.get(i);
//						List jobList = jobManager.getJobList(org, tuser);
//						tuser.setJobList(jobList);
//					}
//					// add end
//					listInfo.setTotalSize(pageConfig.getTotalSize());
//
//					listInfo.setDatas(list);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			// 正常的取机构用户列表
//			else {
//				if (orgId == null) {
//					orgId = (String) request.getAttribute("orgId");
//				}
//
//				try {
//					UserManager userManager = SecurityDatabase.getUserManager();
//					JobManager jobManager = SecurityDatabase.getJobManager();
//					Organization org = new Organization();
//					org.setOrgId(orgId);
//					List list = null;
//					PageConfig pageConfig = userManager.getPageConfig();
//					pageConfig.setPageSize(maxPagesize);
//					pageConfig.setStartIndex((int) offset);
//
//					list = userManager.getUserList(org);
//					// 景峰添加
//					for (int i = 0; list != null && i < list.size(); i++) {
//						User tuser = (User) list.get(i);
//						List jobList = jobManager.getJobList(org, tuser);
//						tuser.setJobList(jobList);
//					}
//					// 景峰添加结束
//					listInfo.setTotalSize(pageConfig.getTotalSize());
//					listInfo.setDatas(list);
//					session.setAttribute("userListPageOffset", "" + offset);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.String,
	 *      boolean)
	 */
	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
