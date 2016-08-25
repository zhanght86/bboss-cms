package com.frameworkset.platform.sysmgrcore.purviewmanager.action;

import java.util.ArrayList;
import java.util.List;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.JobManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.util.ListInfo;

public class JobControler {
	public String selectjoblist(@PagerParam(name = PagerParam.SORT, defaultvalue = "monitorTime") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "true") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,String jobname,String jobnubmer,ModelMap model) throws ManagerException
	{
		JobManager jobManager = SecurityDatabase.getJobManager();
		if(jobname!=null&&jobname.isEmpty()){
			jobname=null;
		}
		ListInfo datas = jobManager.selectjoblist(jobname, jobnubmer, offset, pagesize);
		AccessControl accessControl = AccessControl.getAccessControl();
		if(datas!=null&&datas.getDatas()!=null){
			List<Job> jobs = datas.getDatas();
			List<Job> list = new ArrayList<Job>();
			for(Job job:jobs){
				if(accessControl.checkPermission(job.getJobId(), "jobset", AccessControl.JOB_RESOURCE))
				{
					list.add(job);
				}
			}
			datas.setDatas(list);
		}
		model.addAttribute("datas",datas);
		return "path:selectjoblist";
	}
}
