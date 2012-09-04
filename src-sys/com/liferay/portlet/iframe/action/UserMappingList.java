package com.liferay.portlet.iframe.action;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class UserMappingList extends DataInfoImpl{
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		List list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from  SSO_USER_MAPPING  ");
		try {
			DBUtil db = new DBUtil();
			db.executeSelect("portal",sql.toString(), (int) offset, maxPagesize);
			if (db.size() > 0) { // 当找到了数据时
				for (int i = 0; i < db.size(); i++) {
					UserMapping base = new UserMapping();
					base.setA_APP_ID(db.getString(i, "a_app_id"));
					base.setA_USER_NAME(db.getString(i, "a_user_name"));
					base.setB_APP_ID(db.getString(i, "b_app_id"));
					base.setB_USER_NAME(db.getString(i, "b_user_name"));
					base.setB_USER_PASSWORD(db.getString(i, "b_user_password"));
					base.setUSER_REMARK(db.getString(i, "user_remark"));
					base.setID(db.getInt(i, "id"));
					list.add(base);
				}
				listInfo.setDatas(list);
				listInfo.setTotalSize(db.getTotalSize());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		String id = request.getParameter("ID");
		String sql = "";
		DBUtil db = new DBUtil();
		List infoList = new ArrayList();
		ListInfo listInfo = new ListInfo();
		UserMapping base = new UserMapping();
		try {
			sql = " select * from SSO_USER_MAPPING where id = " + id;
			db.executeSelect("portal",sql.toString());
			if (db.size() > 0) {// 当找到了数据时
				base.setA_APP_ID(db.getString(0, "a_app_id"));
				base.setA_USER_NAME(db.getString(0, "a_user_name"));
				base.setB_APP_ID(db.getString(0, "b_app_id"));
				base.setB_USER_NAME(db.getString(0, "b_user_name"));
				base.setB_USER_PASSWORD(db.getString(0, "b_user_password"));
				base.setUSER_REMARK(db.getString(0, "user_remark"));
				base.setID(db.getInt(0, "id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		infoList.add(base);
		listInfo.setDatas(infoList);
		listInfo.setTotalSize(db.getTotalSize());
		return listInfo;
	}
}
