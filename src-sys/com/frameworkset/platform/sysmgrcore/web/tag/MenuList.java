package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.config.model.ResourceInfo;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.sysmgrcore.entity.MenuSee;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class MenuList  extends DataInfoImpl implements Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		
		try {
			String roleName = request.getParameter("roleName");
			String resName = request.getParameter("resName");
			

			StringBuffer hsql = new StringBuffer("select a.OP_ID,a.RESTYPE_ID,a.RES_NAME,b.ROLE_NAME from" +
					" TD_SM_ROLERESOP a,TD_SM_ROLE b" +
					" where a.role_id = b.role_id");
			
			if (roleName != null && roleName.length() > 0) {
				hsql.append(" and b.ROLE_NAME like '%" + roleName + "%' ");
			}
			if (resName != null && resName.length() > 0) {
				hsql.append(" and a.RES_NAME like '%" + resName+ "%'");
			}
					hsql.append(" and a.RESTYPE_ID ='column' order by a.RESTYPE_ID asc");
//			System.out.println(hsql.toString());
			dbUtil.executeSelect(hsql.toString(),(int)offset,maxPagesize);
			
			MenuSee ms = null;
			List menu = new ArrayList();
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				ms = new MenuSee();
				ResourceManager rm = new ResourceManager();
				com.frameworkset.platform.config.model.Operation op = rm.getOperation(dbUtil.getString(i,"RESTYPE_ID"),dbUtil.getString(i,"OP_ID"));
				String opid = op.getName();
				ms.setOpid(opid);
				ResourceInfo ri = rm.getResourceInfoByType(dbUtil.getString(i,"RESTYPE_ID"));
				String resTypename = ri.getName();
				ms.setResTypeName(resTypename);
				ms.setResName(dbUtil.getString(i,"RES_NAME"));
				ms.setRoleName(dbUtil.getString(i,"ROLE_NAME"));
				menu.add(ms);
				
				
			}
			listInfo.setDatas(menu);
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
