package org.frameworkset.esb.datareuse.datasource.dao;

import java.util.List;

import org.frameworkset.esb.datareuse.codeset.entity.CodesetBean;
import org.frameworkset.esb.datareuse.codeset.entity.CodesetBeanDetail;
import org.frameworkset.esb.datareuse.datasource.entity.DatasourceBean;
import org.frameworkset.esb.datareuse.datasource.entity.OracleTable;
import org.frameworkset.esb.datareuse.datasource.entity.OracleUser;
import com.frameworkset.util.ListInfo;

/**
 * @author 作 者: hilary
 * @version 创建时间 ：Feb 9, 2011 2:35:35 PM 类说明:
 */
public interface DatasourceDao {

	void updateStatus(Object obj) throws Exception;

	public List getDatasourceDropList() throws Exception;

	public ListInfo getDatasourceListInfo(String sortKey, boolean desc,
			long offset, int pagesize, Object queryObj) throws Exception;

	public List getAllDatasourceNames() throws Exception;

	public void insert(Object obj) throws Exception;

	public void update(Object obj) throws Exception;

	public void delete(Object id) throws Exception;

	public Object findObjectById(String id) throws Exception;

	public List<DatasourceBean> getCustomDatasourceList() throws Exception;

	public List<OracleUser> getAllOracleUser(OracleUser info) throws Exception;
	
	public List<CodesetBean> getAllTableByOwner(OracleTable info) throws Exception;

	public List<CodesetBeanDetail> getAllColumnByOwner(String tablename,
			String dbname, String owner) throws Exception;
}
