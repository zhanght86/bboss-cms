//Source file: D:\\WorkSpace\\console\\src\\com\\frameworkset\\platform\\schedularmanage\\MeetingTaskNotic.java

package com.frameworkset.platform.schedularmanage;

import java.io.Serializable;
import java.util.Date;

/**
 * 会议和任务通知
 */
public class Notic implements Serializable
{
    /**
     * 通知id
     */
    private int noticID;

    /**
     * 内容
     */
    private String content;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 地点
     */
    private String place;

    /**
     * 执行人id
     */
    private int executorID;

    /**
     * 通知状态
     * 
     */
    private int status;

    /**
     * 通知主题
     * 
     */
    private String topic;

    /**
     * 通知来源
     * 
     */
    private String source;

    /**
     * 通知安排人ID
     * 
     */
    private int noticPlannerID;

    /**
     * 通知安排人名称
     * 
     */
    private String noticPlannerName;

    /**
     * 通知安排人真实名称
     * 
     */
    private String noticPlannerRealName;

    public Notic()
    {

    }

    /**
     * Access method for the content property.
     * 
     * @return the current value of the content property
     */
    public String getContent()
    {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param aContent
     *            the new value of the content property
     */
    public void setContent(String aContent)
    {
        content = aContent;
    }

    /**
     * Access method for the beginTime property.
     * 
     * @return the current value of the beginTime property
     */
    public Date getBeginTime()
    {
        return beginTime;
    }

    /**
     * Sets the value of the beginTime property.
     * 
     * @param aBeginTime
     *            the new value of the beginTime property
     */
    public void setBeginTime(Date aBeginTime)
    {
        beginTime = aBeginTime;
    }

    /**
     * Access method for the endTime property.
     * 
     * @return the current value of the endTime property
     */
    public Date getEndTime()
    {
        return endTime;
    }

    /**
     * Sets the value of the endTime property.
     * 
     * @param aEndTime
     *            the new value of the endTime property
     */
    public void setEndTime(Date aEndTime)
    {
        endTime = aEndTime;
    }

    /**
     * Access method for the place property.
     * 
     * @return the current value of the place property
     */
    public String getPlace()
    {
        return place;
    }

    /**
     * Sets the value of the place property.
     * 
     * @param aPlace
     *            the new value of the place property
     */
    public void setPlace(String aPlace)
    {
        place = aPlace;
    }

    public int getExecutorID()
    {
        return executorID;
    }

    public void setExecutorID(int executorID)
    {
        this.executorID = executorID;
    }

    public int getNoticID()
    {
        return noticID;
    }

    public void setNoticID(int noticID)
    {
        this.noticID = noticID;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public int getNoticPlannerID()
    {
        return noticPlannerID;
    }

    public void setNoticPlannerID(int noticPlannerID)
    {
        this.noticPlannerID = noticPlannerID;
    }

    public String getNoticPlannerName()
    {
        return noticPlannerName;
    }

    public void setNoticPlannerName(String noticPlannerName)
    {
        this.noticPlannerName = noticPlannerName;
    }

    public String getNoticPlannerRealName()
    {
        return noticPlannerRealName;
    }

    public void setNoticPlannerRealName(String noticPlannerRealName)
    {
        this.noticPlannerRealName = noticPlannerRealName;
    }
}
