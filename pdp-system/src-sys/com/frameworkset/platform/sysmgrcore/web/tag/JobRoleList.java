package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import org.frameworkset.spi.SPIException;

import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.ResManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class JobRoleList extends DataInfoImpl implements Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		
		String jobId = request.getParameter("jobId");//岗位ID
		String resName = request.getParameter("resName");//资源名称
		String resid = request.getParameter("resid");//资源标识
		String resTypeId = request.getParameter("resTypeId");//资源类型
		String opId = request.getParameter("opId");//资源操作
		try {
			ResManager resManager = SecurityDatabase.getResourceManager();
			listInfo = resManager.getJobRoleList(jobId, resName, resid, resTypeId, opId, offset, maxPagesize);
		} catch (SPIException e) {
			e.printStackTrace();
		} catch (ManagerException e) {
			e.printStackTrace();
		}
		
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}

}
