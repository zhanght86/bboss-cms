package com.frameworkset.platform.sysmgrcore.manager;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.frameworkset.event.EventHandle;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.remote.RPCHelper;

import com.frameworkset.platform.config.BaseSystemInit;
import com.frameworkset.platform.config.DestroyException;
import com.frameworkset.platform.config.InitException;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.poolman.util.SQLManager;

/**
 * 系统管理初始化程序
 * <p>Title: SysmanagerInit</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-5-18 8:38:38
 * @author biaoping.yin
 * @version 1.0
 */
public class SysmanagerInit extends BaseSystemInit implements Serializable {
	private static final Logger log = Logger.getLogger(SysmanagerInit.class);
	public void init() throws InitException {
		
		log.debug("初始化数据库链接池.");
		Connection con = null;
		try {
			//初始化数据库链接池
			con = SQLManager.getInstance().requestConnection();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(con != null)
				SQLManager.getInstance().returnConnection(con);
		}

		
		log.debug("初始化数据库链接池完毕.");
		log.debug("加载机构信息开始.");
		try
		{
			OrgCacheManager.init();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		log.debug("加载机构信息结束.");
		
//		Utils a = new Utils();
//		JGroupHelper.getJGroupHelper();
		RPCHelper.getRPCHelper().startServers();
		
		
	}

	public void destroy() throws DestroyException {
		
		try {
			 OrgCacheManager.destroy();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			RPCHelper instance = RPCHelper.getRPCHelperInstance();
			if(instance != null)
				instance.stopRPCServices();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try 
		{
			SQLManager instance = SQLManager.getInstance();
			if(instance != null)
				instance.destroyPools();	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			EventHandle.shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
