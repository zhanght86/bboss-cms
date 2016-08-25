package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.Date;

import com.frameworkset.platform.sysmgrcore.manager.db.SchedularManagerImpl;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class OverTimeSchedularList extends DataInfoImpl implements Serializable{
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {

		Date dt = new Date();
		ListInfo listInfo = new ListInfo();
		String executorID = super.accessControl.getUserID();
		int j = Integer.parseInt(executorID);
		SchedularManagerImpl sch = new SchedularManagerImpl();
		try {
			String sql = "select * from TD_SD_SCHEDULAR where status = 0 and executor_id = "
					+ j
					+ " "
					+ "and endtime < "
					+ SQLManager.getInstance().getDBAdapter().getDateString(dt)
					+ "" + " order by begintime";
			listInfo = sch.getSchedularList(sql, (int) offset, maxPagesize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
