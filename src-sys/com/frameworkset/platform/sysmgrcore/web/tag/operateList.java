package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.platform.config.model.Operation;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class operateList extends DataInfoImpl implements Serializable{
    
	private Logger log = Logger.getLogger(operateList.class);
    
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		String restypeId = request.getParameter("restypeId");
		ListInfo listInfo = new ListInfo();		
		try {
			ResourceManager rm = new ResourceManager();
			List list = new ArrayList();
			Operation opp = null;
			list = rm.getOperations(restypeId);
			listInfo = pagerList(list,(int)offset,maxPagesize);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.String,
	 *      boolean)
	 */
	protected ListInfo getDataList(String arg0, boolean arg1) {
		
		return null;
	}
}