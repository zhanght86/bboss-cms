package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class OrgJobUserList extends DataInfoImpl implements Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset, int maxItems) {
//		ListInfo listInfo = new ListInfo();
//		String resTypeId = "user";
//		String jobid ="";
//		String resId = request.getParameter("resId");	//这个是orgid+":"+jobId
//		String[] tmp = resId.split(":");				//获得jobId
//		if(tmp.length == 2){
//			 jobid = tmp[1];
//		}
//		String orgId = request.getParameter("orgId");	
//		//根据机构和岗位取用户列表
//		UserManager userManager = null;
//		try {
//			userManager = SecurityDatabase.getUserManager();
//		} catch (SPIException e) {
//			e.printStackTrace();
//			return null;
//			// TODO Auto-generated catch block
//			
//		}
//		Job job = new Job();
//	    job.setJobId(jobid);
//	    Organization org=new Organization();
//	    org.setOrgId(orgId);
//		List userlist = null;
//		try {
//			PageConfig pageConfig = userManager.getPageConfig();		
// 			pageConfig.setPageSize(maxItems);
// 			pageConfig.setStartIndex((int)offset);
//			userlist = userManager.getUserList(org,job);
//			
// 			listInfo.setTotalSize(pageConfig.getTotalSize());
// 			listInfo.setDatas(userlist);
// 			return listInfo;
//		} catch (ManagerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return null;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
