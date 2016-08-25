package com.frameworkset.platform.sysmgrcore.purviewmanager.menu;

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

public class ResJobTree extends COMTree implements Serializable{

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
			String resTypeId=request.getParameter("resTypeId");
			String roleId = request.getParameter("currRoleId");
			String roleTypeId = request.getParameter("role_type");
			String currOrgId = request.getParameter("currOrgId");
	        
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
	                String jobId = job.getJobId();
	                String jobName = job.getJobName();
	                map.put("jobId", jobId);
	                //res_id:restype_id:res_name
	                String ckeckVal = jobId+"#"+resTypeId+"#"+jobName;
	              //已经授权的复选框显示选中状态
                    String ms = ((AccessControl)accessControl).getSourceUserRes_jobRoleandRoleandSelf(currOrgId,roleId,jobName,resTypeId,jobId,"jobset");
                    //System.out.println("ms = " + ms);
                    if(!"".equals(ms) && ms != null){
                    	ms = "-->资源来源：" + ms;
                    }
                    if(AccessControl.hasGrantedRole(roleId,roleTypeId,jobId,resTypeId)){
        				map.put("node_checkboxchecked",new Boolean(true));
        			}
//	                String nodeType = "";
//	                if(AccessControl.hasGrantedRole(roleId,roleTypeId,jobId,resTypeId)){
//                    	nodeType = "job_true";
//                    }else{
//                    	nodeType = "job";
//                    }
					map.put("resId", jobId);
					map.put("resName", jobName);
	                if(super.accessControl.checkPermission(jobId,
							AccessControl.WRITE_PERMISSION,
							AccessControl.JOB_RESOURCE))
	                {
	                	addNode(father, jobId, (jobName+ms).trim(), "job", true, curLevel,
								(String) null, (String) null, ckeckVal,
								map);	                	
	                }
	                else if(super.accessControl.checkPermission(jobId,
							AccessControl.READ_PERMISSION,
							AccessControl.JOB_RESOURCE))
	                {
	                	addNode(father, jobId, jobName.trim(), "job", false, curLevel,
								(String) null, (String) null, ckeckVal,
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

