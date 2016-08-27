package org.frameworkset.esb.datareuse.datasource.service.impl;

import java.util.List;

import org.frameworkset.esb.DropListEntity;
import org.frameworkset.esb.datareuse.codeset.entity.CodesetBean;
import org.frameworkset.esb.datareuse.codeset.entity.CodesetBeanDetail;
import org.frameworkset.esb.datareuse.datasource.dao.DatasourceDao;
import org.frameworkset.esb.datareuse.datasource.entity.DatasourceBean;
import org.frameworkset.esb.datareuse.datasource.entity.OracleTable;
import org.frameworkset.esb.datareuse.datasource.entity.OracleUser;
import org.frameworkset.esb.datareuse.datasource.service.DatasourceService;
import com.frameworkset.util.ListInfo;

public class DatasourceServiceImpl implements DatasourceService {
	private DatasourceDao datasourceDao;
	
	public DatasourceDao getDatasourceDao() {
		return datasourceDao;
	}

	public void setDatasourceDao(DatasourceDao datasourceDao) {
		this.datasourceDao = datasourceDao;
	}
	
	public void deleteDatasource(Object id) throws Exception {
		// TODO Auto-generated method stub
		getDatasourceDao().delete(id);
	}

	
	public List getDatasourceDropList() throws Exception {
		// TODO Auto-generated method stub
		return getDatasourceDao().getDatasourceDropList();
	}

	
	public ListInfo getDatasourceListInfo(String sortKey, boolean desc,
			long offset, int pagesize, DatasourceBean dbean) throws Exception {
		// TODO Auto-generated method stub
		return getDatasourceDao().getDatasourceListInfo(sortKey, desc, offset, pagesize, dbean);
	}

	
	public void insertDatasource(DatasourceBean obj) throws Exception {
		// TODO Auto-generated method stub
		getDatasourceDao().insert(obj);
	}

	public void updateDatasourceStatus(DatasourceBean obj) throws Exception{
		
		getDatasourceDao().updateStatus(obj);
	} 
	
	public void updateDatasource(DatasourceBean obj) throws Exception {
		// TODO Auto-generated method stub
		getDatasourceDao().update(obj);
	}

	
	public DatasourceBean findDatasourceById(String dsName)  throws Exception {
		// TODO Auto-generated method stub
		return (DatasourceBean) getDatasourceDao().findObjectById(dsName);
	}

	public List<DropListEntity> getAllDatasourceNames() throws Exception {
		// TODO Auto-generated method stub
		return datasourceDao.getAllDatasourceNames();
	}
	public List<OracleUser> getAllOracleUser(OracleUser info) throws Exception{
		return datasourceDao.getAllOracleUser(info);
	}
	
	public List<CodesetBean> getAllTableByOwner(OracleTable info) throws Exception{
		return datasourceDao.getAllTableByOwner(info);
	}
	
	public List<CodesetBeanDetail> getAllColumnByOwner(String tablename,
			String dbname, String owner) throws Exception{
		return datasourceDao.getAllColumnByOwner(tablename, dbname, owner);
	}
}
