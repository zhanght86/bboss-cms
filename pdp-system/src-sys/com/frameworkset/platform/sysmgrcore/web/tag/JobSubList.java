package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class JobSubList extends DataInfoImpl implements Serializable {
	private Logger logger = Logger.getLogger(JobSubList.class.getName());

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		String jobId = request.getParameter("jobId");
		
		try {
//			OrgManager orgManager = SecurityDatabase.getOrgManager();
			Job job = new Job();
			job.setJobId(jobId);

//			List list = null;
//			PageConfig pageConfig = orgManager.getPageConfig();
//			pageConfig.setPageSize(maxPagesize);
//			pageConfig.setStartIndex((int) offset);

			listInfo = getOrgList(job,offset,maxPagesize);// 根据岗位得机构列表
			
//			listInfo.setTotalSize(pageConfig.getTotalSize());
//			listInfo.setDatas(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
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
	
	private ListInfo getOrgList(Job job ,long offset,int maxPagesize) throws ManagerException {
		ListInfo listinfo = new ListInfo();
		List list = null;
		DBUtil db = new DBUtil();
		StringBuffer sql = new StringBuffer() 
			.append("select * from td_sm_organization where org_id in(select org_id from ")
			.append("td_sm_orgjob where job_id='").append(job.getJobId()).append("')")
			.append(" order by ORG_SN asc");
		try {
			db.executeSelect(sql.toString(),offset,maxPagesize);
			list = new OrgManagerImpl().dbutilToOrganziationList(db);
			listinfo.setTotalSize(db.getTotalSize());
			listinfo.setDatas(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listinfo;
	}
}
