package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.UserJobs;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class UserJobList  extends DataInfoImpl implements Serializable{
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		String userName = request.getParameter("user_Name");
		String userRealName = request.getParameter("user_Realname");
		String jobName = request.getParameter("job_name");
		//System.out.println("jobName................"+jobName);
//		System.out.println("userName................"+userName);
//		System.out.println("userRealName................"+userRealName);

		try {
			if (userName == null && userRealName == null && jobName == null){
				String str ="select a.user_id,a.user_name,a.USER_REALNAME,a.USER_MOBILETEL1,j.JOB_NAME," +
				" o.ORG_NAME from td_sm_user a,TD_SM_ORGANIZATION o, "+
				" td_sm_job j,td_sm_userjoborg ujo where a.user_id = ujo.user_id and o.org_id = ujo.ORG_ID "+
				" and j.JOB_ID =ujo.JOB_ID order by j.job_number,o.ORG_SN,a.USER_REALNAME";
//				System.out.println("....."+str);
				dbUtil.executeSelect(str,(int)offset,maxPagesize);
				UserJobs ujl = null;
				List users = new ArrayList();
				for(int i = 0; i < dbUtil.size(); i ++)
				{
					ujl = new UserJobs();
					ujl.setUserId(new Integer(dbUtil.getInt(i,"user_id")));
					ujl.setUserName(dbUtil.getString(i,"user_name").trim());
					ujl.setUserRealname(dbUtil.getString(i,"USER_REALNAME").trim());
					ujl.setUserMobiletel1(dbUtil.getString(i,"USER_MOBILETEL1").trim());
					ujl.setOrgName(dbUtil.getString(i,"ORG_NAME").trim());
					ujl.setJobName(dbUtil.getString(i,"JOB_NAME").trim());
					users.add(ujl);
					
				}
				listInfo.setDatas(users);
				listInfo.setTotalSize(dbUtil.getTotalSize());
			}

			String hsql ="select a.user_id,a.user_name,a.USER_REALNAME,a.USER_MOBILETEL1,j.JOB_NAME," +
					" o.ORG_NAME from td_sm_user a,TD_SM_ORGANIZATION o, "+
					" td_sm_job j,td_sm_userjoborg ujo where a.user_id = ujo.user_id and o.org_id = ujo.ORG_ID"+
					" and j.JOB_ID =ujo.JOB_ID";
			
			if (userName != null && userName.length() > 0) {
				hsql = hsql +" and a.user_name like '%"+ userName +"%'";
			}
			if (userRealName != null && userRealName.length() > 0) {
				hsql = hsql +" and a.USER_REALNAME like '%"+ userRealName +"%'";
			}
			if (jobName != null && jobName.length() > 0) {
				hsql = hsql +" and j.JOB_NAME like '%"+ jobName +"%'";
			}
			
//			System.out.println(">>>>>"+hsql);
			dbUtil.executeSelect(hsql+" order by j.job_number,o.ORG_SN,a.USER_REALNAME",(int)offset,maxPagesize);
		
			UserJobs ujl = null;
			List users = new ArrayList();
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				ujl = new UserJobs();
				ujl.setUserId(new Integer(dbUtil.getInt(i,"user_id")));
				ujl.setUserName(dbUtil.getString(i,"user_name").trim());
				ujl.setUserRealname(dbUtil.getString(i,"USER_REALNAME").trim());
				ujl.setUserMobiletel1(dbUtil.getString(i,"USER_MOBILETEL1").trim());
				ujl.setOrgName(dbUtil.getString(i,"ORG_NAME").trim());
				ujl.setJobName(dbUtil.getString(i,"JOB_NAME").trim());
				users.add(ujl);
				
			}
			listInfo.setDatas(users);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
}
