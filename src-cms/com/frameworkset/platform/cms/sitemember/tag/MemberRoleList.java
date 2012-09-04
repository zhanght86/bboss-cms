package com.frameworkset.platform.cms.sitemember.tag;


import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;
/**
 * 取会员角色类型列表
 * @author Administrator
 *
 */
public class MemberRoleList  extends DataInfoImpl implements java.io.Serializable{
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize) {
		String rolename = request.getParameter("rolename");
		
//		System.out.println("...."+membername);
//		System.out.println("...."+memberRealname);
//		System.out.println("...."+memberType);
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		try {
			String sql ="select * from td_sm_role where 1=1";
      		if(rolename==null){
      			sql ="select * from td_sm_role";
      		}
      		if (rolename != null && rolename.length() > 0) {
				sql = sql +" and role_name like '%"+ rolename +"%'";
			}
      		
//      		System.out.println("...."+sql);
			dbUtil.executeSelect(sql + " order by role_name",(int)offset,maxPagesize);
			List list = new ArrayList();
			if(dbUtil.size()>0){
				for (int i = 0; i < dbUtil.size(); i++) {
					Role role = new Role();
					role.setRoleId(dbUtil.getString(i,"role_id"));
					role.setRoleName(dbUtil.getString(i,"role_name"));
					role.setRoleDesc(dbUtil.getString(i,"role_desc"));
					list.add(role);
				}
				listInfo.setDatas(list);
				listInfo.setTotalSize(dbUtil.getTotalSize());
				return listInfo;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return listInfo;
	}
	
	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
}
