package com.chinacreator.sysmgrcore.purviewmanager.db;

import java.sql.SQLException;

import com.chinacreator.sysmgrcore.manager.db.GroupManagerImpl;
import com.chinacreator.sysmgrcore.manager.db.OrgManagerImpl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;

public class OrgQuery {
	public static void main(String[] args) throws SQLException
	{
		
		String concat = DBUtil.getDBAdapter().concat("'来自用户自身角色【'","b.role_name","'】'");
		System.out.println(concat);
//		updateOrg("0");
//		updateGroup("0");
	}
	
	public static void updateOrg(String parentID) throws SQLException
	{
		
		
		String sql = "select org_id from TD_SM_ORGANIZATION where parent_id='" + parentID+ "'";
		String update = "update TD_SM_ORGANIZATION set org_tree_level=? where org_id=?";
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.executeSelect(sql);
		for(int i = 0; i < dbutil.size(); i ++)
		{
			String org_id = dbutil.getString(i,"org_id");
			String org_sn = OrgManagerImpl.getorgSN(org_id);
			String org_level = OrgManagerImpl.getOrgTreeLevel(parentID, org_sn);
			PreparedDBUtil dbutil_ = new PreparedDBUtil();
			dbutil_.preparedUpdate(update);
			dbutil_.setString(1, org_level);
			dbutil_.setString(2, org_id);
			dbutil_.executePrepared();
			updateOrg(org_id);
		}
	}
		
		
		public static void updateGroup(String parentID) throws SQLException
		{
			
			
			String sql = "select group_id from TD_SM_GROUP where parent_id='" + parentID+ "'";
			String update = "update TD_SM_GROUP set group_tree_level=? where group_id=?";
			PreparedDBUtil dbutil = new PreparedDBUtil();
			dbutil.executeSelect(sql);
			
			for(int i = 0; i < dbutil.size(); i ++)
			{
				
				String group_id = dbutil.getString(i,"group_id");
//				String org_sn = OrgManagerImpl.getorgSN(org_id);
//				String org_level = OrgManagerImpl.getOrgTreeLevel(parentID, org_sn);
				String group_level = GroupManagerImpl.getGroupTreeLevel(parentID, group_id);
				PreparedDBUtil dbutil_ = new PreparedDBUtil();
				dbutil_.preparedUpdate(update);
				dbutil_.setString(1, group_level);
				dbutil_.setString(2, group_id);
				dbutil_.executePrepared();
				
				updateGroup(group_id);
			}
		
	}
	
	
}
