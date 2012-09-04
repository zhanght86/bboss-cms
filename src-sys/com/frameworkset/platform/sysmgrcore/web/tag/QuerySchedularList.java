package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import com.frameworkset.platform.sysmgrcore.manager.db.SchedularManagerImpl;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

public class QuerySchedularList extends DataInfoImpl implements Serializable{
	
	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
             int maxPagesize) {
		
		String userId = super.accessControl.getUserID();
		int j = Integer.parseInt(userId);	
		ListInfo listInfo = new ListInfo();

		String sdTopic = request.getParameter("topic");
		String sdPlace = request.getParameter("place");
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
       try{
    	   SchedularManagerImpl sch = new SchedularManagerImpl();
    	   String sql= "select * from TD_SD_SCHEDULAR where  executor_id = "+j+" and status = 0";
    	   if(sdTopic != null && sdTopic.length() > 0){
    		   sql = sql + " and topic like '%"+sdTopic+"%'";
    	   }
    	   if ( sdPlace!=null && sdPlace.length() > 0){
    		   sql = sql + " and place like '%"+sdPlace+"%'";
    	   }
    	  sql = sql + " and begintime >= "+SQLManager.getInstance().getDBAdapter().getDateString(StringUtil.stringToDate(sdBeginTime))+" " +
    	  		" and begintime <= "+SQLManager.getInstance().getDBAdapter().getDateString(StringUtil.stringToDate(sdEndTime))+"" +
    	  		" order by begintime";
    	   listInfo = sch.getSchedularList(sql,(int)offset,maxPagesize); 
       }catch(Exception e){
             e.printStackTrace();
       }
		return listInfo;	
 }	
      
      protected ListInfo getDataList(String arg0, boolean arg1) {
            return null;
      }
}