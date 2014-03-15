package org.frameworkset.esb.datareuse.datasource.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.frameworkset.esb.DropListEntity;
import org.frameworkset.esb.datareuse.codeset.entity.CodesetBean;
import org.frameworkset.esb.datareuse.codeset.entity.CodesetBeanDetail;
import org.frameworkset.esb.datareuse.datasource.dao.DatasourceDao;
import org.frameworkset.esb.datareuse.datasource.entity.DatasourceBean;
import org.frameworkset.esb.datareuse.datasource.entity.OracleTable;
import org.frameworkset.esb.datareuse.datasource.entity.OracleTableColumn;
import org.frameworkset.esb.datareuse.datasource.entity.OracleUser;
import org.frameworkset.esb.datareuse.util.Constants;
import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.util.ListInfo;

/**
 * @author 作 者: qian.wang
 * @version 创建时间 ：3.30, 2011 3:30:41 PM 类说明:
 */
public class DatasourceDaoImpl implements DatasourceDao {
	private static String dbName = Constants.DATAREUSE_DBNAME;

	private ConfigSQLExecutor executor;

	public void setExecutor(ConfigSQLExecutor executor) {
		this.executor = executor;
	}

	public void insert(Object obj) throws Exception {
		// TODO Auto-generated method stub
		// String sql =
		// "insert into TD_ESB_DATASOURCE(CONNECTION_TYPE ,DB_TYPE ,DRIVER ,DS_NAME ,INITIAL_CONNECTIONS ,JDBC_URL ,JNDI_NAME ,MAXIMUM_SIZE ,MINIMUM_SIZE ,PASSWORD ,READ_ONLY ,REMARK ,SHOW_SQL ,USE_POOL ,USERNAME ,VALIDATION_QUERY,EXT_JNDI_NAME,CREATOR,CREATE_TIME,MODIFIER,MODIFY_TIME) values(#[connection_type] ,#[db_type] ,#[driver] ,#[ds_name] ,#[initial_connections] ,#[jdbc_url] ,#[jndi_name] ,#[maximum_size] ,#[minimum_size] ,#[password] ,#[read_only] ,#[remark] ,#[show_sql] ,#[use_pool] ,#[username] ,#[validation_query],#[ext_jndi_name],#[creator],#[create_time],#[modifier],#[modify_time])";
		// SQLExecutor.insertBean(dbName, sql, obj);
		executor.insertBean(dbName, "insert", obj);
	}

	public void update(Object obj) throws Exception {
		// TODO Auto-generated method stub
		// String sql =
		// "update TD_ESB_DATASOURCE set CONNECTION_TYPE=#[connection_type] ,DB_TYPE=#[db_type] ,DRIVER=#[driver]  ,INITIAL_CONNECTIONS=#[initial_connections] ,JDBC_URL=#[jdbc_url] ,JNDI_NAME=#[jndi_name] ,MAXIMUM_SIZE=#[maximum_size] ,MINIMUM_SIZE=#[minimum_size] ,PASSWORD=#[password] ,READ_ONLY=#[read_only] ,REMARK=#[remark] ,SHOW_SQL=#[show_sql] ,USE_POOL=#[use_pool] ,USERNAME=#[username] ,VALIDATION_QUERY=#[validation_query] ,EXT_JNDI_NAME=#[ext_jndi_name],MODIFIER=#[modifier],MODIFY_TIME=#[modify_time]where DS_NAME=#[ds_name] ";
		// SQLExecutor.updateBean(dbName, sql, obj);
		executor.updateBean(dbName, "update", obj);

	}

	public void updateStatus(Object obj) throws SQLException {
		// String sql =
		// "update TD_ESB_DATASOURCE set STATUS=#[status],MODIFIER=#[modifier],MODIFY_TIME=#[modify_time] where DS_NAME=#[ds_name]";
		// SQLExecutor.updateBean(dbName, sql, obj);
		executor.updateBean(dbName, "updateStatus", obj);

	}

	public void delete(Object id) throws Exception {
		String DS_NAME = (String) id;
		// TODO Auto-generated method stub
		// String sql = "delete from TD_ESB_DATASOURCE where DS_NAME = ?";
		executor.deleteByKeysWithDBName(dbName, "delete", DS_NAME);
		// SQLExecutor.deleteByKeysWithDBName(dbName,sql, DS_NAME);
	}

	public DatasourceBean findObjectById(String id) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select * from TD_ESB_DATASOURCE where DS_NAME = ?";
		DatasourceBean obj = SQLExecutor.queryObjectWithDBName(
				DatasourceBean.class, dbName, sql, id);
		// DatasourceBean obj =
		// executor.queryObjectWithDBName(DatasourceBean.class, dbName,
		// "getObjectById", id);
		return obj;
	}

	public List<DatasourceBean> getDatasourceDropList() throws Exception {
		List<DatasourceBean> list = new ArrayList<DatasourceBean>();

		// 获取代码集列表
		// StringBuilder sqlBuilder = new StringBuilder();
		// sqlBuilder.append("select DS_NAME, REMARK from TD_ESB_DATASOURCE where 1=1");
		// String sql = sqlBuilder.toString();
		// list = SQLExecutor.queryListWithDBName(DatasourceBean.class, dbName,
		// sql);
		list = executor.queryListWithDBName(DatasourceBean.class, dbName,
				"getDatasourceDropList");

		return list;
	}

	public ListInfo getDatasourceListInfo(String sortKey, boolean desc,
			long offset, int pagesize, Object queryObj) throws Exception {

		// 获取代码集列表
		// StringBuilder sqlBuilder = new StringBuilder();
		// sqlBuilder.append("select * from TD_ESB_DATASOURCE where 1=1");
		//		
		//		
		// sqlBuilder.append("#if($jdbc_url && !$jdbc_url.equals(\"\")) ");
		// sqlBuilder.append("	 and upper(jdbc_url) like #[jdbc_url]");
		// sqlBuilder.append("#end");
		//		
		// sqlBuilder.append("#if($ds_name && !$ds_name.equals(\"\")) ");
		// sqlBuilder.append("	 and upper(ds_name) like #[ds_name]");
		// sqlBuilder.append("#end");
		// sqlBuilder.append(" order by ds_name");
		//
		// String sql = sqlBuilder.toString();
		// ListInfo listinfo =
		// SQLExecutor.queryListInfoBeanWithDBName(DatasourceBean.class,dbName,sql,offset,
		// pagesize,queryObj);
		ListInfo listinfo = executor.queryListInfoBeanWithDBName(
				DatasourceBean.class, dbName, "getDatasourceListInfo", offset,
				pagesize, queryObj);
		return listinfo;
	}

	public List<DatasourceBean> getCustomDatasourceList() throws Exception {
		DatasourceBean bean = new DatasourceBean();
		bean.setConnection_type("custom_jdbc");
		bean.setStatus("1");
		return executor.queryListBeanWithDBName(DatasourceBean.class, dbName,
				"getCustomDatasourceList", bean);
	}

	public List<DropListEntity> getAllDatasourceNames() throws Exception {
		// TODO Auto-generated method stub
		// String sql = "select DS_NAME from TD_ESB_DATASOURCE";
		// List<DatasourceBean> obj =
		// SQLExecutor.queryListWithDBName(DatasourceBean.class, dbName, sql);
		List<DatasourceBean> obj = executor.queryListWithDBName(
				DatasourceBean.class, dbName, "getAllDatasourceNames");
		List<DropListEntity> dropList = new ArrayList<DropListEntity>();
		for (DatasourceBean str : obj) {
			DropListEntity dlm = new DropListEntity();
			dlm.setValue(str.getDs_name());
			dlm.setText(str.getDs_name());
			dropList.add(dlm);
		}
		return dropList;
	}

	public List<OracleUser> getAllOracleUser(OracleUser info) throws Exception {
		OracleUser bean = new OracleUser();
		return executor.queryListBeanWithDBName(OracleUser.class, info
				.getDbname(), "getAllOracleUser", bean);

	}

	public List<CodesetBean> getAllTableByOwner(OracleTable info)
			throws Exception {
		List<OracleTable> list = executor
				.queryListBeanWithDBName(OracleTable.class, info.getDbname(),
						"getAllTableByOwner", info);
		List<CodesetBean> codes = new ArrayList<CodesetBean>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				OracleTable a = list.get(i);
				CodesetBean b = new CodesetBean();
				b.setParam_table_name(a.getTable_name());
				codes.add(b);
			}
		}
		return codes;
	}

	public List<CodesetBeanDetail> getAllColumnByOwner(String tablename,
			String dbname, String owner) throws Exception {
		List<CodesetBeanDetail> list = new ArrayList<CodesetBeanDetail>();
		OracleTableColumn otc = new OracleTableColumn();
		otc.setDbname(dbname);
		otc.setOwner(owner);
		otc.setTable_name(tablename);
		List<OracleTableColumn> otclist = executor.queryListBeanWithDBName(
				OracleTableColumn.class, dbname, "getAllColumnByTable", otc);
		if (otclist != null && otclist.size() > 0) {
			for (OracleTableColumn cbean : otclist) {
				CodesetBeanDetail bbean = new CodesetBeanDetail();
				bbean.setColumn_code(cbean.getColumn_name());
				bbean.setColumn_name(cbean.getColumn_name());
				bbean.setColumn_type(cbean.getData_type());
				bbean.setLength(cbean.getData_length() + "");
				bbean.setPrecision(cbean.getData_scale() + "");
				list.add(bbean);
			}
		}
		return list;
	}
}
