package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import com.frameworkset.platform.sysmgrcore.manager.db.SchedularManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class HistorySchedularList extends DataInfoImpl implements Serializable{
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {

		ListInfo listInfo = new ListInfo();
		String executorID = super.accessControl.getUserID();
		int j = Integer.parseInt(executorID);
		SchedularManagerImpl sch = new SchedularManagerImpl();
		try {
			String sql = "select * from TD_SD_SCHEDULAR where ishistory=1 and executor_id="
					+ j + " order by beginTime";
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
