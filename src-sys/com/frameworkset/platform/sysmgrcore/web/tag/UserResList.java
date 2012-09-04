package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.config.ConfigException;
import com.frameworkset.platform.config.model.ResourceInfo;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.sysmgrcore.entity.UserRes;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class UserResList  extends DataInfoImpl implements Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		
		String userId = request.getParameter("userId");
		if(userId == null)
			userId = (String)request.getAttribute("userId");
		try {
			  String restype = request.getParameter("restype");
			  String resname = request.getParameter("resname");
              StringBuffer hsql = new StringBuffer("select b.op_id,b.res_id,b.res_name,b.restype_id,c.role_name" +
					" from td_sm_roleresop b," +
					"td_sm_role c where " +
					"b.role_id = c.role_id and b.types = 'role' and c.role_id in ("
							+ "select ur.role_id from td_sm_userrole ur where ur.user_id = '"
							+ userId
							+ "' union  select gr.role_id from td_sm_grouprole gr where gr.group_id in "
							+ " (select ug.group_id from td_sm_usergroup ug where ug.user_id = '"
							+ userId + "') "
							+" union  select orgr.role_id from td_sm_orgrole orgr where orgr.org_id in "
							+ " (select ujo.org_id from td_sm_userjoborg ujo where ujo.user_id = '"
							+ userId + "') )");
            
			if (restype != null && restype.length() > 0 && !String.valueOf(restype).equals("undefined")) {
				hsql.append(" and restype_id = '" + restype + "' ");
			}
			if (resname != null && resname.length() > 0) {
				hsql.append(" and res_name like '%" + resname
								+ "%'");
			}
			
			hsql.append(" order by b.op_id asc");
			//System.out.println(hsql.toString());
			ResourceManager rsManager = new ResourceManager();			
			
			dbUtil.executeSelect(hsql.toString(),(int)offset,maxPagesize);
			UserRes ur = null;
			List users = new ArrayList();
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				//判断操作类型是否存在 -- gao.tang 2007.11.09
				String opName = "";
				com.frameworkset.platform.config.model.Operation operation  = null;
				try {
					String gresid  = rsManager.getGlobalResourceid(dbUtil.getString(i,"restype_id"));
					if(gresid == null || !gresid.equals(dbUtil.getString(i,"RES_ID")))
					{
						operation  = rsManager.getOperation(dbUtil.getString(i,"restype_id"),dbUtil.getString(i, "op_id"));
					}
					else
					{
						operation  = rsManager.getGlobalOperation(dbUtil.getString(i,"restype_id"),dbUtil.getString(i, "op_id"));
					}
				} catch (ConfigException e) {
					e.printStackTrace();
				}
				//com.frameworkset.platform.config.model.Operation operation  = rsManager.getOperation(dbUtil.getString(i,"restype_id"),dbUtil.getString(i, "op_id"));
				if(operation != null)
				{
				    opName = operation.getName();
				}
				else
				{
					opName = "未知";
				}
				
				//判断资源类型名称是否存在
				String typeName = "";
				ResourceInfo resourceInfo = rsManager.getResourceInfoByType(dbUtil.getString(i,"restype_id"));
				if(resourceInfo != null)
					typeName = resourceInfo.getName();		
				else
				{
					typeName = "未知"; 
				}
				
				ur = new UserRes();
				//String restypeid = dbUtil.getString(i,"restype_id");
				//rsManager.getOperation(restypeid,dbUtil.getString(i,"op_id"));
				ur.setOpId(dbUtil.getString(i,"op_id"));
				ur.setResName(dbUtil.getString(i,"res_name"));
				ur.setRoleName(dbUtil.getString(i,"role_name"));
				ur.setOpName(opName);
				ur.setResTypeName(typeName);
				users.add(ur);
				
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

