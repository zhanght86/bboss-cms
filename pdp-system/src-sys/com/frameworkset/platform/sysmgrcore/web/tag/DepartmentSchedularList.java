package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import com.frameworkset.platform.sysmgrcore.manager.db.SchedularManagerImpl;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

public class DepartmentSchedularList extends DataInfoImpl implements Serializable
{
    protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize)
    {

        ListInfo listInfo = new ListInfo();
        String executorID = super.accessControl.getUserID();
        int j = Integer.parseInt(executorID);
        String sdTopic = request.getParameter("topic");
        String userName = request.getParameter("userName");
        String sdBeginTime = request.getParameter("beginTime");
        String sdEndTime = request.getParameter("endTime");
        if(sdBeginTime == null || sdBeginTime.length()==0)
        {
           sdBeginTime="1000-03-02 12:00:00";
        }
        if(sdEndTime == null || sdEndTime.length()==0)
        {
            sdEndTime="3000-03-02 12:00:00";
        }
        
        SchedularManagerImpl sch = new SchedularManagerImpl();
        try
        {
            String sql = "select  a.* ,b.user_name,b.user_realname from td_sd_schedular a,td_sm_user b "
                    + "where begintime > "+SQLManager.getInstance().getDBAdapter().getDateString(StringUtil.stringToDate(sdBeginTime))+" " +
                    " and begintime < "+SQLManager.getInstance().getDBAdapter().getDateString(StringUtil.stringToDate(sdEndTime))+"";
            if(sdTopic != null && sdTopic.length() > 0){
                sql = sql + " and topic like '%"+sdTopic+"%'";
            }
            if ( userName!=null && userName.length() > 0){
                sql = sql + " and b.user_name like '%"+userName+"%'";
            }
            
            sql += " and a.status=0 and a.isopen=1 and a.executor_id!=" + j + " and a.executor_id=b.user_id "
                    + "and a.executor_id in (select distinct  c.user_id  from td_sd_schedular a,td_sm_userjoborg c "
                    + "where c.org_id in (select c.org_id from td_sm_userjoborg c where c.user_id=a.executor_id "
                    + "and a.executor_id=" + j + ")) order by schedular_id desc";
            
            listInfo = sch.getSchedularAndNameList(sql, (int) offset, maxPagesize);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return listInfo;
    }

    protected ListInfo getDataList(String arg0, boolean arg1)
    {
        return null;
    }
}