package com.frameworkset.platform.sysmgrcore.purviewmanager;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;


public class GroupSearchList extends DataInfoImpl implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(GroupSearchList.class
			.getName());
	
	protected ListInfo  getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize) {
		
		String groupName = StringUtil.replaceNull(request.getParameter("groupName"));
		
		String groupDesc = StringUtil.replaceNull(request.getParameter("groupDesc"));
		String groupOwnerName = StringUtil.replaceNull(request.getParameter("groupOwnerName"));
		
		String sql = "select g.* from td_sm_group g,td_sm_user u where u.user_id=g.owner_id";
	
			try {
				
				GroupManager groupManager = SecurityDatabase.getGroupManager();
				
				//构造查询语句:条件 groupName groupDesc
				if(!groupName.equals("")){
					
					sql += " and g.group_name like '%" +groupName + "%'";
				}
				
	
				if(!groupDesc.equals("")){
					
					sql +=" and g.group_desc like '%" + groupDesc+ "%'";
					
					}
				
				if(!groupOwnerName.equals("")){
					
						sql += " and  u.user_name like '%" + groupOwnerName+ "%'";
					}
				//System.out.println(sql);				
				return  groupManager.getGroupList(sql,offset,maxPagesize);
				
			} catch (Exception e) {
				logger.error("",e);
			}
			return null;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		
		return null;
	}

	

	

}
