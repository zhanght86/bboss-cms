package com.frameworkset.platform.cms.documentmanager;

import java.sql.SQLException;
import java.util.List;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.documentmanager.bean.DocClass;
import com.frameworkset.util.ListInfo;

/**
 * <p>
 * Title: DocClassManager.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: bbossgroups
 * </p>
 * 
 * @Date 2012-8-13 下午3:42:29
 * @author biaoping.yin
 * @version 1.0.0
 */
public class DocClassManager {
	ConfigSQLExecutor executor;
	public ListInfo queryListInfoDocclass(long offset, int pagesize) throws SQLException
	{
		return executor.queryListInfo(DocClass.class, "queryDocclass", offset, pagesize);
	}
	
	public List<DocClass> queryListDocclass(String currentSiteid) throws SQLException
	{
		return executor.queryList(DocClass.class, "queryDocclass",currentSiteid);
	}

	public void saveClasses(List<DocClass> docClasses,String site_id) throws Exception {
		// TODO Auto-generated method stub
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			executor.delete("deleteClasses", site_id);
			executor.insertBeans("saveClasses", docClasses);
			tm.commit();
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			tm.release();
		}
	}
}
