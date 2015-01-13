package com.frameworkset.platform.sysmgrcore.web.struts.form;

import java.io.Serializable;

public class SchedularForm  implements Serializable
{

    private int schedularID;

    private int plannerID;

    private int executorID;

    private String plannerName;

    private String plannerRealName;

    private String executors;

    private String date1;

    private String weekday;

    /**
     * 表示此列预约日程的所有执行人。
     */
    private String partner;

    private int requestID;

    /**
     * 日程主题
     */
    private String topic;

    /**
     * 日程地点
     */
    private String place;

    private String content;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 日程类型
     */
    private String type;

    /**
     * 是否公事 1表示公事，0表示私事
     */
    private int isPublicAffair;

    /**
     * 重要性
     */
    private String essentiality;

    /**
     * 是否历史日程 0表示是不是历史日程，1表示历史日程
     */
    private int isHistory;

    /**
     * 是否空闲，1表示空闲，0表示忙。
     */
    private int isLeisure;

    /**
     * 是否公开，0表示不公开，1表示公开
     */
    private int isOpen;

    private int status;

    /**
     * 审核建议
     */
    private String advice;

    /**
     * 审核人
     */
    private String ratifierID;

    private String remindBeginTime;

    private String remindEndTime;

    private double interval;

    private int intervalType;

    private int isSys;

    private int isEmail;

    private int isMessage;

    public String getAdvice()
    {
        return advice;
    }

    public void setAdvice(String advice)
    {
        this.advice = advice;
    }

    public String getBeginTime()
    {
        return beginTime;
    }

    public void setBeginTime(String beginTime)
    {
        this.beginTime = beginTime;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getEssentiality()
    {
        return essentiality;
    }

    public void setEssentiality(String essentiality)
    {
        this.essentiality = essentiality;
    }

    public int getExecutorID()
    {
        return executorID;
    }

    public void setExecutorID(int executorID)
    {
        this.executorID = executorID;
    }

    public int getIsHistory()
    {
        return isHistory;
    }

    public void setIsHistory(int isHistory)
    {
        this.isHistory = isHistory;
    }

    public int getIsLeisure()
    {
        return isLeisure;
    }

    public void setIsLeisure(int isLeisure)
    {
        this.isLeisure = isLeisure;
    }

    public int getIsPublicAffair()
    {
        return isPublicAffair;
    }

    public void setIsPublicAffair(int isPublicAffair)
    {
        this.isPublicAffair = isPublicAffair;
    }

    public String getPlace()
    {
        return place;
    }

    public void setPlace(String place)
    {
        this.place = place;
    }

    public int getPlannerID()
    {
        return plannerID;
    }

    public void setPlannerID(int plannerID)
    {
        this.plannerID = plannerID;
    }

    public int getRequestID()
    {
        return requestID;
    }

    public void setRequestID(int requestID)
    {
        this.requestID = requestID;
    }

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getSchedularID()
    {
        return schedularID;
    }

    public void setSchedularID(int schedularID)
    {
        this.schedularID = schedularID;
    }

    

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getRatifierID()
    {
        return ratifierID;
    }

    public void setRatifierID(String ratifierID)
    {
        this.ratifierID = ratifierID;
    }

    public String getExecutors()
    {
        return executors;
    }

    public void setExecutors(String executors)
    {
        this.executors = executors;
    }

    public String getDate1()
    {
        return date1;
    }

    public void setDate1(String date1)
    {
        this.date1 = date1;
    }

    public String getWeekday()
    {
        return weekday;
    }

    public void setWeekday(String weekday)
    {
        this.weekday = weekday;
    }

    public double getInterval()
    {
        return interval;
    }

    public void setInterval(double interval)
    {
        this.interval = interval;
    }

    public int getIsEmail()
    {
        return isEmail;
    }

    public void setIsEmail(int isEmail)
    {
        this.isEmail = isEmail;
    }

    public int getIsMessage()
    {
        return isMessage;
    }

    public void setIsMessage(int isMessage)
    {
        this.isMessage = isMessage;
    }

    public int getIsSys()
    {
        return isSys;
    }

    public void setIsSys(int isSys)
    {
        this.isSys = isSys;
    }

    public String getRemindBeginTime()
    {
        return remindBeginTime;
    }

    public void setRemindBeginTime(String remindBeginTime)
    {
        this.remindBeginTime = remindBeginTime;
    }

    public String getRemindEndTime()
    {
        return remindEndTime;
    }

    public void setRemindEndTime(String remindEndTime)
    {
        this.remindEndTime = remindEndTime;
    }

    public int getIntervalType()
    {
        return intervalType;
    }

    public void setIntervalType(int intervalType)
    {
        this.intervalType = intervalType;
    }

    public String getPlannerName()
    {
        return plannerName;
    }

    public void setPlannerName(String plannerName)
    {
        this.plannerName = plannerName;
    }

    public int getIsOpen()
    {
        return isOpen;
    }

    public void setIsOpen(int isOpen)
    {
        this.isOpen = isOpen;
    }

    public String getPlannerRealName()
    {
        return plannerRealName;
    }

    public void setPlannerRealName(String plannerRealName)
    {
        this.plannerRealName = plannerRealName;
    }

    public String getPartner()
    {
        return partner;
    }

    public void setPartner(String partner)
    {
        this.partner = partner;
    }

} 
