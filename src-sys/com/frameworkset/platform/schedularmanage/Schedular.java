//Source file: D:\\WorkSpace\\console\\src\\com\\frameworkset\\platform\\schedularmanage\\Schedular.java

package com.frameworkset.platform.schedularmanage;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.frameworkset.platform.config.model.SchedulejobInfo;

/**
 * 日程
 */
public class Schedular extends SchedulejobInfo
							implements Serializable
{
    /**
     * 日程编号
     */
    private int schedularID;

    /**
      * 日程安排人ID
     */
    private int plannerID;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 表示此列预约日程的所有执行人。
     */
    private String partner;

    /**
     * 用户真实姓名
     */
    private String userRealName;

    /**
     * 执行人ID
     */
    private int executorID;

    /**
     * 请求ID
     */
    private int requestID;

    /**
     * 日程主题
     */
    private String topic;

    /**
     * 日程地点
     */
    private String place;

    /**
     * 日程内容
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
     * 日程类型
     */
    private String type;
    
    private String typeDesc;
    
    private String url;

    /**
     * 是否公事
     */
    private int isPublicAffair;

    /**
     * 重要性
     */
    private String essentiality;

    /**
     * 是否历史日程
     */
    private int isHistory;

    /**
     * 日程状态
     */
    private int status;

    /**
     * 是否空闲
     */
    private int isLeisure;

//    /**
//     * 是否重复
//     */
//    private int isCycle;
//
//    /**
//     * 重复周期
//     */
//    private long cycleInterval;
//
//    /**
//     * 重复开始时间，指出在某个时间段内该日程重复。
//     */
//    private String beginCycleTime;
//
//    /**
//     * 重复结束时间。指出在某个时间段内该日程重复。
//     */
//    private String endCycleTime;
//
//    /**
//     * 重复次数
//     */
//    private int cycleTimes;
//
    /**
     * 提醒开始时间
     */
    private Date remindBeginTime;

    /**
     * 提醒结束时间
     */
    private Date remindEndTime;

    /**
     * 提醒时间间隔
     */
    private double interval;

    /**
     * 提醒时间间隔单位，有分(0)、时(1)、天(2)、周(3)。
     */
    private int intervalType;

    /**
     * 是否采用系统提醒
     */
    private int isSys;

    /**
     * 是否采用Email提醒
     */
    private int isEmail;

    /**
     * 是否采用短信提醒
     */
    private int isMessage;

    /**
     * 日程安排时间
     */
    private String assignmentTime;

    /**
     * 日程共享人
     * 
     */
    private List viewers;

    /**
     * 
     */
    private List ratifyAdvice;

    /**
     * 是否公开，1表示公开，0表示不公开
     */
    private int isOpen;

    /**
     * @roseuid 449B452802FD
     */
    public int getIsOpen()
    {
        return isOpen;
    }

    public void setIsOpen(int isOpen)
    {
        this.isOpen = isOpen;
    }

    public Schedular()
    {

    }

    /**
     * Access method for the topic property.
     * 
     * @return the current value of the topic property
     */
    public String getTopic()
    {
        return topic;
    }

    /**
     * Sets the value of the topic property.
     * 
     * @param aTopic
     *            the new value of the topic property
     */
    public void setTopic(String aTopic)
    {
        topic = aTopic;
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
     * Access method for the type property.
     * 
     * @return the current value of the type property
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param aType
     *            the new value of the type property
     */
    public void setType(String aType)
    {
        type = aType;
    }

    /**
     * Access method for the isPublicAffair property.
     * 
     * @return the current value of the isPublicAffair property
     */
    public int getIsPublicAffair()
    {
        return isPublicAffair;
    }

    /**
     * Sets the value of the isPublicAffair property.
     * 
     * @param aIsPublicAffair
     *            the new value of the isPublicAffair property
     */
    public void setIsPublicAffair(int aIsPublicAffair)
    {
        isPublicAffair = aIsPublicAffair;
    }

    /**
     * Access method for the essentiality property.
     * 
     * @return the current value of the essentiality property
     */
    public String getEssentiality()
    {
        return essentiality;
    }

    /**
     * Sets the value of the essentiality property.
     * 
     * @param aEssentiality
     *            the new value of the essentiality property
     */
    public void setEssentiality(String aEssentiality)
    {
        essentiality = aEssentiality;
    }

    /**
     * Access method for the isHistory property.
     * 
     * @return the current value of the isHistory property
     */
    public int getIsHistory()
    {
        return isHistory;
    }

    /**
     * Sets the value of the isHistory property.
     * 
     * @param aIsHistory
     *            the new value of the isHistory property
     */
    public void setIsHistory(int aIsHistory)
    {
        isHistory = aIsHistory;
    }

    /**
     * Access method for the isLeisure property.
     * 
     * @return the current value of the isLeisure property
     */
    public int getIsLeisure()
    {
        return isLeisure;
    }

    /**
     * Sets the value of the isLeisure property.
     * 
     * @param aIsLeisure
     *            the new value of the isLeisure property
     */
    public void setIsLeisure(int aIsLeisure)
    {
        isLeisure = aIsLeisure;
    }

//    /**
//     * Access method for the isCycle property.
//     * 
//     * @return the current value of the isCycle property
//     */
//    public int getIsCycle()
//    {
//        return isCycle;
//    }
//
//    /**
//     * Sets the value of the isCycle property.
//     * 
//     * @param aIsCycle
//     *            the new value of the isCycle property
//     */
//    public void setIsCycle(int aIsCycle)
//    {
//        isCycle = aIsCycle;
//    }
//
//    /**
//     * Access method for the cycleInterval property.
//     * 
//     * @return the current value of the cycleInterval property
//     */
//    public long getCycleInterval()
//    {
//        return cycleInterval;
//    }
//
//    /**
//     * Sets the value of the cycleInterval property.
//     * 
//     * @param aCycleInterval
//     *            the new value of the cycleInterval property
//     */
//    public void setCycleInterval(long aCycleInterval)
//    {
//        cycleInterval = aCycleInterval;
//    }
//
//    /**
//     * Access method for the beginCycleTime property.
//     * 
//     * @return the current value of the beginCycleTime property
//     */
//    public String getBeginCycleTime()
//    {
//        return beginCycleTime;
//    }
//
//    /**
//     * Sets the value of the beginCycleTime property.
//     * 
//     * @param aBeginCycleTime
//     *            the new value of the beginCycleTime property
//     */
//    public void setBeginCycleTime(String aBeginCycleTime)
//    {
//        beginCycleTime = aBeginCycleTime;
//    }
//
//    /**
//     * Access method for the endCycleTime property.
//     * 
//     * @return the current value of the endCycleTime property
//     */
//    public String getEndCycleTime()
//    {
//        return endCycleTime;
//    }
//
//    /**
//     * Sets the value of the endCycleTime property.
//     * 
//     * @param aEndCycleTime
//     *            the new value of the endCycleTime property
//     */
//    public void setEndCycleTime(String aEndCycleTime)
//    {
//        endCycleTime = aEndCycleTime;
//    }
//
//    /**
//     * Access method for the cycleTimes property.
//     * 
//     * @return the current value of the cycleTimes property
//     */
//    public int getCycleTimes()
//    {
//        return cycleTimes;
//    }
//
//    /**
//     * Sets the value of the cycleTimes property.
//     * 
//     * @param aCycleTimes
//     *            the new value of the cycleTimes property
//     */
//    public void setCycleTimes(int aCycleTimes)
//    {
//        cycleTimes = aCycleTimes;
//    }

    /**
     * Access method for the assignmentTime property.
     * 
     * @return the current value of the assignmentTime property
     */
    public String getAssignmentTime()
    {
        return assignmentTime;
    }

    /**
     * Sets the value of the assignmentTime property.
     * 
     * @param aAssignmentTime
     *            the new value of the assignmentTime property
     */
    public void setAssignmentTime(String aAssignmentTime)
    {
        assignmentTime = aAssignmentTime;
    }

    public List getRatifyAdvice()
    {
        return ratifyAdvice;
    }

    public void setRatifyAdvice(List ratifyAdvice)
    {
        this.ratifyAdvice = ratifyAdvice;
    }

    public List getViewers()
    {
        return viewers;
    }

    public void setViewers(List viewers)
    {
        this.viewers = viewers;
    }

    public int getExecutorID()
    {
        return executorID;
    }

    public void setExecutorID(int executorID)
    {
        this.executorID = executorID;
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

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getSchedularID()
    {
        return schedularID;
    }

    public void setSchedularID(int schedularID)
    {
        this.schedularID = schedularID;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
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

    public Date getRemindBeginTime()
    {
        return remindBeginTime;
    }

    public void setRemindBeginTime(Date remindBeginTime)
    {
        this.remindBeginTime = remindBeginTime;
    }

    public Date getRemindEndTime()
    {
        return remindEndTime;
    }

    public void setRemindEndTime(Date remindEndTime)
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

    public String getUserRealName()
    {
        return userRealName;
    }

    public void setUserRealName(String userRealName)
    {
        this.userRealName = userRealName;
    }

    public String getPartner()
    {
        return partner;
    }

    public void setPartner(String partner)
    {
        this.partner = partner;
    }

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
