package com.frameworkset.platform.task;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.config.model.ScheduleServiceInfo;
import com.frameworkset.platform.config.model.SchedulejobInfo;

public class TaskService implements Service
{
    private static Logger log = Logger.getLogger(ScheduleService.class);

    private static SchedulerFactory factory = new StdSchedulerFactory();

    private static Scheduler scheduler = null;

    private static TaskService taskService = null;

    private static boolean started = false;

    private TaskService()
    {

    }

    static
    {
        try
        {
            if (ConfigManager.getInstance().isUseTaskservice())
            {
                scheduler = factory.getScheduler();
                scheduler.start();
                log.debug("Scheduler started.");
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
            log.error("Scheduler failed:" + ex);
        }
        try
        {
            // use reflection and catch the Exception to allow PoolMan to work
            // with 1.2 VM's
            Class r = Runtime.getRuntime().getClass();
            java.lang.reflect.Method m = r.getDeclaredMethod("addShutdownHook",
                    new Class[] { Thread.class });
            m.invoke(Runtime.getRuntime(),
                    new Object[] { new ShutdownThread() });
        } catch (Exception e)
        {
            log.error("Add shutdownHook error:" + e);
        }

    }

    static class ShutdownThread extends Thread
    {
        public void run()
        {
            if (scheduler != null)
                try
                {
                    scheduler.shutdown();
                } catch (SchedulerException ex)
                {
                    ex.printStackTrace();
                }
        }
    }

    public static TaskService getTaskService()
    {
        if (taskService == null)
            taskService = new TaskService();
        return taskService;
    }

    public void startService()
    {

        if (scheduler == null)
        {
            log.debug("没有启动计划执行引擎，启动任务服务失败!");
            return;

        }
        if (ConfigManager.getInstance().isUseTaskservice() && !started)
        {
            List scheduleServices = ConfigManager.getInstance()
                    .getScheduleServices();
            for (int i = 0; i < scheduleServices.size(); i++)
            {
                ScheduleServiceInfo scheduleServiceInfo = (ScheduleServiceInfo) scheduleServices
                        .get(i);
                if (!scheduleServiceInfo.isUsed())
                    continue;
                String clazz = scheduleServiceInfo.getClazz();
                try
                {
                    ScheduleService instance = (ScheduleService) Class.forName(
                            clazz).newInstance();
                    instance.init(scheduleServiceInfo);
                    instance.startService(scheduler);
                    instance.startupConfigedService(scheduler);
                } catch (InstantiationException e)
                {
                    e.printStackTrace();
                    continue;
                } catch (IllegalAccessException e)
                {

                    e.printStackTrace();
                    continue;
                } catch (ClassNotFoundException e)
                {

                    e.printStackTrace();
                    continue;
                } catch (ScheduleServiceException e)
                {
                    e.printStackTrace();
                    continue;
                }

            }
            started = true;
        }

    }

    /**
     * 启动一个任务
     * 
     * @param jobInfo
     */
    public void startExecuteJob(String groupid, SchedulejobInfo jobInfo)
    {
        if (!started)
            startService();
        if (scheduler == null)
        {
            log.debug("没有启动计划执行引擎服务，启动任务服务失败!");
            return;

        }
        ScheduleServiceInfo scheduleServiceInfo = ConfigManager.getInstance()
                .getScheduleServiceInfo(groupid);
        if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed())
        {
            jobInfo.setParent(scheduleServiceInfo);
            String clazz = scheduleServiceInfo.getClazz();
            try
            {
                ScheduleService instance = (ScheduleService) Class.forName(
                        clazz).newInstance();
                instance.init(scheduleServiceInfo);
                instance.startExecuteJob(scheduler, jobInfo);
            } catch (InstantiationException e)
            {
                e.printStackTrace();

            } catch (IllegalAccessException e)
            {

                e.printStackTrace();

            } catch (ClassNotFoundException e)
            {

                e.printStackTrace();

            }
        }
    }

    /**
     * 更新一个任务项
     * 
     * @param jobInfo
     */
    public void updateExecuteJob(String groupid, SchedulejobInfo jobInfo)
    {
//        Schedular sch = (Schedular) jobInfo;
        if (!started)
            startService();
        if (scheduler == null)
        {
            log.debug("没有启动计划执行引擎，启动任务服务失败!");
            return;

        }
        ScheduleServiceInfo scheduleServiceInfo = ConfigManager.getInstance()
                .getScheduleServiceInfo(groupid);
        if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed())
        {
            jobInfo.setParent(scheduleServiceInfo);
            String clazz = scheduleServiceInfo.getClazz();
            try
            {
                ScheduleService instance = (ScheduleService) Class.forName(
                        clazz).newInstance();
                instance.init(scheduleServiceInfo);
                //scheduler.deleteJob(sch.getSchedularID()+"",groupid);
                instance.updateJobAndTriger(scheduler, jobInfo);
            } catch (InstantiationException e)
            {
                e.printStackTrace();

            } catch (IllegalAccessException e)
            {

                e.printStackTrace();

            } catch (ClassNotFoundException e)
            {

                e.printStackTrace();

            } 
//            catch (SchedulerException e)
//            {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
        }
    }

    public void restartService()
    {
        // TODO Auto-generated method stub

    }

    /**
     * 恢复所有触发器
     * 
     */
    public void resumeAll()
    {
        if (scheduler == null)
        {
            log.debug("没有启动计划执行引擎，启动任务服务失败!");
            return;

        }
        try
        {
            scheduler.resumeAll();
        } catch (SchedulerException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 恢复一个任务
     * 
     * @param name
     * @param groupid
     */
    public void resumeJob(String name, String groupid)
    {
        if (scheduler == null)
        {
            log.debug("没有启动计划执行引擎，启动任务服务失败!");
            return;

        }

        ScheduleServiceInfo scheduleServiceInfo = ConfigManager.getInstance()
                .getScheduleServiceInfo(groupid);
        if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed())
        {
            try
            {
                scheduler.resumeJob(name, groupid);
            } catch (SchedulerException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 暂停所有触发器
     * 
     */
    public void pauseAll()
    {
        if (scheduler == null)
        {
            log.debug("没有启动计划执行引擎，启动任务服务失败!");
            return;

        }
        try
        {
            scheduler.pauseAll();
        } catch (SchedulerException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 删除一个任务
     * 
     * @param jobname
     * @param groupid
     */
    public void deleteJob(String jobname, String groupid)
    {

        if (scheduler == null)
        {
            log.debug("没有启动计划执行引擎，启动任务服务失败!");
            return;

        }
        ScheduleServiceInfo scheduleServiceInfo = ConfigManager.getInstance()
                .getScheduleServiceInfo(groupid);
        if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed())
        {
            try
            {
                // scheduler.unscheduleJob(jobname, groupid);
                scheduler.deleteJob(jobname, groupid);
            } catch (SchedulerException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 暂停一个任务
     * 
     * @param jobname
     * @param groupid
     */
    public void pauseJob(String jobname, String groupid)
    {

        if (scheduler == null)
        {
            log.debug("没有启动计划执行引擎，启动任务服务失败!");
            return;

        }
        ScheduleServiceInfo scheduleServiceInfo = ConfigManager.getInstance()
                .getScheduleServiceInfo(groupid);
        if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed())
        {
            try
            {
                scheduler.pauseJob(jobname, groupid);
            } catch (SchedulerException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消一个触发器
     * 
     * @param triggerName
     * @param triggerGroup
     */
    public void unscheduleJob(String triggerName, String triggerGroup)
    {
        if (scheduler == null)
        {
            log.debug("没有启动计划执行引擎服务，启动任务服务失败!");
            return;

        }
        ScheduleServiceInfo scheduleServiceInfo = ConfigManager.getInstance()
                .getScheduleServiceInfo(triggerGroup);
        if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed())
        {
            try
            {
                scheduler.unscheduleJob(triggerName, triggerGroup);
            } catch (SchedulerException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 终止正在执行的触发器
     * 
     */
    public void standbyScheduler()
    {
        if (scheduler == null)
        {
            log.debug("没有启动计划执行引擎服务，启动任务服务失败!");
            return;

        }
        try
        {
            scheduler.standby();
        } catch (SchedulerException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 立刻执行一次触发器
     * 
     * @param jobName
     * @param groupName
     */
    public void triggerJob(String jobName, String groupName)
    {
        if (scheduler == null)
        {
            log.debug("没有启动计划执行引擎服务，启动任务服务失败!");
            return;

        }
        ScheduleServiceInfo scheduleServiceInfo = ConfigManager.getInstance()
                .getScheduleServiceInfo(groupName);
        if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed())
        {
            try
            {
                scheduler.triggerJob(jobName, groupName);
            } catch (SchedulerException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 按照传过来的参数立刻执行一次触发器
     * 
     * @param jobName
     * @param groupName
     * @param parameters
     */
    public void triggerJob(String jobName, String groupName, Map parameters)
    {
        if (scheduler == null)
        {
            log.debug("没有启动计划执行引擎服务，启动任务服务失败!");
            return;

        }
        ScheduleServiceInfo scheduleServiceInfo = ConfigManager.getInstance()
                .getScheduleServiceInfo(groupName);
        if (scheduleServiceInfo != null && scheduleServiceInfo.isUsed())
        {
            try
            {
                JobDetail jobdetail = scheduler
                        .getJobDetail(jobName, groupName);
                Map parameters_ = (Map) jobdetail.getJobDataMap().get(
                        "parameters");
                Set entrySet = parameters.entrySet();

                for (Iterator it = entrySet.iterator(); it.hasNext();)
                {
                    Map.Entry entry = (Map.Entry) it.next();
                    parameters_.put(entry.getKey(), entry.getValue());

                }
                scheduler.triggerJob(jobName, groupName, jobdetail
                        .getJobDataMap());

            } catch (SchedulerException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
