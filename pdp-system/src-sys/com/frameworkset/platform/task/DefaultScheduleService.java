package com.frameworkset.platform.task;

import java.io.Serializable;

import org.quartz.Scheduler;

import com.frameworkset.platform.config.model.SchedulejobInfo;

/**
 * 缺省的任务调度器
 * <p>Title: DefaultScheduleService</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2008-2-28 11:37:17
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultScheduleService extends ScheduleService implements Serializable{

	public void startService(Scheduler scheduler)
			throws ScheduleServiceException {
		// TODO Auto-generated method stub

	}

	public void startExecuteJob(Scheduler scheduler, SchedulejobInfo jobInfo) {
		// TODO Auto-generated method stub

	}

	public void updateJob(Scheduler scheduler, SchedulejobInfo jobInfo) {
		// TODO Auto-generated method stub

	}

	public void updateTriger(Scheduler scheduler, SchedulejobInfo jobInfo) {
		// TODO Auto-generated method stub

	}

	public void updateJobAndTriger(Scheduler scheduler, SchedulejobInfo jobInfo) {
		// TODO Auto-generated method stub

	}

}
