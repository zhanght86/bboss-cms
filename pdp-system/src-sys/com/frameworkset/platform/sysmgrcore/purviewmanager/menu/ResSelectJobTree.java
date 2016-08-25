package com.frameworkset.platform.sysmgrcore.purviewmanager.menu;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.frameworkset.spi.SPIException;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.JobManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class ResSelectJobTree extends COMTree implements Serializable {

	public boolean hasSon(ITreeNode father) {
		if(father.isRoot()){
			return true;
		}
		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		try {
			JobManager jobManager = SecurityDatabase.getJobManager();
			List joblist = jobManager.getJobList("select * from td_sm_Job j order by j.JOB_NUMBER asc");
			if(joblist!=null){
				Iterator iterator = joblist.iterator();
				while(iterator.hasNext()){
					 Job job = (Job) iterator.next();    
					 String jobId = job.getJobId();
					 String jobName = job.getJobName().trim();
	                if(job.getJobId().equals("1"))
	                	continue;
	                if(super.accessControl.checkPermission(jobId,
							AccessControl.WRITE_PERMISSION,
							AccessControl.JOB_RESOURCE)
							|| super.accessControl.checkPermission(jobId,
									AccessControl.READ_PERMISSION,
									AccessControl.JOB_RESOURCE))
	                {
	                	addNode(father, jobId, jobName, "job", true, curLevel,
	                			(String) null, jobId+":"+jobName, "",
								(Map)null);	                	
	                }
				}
			}
		} catch (SPIException e) {
			e.printStackTrace();
		} catch (ManagerException e) {
			e.printStackTrace();
		}
		return false;
	}

}
