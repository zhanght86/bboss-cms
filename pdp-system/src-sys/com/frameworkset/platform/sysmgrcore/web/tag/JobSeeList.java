package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.JobSee;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class JobSeeList  extends DataInfoImpl implements Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		String jobId = request.getParameter("jobId");
		try {
			String userName = request.getParameter("userName");
			String orgname = request.getParameter("orgname");
			String jobname = request.getParameter("jobname");

			StringBuffer hsql = new StringBuffer("select a.USER_REALNAME,a.USER_NAME,b.job_name,d.org_name," +
					"c.JOB_STARTTIME,c.JOB_FETTLE from td_sm_user a,td_sm_job b," +
					"td_sm_userjoborg c ,TD_SM_ORGANIZATION d where a.user_id = c.user_id and " +
					"b.job_id = c.job_id and d.org_id = c.org_id and c.job_id='"+ jobId +"'");
			
			if (userName != null && userName.length() > 0) {
				hsql.append(" and USER_REALNAME  like '%" + userName + "%' ");
			}
			if (orgname != null && orgname.length() > 0) {
				hsql.append(" and ORG_NAME like '%" + orgname
								+ "%'");
			}
			if (jobname != null && jobname.length() > 0) {
				hsql.append(" and JOB_NAME like '%" + jobname
								+ "%'");
			}
			hsql.append(" order by d.ORG_SN asc,c.JOB_STARTTIME asc");
			//System.out.println(hsql.toString());
			dbUtil.executeSelect(hsql.toString(),(int)offset,maxPagesize);
			JobSee js = null;
			List users = new ArrayList();
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				js = new JobSee();
				js.setUserRealname(dbUtil.getString(i,"USER_REALNAME"));
				js.setUserName(dbUtil.getString(i,"USER_NAME"));
				js.setJobName(dbUtil.getString(i,"job_name"));
				js.setOrgName(dbUtil.getString(i,"org_name"));
				js.setJobStartTime(dbUtil.getTimestamp(i,"JOB_STARTTIME"));
				js.setFettle(dbUtil.getInt(i,"JOB_FETTLE"));
				users.add(js);
				
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

