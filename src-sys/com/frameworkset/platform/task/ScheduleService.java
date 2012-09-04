package com.frameworkset.platform.task;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import com.frameworkset.platform.config.model.ScheduleServiceInfo;
import com.frameworkset.platform.config.model.SchedulejobInfo;



public abstract class ScheduleService implements Serializable{	
	protected ScheduleServiceInfo scheduleServiceInfo;
	
	public void init(ScheduleServiceInfo scheduleServiceInfo)
	{
		this.scheduleServiceInfo = scheduleServiceInfo;
	}
	
	/**
	 * 装载任务项的
	 * @param scheduler
	 * @throws ScheduleServiceException
	 */
	public abstract void startService(Scheduler scheduler) throws ScheduleServiceException;
	public void startupConfigedService(Scheduler scheduler)
	{
		List list  = this.scheduleServiceInfo.getJobs();
		for(int i = 0; list != null && i < list.size(); i ++)
		{
			SchedulejobInfo jobInfo = (SchedulejobInfo)list.get(i);
			if(!jobInfo.isUsed())
				continue;
			JobDetail jobDetail = new JobDetail(jobInfo.getId(),
                    //Scheduler.DEFAULT_GROUP,
					scheduleServiceInfo.getId(),
                    ExecuteJOB.class);
			
			try {
				JobDataMap map = new JobDataMap();
				Execute instance = (Execute)Class.forName(jobInfo.getClazz() ).newInstance();
				map.put("action",instance);
				map.put("parameters",jobInfo.getParameters());
				jobDetail.setJobDataMap(map);
				//jobDetail.se
				CronTrigger trigger = new CronTrigger(jobInfo.getId(), scheduleServiceInfo.getId());
				
				trigger.setCronExpression(jobInfo.getCronb_time());
				scheduler.scheduleJob(jobDetail,trigger);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			
			catch (ParseException ex1) {
	            ex1.printStackTrace();
	            continue;
	        } catch (SchedulerException ex) {
	           ex.printStackTrace();
	           continue;
	        }
		} 
	}
	
	public  abstract void startExecuteJob(Scheduler scheduler,SchedulejobInfo jobInfo);
	public  abstract void updateJob(Scheduler scheduler,SchedulejobInfo jobInfo);
	public  abstract void updateTriger(Scheduler scheduler,SchedulejobInfo jobInfo);
    public  abstract void updateJobAndTriger(Scheduler scheduler, SchedulejobInfo jobInfo);
	public boolean isExist(Scheduler scheduler,String groupid,String jobid)
	{
		try {
			Object obj = scheduler.getJobDetail(jobid,groupid);
			if(obj != null)
				return true;
			else
				return false;
		} catch (SchedulerException e) {
			e.printStackTrace();			
			return false;
		}
		
	}
}
