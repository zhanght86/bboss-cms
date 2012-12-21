package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 机构中生成邮箱帐户的用户列表
 * 
 * @author 
 * @file MailUserList.java Created on: Apr 8, 2006
 */
public class MailUserList extends DataInfoImpl implements Serializable {	

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {

//		ListInfo listInfo = new ListInfo();
//		String orgId = request.getParameter("orgId");
//
//		// 机构用户列表
//
//		if (orgId == null) {
//			orgId = (String) request.getAttribute("orgId");
//		}
//
//		try {
//			UserManager userManager = SecurityDatabase.getUserManager();
//			JobManager jobManager = SecurityDatabase.getJobManager();
//			Organization org = new Organization();
//			org.setOrgId(orgId);
//			List list = null;
//			PageConfig pageConfig = userManager.getPageConfig();
//			pageConfig.setPageSize(maxPagesize);
//			pageConfig.setStartIndex((int) offset);
//
//			list = userManager.getUserList(org);
//			// 添加
//			for (int i = 0; list != null && i < list.size(); i++) {
//				User tuser = (User) list.get(i);
//				List jobList = jobManager.getJobList(org, tuser);
//				tuser.setJobList(jobList);
//			}
//			// 添加结束
//			listInfo.setTotalSize(pageConfig.getTotalSize());
//			listInfo.setDatas(list);
////			session.setAttribute("userListPageOffset", "" + offset);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return null;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
}
