package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import com.frameworkset.platform.sysmgrcore.manager.db.SchedularManagerImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class NotepaperList extends DataInfoImpl implements Serializable{
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		String executorID = super.accessControl.getUserID();
		SchedularManagerImpl sch = new SchedularManagerImpl();
		try {
            listInfo = sch.getNotepaperList(Integer.parseInt(executorID),(int) offset, maxPagesize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}

}
