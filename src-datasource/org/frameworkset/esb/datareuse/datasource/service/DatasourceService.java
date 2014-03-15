package org.frameworkset.esb.datareuse.datasource.service;

import java.util.List;

import org.frameworkset.esb.DropListEntity;
import org.frameworkset.esb.datareuse.codeset.entity.CodesetBean;
import org.frameworkset.esb.datareuse.codeset.entity.CodesetBeanDetail;
import org.frameworkset.esb.datareuse.datasource.entity.DatasourceBean;
import org.frameworkset.esb.datareuse.datasource.entity.OracleTable;
import org.frameworkset.esb.datareuse.datasource.entity.OracleUser;
import com.frameworkset.util.ListInfo;

public interface DatasourceService {
	/***************************** datasource manage ********************************/

	public ListInfo getDatasourceListInfo(String sortKey, boolean desc,
			long offset, int pagesize, DatasourceBean dbean) throws Exception;

	public List getDatasourceDropList() throws Exception;

	public void deleteDatasource(Object id) throws Exception;

	public void insertDatasource(DatasourceBean obj) throws Exception;

	public void updateDatasourceStatus(DatasourceBean obj) throws Exception;

	public void updateDatasource(DatasourceBean obj) throws Exception;

	public DatasourceBean findDatasourceById(String dsName) throws Exception;

	public List<DropListEntity> getAllDatasourceNames() throws Exception;

	public List<OracleUser> getAllOracleUser(OracleUser info) throws Exception;

	public List<CodesetBean> getAllTableByOwner(OracleTable info)
			throws Exception;

	public List<CodesetBeanDetail> getAllColumnByOwner(String tablename,
			String dbname, String owner) throws Exception;
}
