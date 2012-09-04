package com.frameworkset.platform.sysmgrcore.manager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.frameworkset.spi.Provider;

import com.frameworkset.platform.schedularmanage.Notepaper;
import com.frameworkset.platform.schedularmanage.Notic;
import com.frameworkset.platform.schedularmanage.RatifyAdvice;
import com.frameworkset.platform.schedularmanage.Schedular;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.util.ListInfo;

public interface SchedularManager extends Provider, Serializable
{

    /**
     * 增加日程
     * 
     * @param schedular
     * @return 成功则返回日程ID，否则返回-1。
     * @throws ManagerException
     */

    public int addSchedular(Schedular schedular) throws ManagerException;

    /**
     * 修改提醒表
     * 
     * @param schedular
     * @return boolean 成功返回TRUE，否侧返回FALSE。
     * @throws ManagerException
     */
    public boolean modifyRemind(Schedular schedular) throws ManagerException;

    /**
     * 修改日程
     * 
     * @param schedular
     * @return boolean 成功返回TRUE，否侧返回FALSE。
     * @throws ManagerException
     */
    public boolean modifySchedular(Schedular schedular) throws ManagerException;

    /**
     * 
     * @param schedular
     * @return boolean 成功返回TRUE，否侧返回FALSE。
     * @throws ManagerException
     */
    public boolean requestSchedular(Schedular schedular)
            throws ManagerException;

    /**
     * 更改日程状态
     * 
     * @param schedular
     * @return boolean 成功返回TRUE，否侧返回FALSE。
     * @throws ManagerException
     */
    public boolean modifyStatus(Schedular schedular) throws ManagerException;

    /**
     * 删除日程
     * 
     * @return boolean 成功返回TRUE，否侧返回FALSE。
     * @throws ManagerException
     */
    public boolean deleteSchedular(int scheduleID) throws ManagerException;

    /**
     * 
     * @param j
     * @return
     * @throws ManagerException
     */
    public boolean deleteAdvice(int j) throws ManagerException;

    /**
     * 
     * @param scheduleID
     * @return
     * @throws ManagerException
     */
    public RatifyAdvice getAdvice(int j) throws ManagerException;

    /**
     * 改为历史日程
     * 
     * @param
     * @return boolean 成功返回TRUE，否侧返回FALSE。
     * @throws ManagerException
     */
    public boolean history(int scheduleID) throws ManagerException;

    /**
     * 将历史日程恢复为一般日程
     * 
     * @param 日程ID
     * @return 成功返回TRUE，否侧返回FALSE。
     * @throws ManagerException
     */
    public boolean resumeHistory(int scheduleID) throws ManagerException;

    /**
     * 得到该日程的属性。
     * 
     * @param scheduleID
     * @return Schedular
     *         返回该日程的SchedularID，PlannerID，ExecutorID，BeginTime，EndTime，Type，Place，Topic等。
     * @throws ManagerException
     */
    public Schedular getSchedular(int scheduleID) throws ManagerException;

    /**
     * 
     * @param j
     * @return 得到需要修改的日程属性，包括日程的提醒设置
     * @throws ManagerException
     */
    public Schedular getModifySchedular(int j) throws ManagerException;

    /**
     * 用于得到日程列表
     * 
     * @param sql
     * @param offset
     * @param maxItem
     * @return 返回日程列表。
     * @throws ManagerException
     */
    public ListInfo getSchedularList(String sql, int offset, int maxItem)
            throws ManagerException;

    /**
     * 
     * @param sql
     * @param offset
     * @param maxItem
     * @return返回日程列表，包括一个用户名。
     * @throws ManagerException
     */
    public ListInfo getSchedularAndNameList(String sql, int offset, int maxItem)
            throws ManagerException;

    /**
     * 
     * @param sql
     * @param offset
     * @param maxItem
     * @return 返回通知列表
     * @throws ManagerException
     */
    public ListInfo getNoticList(String sql, int offset, int maxItem)
            throws ManagerException;

    /**
     * 
     * @param sql
     * @return 得到日程列表
     * @throws ManagerException
     */
    public ListInfo getSchedularList(String sql) throws ManagerException;

    /**
     * 添加日程批准属性。
     * 
     * @param ratifyAdvice
     * @return 成功则返回true，否则返回false
     * @throws ManagerException
     */
    public boolean addRatifyAdvice(RatifyAdvice ratifyAdvice)
            throws ManagerException;

    /**
     * 得到通知的属性NOTIC_ID、CONTENT、BEGINTIME、ENDTIME、PLACE、EXECUTOR_ID。
     * 
     * @param Notic
     * @return 返回Notic对象
     * @throws ManagerException
     */

    public Notic getNotic(int j) throws ManagerException;

    /**
     * 修改通知的topic、content、begintime、endtime、place、SOURCE属性。
     * 
     * @param notic对象
     * @return 修改成功则返回true，否则返回false
     * @throws ManagerException
     */
    public boolean modifyNotic(Notic notic) throws ManagerException;

    /**
     * 安排通知
     * 
     * @param j
     *            通知id
     * @return 安排成功则返回true，否则返回false
     * @throws ManagerException
     */
    public boolean arrNotic(int j) throws ManagerException;

    /**
     * 删除日程
     * 
     * @param 日程ID
     * @return 成功则返回true，否则返回false
     * @throws ManagerException
     */

    public boolean deleteNotic(int j) throws ManagerException;

    /**
     * 得到该天的日程
     * 
     * @param date
     * @param executorID
     * @return 日程列表
     * @throws ManagerException
     */
    public List getDaySchedular(String date, int executorID)
            throws ManagerException;

    /**
     * 增加通知
     * 
     * @param notic
     * @return 成功则返回1，否则返回0
     * @throws ManagerException
     * @author lin.jian
     */
    public boolean addNotic(Notic notic) throws ManagerException;

    /**
     * 
     * @param beginTime
     * @param endTime
     * @param executorID
     * @return 返回"<yyyy-mm-dd><yyyy-mm-dd>"型字符串
     * @throws ManagerException
     */
    public String getArrDays(int executorID) throws ManagerException;

    /**
     * 生成quartz表达式。
     * 
     * @param beginTime
     *            提醒开始时间
     * @param endTime
     *            提醒结束时间
     * @param interval
     *            时间间隔
     * @param intervalType
     *            间隔类型
     * @return 返回quartz 要求的字符串
     * @throws ManagerException
     */
    public String generateCrontime(Date beginTime, Date endTime,
            double interval, int intervalType) throws ManagerException;

    public List getStartSchedular() throws ManagerException;
    
    public boolean isRemind(int schedularID) throws ManagerException;
    /**
     * 增加便笺
     * @param notepaper
     * @return 成功则返回便笺ID，否则返回-1。
     * @throws ManagerException
     */
    public int addNotepaper(Notepaper notepaper) throws ManagerException;
    /**
     * 删除便笺
     * @param notepaperID
     * @return boolean 成功返回TRUE，否侧返回FALSE
     * @throws ManagerException
     */
     
    public boolean deleteNotepaper(int notepaperID) throws ManagerException;
    /**
     * 修改便笺的content,time
     * @param notepaper
     * @return boolean 成功返回TRUE，否侧返回FALSE。
     * @throws ManagerException
     */
    public boolean modifyNotepaper(Notepaper notepaper) throws ManagerException;
    /**
     * 得到便笺的属性
     * @param notepaperID
     * @return 返回该便笺的notepaperID,content,time
     * @throws ManagerException
     */
    public Notepaper getNotepaper(int notepaperID) throws ManagerException;
    /**
     * 得到便笺列表
     * @param userID
     * @param offset
     * @param maxItem
     * @return 便笺列表
     * @throws ManagerException
     */
    public ListInfo getNotepaperList(int userID, int offset, int maxItem) throws ManagerException;
    /**
     * 
     * <p>
     * 功能描述：根据用户id删除所有相关联的表
     * </p>
     * 作   者：lin.jian
     * 创建时间 Oct 12, 2006    
     * @param userID
     * @throws ManagerException
     */
    public void deleteAllSchTableByUserId(int userID) throws ManagerException;
}
