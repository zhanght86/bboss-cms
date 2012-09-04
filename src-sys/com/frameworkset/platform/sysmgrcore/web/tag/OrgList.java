package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class OrgList extends DataInfoImpl implements Serializable{
	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	            int maxPagesize) {
	    	
	        ListInfo listInfo = new ListInfo();
	        String orgId = request.getParameter("orgId");
	        String orgname = request.getParameter("orgname");
	        String orgnumber = request.getParameter("orgnumber");
	        	      
			DBUtil dbUtil = new DBUtil();
	
			try {
				String sql = "select * from TD_SM_ORGANIZATION where 1=1 ";
				if (orgname != null && orgname.length() > 0) {
					 sql += " and org_Name like '%" + orgname + "%'";
				}
				if (orgnumber != null && orgnumber.length() > 0) {
					 sql += " and orgnumber like '%" + orgnumber + "%'";
				}
				
				dbUtil.executeSelect(sql,(int)offset,maxPagesize);
				
				Organization org = null;
				List users = new ArrayList();
				for(int i = 0; i < dbUtil.size(); i ++)
				{
					org = new Organization();
					org.setOrgId(dbUtil.getString(i,"ORG_ID"));
					org.setOrgName(dbUtil.getString(i,"ORG_NAME"));
					org.setRemark5(dbUtil.getString(i,"REMARK5"));
					org.setOrgnumber(dbUtil.getString(i,"ORGNUMBER"));
					
					users.add(org);
					
				}
				listInfo.setDatas(users);
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


