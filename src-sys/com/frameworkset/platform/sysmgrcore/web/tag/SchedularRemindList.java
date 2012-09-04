package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import com.frameworkset.platform.sysmgrcore.manager.db.SchedularManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class SchedularRemindList extends DataInfoImpl implements Serializable
{
    protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize)
    {

        ListInfo listInfo = new ListInfo();
        String schId = request.getParameter("schId");
        String sql = "select * from TD_SD_SCHEDULAR where 1=1 ";
        int lastindex = 0;
        String schedularId = "";
        int isFirst = 0;
        for (int i = 0; i < schId.length();)
        {
            schedularId = schId.substring(lastindex, schId.indexOf(",",
                    lastindex));
            lastindex = schId.indexOf(",", lastindex) + 1;
            i = lastindex;
            if(isFirst == 0)
            {
            	sql += " and schedular_id ="+Integer.parseInt(schedularId)+" ";
            	isFirst = 1;
            }
            else 
            {
            	sql += " or schedular_id ="+Integer.parseInt(schedularId)+" ";
            }
        }
        SchedularManagerImpl sch = new SchedularManagerImpl();
        try
        {
            sql += " order by begintime";
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