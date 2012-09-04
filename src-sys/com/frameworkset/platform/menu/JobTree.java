package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.manager.JobManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class JobTree extends COMTree implements Serializable{

	    public boolean hasSon(ITreeNode father) {
//	        String treeID = father.getId();
//	        try {	            	            
	            if(father.isRoot())
	            {
//	            	JobManager jobManager;	    	        
//	    	        jobManager = SecurityDatabase.getJobManager();
////	            	List joblist = jobManager.getJobList();
//	            	if(joblist.size() > 1)
//	            	{
	            		return true;
//	            	}
//	            	return false;
	            }
//	            
//	            
//	        } catch (Exception e) {
//	            // TODO Auto-generated catch block
//	            e.printStackTrace();
//	        }
//
	        return false;
	    }

	    public boolean setSon(ITreeNode father, int curLevel) {
	        //String treeID = father.getId();
	        JobManager jobManager;
	        
	        try {
	            jobManager = SecurityDatabase.getJobManager();
	           
	            List joblist = jobManager.getJobList("select * from td_sm_Job j order by j.JOB_NUMBER asc");
	            if(joblist!=null){
	            Iterator iterator = joblist.iterator();
	            while (iterator.hasNext()) {
	                Job job = (Job) iterator.next();      
	                if(job.getJobId().equals("1"))
	                	continue;
	                Map map = new HashMap();
	                map.put("jobId", job.getJobId());
	                if(super.accessControl.checkPermission(job.getJobId(),
							AccessControl.WRITE_PERMISSION,
							AccessControl.JOB_RESOURCE))
	                {
	                	addNode(father, job.getJobId(), job.getJobName(), "job", true, curLevel,
								(String) null, (String) null, (String) null,
								map);
	                }
	                else if(super.accessControl.checkPermission(job.getJobId(),
							AccessControl.READ_PERMISSION,
							AccessControl.JOB_RESOURCE))
	                {
	                	addNode(father, job.getJobId(), job.getJobName(), "job", false, curLevel,
								(String) null, (String) null, (String) null,
								map);
	                }
	            }
	          
	            }
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return true;
	    }
}
