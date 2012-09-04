package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.Date;

import com.frameworkset.platform.sysmgrcore.manager.db.SchedularManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class BookingSchedularList extends DataInfoImpl implements Serializable
{
    protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize)
    {

        Date dt = new Date();
        ListInfo listInfo = new ListInfo();
        String executorID = super.accessControl.getUserID();
        int j = Integer.parseInt(executorID);
        SchedularManagerImpl sch = new SchedularManagerImpl();
        try
        {
            String sql = "select a.*,b.user_name,b.user_realname from TD_SD_SCHEDULAR a,td_sm_user b where a.executor_id = "
                    + j
                    + " and a.planner_id=b.user_id and a.type = '他人预约' order by schedular_id desc";
            listInfo = sch.getSchedularAndNameList(sql, (int) offset,
                    maxPagesize);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return listInfo;
    }

    protected ListInfo getDataList(String arg0, boolean arg1)
    {
        // TODO Auto-generated method stub
        return null;
    }
}