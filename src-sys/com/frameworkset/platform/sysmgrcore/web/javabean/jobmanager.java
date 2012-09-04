package com.frameworkset.platform.sysmgrcore.web.javabean;

import java.io.Serializable;

import com.frameworkset.platform.sysmgrcore.control.DataControl;
import com.frameworkset.platform.sysmgrcore.control.Parameter;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.db.JobManagerImpl;

public class jobmanager implements Serializable {//添加岗位
	public String Addjob(String jobname,String jobdesc){
		 String s="0";
		 DataControl cb = DataControl
	     .getInstance(DataControl.CONTROL_INSTANCE_DB);
		 try {
	            Job job = new Job();
	            job.setJobName(jobname);
	            job.setJobDesc(jobdesc);
	            Parameter par = new Parameter();
	            par.setCommand(Parameter.COMMAND_STORE);
	            par.setObject(job);
	            cb.execute(par);
	            s="1";

	         
	        } catch (Exception e) {
	        	s="0";
	            e.printStackTrace();
	        }
	        return s;
	}
	public void Updatejob(String jobname,String jobdesc){//修改岗位
		JobManagerImpl jobMgr = new JobManagerImpl();
		Job job = new Job();
		job.setJobName(Thread.currentThread().getName() +jobname);
		job.setJobDesc(Thread.currentThread().getName() +jobdesc);
		try {
			jobMgr.storeJob(job);
		} catch (ManagerException e) {
		
			e.printStackTrace();
		}
    }
	public void deletejob(String jobid){ //删除岗位
		 JobManagerImpl jobMgr = new JobManagerImpl();
	    try {
			jobMgr.deleteJob(jobid);
		} catch (ManagerException e) {
			e.printStackTrace();
		}
	}
}
