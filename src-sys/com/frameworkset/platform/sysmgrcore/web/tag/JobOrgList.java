package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import org.frameworkset.spi.SPIException;

import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.JobManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class JobOrgList extends DataInfoImpl implements Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		String jobId = request.getParameter("jobId");
		String orgnumber = request.getParameter("orgnumber");
		String orgName = request.getParameter("orgName");
		ListInfo listInfo = new ListInfo();
		try {
			JobManager jobManager = SecurityDatabase.getJobManager();
			//listInfo = jobManager.getJobOrgList(jobId, orgnumber, orgName, offset, maxPagesize);
			listInfo = jobManager.getJobOrgShowList(jobId, orgnumber, orgName, offset, maxPagesize);
		} catch (SPIException e) {
			e.printStackTrace();
		} catch (ManagerException e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}

}
