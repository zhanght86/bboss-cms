package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.RoleType;
import com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class RoleTypeList extends DataInfoImpl implements Serializable
{
	protected ListInfo getDataList(String sortKey, boolean desc, long offset, int maxPagesize)
	{
		String roleTypeName = request.getParameter("typeName");
		String roleTypeDesc = request.getParameter("typeDesc");
		RoleType rt;
		ListInfo listInfo = new ListInfo();	
		List list = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		RoleTypeManager rtm = new RoleTypeManager();
		String condition = "";
		
		try {
			List roleTypeList = rtm.getTypeNameList();
			
			boolean rolemanager = super.accessControl.checkPermission("globalrole",
					"rolemanager",
					AccessControl.ROLE_RESOURCE);
				
			
			for(int i = 0; i < roleTypeList.size(); i ++){
				RoleType roleType = (RoleType)roleTypeList.get(i);
				String roleTypeId = roleType.getRoleTypeID();
				boolean isRoletype = false;
				if(rolemanager || roleType.getCreatorOrgId().equals(super.accessControl.getChargeOrgId()) ){
					isRoletype = true;
				}
				if(isRoletype){
					if("".equals(condition)){
						condition = "'" + roleTypeId + "'";
					}else{
						condition += "," + "'" + roleTypeId + "'";
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		try
		{
			StringBuffer sql = new StringBuffer("select * from TD_SM_ROLETYPE where 1=1 ");
			if(!super.accessControl.isAdmin()){
				if(!"".equals(condition)){
					sql.append(" and type_id in(").append(condition).append(") "); 
				}
			}
			if(roleTypeName!=null&&roleTypeName.length()>0)
			{
				sql.append(" and TYPE_NAME like '%"+ roleTypeName + "%' ");
				
			}
			if(roleTypeDesc!=null&&roleTypeDesc.length()>0)
			{
				sql.append(" and TYPE_desc like '%"+ roleTypeDesc + "%' ");
				
			}
			sql.append(" order by type_id");
			dbUtil.executeSelect(sql.toString(),(int)offset,maxPagesize);
			if(dbUtil.size()>0)
			{
				for (int i = 0; i < dbUtil.size(); i++)
				{
					rt = new RoleType();
					rt.setRoleTypeID(dbUtil.getString(i,"type_id"));
					rt.setTypeName(dbUtil.getString(i,"type_name"));
					rt.setTypeDesc(dbUtil.getString(i,"type_desc"));
					rt.setCreatorOrgId(dbUtil.getString(i, "creator_org_id"));
					rt.setCreatorUserId(dbUtil.getString(i, "creator_user_id"));
					list.add(rt);
				}
				listInfo.setDatas(list);
				listInfo.setTotalSize(dbUtil.getTotalSize());
				return listInfo;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return listInfo;
	}
	protected ListInfo getDataList(String arg0, boolean arg1)
	{
		return null;
	}
}
