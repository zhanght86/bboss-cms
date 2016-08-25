package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import com.frameworkset.platform.sysmgrcore.purviewmanager.DefaultBussinessCheckImpl;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;
/**
 * @author gao.tang  2008-03-18
 * 岗位关联的机构列表
 */
public class OrgSetJobList extends DataInfoImpl implements Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		
		String jobId = request.getParameter("jobId");
		String remark5 = request.getParameter("remark5")==null?"":request.getParameter("remark5");
		String orgnumber = request.getParameter("orgnumber")==null?"":request.getParameter("orgnumber");
		
		DefaultBussinessCheckImpl bussinessCheck = new DefaultBussinessCheckImpl();
		listInfo = bussinessCheck.orgSetJobList(jobId, remark5, orgnumber, (int)offset, maxPagesize);
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}

}
