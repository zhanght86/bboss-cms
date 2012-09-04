package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class JobByUserList  extends DataInfoImpl implements Serializable {

	protected ListInfo getDataList(String sortKey, boolean desc, 
			long offset,int maxPagesize) {
		String userId = this.accessControl.getUserID();
	   	
	   	DBUtil db = new DBUtil();
	   	DBUtil db1 = new DBUtil(); 
	   	String sql = " select job.*,org.org_name,org.remark5 " 
	   		+" from td_sm_job job inner "
	   		+" join td_sm_userjoborg ujo on job.job_id = ujo.job_id "
	   		+" join td_sm_organization org on ujo.org_id = org.org_id "
	   		+" where ujo.user_id = "+userId;
	   	
	   	List jobs = new ArrayList();
	   	ListInfo listInfo = new ListInfo();
	   	try {
	   		db.executeSelect(sql,offset,maxPagesize);
	   		for (int i = 0; i < db.size(); i++) {
				Job job = new Job();
				job.setJobId(db.getString(i,"JOB_ID"));
				job.setJobName(db.getString(i,"JOB_NAME"));
				job.setJobDesc(db.getString(i,"JOB_DESC"));
				job.setJobFunction(db.getString(i,"JOB_FUNCTION"));
				job.setJobAmount(db.getString(i,"JOB_AMOUNT"));
				job.setJobNumber(db.getString(i,"REMARK5"));
				job.setJobCondition(db.getString(i,"JOB_CONDITION"));
				job.setJobRank(db.getString(i,"JOB_RANK"));
				jobs.add(job);
			}
	   		
	   		listInfo.setDatas(jobs);
	   		listInfo.setTotalSize(db.getTotalSize());
	   		
	   	} catch (Exception e) {
	   		e.printStackTrace();
	   	}
	   	
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO 自动生成方法存根
		return null;
	}

}
