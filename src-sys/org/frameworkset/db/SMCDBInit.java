package org.frameworkset.db;

import java.sql.SQLException;
import java.util.List;

import org.frameworkset.persitent.util.SQLUtil;
import org.frameworkset.spi.assemble.Pro;

import com.frameworkset.common.poolman.DBUtil;

public class SMCDBInit {
	
	
	
	public static final String[] propertys ={"addtableinfo_seqlist","addtableinfo_list","addtableinfo_seqlist","addtb_sm_inputtype_list",
		"addtd_sm_dicttype_list","addtd_sm_dictdata_list","addtd_sm_job",
		"addtd_sm_roleresop_list","addtd_sm_roletype","addtd_sm_user",
		"addtd_sm_role_list","addtd_sm_userrole","addmq_rules_tpl_list"};
	
	public static void insertDB() throws SQLException{
		DBUtil dbUtil = new DBUtil();
		for(String property : propertys){
			System.out.println("property = " + property);
			
			SQLUtil sqlUtil = SQLUtil.getInstance("");
			try{
				List<Pro> addList = sqlUtil .getListSQLs(property);
				String dbname = sqlUtil.getDBName(property);
				
				for(Pro pro : addList){
					String sql = pro.toString();
//     				System.out.println("sql = " + sql);
					dbUtil.executeInsert(dbname, sql);
	//				dbUtil.addBatch(sql);
				}
			}catch(Exception e){
				e.printStackTrace();
				try{
					String dbname = sqlUtil.getDBName(property);
					dbUtil.executeInsert(dbname, sqlUtil.getSQL(property));
				}catch(Exception e1){
					e1.printStackTrace();
				}
			}
		}
//		dbUtil.executeBatch("mq");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			insertDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
