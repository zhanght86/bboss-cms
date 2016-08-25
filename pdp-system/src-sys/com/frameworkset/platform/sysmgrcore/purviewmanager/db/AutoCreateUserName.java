package com.frameworkset.platform.sysmgrcore.purviewmanager.db;

import java.sql.SQLException;
import java.util.Map;

import com.frameworkset.platform.config.ConfigException;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.sysmgrcore.purviewmanager.GenerateService;
import com.frameworkset.common.poolman.DBUtil;

public class AutoCreateUserName implements GenerateService{
//	/**
//	 * 序列自动生成用户登陆名
//	 * @param orgId
//	 * @return 返回登陆名称
//	 */
//	public String getSeqUserName(String orgId){
//		String userName = "";
//		DBUtil db = new DBUtil();
//		//省局用户登陆名生成
//		String sql_province = "select SEQ_AUTO_PROVINCE_USERNAME.nextval as seqval from dual";
//		//省局以外的用户登陆名生成
//		String sql_noprovince = "select SEQ_AUTO_NOPROVINCE_USERNAME.nextval as seqval from dual";
//		try {
//			if("1".equals(orgId)){
//				db.executeSelect(sql_province);
//			}else{
//				db.executeSelect(sql_noprovince);
//			}
//			if(db.getString(0, 0) != null){
//				userName = db.getString(0, 0); 
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return userName;
//	}

	public boolean enableUserNameGenerate() {
		boolean  isAutoCreateUserName = false;
		try {
			isAutoCreateUserName = ConfigManager.getInstance().getConfigBooleanValue("isAutoCreateUserName");
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		return isAutoCreateUserName;
	}

	public String generateUserName(Map preferences) throws Exception {
		String userName = "";
		String orgId = (String)preferences.get("orgId"); 
		DBUtil db = new DBUtil();
		DBUtil dbprovince = new DBUtil();
		//根据传入的ID判断是否是省局机构
		StringBuffer sql = new StringBuffer()
			.append("SELECT COUNT (*) FROM (SELECT * FROM td_sm_organization START WITH org_id IN (")
			.append(" SELECT org_id FROM td_sm_organization WHERE parent_id = '1' AND (isdirectlyparty = '1' ")
			.append(" OR org_level = '0')) CONNECT BY PRIOR org_id = parent_id UNION SELECT * ")
			.append(" FROM td_sm_organization WHERE org_id = '1') WHERE org_id = '")
			.append(orgId).append("'");
		
		//省局用户登陆名生成
		String sql_province = "select SEQ_AUTO_PROVINCE_USERNAME.nextval as seqval from dual";
		//省局以外的用户登陆名生成
		String sql_noprovince = "select SEQ_AUTO_NOPROVINCE_USERNAME.nextval as seqval from dual";
		try {
			dbprovince.executeSelect(sql.toString());
			while(true){
				if(dbprovince.getInt(0, 0) > 0){
					try{
						db.executeSelect(sql_province);
					}catch(SQLException e){
						db.executeSelect(sql_noprovince);
					}
				}else{
					db.executeSelect(sql_noprovince);
				}
				if(db.getString(0, 0) != null){
					userName = db.getString(0, 0); 
				}
				String sqlU = "select count(1) from td_sm_user where user_name='"+userName+"'";
				DBUtil dbU = new DBUtil();
				dbU.executeSelect(sqlU);
				if(dbU.getInt(0, 0) == 0){
					break;
				}
			}
			//System.out.println("生成的用户名为： " + userName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userName;
	}
}
