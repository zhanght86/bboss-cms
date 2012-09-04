package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.sysmgrcore.entity.LogModule;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class LogModuleList extends DataInfoImpl implements Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
	
		try {
	
			String hsql ="select * from TD_SM_LOGMODULE order by logmodule";
			
		

			dbUtil.executeSelect(hsql,(int)offset,maxPagesize);
		
			LogModule lm = null;
			List logmodules = new ArrayList();
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				lm = new LogModule();
				lm.setId(dbUtil.getString(i,"id"));
				lm.setLOGMODULE(dbUtil.getString(i,"LOGMODULE"));
				lm.setMODULE_DESC(dbUtil.getString(i,"MODULE_DESC"));
				lm.setSTATUS(String.valueOf(dbUtil.getInt(i,"status")));
				if(lm.getLOGMODULE().equals("岗位管理") 
						&& !ConfigManager.getInstance().getConfigBooleanValue("enablejobfunction", false))
				{
					//如果岗位不使用且当前模块为岗位
				}
				else
				{
					logmodules.add(lm);
				}				
			}
			listInfo.setDatas(logmodules);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
}