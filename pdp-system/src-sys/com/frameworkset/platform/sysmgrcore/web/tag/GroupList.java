package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.sysmgrcore.manager.db.GroupManagerImpl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

public class GroupList extends DataInfoImpl implements Serializable{

	private static final Logger logger = LoggerFactory.getLogger(GroupList.class
			.getName());

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		try {
			ListInfo listInfo = new ListInfo();
			String groupName = StringUtil.replaceNull(request
					.getParameter("groupName"));

//			GroupManager gm = SecurityDatabase.getGroupManager();
//			PageConfig pc = gm.getPageConfig();
//			pc.setStartIndex((int) offset);
//			pc.setPageSize(maxPagesize);

			if (groupName.length() == 0) {
//				List list = gm.getGroupList();
//				listInfo.setTotalSize(pc.getTotalSize());
//				listInfo.setDatas(list);
				listInfo = getGroupList(offset,maxPagesize);
			} else {
//				List list = gm.getGroupList("group_Name", "%" + groupName + "%", true);
//				listInfo.setTotalSize(pc.getTotalSize());
//				listInfo.setDatas(list);
				listInfo = getGroupList(groupName,offset,maxPagesize);
			}

			return listInfo;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}
	
	private ListInfo getGroupList(long offset,int maxPagesize){
		ListInfo listinfo = new ListInfo();
		List list = new ArrayList();
		try {
			String sql = "select * from td_sm_group t";
			DBUtil db = new DBUtil();
			db.executeSelect(sql,offset,maxPagesize);
			list = new GroupManagerImpl().dbutilToGroupList(db);
			listinfo.setTotalSize(db.getTotalSize());
			listinfo.setDatas(list);
		} catch (Exception e) {
			logger.error("",e);
			e.printStackTrace();
		}
		return listinfo;
	}
	
	private ListInfo getGroupList(String groupName,long offset,int maxPagesize){
		ListInfo listinfo = new ListInfo();
		String sql = "select * from td_sm_group where group_name like '%" + groupName + "%'";
		DBUtil db = new DBUtil();
		try {
			List list = new ArrayList();
			db.executeSelect(sql,offset,maxPagesize);
			list = new GroupManagerImpl().dbutilToGroupList(db);
			listinfo.setTotalSize(db.getTotalSize());
			listinfo.setDatas(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listinfo;
	}

}
