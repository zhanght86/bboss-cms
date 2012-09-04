package com.frameworkset.platform.sysmgrcore.manager.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.frameworkset.event.EventHandle;

import com.frameworkset.platform.schedularmanage.Notepaper;
import com.frameworkset.platform.schedularmanage.Notic;
import com.frameworkset.platform.schedularmanage.RatifyAdvice;
import com.frameworkset.platform.schedularmanage.Schedular;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.SchedularManager;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

public class SchedularManagerImpl extends EventHandle implements
        SchedularManager
{
    /**
     * 
     */
    private static final long serialVersionUID = 3001659447071148470L;

    /**
     * 增加日程
     */
    public int addSchedular(Schedular schedular) throws ManagerException
    {
        int r = -1;
        DBUtil db = new DBUtil();

        StringBuffer sql = new StringBuffer(
                "insert into TD_SD_SCHEDULAR (PLANNER_ID,executor_id,")
                .append(
                        "REQUEST_ID,TOPIC,PLACE,CONTENT,BEGINTIME,ENDTIME,TYPE,ISPUBLICAFFAIR,")
                .append(
                        "ESSENTIALITY,ISHISTORY,ISLEISURE,STATUS,isopen,partner)")
                .append(" values(").append(schedular.getPlannerID())
                .append(",").append(schedular.getExecutorID()).append(",")
                .append(schedular.getRequestID()).append(",'").append(
                        schedular.getTopic()).append("','").append(
                        schedular.getPlace()).append("'").append(",'").append(
                        schedular.getContent()).append("',").append(
                        SQLManager.getInstance().getDBAdapter().getDateString(
                                schedular.getBeginTime())).append(",").append(
                        SQLManager.getInstance().getDBAdapter().getDateString(
                                schedular.getEndTime())).append(",'").append(
                        schedular.getType()).append("',").append(
                        schedular.getIsPublicAffair()).append(",'").append(
                        schedular.getEssentiality()).append("',").append(
                        schedular.getIsHistory()).append(",").append(
                        schedular.getIsLeisure()).append(",").append(
                        schedular.getStatus()).append(",").append(
                        schedular.getIsOpen()).append(",'").append(
                        schedular.getPartner()).append("')");
        try
        {
            r = Integer.parseInt(db.executeInsert(sql.toString()).toString());
            return r;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;

    }

    /**
     * 增加提醒
     * 
     * @param schedular
     * @return
     * @throws ManagerException
     */
    public int addRemind(Schedular schedular) throws ManagerException
    {
        int r = 0;
        DBUtil db = new DBUtil();

        String sql = "insert into TD_SD_REMIND (SCHEDULAR_ID,REMIND_BEGIN_TIME,REMIND_END_TIME,ISSYSTEM,ISEMAIL,ISMESSAGE,INTERVAL_TIME,INTERVALTYPE) "
                + " values("
                + schedular.getSchedularID()
                + ","
                + ""
                + SQLManager.getInstance().getDBAdapter().getDateString(
                        schedular.getRemindBeginTime())
                + ","
                + ""
                + SQLManager.getInstance().getDBAdapter().getDateString(
                        schedular.getRemindEndTime())
                + ","
                + ""
                + schedular.getIsSys()
                + ","
                + schedular.getIsEmail()
                + ","
                + ""
                + schedular.getIsMessage()
                + ","
                + schedular.getInterval()
                + "," + schedular.getIntervalType() + ")";
        try
        {
            r = Integer.parseInt(db.executeInsert(sql.toString()).toString());
            return r;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;

    }

    /**
     * 修改提醒表
     */
    public boolean modifyRemind(Schedular schedular) throws ManagerException
    {

        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "update TD_SD_remind set"
                + " ISSYSTEM="
                + schedular.getIsSys()
                + ", ISEMAIL="
                + schedular.getIsEmail()
                + ", ISMESSAGE="
                + schedular.getIsMessage()
                + ", REMIND_BEGIN_TIME="
                + SQLManager.getInstance().getDBAdapter().getDateString(
                        schedular.getRemindBeginTime())
                + ", REMIND_END_TIME="
                + SQLManager.getInstance().getDBAdapter().getDateString(
                        schedular.getRemindEndTime()) + ", INTERVAL_TIME="
                + schedular.getInterval() + ", INTERVALTYPE="
                + schedular.getIntervalType() + " where schedular_id="
                + schedular.getSchedularID() + "";
        try
        {
            db.executeUpdate(sql);
            r = true;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 修改日程表
     */
    public boolean modifySchedular(Schedular schedular) throws ManagerException
    {

        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "update TD_SD_SCHEDULAR set"
                + " topic='"
                + schedular.getTopic()
                + "', content='"
                + schedular.getContent()
                + "', begintime="
                + SQLManager.getInstance().getDBAdapter().getDateString(
                        schedular.getBeginTime())
                + ", endtime="
                + SQLManager.getInstance().getDBAdapter().getDateString(
                        schedular.getEndTime()) + ", place='"
                + schedular.getPlace() + "', ispublicaffair="
                + schedular.getIsPublicAffair() + ", essentiality='"
                + schedular.getEssentiality() + "', isleisure="
                + schedular.getIsLeisure() + ", status="
                + schedular.getStatus() + " , isopen=" + schedular.getIsOpen()
                + " where schedular_id=" + schedular.getSchedularID() + "";
        try
        {
            db.executeUpdate(sql);
            r = true;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 修改请求安排日程的属性
     */
    public boolean requestSchedular(Schedular schedular)
            throws ManagerException
    {

        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "update TD_SD_SCHEDULAR set"
                + " topic='"
                + schedular.getTopic()
                + "', content='"
                + schedular.getContent()
                + "', begintime="
                + SQLManager.getInstance().getDBAdapter().getDateString(
                        schedular.getBeginTime())
                + ", endtime="
                + SQLManager.getInstance().getDBAdapter().getDateString(
                        schedular.getEndTime()) + ", place='"
                + schedular.getPlace() + "', ispublicaffair="
                + schedular.getIsPublicAffair() + ", essentiality='"
                + schedular.getEssentiality() + "', isleisure="
                + schedular.getIsLeisure() + ",isopen=" + schedular.getIsOpen()
                + ", status=" + schedular.getStatus() + ",planner_id="
                + schedular.getPlannerID() + " where schedular_id="
                + schedular.getSchedularID() + "";
        try
        {
            db.executeUpdate(sql);
            r = true;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 改变日程的状态
     */
    public boolean modifyStatus(Schedular schedular) throws ManagerException
    {

        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "update TD_SD_SCHEDULAR set status="
                + schedular.getStatus() + " where schedular_id="
                + schedular.getSchedularID() + "";
        try
        {
            db.executeUpdate(sql);
            r = true;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 安排日程，即修改日程的某些属性
     * 
     * @param schedular
     * @return
     * @throws ManagerException
     */
    public boolean arrangeSchedular(Schedular schedular)
            throws ManagerException
    {

        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "update TD_SD_SCHEDULAR set"
                + " topic='"
                + schedular.getTopic()
                + "', content='"
                + schedular.getContent()
                + "', begintime="
                + SQLManager.getInstance().getDBAdapter().getDateString(
                        schedular.getBeginTime())
                + ", endtime="
                + SQLManager.getInstance().getDBAdapter().getDateString(
                        schedular.getEndTime()) + ", place='"
                + schedular.getPlace() + "', ispublicaffair="
                + schedular.getIsPublicAffair() + ", essentiality='"
                + schedular.getEssentiality() + "', isleisure="
                + schedular.getIsLeisure() + ", status="
                + schedular.getStatus() + ", isopen=" + schedular.getIsOpen()
                + " where schedular_id=" + schedular.getSchedularID() + "";
        try
        {
            db.executeUpdate(sql);
            r = true;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 删除日程
     */
    public boolean deleteSchedular(int i) throws ManagerException
    {

        boolean r = false;
        DBUtil db = new DBUtil();

        String sql1 = "delete from td_sd_ratifyadvice where schedular_id=" + i
                + "";
        String sql2 = "delete from td_sd_remind where schedular_id=" + i + "";
        String sql3 = "delete from TD_SD_SCHEDULAR where schedular_id=" + i
                + "";
        try
        {
            db.executeDelete(sql1);
            db.executeDelete(sql2);
            db.executeDelete(sql3);

            r = true;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 得到日程的属性和日程安排人的用户名
     * 
     * @param schedular
     * @return
     * @throws ManagerException
     */

    public Schedular getSchedular(int j) throws ManagerException
    {
        DBUtil dbUtil = new DBUtil();
        String sql = "select a.*,b.user_name,b.user_realname from TD_SD_SCHEDULAR a,td_sm_user b "
                + "where a.planner_id = b.user_id and a.schedular_id = "
                + j
                + "";
        Schedular sch = new Schedular();

        try
        {
            dbUtil.executeSelect(sql);
            for (int i = 0; i < dbUtil.size(); i++)
            {
                sch.setSchedularID(dbUtil.getInt(i, "SCHEDULAR_ID"));
                sch.setPlannerID(dbUtil.getInt(i, "PLANNER_ID"));
                sch.setRequestID(dbUtil.getInt(i, "REQUEST_ID"));
                sch.setExecutorID(dbUtil.getInt(i, "executor_id"));
                sch.setBeginTime(dbUtil.getDate(i, "BEGINTIME"));
                sch.setEndTime(dbUtil.getDate(i, "ENDTIME"));
                sch.setType(dbUtil.getString(i, "TYPE"));
                sch.setIsLeisure(dbUtil.getInt(i, "ISLEISURE"));
                sch.setIsPublicAffair(dbUtil.getInt(i, "ISPUBLICAFFAIR"));
                sch.setContent(dbUtil.getString(i, "CONTENT"));
                sch.setPlace(dbUtil.getString(i, "PLACE"));
                sch.setTopic(dbUtil.getString(i, "TOPIC"));
                sch.setEssentiality(dbUtil.getString(i, "ESSENTIALITY"));
                sch.setStatus(dbUtil.getInt(i, "STATUS"));
                sch.setIsHistory(dbUtil.getInt(i, "ISHISTORY"));
                sch.setIsOpen(dbUtil.getInt(i, "isopen"));
                sch.setUserName(dbUtil.getString(i, "user_name"));
                sch.setUserRealName(dbUtil.getString(i, "user_realname"));

                return sch;
            }

        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sch;
    }

    /**
     * 得到日程的属性和日程安排人用户名和日程提醒表的属性
     */
    public Schedular getModifySchedular(int j) throws ManagerException
    {
        DBUtil dbUtil = new DBUtil();
        String sql = "select a.*,b.user_name,b.user_realname,c.* from TD_SD_SCHEDULAR a,td_sm_user b,td_sd_remind c "
                + "where a.planner_id = b.user_id and a.schedular_id = "
                + j
                + " and c.schedular_id = " + j + " ";
        String sql1 = "select a.*,b.user_name,b.user_realname from TD_SD_SCHEDULAR a,td_sm_user b"
                + " where a.planner_id = b.user_id and a.schedular_id = "
                + j
                + "";
        Schedular sch = new Schedular();

        try
        {
            dbUtil.executeSelect(sql);
            if (dbUtil.size() == 0)
            {
                dbUtil.executeSelect(sql1);
                for (int i = 0; i < dbUtil.size(); i++)
                {
                    sch.setSchedularID(dbUtil.getInt(i, "SCHEDULAR_ID"));
                    sch.setPlannerID(dbUtil.getInt(i, "PLANNER_ID"));
                    sch.setRequestID(dbUtil.getInt(i, "REQUEST_ID"));
                    sch.setExecutorID(dbUtil.getInt(i, "executor_id"));
                    sch.setBeginTime(dbUtil.getDate(i, "BEGINTIME"));
                    sch.setEndTime(dbUtil.getDate(i, "ENDTIME"));
                    sch.setType(dbUtil.getString(i, "TYPE"));
                    sch.setIsLeisure(dbUtil.getInt(i, "ISLEISURE"));
                    sch.setIsPublicAffair(dbUtil.getInt(i, "ISPUBLICAFFAIR"));
                    sch.setContent(dbUtil.getString(i, "CONTENT"));
                    sch.setPlace(dbUtil.getString(i, "PLACE"));
                    sch.setTopic(dbUtil.getString(i, "TOPIC"));
                    sch.setEssentiality(dbUtil.getString(i, "ESSENTIALITY"));
                    sch.setStatus(dbUtil.getInt(i, "STATUS"));
                    sch.setIsHistory(dbUtil.getInt(i, "ISHISTORY"));
                    sch.setIsOpen(dbUtil.getInt(i, "isopen"));
                    sch.setUserName(dbUtil.getString(i, "user_name"));
                    sch.setUserRealName(dbUtil.getString(i, "user_realname"));
                    sch.setPartner(dbUtil.getString(i, "partner"));
                    return sch;
                }

            } else
            {
                for (int i = 0; i < dbUtil.size(); i++)
                {
                    sch.setSchedularID(dbUtil.getInt(i, "SCHEDULAR_ID"));
                    sch.setPlannerID(dbUtil.getInt(i, "PLANNER_ID"));
                    sch.setRequestID(dbUtil.getInt(i, "REQUEST_ID"));
                    sch.setExecutorID(dbUtil.getInt(i, "executor_id"));
                    sch.setBeginTime(dbUtil.getDate(i, "BEGINTIME"));
                    sch.setEndTime(dbUtil.getDate(i, "ENDTIME"));
                    sch.setType(dbUtil.getString(i, "TYPE"));
                    sch.setIsLeisure(dbUtil.getInt(i, "ISLEISURE"));
                    sch.setIsPublicAffair(dbUtil.getInt(i, "ISPUBLICAFFAIR"));
                    sch.setContent(dbUtil.getString(i, "CONTENT"));
                    sch.setPlace(dbUtil.getString(i, "PLACE"));
                    sch.setTopic(dbUtil.getString(i, "TOPIC"));
                    sch.setEssentiality(dbUtil.getString(i, "ESSENTIALITY"));
                    sch.setStatus(dbUtil.getInt(i, "STATUS"));
                    sch.setIsHistory(dbUtil.getInt(i, "ISHISTORY"));
                    sch.setIsOpen(dbUtil.getInt(i, "isopen"));
                    sch.setUserName(dbUtil.getString(i, "user_name"));
                    sch.setUserRealName(dbUtil.getString(i, "user_realname"));
                    sch.setPartner(dbUtil.getString(i, "partner"));
                    sch.setRemindBeginTime(dbUtil.getDate(i,
                            "REMIND_BEGIN_TIME"));
                    sch.setRemindEndTime(dbUtil.getDate(i, "REMIND_END_TIME"));
                    sch.setInterval(dbUtil.getDouble(i, "INTERVAL_TIME"));
                    sch.setIntervalType(dbUtil.getInt(i, "INTERVALTYPE"));
                    sch.setIsSys(dbUtil.getInt(i, "ISSYSTEM"));
                    sch.setIsMessage(dbUtil.getInt(i, "ISMESSAGE"));
                    sch.setIsEmail(dbUtil.getInt(i, "ISEMAIL"));
                    return sch;
                }
            }
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sch;
    }

    /**
     * 得到日程列表
     */
    public ListInfo getSchedularList(String sql, int offset, int maxItem)
            throws ManagerException
    {
        DBUtil dbUtil = new DBUtil();
        try
        {

            dbUtil.executeSelect(sql, offset, maxItem);
            ListInfo listInfo = new ListInfo();
            List list = new ArrayList();

            for (int i = 0; i < dbUtil.size(); i++)
            {
                Schedular sch = new Schedular();

                sch.setSchedularID(dbUtil.getInt(i, "SCHEDULAR_ID"));
                sch.setPlannerID(dbUtil.getInt(i, "PLANNER_ID"));
                sch.setExecutorID(dbUtil.getInt(i, "executor_id"));
                sch.setBeginTime(dbUtil.getDate(i, "BEGINTIME"));
                sch.setEndTime(dbUtil.getDate(i, "ENDTIME"));
                sch.setType(dbUtil.getString(i, "TYPE"));
                sch.setPlace(dbUtil.getString(i, "PLACE"));
                sch.setTopic(dbUtil.getString(i, "TOPIC"));
                sch.setIsOpen(dbUtil.getInt(i, "isopen"));
                sch.setStatus(dbUtil.getInt(i, "status"));
                sch.setEssentiality(dbUtil.getString(i, "ESSENTIALITY"));
                list.add(sch);
            }
            listInfo.setDatas(list);
            listInfo.setTotalSize(dbUtil.getTotalSize());
            return listInfo;
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw new ManagerException(e.getMessage());

        }

    }

    /**
     * 得到包含执行人或者安排人用户名的日程列表
     */
    public ListInfo getSchedularAndNameList(String sql, int offset, int maxItem)
            throws ManagerException
    {
        DBUtil dbUtil = new DBUtil();
        try
        {

            dbUtil.executeSelect(sql, offset, maxItem);
            ListInfo listInfo = new ListInfo();
            List list = new ArrayList();

            for (int i = 0; i < dbUtil.size(); i++)
            {
                Schedular sch = new Schedular();

                sch.setSchedularID(dbUtil.getInt(i, "SCHEDULAR_ID"));
                sch.setPlannerID(dbUtil.getInt(i, "PLANNER_ID"));
                sch.setExecutorID(dbUtil.getInt(i, "executor_id"));
                sch.setBeginTime(dbUtil.getDate(i, "BEGINTIME"));
                sch.setUserName(dbUtil.getString(i, "USER_NAME"));
                sch.setUserRealName(dbUtil.getString(i, "user_realname"));
                sch.setEndTime(dbUtil.getDate(i, "ENDTIME"));
                sch.setType(dbUtil.getString(i, "TYPE"));
                sch.setPlace(dbUtil.getString(i, "PLACE"));
                sch.setTopic(dbUtil.getString(i, "TOPIC"));
                sch.setStatus(dbUtil.getInt(i, "status"));
                sch.setIsOpen(dbUtil.getInt(i, "isopen"));
                sch.setEssentiality(dbUtil.getString(i, "essentiality"));
                sch.setContent(dbUtil.getString(i, "content"));
                sch.setIsPublicAffair(dbUtil.getInt(i, "ispublicaffair"));

                list.add(sch);
            }
            listInfo.setDatas(list);
            listInfo.setTotalSize(dbUtil.getTotalSize());
            return listInfo;
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw new ManagerException(e.getMessage());

        }

    }

    /**
     * 得到没有分页的日程列表
     */
    public ListInfo getSchedularList(String sql) throws ManagerException
    {

        DBUtil dbUtil = new DBUtil();
        try
        {
            dbUtil.executeSelect(sql);
            ListInfo listInfo = new ListInfo();
            List list = new ArrayList();

            for (int i = 0; i < dbUtil.size(); i++)
            {
                Schedular sch = new Schedular();

                sch.setSchedularID(dbUtil.getInt(i, "SCHEDULAR_ID"));
                sch.setPlannerID(dbUtil.getInt(i, "PLANNER_ID"));
                sch.setExecutorID(dbUtil.getInt(i, "executor_id"));
                sch.setBeginTime(dbUtil.getDate(i, "BEGINTIME"));
                sch.setEndTime(dbUtil.getDate(i, "ENDTIME"));
                sch.setType(dbUtil.getString(i, "TYPE"));
                sch.setPlace(dbUtil.getString(i, "PLACE"));
                sch.setTopic(dbUtil.getString(i, "TOPIC"));
                sch.setEssentiality(dbUtil.getString(i, "essentiality"));
                sch.setContent(dbUtil.getString(i, "content"));
                sch.setIsOpen(dbUtil.getInt(i, "isopen"));
                sch.setIsPublicAffair(dbUtil.getInt(i, "ispublicaffair"));
                sch.setStatus(dbUtil.getInt(i, "status"));

                list.add(sch);
            }
            listInfo.setDatas(list);
            listInfo.setTotalSize(dbUtil.getTotalSize());
            return listInfo;
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw new ManagerException(e.getMessage());

        }

    }

    /**
     * 得到通知列表
     */
    public ListInfo getNoticList(String sql, int offset, int maxItem)
            throws ManagerException
    {

        DBUtil dbUtil = new DBUtil();
        try
        {

            dbUtil.executeSelect(sql, offset, maxItem);
            ListInfo listInfo = new ListInfo();
            List list = new ArrayList();
            for (int i = 0; i < dbUtil.size(); i++)
            {
                Notic notic = new Notic();

                notic.setNoticID(dbUtil.getInt(i, "NOTIC_ID"));
                notic.setBeginTime(dbUtil.getDate(i, "begintime"));
                notic.setEndTime(dbUtil.getDate(i, "endtime"));
                notic.setContent(dbUtil.getString(i, "content"));
                notic.setPlace(dbUtil.getString(i, "place"));
                notic.setExecutorID(dbUtil.getInt(i, "executor_id"));
                notic.setNoticPlannerName(dbUtil.getString(i, "user_name"));
                notic.setNoticPlannerRealName(dbUtil.getString(i,
                        "user_realname"));
                notic.setSource(dbUtil.getString(i, "source"));
                notic.setStatus(dbUtil.getInt(i, "status"));
                notic.setTopic(dbUtil.getString(i, "topic"));
                list.add(notic);
            }
            listInfo.setDatas(list);
            listInfo.setTotalSize(dbUtil.getTotalSize());
            return listInfo;
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw new ManagerException(e.getMessage());

        }
    }

    /**
     * 增加批准建议
     */
    public boolean addRatifyAdvice(RatifyAdvice ratifyAdvice)
            throws ManagerException
    {
        boolean r = false;
        DBUtil db = new DBUtil();
        String delSql = "delete TD_SD_RATIFYADVICE where SCHEDULAR_ID = "
                + ratifyAdvice.getSchedularID() + "";
        String sql = "insert into TD_SD_RATIFYADVICE (SCHEDULAR_ID,RATIFIER_ID,advice) values("
                + ratifyAdvice.getSchedularID()
                + ","
                + ratifyAdvice.getRatifierID()
                + ",'"
                + ratifyAdvice.getAdvice() + "')";
        try
        {
            db.executeDelete(delSql);
            db.executeInsert(sql);
            r = true;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 删除建议
     */
    public boolean deleteAdvice(int j) throws ManagerException
    {
        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "delete from TD_SD_RATIFYADVICE where SCHEDULAR_ID=" + j
                + "";
        try
        {
            db.executeDelete(sql);
            r = true;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 得到批准建议表的属性
     */
    public RatifyAdvice getAdvice(int j) throws ManagerException
    {
        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "select * from TD_SD_RATIFYADVICE where SCHEDULAR_ID = "
                + j + "";
        RatifyAdvice ratifyAdvice = new RatifyAdvice();
        try
        {
            db.executeSelect(sql);
            for (int i = 0; i < db.size(); i++)
            {
                ratifyAdvice.setAdvice(db.getString(i, "ADVICE"));
                ratifyAdvice.setSchedularID(db.getInt(i, "SCHEDULAR_ID"));
                ratifyAdvice.setRatifierID(db.getInt(i, "RATIFIER_ID"));
                return ratifyAdvice;
            }
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ratifyAdvice;
    }

    /**
     * 将某日程放入历史日程
     */
    public boolean history(int j) throws ManagerException
    {
        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "update TD_SD_SCHEDULAR set" + " ishistory=" + 1
                + ", status = " + 6 + " where schedular_id=" + j + "";
        try
        {
            db.executeInsert(sql);
            r = true;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 将某日程从历史日程中恢复
     */
    public boolean resumeHistory(int j) throws ManagerException
    {
        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "update TD_SD_SCHEDULAR set" + " ishistory=" + 0
                + ", status = " + 0 + " where schedular_id=" + j + "";
        try
        {
            db.executeInsert(sql);
            r = true;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 更具通知ID得到通知属性
     */
    public Notic getNotic(int j) throws ManagerException
    {
        Notic notic = new Notic();
        DBUtil db = new DBUtil();
        String sql = "select a.*,b.user_name from TD_SD_NOTIC a,td_sm_user b "
                + "where a.notic_planner_id = b.user_id and NOTIC_ID=" + j + "";
        try
        {
            db.executeSelect(sql);
            for (int i = 0; i < db.size(); i++)
            {
                notic.setBeginTime(db.getDate(i, "begintime"));
                notic.setEndTime(db.getDate(i, "endtime"));
                notic.setContent(db.getString(i, "content"));
                notic.setExecutorID(db.getInt(i, "executor_id"));
                notic.setPlace(db.getString(i, "place"));
                notic.setNoticPlannerName(db.getString(i, "user_nasme"));
                notic.setStatus(db.getInt(i, "status"));
                notic.setTopic(db.getString(i, "topic"));
                notic.setSource(db.getString(i, "source"));
                notic.setNoticPlannerID(db.getInt(i, "notic_planner_id"));
                return notic;
            }
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return notic;
    }

    /**
     * 删除通知
     */
    public boolean deleteNotic(int i) throws ManagerException
    {
        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "delete from TD_SD_NOTIC where NOTIC_ID=" + i + "";
        try
        {
            db.executeDelete(sql);
            r = true;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;

    }

    /**
     * 增加通知
     */
    public boolean addNotic(Notic notic) throws ManagerException
    {
        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "intsert into TD_SD_NOTIC (notic_planner_id,EXECUTOR_ID,topic,place,begintime,endtime,content,status,source) "
                + "values ("
                + notic.getNoticPlannerID()
                + ","
                + notic.getExecutorID()
                + ",'"
                + notic.getTopic()
                + "',"
                + notic.getBeginTime()
                + ","
                + notic.getEndTime()
                + ",'"
                + notic.getContent()
                + "',"
                + notic.getStatus()
                + ",'"
                + notic.getSource() + "')";
        try
        {
            db.executeInsert(sql);
            r = true;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 修改通知
     */
    public boolean modifyNotic(Notic notic) throws ManagerException
    {

        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "update TD_SD_NOTIC set"
                + " topic='"
                + notic.getTopic()
                + "', content='"
                + notic.getContent()
                + "', begintime="
                + SQLManager.getInstance().getDBAdapter().getDateString(
                        notic.getBeginTime())
                + ", endtime="
                + SQLManager.getInstance().getDBAdapter().getDateString(
                        notic.getEndTime()) + ", place='" + notic.getPlace()
                + "',  SOURCE=" + notic.getSource() + " where schedular_id="
                + notic.getNoticID() + "";
        try
        {
            db.executeUpdate(sql);
            r = true;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 将通知的安排状态修改为1，即“已安排”。
     */
    public boolean arrNotic(int j) throws ManagerException
    {
        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "update TD_SD_NOTIC set status=" + 1 + " where notic_id="
                + j + "";
        try
        {
            db.executeUpdate(sql);
            r = true;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 得到某天某执行人的日程
     */
    public List getDaySchedular(String date, int executorID)
            throws ManagerException
    {

        Date start = StringUtil.stringToDate(date + " 00:00:00");
        Date end = StringUtil.stringToDate(date + " 23:59:59");
        DBUtil dbUtil = new DBUtil();
        List list = new ArrayList();

        String sql = "select *  from TD_SD_SCHEDULAR  where status=0 and executor_id = "
                + executorID
                + " and beginTime>="
                + SQLManager.getInstance().getDBAdapter().getDateString(start)
                + ""
                + "and beginTime <="
                + SQLManager.getInstance().getDBAdapter().getDateString(end);
        try
        {

            dbUtil.executeSelect(sql);
            for (int i = 0; i < dbUtil.size(); i++)
            {
                Schedular sch = new Schedular();
                sch.setSchedularID(dbUtil.getInt(i, "SCHEDULAR_ID"));
                sch.setPlannerID(dbUtil.getInt(i, "PLANNER_ID"));
                sch.setExecutorID(dbUtil.getInt(i, "EXECUTOR_ID"));
                sch.setRequestID(dbUtil.getInt(i, "REQUEST_ID"));
                sch.setBeginTime(dbUtil.getDate(i, "BEGINTIME"));
                sch.setEndTime(dbUtil.getDate(i, "ENDTIME"));
                sch.setType(dbUtil.getString(i, "TYPE"));
                sch.setIsLeisure(dbUtil.getInt(i, "ISLEISURE"));
                sch.setIsPublicAffair(dbUtil.getInt(i, "ISPUBLICAFFAIR"));
                sch.setContent(dbUtil.getString(i, "CONTENT"));
                sch.setPlace(dbUtil.getString(i, "PLACE"));
                sch.setTopic(dbUtil.getString(i, "TOPIC"));
                sch.setEssentiality(dbUtil.getString(i, "ESSENTIALITY"));
                sch.setStatus(dbUtil.getInt(i, "STATUS"));
                sch.setIsHistory(dbUtil.getInt(i, "ISHISTORY"));
                list.add(sch);
            }
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 得到该执行人的所有有日程的日期
     */
    public String getArrDays(int executorID) throws ManagerException
    {
        String days = "";
        String str;
        DBUtil db = new DBUtil();
        String sql = "select distinct begintime from TD_SD_SCHEDULAR where status = 0 and executor_id = "
                + executorID + " order by begintime";
        try
        {
            db.executeSelect(sql);
            for (int i = 0; i < db.size(); i++)
            {
                Schedular sch = new Schedular();
                sch.setBeginTime(db.getDate(i, "begintime"));
                str = StringUtil
                        .getFormatDate(sch.getBeginTime(), "yyyy-MM-dd");
                if (days.indexOf(str) == -1)
                {
                    days = days
                            + "<"
                            + StringUtil.getFormatDate(sch.getBeginTime(),
                                    "yyyy-MM-dd") + ">";
                }
            }
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 
     * @param beginTime
     * @param endTime
     * @param interval
     * @param intervalType
     * @return
     * @throws ManagerException
     */
    public String generateCrontime(Date beginTime, Date endTime,
            double interval, int intervalType) throws ManagerException
    {

        String begintime = StringUtil.getFormatDate(beginTime,
                "yyyy-MM-dd HH:mm:ss");
        String endtime = StringUtil.getFormatDate(endTime,
                "yyyy-MM-dd HH:mm:ss");
        String expression = "";

        // 设置年份，如果开始年和结束年一致则为"yyyy",否则为yyyy1-yyyy2格式的串
        // yyyy1为开始年份，yyyy2为结束时间连份
        if (begintime.regionMatches(0, endtime, 0, 4))// yyyy
        {
            expression = expression + begintime.substring(0, 4);
        } else
        {
            expression = expression + begintime.substring(0, 4) + "-"
                    + endtime.substring(0, 4);
        }

        // 设置星期
        expression = " ? " + expression;

        // 间隔类型为月时，设置每月执行的次数
        if (intervalType == 3)
        {
            expression = "/" + interval + expression;
        }
        if (begintime.regionMatches(5, endtime, 5, 2))// mm
        {
            expression = " " + begintime.substring(5, 7) + expression;
        } else
        {
            expression = " " + begintime.substring(5, 7) + "-"
                    + endtime.substring(5, 7) + expression;
        }
        if (intervalType == 2)
        {
            expression = "/" + interval + expression;
        }
        if (begintime.regionMatches(8, endtime, 8, 2))// dd
        {
            expression = " " + begintime.substring(8, 10) + expression;
        } else
        {
            expression = " " + begintime.substring(8, 10) + "-"
                    + endtime.substring(8, 10) + expression;
        }
        if (intervalType == 1)
        {
            expression = "/" + interval + expression;
        }
        if (begintime.regionMatches(11, endtime, 11, 2))// hh
        {
            expression = " " + begintime.substring(11, 13) + expression;
        } else
        {
            expression = " " + begintime.substring(11, 13) + "-"
                    + endtime.substring(11, 13) + expression;
        }

        if (intervalType == 0)
        {
            expression = "/" + interval + expression;
        }

        if (begintime.regionMatches(14, endtime, 14, 2))// mm
        {
            expression = " " + begintime.substring(14, 16) + expression;
        } else
        {
            expression = " " + begintime.substring(14, 16) + "-"
                    + endtime.substring(14, 16) + expression;
        }

        if (begintime.regionMatches(17, endtime, 17, 2))// ss
        {
            expression = begintime.substring(17) + expression;
        } else
        {
            expression = begintime.substring(17) + "-" + endtime.substring(17)
                    + expression;
        }
        return expression;
    }

    /**
     * 得到所有需要提醒的日程
     * 
     * @return
     * @throws ManagerException
     */
    public List getStartSchedular() throws ManagerException
    {

        Date nowDate = new Date();
        DBUtil dbUtil = new DBUtil();
        List list = new ArrayList();

        String sql = "select a.*,b.*  from TD_SD_SCHEDULAR a,td_sd_remind b  where a.status=0 and  b.REMIND_BEGIN_TIME<="
                + SQLManager.getInstance().getDBAdapter()
                        .getDateString(nowDate)
                + " and a.SCHEDULAR_ID=b.SCHEDULAR_ID and b.REMIND_BEGIN_TIME<a.begintime and b.REMIND_END_TIME>="
                + SQLManager.getInstance().getDBAdapter()
                        .getDateString(nowDate) + "";
        try
        {

            dbUtil.executeSelect(sql);
            for (int i = 0; i < dbUtil.size(); i++)
            {
                Schedular sch = new Schedular();
                sch.setSchedularID(dbUtil.getInt(i, "SCHEDULAR_ID"));
                sch.setExecutorID(dbUtil.getInt(i, "EXECUTOR_ID"));
                sch.setBeginTime(dbUtil.getDate(i, "BEGINTIME"));
                sch.setEndTime(dbUtil.getDate(i, "ENDTIME"));
                sch.setPlace(dbUtil.getString(i, "PLACE"));
                sch.setTopic(dbUtil.getString(i, "TOPIC"));
                sch.setEssentiality(dbUtil.getString(i, "ESSENTIALITY"));
                sch.setRemindBeginTime(dbUtil.getDate(i, "REMIND_BEGIN_TIME"));
                sch.setRemindEndTime(dbUtil.getDate(i, "REMIND_END_TIME"));
                sch.setIsSys(dbUtil.getInt(i, "ISSYSTEM"));
                sch.setIsEmail(dbUtil.getInt(i, "ISEMAIL"));
                sch.setIsMessage(dbUtil.getInt(i, "ISMESSAGE"));
                sch.setInterval(dbUtil.getDouble(i, "INTERVAL_TIME"));
                sch.setIntervalType(dbUtil.getInt(i, "INTERVALTYPE"));

                list.add(sch);
            }
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 判断该日程是否需要提醒,需要提醒则返回true,不需要提醒返回false.
     * 
     * @param schedularID
     * @return
     * @throws ManagerException
     */
    public boolean isRemind(int schedularID) throws ManagerException
    {
        boolean r = false;
        Date dt = null;
        DBUtil db = new DBUtil();
        String sql = "select * from td_sd_remind where schedular_id = "
                + schedularID + "";
        try
        {
            db.executeSelect(sql);
            for (int i = 0; i < db.size(); i++)
            {
                dt = db.getDate(i, "remind_begin_time");
            }
            if (dt == null)
            {
                r = false;
            }
            r = true;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 增加便笺
     */
    public int addNotepaper(Notepaper notepaper) throws ManagerException
    {
        int r = -1;
        DBUtil db = new DBUtil();

        String sql = "insert into td_sd_notepaper (content,user_id,time) values('"
                + notepaper.getContent()
                + "',"
                + notepaper.getUserID()
                + ","
                + SQLManager.getInstance().getDBAdapter().getDateString(
                        notepaper.getTime()) + ")";
        try
        {
            r = Integer.parseInt(db.executeInsert(sql).toString());
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated method stub
        return r;
    }

    /**
     * 删除便笺NOTEPAPER_ID= notepaperID的便笺
     */
    public boolean deleteNotepaper(int notepaperID) throws ManagerException
    {
        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "delete from td_sd_notepaper where NOTEPAPER_ID = "
                + notepaperID + "";
        try
        {
            db.executeDelete(sql);
            r = true;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 修改便笺的内容和时间属性
     */
    public boolean modifyNotepaper(Notepaper notepaper) throws ManagerException
    {
        boolean r = false;
        DBUtil db = new DBUtil();
        String sql = "update TD_SD_Notepaper set CONTENT = '"
                + notepaper.getContent()
                + "', time = "
                + SQLManager.getInstance().getDBAdapter().getDateString(
                        notepaper.getTime()) + " where NOTEPAPER_ID = "
                + notepaper.getNotepaparID() + "";
        try
        {
            db.executeUpdate(sql);
            r = true;
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 得到便笺的所有属性
     */
    public Notepaper getNotepaper(int notepaperID) throws ManagerException
    {

        DBUtil db = new DBUtil();
        String sql = "select * from td_sd_notepaper where NOTEPAPER_ID = "
                + notepaperID + "";
        Notepaper notepaper = new Notepaper();
        try
        {
            db.executeSelect(sql);
            for (int i = 0; i < db.size(); i++)
            {
                notepaper.setNotepaparID(db.getInt(i, "NOTEPAPER_ID"));
                notepaper.setContent(db.getString(i, "CONTENT"));
                notepaper.setTime(db.getDate(i, "time"));
                notepaper.setUserID(db.getInt(i, "USER_ID"));
            }

        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return notepaper;
    }

    /**
     * 得到某用户的便笺列表。
     */
    public ListInfo getNotepaperList(int userID, int offset, int maxItem)
            throws ManagerException
    {
        DBUtil db = new DBUtil();
        String sql = "select * from td_sd_notepaper where USER_ID = " + userID
                + " order by time desc";
        try
        {
            db.executeSelect(sql, offset, maxItem);
            ListInfo listInfo = new ListInfo();
            List list = new ArrayList();
            for (int i = 0; i < db.size(); i++)
            {
                Notepaper notepaper = new Notepaper();
                notepaper.setNotepaparID(db.getInt(i, "NOTEPAPER_ID"));
                notepaper.setContent(db.getString(i, "CONTENT"));
                notepaper.setTime(db.getDate(i, "time"));
                notepaper.setUserID(db.getInt(i, "USER_ID"));
                list.add(notepaper);
            }
            listInfo.setDatas(list);
            listInfo.setTotalSize(db.getTotalSize());
            return listInfo;
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw new ManagerException(e.getMessage());
        }
    }

    /**
     * 
     * <p>
     * 功能描述：根据用户id删除所有相关联的表
     * </p>
     * 作 者：lin.jian 创建时间 Oct 12, 2006
     * 
     * @param userID
     * @throws ManagerException
     */
    public void deleteAllSchTableByUserId(int userID) throws ManagerException
    {
        DBUtil db = new DBUtil();
        String sql1 = "delete from td_remindinfo where user_Id = " + userID
                + "";
        String sql2 = "delete from td_sd_notepaper where user_Id = " + userID
                + "";
        String sql3 = "delete from TD_SD_NOTIC where EXECUTOR_ID = " + userID
                + "";
        String sql4 = "delete from TD_SD_SCHEDULAR where EXECUTOR_ID = "
                + userID + " or PLANNER_ID = " + userID + "";
        try
        {
            db.executeDelete(sql1);
            db.executeDelete(sql2);
            db.executeDelete(sql3);
            db.executeDelete(sql4);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

}