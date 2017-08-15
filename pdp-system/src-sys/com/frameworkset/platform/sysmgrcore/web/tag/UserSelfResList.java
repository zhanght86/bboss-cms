package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.config.ConfigException;
import com.frameworkset.platform.config.model.Operation;
import com.frameworkset.platform.config.model.ResourceInfo;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.sysmgrcore.entity.UserSelfRes;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * 
 * 描述： 用户自身权限列表
 * @author meiyu.liu 
 * version 1.0
 * 日期：@date 
 * 公司：bbossgroups
 */
public class UserSelfResList  extends DataInfoImpl implements Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		
		String userId = request.getParameter("userId");
		if(userId == null)
			userId = (String)request.getAttribute("userId");
		try {
			  String restype = request.getParameter("restype1");
			  String resname = request.getParameter("resname1");
              StringBuffer hsql = new StringBuffer("select b.op_id,b.res_id,b.res_name,b.restype_id" +
					" from td_sm_roleresop b" +
					" where b.role_id = '"+userId+"' and b.types ='user'");
			
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
			UserSelfRes ur = null;
			List users = new ArrayList();
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				ur = new UserSelfRes();
				String typeName = "";
				ResourceInfo resourceInfo =  rsManager.getResourceInfoByType(dbUtil.getString(i, "restype_id"));
				if(resourceInfo != null){
					typeName = resourceInfo.getName();
				}else{
					typeName = "未知";
				}
				String opName = "";
				//Operation operation = rsManager.getOperation(dbUtil.getString(i,"restype_id"),dbUtil.getString(i, "op_id"));
				Operation operation  = null;
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
				if(operation != null){
					opName = operation.getName();
				}else{
					opName = "未知";
				}
//				String restypeid = dbUtil.getString(i,"restype_id");
//				rsManager.getOperation(restypeid,dbUtil.getString(i,"op_id"));
				ur.setOpId(dbUtil.getString(i,"op_id"));
				ur.setResName(dbUtil.getString(i,"res_name"));
				//ur.setUserName(dbUtil.getString(i,"user_name"));
//				ur.setOpName(rsManager.getOperation(restypeid,dbUtil.getString(i,"op_id")).getName());
//				ur.setResTypeName(rsManager.getResourceInfoByType(restypeid).getName());
				ur.setResTypeName(typeName);
				ur.setOpName(opName);
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

