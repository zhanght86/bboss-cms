package org.frameworkset.esb.datareuse.datasource.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.frameworkset.esb.datareuse.datasource.dao.DatasourceDao;
import org.frameworkset.esb.datareuse.datasource.dao.impl.DatasourceDaoImpl;
import org.frameworkset.esb.datareuse.datasource.entity.DatasourceBean;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.util.JDBCPool;
import com.frameworkset.common.poolman.util.JDBCPoolMetaData;
import com.frameworkset.common.poolman.util.SQLUtil;

public class DynamicDatasoureManagerUtil {
	private static byte[] synObj = new byte[0];
	private static DatasourceDao datasourceDao = new DatasourceDaoImpl();

	public static void loadDynamicDatasource(String dsName) throws Exception {
		DatasourceBean obj = (DatasourceBean) datasourceDao
				.findObjectById(dsName);
		loadDynamicDatasource(obj);
	}
	
	public static void stopDynamicDatasource(String dsName) throws Exception {
		 SQLUtil.stopPool(dsName);
	}

	public static void loadDynamicDatasource(DatasourceBean obj)
			throws Exception {		
		if (obj != null && obj.getDs_name() != null
				&& obj.getDs_name().trim().length() > 0) {
			synchronized (synObj) {
				String poolname = obj.getDs_name();
				JDBCPoolMetaData meta = SQLUtil.getJDBCPoolMetaData(poolname);
				//如果数据源未加载，或已经停止
				if (meta == null ||  DBUtil.getStatus(poolname).equals("stop")) {
					String readonly = "false";
					if (obj.getRead_only().equals("1")) {
						readonly = "true";
					}

					boolean userpool = obj.getUse_pool().equals("1") ? true
							: false;
					boolean showSql = obj.getShow_sql().equals("1") ? true
							: false;

					if (obj.getConnection_type().equals("custom_jdbc")) {
						// 创建数据源
						// SQLUtil.startPool(poolname, obj.getDriver(),
						// obj.getJdbc_url(), obj.getUsername(),
						// obj.getPassword(), readonly,
						// obj.getValidation_query());

						SQLUtil.startPool(poolname, obj.getDriver(), obj
								.getJdbc_url(), obj.getUsername(), obj
								.getPassword(), readonly, "READ_COMMITTED", obj
								.getValidation_query(), obj.getJndi_name(), obj
								.getInitial_connections(), obj
								.getMinimum_size(), obj.getMaximum_size(),
								userpool, false, null, showSql);

					} else {

						SQLUtil.startPool(poolname, obj.getDriver(), obj
								.getJdbc_url(), obj.getUsername(), obj
								.getPassword(), readonly, "READ_COMMITTED", obj
								.getValidation_query(), obj.getJndi_name(), obj
								.getInitial_connections(), obj
								.getMinimum_size(), obj.getMaximum_size(),
								userpool, true, obj.getExt_jndi_name(), showSql);

					}

				}
			}
		}
	}

	public static boolean validationJdbcConfig(DatasourceBean obj)
			throws Exception {

		Connection con = null;
		java.sql.Statement stmt = null;

		try {
			if (obj.getConnection_type().equals("custom_jdbc")) {
				String driverClassName = obj.getDriver();
				String user = obj.getUsername();
				String pwd = obj.getPassword();
				String url = obj.getJdbc_url();

				Class.forName(driverClassName);
				con = DriverManager.getConnection(url, user, pwd);				
			} else {
				
				DataSource dataSource = JDBCPool.find(obj.getExt_jndi_name());				
				con = dataSource.getConnection();
			}

			String testSql = obj.getValidation_query();
			
			if (testSql != null && testSql.trim().length() > 0) {
				try {
					stmt = con.createStatement();
					stmt.execute(testSql);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					return false;
				}				
			}

			return true;
		} catch (Exception e) {
			// e.printStackTrace();
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
	}
	

	// public static boolean validationJdbcConfig(String driverClassName,String
	// url,String user,String pwd,String testSql) throws Exception
	// {
	// Connection con = null;
	// java.sql.Statement stmt = null;
	//		
	// try {
	// Class.forName(driverClassName);
	//			
	// con = DriverManager.getConnection(url, user, pwd);
	//			
	// if(testSql!=null && testSql.trim().length() > 0)
	// {
	// try{
	// stmt = con.createStatement();
	//					
	// stmt.execute(testSql);
	//					
	// return true;
	// }catch(SQLException ex)
	// {
	// // ex.printStackTrace();
	// }
	// }
	// return false;
	// } catch (Exception e) {
	// //e.printStackTrace();
	// throw e;
	// }
	// finally{
	// try {
	// if(stmt != null)
	// {
	// stmt.close();
	// }
	// if(con!=null)
	// {
	// con.close();
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	// }
}
