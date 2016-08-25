package com.frameworkset.platform.holiday;

import java.util.Date;
import java.util.List;

import org.frameworkset.spi.Provider;

import com.frameworkset.platform.sysmgrcore.exception.ManagerException;

public interface HolidayManager extends Provider,java.io.Serializable
{
    /**
     * 比较两个日期的大小，如果endDateStr小于beginDateStr则返回-1，大于则放回1，等于则返回0
     * 
     * @param beginDateStr
     * @param endDateStr
     * @return
     * @throws ManagerException
     */
    public int compareDateStr(String beginDateStr, String endDateStr)
            throws ManagerException;

    /**
     * 得到从开始日期经过几个工作日后的日期
     * 
     * @param beginDate
     * @param workdays
     * @return 
     * @throws ManagerException
     */
    public Date getEndDate(Date beginDate, int workdays)
            throws ManagerException;

    /**
     * 得到两个日期内的工作日天数并返回。
     * 
     * @param beginDate
     * @param endDate
     * @return
     * @throws ManagerException
     */
    public int getWorkdayCounts(Date beginDate, Date endDate)
            throws ManagerException;

    /**
     * 得到两个日期内的工作日数组
     * 
     * @param beginDate
     * @param endDate
     * @return Date
     * @throws ManagerException
     */
    public List getWorkdays(Date beginDate, Date endDate)
            throws ManagerException;

    /**
     * 得到从某个时间开始到第i个工作日内的工作日数组
     * 
     * @param beginDate
     * @param workdaysCount
     * @return Date
     * @throws ManagerException
     */
    public List getWorkdays(Date beginDate, int workdaysCount)
            throws ManagerException;

    /**
     * 得到两个日期内的假日数组
     * 
     * @param beginDate
     * @param endDate
     * @return Date
     * @throws ManagerException
     */
    public List getHolidays(Date beginDate, Date endDate)
            throws ManagerException;
    /**
     * 得到从某个时间开始到第i个工作日内的假日数组
     * @param beginDate
     * @param workdaysCount
     * @return Date
     * @throws ManagerException
     */
    public List getHolidays(Date beginDate, int workdaysCount)
    throws ManagerException;
    /**
     * 
     * <p>Description: 得到beginDate经过workdays个工作日后经过多少个自然日</p>
     * <p>creationTime: Oct 19, 2006</p>
     * @author lin.jian
     * @param beginDate
     * @param workdays
     * @return 得到beginDate经过workdays个工作日后经过多少个自然日
     * @throws ManagerException
     */
    public int getRealdays(Date beginDate, int workdays)
    throws ManagerException;

}
