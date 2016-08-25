package com.frameworkset.platform.sysmgrcore.purviewmanager.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.sysmgrcore.entity.IpControl;
import com.frameworkset.util.ListInfo;

public class IpControlServiceImpl {
	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;
	 /**
     * 查询IP限制列表
     */
	public ListInfo querylist(long offset, int pagesize, IpControl appcondition ) {
		  ListInfo datas = null;
		  try {
			datas = executor.moreListInfoBean(IpControl.class, "querylist",offset, pagesize, appcondition);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		  
		
		return datas;
	}
	/**
	 * 新增IP控制
	 * @param bean
	 * @param request
	 * @return
	 */
	public boolean addIpControl(IpControl bean){
		boolean flag = false;
		 try {
			executor.insertBean("addIpControl", bean);
			flag = true;
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 根据id查找对象
	 * @param id
	 * @return
	 */
	public IpControl getIpControlById(String id){
		Map idmap = new HashMap();
		idmap.put("id", id);
		IpControl bean;
		try {
			bean = executor.queryObjectBean(IpControl.class, "getIpControlById", idmap);
			return bean;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 更新
	 * @param bean
	 * @return
	 */
	public boolean updateIpControl(IpControl bean){
		boolean flag = false;
		try {
			executor.updateBean("updateIpControl", bean);
			flag =true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
//	/**
//	 * 删除
//	 * @param ids
//	 * @return
//	 */
//	public boolean delteIpControl(String ids){
//		boolean flag = false;
//		StringBuffer sql = new StringBuffer()
//		                  .append("delete TD_SM_IPCONTROL where id in(");
//		DBUtil db = new DBUtil();
//		if(ids!=null && !ids.equals("")){
//			String[] idList = ids.split(",");
//			for(int i=0;i<idList.length;i++){
//				sql.append("'").append(idList[i]).append("',");
//			}
//			try {
//				db.executeDelete(sql.substring(0, sql.length()-1)+")");
//				flag = true;
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		
//		return flag;
//	}
	
	
	/**
	 * 批量删除
	 * @param beans
	 * @return
	 */
	public boolean delteIpControl(List<IpControl> beans) {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			executor.deleteBeans("delteIpControl", beans);
			tm.commit();
		} catch (Throwable e) {
			
			return false;
		}
		finally
		{
			tm.release();
		}

		return true;
	}
	
}
