package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.Date;

import com.frameworkset.platform.sysmgrcore.manager.db.SchedularManagerImpl;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class LatestSchedularList extends DataInfoImpl implements Serializable
{
    protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize)
    {
        Date dt = new Date();
        Date dt1 = new Date();
        dt1.setDate(dt1.getDate() + 3);

        ListInfo listInfo = new ListInfo();
        String executorID = super.accessControl.getUserID();
        int j = Integer.parseInt(executorID);
        SchedularManagerImpl sch = new SchedularManagerImpl();
        try
        {
            sch.generateCrontime(dt, dt1, 20, 1);
            String sql = "select * from TD_SD_SCHEDULAR where status = 0 and executor_id = "
                    + j
                    + " "
                    + "and endtime >= "
                    + SQLManager.getInstance().getDBAdapter().getDateString(dt)
                    + ""
                    + "and begintime <= "
                    + SQLManager.getInstance().getDBAdapter()
                            .getDateString(dt1) + "" + " order by begintime";
            listInfo = sch.getSchedularList(sql, (int) offset, maxPagesize);
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
