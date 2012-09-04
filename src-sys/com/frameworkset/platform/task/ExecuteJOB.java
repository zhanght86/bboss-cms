package com.frameworkset.platform.task;

import java.io.Serializable;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * <p>Title: ExecuteJOB</p>
 *
 * <p>Description:
 * 	日期控件工作实体 
 * </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class ExecuteJOB implements Job, Serializable{


    /**
     * execute
     *
     * @param jobExecutionContext JobExecutionContext
     * @throws JobExecutionException    
     */
    public void execute(JobExecutionContext jobExecutionContext) throws
            JobExecutionException {
        JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
        Execute action = (Execute)data.get("action");
        Map parameters = (Map)data.get("parameters");
        action.execute(parameters);
    }
}
