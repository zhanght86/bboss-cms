package com.frameworkset.platform.sysmgrcore.purviewmanager.db;

import java.sql.SQLException;

import org.frameworkset.persitent.util.SQLUtil;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;

public class FunctionDB {
	
	public static SQLUtil sqlUtilInsert = SQLUtil
	.getInstance("org/frameworkset/insert.xml");
	public static  String getUserorgjobinfos(final int user_id) {

		int id = user_id;
		final StringBuffer ora_org_name = new StringBuffer();
		
		PreparedDBUtil preparedDBUtilour = new PreparedDBUtil();

		String sql = new String();
		sql=(sqlUtilInsert.getSQL("FunctionDB_getUserorgjobinfos"));
		try {
			preparedDBUtilour.preparedSelect(sql.toString());
			preparedDBUtilour.setInt(1, id);
//			preparedDBUtilour.setInt(2, id);
//			preparedDBUtilour.setInt(3, id);
			preparedDBUtilour.executePreparedWithRowHandler(new NullRowHandler(){

				@Override
				public void handleRow(Record origine) throws Exception {
					String org_id = origine.getString("org_id");
					
					StringBuffer sb = new StringBuffer();
					try {
					    Organization org = OrgCacheManager.getInstance().getOrganization(org_id);
					    if (org == null) {
                            sb.append(origine.getString("remark5")).append("<机构已失效，机构ID：")
                                .append(org_id).append(">");
					    } else {
					        buildOrg(sb, org);
					    }
					} catch(Exception e) {
					    e.printStackTrace();
					}
					if(ora_org_name.length() == 0){
					
					    ora_org_name.append(sb).append("("+getUserjobinfos(user_id,org_id)+")");
						
					}
					else
					{
						ora_org_name.append("、").append(sb).append("("+getUserjobinfos(user_id,org_id)+")");
						
					}
					
				}});

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
		
		return ora_org_name.toString();
		
	}

	public static void buildOrg(StringBuffer temp, Organization org) {
	    
	    if (org!= null) {
    	    if (org.getParentId() != null && !org.getParentId().equals("0")) {
    	        buildOrg(temp, org.getParentOrg());
    	        temp.append(">");
    	    } 
    	    temp.append(org.getRemark5());
	    } 
	}
	
	
	public static void buildOrg(StringBuffer temp, String orgid) throws ManagerException {
	    
		if(orgid == null)
			return; 
		Organization org = OrgCacheManager.getInstance().getOrganization(orgid);
	    if (org!= null) {
    	    if (org.getParentId() != null && !org.getParentId().equals("0")) {
    	        buildOrg(temp, org.getParentOrg());
    	        temp.append(">");
    	    } 
    	    temp.append(org.getRemark5());
	    } 
	}
	
	public static String buildOrgPath(String orgid) throws ManagerException {
		StringBuffer temp = new StringBuffer();
		buildOrg(temp, orgid);
//		Organization org = OrgCacheManager.getInstance().getOrganization(orgid);
//	    if (org!= null) {
//    	    if (org.getParentId() != null && !org.getParentId().equals("0")) {
//    	        buildOrg(temp, org.getParentOrg());
//    	        temp.append(">");
//    	    } 
//    	    temp.append(org.getRemark5());
//	    } 
		return temp.toString();
	}
	
	public static String getUserjobinfos(final int user_id, final String org_id) {

		final StringBuffer job_name = new StringBuffer();

		PreparedDBUtil preparedDBUtil = new PreparedDBUtil();

		String sql = new String();
		sql=(sqlUtilInsert.getSQL("FunctionDB_getUserjobinfos"));
		
		try {
			preparedDBUtil.preparedSelect(sql.toString());
			preparedDBUtil.setInt(1,user_id);
			preparedDBUtil.setString(2, org_id);
			preparedDBUtil.executePreparedWithRowHandler(new NullRowHandler(){

				@Override
				public void handleRow(Record origine) throws Exception {
					if(job_name.length() == 0){
						job_name.append( origine.getString("job_name"));
					}
					else
					{
						job_name.append( "、" ).append(origine.getString("job_name"));
					    
					}	
					
				}});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return job_name.toString();
	}
	
	
}
