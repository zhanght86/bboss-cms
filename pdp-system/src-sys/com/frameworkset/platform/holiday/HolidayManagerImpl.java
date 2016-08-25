package com.frameworkset.platform.holiday;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.frameworkset.event.EventHandle;

import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.util.StringUtil;

public class HolidayManagerImpl extends EventHandle implements HolidayManager
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 比较两个日期的大小，如果endDateStr小于beginDateStr则返回-1，大于则放回1，等于则返回0
     * 
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public int compareDateStr(String beginDateStr, String endDateStr)
            throws ManagerException
    {
        int by = 0;
        int ey = 0;
        int bm = 0;
        int em = 0;
        int bd = 0;
        int ed = 0;
        
        
        by = Integer.parseInt(beginDateStr.substring(0, 4));
        ey = Integer.parseInt(endDateStr.substring(0, 4));
        // 取得年整数型
        bm = Integer.parseInt(beginDateStr.substring(5, beginDateStr.indexOf(
                '-', 5)));
        em = Integer.parseInt(endDateStr.substring(5, endDateStr
                .indexOf('-', 5)));
        // 取得月的整数型
        bd = Integer.parseInt(beginDateStr.substring(beginDateStr.indexOf('-',
                5) + 1));
        ed = Integer.parseInt(endDateStr
                .substring(endDateStr.indexOf('-', 5) + 1));
        // 取得日的整数型
        if (ey < by)
        {
            return -1;
        }
        if (ey > by)
        {
            return 1;
        }
        if (em < bm)
        {
            return -1;
        }
        if (em > bm)
        {
            return 1;
        }
        if (ed < bd)
        {
            return -1;
        }
        if (ed > bd)
        {
            return 1;
        }
        return 0;
    }

    /**
     * 得到从某天开始到第几个工作日的日期。
     * 包括开始的日期和得到的日期。
     */
    public Date getEndDate(Date beginDate, int workdays)
            throws ManagerException
    {
        Date endDate = new Date();
        endDate.setDate(beginDate.getDate() + workdays - 1);
        int holidays = workdays - getWorkdayCounts(beginDate, endDate);
        while (holidays > 0)
        {
            endDate.setDate(endDate.getDate() + holidays);
            holidays = workdays - getWorkdayCounts(beginDate, endDate);
        }
        endDate.setDate(endDate.getDate() + holidays);
        return endDate;
    }

    /**
     * 得到两个日期之间的工作日天数
     */
    public int getWorkdayCounts(Date beginDate, Date endDate)
            throws ManagerException
    {
        DBUtil db = new DBUtil();
        Date cloneBeginDate = new Date();
        cloneBeginDate = (Date) beginDate.clone();
        int workdays = 1;
        String beginDateStr = StringUtil.getFormatDate(beginDate, "yyyy-M-d");
        String endDateStr = StringUtil.getFormatDate(endDate, "yyyy-M-d");
        if (endDate.before(beginDate))
        {
            beginDateStr = StringUtil.getFormatDate(endDate, "yyyy-M-d");
            endDateStr = StringUtil.getFormatDate(beginDate, "yyyy-M-d");
        }
        String sql = "select holiday,yholiday,mholiday from TD_SP_HOLIDAY where 1=1 and";
        int bm = 0;
        int em = 0;
        int bd = 0;
        int ed = 0;
        int count = 0;
        if (endDate.getYear() == cloneBeginDate.getYear()
                && endDate.getMonth() == cloneBeginDate.getMonth()
                && endDate.getDate() == cloneBeginDate.getDate())
        {
            return 1;
        }
        for (workdays = 1; workdays < 100; workdays++)
        {
            cloneBeginDate.setDate(cloneBeginDate.getDate() + 1);
            if (endDate.getYear() == cloneBeginDate.getYear()
                    && endDate.getMonth() == cloneBeginDate.getMonth()
                    && endDate.getDate() == cloneBeginDate.getDate())
                break;
        }

        bm = Integer.parseInt(beginDateStr.substring(5, beginDateStr.indexOf(
                '-', 5)));
        em = Integer.parseInt(endDateStr.substring(5, endDateStr
                .indexOf('-', 5)));
        bd = Integer.parseInt(beginDateStr.substring(beginDateStr.indexOf('-',
                5) + 1));
        ed = Integer.parseInt(endDateStr
                .substring(endDateStr.indexOf('-', 5) + 1));
        if (beginDateStr.substring(0, 4).equals(beginDateStr.substring(0, 4)))
        {
            sql += " yholiday = '" + beginDateStr.substring(0, 4) + "'";
            for (int i = bm; i <= em; i++)
            {
                if (i == bm)
                {
                    sql += " and mholiday = '" + i + "" + "'";
                } else
                {
                    sql += " or mholiday = '" + i + "" + "'";
                }
            }
        } else
        {
            for (int i = Integer.parseInt(beginDateStr.substring(0, 4)); i <= Integer
                    .parseInt(endDateStr.substring(0, 4)); i++)
            {
                if (i == Integer.parseInt(beginDateStr.substring(0, 4)))
                {
                    sql += " and yholiday = '" + i + "" + "'";
                } else
                {
                    sql += " or yholiday = '" + i + "" + "'";
                }
            }
        }
        try
        {
            db.executeSelect(sql);
            for (int i = 0; i < db.size(); i++)
            {
                String currDateStr = db.getString(i, "holiday");
                if (compareDateStr(beginDateStr, currDateStr) >= 0
                        && compareDateStr(currDateStr, endDateStr) >= 0)
                {
                    count++;
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        workdays = workdays - count + 1;
        return workdays;
    }

    /**
     * 得到两个日期内的工作日数组 包括beginDate但不包括endDate. 注：beginDate要早于endDate
     * 
     * @param beginDate
     * @param endDate
     * @return
     * @throws ManagerException
     */
    public List getWorkdays(Date beginDate, Date endDate)
            throws ManagerException
    {
        List workdaysList = new ArrayList();
        List holidaysList = new ArrayList();
        holidaysList = getHolidays(beginDate, endDate);
        Date cloneBeginDate = new Date();
        cloneBeginDate = (Date) beginDate.clone();
        while (!(cloneBeginDate.getYear() == endDate.getYear()
                && cloneBeginDate.getMonth() == endDate.getMonth() && cloneBeginDate
                .getDate() == endDate.getDate()))
        {
            boolean isHoliday = false;
            for (int i = 0; i < holidaysList.size(); i++)
            {
                Date holiday = new Date();
                holiday = (Date) holidaysList.get(i);
                if (cloneBeginDate.getYear() == holiday.getYear()
                        && cloneBeginDate.getMonth() == holiday.getMonth()
                        && cloneBeginDate.getDate() == holiday.getDate())
                {
                    isHoliday = true;
                    holidaysList.remove(i);
                    break;
                }
            }
            if (isHoliday == false)
            {
                Date workday = new Date();
                workday.setYear(cloneBeginDate.getYear());
                workday.setMonth(cloneBeginDate.getMonth());
                workday.setDate(cloneBeginDate.getDate());
                workday.setHours(0);
                workday.setMinutes(0);
                workday.setSeconds(0);
                workdaysList.add(workday);
            }
            cloneBeginDate.setDate(cloneBeginDate.getDate() + 1);
        }
        return workdaysList;
    }

    /**
     * 得到从某个时间开始到第i个工作日内的工作日数组
     * 
     * @param beginDate
     * @param workdaysCount
     * @return
     * @throws ManagerException
     */
    public List getWorkdays(Date beginDate, int workdaysCount)
            throws ManagerException
    {
        List workdaysList = new ArrayList();
        Date endDate = new Date();
        endDate = getEndDate(beginDate, workdaysCount + 1);
        workdaysList = getWorkdays(beginDate, endDate);
        return workdaysList;
    }

    /**
     * 得到两个日期内的假日数组
     * 
     * @param beginDate
     * @param endDate
     * @return
     * @throws ManagerException
     */
    public List getHolidays(Date beginDate, Date endDate)
            throws ManagerException
    {
        List list = new ArrayList();
        DBUtil db = new DBUtil();
        String beginDateStr = StringUtil.getFormatDate(beginDate, "yyyy-M-d");
        String endDateStr = StringUtil.getFormatDate(endDate, "yyyy-M-d");
        if (endDate.before(beginDate))
        {
            beginDateStr = StringUtil.getFormatDate(endDate, "yyyy-M-d");
            endDateStr = StringUtil.getFormatDate(beginDate, "yyyy-M-d");
        }
        String sql = "select holiday,yholiday,mholiday from TD_SP_HOLIDAY where 1=1 and";
        int bm = 0;
        int em = 0;
        int bd = 0;
        int ed = 0;
        int count = 0;

        bm = Integer.parseInt(beginDateStr.substring(5, beginDateStr.indexOf(
                '-', 5)));
        em = Integer.parseInt(endDateStr.substring(5, endDateStr
                .indexOf('-', 5)));
        bd = Integer.parseInt(beginDateStr.substring(beginDateStr.indexOf('-',
                5) + 1));
        ed = Integer.parseInt(endDateStr
                .substring(endDateStr.indexOf('-', 5) + 1));
        if (beginDateStr.substring(0, 4).equals(beginDateStr.substring(0, 4)))
        {
            sql += " yholiday = '" + beginDateStr.substring(0, 4) + "'";
            for (int i = bm; i <= em; i++)
            {
                if (i == bm)
                {
                    sql += " and mholiday = '" + i + "" + "'";
                } else
                {
                    sql += " or mholiday = '" + i + "" + "'";
                }
            }
        } else
        {
            for (int i = Integer.parseInt(beginDateStr.substring(0, 4)); i <= Integer
                    .parseInt(endDateStr.substring(0, 4)); i++)
            {
                if (i == Integer.parseInt(beginDateStr.substring(0, 4)))
                {
                    sql += " and yholiday = '" + i + "" + "'";
                } else
                {
                    sql += " or yholiday = '" + i + "" + "'";
                }
            }
        }
        try
        {
            db.executeSelect(sql);
            for (int i = 0; i < db.size(); i++)
            {
                String currDateStr = db.getString(i, "holiday");
                if (compareDateStr(beginDateStr, currDateStr) >= 0
                        && compareDateStr(currDateStr, endDateStr) >= 0)
                {
                    Date holiday = new Date();
                    holiday.setYear(Integer.parseInt(db
                            .getString(i, "yholiday")) - 1900);
                    holiday.setMonth(Integer.parseInt(db.getString(i,
                            "mholiday")) - 1);
                    holiday.setDate(Integer.parseInt(currDateStr
                            .substring(currDateStr.indexOf('-', 5) + 1)));
                    holiday.setHours(0);
                    holiday.setMinutes(0);
                    holiday.setSeconds(0);
                    list.add(holiday);
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 得到从某个时间开始到第i个工作日内的假日数组
     * 
     * @param beginDate
     * @param workdaysCount
     * @return
     * @throws ManagerException
     */
    public List getHolidays(Date beginDate, int workdaysCount)
            throws ManagerException
    {
        List holidaysList = new ArrayList();
        Date endDate = new Date();
        endDate = getEndDate(beginDate, workdaysCount + 1);
        holidaysList = getHolidays(beginDate, endDate);
        return holidaysList;
    }

    /**
     * 获取从开始日期beginDate开始的工作日workdays中所包含的总天数：总天数=节假日天数+工作日天数
     * @see com.frameworkset.platform.holiday.HolidayManager#getRealdays(java.util.Date, int)
     */
    public int getRealdays (Date beginDate , int workdays) throws ManagerException
    {
        return this.getHolidays(beginDate,workdays).size()+workdays;
    }
}