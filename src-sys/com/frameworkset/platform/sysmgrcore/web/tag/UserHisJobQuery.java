package com.frameworkset.platform.sysmgrcore.web.tag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class UserHisJobQuery extends DataInfoImpl {

	protected ListInfo getDataList(String sortKey, boolean desc) {
		return null;
	}

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		
		String userId = request.getParameter("userId");
		StringBuffer sql = new StringBuffer()
//			.append("SELECT a.user_id,b.job_id, b.job_name as job_name,a.org_id,o.remark5 as remark5,")
//			.append("a.JOB_FETTLE,a.JOB_STARTTIME,a.JOB_QUASHTIME ")
//			.append("FROM td_sm_userjoborg_history a LEFT JOIN td_sm_job b ON a.job_id = b.job_id,")
//			.append("td_sm_organization o where o.org_id=a.org_id ")
//			.append(" and a.user_id=").append(userId).append("  order by a.job_quashtime desc");
//			.append("SELECT a.user_id, b.job_id, nvl(b.job_name,'未知岗位') AS job_name, a.org_id,nvl(o.remark5,'未知机构') as remark5,")
//			.append(" a.job_fettle, a.job_starttime, a.job_quashtime")
//			.append(" FROM td_sm_userjoborg_history a ")
//			.append("LEFT JOIN td_sm_job b ON a.job_id = b.job_id ")
//			.append("LEFT JOIN td_sm_organization o ON a.org_id = o.org_id where ")
//			.append("a.user_id = ").append(userId).append(" order by a.job_quashtime desc");
			.append("select * from TD_SM_USERJOBORG_HISTORY where user_id=")
			.append(userId).append(" order by job_quashtime desc ");
		return getUserHisJob(sql.toString(),offset,maxPagesize);
	}
	
	private ListInfo getUserHisJob(String sql,long offset,int maxItem){
		ListInfo listInfo = new ListInfo();
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql.toString(),offset,maxItem);
			if(db.size() > 0){
				List list = new ArrayList();
				Map record = null;
				for(int i = 0; i < db.size(); i++){
					record = new HashMap();
					record.put("jobname", db.getString(i, "job_name"));
					record.put("remark5", db.getString(i, "org_name"));
					record.put("JOB_STARTTIME", db.getDate(i, "JOB_STARTTIME"));
					record.put("JOB_QUASHTIME", db.getDate(i, "JOB_QUASHTIME"));
					record.put("jobid", db.getString(i, "job_id"));
					record.put("orgid", db.getString(i, "org_id"));
					list.add(record);
				}
				listInfo.setDatas(list);
				listInfo.setTotalSize(db.getTotalSize());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listInfo;
	}

}
