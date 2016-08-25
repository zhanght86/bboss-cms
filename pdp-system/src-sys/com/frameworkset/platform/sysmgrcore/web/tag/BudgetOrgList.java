package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class BudgetOrgList extends DataInfoImpl implements Serializable{

	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	            int maxPagesize) {
	    	
	        ListInfo listInfo = new ListInfo();
	        String resId = request.getParameter("resId");
			
			String[] tmp = resId.split(":");
			String orgid1 = tmp[0]; //主管处室
			String jobid = tmp[1]; //主管岗位
	        	      
			DBUtil dbUtil = new DBUtil();
	
			try {
				String sql = "select * from TD_SM_ORGANIZATION where CHARGEORGID='" + orgid1 + "' and" +
						" SATRAPJOBID='" + jobid + "'";
				
				
				dbUtil.executeSelect(sql,(int)offset,maxPagesize);
				
				Organization org = null;
				List orgs = new ArrayList();
				for(int i = 0; i < dbUtil.size(); i ++)
				{
					org = new Organization();
					org.setOrgId(dbUtil.getString(i,"ORG_ID"));
					org.setOrgName(dbUtil.getString(i,"ORG_NAME"));
					org.setRemark5(dbUtil.getString(i,"REMARK5"));
					org.setOrgnumber(dbUtil.getString(i,"ORGNUMBER"));
					
					orgs.add(org);
					
				}
				listInfo.setDatas(orgs);
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
