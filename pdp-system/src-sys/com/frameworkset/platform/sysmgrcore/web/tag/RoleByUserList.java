package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class RoleByUserList extends DataInfoImpl implements Serializable{
	
	protected ListInfo getDataList(String sortKey, boolean desc, 
			long offset,int maxPagesize) {
String userId = request.getSession().getAttribute("userId").toString();
	   	
	   	DBUtil db = new DBUtil();
	   	String sql = " select r.role_id,r.role_name,r.role_desc, u.user_name from td_sm_role r"
	   		+" inner join td_sm_user u on user_id = "+userId
	   		+" where r.role_id in ( select role_id from td_sm_userrole where user_id = "+userId+")";
	   	List roles = new ArrayList();
	   	ListInfo listInfo = new ListInfo();
	   	try {
	   		db.executeSelect(sql,offset,maxPagesize);
	   		for (int i = 0; i < db.size(); i++) {
				Role role = new Role();
				role.setRoleId(db.getString(i,"ROLE_ID"));
				role.setRoleName(db.getString(i,"ROLE_NAME"));
				role.setRoleDesc(db.getString(i,"ROLE_DESC"));
				role.setRemark1(db.getString(i,"USER_NAME"));
				roles.add(role);
			}
	   		
	   		listInfo.setDatas(roles);
	   		listInfo.setTotalSize(db.getTotalSize());
	   		
	   	} catch (Exception e) {
	   		e.printStackTrace();
	   	}
	   	
		return listInfo;
	   	
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO 自动生成方法存根
		return null;
	}
}
