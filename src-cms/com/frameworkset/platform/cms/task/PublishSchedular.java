//Source file: D:\\workspace\\cms\\src\\com\\frameworkset\\platform\\cms\\task\\PublishSchedular.java

package com.frameworkset.platform.cms.task;

import java.util.Date;


/**
 * 计划发布（暂不实现）
 */
public class PublishSchedular implements java.io.Serializable
{
    
    /**
     * 每天发布
     * 每周发布
     * 每月发布
     */
    private int schedulerType;
    
    /**
     * 发布计划开始时间,
     * 如果是每天发布,记录发布日期
     * 如果是按周发布,记录发布的周日期,用逗号分隔多个日期
     */
    private Date startTime;
    
    /**
     * 发布次数
     */
    private int publishTimes;
    
    /**
     * 发布时间间隔
     */
    private int publishInterval;
    
    /**
     * 计划结束时间
     */
    private Date endTime;
    
    public PublishSchedular() 
    {
     
    }
    
    /**
     * Access method for the schedulerType property.
     * 
     * @return   the current value of the schedulerType property
     */
    public int getSchedulerType() 
    {
      return schedulerType;     
    }
    
    /**
     * Sets the value of the schedulerType property.
     * 
     * @param aSchedulerType the new value of the schedulerType property
     */
    public void setSchedulerType(int aSchedulerType) 
    {
      schedulerType = aSchedulerType;     
    }
    
    /**
     * Access method for the startTime property.
     * 
     * @return   the current value of the startTime property
     */
    public Date getStartTime() 
    {
      return startTime;     
    }
    
    /**
     * Sets the value of the startTime property.
     * 
     * @param aStartTime the new value of the startTime property
     */
    public void setStartTime(Date aStartTime) 
    {
      startTime = aStartTime;     
    }
    
    /**
     * Access method for the publishTimes property.
     * 
     * @return   the current value of the publishTimes property
     */
    public int getPublishTimes() 
    {
      return publishTimes;     
    }
    
    /**
     * Sets the value of the publishTimes property.
     * 
     * @param aPublishTimes the new value of the publishTimes property
     */
    public void setPublishTimes(int aPublishTimes) 
    {
      publishTimes = aPublishTimes;     
    }
    
    /**
     * Access method for the publishInterval property.
     * 
     * @return   the current value of the publishInterval property
     */
    public int getPublishInterval() 
    {
      return publishInterval;     
    }
    
    /**
     * Sets the value of the publishInterval property.
     * 
     * @param aPublishInterval the new value of the publishInterval property
     */
    public void setPublishInterval(int aPublishInterval) 
    {
      publishInterval = aPublishInterval;     
    }
    
    /**
     * Access method for the endTime property.
     * 
     * @return   the current value of the endTime property
     */
    public Date getEndTime() 
    {
      return endTime;     
    }
    
    /**
     * Sets the value of the endTime property.
     * 
     * @param aEndTime the new value of the endTime property
     */
    public void setEndTime(Date aEndTime) 
    {
      endTime = aEndTime;     
    }
}
