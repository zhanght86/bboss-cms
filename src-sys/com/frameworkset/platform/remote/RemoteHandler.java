package com.frameworkset.platform.remote;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.util.JDBCPoolMetaData;
import com.frameworkset.platform.security.AccessControl;

/**
 * <p>Title: RemoteHandler.java</p>
 *
 * <p>Description: 系统通用的远程处理接口，Utils类将远程接口注册到集群通讯组件中</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: 三一集团</p>
 * @Date Jul 11, 2008 5:31:56 PM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class RemoteHandler implements RemoteHandlerInf{
	
	
	
	
	/**
	 * 获取poolman链接池信息
	 * @return Map<dbName,Object[idleconnections,usedconnections,maxusedconnections,poolmetaData]>
	 */
	public Map getDataSourceInfos()
	{
		Map map = new HashMap();
		DBUtil dbUtil = new DBUtil();
		Iterator<String> enum_ = dbUtil.getAllPoolNames().iterator();
		while(enum_.hasNext()){
			Object infos[] = new Object[4];
			String poolname = enum_.next();
			infos[0] = String.valueOf(DBUtil.getNumIdle(poolname));
			infos[1] = String.valueOf(DBUtil.getNumActive(poolname));
			infos[2] = String.valueOf(DBUtil.getMaxNumActive(poolname));
			infos[3] = DBUtil.getPool(poolname).getJDBCPoolMetadata();
			map.put(poolname, infos);
		}
		return map;
	}
	
	/**
	 * 返回本机链接池链接使用状态
	 * @return Map<dbName,Object[idleconnections,usedconnections,,maxusedconnections]>
	 */
	public Map getDataSourceStatus()
	{
		Map map = new HashMap();
		DBUtil dbUtil = new DBUtil();
		Iterator<String> enum_ = dbUtil.getAllPoolNames().iterator();
		while(enum_.hasNext()){
			Object infos[] = new Object[5];
			String poolname =  enum_.next();
			JDBCPoolMetaData metadata = DBUtil.getPool(poolname).getJDBCPoolMetadata();
			infos[0] = String.valueOf(DBUtil.getNumIdle(poolname));
			infos[1] = String.valueOf(DBUtil.getNumActive(poolname));
			infos[2] = String.valueOf(DBUtil.getMaxNumActive(poolname));
			infos[3] = String.valueOf(metadata.getMaximumSize());
			infos[4] = String.valueOf(metadata.getMinimumSize());
			map.put(poolname, infos);
		}
		return map;		
	}
	
	public Object refreshReadorgname(String event,String requestServer)
	{
		
		 UserOrgChangeHandle.getInstance().addRequests(event,requestServer);
		 return new Boolean(true);
	}
	
	/**
	 * 返回服务器当前在线用户数
	 * @return
	 */
	public Object getOnlineUserCount(){
		return new Integer(AccessControl.getInstance().getLoginUsers().size());
	}
	
	
}
